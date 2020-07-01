package com.test.nss;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CampInputDetailsFrag extends Fragment {

    View root;
    TextView which_day;
    LinearLayout checkbox1;
    LinearLayout checkbox2;
    LinearLayout checkbox3;

    Boolean isCh1;
    Boolean isCh2;
    Boolean isCh3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_camp_input, container, false);
        which_day = root.findViewById(R.id.which_day);
        checkbox1 = root.findViewById(R.id.checkbox1);
        checkbox2 = root.findViewById(R.id.checkbox2);
        checkbox3 = root.findViewById(R.id.checkbox3);


        assert getArguments() != null;
        isCh1 = getArguments().getBoolean("is_ch1");
        isCh2 = getArguments().getBoolean("is_ch2");
        isCh3 = getArguments().getBoolean("is_ch3");

        which_day.setTextColor(getResources().getColor(R.color.blackish, requireActivity().getTheme()));
        which_day.setText(getArguments().getString("whichDay"));

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (isCh1 && isCh2 && isCh3) {
            checkbox3.setVisibility(View.VISIBLE);
            checkbox2.setVisibility(View.VISIBLE);
            checkbox1.setVisibility(View.VISIBLE);
        }
        if (isCh1 && isCh2) {
            checkbox2.setVisibility(View.VISIBLE);
            checkbox1.setVisibility(View.VISIBLE);
        }
        if (isCh1 && isCh3) {
            checkbox2.setVisibility(View.VISIBLE);
            checkbox1.setVisibility(View.VISIBLE);
        }
        if (isCh2 && isCh3) {
            checkbox2.setVisibility(View.VISIBLE);
            checkbox1.setVisibility(View.VISIBLE);
        }
        if (isCh1) {
            checkbox1.setVisibility(View.VISIBLE);
        }
        if (isCh2) {
            checkbox2.setVisibility(View.VISIBLE);
        }
        if (isCh3) {
            checkbox3.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        checkbox1.setVisibility(View.GONE);
        checkbox2.setVisibility(View.GONE);
        checkbox3.setVisibility(View.GONE);
        which_day.setText("");
    }
}