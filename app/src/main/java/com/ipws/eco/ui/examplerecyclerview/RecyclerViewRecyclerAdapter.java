package com.ipws.eco.ui.examplerecyclerview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ipws.eco.R;
import com.ipws.eco.model.StaffScheduleDetail;
import com.ipws.eco.ui.expandablelayout.ExpandableLinearLayout;
import com.ipws.eco.ui.expandablelayout.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import jp.android.aakira.sample.expandablelayout.R;

public class RecyclerViewRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewRecyclerAdapter.ViewHolder> {

    private final List<String> data =new ArrayList<>();
    private Context context;
    private SparseBooleanArray expandState = new SparseBooleanArray();
    private int mSelPosition=0;
    private HashMap<String, List<StaffScheduleDetail>> mMap= new HashMap<>();

    public RecyclerViewRecyclerAdapter(Context context,
                                       HashMap<String, List<StaffScheduleDetail>> map) {
        data.add("Today");
        data.add("Tomorrow");
        data.add("Next Schedule");
        this.context =context;
        this.mMap= map;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.row_staff_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String string = data.get(position);
        holder.txtTitle.setText(string);

        if(mMap.containsKey(string) && mMap.get(string)!=null)
        {
            StaffDetailRecyclerViewAdapter adapter = new StaffDetailRecyclerViewAdapter(context, mMap.get(string));
            holder.recylerExpand.setLayoutManager(new LinearLayoutManager(context));
            holder.recylerExpand.setAdapter(adapter);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle;
        private RecyclerView recylerExpand;

        public ViewHolder(View v) {
            super(v);
            txtTitle = (TextView) v.findViewById(R.id.txtTitle);
            recylerExpand = (RecyclerView) v.findViewById(R.id.recylerExpand);
        }
    }

}