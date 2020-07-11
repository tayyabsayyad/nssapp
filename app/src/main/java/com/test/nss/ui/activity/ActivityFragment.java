package com.test.nss.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.test.nss.R;
import com.test.nss.ui.main.MainFragment;

public class ActivityFragment extends Fragment {

    View root;
    Toolbar toolbar;
    ImageView home;
    Button firstButton;
    Button secButton;
    LinearLayout actDetails;
    View line_main;
    View line_main2;
    TextView univ_act;
    TextView clg_act;
    TextView area_act;
    LinearLayout act_list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_activity, container, false);

        home = root.findViewById(R.id.homeButton);
        toolbar = requireActivity().findViewById(R.id.toolbar);
        firstButton = root.findViewById(R.id.firstButton);
        secButton = root.findViewById(R.id.secButton);
        actDetails = root.findViewById(R.id.act_details);
        act_list = root.findViewById(R.id.act_list);
        area_act = root.findViewById(R.id.area_act);
        line_main = root.findViewById(R.id.line_main);
        line_main2 = root.findViewById(R.id.line_main2);
        univ_act = root.findViewById(R.id.univ_act);
        clg_act = root.findViewById(R.id.clg_act);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setVisibility(View.GONE);

        firstButton.setOnClickListener(v -> {
            firstButton.setTextColor(Color.WHITE);
            firstButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
            secButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.blackish));
            secButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent));
            actDetails.setVisibility(View.VISIBLE);
        });

        secButton.setOnClickListener(v -> {
            secButton.setTextColor(Color.WHITE);
            secButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
            firstButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.blackish));
            firstButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent));
            actDetails.setVisibility(View.VISIBLE);
        });

        home.setOnClickListener(view1 -> {
            MainFragment mainFragment = new MainFragment();
            FragmentManager fragmentManager = getFragmentManager();

            assert fragmentManager != null;
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, mainFragment, mainFragment.getTag()).commit();
            toolbar.setTitle(getString(R.string.main_frag));
            toolbar.setVisibility(View.VISIBLE);
        });

        area_act.setOnClickListener(v -> {
            line_main.setVisibility(View.GONE);
            univ_act.setVisibility(View.GONE);
            line_main2.setVisibility(View.GONE);
            clg_act.setVisibility(View.GONE);
            act_list.setVisibility(View.VISIBLE);
        });
    }
}