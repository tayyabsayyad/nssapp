package com.test.nss;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class CampDetailsDays extends Fragment {

    View root;
    ListView listView;
    Boolean is_ch1;
    Boolean is_ch2;
    Boolean is_ch3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_camp_details_days, container, false);
        assert getArguments() != null;
        is_ch1 = getArguments().getBoolean("is_ch1");
        is_ch2 = getArguments().getBoolean("is_ch2");
        is_ch3 = getArguments().getBoolean("is_ch3");
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = root.findViewById(R.id.camp_days_list);
        ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(requireContext(), R.array.days, android.R.layout.simple_list_item_1);
        listView.setAdapter(list);
        //Boolean ch1_checked = Boolean.valueOf(getArguments().getString("is_ch1"));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String day = parent.getItemAtPosition(position).toString();
                CampInputDetailsFrag campInputDetailsFrag = new CampInputDetailsFrag();

                Bundle args = new Bundle();
                args.putString("whichDay", day);
                args.putBoolean("is_ch1", is_ch1);
                args.putBoolean("is_ch2", is_ch2);
                args.putBoolean("is_ch3", is_ch3);
                campInputDetailsFrag.setArguments(args);
                Toast.makeText(getContext(), day, Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.camp_frag, campInputDetailsFrag).addToBackStack("CampFrag").commit();
            }
        });
    }
}