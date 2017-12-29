    package com.ipws.eco.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ipws.eco.R;
import com.ipws.eco.network.NetworkDAO;
import com.ipws.eco.ui.activity.BaseActivity;
import com.ipws.eco.ui.activity.LoginActivity;
import com.ipws.eco.utils.AppDates;
import com.ipws.eco.utils.AppPreference;
import com.ipws.eco.utils.Logs;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by arun on 10/08/15.
 */
public class NavigationDrawerFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = NavigationDrawerFragment.class.getSimpleName();
    Map<Integer, View> mMenuViews;
    private NavDrawerItemClickListener mNavDrawerItemClickListener;
    private boolean mIsAccountDrawerOpen = false;
    private LinearLayout mLnrAccountsSection;
    //New Navigation Drawer
//    private TextView mProviderName, mProviderBranchName, mTxtSetting;
    private RelativeLayout mProviderDetails;
    private ImageView mImgBack;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navigation_drawer_fragment_new, container, false);
        mView = view;
        initialize(view);
        initViews();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        sendScreenDataToGA(TAG);
    }

    private void initialize(View view) {
        mProviderDetails = (RelativeLayout) view.findViewById(R.id.rl_provider_details);
//        mRatings_1 = (ImageView) view.findViewById(R.id.iv_rating_1);
//        mRatings_2 = (ImageView) view.findViewById(R.id.iv_rating_2);
//        mRatings_3 = (ImageView) view.findViewById(R.id.iv_rating_3);
//        mRatings_4 = (ImageView) view.findViewById(R.id.iv_rating_4);
//        mRatings_5 = (ImageView) view.findViewById(R.id.iv_rating_5);
        mImgBack = (ImageView) view.findViewById(R.id.img_back);
        mProviderDetails.setOnClickListener(this);
        mImgBack.setOnClickListener(this);
        view.findViewById(R.id.txtNavClockInOut).setOnClickListener(this);
        view.findViewById(R.id.txtNavSchedule).setOnClickListener(this);
        view.findViewById(R.id.txtNavChangePw).setOnClickListener(this);
        view.findViewById(R.id.txtNavTimeSheet).setOnClickListener(this);
        view.findViewById(R.id.txtNavReqStock).setOnClickListener(this);
        view.findViewById(R.id.txtNavLeave).setOnClickListener(this);
        view.findViewById(R.id.txtNavRegLocation).setOnClickListener(this);
        view.findViewById(R.id.txtNavLogut).setOnClickListener(this);
        view.findViewById(R.id.txtNavInspection).setOnClickListener(this);
//        ProviderRole selectedProvider = User.getInstance().getSelectedProviderRole();
//
//        ImageView profileImage = (ImageView) view.findViewById(R.id.iv_provider_image);
//
//        if (selectedProvider.getProviderImages() != null) {
//            List<ImageResults> images = selectedProvider.getProviderImages() ;
//            ImageResults image = images.get(0);
//            if (image != null) {
//                Picasso.with(getActivity()).load(image.getNormalImage().getImageUrl()).placeholder(getResources().getDrawable(R.drawable.image_holder_salon_profile)).into(profileImage);
//            }
//        }

//        List<ProviderRole> providerRoles = User.getInstance().getUserDataWrapper().getUserDataMessage().getUserData().getProviderRoles();


//        if(AppUtils.isStringDataValid(selectedProvider.getRating())){
//            try{
//                ratings = Float.valueOf(selectedProvider.getRating());
//            } catch (NumberFormatException e){
//                e.printStackTrace();
//            }
//        }


//        mLnrAccountsSection = (LinearLayout) view.findViewById(R.id.lnr_accounts_selection);
//        setupAccounts();
     Typeface typeface = ((BaseActivity)getActivity()).getTypefaceFontAwesome();
        ((TextView)view.findViewById(R.id.text1)).setTypeface(typeface);
        ((TextView)view.findViewById(R.id.text2)).setTypeface(typeface);
        ((TextView)view.findViewById(R.id.text3)).setTypeface(typeface);
        ((TextView)view.findViewById(R.id.text4)).setTypeface(typeface);
        ((TextView)view.findViewById(R.id.text5)).setTypeface(typeface);
        ((TextView)view.findViewById(R.id.text6)).setTypeface(typeface);
        ((TextView)view.findViewById(R.id.text7)).setTypeface(typeface);
        ((TextView)view.findViewById(R.id.text8)).setTypeface(typeface);
        ((TextView)view.findViewById(R.id.text11)).setTypeface(typeface);
    }
