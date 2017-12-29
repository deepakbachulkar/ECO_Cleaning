package com.ipws.eco.ui.fragment;

import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ipws.eco.R;
import com.ipws.eco.custom.view.CalendarListener;
import com.ipws.eco.custom.view.CustomCalendarView;
import com.ipws.eco.network.NetworkDAO;
import com.ipws.eco.ui.activity.BaseActivity;
import com.ipws.eco.ui.controls.AppDialogs;
import com.ipws.eco.utils.AppDates;
import com.ipws.eco.utils.AppPreference;
import com.ipws.eco.utils.Logs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimeSheetFragment extends BaseFragment implements View.OnClickListener
{
    public static String TAG= "time_sheet";
    private OnFragmentInteractionListener mListener;
    private CustomCalendarView calendarView;
    private RelativeLayout layoutDatePicker, layoutTimePicker;
    private TextView textDate, txtTime, txtComment, txtBtSubmit;
    private Spinner spinnerAccount, spinnerLocation;
    List<String>  listLocation;
    List<Integer> listLocationId;
    private int fromTimeHr, fromTimeMin;
    private String date= "2017-06-28";
    private  int locationId=0;
    private View mView;

    public TimeSheetFragment() {
    }

    public static TimeSheetFragment newInstance(Bundle bundle) {
        TimeSheetFragment fragment = new TimeSheetFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) { }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_timesheet, container, false);
        mView = view;
        init(view);
        return addToBaseFragment(view);
    }

    private void init(View view)
    {
        layoutTimePicker = (RelativeLayout) view.findViewById(R.id.layoutTimePicker);
        layoutTimePicker.setVisibility(View.GONE);
        textDate = (TextView) view.findViewById(R.id.txtDate);
        txtComment = (TextView) view.findViewById(R.id.txtRemarkNote);
        txtBtSubmit =(TextView) view.findViewById(R.id.txtBtSubmit);
        layoutDatePicker = (RelativeLayout) view.findViewById(R.id.layoutDatePicker);
        layoutDatePicker.setVisibility(View.GONE);
        calendarView = (CustomCalendarView) view.findViewById(R.id.calendarView);
        spinnerAccount = (Spinner) view.findViewById(R.id.spinnerAccount);
        spinnerLocation = (Spinner) view.findViewById(R.id.spinnerLocation);
        textDate.setOnClickListener(this);
        view.findViewById(R.id.txtLeaveTimeFrom).setOnClickListener(this);
        view.findViewById(R.id.txtLeaveTimeTo).setOnClickListener(this);
        view.findViewById(R.id.btnSave).setOnClickListener(this);
        view.findViewById(R.id.layoutTimePicker).setOnClickListener(this);
        view.findViewById(R.id.layoutDatePicker).setOnClickListener(this);
        txtBtSubmit.setOnClickListener(this);
        //Initialize calendar with date
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
        //Show monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);
        //call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar);
        //Handling custom calendar events
        calendarView.setCalendarListener(calendarListener);
        textDate.setText(AppDates.with().currentDateTime(AppDates.APP_SHOW_DATE_FORMAT));
    }

    private void initListener(){ }

    @Override
    public void onStart() {
        super.onStart();
        customize();
        requestAccount();
    }

    private void customize(){
        ((BaseActivity)getActivity()).setTitleIcon(getResources().getString(R.string.fa_icon_calender_2), getResources().getString(R.string.time_sheet));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    CalendarListener calendarListener = new CalendarListener() {
        @Override
        public void onDateSelected(Date date) {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            textDate.setText(df.format(date));
            layoutDatePicker.setVisibility(View.GONE);
            requestGetTimeSheet();
        }

        @Override
        public void onMonthChanged(Date date) {
//                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
        }
    };

    public void setTime(int hour, int min)
    {

        if(txtTime.getId() == R.id.txtLeaveTimeTo)
        {
            if(fromTimeHr>=hour) {
                Toast.makeText(getActivity(), "Please select after from time.", Toast.LENGTH_SHORT).show();
                return;
            }
        }else{
            fromTimeHr=hour;
            fromTimeMin = min;
        }
        String hr="", mins="";
        Logs.d("Time: "+hour+":"+min);
        if(hour<10)
            hr= "0"+hour;
        else
            hr=hour+"";
        if(min<10)
            mins= "0"+min;
        else
            mins = min+"";
        Logs.d("Time: "+hr+":"+mins);
        txtTime.setText(hr+":"+mins);

//            txtTime.setText(hr+":"+mins+":00");
//        else
//            endTime = hr+":"+mins+":00";
    }

//    public void showTime(int hour, int min) {
//        if (hour == 0) {
//            hour += 12;
//            format = "AM";
//        } else if (hour == 12) {
//            format = "PM";
//        } else if (hour > 12) {
//            hour -= 12;
//            format = "PM";
//        } else {
//            format = "AM";
//        }
//
//        txtTime.setText(new StringBuilder().append(hour).append(" : ").append(min)
//                .append(" ").append(format));
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txtDate:
                layoutDatePicker.setVisibility(View.VISIBLE);
                break;
            case R.id.txtLeaveTimeFrom:
                txtTime = (TextView) view ;
//                layoutTimePicker.setVisibility(View.VISIBLE);
                showTimePickerDialog("Select To Time");
                break;
            case R.id.txtLeaveTimeTo:
                txtTime = (TextView) view ;
                showTimePickerDialog("Select From Time");
//                layoutTimePicker.setVisibility(View.VISIBLE);
                break;
            case R.id.btnSave:
                layoutTimePicker.setVisibility(View.GONE);
//                setTime();
                break;
            case R.id.layoutTimePicker:
                layoutTimePicker.setVisibility(View.GONE);
                break;
            case R.id.layoutDatePicker:
                layoutDatePicker.setVisibility(View.GONE);
                break;
            case R.id.txtBtSubmit:
                String from= ((TextView)mView.findViewById(R.id.txtLeaveTimeFrom)).getText().toString().trim();
                String to= ((TextView)mView.findViewById(R.id.txtLeaveTimeTo)).getText().toString().trim();
                if(from.equals("")
                        || to.equals(""))
                    Toast.makeText(getActivity(), "Please select Time.", Toast.LENGTH_SHORT).show();
                else
                    requestSetTimeSheet();
                break;
        }
    }

    private void requestAccount()
    {
        showProgressBar();
        String parameter = "TxnType=PACCOUNTLIST&LOGINNAME1="+AppPreference.getInstance(getActivity()).getLoginName()+"&Msg=USERID:"+AppPreference.getInstance(getActivity()).getUserId()+"|CRITERIA:NODELETE|TDTSM:2017-06-20 12*30*38.287";
        Logs.d("Network Url: "+parameter);
        parameter = parameter.replace(" ", "%20");
        NetworkDAO.getInstance(getActivity()).login(getActivity(), parameter, success, error);
    }

    Response.Listener success= new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            hideProgressBar();
            Logs.d("Network response :"+ response);
            parse(response);
        }
    };

    Response.ErrorListener error = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            hideProgressBar();
            Logs.d("Network Error: " + error.getMessage());
            error.printStackTrace();
            Toast.makeText(getActivity(), "Unable to connect server.", Toast.LENGTH_SHORT).show();
        }
    };

    private void parse(String data)
    {
        try
        {
            JSONObject object = new JSONObject(data);
            if(object.has("status") && object.getString("status").equals("00"))
            {
                listId = new ArrayList<>();
                listName = new ArrayList<>();
                listId.add(0);
                listName.add("Seelct");
                JSONArray array = object.getJSONArray("Msg");
                for (int i=0; i<array.length(); i++){
                    JSONObject j= array.getJSONObject(i);
                    listId.add(j.getInt("id"));
                    listName.add(j.getString("org_name"));
                    spinnerAccount();
                }
            }else {
                Toast.makeText(getActivity(), object.getJSONObject("errormsg").getString("error") , Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), getResources().getString(R.string.error_server_something_worng), Toast.LENGTH_SHORT).show();
        }
    }

    private void spinnerAccount()
    {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,listName);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAccount.setAdapter(dataAdapter);
        spinnerAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0)
                    requestLocation(listId.get(i));
                else{
                    listLocationId = new ArrayList<>();
                    listLocation = new ArrayList<>();
                    listLocationId.add(0);
                    listLocation.add("Select");
                    spinnerLocation();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void showTimePickerDialog(String dialogTitle)
    {
        if(getActivity()!=null)
        {

            TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1)
                {
                    setTime(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                }
            };
            AppDialogs.with(getActivity()).showTimePicker(dialogTitle, listener);
        }
    }

    private void requestLocation(int custId)
    {
        showProgressBar();
        String parameter = "TxnType=PLOCATIONLIST&LOGINNAME1="+AppPreference.getInstance(getActivity()).getLoginName()+"&Msg=USERID:"+AppPreference.getInstance(getActivity()).getUserId()+"|CUSTNAME:NONE|CUSTID:"+custId+"|CRITERIA:ID|TDTSM:2017-06-20 12*30*38.287";
        Logs.d("Network Url: "+parameter);
        parameter = parameter.replace(" ", "%20");
        NetworkDAO.getInstance(getActivity()).login(getActivity(), parameter, success1, error2);
    }

    Response.Listener success1= new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            hideProgressBar();
            Logs.d("Network response :"+ response);
            parseLocation(response);
        }
    };

    Response.ErrorListener error2= new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            hideProgressBar();
            Logs.d("Network Error: " + error.getMessage());
            error.printStackTrace();
            Toast.makeText(getActivity(), "Unable to connect server.", Toast.LENGTH_SHORT).show();
        }
    };

    List<String>  listName;
    List<Integer> listId;
    private void parseLocation(String data)
    {
        try
        {
            JSONObject object = new JSONObject(data);
            if(object.has("status") && object.getString("status").equals("00"))
            {
                listLocationId = new ArrayList<>();
                listLocation = new ArrayList<>();
                JSONArray array = object.getJSONArray("Msg");
                for (int i=0; i<array.length(); i++){
                    JSONObject j= array.getJSONObject(i);
                    listLocationId.add(j.getInt("id"));
                    listLocation.add(j.getString("loc_name"));
                    spinnerLocation();
                }
            }else {
                Toast.makeText(getActivity(), object.getJSONObject("errormsg").getString("error") , Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), getResources().getString(R.string.error_server_something_worng), Toast.LENGTH_SHORT).show();
        }
    }

    private void spinnerLocation()
    {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,listLocation);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(dataAdapter);
        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                locationId = listLocationId.get(i);
                requestGetTimeSheet();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    private void requestGetTimeSheet()
    {
        date = AppDates.with().stringToString(textDate.getText().toString().trim(),
                AppDates.APP_SHOW_DATE_FORMAT, AppDates.APP_SEND_DATE_FORMAT);
        if(locationId<=0 || date.equals("")){
            Toast.makeText(getActivity(), "Please select location.", Toast.LENGTH_SHORT).show();
            return;
        }
//        http://54.252.182.129/cleaningMobileApidev/API/PrepaidAPI.aspx?TxnType=PGETTIMESHEET&LOGINNAME1=9595036832&Msg=USERID:1002|FORDATE:2017-06-28|LOCATIONID:1021|TDTSM:2017-06-20 12*30*38.287
        showProgressBar();
        String parameter = "TxnType=PGETTIMESHEET&LOGINNAME1="+AppPreference.getInstance(getActivity()).getLoginName()+"&Msg=USERID:"+AppPreference.getInstance(getActivity()).getUserId()+"|FORDATE:"+date+"|LOCATIONID:"+locationId+"|TDTSM:2017-06-20 12*30*38.287";
        Logs.d("Network Url: "+parameter);
        parameter = parameter.replace(" ", "%20");
        NetworkDAO.getInstance(getActivity()).login(getActivity(), parameter, successGT, errorGT);
    }

    Response.Listener successGT= new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            hideProgressBar();
            Logs.d("Network response :"+ response);
            parseGetTimeSheet
                    (response);
        }
    };

    Response.ErrorListener errorGT= new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            hideProgressBar();
            Logs.d("Network Error: " + error.getMessage());
            error.printStackTrace();
            Toast.makeText(getActivity(), "Unable to connect server.", Toast.LENGTH_SHORT).show();
        }
    };
    private void parseGetTimeSheet(String data)
    {
        try
        {
            JSONObject object = new JSONObject(data);
            if(object.has("status") && object.getString("status").equals("00"))
            {
                Toast.makeText(getActivity(), "Submitted Timesheet." , Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(), object.getJSONObject("errormsg").getString("error") , Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), getResources().getString(R.string.error_server_something_worng), Toast.LENGTH_SHORT).show();
        }
    }

    private void requestSetTimeSheet()
    {
        date = AppDates.with().stringToString(textDate.getText().toString().trim(),
                AppDates.APP_SHOW_DATE_FORMAT, AppDates.APP_SEND_DATE_FORMAT);
        if(locationId<=0 || date.equals("")){
            Toast.makeText(getActivity(), "Please select location.", Toast.LENGTH_SHORT).show();
            return;
        }
        showProgressBar();
        String strFrom =date+" "+((TextView)mView.findViewById(R.id.txtLeaveTimeFrom)).getText().toString().trim().replace(":","*")+"*00";
        String strTo =date+" "+((TextView)mView.findViewById(R.id.txtLeaveTimeTo)).getText().toString().trim().replace(":","*")+"*00";
        String parameter = "TxnType=PSETTIMESHEET&LOGINNAME1="
                +AppPreference.getInstance(getActivity()).getLoginName()
                +"&Msg=USERID:"+AppPreference.getInstance(getActivity()).getUserId()
                +"|FORDATE:"
                +date
                +"|FROMDATETIME:"+strFrom+"|TODATETIME:"+strTo+"|COMMENT:"+txtComment.getText().toString().trim()
                +"|LOCATIONID:"+locationId;

//        parameter = "TxnType=PSETTIMESHEET&LOGINNAME1="+AppPreference.getInstance(getActivity()).getLoginName()+"&Msg=USERID:"+AppPreference.getInstance(getActivity()).getUserId()+"|FORDATE:"+date+
//                "|FROMDATETIME:"+strFrom+"|TODATETIME:"+strTo+"|LOCATIONID:"+locationId
//                +"|COMMENT:"+txtComment.getText().toString().trim()+"|TDTSM:2017-06-28 12*30*38.287";
        Logs.d("Network Url parameter: "+parameter);
        parameter = parameter.replace(" ", "%20");
        NetworkDAO.getInstance(getActivity()).login(getActivity(), parameter, successST, errorST);
    }

    Response.Listener successST= new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            hideProgressBar();
            Logs.d("Network response :"+ response);
            parseSetTimeSheet(response);
        }
    };

    Response.ErrorListener errorST= new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            hideProgressBar();
            Logs.d("Network Error: " + error.getMessage());
            error.printStackTrace();
            Toast.makeText(getActivity(), "Unable to connect server.", Toast.LENGTH_SHORT).show();
        }
    };

    private void parseSetTimeSheet(String data)
    {
        try
        {
            JSONObject object = new JSONObject(data);
            if(object.has("status") && object.getString("status").equals("00"))
            {
                ((TextView)mView.findViewById(R.id.txtLeaveTimeFrom)).setText("");
                ((TextView)mView.findViewById(R.id.txtLeaveTimeTo)).setText("");
            }else {
//                if(object.getJSONObject("errormsg").getString("error").equals("No reco"))
                Toast.makeText(getActivity(), object.getJSONObject("errormsg").getString("error") , Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), getResources().getString(R.string.error_server_something_worng), Toast.LENGTH_SHORT).show();
        }
    }
}