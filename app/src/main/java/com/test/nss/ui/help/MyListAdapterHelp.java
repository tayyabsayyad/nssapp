package com.test.nss.ui.help;

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

public class MyListAdapterHelp extends RecyclerView.Adapter<MyListAdapterHelp.ViewHolder> {
    //private AdapterDataAct[] listdata;
    List<AdapterDataHelp> list = Collections.emptyList();
    Context mCon;
    onClickInterface onClickInterface;

    public MyListAdapterHelp(List<AdapterDataHelp> list, Context mCon) {
        this.list = list;
        this.mCon = mCon;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_view_leader_help, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.leadName.setText(list.get(position).getLeadName());
        holder.leadEmail.setText(list.get(position).getLeadEmail());
        holder.leadCont.setText(list.get(position).getLeadCont());
        holder.clgNameLead.setText(list.get(position).getLeadClg());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView leadEmail;
        public TextView leadName;
        public TextView clgNameLead;
        public TextView leadCont;

        public CardView actDataCard;

        public ViewHolder(View itemView) {
            super(itemView);
            this.leadEmail = itemView.findViewById(R.id.lead_email);
            this.leadName = itemView.findViewById(R.id.lead_name);
            this.clgNameLead = itemView.findViewById(R.id.clgNameLead);
            this.leadCont = itemView.findViewById(R.id.lead_contact);

            actDataCard = itemView.findViewById(R.id.leaderHelpDataCard);
        }
    }
}
