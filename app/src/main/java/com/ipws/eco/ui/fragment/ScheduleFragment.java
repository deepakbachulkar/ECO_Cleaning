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

public class ScheduleFragment extends BaseFragment implements View.OnClickListener
{
    public static String TAG= "schedule";
    private OnFragmentInteractionListener mListener;
    private List<StaffScheduleDetail> mListDetails = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayout linSchedule;
    private ExpandingList mExpandingList;
    final private int ONE_DAY_MILL_SEC =86400000;
    final public static String TODAY = "Today", TOMORROW = "Tomorrow", NEXT_SCHEDULE = "Next Schedule";

    public ScheduleFragment() {
    }

    public static ScheduleFragment newInstance(Bundle bundle) {
        ScheduleFragment fragment = new ScheduleFragment();
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
        View view= inflater.inflate(R.layout.fragment_schedule, container, false);
        init(view);
        return addToBaseFragment(view);
    }

    private void init(View view)
    {
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerStaff);
//        linSchedule = (LinearLayout) view.findViewById(R.id.linSchedule);
        view.findViewById(R.id.txtRetry).setOnClickListener(this);
//        mExpandingList = (ExpandingList) view.findViewById(R.id.expanding_list_main);
    }

    @Override
    public void onStart() {
        super.onStart();
        customize();
        requestSchedule();
    }

    private void customize(){
        ((BaseActivity)getActivity()).setTitleIcon(getResources().getString(R.string.fa_icon_calender), "Schedule");
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
        if(view.getId()==R.id.txtRetry){
            showProgressBar();
            requestSchedule();
        }
    }

    private void requestSchedule()
    {
        mListDetails =new ArrayList<>();
        showRetry(View.GONE,"");
        showProgressBar();
        String date= AppDates.with().currentDateTime("yyyy-MM-dd");
        date="2017-10-20";
        String parameter = "TxnType=PSCHEDULE&LOGINNAME1="+AppPreference.getInstance(getActivity()).getLoginName()+"&Msg=USERID:"+AppPreference.getInstance(getActivity()).getUserId()+"|DATEOFSCH:"+date+"|TDTSM:2017-06-20 12-30-38.287";
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
            showRetry(View.VISIBLE,"Server Error! Please Retry");
            Logs.d("Network Error: " + error.getMessage());
            error.printStackTrace();
            Toast.makeText(getActivity(), "Unable to connect server.", Toast.LENGTH_SHORT).show();
        }
    };

    private void parse(String data)
    {
        List<StaffScheduleDetail> mListToday = new ArrayList<>();
        List<StaffScheduleDetail> mListTomorrow = new ArrayList<>();
        List<StaffScheduleDetail> mListNextSchedule = new ArrayList<>();

        long todayStart = AppDates.with().stringToMillSec(AppDates.with().currentDateTime("dd mm yyyy"), "dd mm yyyy");
        Logs.i("Date today Start MillSec:"+ todayStart);
        long todayEnd=todayStart +  ONE_DAY_MILL_SEC;
        Logs.i("Date MillSec today End :"+ todayEnd);
        long tommEnd = todayEnd + ONE_DAY_MILL_SEC;
        Logs.i("Date MillSec Tomm Start :"+ tommEnd);
        Logs.d("Time Stam Today: "+ Calendar.getInstance().getTimeInMillis());
        try
        {
            String currentDate= AppDates.with().currentDateTime("yyyy-mm-dd");
            String nextDate = AppDates.with().currentNextDateTime("yyyy-mm-dd");
            Logs.d("Schedule currentDate:"+currentDate);
            Logs.d("Schedule nextDate:"+nextDate);
            JSONObject object = new JSONObject(data);
            if(object.has("status") && object.getString("status").equals("00"))
            {
               JSONArray array = object.getJSONArray("Msg");
                StaffScheduleDetail today = new StaffScheduleDetail();
//                today.setAddress("All Schedule");
//                mListDetails.add(today);
                long time = AppDates.with().stringToMillSec(AppDates.with().currentDateTime("yyyy-mm-dd"),"yyyy-mm-dd");
                long currentTime = Calendar.getInstance().getTimeInMillis();
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

                    long timeDate = AppDates.with().stringToMillSec(detail.getForDate(),"yyyy-mm-dd")+ ONE_DAY_MILL_SEC;

                    Logs.i("Date MillSec serverTime:"+ timeDate);
                    if(timeDate>todayStart  && timeDate<=todayEnd) {
                        detail.setDateType(TODAY);
                        mListToday.add(detail);
                    } else if(timeDate>todayEnd && timeDate<=tommEnd) {
                        detail.setDateType(TOMORROW);
                        mListTomorrow.add(detail);
                    } else if(timeDate>tommEnd) {
                        detail.setDateType(NEXT_SCHEDULE);
                        mListNextSchedule.add(detail);
                    }
                    Logs.d("Schedule From Date:"+detail.getForDate());
                }
//                HashSet<Integer> hashSet= new HashSet<>();
//                hashSet.add(0);

                if(mListToday!=null && mListToday.size()>0) {
                    StaffScheduleDetail sd= new StaffScheduleDetail();
                    sd.setLocation(TODAY);
                    mListDetails.add(sd);
                    mListDetails.addAll(mListToday);
                }
                if(mListTomorrow!=null && mListTomorrow.size()>0) {
                    StaffScheduleDetail sd= new StaffScheduleDetail();
                    sd.setLocation(TOMORROW);
                    mListDetails.add(sd);
                    mListDetails.addAll(mListTomorrow);
                }
                if(mListNextSchedule!=null && mListNextSchedule.size()>0) {
                    StaffScheduleDetail sd= new StaffScheduleDetail();
                    sd.setLocation(NEXT_SCHEDULE);
                    mListDetails.add(sd);
                    mListDetails.addAll(mListNextSchedule);
                }
//                setScrollList(mListDetails);

                setRecyclerView(mListDetails);
                if(mListNextSchedule.size()<=0){
                    showRetry(View.GONE, "No record found.");
                }
            }else {
                showRetry(View.VISIBLE,"No record found.");
                if(getActivity()!=null)
                    Toast.makeText(getActivity(), "No record found.", Toast.LENGTH_SHORT).show();
                if(getView()!=null)
                    ((TextView)getView().findViewById(R.id.txtRetry)).setText("No record found");
            }
        }catch (Exception e){
            e.printStackTrace();
            showRetry(View.VISIBLE, "No record found.");
            Toast.makeText(getActivity(), "No record found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showRetry(int isVib, String txtBtn)
    {
        if(getView()!=null) {
            ((TextView)getView().findViewById(R.id.txtRetry)).setText(txtBtn);
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

    private void setScrollList(List<StaffScheduleDetail> list)
    {
        if(linSchedule!=null && linSchedule.getChildCount()>0)
            linSchedule.removeAllViews();
        List<String> data = new ArrayList<>();
        data.add("Today");
        data.add("Tomorrow");
        data.add("Next Schedule");
        for (int i=0; i < data.size(); i++)
        {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View inflatedLayout = inflater.inflate(R.layout.row_staff_detail, null, false);
            ((TextView) inflatedLayout.findViewById(R.id.txtTitle)).setText(data.get(i));
            RecyclerView recyclerView = (RecyclerView) inflatedLayout.findViewById(R.id.recylerExpand);
            StaffDetailRecyclerViewAdapter adapter =new StaffDetailRecyclerViewAdapter(getActivity(), list);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
            linSchedule.addView(inflatedLayout);
        }
    }

    private void setRecyclerView(List<StaffScheduleDetail> list){
        HashMap<String, List<StaffScheduleDetail>> mMap= new HashMap<>();
        mMap.put("Today", list);
        mMap.put("Tomorrow", list);
        mMap.put("Next Schedule", list);
//        RecyclerViewRecyclerAdapter adapter = new RecyclerViewRecyclerAdapter(getActivity(), mMap);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(adapter);
        StaffDetailRecyclerViewAdapter adapter =new StaffDetailRecyclerViewAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

    }
}