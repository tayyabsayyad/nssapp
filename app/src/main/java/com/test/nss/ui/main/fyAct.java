package com.test.nss.ui.main;

import android.content.Context;
import android.database.Cursor;
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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.nss.R;
import com.test.nss.TestAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.test.nss.ediary.blackish;
import static com.test.nss.ediary.primaryColDark;

public class fyAct extends Fragment {

    View root;
    Button univ;
    Button area;
    Button clg;

    LinearLayout mainFy;
    Context mContext;

    RecyclerView univRecFy;
    RecyclerView areaRecFy;
    RecyclerView clgRecFy;

    List<AdapterDataMain> univListDataFy;
    List<AdapterDataMain> areaDataMainFy;
    List<AdapterDataMain> clgListDataFy;

    Button add;
    LinearLayout fragFy;

    int whichAct;
    int act;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_fy_act, container, false);
        mContext = requireContext();

        // add data dynamically

        univ = root.findViewById(R.id.main_univ_fy);
        area = root.findViewById(R.id.main_area_fy);
        clg = root.findViewById(R.id.main_clg_fy);

        mainFy = root.findViewById(R.id.main_fy);
        add = root.findViewById(R.id.add);

        univRecFy = root.findViewById(R.id.univRecFy);
        areaRecFy = root.findViewById(R.id.areaRecFy);
        clgRecFy = root.findViewById(R.id.hoursRecFy);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragFy = root.findViewById(R.id.frag_fy);

        univ.setOnClickListener(v -> {
            whichAct = 13;
            act = 0;
            mainFy.setVisibility(View.VISIBLE);
            univRecFy.setVisibility(View.VISIBLE);
            areaRecFy.setVisibility(View.GONE);
            clgRecFy.setVisibility(View.GONE);
            add.setVisibility(View.VISIBLE);

            //univ.setBackgroundColor(primaryCol);
            univ.setTextColor(primaryColDark);
            //area.setBackgroundColor(transparent);
            area.setTextColor(Color.BLACK);
            //clg.setBackgroundColor(transparent);
            clg.setTextColor(Color.BLACK);
        });

        area.setOnClickListener(v -> {
            whichAct = 12;
            act = 1;
            mainFy.setVisibility(View.VISIBLE);
            add.setVisibility(View.VISIBLE);

            univRecFy.setVisibility(View.GONE);
            areaRecFy.setVisibility(View.VISIBLE);
            clgRecFy.setVisibility(View.GONE);

            //area.setBackgroundColor(primaryColDark);
            area.setTextColor(primaryColDark);
            //univ.setBackgroundColor(transparent);
            univ.setTextColor(Color.BLACK);
            //clg.setBackgroundColor(transparent);
            clg.setTextColor(Color.BLACK);
        });

        clg.setOnClickListener(v -> {
            mainFy.setVisibility(View.VISIBLE);

            whichAct = 11;
            act = 2;
            add.setVisibility(View.VISIBLE);
            univRecFy.setVisibility(View.GONE);
            areaRecFy.setVisibility(View.GONE);
            clgRecFy.setVisibility(View.VISIBLE);

            //clg.setBackgroundColor(primaryColDark);
            clg.setTextColor(primaryColDark);
            //univ.setBackgroundColor(transparent);
            univ.setTextColor(Color.BLACK);
            //area.setBackgroundColor(transparent);
            area.setTextColor(Color.BLACK);
        });

        clgListDataFy = addAct("First Year College");
        areaDataMainFy = addAct("First Year Area Based");
        univListDataFy = addAct("First Year University");

        RecyclerView recyclerViewUniv = root.findViewById(R.id.univRecFy);
        MyListAdapter adapterUniv = new MyListAdapter(univListDataFy, mContext);
        recyclerViewUniv.setHasFixedSize(true);
        recyclerViewUniv.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewUniv.setAdapter(adapterUniv);

        RecyclerView recyclerViewArea = root.findViewById(R.id.areaRecFy);
        MyListAdapter adapterArea = new MyListAdapter(areaDataMainFy, mContext);
        recyclerViewArea.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewArea.setAdapter(adapterArea);

        RecyclerView recyclerViewHours = root.findViewById(R.id.hoursRecFy);
        MyListAdapter adapterHours = new MyListAdapter(clgListDataFy, mContext);
        recyclerViewHours.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewHours.setAdapter(adapterHours);

        add.setOnClickListener(view1 -> {
            onDetach();

            mainFy.setVisibility(View.GONE);
            fragFy.setVisibility(View.GONE);
            univRecFy.setVisibility(View.GONE);
            areaRecFy.setVisibility(View.GONE);
            clgRecFy.setVisibility(View.GONE);
            univ.setBackgroundColor(Color.TRANSPARENT);
            clg.setBackgroundColor(Color.TRANSPARENT);
            area.setBackgroundColor(Color.TRANSPARENT);

            univ.setTextColor(blackish);
            area.setTextColor(blackish);
            clg.setTextColor(blackish);

            AddDetailsActivity detailsActivity = new AddDetailsActivity();
            Bundle args = new Bundle();
            args.putInt("whichAct", whichAct);
            args.putInt("act", act);
            detailsActivity.setArguments(args);

            Log.e("AA", "" + whichAct);
            Log.e("AA", "" + act);

            FragmentManager fm = requireActivity().getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.halves_frame, detailsActivity, "AddDetailsActivity").addToBackStack("fyAct").commit();

            /*
            Save in sqlite
            if(interntAvail){
            call save api(post in api)
            if(response) {
            sync = 1
            } else
            sync = 0
            }
            else
            sync = 0
             */
        });
    }

    // TODO: Check this
    @Override
    public void onDetach() {
        super.onDetach();
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.e("fyAct", "onDetach: " + fm.getBackStackEntryCount());
            fm.popBackStackImmediate("fyAct", 0);
            fm.popBackStack("fyAct", 0);
        }
    }

    public List<AdapterDataMain> addAct(String whichAct) {

        ArrayList<AdapterDataMain> data = new ArrayList<>();

        TestAdapter mDbHelper = new TestAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();

        Cursor c = mDbHelper.getActList(whichAct);
        Log.e("SSS", "" + c.getCount());

        while (c.moveToNext()) {

            data.add(new AdapterDataMain(
                    c.getString(c.getColumnIndex("Date")),
                    c.getString(c.getColumnIndex("ActivityName")),
                    c.getString(c.getColumnIndex("HoursWorked")
                    )));
        }
        mDbHelper.close();
        return data;
    }
}