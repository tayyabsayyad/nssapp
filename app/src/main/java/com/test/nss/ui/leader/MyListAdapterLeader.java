package com.test.nss.ui.leader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.test.nss.R;
import com.test.nss.ui.onClickInterface;

import java.util.Collections;
import java.util.List;

public class MyListAdapterLeader extends RecyclerView.Adapter<MyListAdapterLeader.ViewHolder> {
    //private AdapterDataAct[] listdata;
    List<AdapterDataLeader> list = Collections.emptyList();
    Context mCon;
    onClickInterface onClickInterface;

    public MyListAdapterLeader(List<AdapterDataLeader> list, Context mCon, onClickInterface onClickInterface) {
        this.list = list;
        this.mCon = mCon;
        this.onClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_view_leader, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.volunteerVec.setText(list.get(position).getvolVec());
        holder.volunteerVec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickInterface.setClick(list.get(position).getvolVec());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView volunteerVec;

        public CardView actDataCard;

        public ViewHolder(View itemView) {
            super(itemView);
            this.volunteerVec = itemView.findViewById(R.id.vol_vec);

            actDataCard = itemView.findViewById(R.id.leaderDataCard);
        }
    }
}
