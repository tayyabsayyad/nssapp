package com.test.nss.ui.info;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.nss.R;
import com.test.nss.ui.onClickInterface;

import java.util.Collections;
import java.util.List;

import static android.content.Context.VIBRATOR_SERVICE;

public class MyListAdapterInfo extends RecyclerView.Adapter<MyListAdapterInfo.ViewHolder> {
    //private AdapterDataAct[] listdata;
    List<AdapterDataInfo> list = Collections.emptyList();
    Context mCon;
    onClickInterface onClickInterface;

    public MyListAdapterInfo(List<AdapterDataInfo> list, Context mCon, onClickInterface onClickInterface) {
        this.list = list;
        this.mCon = mCon;
        this.onClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_view_info, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.infoName.setText(list.get(position).getInfoName());
        holder.infoDesc.setText(list.get(position).getInfoDesc());

        if (holder.getAdapterPosition() % 2 == 1)
            holder.rightChild.setBackgroundColor(mCon.getColor(R.color.pastel_cyan));
        else {
            holder.infoDesc.setTextColor(mCon.getColor(R.color.white));
            holder.infoName.setTextColor(mCon.getColor(R.color.white));
            holder.leftChild.setBackgroundColor(mCon.getColor(R.color.colorPrimary));
            holder.git.setImageResource(R.drawable.ic_git_24);
            holder.ig.setImageResource(R.drawable.ic_ig_24);
        }
        long[] pattern = {0, 100, 1000, 300, 200, 100, 300, 200, 100};

        holder.infoName.setOnClickListener(view -> {
            Vibrator v = (Vibrator) mCon.getSystemService(VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= 26) {
                ((Vibrator) mCon.getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(pattern, -1);
            }
        });
        holder.git.setOnClickListener(view -> {
            onClickInterface.setClick(list.get(position).getGitId());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView infoName;
        public TextView infoDesc;

        public LinearLayout leftChild;
        public LinearLayout rightChild;
        public ImageView git;
        public ImageView ig;

        public ViewHolder(View itemView) {
            super(itemView);
            this.infoName = itemView.findViewById(R.id.info_name);
            this.infoDesc = itemView.findViewById(R.id.info_details);

            leftChild = itemView.findViewById(R.id.leftChild);
            rightChild = itemView.findViewById(R.id.rightChild);

            git = itemView.findViewById(R.id.git_icon);
            ig = itemView.findViewById(R.id.ig_icon);
        }
    }
}
