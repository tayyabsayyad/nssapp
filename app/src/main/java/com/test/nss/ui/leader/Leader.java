package com.test.nss.ui.leader;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.nss.R;
import com.test.nss.TestAdapter;
import com.test.nss.ui.onClickInterface;

import java.util.ArrayList;
import java.util.List;

public class Leader extends Fragment {

    TextView toolbarTitle;
    List<AdapterDataLeader> dataLeaderList;
    onClickInterface onClickInterface;
    View root;
    RecyclerView recViewLeader;
    View line;
    TextView isEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_leader, container, false);
        dataLeaderList = addActData();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbarTitle = requireActivity().findViewById(R.id.titleTool);
        toolbarTitle.setText(getString(R.string.menu_leader));
        isEmpty = root.findViewById(R.id.isEmpty);

        line = root.findViewById(R.id.line_details_bot);

        if (dataLeaderList.size() == 0)
            isEmpty.setText("Yay!\nNo Activities to approve");
        else
            isEmpty.setVisibility(View.GONE);
        recViewLeader = root.findViewById(R.id.vecLeaderList);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        onClickInterface = abc -> {
            //Toast.makeText(requireContext(), abc, Toast.LENGTH_SHORT).show();
            line.setVisibility(View.GONE);
            new Handler().postDelayed(() -> {
                ModifyVolunteer modifyVolunteer = new ModifyVolunteer();
                Bundle args = new Bundle();
                args.putString("thisVec", abc);
                modifyVolunteer.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.detailsModify, modifyVolunteer, "ModifyVolunteer").addToBackStack("Leader").commit();
                recViewLeader.setVisibility(View.GONE);
            }, 500);
        };

        MyListAdapterLeader campActDataAdapter = new MyListAdapterLeader(dataLeaderList, requireContext(), onClickInterface);

        recViewLeader.setLayoutManager(new LinearLayoutManager(requireContext()));
        recViewLeader.setAdapter(campActDataAdapter);
    }

    public List<AdapterDataLeader> addActData() {
        ArrayList<AdapterDataLeader> data3 = new ArrayList<>();

        TestAdapter mDbHelper = new TestAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();
        Cursor c3 = mDbHelper.getVec();

        while (c3.moveToNext()) {
            data3.add(new AdapterDataLeader(
                    c3.getString(c3.getColumnIndex("VEC"))
            ));
        }
        mDbHelper.close();
        return data3;
    }
}