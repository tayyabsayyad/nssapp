package com.test.nss.ui.work;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.nss.DatabaseAdapter;
import com.test.nss.R;

import java.util.ArrayList;
import java.util.List;


public class WorkDetailsSecFrag extends Fragment {
    View root;
    static String c = "College";
    static String u = "University";
    static String a1 = "Area Based 1";
    static String a2 = "Area Based 2";
    public Context context;

    List<AdapterDataWork> workListData2;

    public WorkDetailsSecFrag(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_work_details_sec, container, false);
        workListData2 = secHalfWorkData();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerViewWork = root.findViewById(R.id.secWorkRec);

        WorkDataAdapter adapterWork = new WorkDataAdapter(workListData2, context);
        recyclerViewWork.setHasFixedSize(true);
        recyclerViewWork.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewWork.setAdapter(adapterWork);
    }

    public List<AdapterDataWork> secHalfWorkData() {
        ArrayList<AdapterDataWork> data = new ArrayList<>();


        Cursor col, univ, area1, area2;
        DatabaseAdapter m = new DatabaseAdapter(context);

        m.createDatabase();
        m.open();

        col = m.getHoursDet(c, 2);
        col.moveToFirst();

        univ = m.getHoursDet(u, 2);
        univ.moveToFirst();

        area1 = m.getHoursDet(a1, 2);
        area1.moveToFirst();

        area2 = m.getHoursDet(a2, 2);
        area2.moveToFirst();

        data.add(new AdapterDataWork(a1, area1.getString(area1.getColumnIndex("TotalHours")),
                area1.getString(area1.getColumnIndex("HoursWorked")), area1.getString(area1.getColumnIndex("RemainingHours"))));


        data.add(new AdapterDataWork(a2, area2.getString(area2.getColumnIndex("TotalHours")),
                area2.getString(area2.getColumnIndex("HoursWorked")), area2.getString(area2.getColumnIndex("RemainingHours"))));


        data.add(new AdapterDataWork(u, univ.getString(univ.getColumnIndex("TotalHours")),
                univ.getString(univ.getColumnIndex("HoursWorked")), univ.getString(univ.getColumnIndex("RemainingHours"))));


        data.add(new AdapterDataWork(c, col.getString(col.getColumnIndex("TotalHours")),
                col.getString(col.getColumnIndex("HoursWorked")), col.getString(col.getColumnIndex("RemainingHours"))));

        m.close();
        return data;
    }
}