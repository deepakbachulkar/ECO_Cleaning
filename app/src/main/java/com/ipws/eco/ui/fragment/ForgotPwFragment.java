package com.ipws.eco.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.bumptech.glide.util.Util;
import com.ipws.eco.R;
import com.ipws.eco.network.NetworkDAO;
import com.ipws.eco.ui.activity.ChangePwActivity;
import com.ipws.eco.ui.activity.HomeActivity;
import com.ipws.eco.utils.AppPreference;
import com.ipws.eco.utils.AppUtils;

import org.json.JSONObject;

public class ForgotPwFragment extends BaseFragment implements View.OnClickListener
{
    public static String TAG= "forgot_pw";
    private TextView txtBtnSendPw;
    private TextView txtBtnIKnow;
    private EditText edtEmail;

    private OnFragmentInteractionListener mListener;

    public ForgotPwFragment() { }

    public static ForgotPwFragment newInstance(Bundle bundle) {
        ForgotPwFragment fragment = new ForgotPwFragment();
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
        View view= inflater.inflate(R.layout.fragment_forgot_pw, container, false);
        init(view);
        return addToBaseFragment(view);
    }

    private void init(View view)
    {
        edtEmail = (EditText) view.findViewById(R.id.edtEmail);
        txtBtnIKnow = (TextView) view.findViewById(R.id.txtBtnIKnowPw);
        txtBtnSendPw = (TextView) view.findViewById(R.id.txtBtnSendPw);
        txtBtnIKnow.setOnClickListener(this);
        txtBtnSendPw.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.txtBtnIKnowPw){
//            Intent intent = new Intent(getActivity(), ChangePwActivity.class);
//            startActivity(intent);
            getActivity().finish();
        }else if(view.getId() == R.id.txtBtnSendPw)
        {

            boolean cancel = false;
            View focusView = null;

            if (TextUtils.isEmpty(edtEmail.getText().toString()))
            {
                edtEmail.setError(getString(R.string.error_email_required));
                focusView = edtEmail;
                cancel = true;
            }else if(!AppUtils.isValidEmail(edtEmail.getText().toString())){
                edtEmail.setError(getString(R.string.error_invalid_email));
                focusView = edtEmail;
                cancel = true;
            }
            if (cancel) {
                focusView.requestFocus();
            } else {
                showFragmentProgressBar();
                requestForgotPw(edtEmail.getText().toString());

            }

//            Intent intent = new Intent(getActivity(), HomeActivity.class);
//            startActivity(intent);

        }
    }


    private void requestForgotPw(String email)
    {
//9595036832 : Ipws@123
//        [Base URL]?TxnType=PCHANGEPASSWORD&LOGINNAME1=9595036832&Msg=USERID:1001|OLDPASS:Ipws@123|NEWPASS:Ipws@123|MACHINEIP:10.10.0.57|DOB:14-10-1990|TDTSM:2017-06-20 12:30:38.287
        String parameter =  "TxnType=PSENDFPLINK&Msg=EMAIL:"+email+"|TDTSM:2017-06-20 12-30-38.287";

//            [Base URL]?
        //+"|MACHINEIP:10.10.0.57|DOB:14-10-1990|TDTSM:2017-06-20 1*30*38.287";
        parameter= parameter.replace(" ","%20");
        NetworkDAO.getInstance(getActivity()).login(getActivity(), parameter, success, error);

//        String parameter = "TxnType=PLOGIN&LOGINNAME1="+name+"&Msg=" +
//                "LOGINNAME2:9595036832|PASS:"+password+"|ROLE:C|MACHINEIP:10.10.0.57|DOB:14-10-1990|TDTSM:2017-06-20%2012:30:38.287";
    }

    Response.Listener success= new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            hideProgressBar();
            Log.d(TAG, response.toString());
            parse(response);

        }
    };

    private void parse(String data)
    {
        try
        {
            JSONObject object = new JSONObject(data);
            if (object.has("status") && object.getString("status").equals("00")) {
                Toast.makeText(getActivity(), "A link to reset your password has be sent. Please check your email.", Toast.LENGTH_LONG).show();
                getActivity().finish();
            }else if(object.has("errormsg"))
            {
                JSONObject object1 = object.getJSONObject("errormsg");
                if(object1.has("error")){
                    Toast.makeText(getActivity(), object1.getString("error"), Toast.LENGTH_SHORT).show();
                }
            }else
                Toast.makeText(getActivity(), "Password not reset.", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), "JSON not valid.", Toast.LENGTH_SHORT).show();
        }
    }

    Response.ErrorListener error = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            hideProgressBar();
            VolleyLog.d(TAG, "Error: " + error.getMessage());
            error.printStackTrace();
            Toast.makeText(getActivity(), "Unable to connect server.", Toast.LENGTH_SHORT).show();
        }
    };

}