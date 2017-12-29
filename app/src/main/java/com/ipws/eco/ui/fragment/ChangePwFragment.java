package com.ipws.eco.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ipws.eco.R;
import com.ipws.eco.network.NetworkDAO;
import com.ipws.eco.ui.activity.BaseActivity;
import com.ipws.eco.ui.activity.HomeActivity;
import com.ipws.eco.utils.AppPreference;
import com.ipws.eco.utils.Logs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class ChangePwFragment extends BaseFragment {

    public static String TAG= "change_pw";

    private OnFragmentInteractionListener mListener;
    private EditText edtReEntNewPw, eddOldPw, edtNewPw;
    private ImageView imgPassShowOnOff;
    private boolean isShow=false;

    public ChangePwFragment() { }

    public static ChangePwFragment newInstance(Bundle bundle) {
        ChangePwFragment fragment = new ChangePwFragment();
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
        View view =inflater.inflate(R.layout.fragment_change_pw, container, false);
        init(view);
        initLence(view);
        return addToBaseFragment(view);
    }

    private void init(View v)
    {
        edtReEntNewPw= (EditText)v.findViewById(R.id.edtReEntNewPw);
        eddOldPw= (EditText)v.findViewById(R.id.eddOldPw);
        edtNewPw= (EditText)v.findViewById(R.id.edtNewPw);
        imgPassShowOnOff = (ImageView) v.findViewById(R.id.imgPassShowOnOff);


    }

    private void initLence(final View v){
        v.findViewById(R.id.txtBtnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        v.findViewById(R.id.txtBtnChangePw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    attmentChangePw();
//                Toast.makeText(getActivity(), "Successfully changed the password.", Toast.LENGTH_SHORT).show();
//                getActivity().finish();;
//                Intent intent= new Intent(getActivity(), HomeActivity.class);
//                startActivity(intent);
            }
        });

        imgPassShowOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isShow){
                    eddOldPw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isShow =false;
                    ((ImageView) v.findViewById(R.id.imgPassShowOnOff)).setImageResource(R.drawable.ic_eye);
                }else {
                    eddOldPw.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
                    isShow =true;
                    ((ImageView) v.findViewById(R.id.imgPassShowOnOff)).setImageResource(R.drawable.ic_eye_off);
                }
            }
        });

        eddOldPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().trim().length()>0)
                    imgPassShowOnOff.setVisibility(View.VISIBLE);
                else
                    imgPassShowOnOff.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        isShow =false;
        customize();
    }

    private void customize(){
        ((BaseActivity)getActivity()).setTitleIcon(getResources().getString(R.string.fa_icon_change_pw), "Reset Password");
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

    private void attmentChangePw()
    {
        boolean cancel = false;
        View focusView = null;

        try {
            String user = AppPreference.getInstance(getActivity()).getUserId();
            String pass1="", pass2="", passEN="";
            if(eddOldPw==null) {
                eddOldPw.setError(getString(R.string.error_field_required));
                focusView = eddOldPw;
                cancel = true;
            }else
            if(edtNewPw==null) {
                edtNewPw.setError(getString(R.string.error_invalid_password));
                focusView = edtNewPw;
                cancel = true;
            }else
            if(edtReEntNewPw==null) {
                edtReEntNewPw.setError(getString(R.string.error_invalid_password));
                focusView = edtReEntNewPw;
                cancel = true;
            }else {
                pass1 = eddOldPw.getText().toString();
                pass2 = edtNewPw.getText().toString();
                passEN = edtReEntNewPw.getText().toString();
            }
            if (TextUtils.isEmpty(pass1)) {
                eddOldPw.setError(getString(R.string.error_field_required));
                focusView = eddOldPw;
                cancel = true;
            }else
            if (TextUtils.isEmpty(pass2)) {
                edtNewPw.setError(getString(R.string.error_invalid_password));
                focusView = edtNewPw;
                cancel = true;
            }else
            if (TextUtils.isEmpty(passEN)) {
                edtReEntNewPw.setError(getString(R.string.error_invalid_password));
                focusView = edtReEntNewPw;
                cancel = true;
            }
            if (cancel) {
                focusView.requestFocus();
            } else {
                showFragmentProgressBar();
                requestChangePw(pass1, pass2);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void requestChangePw(String oldPass, String pass)
    {
//9595036832 : Ipws@123
//        [Base URL]?TxnType=PCHANGEPASSWORD&LOGINNAME1=9595036832&Msg=USERID:1001|OLDPASS:Ipws@123|NEWPASS:Ipws@123|MACHINEIP:10.10.0.57|DOB:14-10-1990|TDTSM:2017-06-20 12:30:38.287
        String parameter =  "TxnType=PCHANGEPASSWORD&LOGINNAME1=9595036832&Msg=USERID:"+AppPreference.getInstance(getActivity()).getUserId()+"|OLDPASS:"+oldPass+"|NEWPASS:"+pass;
                //+"|MACHINEIP:10.10.0.57|DOB:14-10-1990|TDTSM:2017-06-20 1*30*38.287";
        parameter= parameter.replace(" ","%20");
        NetworkDAO.getInstance(getActivity()).login(getActivity(), parameter, success, error);

//        String parameter = "TxnType=PLOGIN&LOGINNAME1="+name+"&Msg=" +
//                "LOGINNAME2:9595036832|PASS:"+password+"|ROLE:C|MACHINEIP:10.10.0.57|DOB:14-10-1990|TDTSM:2017-06-20%2012:30:38.287";
    }

    private void parse(String data)
    {
        try
        {
                JSONObject object = new JSONObject(data);
                if (object.has("status") && object.getString("status").equals("00")) {
                    Toast.makeText(getActivity(), "You're password is reset.", Toast.LENGTH_SHORT).show();
                    edtReEntNewPw.setText("");
                    eddOldPw.setText("");
                    edtNewPw.setText("");
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





    Response.Listener success= new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            hideProgressBar();
            Log.d(TAG, response.toString());
            parse(response);

        }
    };

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
