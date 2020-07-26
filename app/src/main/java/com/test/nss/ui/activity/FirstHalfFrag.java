package com.test.nss.ui.activity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.nss.R;
import com.test.nss.TestAdapter;

import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class FirstHalfFrag extends Fragment {

    View root;
    Context mContext;
    List<AdapterDataAct> areaListDataAct;
    List<AdapterDataAct> univListDataAct;
    List<AdapterDataAct> clgListDataAct;

    RecyclerView recyclerViewAreaAct;
    RecyclerView recyclerViewUnivAct;
    RecyclerView recyclerViewClgAct;

    TextView areaAct;
    TextView clgAct;
    TextView univAct;

    Button backAct;

    LinearLayout actDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_first_half_act, container, false);
        mContext = requireContext();
        backAct = root.findViewById(R.id.backActBtn);

        clgListDataAct = addActData(11);
        areaListDataAct = ListUtils.union(addActData(121), addActData(122));
        univListDataAct = addActData(13);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backAct.setVisibility(View.GONE);
        recyclerViewAreaAct = root.findViewById(R.id.areafyList);
        recyclerViewClgAct = root.findViewById(R.id.clgfyList);
        recyclerViewUnivAct = root.findViewById(R.id.univfyList);


        actDetails = root.findViewById(R.id.act_details);

        areaAct = root.findViewById(R.id.area_act);
        clgAct = root.findViewById(R.id.clg_act);
        univAct = root.findViewById(R.id.univ_act);

        areaAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backAct.setVisibility(View.VISIBLE);
                actDetails.setVisibility(View.GONE);
                recyclerViewAreaAct.setVisibility(View.VISIBLE);
                recyclerViewClgAct.setVisibility(View.GONE);
                recyclerViewUnivAct.setVisibility(View.GONE);
            }
        });

        clgAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backAct.setVisibility(View.VISIBLE);
                actDetails.setVisibility(View.GONE);
                recyclerViewClgAct.setVisibility(View.VISIBLE);
                recyclerViewAreaAct.setVisibility(View.GONE);
                recyclerViewUnivAct.setVisibility(View.GONE);
            }
        });

        univAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backAct.setVisibility(View.VISIBLE);
                actDetails.setVisibility(View.GONE);
                recyclerViewUnivAct.setVisibility(View.VISIBLE);
                recyclerViewClgAct.setVisibility(View.GONE);
                recyclerViewAreaAct.setVisibility(View.GONE);
            }
        });

        MyListAdapterAct adapterAreaAct = new MyListAdapterAct(areaListDataAct, mContext);
        recyclerViewAreaAct.setHasFixedSize(true);
        recyclerViewAreaAct.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewAreaAct.setAdapter(adapterAreaAct);

        MyListAdapterAct adapterUnivAct = new MyListAdapterAct(univListDataAct, mContext);
        recyclerViewUnivAct.setHasFixedSize(true);
        recyclerViewUnivAct.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewUnivAct.setAdapter(adapterUnivAct);

        MyListAdapterAct adapterClgAct = new MyListAdapterAct(clgListDataAct, mContext);
        recyclerViewClgAct.setHasFixedSize(true);
        recyclerViewClgAct.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewClgAct.setAdapter(adapterClgAct);

        backAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backAct.setVisibility(View.GONE);
                actDetails.setVisibility(View.VISIBLE);
                recyclerViewUnivAct.setVisibility(View.GONE);
                recyclerViewAreaAct.setVisibility(View.GONE);
                recyclerViewClgAct.setVisibility(View.GONE);
            }
        });
    }

    public List<AdapterDataAct> addActData(int yr) {
        Log.e("opening db", "now for yr:" + yr);
        ArrayList<AdapterDataAct> data3 = new ArrayList<>();

        TestAdapter mDbHelper = new TestAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();

        Cursor c3 = mDbHelper.getActAllAdmin(yr);
        Log.e("SSS", "" + c3.getCount());

        while (c3.moveToNext()) {
            data3.add(new AdapterDataAct(
                            c3.getString(c3.getColumnIndex("ActivityName")),
                            c3.getString(c3.getColumnIndex("HoursAssigned"))
                    )
            );
        }
        mDbHelper.close();
        return data3;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        FragmentManager fm = getChildFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.e("FirstHalfFrag", "onDetach: " + fm.getBackStackEntryCount());
            fm.popBackStack("FirstHalfFrag", 0);
        }
        backAct.setVisibility(View.GONE);
        actDetails.setVisibility(View.VISIBLE);
        recyclerViewUnivAct.setVisibility(View.GONE);
        recyclerViewAreaAct.setVisibility(View.GONE);
        recyclerViewClgAct.setVisibility(View.GONE);
    }
}