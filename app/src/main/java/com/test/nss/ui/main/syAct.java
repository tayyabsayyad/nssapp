package com.test.nss.ui.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.nss.R;

import java.util.ArrayList;
import java.util.List;

import static com.test.nss.ediary.blackish;
import static com.test.nss.ediary.primaryCol;
import static com.test.nss.ediary.transparent;

public class syAct extends Fragment {

    View doraemon;
    Button univ;
    Button area;
    Button clg;

    LinearLayout mainSy;
    Context mContext;

    RecyclerView univRecSy;
    RecyclerView areaRecSy;
    RecyclerView hoursRecSy;

    List<AdapterDataMain> univListDataSy;
    List<AdapterDataMain> adapterDataSy;
    List<AdapterDataMain> hoursListDataSy;

    Button add;
    int whichAct = -1;
    LinearLayout fragSy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        doraemon = inflater.inflate(R.layout.fragment_sy_act, container, false);
        mContext = requireContext();

        univ = doraemon.findViewById(R.id.main_univ_sy);
        area = doraemon.findViewById(R.id.main_area_sy);
        clg = doraemon.findViewById(R.id.main_clg_sy);

        mainSy = doraemon.findViewById(R.id.main_sy);
        add = doraemon.findViewById(R.id.add);

        univRecSy = doraemon.findViewById(R.id.univRecSy);
        areaRecSy = doraemon.findViewById(R.id.areaRecSy);
        hoursRecSy = doraemon.findViewById(R.id.hoursRecSy);

        hoursListDataSy = fill_with_data2();

        return doraemon;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragSy = doraemon.findViewById(R.id.frag_sy);

        univ.setOnClickListener(v -> {
            whichAct = 23;
            mainSy.setVisibility(View.VISIBLE);
            univRecSy.setVisibility(View.VISIBLE);
            areaRecSy.setVisibility(View.GONE);
            hoursRecSy.setVisibility(View.GONE);
            add.setVisibility(View.VISIBLE);

            univ.setBackgroundColor(primaryCol);
            univ.setTextColor(Color.WHITE);
            area.setBackgroundColor(transparent);
            area.setTextColor(Color.BLACK);
            clg.setBackgroundColor(transparent);
            clg.setTextColor(Color.BLACK);
        });

        area.setOnClickListener(v -> {
            whichAct = 22;
            mainSy.setVisibility(View.VISIBLE);
            add.setVisibility(View.VISIBLE);

            univRecSy.setVisibility(View.GONE);
            areaRecSy.setVisibility(View.VISIBLE);
            hoursRecSy.setVisibility(View.GONE);

            area.setBackgroundColor(primaryCol);
            area.setTextColor(Color.WHITE);
            univ.setBackgroundColor(transparent);
            univ.setTextColor(Color.BLACK);
            clg.setBackgroundColor(transparent);
            clg.setTextColor(Color.BLACK);
        });

        clg.setOnClickListener(v -> {
            mainSy.setVisibility(View.VISIBLE);

            whichAct = 21;
            add.setVisibility(View.VISIBLE);
            univRecSy.setVisibility(View.GONE);
            areaRecSy.setVisibility(View.GONE);
            hoursRecSy.setVisibility(View.VISIBLE);

            clg.setBackgroundColor(primaryCol);
            clg.setTextColor(Color.WHITE);
            univ.setBackgroundColor(transparent);
            univ.setTextColor(Color.BLACK);
            area.setBackgroundColor(transparent);
            area.setTextColor(Color.BLACK);
        });

        /*RecyclerView recyclerViewUniv = doraemon.findViewById(R.id.univRecSy);
        MyListAdapter adapterUniv = new MyListAdapter(univListDataFy, mContext);
        recyclerViewUniv.setHasFixedSize(true);
        recyclerViewUniv.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewUniv.setAdapter(adapterUniv);

        RecyclerView recyclerViewArea = doraemon.findViewById(R.id.areaRecSy);
        MyListAdapter adapterArea = new MyListAdapter(adapterDataMain, mContext);
        recyclerViewArea.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewArea.setAdapter(adapterArea);*/

        RecyclerView recyclerViewHours = doraemon.findViewById(R.id.hoursRecSy);
        MyListAdapter adapterHours = new MyListAdapter(hoursListDataSy, mContext);
        recyclerViewHours.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewHours.setAdapter(adapterHours);

        add.setOnClickListener(view1 -> {
            onDetach();
            Log.e("AAAAAA", "" + whichAct);
            mainSy.setVisibility(View.GONE);
            fragSy.setVisibility(View.GONE);
            univRecSy.setVisibility(View.GONE);
            areaRecSy.setVisibility(View.GONE);
            hoursRecSy.setVisibility(View.GONE);
            univ.setBackgroundColor(Color.TRANSPARENT);
            clg.setBackgroundColor(Color.TRANSPARENT);
            area.setBackgroundColor(Color.TRANSPARENT);

            univ.setTextColor(blackish);
            area.setTextColor(blackish);
            clg.setTextColor(blackish);
        });
    }

    public List<AdapterDataMain> fill_with_data2() {
        ArrayList<AdapterDataMain> data = new ArrayList<>();

        // TODO: Switch to activity or new frag for showing more data
        for (int i = 1; i <= 12; i++) {
            data.add(new AdapterDataMain("DD/MM/YYYY", "Activity " + i, "00", "", 0));
        }
        return data;
    }
}