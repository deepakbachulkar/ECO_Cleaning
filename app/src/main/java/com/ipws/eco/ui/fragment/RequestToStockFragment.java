package com.ipws.eco.ui.fragment;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ipws.eco.R;
import com.ipws.eco.custom.view.CalendarListener;
import com.ipws.eco.custom.view.CustomCalendarView;
import com.ipws.eco.model.Items;
import com.ipws.eco.network.NetworkDAO;
import com.ipws.eco.ui.activity.BaseActivity;
import com.ipws.eco.ui.controls.AppDialogs;
import com.ipws.eco.ui.examplerecyclerview.RecyclerViewRecyclerAdapter;
import com.ipws.eco.ui.expandablelayout.Utils;
import com.ipws.eco.utils.AppPreference;
import com.ipws.eco.utils.AppUtils;
import com.ipws.eco.utils.Logs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RequestToStockFragment extends BaseFragment implements View.OnClickListener
{
    public static String TAG= "request_stack";
    private OnFragmentInteractionListener mListener;
    private Spinner spinnerAccount, spinnerLocation, spinnerRCC2;
    List<String>  listLocation;
    List<Integer> listLocationId;
    List<String>  listName;
    List<Integer> listId;
    List<Items> listItemsCapital, listItemsConsumable, listSelectedItems = new ArrayList<>();
    List<String> listItemsCapitalStr, listItemsConsumableStr;
    HashMap<Integer, Items> mapSelectStock = new HashMap<>();
    LinearLayout layoutItems;
    boolean isConsumable = true;
    TextView txtCapital, txtConsumable, txtBtnAdd;
    RecyclerView recylerViewItems;
    View mView;

    public RequestToStockFragment() {
    }

    public static RequestToStockFragment newInstance(Bundle bundle) {
        RequestToStockFragment fragment = new RequestToStockFragment();
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
        View view= inflater.inflate(R.layout.fragment_request_to_stock, container, false);
        init(view);
        mView =view;
        return addToBaseFragment(view);
    }

    private void init(View view)
    {
        spinnerAccount = (Spinner) view.findViewById(R.id.spinnerAccount);
        spinnerLocation = (Spinner) view.findViewById(R.id.spinnerLocation);
        spinnerAccount = (Spinner) view.findViewById(R.id.spinnerAccount);

        txtBtnAdd = (TextView) view.findViewById(R.id.txtBtnAdd);
        layoutItems = (LinearLayout) view.findViewById(R.id.linItems);
        txtCapital = (TextView) view.findViewById(R.id.txtCapital);
        txtConsumable = (TextView) view.findViewById(R.id.txtConsumable);
        spinnerRCC2 = (Spinner) view.findViewById(R.id.spinnerRCC2);
        txtCapital.setOnClickListener(this);
        txtConsumable.setOnClickListener(this);
        txtBtnAdd.setOnClickListener(this);
        view.findViewById(R.id.txtBtReqStock).setOnClickListener(this);
        txtConsumable.setPadding((int)AppUtils.convertToPixels(getActivity(), 10),
                (int)AppUtils.convertToPixels(getActivity(), 10),
                (int)AppUtils.convertToPixels(getActivity(), 10),
                (int)AppUtils.convertToPixels(getActivity(), 10));
        txtCapital.setPadding((int)AppUtils.convertToPixels(getActivity(), 8),
                (int)AppUtils.convertToPixels(getActivity(), 8),
                (int)AppUtils.convertToPixels(getActivity(), 8),
                (int)AppUtils.convertToPixels(getActivity(), 8));
        recylerViewItems = (RecyclerView) view.findViewById(R.id.recylerViewItems);

    }

    private void initListener(){ }

    @Override
    public void onStart() {
        super.onStart();
        customize();
        requestAccount();
        requestConsumableCapital();
        txtBtnAdd.setTypeface(((BaseActivity)getActivity()).getTypefaceFontAwesome());
    }

    private void customize(){
        ((BaseActivity)getActivity()).setTitleIcon(getResources().getString(R.string.fa_icon_req_stock), getResources().getString(R.string.request_stock));
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
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.txtCapital:
                isConsumable = false;
                txtCapital.setBackgroundResource(R.color.col_app1);
                txtConsumable.setBackgroundResource(R.color.grey_light);
               // itemsShowConsumable(listItemsCapitalStr);
                txtCapital.setPadding((int)AppUtils.convertToPixels(getActivity(), 10),
                        (int)AppUtils.convertToPixels(getActivity(), 10),
                        (int)AppUtils.convertToPixels(getActivity(), 10),
                        (int)AppUtils.convertToPixels(getActivity(), 10));
                txtConsumable.setPadding((int)AppUtils.convertToPixels(getActivity(), 8),
                        (int)AppUtils.convertToPixels(getActivity(), 8),
                        (int)AppUtils.convertToPixels(getActivity(), 8),
                        (int)AppUtils.convertToPixels(getActivity(), 8));
                spinnerStock(spinnerRCC2, listItemsCapitalStr, 0);
                break;
            case R.id.txtConsumable:
                isConsumable = true;
                        txtCapital.setBackgroundResource(R.color.grey_light);
                txtConsumable.setBackgroundResource(R.color.col_app1);
              //  itemsShowConsumable(listItemsConsumableStr);
                txtConsumable.setPadding((int)AppUtils.convertToPixels(getActivity(), 10),
                        (int)AppUtils.convertToPixels(getActivity(), 10),
                        (int)AppUtils.convertToPixels(getActivity(), 10),
                        (int)AppUtils.convertToPixels(getActivity(), 10));
                txtCapital.setPadding((int)AppUtils.convertToPixels(getActivity(), 8),
                        (int)AppUtils.convertToPixels(getActivity(), 8),
                        (int)AppUtils.convertToPixels(getActivity(), 8),
                        (int)AppUtils.convertToPixels(getActivity(), 8));
                spinnerStock(spinnerRCC2, listItemsConsumableStr, 0);
                break;
            case R.id.txtBtReqStock:
                if(spinnerAccount.getSelectedItemPosition()<=0){
                    Toast.makeText(getActivity(),"Please Select Account..", Toast.LENGTH_SHORT).show();
                }
                if(listSelectedItems.size()>0)
                    requestToStock();
                else{
                    Toast.makeText(getActivity(),"Please add stock..", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txtBtnAdd:
                int sel = spinnerRCC2.getSelectedItemPosition();
                if(((EditText)mView.findViewById(R.id.edtItems2)).getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Please enter count items.", Toast.LENGTH_SHORT).show();
                    return;
                }else
                {
                    String count = ((EditText)mView.findViewById(R.id.edtItems2)).getText().toString().trim();

                    if(Integer.parseInt(count)<=0)
                    {
                        Toast.makeText(getActivity(), "Please enter items.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(isConsumable){
                    Items items = listItemsConsumable.get(sel);
                    items.setSelPos(spinnerRCC2.getSelectedItemPosition());
                    items.setConsumable(isConsumable);
                    items.setCurrentStatus(Integer.parseInt(((EditText)mView.findViewById(R.id.edtItems2)).getText().toString().trim()));
                    listSelectedItems.add(items);
                    Logs.d("Item Pos Get spn:"+ spinnerRCC2.getSelectedItemPosition());
//                    mapSelectStock.put(mapSelectStock.size(), items);
//                    listSelectedItems.add(items);
                }else{
                    Items items = listItemsCapital.get(sel);
                    items.setConsumable(isConsumable);
                    items.setSelPos(spinnerRCC2.getSelectedItemPosition());
                    Logs.d("Item Pos Get spn :"+ spinnerRCC2.getSelectedItemPosition());

                    items.setCurrentStatus(Integer.parseInt(((EditText)mView.findViewById(R.id.edtItems2)).getText().toString().trim()));
                    listSelectedItems.add(items);
                    Logs.d("Item Pos :"+ items.getSelPos());
//                    mapSelectStock.put(mapSelectStock.size(), items);
//                    listSelectedItems.add(items);
                }
                Logs.d("Items :"+listSelectedItems);
                itemsAdd();
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
                listId.add(0);
                listName.add("Select Account");
                JSONArray array = object.getJSONArray("Msg");
                for (int i=0; i<array.length(); i++)
                {
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
                if(i>0)
                    requestLocation(listId.get(i));
                else{
                    listLocationId = new ArrayList<>();
                    listLocation = new ArrayList<>();
                    listLocationId.add(0);
                    listLocation.add("Select Location");
                    spinnerLocation();
                }
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

    private void requestConsumableCapital()
    {
        showProgressBar();
        String parameter = "TxnType=PITEMS&LOGINNAME1="+AppPreference.getInstance(getActivity()).getLoginName()+"&Msg=USERID:"+AppPreference.getInstance(getActivity()).getUserId()+"|CRITERIA:NODELETE|TDTSM:2017-06-20 12*30*38.287";
        Logs.d("Network Url: "+parameter);
        parameter = parameter.replace(" ", "%20");
        NetworkDAO.getInstance(getActivity()).login(getActivity(), parameter, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideProgressBar();
                Logs.d("Network response :"+ response);
                parseItems(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar();
                Logs.d("Network Error: " + error.getMessage());
                error.printStackTrace();
                Toast.makeText(getActivity(), "Unable to connect server.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void parseItems(String data)
    {
        try
        {
            JSONObject object = new JSONObject(data);
            if(object.has("status") && object.getString("status").equals("00"))
            {
                listItemsCapital = new ArrayList<>();
                listItemsConsumable = new ArrayList<>();
                listItemsCapitalStr = new ArrayList<>();
                listItemsConsumableStr = new ArrayList<>();
                JSONArray array = object.getJSONArray("Msg");
                for (int i=0; i<array.length(); i++)
                {
                    JSONObject j= array.getJSONObject(i);
                    Items items = new Items();
                    items.setId(j.getInt("id"));
                    items.setName(j.getString("item_name"));
                    items.setType(j.getInt("item_type"));
                    items.setTypeName(j.getString("item_type_name"));
                    if(j.getString("item_description")!=null  && !j.getString("unit_name").equals("null")) {
                        items.setDesc(j.getString("item_description"));
                    }
                    if(j.getString("unit_id")!=null  && !j.getString("unit_name").equals("null")) {
                        items.setUniteId(j.getInt("unit_id"));
                    }
                    items.setCurrentStatus(j.getInt("current_status"));
                    if(j.getString("unit_name")!=null && !j.getString("unit_name").equals("null")) {
                        items.setUnitName(j.getString("unit_name"));
                    }
                    Log.d("Date","D:"+j.getString("item_name"));
                    if(items.getTypeName().equals("Capital")) {
                        listItemsCapital.add(items);
                        listItemsCapitalStr.add(j.getString("item_name"));
                    }
                    else {
                        listItemsConsumable.add(items);
                        listItemsConsumableStr.add(j.getString("item_name"));
                    }
                }
//                 listSelectedItems.add(listItemsConsumable.get(0));
//                itemsShowConsumable(listItemsConsumableStr, 0);

                spinnerStock(spinnerRCC2, listItemsConsumableStr, 0);
            }else {
                Toast.makeText(getActivity(), object.getJSONObject("errormsg").getString("error") , Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), getResources().getString(R.string.error_server_something_worng), Toast.LENGTH_SHORT).show();
        }
    }

    int position=0;
    private void itemsShowConsumable(final List<String> items, int selPos)
    {
//        if(layoutItems!=null && layoutItems.getChildCount()>0)
//            layoutItems.removeAllViews();
        if(items!=null)
        {
          //  for (int i=0; i<items.size(); i++)
            {
                View view = getActivity().getLayoutInflater().inflate(R.layout.row_items_request_stock,null);
                ((TextView)view.findViewById(R.id.txtRemove)).setTypeface(((BaseActivity)getActivity()).getTypefaceFontAwesome());

                spinnerStock( ((Spinner)view.findViewById(R.id.spinnerReqCC)), items, selPos);
                ((EditText)view.findViewById(R.id.edtItems)).setText("0");
                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if(!editable.toString().equals("")){
                            int count= Integer.parseInt(editable.toString());
                            if(isConsumable){
                                listItemsConsumable.get(position).setCurrentStatus(count);
                            }else {
                                listItemsCapital.get(position).setCurrentStatus(count);
                            }
                        }
                    }
                };

                ((EditText)view.findViewById(R.id.edtItems)).setTag(position);
                ((EditText)view.findViewById(R.id.edtItems)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        position = (int) view.getTag();
                    }
                });

                 ((EditText)view.findViewById(R.id.edtItems)).addTextChangedListener(textWatcher);
                 view.findViewById(R.id.txtRemove).setTag(listSelectedItems.size()  );
                 view.findViewById(R.id.txtRemove).setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                     int pos = (int)view.getTag();
                         Log.d("","Tag:"+pos);
                         if(layoutItems.getChildCount()>pos) {
                             layoutItems.removeViewAt(pos);
                             listSelectedItems.remove(pos);
                         }
                         for(int j=0; j < listSelectedItems.size(); j++){
                             itemsShowConsumable(items, listSelectedItems.get(pos).getSelPos());
                         }
                     }
                 });
                layoutItems.addView(view);
            }
        }
    }

    private void spinnerStock(Spinner spinner, List<String> items, int selPos)
    {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(selPos);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0)
                { }
                else{}
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void itemsAdd()
    {
        if(layoutItems!=null && layoutItems.getChildCount()>0)
            layoutItems.removeAllViews();
//        if(items!=null)
        {
            for (int k= 0; k < listSelectedItems.size(); k++)
            {
                final Items item= listSelectedItems.get(k);
                final int p = k;
                Logs.d("Item Pos :"+p);
                Logs.d("Item Pos Get:"+p+":"+ listSelectedItems.get(p).getSelPos());
                View view = getActivity().getLayoutInflater().inflate(R.layout.row_items_request_stock_sel,null);
                ((TextView)view.findViewById(R.id.spinnerItems)).setText(item.getName());
                if(item.isConsumable())
                {
//                   ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listItemsConsumableStr);
//                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////                    ((TextView)view.findViewById(R.id.spinnerItemsTextViewTextView)).setAdapter(dataAdapter);
////                    ((TextView)view.findViewById(R.id.spinnerItems)).setSelection(listSelectedItems.get(p).getSelPos());
////                    ((Spinner)view.findViewById(R.id.spinnerItems)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////                        @Override
////                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//////                            Items items = listSelectedItems.get(p);
//////                            items.setSelPos(i);
//////                            items.setName(listItemsConsumableStr.get(i));
//////                            listSelectedItems.remove(p);
//////                            listSelectedItems.add(p, item);
////                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> adapterView) {
//                        }
//                    });
                } else {
//                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listItemsCapitalStr);
//                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    ((Spinner)view.findViewById(R.id.spinnerItems)).setAdapter(dataAdapter);
//                    ((Spinner)view.findViewById(R.id.spinnerItems)).setSelection(listSelectedItems.get(p).getSelPos());
//                    ((Spinner)view.findViewById(R.id.spinnerItems)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
////                            Items items = listSelectedItems.get(p);
////                            items.setSelPos(i);
////                            items.setName(listItemsCapitalStr.get(i));
////                            listSelectedItems.remove(p);
////                            listSelectedItems.add(p, item);
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> adapterView) {
//                        }
//                    });

                }

                ((TextView)view.findViewById(R.id.txtRemove)).setTypeface(((BaseActivity)getActivity()).getTypefaceFontAwesome());
                ((EditText)view.findViewById(R.id.edtCount)).setText(item.getCurrentStatus()+"");
                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                    @Override
                    public void afterTextChanged(Editable editable) {
                        if(!editable.toString().equals("")){
                            int count= Integer.parseInt(editable.toString());
                            if(isConsumable){
                                listItemsConsumable.get(p).setCurrentStatus(count);
                            }else {
                                listItemsCapital.get(p).setCurrentStatus(count);
                            }
                        }
                    }
                };

//            ((EditText)view.findViewById(R.id.edtItems)).setTag(position);
//            ((EditText)view.findViewById(R.id.edtItems)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    position = (int) view.getTag();
//                }
//            });
                ((EditText)view.findViewById(R.id.edtCount)).addTextChangedListener(textWatcher);
                view.findViewById(R.id.txtRemove).setTag(p);
                view.findViewById(R.id.txtRemove).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = (int)view.getTag();
                        if(layoutItems.getChildCount()>pos) {
                            listSelectedItems.remove(pos);
                        }
                        Logs.d("Item deep: "+pos);
                        itemsAdd();
                    }
                });
                layoutItems.addView(view);
            }
        }
    }

    private void itemsAddList(final List<Items> items)
    {
        if(layoutItems!=null && layoutItems.getChildCount()>0)
            layoutItems.removeAllViews();

        for (Map.Entry<Integer, Items> entry : mapSelectStock.entrySet())
        {
            System.out.println(entry.getKey() + "/" + entry.getValue());
            final int p = entry.getKey();
            Items item= mapSelectStock.get(p);
            View view = getActivity().getLayoutInflater().inflate(R.layout.row_items_request_stock_sel,null);
            ((TextView)view.findViewById(R.id.txtTitle)).setText(item.getName());
            ((TextView)view.findViewById(R.id.txtRemove)).setTypeface(((BaseActivity)getActivity()).getTypefaceFontAwesome());
            ((EditText)view.findViewById(R.id.edtCount)).setText(item.getCurrentStatus()+"");
            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(!editable.toString().equals("")){
                        int count= Integer.parseInt(editable.toString());
                        if(isConsumable){
                            listItemsConsumable.get(p).setCurrentStatus(count);
                        }else {
                            listItemsCapital.get(p).setCurrentStatus(count);
                        }
                    }
                }
            };

