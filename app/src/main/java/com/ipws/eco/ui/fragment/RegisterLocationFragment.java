package com.ipws.eco.ui.fragment;

import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import com.ipws.eco.utils.AppPreference;
import com.ipws.eco.utils.GPSTracker;
import com.ipws.eco.utils.Logs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RegisterLocationFragment extends BaseFragment implements View.OnClickListener
{
    public static String TAG= "registor_location";
    private Spinner spinnerAccount, spinnerLocation;
    private List<String>  listLocation;
    private List<Integer> listLocationId;
    private String mLat="", mLong ="";
    private int mSelSprLoc=0;

    public RegisterLocationFragment() { }

    public static RegisterLocationFragment newInstance(Bundle bundle) {
        RegisterLocationFragment fragment = new RegisterLocationFragment();
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
        View view= inflater.inflate(R.layout.fragment_register_location, container, false);
        init(view);
        initListener(view);
        return addToBaseFragment(view);
    }

    private void init(View view)
    {
         spinnerAccount = (Spinner) view.findViewById(R.id.spinnerAccount);
         spinnerLocation = (Spinner) view.findViewById(R.id.spinnerLocation);
        ((TextView) view.findViewById(R.id.text1)).setTypeface(((BaseActivity)getActivity()).getTypefaceFontAwesome());
        ((TextView) view.findViewById(R.id.text2)).setTypeface(((BaseActivity)getActivity()).getTypefaceFontAwesome());
    }

    private void initListener(View v){

        v.findViewById(R.id.txtBtRegisterLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GPSTracker gps = new GPSTracker(getActivity());
                if(gps.canGetLocation()){
                    double mLatitude= gps.getLatitude(); // returns latitude
                    double mLongitude = gps.getLongitude(); // returns longitude
                    requestRegLocation(mSelSprLoc,mLatitude, mLongitude);
                }else
                    gps.showSettingsAlert();

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        customize();
        requestAccount();
    }

    private void customize(){
        ((BaseActivity)getActivity()).setTitleIcon(getResources().getString(R.string.fa_icon_reg_loc), getResources().getString(R.string.menu_reg_location));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    CalendarListener calendarListener = new CalendarListener() {
        @Override
        public void onDateSelected(Date date) {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        }

        @Override
        public void onMonthChanged(Date date) {
//                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
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
                listName.add("Select Account");

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
                    listLocation.add("Select Location");
                    spinnerLocation();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void requestLocation(int custId)
    {
        showProgressBar();
        String parameter = "TxnType=PLOCATIONLIST&LOGINNAME1="+AppPreference.getInstance(getActivity()).getLoginName()+"&Msg=USERID:"+AppPreference.getInstance(getActivity()).getUserId()+"|CUSTNAME:NONE|CUSTID:"+custId+"|CRITERIA:ID|TDTSM:2017-06-20 12*30*38.287";
        Logs.d("Network Url: "+parameter);
        parameter = parameter.replace(" ", "%20");
        NetworkDAO.getInstance(getActivity()).login(getActivity(), parameter, success1, error1);
    }

    Response.Listener success1= new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            hideProgressBar();
            Logs.d("Network response :"+ response);
            parseLocation(response);
        }
    };

    Response.ErrorListener error1= new Response.ErrorListener() {
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
                mSelSprLoc=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void requestRegLocation(int i, double mLatitude, double mLong) { //1004
        if (listLocationId.get(i) <= 0){
            Toast.makeText(getActivity(), "Plrase select location.", Toast.LENGTH_SHORT).show();
            return;
        }
        showProgressBar();
        String parameter = "TxnType=PREGISTERLOCATION&LOGINNAME1="+AppPreference.getInstance(getActivity()).getLoginName()+"&Msg=USERID:"+AppPreference.getInstance(getActivity()).getUserId()+"|LAT:"+mLatitude+"|LONG:"+mLong+"|DATETIME:2017-06-20 12:30:38.287|LOCATION:"+listLocationId.get(i)+"|DEVICEID:HSJDHHDHD293|TDTSM:2017-06-20 12:30:38.287";
        Logs.d("Network Url: "+parameter);
        parameter = parameter.replace(" ", "%20");
        NetworkDAO.getInstance(getActivity()).login(getActivity(), parameter, success2, error2);
    }

    Response.Listener success2= new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            hideProgressBar();
            Logs.d("Network response :"+ response);
            parseRegLocation(response);
        }
    };

    Response.ErrorListener error2 = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            hideProgressBar();
            Logs.d("Network Error: " + error.getMessage());
            error.printStackTrace();
            Toast.makeText(getActivity(), "Unable to connect server.", Toast.LENGTH_SHORT).show();
        }
    };

    private void parseRegLocation(String data)
    {
        try
        {
            JSONObject object = new JSONObject(data);
            if(object.has("status") && object.getString("status").equals("00"))
            {
                listId = new ArrayList<>();
                listName = new ArrayList<>();
                JSONArray array = object.getJSONArray("Msg");
                for (int i=0; i<array.length(); i++){
                    JSONObject jObj= array.getJSONObject(0);
                    if(jObj.has("errorMsg"))
                    {
                        Toast.makeText(getActivity(),jObj.getString("errorMsg"), Toast.LENGTH_SHORT).show();
                    }
                }
            }else {
                Toast.makeText(getActivity(), getResources().getString(R.string.error_server_something_worng), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), getResources().getString(R.string.error_server_something_worng), Toast.LENGTH_SHORT).show();
        }
    }
}