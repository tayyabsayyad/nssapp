package com.test.nss;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_fy_act, container, false);

        univ = root.findViewById(R.id.main_univ_fy);
        area = root.findViewById(R.id.main_area_fy);
        clg = root.findViewById(R.id.main_clg_fy);

        mainFy = root.findViewById(R.id.main_fy);
        listView = root.findViewById(R.id.list_hours);
        listView2 = root.findViewById(R.id.list_act);
        listView3 = root.findViewById(R.id.list_date);
        univ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFy.setVisibility(View.VISIBLE);
            }
        });

        area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFy.setVisibility(View.VISIBLE);
            }
        });

        clg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFy.setVisibility(View.VISIBLE);
            }
        });

        mContext = requireContext();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TestAdapter mDbHelper = new TestAdapter(mContext);
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

        mDbHelper.close();
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
}