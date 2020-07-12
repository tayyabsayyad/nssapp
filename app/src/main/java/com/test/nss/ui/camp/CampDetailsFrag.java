package com.test.nss.ui.camp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.test.nss.R;

public class CampDetailsFrag extends Fragment {

    Button go_back;
    Button go_next;
    CheckBox ch1;
    CheckBox ch2;
    CheckBox ch3;
    LinearLayout camp_main_details;
    String whichDay;
    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_camp_activity, container, false);

        camp_main_details = requireActivity().findViewById(R.id.camp_main_details);

        go_back = root.findViewById(R.id.go_back);
        go_next = root.findViewById(R.id.go_next);
        ch1 = root.findViewById(R.id.checkAct1);
        ch2 = root.findViewById(R.id.checkAct2);
        ch3 = root.findViewById(R.id.checkAct3);

        assert getArguments() != null;
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        go_back.setOnClickListener(v -> {
            camp_main_details.setVisibility(View.VISIBLE);
            onDetach();
        });

        go_next.setOnClickListener(v -> {
            CampDetailsDays campDetailsDays = new CampDetailsDays();
            Bundle args = new Bundle();
            args.putString("whichDay", whichDay);
            args.putBoolean("is_ch1", ch1.isChecked());
            args.putBoolean("is_ch2", ch2.isChecked());
            args.putBoolean("is_ch3", ch3.isChecked());
            campDetailsDays.setArguments(args);

            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.camp_frag, campDetailsDays).addToBackStack("CampFrag").commit();
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.e("CampFrag", "onDetach: " + fm.getBackStackEntryCount());
            fm.popBackStackImmediate(null, 0);
            fm.popBackStack("CampFrag", 0);
        }
    }
}