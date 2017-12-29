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

public class CalenderFragment extends BaseFragment   {

    public static String TAG= "home";

    private  double mLatitude,  mLongitude;
    public CalenderFragment() {
    }

    public static CalenderFragment newInstance(Bundle bundle) {
        CalenderFragment fragment = new CalenderFragment();
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
        View view =inflater.inflate(R.layout.fragment_calender, container, false);
        init(view);
        initValue(view);
        initListener(view);
        return view;
    }

    private void init(View v){
    }

    private void initValue(View v)
    { }

    @Override
    public void onStart() {
        super.onStart();
        ((BaseActivity) getActivity()).setNavDrawer();
        ((BaseActivity) getActivity()).setTitle("Clock-In");
    }
    private void initListener(final View v){
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void requestStaffInOut(double latitude, double longitude)
    {
        String parameter = "TxnType=PSTAFFINOUT&LOGINNAME1=9595036832&Msg=USERID:"+AppPreference.getInstance(getActivity()).getUserId()+"|LAT:19.108588|LONG:73.000415|DATETIME:2017-06-20 12:30:38.287|LOCATION:0|ENTRYTYPE:AMOBILE|CHECKLOCATON:1|QRCODE:NO QRCODE|TDTSM:2017-06-20 12:30:38.287";
//        19.108588 73.000415
        DecimalFormat precision = new DecimalFormat("0.000000");
// dblVariable is a number variable and not a String in this case
        parameter= "TxnType=PSTAFFINOUT&LOGINNAME1=9595036832&Msg=USERID:"+AppPreference.getInstance(getActivity()).getUserId()+"|LAT:"+ precision.format(latitude)+"|LONG:"+ precision.format(longitude)+"|DATETIME:2017-06-20%2012:30:38.287|LOCATION:0|ENTRYTYPE:AMOBILE|CHECKLOCATON:1|QRCODE:NO QRCODE|TDTSM:2017-06-20%2012:30:38.2872";
        Logs.d("Network Url: "+parameter);
        parameter = parameter.replace(" ", "%20");
        NetworkDAO.getInstance(getActivity()).login(getActivity(), parameter, success, error);
    }

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
                if(object1.has("errorMsg"))
                {
                    Toast.makeText(getActivity(), object1.getString("errorMsg"), Toast.LENGTH_SHORT).show();
                }
                if(object1.has("status") && object1.getString("status").equals("00"))
                {
//                    Toast.makeText(getActivity(), object1.getString("errorMsg"), Toast.LENGTH_SHORT).show();

                }
            }else {
                    Toast.makeText(getActivity(), "Somethings is wrong.", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), "Some things is wrong.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
        }
    }

    Response.Listener success= new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Logs.d("Network response :"+ response);
            parse(response);

        }
    };

    Response.ErrorListener error = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Logs.d("Network Error: " + error.getMessage());
            error.printStackTrace();
            Toast.makeText(getActivity(), "Unable to connect server.", Toast.LENGTH_SHORT).show();
        }
    };


}