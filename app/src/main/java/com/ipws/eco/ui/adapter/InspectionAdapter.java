package com.ipws.eco.ui.adapter;

/**
 * Created by Deepak on 12/25/17.
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.ipws.eco.R;
import com.ipws.eco.model.Inspection;
import java.util.ArrayList;
import java.util.List;

public class InspectionAdapter extends ArrayAdapter<Inspection> {

    List<Inspection> inspectionArrayList = new ArrayList<>();
    boolean isEdit;
    public InspectionAdapter(Context context, int textViewResourceId, List<Inspection> objects, boolean isEdit) {
        super(context, textViewResourceId, objects);
        inspectionArrayList = objects;
        this.isEdit = isEdit;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.expandable_list_item, null);
        TextView textView = (TextView) v.findViewById(R.id.textItem);
        TextView textViewSr = (TextView) v.findViewById(R.id.textSrNo);
        textView.setText(inspectionArrayList.get(position).getName());
        textViewSr.setText(position+1+"");

        CheckBox chkBox = (CheckBox) v.findViewById(R.id.chkBox);
        ImageView imgLeftArrow= (ImageView) v.findViewById(R.id.imgLeftArrow);
        if(isEdit){
            chkBox.setVisibility(View.VISIBLE);
            imgLeftArrow.setVisibility(View.GONE);
        }else
        {
            chkBox.setVisibility(View.GONE);
            imgLeftArrow.setVisibility(View.VISIBLE);
        }
        return v;

    }

}