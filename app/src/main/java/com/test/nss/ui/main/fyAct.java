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
import static com.test.nss.ediary.primaryCol;
import static com.test.nss.ediary.transparent;

public class fyAct extends Fragment {

    View root;
    Button univ;
    Button area;
    Button clg;

    LinearLayout mainFy;
    Context mContext;

    RecyclerView univRecFy;
    RecyclerView areaRecFy;
    RecyclerView hoursRecFy;

    List<AdapterDataMain> univListDataFy;
    List<AdapterDataMain> areaDataMain;
    List<AdapterDataMain> hoursListDataFy;

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

        Log.e("AAA", "AAAA" + whichAct);

        // add data dynamically

        univ = root.findViewById(R.id.main_univ_fy);
        area = root.findViewById(R.id.main_area_fy);
        clg = root.findViewById(R.id.main_clg_fy);

        mainFy = root.findViewById(R.id.main_fy);
        add = root.findViewById(R.id.add);

        univRecFy = root.findViewById(R.id.univRecFy);
        areaRecFy = root.findViewById(R.id.areaRecFy);
        hoursRecFy = root.findViewById(R.id.hoursRecFy);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragFy = root.findViewById(R.id.frag_fy);

        /*TestAdapter mDbHelper = new TestAdapter(mContext);
        mDbHelper.createDatabase();
        mDbHelper.open();

        ArrayList<String> c = mDbHelper.getActList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.simple_list_item_2, c);
        listView.setAdapter(adapter);

        ArrayList<String> c2 = mDbHelper.getActType();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext, R.layout.simple_list_item_2, c2);
        listView2.setAdapter(adapter2);

        ArrayList<String> c3 = mDbHelper.getActType();
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(mContext, R.layout.simple_list_item_2, c3);
        listView3.setAdapter(adapter3);

        mDbHelper.close();*/

        univ.setOnClickListener(v -> {
            whichAct = 13;
            act = 0;
            mainFy.setVisibility(View.VISIBLE);
            univRecFy.setVisibility(View.VISIBLE);
            areaRecFy.setVisibility(View.GONE);
            hoursRecFy.setVisibility(View.GONE);
            add.setVisibility(View.VISIBLE);

            univ.setBackgroundColor(primaryCol);
            univ.setTextColor(Color.WHITE);
            area.setBackgroundColor(transparent);
            area.setTextColor(Color.BLACK);
            clg.setBackgroundColor(transparent);
            clg.setTextColor(Color.BLACK);
        });

        area.setOnClickListener(v -> {
            whichAct = 12;
            act = 1;
            mainFy.setVisibility(View.VISIBLE);
            add.setVisibility(View.VISIBLE);

            univRecFy.setVisibility(View.GONE);
            areaRecFy.setVisibility(View.VISIBLE);
            hoursRecFy.setVisibility(View.GONE);

            area.setBackgroundColor(primaryCol);
            area.setTextColor(Color.WHITE);
            univ.setBackgroundColor(transparent);
            univ.setTextColor(Color.BLACK);
            clg.setBackgroundColor(transparent);
            clg.setTextColor(Color.BLACK);
        });

        clg.setOnClickListener(v -> {
            mainFy.setVisibility(View.VISIBLE);

            whichAct = 11;
            act = 2;
            add.setVisibility(View.VISIBLE);
            univRecFy.setVisibility(View.GONE);
            areaRecFy.setVisibility(View.GONE);
            hoursRecFy.setVisibility(View.VISIBLE);

            clg.setBackgroundColor(primaryCol);
            clg.setTextColor(Color.WHITE);
            univ.setBackgroundColor(transparent);
            univ.setTextColor(Color.BLACK);
            area.setBackgroundColor(transparent);
            area.setTextColor(Color.BLACK);
        });

        hoursListDataFy = addClgAct(11);
        areaDataMain = addAreaAct(12);
        univListDataFy = addUnivData(13);

        RecyclerView recyclerViewUniv = root.findViewById(R.id.univRecFy);
        MyListAdapter adapterUniv = new MyListAdapter(univListDataFy, mContext);
        recyclerViewUniv.setHasFixedSize(true);
        recyclerViewUniv.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewUniv.setAdapter(adapterUniv);

        RecyclerView recyclerViewArea = root.findViewById(R.id.areaRecFy);
        MyListAdapter adapterArea = new MyListAdapter(areaDataMain, mContext);
        recyclerViewArea.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewArea.setAdapter(adapterArea);

        RecyclerView recyclerViewHours = root.findViewById(R.id.hoursRecFy);
        MyListAdapter adapterHours = new MyListAdapter(hoursListDataFy, mContext);
        recyclerViewHours.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewHours.setAdapter(adapterHours);

        add.setOnClickListener(view1 -> {
            onDetach();

            mainFy.setVisibility(View.GONE);
            fragFy.setVisibility(View.GONE);
            univRecFy.setVisibility(View.GONE);
            areaRecFy.setVisibility(View.GONE);
            hoursRecFy.setVisibility(View.GONE);
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

    public List<AdapterDataMain> addClgAct(int whichAct) {

        ArrayList<AdapterDataMain> data = new ArrayList<>();

        TestAdapter mDbHelper = new TestAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();

        Cursor c = mDbHelper.getActList(whichAct);
        Log.e("SSS", "" + c.getCount());

        while (c.moveToNext()) {
            //Log.e("camp", c2.getString(c2.getColumnIndex("College_name")));
            data.add(new AdapterDataMain(
                    c.getString(c.getColumnIndex("Date")),
                    c.getString(c.getColumnIndex("ActivityName")),
                    c.getString(c.getColumnIndex("HoursWorked")
                    )));
        }
        mDbHelper.close();
        return data;
    }

    public List<AdapterDataMain> addAreaAct(int whichAct) {
        ArrayList<AdapterDataMain> data2 = new ArrayList<>();

        TestAdapter mDbHelper = new TestAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();

        Cursor c2 = mDbHelper.getActList(whichAct);
        Log.e("SSS", "" + c2.getCount());

        while (c2.moveToNext()) {
            //Log.e("camp", c2.getString(c2.getColumnIndex("College_name")));
            data2.add(new AdapterDataMain(
                    c2.getString(c2.getColumnIndex("Date")),
                    c2.getString(c2.getColumnIndex("ActivityName")),
                    c2.getString(c2.getColumnIndex("HoursWorked")
                    )));
        }
        mDbHelper.close();
        return data2;
    }

    public List<AdapterDataMain> addUnivData(int whichAct) {
        Log.e("opening db", "now");
        ArrayList<AdapterDataMain> data3 = new ArrayList<>();

        TestAdapter mDbHelper = new TestAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();
        Cursor c3 = mDbHelper.getActList(whichAct);
        Log.e("SSS", "" + c3.getCount());

        while (c3.moveToNext()) {
            //Log.e("camp", c2.getString(c2.getColumnIndex("College_name")));
            data3.add(new AdapterDataMain(
                    c3.getString(c3.getColumnIndex("Date")),
                    c3.getString(c3.getColumnIndex("ActivityName")),
                    c3.getString(c3.getColumnIndex("HoursWorked")
                    )));
        }
        mDbHelper.close();
        return data3;
    }
}