//    public void onEvent(EventBusVo eventBusVo){
//        if(eventBusVo != null && eventBusVo.getTag() != null && eventBusVo.getTag().equalsIgnoreCase("OFFLINE_CUSTOMER")){
//            Log.e("salonResponse", eventBusVo.getData().toString());
//            boolean flag = (boolean) eventBusVo.getData();
//            showOfflineCustomer(flag);
//        }
//    }

    private void initViews(){
        if(AppPreference.getInstance(getActivity()).getUserRoleId().equals("4"))
        {
            mView.findViewById(R.id.relInspection).setVisibility(View.VISIBLE);
        }else{

            mView.findViewById(R.id.relInspection).setVisibility(View.GONE);
        }
    }


    public void setSelectedItem(int id) {
        if (mMenuViews != null && mMenuViews.containsKey(id)) {
            mMenuViews.get(id).setSelected(true);
        }
    }

    public boolean isItemSelected(int id) {
        return (mMenuViews != null && mMenuViews.containsKey(id) && mMenuViews.get(id).isSelected());
    }

    public interface NavDrawerItemClickListener {
        void onNavDrawerItemClick();
    }

    public void setNavDrawerItemCLickListener(NavDrawerItemClickListener listener) {
        mNavDrawerItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        BaseFragment fragment;
        String tag;

        Activity activity = getActivity();
//        if (id == R.id.iv_drop_down || id == R.id.tv_provider_branch || id == R.id.rl_provider_details ) {
//            mIsAccountDrawerOpen = !mIsAccountDrawerOpen;
//            if(mIsAccountDrawerOpen){
////                HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder()
////                        .setCategory("Multiple Branch")
////                        .setAction("View")
////                        .setLabel("View Only");
////                sendEventDataToGA(TAG, builder);
//            }
//            mLnrAccountsSection.setVisibility(mIsAccountDrawerOpen ? View.VISIBLE : View.GONE);
//            return;
//        }
        if (mNavDrawerItemClickListener != null) {
            mNavDrawerItemClickListener.onNavDrawerItemClick();
        }
        switch (id) {
            case R.id.img_back:
                ((BaseActivity) getActivity()).onBackPressed();
            break;
            case R.id.txtNavLogut:
                requestLogout();
//                toNext();
                break;
            case R.id.txtNavChangePw:
                fragment = new ChangePwFragment();
                tag = ChangePwFragment.TAG;
                ((BaseActivity)getActivity()).onOpenFragment(fragment);
                break;
            case R.id.txtNavLeave:
                fragment = new ApplyForLeaveFragment();
                tag = ApplyForLeaveFragment.TAG;
                ((BaseActivity)getActivity()).onOpenFragment(fragment);
                break;
            case R.id.txtNavReqStock:
                fragment = new RequestToStockFragment();
                tag = RequestToStockFragment.TAG;
                ((BaseActivity)getActivity()).onOpenFragment(fragment);
                break;
            case R.id.txtNavTimeSheet:
                fragment = new TimeSheetFragment();
                tag = TimeSheetFragment.TAG;
                ((BaseActivity)getActivity()).onOpenFragment(fragment);
                break;
            case R.id.txtNavSchedule:
                fragment = new ScheduleFragment();
                tag = ScheduleFragment.TAG;
                ((BaseActivity)getActivity()).onOpenFragment(fragment);
                break;
            case R.id.txtNavClockInOut:
                fragment = new ClockInOutFragment();
                tag = ClockInOutFragment.TAG;
                ((BaseActivity)getActivity()).onOpenFragment(fragment);
                break;
            case R.id.txtNavRegLocation:
                fragment = new RegisterLocationFragment();
                tag = RegisterLocationFragment.TAG;
                ((BaseActivity)getActivity()).onOpenFragment(fragment);
                break;
            case R.id.txtNavInspection:
                fragment = new InspectionListFragment();
                tag = InspectionListFragment.TAG;
                ((BaseActivity)getActivity()).onOpenFragment(fragment);
                break;
//            case R.id.txt_nav_profile:
//                HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder()
//                        .setCategory("Profile")
//                        .setAction("View")
//                        .setLabel("View Profile");
//                sendEventDataToGA(TAG,builder);
//                if (!(activity instanceof SalonProfileActivity)) {
//                    setSelectedItem(R.string.profile);
//                    startActivity(new Intent(getActivity(), SalonProfileActivity.class));
//                }
//                break;
//            case R.id.tv_provider_logout:
//                activity = getActivity();
//                if (activity != null) {
//                    builder = new HitBuilders.EventBuilder()
//                            .setCategory("LogOut")
//                            .setAction("Logged Out");
//                    sendEventDataToGA(TAG, builder);
//                    ((BaseActivity) activity).logout();
//                }
        }
    }


    private void requestLogout()
    {
//9595036832 : Ipws@123
//        [Base URL]?TxnType=PSTAFFINOUT&LOGINNAME1=9595036832&Msg=USERID:1002|LAT:19.108588|LONG:73.000415|DATETIME:2017-06-20 12:30:38.287|LOCATION:0|ENTRYTYPE:AMOBILE|CHECKLOCATON:1|QRCODE:NO QRCODE|TDTSM:2017-06-20 12:30:38.287
        try {
            String parameter;
            String userId = AppPreference.getInstance(getActivity()).getUserId()+"";
            parameter ="TxnType=PLOGOUT&LOGINNAME1="+userId+"&Msg=MACHINEIP:10.10.10.10|TDTSM:2017-08-19 19:02:10.093|LOGINNAME2:"+userId;
            parameter ="TxnType=PSTAFFINOUT&LOGINNAME1=9595036832&Msg=USERID:"+userId+"|LAT:19.108588|LONG:73.000415|DATETIME:2017-06-20 12:30:38.287|LOCATION:0|ENTRYTYPE:AMOBILE|CHECKLOCATON:1|QRCODE:NO QRCODE|TDTSM:2017-06-20 12:30:38.287";
            Logs.d("Network logout:"+parameter);
            parameter= parameter.replace(" ","%20");
               Logs.d("Network logout:"+parameter);
            NetworkDAO.getInstance(getActivity()).login(getActivity(), parameter, success, error);
        }catch (Exception e){
            e.printStackTrace();
        }

//        String parameter = "TxnType=PLOGIN&LOGINNAME1="+name+"&Msg=" +
//                "LOGINNAME2:9595036832|PASS:"+password+"|ROLE:C|MACHINEIP:10.10.0.57|DOB:14-10-1990|TDTSM:2017-06-20%2012:30:38.287";

    }

    private void parse(String data)
    {
        try
        {
            JSONObject object = new JSONObject(data);
            if(object.has("status") && object.getString("status").equals("00"))
            {
                if(object.has("errormsg")){
//                    if(getActivity()!=null)
//                        Toast.makeText(getActivity(), object.getString("errormsg"), Toast.LENGTH_SHORT).show();
                }

            }else {
//                if(getActivity()!=null)
//                    Toast.makeText(getActivity(), "Not logout.", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
//            if(getActivity()!=null)
//                Toast.makeText(getActivity(), "JSON not valid.", Toast.LENGTH_SHORT).show();

        }finally {
            toNext();
        }
    }

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


    Response.Listener success= new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d(TAG, response.toString());
            parse(response);
        }
    };

    Response.ErrorListener error = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            VolleyLog.d(TAG, "Error: " + error.getMessage());
            error.printStackTrace();
//            if(getActivity()!=null)
//                Toast.makeText(getActivity(), "Unable to connect server.", Toast.LENGTH_SHORT).show();
            toNext();
        }
    };

    private void toNext()
    {
        AppPreference.getInstance(getActivity()).setLastLogout(
                AppDates.with().currentDateTime("hh:mm") +" on "+AppDates.with().currentDateTime("dd MMMM yyyy")+".");
        AppPreference.getInstance(getActivity()).setUserId("");
        AppPreference.getInstance(getActivity()).setLogin("");
        getActivity().finish();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
}
