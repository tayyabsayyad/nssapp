package com.test.nss.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
    FragmentManager fm;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_activity, container, false);

        home = root.findViewById(R.id.homeButton);
        toolbar = requireActivity().findViewById(R.id.toolbar);
        fm = requireActivity().getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate("ActivityFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.popBackStack(this.toString(), 0);
        }

        firstButton = root.findViewById(R.id.firstButton);
        secButton = root.findViewById(R.id.secButton);
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

            fm.beginTransaction().replace(R.id.act_list, new FirstHalfFrag()).addToBackStack("ActivityFrag").commit();
        });

        secButton.setOnClickListener(v -> {
            secButton.setTextColor(Color.WHITE);
            secButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
            firstButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.blackish));
            firstButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent));

            fm.beginTransaction().replace(R.id.act_list, new SecHalfFrag()).addToBackStack("ActivityFrag").commit();
        });

        home.setOnClickListener(view1 -> {
            MainFragment mainFragment = new MainFragment();
            FragmentManager fragmentManager = getFragmentManager();

            assert fragmentManager != null;
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, mainFragment, mainFragment.getTag()).commit();
            toolbar.setTitle(getString(R.string.main_frag));
            toolbar.setVisibility(View.VISIBLE);
        });
    }

    public void onDetach() {
        super.onDetach();
        if (fm.getBackStackEntryCount() > 0) {
            Log.e("ActivityFragment", "onDetach: " + fm.getBackStackEntryCount());
            fm.popBackStack("ActivityFrag", 0);
        }
    }
}
