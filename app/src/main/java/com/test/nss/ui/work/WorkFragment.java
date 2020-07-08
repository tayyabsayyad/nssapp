package com.test.nss.ui.work;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.test.nss.R;
import com.test.nss.WorkDetailsFirstFrag;
import com.test.nss.WorkDetailsSecFrag;
import com.test.nss.ui.main.MainFragment;

public class WorkFragment extends Fragment {

    View root;
    Toolbar toolbar;
    ImageView home;
    Button firstButton;
    Button secButton;
    FragmentManager fm;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_work, container, false);

        firstButton = root.findViewById(R.id.firstButton);
        secButton = root.findViewById(R.id.secButton);
        fm = requireActivity().getSupportFragmentManager();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        home = root.findViewById(R.id.homeButton);
        toolbar = requireActivity().findViewById(R.id.toolbar);

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

        firstButton.setOnClickListener(v -> {
            firstButton.setTextColor(Color.WHITE);
            firstButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
            secButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.blackish));
            secButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent));

            fm.beginTransaction().replace(R.id.work_details, new WorkDetailsFirstFrag()).addToBackStack("WorkFrag").commit();
        });

        secButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secButton.setTextColor(Color.WHITE);
                secButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
                firstButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.blackish));
                firstButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent));

                fm.beginTransaction().replace(R.id.work_details, new WorkDetailsSecFrag()).addToBackStack("WorkFrag").commit();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.popBackStack("WorkFrag", 0);
        }
    }
}