//            ((EditText)view.findViewById(R.id.edtItems)).setTag(position);
//            ((EditText)view.findViewById(R.id.edtItems)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    position = (int) view.getTag();
//                }
//            });
            ((EditText)view.findViewById(R.id.edtCount)).addTextChangedListener(textWatcher);
            view.findViewById(R.id.txtRemove).setTag(p);
            view.findViewById(R.id.txtRemove).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int)view.getTag();
                    if(layoutItems.getChildCount()>pos) {
                        mapSelectStock.remove(pos);
                    }
                    itemsAdd();
                }
            });
            layoutItems.addView(view);
        }



    }


    private void requestToStock()
    {
        String jAO="";
        JSONArray jsonArray= new JSONArray();
        try {
            for (int q=0; q< listSelectedItems.size(); q++){
                JSONObject object = new JSONObject();
                object.put("item_id", listSelectedItems.get(q).getId()+"");
                object.put("item_count", listSelectedItems.get(q).getCurrentStatus()+"");
                jsonArray.put(object);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        jAO = jsonArray.toString();
        showProgressBar();
        String parameter = "TxnType=REQUESTSTOCK&LOGINNAME1="+AppPreference.getInstance(getActivity()).getLoginName()+"&ITEMLIST="+jAO+"&msg=USERID:1001|LOCATIONID:1048|FORDATE:2017-12-14|COMMENT:jlkjljl|ITEMTYPE:Consumable";
        Logs.d("Network Url: "+parameter);
        parameter = parameter.replace(" ", "%20");
        NetworkDAO.getInstance(getActivity()).login(getActivity(), parameter, successRS, errorRS);
    }

    private void parseRS(String res){

    }


    Response.Listener successRS= new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            hideProgressBar();
            Logs.d("Network response :"+ response);
//            if(response.equals(""))
            {
                Toast.makeText(getActivity(), "Request Stock successfully", Toast.LENGTH_SHORT).show();
                DashBroadFragment fragment = new DashBroadFragment();
                String tag = DashBroadFragment.TAG;
                ((BaseActivity)getActivity()).onOpenFragment(fragment);
            }
            parseRS(response);
        }
    };

    Response.ErrorListener errorRS= new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            hideProgressBar();
            Logs.d("Network Error: " + error.getMessage());
            error.printStackTrace();
            Toast.makeText(getActivity(), "Unable to connect server.", Toast.LENGTH_SHORT).show();
        }
    };
    //http://localhost:6985/API/PrepaidAPI.aspx?TxnType=REQUESTSTOCK&LOGINNAME1=patil@ipwebsoft.com&ITEMLIST=[{"item_id":"1","item_count" : "2"},{"item_id":"2","item_count" : "2"}]&msg=USERID:1001|LOCATIONID:1048|FORDATE:2017-12-14|COMMENT:jlkjljl|ITEMTYPE:Consumable
}