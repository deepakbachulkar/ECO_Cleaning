package com.ipws.eco.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ipws.eco.R;
import com.ipws.eco.expand_list.ActionSlideExpandableListView;
import com.ipws.eco.model.Inspection;
import com.ipws.eco.model.SubInspection;
import com.ipws.eco.network.NetworkDAO;
import com.ipws.eco.ui.activity.BaseActivity;
import com.ipws.eco.ui.adapter.InspectionAdapter;
import com.ipws.eco.utils.AppPreference;
import com.ipws.eco.utils.Logs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InspectionListFragment extends BaseFragment implements View.OnClickListener
{
    public static String TAG= "inspection_list";
    private OnFragmentInteractionListener mListener;
    private TextView txtBtnEdit;
    private Spinner spinnerAccount, spinnerLocation;
    List<String>  listLocation;
    List<Integer> listLocationId;
    List<String>  listName;
    List<Integer> listId;
    private RelativeLayout relEdit;


    public InspectionListFragment() {  }

    public static InspectionListFragment newInstance(Bundle bundle) {
        InspectionListFragment fragment = new InspectionListFragment();
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
        View view= inflater.inflate(R.layout.fragment_inspection_list, container, false);
        init(view);
        return addToBaseFragment(view);
    }

    private void init(View view)
    {
        spinnerAccount = (Spinner) view.findViewById(R.id.spinnerAccount);
        spinnerLocation = (Spinner) view.findViewById(R.id.spinnerLocation);
        txtBtnEdit = (TextView) view.findViewById(R.id.txtBtEdit);
        relEdit = (RelativeLayout) view.findViewById(R.id.relEdit);

        txtBtnEdit.setOnClickListener(this);
        view.findViewById(R.id.txtBtSubmit).setOnClickListener(this);
        // get a reference to the listview, needed in order
        // to call setItemActionListener on it
        ActionSlideExpandableListView list = (ActionSlideExpandableListView)view.findViewById(R.id.expandListItems);
        ActionSlideExpandableListView listEdit = (ActionSlideExpandableListView)view.findViewById(R.id.expandListItemsEdit);
        // fill the list with data
        list.setAdapter(setAdapters(false));
        listEdit.setAdapter(setAdapters(true));
        // listen for events in the two buttons for every list item.
        // the 'position' var will tell which list item is clicked
        list.setItemActionListener(new ActionSlideExpandableListView.OnActionClickListener() {
            @Override
            public void onClick(View listView, View buttonview, int position) {
//                String actionName = "";
//                if(buttonview.getId()==R.id.buttonA) {
//                    actionName = "buttonA";
//                } else {
//                    actionName = "ButtonB";
//                }
            }
            // note that we also add 1 or more ids to the setItemActionListener
            // this is needed in order for the listview to discover the buttons
        }, R.id.text1, R.id.text3);
    }

    private void initListener(){ }

    @Override
    public void onStart() {
        super.onStart();
        customize();
        requestAccount();
        txtBtnEdit.setTypeface(((BaseActivity)getActivity()).getTypefaceFontAwesome());
    }

    private void customize(){
        ((BaseActivity)getActivity()).setTitleIcon(getResources().getString(R.string.fa_icon_click), getResources().getString(R.string.insp_check));
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
        switch (view.getId())
        {
            case R.id.txtBtEdit:
                relEdit.setVisibility(View.VISIBLE);
                buildDummyData2();
                break;
            case R.id.txtBtSubmit:
                relEdit.setVisibility(View.GONE);
                break;
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
                requestLocation(listId.get(i));
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
        NetworkDAO.getInstance(getActivity()).login(getActivity(), parameter, success1, error2);
    }

    Response.Listener success1= new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            hideProgressBar();
            Logs.d("Network response :"+ response);
            parseLocation(response);
        }
    };

    Response.ErrorListener error2= new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            hideProgressBar();
            Logs.d("Network Error: " + error.getMessage());
            error.printStackTrace();
            Toast.makeText(getActivity(), "Unable to connect server.", Toast.LENGTH_SHORT).show();
        }
    };


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
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    public ListAdapter setAdapters(boolean isEdit)
    {
        final int SIZE = 5;
        List<SubInspection> listSubInspections = new ArrayList<>();
        listSubInspections.add(new SubInspection( 1, "Sub Inspection 1",  "", false));

        List<Inspection> listInspections = new ArrayList<>();
        listInspections.add(new Inspection(1, "Inspection 1","","", false, listSubInspections));
        listInspections.add(new Inspection(1, "Inspection 2","","", false, listSubInspections));
        listInspections.add(new Inspection(1, "Inspection 3","","", false, listSubInspections));
        listInspections.add(new Inspection(1, "Inspection 4","","", false, listSubInspections));

        InspectionAdapter inspAdapter=new InspectionAdapter(getActivity(),
                R.layout.expandable_list_item, listInspections, isEdit);
        return  inspAdapter;
    }

    public ListAdapter buildDummyData2() {
        final int SIZE = 5;
        ArrayList<Inspection> listInspections = new ArrayList<>();

        return new ArrayAdapter<Inspection>(
             getActivity(),   R.layout.expandable_list_item,
                R.id.text,
                listInspections
        );
    }
}