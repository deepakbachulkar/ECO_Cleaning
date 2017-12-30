package com.ipws.eco.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.MessageQueue;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ipws.eco.ECOAppControl;
import com.ipws.eco.R;
import com.ipws.eco.network.NetworkConstant;
import com.ipws.eco.network.NetworkDAO;
import com.ipws.eco.network.ServerRequest;
import com.ipws.eco.ui.activity.ForgotPwActivity;
import com.ipws.eco.ui.activity.HomeActivity;
import com.ipws.eco.utils.AppPreference;
import com.ipws.eco.utils.AppUtils;
import com.ipws.eco.utils.Logs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class LoginFragment extends BaseFragment implements View.OnClickListener
{
    private View mView;
    public static String TAG= "login";
    private EditText mPasswordView,mEmailView;
    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
    }

    public static LoginFragment newInstance(Bundle bundle) {
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_login, container, false);
        init(mView);
        return addToBaseFragment(mView);
    }

    private void init(View v)
    {
        mPasswordView = (EditText) v.findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
            {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mEmailView = (EditText) v.findViewById(R.id.email);
        mEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
            {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        TextView txtBtnLogin = (TextView) v.findViewById(R.id.txtBtnLogin);

                        txtBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
                            //                toNext(HomeActivity.class, true);
            }
        });

        TextView txtPw= (TextView) v.findViewById(R.id.txtPw);
        txtPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toNext(ForgotPwActivity.class, false);
            }
        });

        mEmailView.setText(NetworkConstant.user);
        mPasswordView.setText(NetworkConstant.password);
    }

    @Override
    public void onStart() {
        super.onStart();
        showFragmentCopyRight();
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

    private void attemptLogin()
    {
        mPasswordView.setError(null);
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }else
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required_pw));
            focusView = mPasswordView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            showFragmentProgressBar();
            requestLogin(email, password);
        }
    }


//    /**
//     * Shows the progress UI and hides the login form.
//     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    private void showProgress(final boolean show)
//    {
//        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
//        // for very easy animations. If available, use these APIs to fade-in
//        // the progress spinner.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });
//
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mProgressView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                }
//            });
//        } else {
//            // The ViewPropertyAnimator APIs are not available, so simply show
//            // and hide the relevant UI components.
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//        }
//    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login:
                toNext(HomeActivity.class, true);
                break;
        }
    }

    private void toNext(Class classs, boolean isFinish){
        Intent intent = new Intent(getActivity(), classs);
        startActivity(intent);
        if(isFinish)
            getActivity().finish();
    }

    private void requestLogin(String username, String password)
    {
        //9595036832 : Ipws@123
            String parameter = "TxnType=PLOGIN&LOGINNAME1="+username+"&Msg=MACHINEIP:10.10.10.10|TDTSM:2017-08-19%2019*02*10.093|GUID:7987989-98789-98798|LOGINNAME2:"+username+"|PASS:"+password;
        NetworkDAO.getInstance(getActivity()).login(getActivity(), parameter, success, error);
//        String parameter = "TxnType=PLOGIN&LOGINNAME1="+name+"&Msg=" +
//                "LOGINNAME2:9595036832|PASS:"+password+"|ROLE:C|MACHINEIP:10.10.0.57|DOB:14-10-1990|TDTSM:2017-06-20%2012:30:38.287";
    }

    private void parse(String data)
    {
        try
        {
            if(data.contains("\"status\":\"00\""))
            {
                Log.d("eco", "Resp: " + data);
                JSONObject object = new JSONObject(data);
                if (object.has("status") && object.getString("status").equals("00"))
                {
                    String msg = object.getString("Msg");
                    JSONArray jMsg = new JSONArray(msg);
                    JSONObject object1 = jMsg.getJSONObject(0);
                    AppPreference.getInstance(getActivity()).setUserId(object1.getString("id"));
                    AppPreference.getInstance(getActivity()).setLogin(msg);
                    AppPreference.getInstance(getActivity()).setLoginName(mEmailView.getText().toString().trim());
                    Logs.d("Password" + mPasswordView.getText().toString().trim());
                    AppPreference.getInstance(getActivity()).setPassword(mPasswordView.getText().toString().trim());
                    if(getActivity()!=null)
                        Toast.makeText(getActivity(), "You're logged in as "+object1.getString("user_name"), Toast.LENGTH_SHORT).show();
                        AppPreference.getInstance(getActivity()).setDashBroadHours(object.getJSONArray("Individual").toString());
                    toNext(HomeActivity.class, true);

                    if(object.has("Schedule")){
                        try {
                            JSONArray scheduleJA = object.getJSONArray("Schedule");
                            Log.d("Data","Schedule: "+scheduleJA.toString());
                            {
                                AppPreference.getInstance(getActivity()).setDashBroadData(object.getJSONArray("Schedule").toString());
                            }
                        }catch (JSONException e){

                        }
                    }
                }
            }else
            {
                Log.d("eco", "Resp: " + data);
                JSONObject object = new JSONObject(data)    ;
                if(object.has("errormsg")) {
                    JSONObject object1 = object.getJSONObject("errormsg");
                    if(getActivity()!=null)
                        Toast.makeText(getActivity(), object1.getString("error"), Toast.LENGTH_SHORT).show();
                }else
                    if(getActivity()!=null)
                        Toast.makeText(getActivity(), "Invalid User.", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
                e.printStackTrace();
            if(getActivity()!=null)
                Toast.makeText(getActivity(), "User not registered.", Toast.LENGTH_SHORT).show();
        }
    }

    Response.Listener success= new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Logs.d("Network response :"+ response);
            hideProgressBar();
            parse(response);
        }
    };

    Response.ErrorListener error = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Logs.d("Network error :"+ error.getMessage());
            hideProgressBar();
            error.printStackTrace();
            if(getActivity()!=null)
                Toast.makeText(getActivity(), "Unable to connect server.", Toast.LENGTH_SHORT).show();
        }
    };


    private  String encrypt(String data, String password) throws  Exception
    {
        SecretKeySpec key = genrate(password);
        Cipher c =Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] enVal = c.doFinal(data.getBytes());
        String ency = Base64.encodeToString(enVal, Base64.DEFAULT);
        return ency;
    }

    private SecretKeySpec genrate(String password) throws  Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes =password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }


}