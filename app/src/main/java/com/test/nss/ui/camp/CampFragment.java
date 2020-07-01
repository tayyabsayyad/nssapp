package com.test.nss.ui.camp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.test.nss.CampDetailsFrag;
import com.test.nss.R;
import com.test.nss.ui.main.MainFragment;

public class CampFragment extends Fragment {
    public View root;
    Toolbar toolbar;
    ImageView home;

    TextView camp_days;
    TextView camp_act;

    LinearLayout camp_main_details;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_camp, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        home = root.findViewById(R.id.homeButton);
        toolbar = requireActivity().findViewById(R.id.toolbar);
        camp_days = root.findViewById(R.id.camp_days);
        camp_act = root.findViewById(R.id.camp_act);
        camp_main_details = root.findViewById(R.id.camp_main_details);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainFragment mainFragment = new MainFragment();
                FragmentManager fragmentManager = getFragmentManager();

                assert fragmentManager != null;
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, mainFragment, mainFragment.getTag()).commit();
                toolbar.setTitle(getString(R.string.main_frag));
            }
        });
        toolbar.setVisibility(View.GONE);
        camp_main_details.setVisibility(View.VISIBLE);

        camp_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDetach();
                camp_main_details.setVisibility(View.GONE);
                CampDetailsFrag campDetailsFrag = new CampDetailsFrag();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.camp_frag, campDetailsFrag).addToBackStack(campDetailsFrag.getTag()).commit();
            }
        });

        /*camp_days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDetach();
                CampDetailsDays campDetailsDays = new CampDetailsDays();
                FragmentManager fragmentManager= requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.work_details, campDetailsDays).addToBackStack(campDetailsDays.getTag()).commit();
            }
        });*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}