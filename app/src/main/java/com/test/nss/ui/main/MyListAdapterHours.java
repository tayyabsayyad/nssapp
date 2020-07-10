package com.test.nss.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.nss.R;

public class MyListAdapterHours extends RecyclerView.Adapter<MyListAdapterHours.ViewHolder> {
    private HoursListData[] listdata;

    public MyListAdapterHours(HoursListData[] listdata) {
        this.listdata = listdata;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_view_hours, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HoursListData hoursListData = listdata[position];
        holder.date.setText(listdata[position].getDate());
        holder.act.setText(listdata[position].getAct());
        holder.hours.setText(listdata[position].getHours());
    }

    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView act;
        public TextView hours;

        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.date = (TextView) itemView.findViewById(R.id.date_hours);
            this.act = (TextView) itemView.findViewById(R.id.act_hours);
            this.hours = (TextView) itemView.findViewById(R.id.hours_hours);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.hoursLinear);
        }
    }
}
