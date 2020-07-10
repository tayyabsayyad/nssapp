package com.test.nss.ui.main;

import android.content.Context;
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

public class MyListAdapterAreaFy extends RecyclerView.Adapter<MyListAdapterAreaFy.ViewHolder> {
    //private AreaListDataFy[] listdata;
    List<AreaListDataFy> list = Collections.emptyList();
    Context mCon;

    public MyListAdapterAreaFy(List<AreaListDataFy> list, Context mCon) {
        this.list = list;
        this.mCon = mCon;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_view_area, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AreaListDataFy areaListDataFy = list.get(position);
        holder.date.setText(list.get(position).getDate());
        holder.act.setText(list.get(position).getAct());
        holder.hours.setText(list.get(position).getHours());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void insert(int position, AreaListDataFy data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(AreaListDataFy data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView act;
        public TextView hours;

        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.date = (TextView) itemView.findViewById(R.id.date_area);
            this.act = (TextView) itemView.findViewById(R.id.act_area);
            this.hours = (TextView) itemView.findViewById(R.id.hours_area);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.areaLinear);
        }
    }
}
