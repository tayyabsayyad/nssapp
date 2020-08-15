package com.test.nss.ui.work;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.snackbar.Snackbar;
import com.test.nss.DataBaseHelper;
import com.test.nss.DatabaseAdapter;
import com.test.nss.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.test.nss.ediary.VEC;
import static com.test.nss.ediary.blackish;
import static com.test.nss.ediary.isFirst;
import static com.test.nss.ediary.primaryColDark;

public class WorkFragment extends Fragment {

    View root;
    Toolbar toolbar;
    TextView firstButton;
    TextView secButton;
    FragmentManager fm;
    TextView toolbarTitle;
    TextView hoursInfo;
    PieChart pieChart;
    List<AdapterDataWork> adapterDataWorks;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_work, container, false);

        toolbarTitle = requireActivity().findViewById(R.id.titleTool);
        toolbarTitle.setText(getString(R.string.menu_work_hours));

        hoursInfo = root.findViewById(R.id.hoursInfo);

        firstButton = root.findViewById(R.id.firstButton);
        secButton = root.findViewById(R.id.secButton);
        adapterDataWorks = firstHalfWorkData();
        pieChart = root.findViewById(R.id.pieChart);

        for (int i = 0; i < adapterDataWorks.size(); i++) {
            if (!adapterDataWorks.get(i).getCompHours().equals("0")) {
                pieChart.setVisibility(View.VISIBLE);
            }
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = requireActivity().findViewById(R.id.toolbar);

        toolbar.setVisibility(View.VISIBLE);

        ArrayList<PieEntry> data = new ArrayList<>();

        for (int i = 0; i < adapterDataWorks.size(); i++) {
            if (Integer.parseInt(adapterDataWorks.get(i).getCompHours()) > 0) {
                data.add(new PieEntry(Integer.parseInt(adapterDataWorks.get(i).getCompHours()), adapterDataWorks.get(i).getNature()));
            }
        }

        PieDataSet pieData = new PieDataSet(data, "");
        pieData.setSliceSpace(3);
        pieData.setSelectionShift(7);
        //pieData.setValueFormatter(new PercentFormatter());

        int[] colors = new int[10];
        int counter = 0;

        for (int color : ColorTemplate.MATERIAL_COLORS
        ) {
            colors[counter] = color;
            counter++;
        }

        for (int color : ColorTemplate.COLORFUL_COLORS
        ) {
            colors[counter] = color;
            counter++;
        }
        pieData.setColors(colors);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String s = Float.toString(e.getY());

                Toast.makeText(requireContext(), s.substring(0, s.indexOf(".")) + "h Worked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        pieChart.setUsePercentValues(true);
        pieChart.setDrawEntryLabels(false);
        pieData.setValueTextSize(15f);

        PieData pieData1 = new PieData(pieData);

        pieData1.setDataSet(pieData);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setWordWrapEnabled(true);
        l.setDrawInside(false);

        pieChart.setData(pieData1);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Total hours worked");
        pieChart.animate();
        pieChart.invalidate();

        fm = requireActivity().getSupportFragmentManager();

        if (isFirst) {
            secButton.setTextColor(requireContext().getColor(R.color.grey));
            firstButton.setOnClickListener(v -> {
                firstButton.setTextColor(primaryColDark);

                hoursInfo.setVisibility(View.GONE);
                fm.beginTransaction().replace(R.id.work_details, new WorkDetailsFirstFrag(requireContext())).addToBackStack("WorkFrag").commit();
            });
            secButton.setOnClickListener(v -> {
                if (isFirst)
                    Snackbar.make(v, "Please complete First Year", Snackbar.LENGTH_SHORT).show();
            });
        } else {
            firstButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.e("AA", "AAA");
                    SharedPreferences shareit = requireContext().getSharedPreferences("KEY", MODE_PRIVATE);
                    SharedPreferences.Editor eddy = shareit.edit();
                    eddy.putBoolean("isCont", false);
                    eddy.apply();
                    return true;
                }
            });
            secButton.setOnClickListener(v -> {
                secButton.setTextColor(primaryColDark);
                firstButton.setTextColor(blackish);

                hoursInfo.setVisibility(View.GONE);

                fm.beginTransaction().replace(R.id.work_details, new WorkDetailsSecFrag(requireContext())).addToBackStack("WorkFrag").commit();
            });

            firstButton.setOnClickListener(view1 -> {
                firstButton.setTextColor(primaryColDark);
                secButton.setTextColor(blackish);

                hoursInfo.setVisibility(View.GONE);
                fm.beginTransaction().replace(R.id.work_details, new WorkDetailsFirstFrag(requireContext())).addToBackStack("WorkFrag").commit();
            });
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (fm.getBackStackEntryCount() > 0) {
            //fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            //    fm.popBackStack("WorkFrag", 0);
        }
    }

    public List<AdapterDataWork> firstHalfWorkData() {
        ArrayList<AdapterDataWork> data = new ArrayList<>();

        DatabaseAdapter m = new DatabaseAdapter(requireContext());
        m.createDatabase();
        m.open();

        int areaCompOne = m.getSumHours("First Year Area Based One");
        int areaCompTwo = m.getSumHours("First Year Area Based Two");
        int clgComp = m.getSumHours("First Year College");
        int univComp = m.getSumHours("First Year University");

        int areaCompOne2 = m.getSumHours("Second Year Area Based One");
        int areaCompTwo2 = m.getSumHours("Second Year Area Based Two");
        int clgComp2 = m.getSumHours("Second Year College");
        int univComp2 = m.getSumHours("Second Year University");

        int areaLvlOne = m.getHours("Area Based Level One");
        int areaLvlTwo = m.getHours("Area Based Level Two");
        int clgLvl = m.getHours("College Level");
        int univLvl = m.getHours("University Level");

        m.close();
        //Log.i("TAG", "firstHalfWorkData: " + areaCompTwo);
        int areaRemOneHours;
        int areaRemTwoHours;
        int univRemHours;
        int clgRemHours;

        if (areaCompOne >= 1 && areaLvlOne - areaCompOne > 0)
            areaRemOneHours = areaLvlOne - areaCompOne;
        else if (areaCompOne == 0)
            areaRemOneHours = areaLvlOne;
        else
            areaRemOneHours = Integer.parseInt("00");

        if (areaCompTwo >= 1 && areaLvlTwo - areaCompTwo > 0)
            areaRemTwoHours = areaLvlTwo - areaCompTwo;
        else if (areaCompTwo == 0)
            areaRemTwoHours = areaLvlTwo;
        else
            areaRemTwoHours = Integer.parseInt("00");

        if (clgComp >= 1 && clgLvl - clgComp > 0)
            clgRemHours = clgLvl - clgComp;
        else if (clgComp == 0)
            clgRemHours = clgLvl;
        else
            clgRemHours = Integer.parseInt("00");

        if (univComp >= 1 && univLvl - univComp > 0)
            univRemHours = univLvl - univComp;
        else if (univComp == 0)
            univRemHours = univLvl;
        else
            univRemHours = Integer.parseInt("00");

        deleteData("WorkHoursFy");
        m = new DatabaseAdapter(requireContext());
        m.createDatabase();
        m.open();
        m.insertWork(
                VEC,
                "Area Based 1",
                areaLvlOne,
                areaCompOne,
                areaRemOneHours,
                1
        );
        m.insertWork(
                VEC,
                "Area Based 2",
                areaLvlTwo,
                areaCompTwo,
                areaRemTwoHours,
                1
        );
        m.insertWork(
                VEC,
                "University",
                univLvl,
                univComp,
                univRemHours,
                1
        );
        m.insertWork(
                VEC,
                "College",
                clgLvl,
                clgComp,
                clgRemHours,
                1
        );
        m.close();

        data.add(new AdapterDataWork("FY Area Based 1", String.valueOf(areaLvlOne), String.valueOf(areaCompOne), String.valueOf(areaRemOneHours)));
        data.add(new AdapterDataWork("FY Area Based 2", String.valueOf(areaLvlTwo), String.valueOf(areaCompTwo), String.valueOf(areaRemTwoHours)));
        data.add(new AdapterDataWork("FY University", String.valueOf(univLvl), String.valueOf(univComp), String.valueOf(univRemHours)));
        data.add(new AdapterDataWork("FY College", String.valueOf(clgLvl), String.valueOf(clgComp), String.valueOf(clgRemHours)));

        if (areaCompOne2 >= 1 && areaLvlOne - areaCompOne2 > 0)
            areaRemOneHours = areaLvlOne - areaCompOne2;
        else if (areaCompOne2 == 0)
            areaRemOneHours = areaLvlOne;
        else
            areaRemOneHours = Integer.parseInt("00");

        if (areaCompTwo2 >= 1 && areaLvlTwo - areaCompTwo2 > 0)
            areaRemTwoHours = areaLvlTwo - areaCompTwo2;
        else if (areaCompTwo2 == 0)
            areaRemTwoHours = areaLvlTwo;
        else
            areaRemTwoHours = Integer.parseInt("00");

        if (clgComp2 >= 1 && clgLvl - clgComp2 > 0)
            clgRemHours = clgLvl - clgComp2;
        else if (clgComp2 == 0)
            clgRemHours = clgLvl;
        else
            clgRemHours = Integer.parseInt("00");

        if (univComp2 >= 1 && univLvl - univComp2 > 0)
            univRemHours = univLvl - univComp2;
        else if (univComp2 == 0)
            univRemHours = univLvl;
        else
            univRemHours = Integer.parseInt("00");

        deleteData("WorkHoursSy");
        m = new DatabaseAdapter(requireContext());
        m.createDatabase();
        m.open();
        m.insertWork(
                VEC,
                "Area Based 1",
                areaLvlOne,
                areaCompOne2,
                areaRemOneHours,
                2
        );
        m.insertWork(
                VEC,
                "Area Based 2",
                areaLvlTwo,
                areaCompTwo2,
                areaRemTwoHours,
                2
        );
        m.insertWork(
                VEC,
                "University",
                univLvl,
                univComp2,
                univRemHours,
                2
        );
        m.insertWork(
                VEC,
                "College",
                clgLvl,
                clgComp2,
                clgRemHours,
                2
        );
        m.close();

        data.add(new AdapterDataWork("SY First Year Area Based 1", String.valueOf(areaLvlOne), String.valueOf(areaCompOne2), String.valueOf(areaRemOneHours)));
        data.add(new AdapterDataWork("SY Area Based 2", String.valueOf(areaLvlTwo), String.valueOf(areaCompTwo2), String.valueOf(areaRemTwoHours)));
        data.add(new AdapterDataWork("SY University", String.valueOf(univLvl), String.valueOf(univComp2), String.valueOf(univRemHours)));
        data.add(new AdapterDataWork("SY College", String.valueOf(clgLvl), String.valueOf(clgComp2), String.valueOf(clgRemHours)));
        return data;
    }

    public void deleteData(String table) {
        DatabaseAdapter mDbHelper2 = new DatabaseAdapter(requireContext());
        mDbHelper2.createDatabase();
        mDbHelper2.open();
        DataBaseHelper mDb2 = new DataBaseHelper(requireContext());
        SQLiteDatabase m = mDb2.getWritableDatabase();
        m.execSQL("DELETE FROM " + table);
        mDbHelper2.close();
        m.close();
        mDb2.close();
    }
}