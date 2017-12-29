package com.ipws.eco.ui.fragment;

import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
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

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ApplyForLeaveFragment extends BaseFragment implements View.OnClickListener
{
    public static String TAG= "leave_to_apply";
    private OnFragmentInteractionListener mListener;
    private CustomCalendarView calendarView;
    private RelativeLayout layoutDatePicker, layoutTimePicker;
    private TextView txtFromDate, txtToDate, txtTime, txtStartTime, txtEndTime, txtReason;
    private RadioButton fullDay, halfDay;
    private int fromTimeHr, fromTimeMin;
    private LinearLayout linTime;
    private final String  DATE_FORMAT = "";
    public ApplyForLeaveFragment() {
    }

    public static ApplyForLeaveFragment newInstance(Bundle bundle) {
        ApplyForLeaveFragment fragment = new ApplyForLeaveFragment();
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
        View view= inflater.inflate(R.layout.fragment_apply_for_leave, container, false);
        init(view);
        initListener(view);
        return addToBaseFragment(view);
    }

    private void init(View view)
    {
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        layoutTimePicker = (RelativeLayout) view.findViewById(R.id.layoutTimePicker);
        layoutTimePicker.setVisibility(View.GONE);
        txtFromDate = (TextView) view.findViewById(R.id.txtFromDate);
        txtToDate = (TextView) view.findViewById(R.id.txtToDate);
        layoutDatePicker = (RelativeLayout) view.findViewById(R.id.layoutDatePicker);
        layoutDatePicker.setVisibility(View.GONE);
        calendarView = (CustomCalendarView) view.findViewById(R.id.calendarView);
        linTime = (LinearLayout) view.findViewById(R.id.linTime) ;

        fullDay = (RadioButton) view.findViewById(R.id.radioFullDay);
        halfDay = (RadioButton) view.findViewById(R.id.radioHalfDay);

        txtStartTime = (TextView) view.findViewById(R.id.txtLeaveTimeFrom);
        txtEndTime = (TextView) view.findViewById(R.id.txtLeaveTimeTo);
        txtReason = (TextView) view.findViewById(R.id.txtReason);
        txtFromDate.setOnClickListener(this);
        txtToDate.setOnClickListener(this);
        view.findViewById(R.id.txtLeaveTimeFrom).setOnClickListener(this);
        view.findViewById(R.id.txtLeaveTimeTo).setOnClickListener(this);
        view.findViewById(R.id.btnSave).setOnClickListener(this);
        view.findViewById(R.id.layoutTimePicker).setOnClickListener(this);
        view.findViewById(R.id.layoutDatePicker).setOnClickListener(this);
        view.findViewById(R.id.txtBtApplyForLeave).setOnClickListener(this);
        //Initialize calendar with date
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
        //Show monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);
        //call refreshCalendar to update calendar the view;
        txtFromDate.setText(AppDates.with().currentDateTime(AppDates.APP_SHOW_DATE_FORMAT));
        txtToDate.setText(AppDates.with().currentDateTime(AppDates.APP_SHOW_DATE_FORMAT));
    }

    private void initListener(View view)
    {
        fullDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    linTime.setVisibility(View.VISIBLE);
                }else{
                    linTime.setVisibility(View.GONE);
                }
            }
        });

        halfDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    linTime.setVisibility(View.VISIBLE);
                }else{
                    linTime.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        customize();
    }

    private void customize(){
        ((BaseActivity)getActivity()).setTitleIcon(getResources().getString(R.string.fa_icon_leave), getResources().getString(R.string.action_apply_leave));
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

    CalendarListener fromCalendarListener = new CalendarListener() {
        @Override
        public void onDateSelected(Date date) {
            SimpleDateFormat df = new SimpleDateFormat(AppDates.APP_SHOW_DATE_FORMAT);http://54.252.182.129/cleaningMobileApidev/API/PrepaidAPI.aspx?TxnType=PAPPLYLEAVE&LOGINNAME1=9595036832&Msg=USERID:1002|FORDATE:2017-11-19|UPTODATE:2017-11-19|LEAVETYPE:fullDay|STARTTIME:|ENDTIME:|MANAGERID:1002|COMMENT:test|TDTSM:2017-06-20%2012*30*38.287
            txtFromDate.setText(df.format(date));
            layoutDatePicker.setVisibility(View.GONE);

            long fromDate = AppDates.with().stringToMillSec(txtFromDate.getText().toString().trim(), AppDates.APP_SEND_DATE_FORMAT);
            long toDate = AppDates.with().stringToMillSec(txtToDate.getText().toString().trim(), AppDates.APP_SEND_DATE_FORMAT);

            if(fromDate!=toDate){
                halfDay.setEnabled(false);
//                fullDay.setSelected(true);
                fullDay.setChecked(true);
            }else
                halfDay.setEnabled(true);
        }

        @Override
        public void onMonthChanged(Date date) {
//                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
        }
    };


    CalendarListener toCalendarListener = new CalendarListener() {
        @Override
        public void onDateSelected(Date date) {
            SimpleDateFormat df = new SimpleDateFormat(AppDates.APP_SHOW_DATE_FORMAT);
            txtToDate.setText(df.format(date));
            layoutDatePicker.setVisibility(View.GONE);
            long fromDate = AppDates.with().stringToMillSec(txtFromDate.getText().toString().trim(), AppDates.APP_SEND_DATE_FORMAT);
            long toDate = AppDates.with().stringToMillSec(txtToDate.getText().toString().trim(), AppDates.APP_SEND_DATE_FORMAT);

            if(fromDate!=toDate){
                halfDay.setEnabled(false);
//                fullDay.setSelected(true);
                fullDay.setChecked(true);
            }else
                halfDay.setEnabled(true);
        }
        @Override
        public void onMonthChanged(Date date) {
//                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
        }
    };

    private TimePicker timePicker;
    private String format = "", startTime, endTime;

    public void setTime(int hour, int min)
    {
        if(txtTime.getId() == R.id.txtLeaveTimeTo)
        {
            if(fromTimeHr>=hour) {
                Toast.makeText(getActivity(), "Please select after from time", Toast.LENGTH_SHORT).show();
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

        txtTime.setText(hr+":"+min);
        if(txtTime.getId() == R.id.txtLeaveTimeFrom)
            startTime = hr+":"+mins;
        else
            endTime = hr+":"+mins;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txtFromDate:
                //Handling custom calendar events
                calendarView.setCalendarListener(fromCalendarListener);
                layoutDatePicker.setVisibility(View.VISIBLE);
                break;
            case R.id.txtToDate:
                //Handling custom calendar events
                calendarView.setCalendarListener(toCalendarListener);
                layoutDatePicker.setVisibility(View.VISIBLE);
                break;
            case R.id.txtLeaveTimeFrom:
                txtTime = (TextView) view ;
//                layoutTimePicker.setVisibility(View.VISIBLE);
                showTimePickerDialog("Select From Time");
                break;
            case R.id.txtLeaveTimeTo:
                txtTime = (TextView) view;
                showTimePickerDialog("Select To Time");
                break;
            case R.id.btnSave:
                layoutTimePicker.setVisibility(View.GONE);
                break;
            case R.id.layoutTimePicker:
                layoutTimePicker.setVisibility(View.GONE);
                break;
            case R.id.layoutDatePicker:
                layoutDatePicker.setVisibility(View.GONE);
                break;
            case R.id.txtBtApplyForLeave:
                if(isValidation())
                    requestAppayForLeave();
                break;
        }
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

    private boolean isValidation()
    {
        boolean isValid =true;
        long fromDate = AppDates.with().stringToMillSec(txtFromDate.getText().toString().trim(), AppDates.APP_SHOW_DATE_FORMAT);
        long toDate = AppDates.with().stringToMillSec(txtToDate.getText().toString().trim(), AppDates.APP_SHOW_DATE_FORMAT);
        if(fromDate>toDate){
                Toast.makeText(getActivity(), "Please select To date after From date.", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if(txtFromDate.getText().toString().toString().equals(""))
        {
            Toast.makeText(getActivity(), "Please select  date.", Toast.LENGTH_SHORT).show();
            isValid = false;
        }else if(txtReason.getText().toString().toString().equals("")){
            Toast.makeText(getActivity(), "Please enter reason.", Toast.LENGTH_SHORT).show();
            isValid = false;
        }else  if(!fullDay.isChecked())
        {
            if(txtStartTime.getText().toString().toString().equals(""))
            {
                Toast.makeText(getActivity(), "Please select from time.", Toast.LENGTH_SHORT).show();
                isValid = false;
            }else if(txtEndTime.getText().toString().toString().equals("")){
                Toast.makeText(getActivity(), "Please select to date.", Toast.LENGTH_SHORT).show();
                isValid = false;
            }
        }
        return isValid;
    }

    private void requestAppayForLeave()
    {
//        fullDay/halfDay
        String leaveDay="halfDay";
        startTime =txtStartTime.getText().toString().toString().trim().replace(":","-");
        endTime =txtEndTime.getText().toString().toString().trim().replace(":","-");
        if(fullDay.isChecked()) {
            leaveDay = "fullDay";
            startTime ="00*00*00";
            endTime ="00*00*00";
        }
        else
            leaveDay="halfDay";
        showProgressBar();
        String date= AppDates.with().currentDateTime("yyyy-MM-dd");
//        date="2017-10-20";
        String parameter ="TxnType=PAPPLYLEAVE&LOGINNAME1="+AppPreference.getInstance(getActivity()).getLoginName()+"&Msg=USERID:"+AppPreference.getInstance(getActivity()).getUserId()+"|FORDATE:"
                +AppDates.with().stringToString(txtFromDate.getText().toString().trim(),
                AppDates.APP_SHOW_DATE_FORMAT, "yyyy-MM-dd")
                +"|UPTODATE:"
                +AppDates.with().stringToString(txtFromDate.getText().toString().trim(),
                        AppDates.APP_SHOW_DATE_FORMAT, "yyyy-MM-dd")+"|LEAVETYPE:"+leaveDay+"|STARTTIME:"+startTime+"|ENDTIME:"+endTime+"|MANAGERID:"
                +AppPreference.getInstance(getActivity()).getUserId()
                +"|COMMENT:"+txtReason.getText().toString().trim()+"|TDTSM:2017-06-20 12*30*38.287";
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
//            if(data.contains("\"status\":\"01\"")) {
//                data = data.replace("\"{", "{");
//                data = data.replace("}\"", "}");
//            }
//            {"status":"01","errormsg":"{"error":"Unable to apply leave for given data","TDTSM":"2017+06+20 12*30*38.287"}"}
            JSONObject object = new JSONObject(data);
            if(object.has("status") && object.getString("status").equals("00"))
            {
                if(object.getJSONArray("Msg").getJSONObject(0).getString("status").equals("00"))
                    Toast.makeText(getActivity(), object.getJSONArray("Msg").getJSONObject(0).getString("displaymsg"), Toast.LENGTH_SHORT).show();
                DashBroadFragment fragment = new DashBroadFragment();
                String tag = DashBroadFragment.TAG;
                ((BaseActivity)getActivity()).onOpenFragment(fragment);
            } else
            {
               JSONObject object1= object.getJSONObject("errormsg");
               Toast.makeText(getActivity(), object1.getString("error"), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), getResources().getString(R.string.error_server_something_worng), Toast.LENGTH_SHORT).show();
        }
    }

    private String getStringFromJson(JSONObject object, String str) throws Exception
    {
        if(object.has(str))
        {
            return  object.getString(str);
        }
        return "";
    }


}