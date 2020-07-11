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

import com.test.nss.R;

import java.util.ArrayList;
import java.util.List;


public class WorkDetailsSecFrag extends Fragment {
    View root;

    FragmentManager fm;
    List<AdapterDataWork> workListData2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_work_details_sec, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        workListData2 = secHalfWorkData();
        RecyclerView recyclerViewArea = root.findViewById(R.id.secWorkRec);

        WorkDataAdapter adapterArea = new WorkDataAdapter(workListData2, requireContext());
        recyclerViewArea.setHasFixedSize(true);
        recyclerViewArea.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewArea.setAdapter(adapterArea);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fm = requireActivity().getSupportFragmentManager();
        fm.popBackStack("WorkFrag", 0);
    }

    public List<AdapterDataWork> secHalfWorkData() {
        ArrayList<AdapterDataWork> data = new ArrayList<>();

        // TODO: Switch to activity or new frag for showing more data
        data.add(new AdapterDataWork("Area Based Project 2", "40", "00", "40"));
        data.add(new AdapterDataWork("University/District", "10", "00", "10"));
        data.add(new AdapterDataWork("College Work", "10", "00", "10"));
        return data;
    }
}