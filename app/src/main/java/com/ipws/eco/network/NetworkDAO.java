package com.ipws.eco.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ipws.eco.utils.Logs;

/**
 * Created by arun on 22/04/15.
 */
public class NetworkDAO {

    private static NetworkDAO instance;
    private String TAG = "Network";
    private static Context mContext;

    private NetworkDAO() {
    }

    public static NetworkDAO getInstance(Context context) {
        if (null == instance) {
            instance = new NetworkDAO();
        }
        mContext =context;
        return instance;
    }

    public void login(Context context, String parameter,
                       Response.Listener success, Response.ErrorListener error)
    {
        try{
//            String url ="http://54.252.182.129/cleaningMobileApi/API/PrepaidAPI.aspx?";
//            [Base URL]?TxnType=PLOGIN&LOGINNAME1=9595036832&Msg=MACHINEIP:10.10.10.10|TDTSM:2017-08-19 19:02:10.093|GUID=7987989-98789-98798|LOGINNAME2:9595036832|PASS:Ipws@123

//            parameter = "TxnType=PLOGIN&LOGINNAME1=9595036832&Msg=MACHINEIP:10.10.10.10|TDTSM:2017-08-1919:02:10.093|GUID=7987989-98789-98798|LOGINNAME2:9595036832|PASS:Ipws@123";

            //http://54.252.182.129/cleaningMobileApi/API/PrepaidAPI.aspx?TxnType=PLOGIN&LOGINNAME1=9595036832&Msg=MACHINEIP:10.10.10.10|TDTSM:2017-08-19 19*02*10.093|GUID:7987989-98789-98798|LOGINNAME2:9595036832|PASS:Ipws@123Sent on:4:24 pm
            String url = NetworkConstant.BASE_URL + parameter;
            Logs.d("Network url :"+ url);
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, success, error);

            RequestQueue mRequestQueue = Volley.newRequestQueue(context);
            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    6000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mRequestQueue.add(strReq);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void staffInOut(Context context, String parameter,
                      Response.Listener success, Response.ErrorListener error)
    {
        try{
            String url = NetworkConstant.BASE_URL + parameter;
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    url, success, error);
            RequestQueue mRequestQueue = Volley.newRequestQueue(context);
            mRequestQueue.add(strReq);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
