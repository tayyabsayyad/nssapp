package com.test.nss.ui.work;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.nss.DatabaseAdapter;
import com.test.nss.R;

import java.util.ArrayList;
import java.util.List;


public class WorkDetailsFirstFrag extends Fragment {
    View root;
    public Context context;
    FragmentManager fm;
    List<AdapterDataWork> workListData;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_work_details_first, container, false);
        workListData = firstHalfWorkData();

        return root;
    }

    public WorkDetailsFirstFrag(Context context){
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerViewWork = root.findViewById(R.id.firstWorkRec);

        WorkDataAdapter adapterWork = new WorkDataAdapter(workListData, requireContext());
        recyclerViewWork.setHasFixedSize(true);
        recyclerViewWork.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewWork.setAdapter(adapterWork);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fm = requireActivity().getSupportFragmentManager();
        fm.popBackStack("WorkFrag", 0);
    }

    public List<AdapterDataWork> firstHalfWorkData() {
        ArrayList<AdapterDataWork> data = new ArrayList<>();

        DatabaseAdapter m = new DatabaseAdapter(context);
        m.createDatabase();
        m.open();
        int areaCompOne = m.getSumHours("First Year Area Based One");
        int areaCompTwo = m.getSumHours("First Year Area Based Two");
        int clgComp = m.getSumHours("First Year College");
        int univComp = m.getSumHours("First Year University");

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

        data.add(new AdapterDataWork("Area Based 1", String.valueOf(areaLvlOne), String.valueOf(areaCompOne), String.valueOf(areaRemOneHours)));
        data.add(new AdapterDataWork("Area Based 2", String.valueOf(areaLvlTwo), String.valueOf(areaCompTwo), String.valueOf(areaRemTwoHours)));
        data.add(new AdapterDataWork("University", String.valueOf(univLvl), String.valueOf(univComp), String.valueOf(univRemHours)));
        data.add(new AdapterDataWork("College", String.valueOf(clgLvl), String.valueOf(clgComp), String.valueOf(clgRemHours)));

        return data;
    }
}