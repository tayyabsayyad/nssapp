package com.test.nss.ui.work;

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

import static com.test.nss.ediary.VEC;


public class WorkDetailsSecFrag extends Fragment {
    View root;

    FragmentManager fm;
    List<AdapterDataWork> workListData2;

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
    
        RecyclerView recyclerViewArea = root.findViewById(R.id.secWorkRec);

        WorkDataAdapter adapterWork = new WorkDataAdapter(workListData2, requireContext());
        recyclerViewArea.setHasFixedSize(true);
        recyclerViewArea.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewArea.setAdapter(adapterWork);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fm = requireActivity().getSupportFragmentManager();
        //fm.popBackStack("WorkFrag", 0);
    }

    public List<AdapterDataWork> secHalfWorkData() {
        ArrayList<AdapterDataWork> data = new ArrayList<>();

        DatabaseAdapter m = new DatabaseAdapter(requireContext());
        m.createDatabase();
        m.open();
        int areaCompOne = m.getSumHours("Second Year Area Based One");
        int areaCompTwo = m.getSumHours("Second Year Area Based Two");
        int clgComp = m.getSumHours("Second Year College");
        int univComp = m.getSumHours("Second Year University");

        int areaLvlOne = m.getHours("Area Based Level One");
        int areaLvlTwo = m.getHours("Area Based Level Two");
        int clgLvl = m.getHours("College Level");
        int univLvl = m.getHours("University Level");

        m.close();
        //Log.i("TAG", "SecondHalfWorkData: " + areaCompTwo);
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

        data.add(new AdapterDataWork("Area Based 1", String.valueOf(areaLvlOne), String.valueOf(areaCompOne), String.valueOf(areaRemOneHours)));
        data.add(new AdapterDataWork("Area Based 2", String.valueOf(areaLvlTwo), String.valueOf(areaCompTwo), String.valueOf(areaRemTwoHours)));
        data.add(new AdapterDataWork("University", String.valueOf(univLvl), String.valueOf(univComp), String.valueOf(univRemHours)));
        data.add(new AdapterDataWork("College", String.valueOf(clgLvl), String.valueOf(clgComp), String.valueOf(clgRemHours)));

        return data;
    }
}