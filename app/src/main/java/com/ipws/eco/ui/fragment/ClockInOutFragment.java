package com.ipws.eco.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ipws.eco.R;
import com.ipws.eco.network.NetworkDAO;
import com.ipws.eco.ui.activity.BaseActivity;
import com.ipws.eco.ui.activity.HomeActivity;
import com.ipws.eco.utils.AppDates;
import com.ipws.eco.utils.AppPreference;
import com.ipws.eco.utils.GPSTracker;
import com.ipws.eco.utils.Logs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class ClockInOutFragment extends BaseFragment   {
    public static String TAG= "check_in_out";
    private TextView txtCheckInOut, txtLastCheckIn,  txtTime;
    private OnFragmentInteractionListener mListener;
    private  double mLatitude,  mLongitude;
    public ClockInOutFragment() { }

    public static ClockInOutFragment newInstance(Bundle bundle) {
        ClockInOutFragment fragment = new ClockInOutFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        initValue(view);
        initListener(view);

        return addToBaseFragment(view);
    }

    private void init(View v){
        txtCheckInOut = (TextView) v.findViewById(R.id.txtBtnClickIn);
        txtLastCheckIn = (TextView) v.findViewById(R.id.txtLastCheckIn);
        txtTime = (TextView) v.findViewById(R.id. txtTime);
    }

    private void initValue(View v)
    {
        String curDate = AppDates.with().currentDateTime("E, dd MMMM yyyy");
        if(curDate!=null)
            ((TextView) v.findViewById(R.id.txtDate)).setText(curDate);
        curDate = AppDates.with().currentDateTime("hh:mm a");
        if(curDate!=null)
            ((TextView) v.findViewById(R.id.txtTime)).setText(curDate);
//        yyyy-MM-dd hh:mm:ss a
        curDate = AppDates.with().currentDateTime("E MMMM, dd MMM yyyy hh:mm a");
//        if(curDate!=null)
//            ((TextView) v.findViewById(R.id.txtLastCheckIn)).setText(curDate);
    }

    @Override
    public void onStart() {
        super.onStart();
        customize();
        GPSTracker gps = new GPSTracker(getActivity());
        if(gps.canGetLocation()){
            mLatitude= gps.getLatitude(); // returns latitude
            mLongitude = gps.getLongitude(); // returns longitude
        }else
            gps.showSettingsAlert();

        if(AppPreference.getInstance(getActivity()).getLastLogout()!=null && !AppPreference.getInstance(getActivity()).getLastLogout().equals("")){
            txtLastCheckIn.setText("Last logout "+AppPreference.getInstance(getActivity()).getLastLogout());
        }
        setClockInOut();
        setTitleBarClockInOut();
    }


    private void customize(){
        ((BaseActivity)getActivity()).setNavDrawer();
    }

    private void initListener(final View v){
        v.findViewById(R.id.txtBtnClickIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GPSTracker gps = new GPSTracker(getActivity());
                if(gps.canGetLocation()){
                    mLatitude= gps.getLatitude(); // returns latitude
                    mLongitude = gps.getLongitude(); // returns longitude
                    requestStaffInOut(mLatitude, mLongitude);
                }else
                    gps.showSettingsAlert();
            }
        });
        v.findViewById(R.id.txtBtnViewMyAttendance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity)getActivity()).onOpenFragment(new ScheduleFragment());
            }
        });
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

    private void requestStaffInOut(double latitude, double longitude)
    {
        showProgressBar();
//        19.108588 73.000415
        DecimalFormat precision = new DecimalFormat("0.000000");
        Logs.e("Lat Long:"+precision.format(latitude)+" || "+precision.format(longitude));
        String parameter= "TxnType=PSTAFFINOUT&LOGINNAME1=9595036832&Msg=USERID:"+AppPreference.getInstance(getActivity()).getUserId()+"|LAT:"+ precision.format(latitude)+"|LONG:"+ precision.format(longitude)+"|DATETIME:2017-06-20%2012:30:38.287|LOCATION:0|ENTRYTYPE:AMOBILE|CHECKLOCATON:1|QRCODE:NO QRCODE|TDTSM:2017-06-20%2012:30:38.2872";
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

        //{"status":"00","Msg":[{"status":"00","errorMsg":"Singed IN/OUT for Koparkhairane 20","sessionkey":"7696545930074066","TDTSM":"2017-06-20 12"}]}
        //{"status":"00","Msg":[{"status":"01","errorMsg":"No location found","sessionkey":"7973455319642904","TDTSM":"2017-06-20 12"}]}
        //{"status":"98","errormsg":"Error.","Msg":"TDTSM=|"}
        try
        {
            JSONObject object = new JSONObject(data);
            if(object.has("status") && object.getString("status").equals("00"))
            {
                    String msg = object.getString("Msg");
                    JSONArray jMsg = new JSONArray(msg);
                    JSONObject object1 = jMsg.getJSONObject(0);

                if(object1.has("status") && object1.getString("status").equals("00"))
                {
                    AppPreference.getInstance(getActivity()).setClockInOut(
                            !AppPreference.getInstance(getActivity()).isLockOut());
                    setClockInOut();
                    if(AppPreference.getInstance(getActivity()).isLockOut())
                    {
                        if(object1.has("errorMsg"))
                        {
                            if(getActivity()!=null)
                                Toast.makeText(getActivity(), object1.getString("errorMsg"), Toast.LENGTH_SHORT).show();
                        }
                        setTextClockOut();
                        toNext();
                    }

//                    Toast.makeText(getActivity(), object1.getString("errorMsg"), Toast.LENGTH_SHORT).show();
//                    if(txtCheckInOut.getText().toString().toString().equalsIgnoreCase("CLICK IN"))
//                        setTextClockIn("Last Logout 12:30 pm on 27 October 2017");
//                    else
//                        setTextClockOut("Last Logout 12:30 pm on 27 October 2017");
                }else{
                    if(object1.has("errorMsg"))
                    {
                        if(getActivity()!=null)
                            Toast.makeText(getActivity(), object1.getString("errorMsg"), Toast.LENGTH_SHORT).show();
                    }
                }
            }else {
                if(getActivity()!=null)
                    Toast.makeText(getActivity(), "Somethings is wrong.", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), "Some things is wrong.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
        }
    }

    private void toNext(){
        Bundle bundle = new Bundle();
        if(txtTime!=null &&  txtTime.getText()!=null)
            bundle.putString("date", txtTime.getText().toString().trim());
        else
            bundle.putString("date", " ");
        SuccessFragment successFragment = SuccessFragment.newInstance(bundle) ;
        ((BaseActivity)getActivity()).onOpenFragment(successFragment);
    }

    private void setClockInOut(){
        if(!AppPreference.getInstance(getActivity()).isLockOut()) {
            setTextClockIn();
        }else{
            setTextClockOut();
        }
    }

    private void setTitleBarClockInOut(){
        if(!AppPreference.getInstance(getActivity()).isLockOut()) {
            ((BaseActivity)getActivity()).setTitleIcon(getResources().getString(R.string.fa_icon_calender_2), "Clock In");
        }else{
            ((BaseActivity)getActivity()).setTitleIcon(getResources().getString(R.string.fa_icon_calender_2), "Clock Out");
        }
    }

    private void setTextClockIn(){
        txtCheckInOut.setText("CLOCK IN");
        txtCheckInOut.setBackgroundResource(R.drawable.bg_shape_clock_in);
//        txtLastCheckIn.setText(lastTime);
        txtLastCheckIn.setTextColor(getResources().getColor(R.color.col_app1));
        ((BaseActivity)getActivity()).setTitleIcon(getResources().getString(R.string.fa_icon_calender_2), "Clock In");
    }

    private void setTextClockOut(){
        txtCheckInOut.setText("CLOCK OUT");
        txtCheckInOut.setBackgroundResource(R.drawable.bg_shape_clock_out);
//        txtLastCheckIn.setText(lastTime);
        txtLastCheckIn.setTextColor(getResources().getColor(R.color.col_app2));
    }


}