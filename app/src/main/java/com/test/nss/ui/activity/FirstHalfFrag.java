package com.test.nss.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.test.nss.DatabaseAdapter;
import com.test.nss.R;

import java.util.ArrayList;
import java.util.List;

public class FirstHalfFrag extends Fragment {

    View root;
    Context mContext;
    List<AdapterDataAct> areaOneListDataAct;
    List<AdapterDataAct> areaTwoListDataAct;
    List<AdapterDataAct> univListDataAct;
    List<AdapterDataAct> clgListDataAct;

    RecyclerView recyclerViewAreaOneAct;
    RecyclerView recyclerViewAreaTwoAct;
    RecyclerView recyclerViewUnivAct;
    RecyclerView recyclerViewClgAct;

    TextView areaActOne;
    TextView areaActTwo;
    TextView clgAct;
    TextView univAct;

    FloatingActionButton backAct;

    LinearLayout actDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_first_half_act, container, false);
        mContext = requireContext();
        backAct = root.findViewById(R.id.backActBtn);

        clgListDataAct = addActData(11);
        areaOneListDataAct = addActData(121);
        areaTwoListDataAct = addActData(122);
        univListDataAct = addActData(13);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(requireContext(), "These are the assigned activities by PO", Toast.LENGTH_SHORT).show();

        hideFab();

        recyclerViewAreaOneAct = root.findViewById(R.id.areafyListOne);
        recyclerViewAreaTwoAct = root.findViewById(R.id.areafyListTwo);
        recyclerViewClgAct = root.findViewById(R.id.clgfyList);
        recyclerViewUnivAct = root.findViewById(R.id.univfyList);

        actDetails = root.findViewById(R.id.act_details);

        areaActOne = root.findViewById(R.id.area_act1);
        areaActTwo = root.findViewById(R.id.area_act2);
        clgAct = root.findViewById(R.id.clg_act);
        univAct = root.findViewById(R.id.univ_act);

        areaActOne.setOnClickListener(view14 -> {
            new Handler().postDelayed(() -> {
                revealFab();
                actDetails.setVisibility(View.GONE);
                recyclerViewAreaOneAct.setVisibility(View.VISIBLE);
                recyclerViewAreaTwoAct.setVisibility(View.GONE);
                recyclerViewClgAct.setVisibility(View.GONE);
                recyclerViewUnivAct.setVisibility(View.GONE);
            }, 350);
        });

        areaActTwo.setOnClickListener(view1 -> {
            new Handler().postDelayed(() -> {
                revealFab();
                actDetails.setVisibility(View.GONE);
                recyclerViewAreaOneAct.setVisibility(View.GONE);
                recyclerViewAreaTwoAct.setVisibility(View.VISIBLE);
                recyclerViewClgAct.setVisibility(View.GONE);
                recyclerViewUnivAct.setVisibility(View.GONE);
            }, 350);
        });
        clgAct.setOnClickListener(view13 -> {
            new Handler().postDelayed(() -> {
                revealFab();
                //backAct.setVisibility(View.VISIBLE);
                actDetails.setVisibility(View.GONE);
                recyclerViewClgAct.setVisibility(View.VISIBLE);
                recyclerViewAreaOneAct.setVisibility(View.GONE);
                recyclerViewAreaTwoAct.setVisibility(View.GONE);
                recyclerViewUnivAct.setVisibility(View.GONE);
            }, 350);
        });

        univAct.setOnClickListener(view12 -> {
            new Handler().postDelayed(() -> {
                revealFab();
                //backAct.setVisibility(View.VISIBLE);
                actDetails.setVisibility(View.GONE);
                recyclerViewUnivAct.setVisibility(View.VISIBLE);
                recyclerViewClgAct.setVisibility(View.GONE);
                recyclerViewAreaOneAct.setVisibility(View.GONE);
                recyclerViewAreaTwoAct.setVisibility(View.GONE);
            }, 350);
        });

        MyListAdapterAct adapterAreaActOne = new MyListAdapterAct(areaOneListDataAct, mContext);
        recyclerViewAreaOneAct.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewAreaOneAct.setAdapter(adapterAreaActOne);

        MyListAdapterAct adapterAreaActTwo = new MyListAdapterAct(areaTwoListDataAct, mContext);
        recyclerViewAreaTwoAct.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewAreaTwoAct.setAdapter(adapterAreaActTwo);

        MyListAdapterAct adapterUnivAct = new MyListAdapterAct(univListDataAct, mContext);
        recyclerViewUnivAct.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewUnivAct.setAdapter(adapterUnivAct);

        MyListAdapterAct adapterClgAct = new MyListAdapterAct(clgListDataAct, mContext);
        recyclerViewClgAct.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewClgAct.setAdapter(adapterClgAct);

        backAct.setOnClickListener(view1 -> {
            hideFab();
            actDetails.setVisibility(View.VISIBLE);
            recyclerViewUnivAct.setVisibility(View.GONE);
            recyclerViewAreaOneAct.setVisibility(View.GONE);
            recyclerViewAreaTwoAct.setVisibility(View.GONE);
            recyclerViewClgAct.setVisibility(View.GONE);
        });
    }

    public List<AdapterDataAct> addActData(int yr) {
        Log.e("opening db", "now for yr:" + yr);
        ArrayList<AdapterDataAct> data3 = new ArrayList<>();

        DatabaseAdapter mDbHelper = new DatabaseAdapter(requireContext());
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

    private void revealFab() {
        View v = root.findViewById(R.id.backActBtn);
        int x = v.getWidth() / 2;
        int y = v.getHeight() / 2;

        float finalRad = (float) Math.hypot(x, y);
        Animator animator = ViewAnimationUtils.createCircularReveal(v, x, y, 0, finalRad);
        v.setVisibility(View.VISIBLE);
        animator.start();
    }

    private void hideFab() {
        View v = root.findViewById(R.id.backActBtn);
        int x = v.getWidth() / 2;
        int y = v.getHeight() / 2;

        float inRad = (float) Math.hypot(x, y);
        Animator animator = ViewAnimationUtils.createCircularReveal(v, x, y, inRad, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.setVisibility(View.GONE);
            }
        });
        animator.start();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        FragmentManager fm = getChildFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.e("FirstHalfFrag", "onDetach: " + fm.getBackStackEntryCount());
            fm.popBackStack("FirstHalfFrag", 0);
        }
        actDetails.setVisibility(View.VISIBLE);
        recyclerViewUnivAct.setVisibility(View.GONE);
        recyclerViewAreaOneAct.setVisibility(View.GONE);
        recyclerViewAreaTwoAct.setVisibility(View.GONE);
        recyclerViewClgAct.setVisibility(View.GONE);
    }
}