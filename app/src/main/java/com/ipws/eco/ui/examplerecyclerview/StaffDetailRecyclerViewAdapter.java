package com.ipws.eco.ui.examplerecyclerview;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ipws.eco.R;
import com.ipws.eco.model.StaffScheduleDetail;
import com.ipws.eco.ui.activity.BaseActivity;
import com.ipws.eco.ui.fragment.ScheduleFragment;
import com.ipws.eco.utils.AppDates;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link
 */
public class StaffDetailRecyclerViewAdapter extends RecyclerView.Adapter<StaffDetailRecyclerViewAdapter.ViewHolder> {

    private final List<StaffScheduleDetail> mValues;
    private Context mContext;
    private String title;

    public StaffDetailRecyclerViewAdapter(Context context, List<StaffScheduleDetail> items) {
        mContext = context;
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_shedule_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.mItem = mValues.get(position);
        if(holder.mItem.getChechInTime()==null || holder.mItem.getChechInTime().equals("") ){
            holder.linTitle.setVisibility(View.VISIBLE);
            holder.txtTilte.setText(holder.mItem.getLocation());
            holder.relCardView.setVisibility(View.GONE);
            title= holder.mItem.getLocation();
            if(title.equalsIgnoreCase(ScheduleFragment.TODAY)){
                holder.divider.setVisibility(View.GONE);
            }else{
                holder.divider.setVisibility(View.VISIBLE);
            }
        }else{
            holder.linTitle.setVisibility(View.GONE);
            holder.relCardView.setVisibility(View.VISIBLE);
            holder.txtDate.setText(AppDates.with().stringToString(holder.mItem.getForDate(), "yyyy-MM-dd","MMM dd"));
//            if(!holder.mItem.getChechInTime().equals("NA"))
//                holder.txtTimeFromTo.setText(AppDates.with().timeCanvert(holder.mItem.getChechInTime()) +" - "+ AppDates.with().timeCanvert(holder.mItem.getCheckOutTime()));
//            else
            holder.txtTimeFromTo.setText(holder.mItem.getShiftTime());
            holder.txtAddress1.setText(holder.mItem.getLocation());
            holder.txtAddress2.setText(holder.mItem.getAddress());
            if(holder.mItem.getDateType().equalsIgnoreCase(ScheduleFragment.TODAY)){
                holder.txtImgDate.setTextColor(mContext.getResources().getColor(R.color.col_app1));
                holder.linSchedule.setBackgroundResource(R.color.col_schedule_today);
                holder.txtTimeFromTo.setTextColor(mContext.getResources().getColor(R.color.white));
                holder.txtDate.setTextColor(mContext.getResources().getColor(R.color.white));
                holder.txtAddress1.setTextColor(mContext.getResources().getColor(R.color.white_three_color));
                holder.txtAddress2.setTextColor(mContext.getResources().getColor(R.color.white_three_color));
            }else if(holder.mItem.getDateType().equalsIgnoreCase(ScheduleFragment.TOMORROW)) {
                holder.txtImgDate.setTextColor(mContext.getResources().getColor(R.color.col_app1));
                holder.linSchedule.setBackgroundResource(R.color.white);
                holder.txtTimeFromTo.setTextColor(mContext.getResources().getColor(R.color.thame_color));
                holder.txtDate.setTextColor(mContext.getResources().getColor(R.color.black_100_percent));
                holder.txtAddress1.setTextColor(mContext.getResources().getColor(R.color.black_100_percent));
                holder.txtAddress2.setTextColor(mContext.getResources().getColor(R.color.grey));
            }else{
                holder.txtImgDate.setTextColor(mContext.getResources().getColor(R.color.grey));
                holder.linSchedule.setBackgroundResource(R.color.white);
                holder.txtTimeFromTo.setTextColor(mContext.getResources().getColor(R.color.thame_color));
                holder.txtAddress1.setTextColor(mContext.getResources().getColor(R.color.black_100_percent));
                holder.txtAddress2.setTextColor(mContext.getResources().getColor(R.color.grey));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView, divider;
        public StaffScheduleDetail mItem;
        public LinearLayout linSchedule;
        public RelativeLayout relCardView;
        public LinearLayout linTitle;
        public TextView txtTilte, txtDate, txtTimeFromTo, txtAddress1, txtAddress2, txtImgDate;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            divider= view.findViewById(R.id.divider);
            linSchedule = (LinearLayout)view.findViewById(R.id.linSchedule);
            txtTilte = (TextView) view.findViewById(R.id.txtTitle);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
            txtTimeFromTo = (TextView) view.findViewById(R.id.txtTimeFromTo);
            txtAddress1 = (TextView) view.findViewById(R.id.txtAddress1);
            txtAddress2 = (TextView) view.findViewById(R.id.txtAddress2);
            relCardView = (RelativeLayout) view.findViewById(R.id.relCardView);
            linTitle = (LinearLayout) view.findViewById(R.id.linTitle);
            txtImgDate = (TextView) view.findViewById(R.id.txtImgDate);
            txtImgDate.setTypeface(((BaseActivity)mContext).getTypefaceFontAwesome());
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}