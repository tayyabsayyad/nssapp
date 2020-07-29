package com.test.nss.ui.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.nss.R;

import java.util.Collections;
import java.util.List;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
    //private AdapterDataMain[] listdata;

    List<AdapterDataMain> list = Collections.emptyList();
    Context mCon;

    public MyListAdapter(List<AdapterDataMain> list, Context mCon) {
        this.list = list;
        this.mCon = mCon;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_view_main, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.date.setText(list.get(position).getDate());
        holder.act.setText(list.get(position).getAct());
        holder.hours.setText(list.get(position).getHours());
        holder.actId.setText(list.get(position).getId());
        if (list.get(position).isApproved() == 1) {
            holder.date.setTypeface(holder.date.getTypeface(), Typeface.BOLD);
            holder.act.setTypeface(holder.act.getTypeface(), Typeface.BOLD);
            holder.hours.setTypeface(holder.hours.getTypeface(), Typeface.BOLD);

            holder.date.setTextColor(Color.parseColor("#008f00"));
            holder.act.setTextColor(Color.parseColor("#008f00"));
            holder.hours.setTextColor(Color.parseColor("#008f00"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void insert(int position, AdapterDataMain data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(AdapterDataMain data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView act;
        public TextView hours;
        public TextView actId;

        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.date = itemView.findViewById(R.id.date);
            this.act = itemView.findViewById(R.id.act);
            this.hours = itemView.findViewById(R.id.hours);
            this.actId = itemView.findViewById(R.id.actID);

            linearLayout = itemView.findViewById(R.id.dataLinear);
        }
    }
}