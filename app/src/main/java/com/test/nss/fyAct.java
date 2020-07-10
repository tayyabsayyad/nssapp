package com.test.nss;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.nss.ui.main.AreaListDataFy;
import com.test.nss.ui.main.HoursListData;
import com.test.nss.ui.main.MyListAdapterAreaFy;
import com.test.nss.ui.main.MyListAdapterHours;
import com.test.nss.ui.main.MyListAdapterUniv;
import com.test.nss.ui.main.UnivListData;

import java.util.ArrayList;
import java.util.List;

public class fyAct extends Fragment {

    View root;
    Button univ;
    Button area;
    Button clg;
    ListView listView;
    ListView listView2;
    ListView listView3;

    LinearLayout mainFy;
    Context mContext;

    RecyclerView univRecFy;
    RecyclerView areaRecFy;
    RecyclerView hoursRecFy;

    Button univAct;
    Button areaAct;
    Button hoursAct;

    View line;
    View line2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_fy_act, container, false);
        mContext = requireContext();

        univ = root.findViewById(R.id.main_univ_fy);
        area = root.findViewById(R.id.main_area_fy);
        clg = root.findViewById(R.id.main_clg_fy);

        mainFy = root.findViewById(R.id.main_fy);

        univRecFy = root.findViewById(R.id.univRecFy);
        areaRecFy = root.findViewById(R.id.areaRecFy);
        hoursRecFy = root.findViewById(R.id.hoursRecFy);

        /*listView = root.findViewById(R.id.list_hours);
        listView2 = root.findViewById(R.id.list_act);
        listView3 = root.findViewById(R.id.list_date);*/

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
            mainFy.setVisibility(View.VISIBLE);
            univRecFy.setVisibility(View.VISIBLE);
            areaRecFy.setVisibility(View.GONE);
            hoursRecFy.setVisibility(View.GONE);
        });

        area.setOnClickListener(v -> {
            mainFy.setVisibility(View.VISIBLE);
            univRecFy.setVisibility(View.GONE);
            areaRecFy.setVisibility(View.VISIBLE);
            hoursRecFy.setVisibility(View.GONE);
        });

        clg.setOnClickListener(v -> {
            mainFy.setVisibility(View.VISIBLE);
            univRecFy.setVisibility(View.GONE);
            areaRecFy.setVisibility(View.GONE);
            hoursRecFy.setVisibility(View.VISIBLE);
        });

        UnivListData[] univListData = new UnivListData[]{
                new UnivListData("DD/MM/YYYY", "Activity 1", "00"),
                new UnivListData("DD/MM/YYYY", "Activity 2", "00"),
                new UnivListData("DD/MM/YYYY", "Activity 3", "00"),
        };

        HoursListData[] hoursListData = new HoursListData[]{
                new HoursListData("DD/MM/YYYY", "Activity 1", "00")
        };

        RecyclerView recyclerViewUniv = (RecyclerView) root.findViewById(R.id.univRecFy);
        MyListAdapterUniv adapterUniv = new MyListAdapterUniv(univListData);
        recyclerViewUniv.setHasFixedSize(true);
        recyclerViewUniv.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewUniv.setAdapter(adapterUniv);

        List<AreaListDataFy> areaListDataFy = fill_with_data();

        RecyclerView recyclerViewArea = (RecyclerView) root.findViewById(R.id.areaRecFy);
        MyListAdapterAreaFy adapterArea = new MyListAdapterAreaFy(areaListDataFy, mContext);
        //recyclerViewArea.setHasFixedSize(true);
        recyclerViewArea.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewArea.setAdapter(adapterArea);

        RecyclerView recyclerViewHours = (RecyclerView) root.findViewById(R.id.hoursRecFy);
        MyListAdapterHours adapterHours = new MyListAdapterHours(hoursListData);
        recyclerViewHours.setHasFixedSize(true);
        recyclerViewHours.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewHours.setAdapter(adapterHours);
    }

    // TODO: Check this
    @Override
    public void onDetach() {
        super.onDetach();
        /*FragmentManager fm = requireActivity().getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }*/
    }

    public List<AreaListDataFy> fill_with_data() {

        List<AreaListDataFy> data = new ArrayList<>();

        for (int i = 1; i <= 15; i++) {
            data.add(new AreaListDataFy("DD/MM/YYYY", "Activity " + i, "00"));
        }
        for (int i = 1; i <= 7; i++) {
            data.add(new AreaListDataFy("", "", ""));
        }
        //data.add(new AreaListDataFy("Batman vs Superman", "Following the destruction of Metropolis, Batman embarks on a personal vendetta against Superman ", R.drawable.ic_action_movie));

        return data;
    }
}