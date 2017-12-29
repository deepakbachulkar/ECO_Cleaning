package com.ipws.eco.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ipws.eco.R;
import com.ipws.eco.expandview.ExpandingList;
import com.ipws.eco.model.StaffScheduleDetail;
import com.ipws.eco.network.NetworkDAO;
import com.ipws.eco.ui.activity.BaseActivity;
import com.ipws.eco.ui.examplerecyclerview.StaffDetailRecyclerViewAdapter;
import com.ipws.eco.utils.AppDates;
import com.ipws.eco.utils.AppPreference;
import com.ipws.eco.utils.Logs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class DashBroadFragment extends BaseFragment implements View.OnClickListener
{
    public static String TAG= "dashboard";
    private OnFragmentInteractionListener mListener;
    private List<StaffScheduleDetail> mListDetails = new ArrayList<>();
    final private int ONE_DAY_MILL_SEC =86400000;
    final public static String TODAY = "Today", TOMORROW = "Tomorrow", NEXT_SCHEDULE = "Next Schedule";
    private TextView txtIconMenu, txtImgDate, txtImgDate2, txtFaIconClock, txtTodayMsg;

    public DashBroadFragment() { }

    private View mView;

    public static DashBroadFragment newInstance(Bundle bundle) {
        DashBroadFragment fragment = new DashBroadFragment();
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
        View view= inflater.inflate(R.layout.fragment_dashborad, container, false);
        mView=view;
        init(view);
        initValue(view);
        return addToBaseFragment(view);
    }

    private void init(View view)
    {
        view.findViewById(R.id.txtRetry).setOnClickListener(this);
        txtIconMenu = (TextView) view.findViewById(R.id.txtIconMenu);
        txtImgDate2 = (TextView) view.findViewById(R.id.txtImgDate2);
        txtImgDate = (TextView) view.findViewById(R.id.txtImgDate);
        txtFaIconClock = (TextView) view.findViewById(R.id.txtFaIconClock);
        txtTodayMsg = (TextView) view.findViewById(R.id.txtTodayMsg);
        ((TextView) view.findViewById(R.id.txtWelcome)).setText("Welcome "+AppPreference.getInstance(getActivity()).getUserName()+",");
        txtIconMenu.setOnClickListener(this);
    }

    private  void setFont(){
        txtIconMenu.setTypeface(((BaseActivity)getActivity()).getTypefaceFontAwesome());
        txtImgDate2.setTypeface(((BaseActivity)getActivity()).getTypefaceFontAwesome());
        txtImgDate.setTypeface(((BaseActivity)getActivity()).getTypefaceFontAwesome());
        txtFaIconClock.setTypeface(((BaseActivity)getActivity()).getTypefaceFontAwesome());
    }

    private void initValue(View v)
    {
        if(AppPreference.getInstance(getActivity()).getDashBroadData().equals(""))
        {
            v.findViewById(R.id.relCardView).setVisibility(View.GONE);
            txtTodayMsg.setText("Your roster is not available.");
        }else{

            parse(AppPreference.getInstance(getActivity()).getDashBroadData());
            v.findViewById(R.id.relCardView).setVisibility(View.VISIBLE);
        }
        ((TextView) v.findViewById(R.id.txtWeekly)).setText(AppPreference.getInstance(getActivity()).getDashBroadHoursWeekly());
        ((TextView) v.findViewById(R.id.txtMonthly)).setText(AppPreference.getInstance(getActivity()).getDashBroadHoursMonthly());
    }


    @Override
    public void onStart() {
        super.onStart();
        customize();
        setFont();
//        requestSchedule();
    }

    private void customize(){
        ((BaseActivity)getActivity()).setNavDrawer();
        ((BaseActivity)getActivity()).hideToolBar();
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

    private void initValue(){

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.txtIconMenu){
            ((BaseActivity)getActivity()).openDrawers();
        }
    }



    private void parse(String data)
    {

        try
        {
                List<StaffScheduleDetail> list = new ArrayList<>();
                JSONArray array = new JSONArray(data);
                for (int i = 0; i < array.length(); i++)
                {
                    StaffScheduleDetail detail = new StaffScheduleDetail();
                    JSONObject jsonObject = array.getJSONObject(i);
                    detail.setLocation(getStringFromJson(jsonObject, "Location"));
                    detail.setShiftTime(getStringFromJson(jsonObject, "Shift_Time"));
                    detail.setChechInTime(getStringFromJson(jsonObject, "Check_In_Time"));
                    detail.setCheckOutTime(getStringFromJson(jsonObject, "Check_Out_Time"));
                    detail.setStaffId(getStringFromJson(jsonObject, "Staff_id"));
                    detail.setAddress(getStringFromJson(jsonObject, "AddressDetails"));
                    detail.setForDate(getStringFromJson(jsonObject, "ForDate"));
//                    detail.setLocation(getStringFromJson(jsonObject, "sessionkey"));
//                    if(i==1)
                     list.add(detail);
                }
                if(list.size()==2){
                    setTomorrow(list.get(1));
                    setToday(list.get(0));
                }else if(list.size()==1){
                    {
                        mView.findViewById(R.id.txtTomm).setVisibility(View.GONE);
                        mView.findViewById(R.id.linScheduleTomm).setVisibility(View.GONE);
                        setToday(list.get(0));
                    }
                }else{
                    mView.findViewById(R.id.txtTomm).setVisibility(View.GONE);
                    mView.findViewById(R.id.linScheduleTomm).setVisibility(View.GONE);
                    mView.findViewById(R.id.txtTodayMsg).setVisibility(View.VISIBLE);
                    txtTodayMsg.setText("Your roster is not available.");
                    mView.findViewById(R.id.linSchedule).setVisibility(View.GONE);

                }
        }catch (Exception e){
            e.printStackTrace();
            showRetry(View.GONE);
            Toast.makeText(getActivity(), "No record found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setToday(StaffScheduleDetail detail)
    {
        txtTodayMsg.setText("Your roster for " + AppDates.with().stringToString(detail.getForDate(),AppDates.APP_SEND_DATE_FORMAT, AppDates.APP_SHOW_DATE_FORMAT_2));
        mView.findViewById(R.id.txtTodayMsg).setVisibility(View.VISIBLE);
        mView.findViewById(R.id.linSchedule).setVisibility(View.VISIBLE);
        ((TextView)mView.findViewById(R.id.txtTimeFromTo)).setText(detail.getShiftTime());
        ((TextView)mView.findViewById(R.id.txtAddress1)).setText(detail.getLocation());
        ((TextView)mView.findViewById(R.id.txtAddress2)).setText(detail.getAddress());

    }

    private void setTomorrow(StaffScheduleDetail detail)
    {
        ((TextView) mView.findViewById(R.id.txtTomm)).setText("Next schedule " + AppDates.with().stringToString(detail.getForDate(),
                AppDates.APP_SEND_DATE_FORMAT, AppDates.APP_SHOW_DATE_FORMAT_2));
        mView.findViewById(R.id.txtTomm).setVisibility(View.VISIBLE);
        mView.findViewById(R.id.linScheduleTomm).setVisibility(View.VISIBLE);
        ((TextView)mView.findViewById(R.id.txtTimeFromTo2)).setText(detail.getShiftTime());
        ((TextView)mView.findViewById(R.id.txtAddress3)).setText(detail.getLocation());
        ((TextView)mView.findViewById(R.id.txtAddress4)).setText(detail.getAddress());
    }

    private void showRetry(int isVib)
    {
        if(getView()!=null) {
            ((TextView)getView().findViewById(R.id.txtRetry)).setText("Retry");
            getView().findViewById(R.id.txtRetry).setVisibility(isVib);
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