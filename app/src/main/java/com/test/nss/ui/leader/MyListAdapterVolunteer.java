package com.test.nss.ui.leader;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.nss.R;
import com.test.nss.ediary;

import java.util.Collections;
import java.util.List;

public class MyListAdapterVolunteer extends RecyclerView.Adapter<MyListAdapterVolunteer.ViewHolder> {
    //private AdapterDataMain[] listdata;

    List<AdapterDataVolunteer> list = Collections.emptyList();
    Context mCon;

    public MyListAdapterVolunteer(List<AdapterDataVolunteer> list, Context mCon) {
        this.list = list;
        this.mCon = mCon;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_view_vol, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.dateVol.setText(list.get(position).getDate());
        holder.actVol.setText(list.get(position).getAct());
        holder.hoursVol.setText(list.get(position).getHours());
        holder.actIDVol.setText(list.get(position).getId());
        holder.actVolState.setText(list.get(position).getState());

        if (list.get(position).getState().equals("Approved")) {
            holder.approvedVol.setText(mCon.getString(R.string.yes));
            setColor(holder, ediary.green);
        } else if (list.get(position).getState().equals("LeaderDelete") || list.get(position).getState().equals("Deleted")) {
            holder.approvedVol.setText(mCon.getString(R.string.no));
            setColor(holder, ediary.red);
        } else if (list.get(position).getState().equals("LeaderModified")) {
            holder.approvedVol.setText(mCon.getString(R.string.yes));
            setColor(holder, ediary.kesar);
        } else {
            holder.approvedVol.setText(mCon.getString(R.string.no));
            holder.dateVol.setTextColor(mCon.getColor(R.color.blackGrey));
            holder.actVol.setTextColor(mCon.getColor(R.color.blackGrey));
            holder.hoursVol.setTextColor(mCon.getColor(R.color.blackGrey));
            holder.approvedVol.setTextColor(mCon.getColor(R.color.blackGrey));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setColor(ViewHolder holder, int color) {
        holder.dateVol.setTypeface(holder.dateVol.getTypeface(), Typeface.BOLD);
        holder.actVol.setTypeface(holder.actVol.getTypeface(), Typeface.BOLD);
        holder.hoursVol.setTypeface(holder.hoursVol.getTypeface(), Typeface.BOLD);
        holder.approvedVol.setTypeface(holder.approvedVol.getTypeface(), Typeface.BOLD);

        holder.dateVol.setTextColor(color);
        holder.actVol.setTextColor(color);
        holder.hoursVol.setTextColor(color);
        holder.approvedVol.setTextColor(color);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dateVol;
        public TextView actVol;
        public TextView hoursVol;
        public TextView actIDVol;
        public TextView approvedVol;
        public TextView actVolState;

        public LinearLayout linearLayoutVol;

        public ViewHolder(View itemView) {
            super(itemView);
            this.dateVol = itemView.findViewById(R.id.dateVol);
            this.actVol = itemView.findViewById(R.id.actVol);
            this.hoursVol = itemView.findViewById(R.id.hoursVol);
            this.actIDVol = itemView.findViewById(R.id.actIDVol);
            this.approvedVol = itemView.findViewById(R.id.approvedVol);
            this.actVolState = itemView.findViewById(R.id.actVolState);

            linearLayoutVol = itemView.findViewById(R.id.dataVolLinear);
        }
    }
}