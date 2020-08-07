package com.test.nss.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;
import com.test.nss.R;
import com.test.nss.ediary;

import static com.test.nss.ediary.blackish;
import static com.test.nss.ediary.isFirst;
import static com.test.nss.ediary.primaryColDark;

public class MainFragment extends Fragment {

    View root;

    TextView firstButton;
    TextView secButton;
    Toolbar toolbar;
    FragmentManager fm;
    FrameLayout halvesFrame;
    Context mContext;
    TextView toolbarTitle;
    TextView maLHay;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_main, container, false);

        mContext = requireContext();

        toolbarTitle = requireActivity().findViewById(R.id.titleTool);
        toolbarTitle.setText(getString(R.string.menu_main_frag));

        fm = requireActivity().getSupportFragmentManager();

        halvesFrame = root.findViewById(R.id.halves_frame);

        firstButton = root.findViewById(R.id.firstButton);
        secButton = root.findViewById(R.id.secButton);

        toolbar = root.findViewById(R.id.toolbar);
        maLHay = root.findViewById(R.id.malHay);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = requireActivity().findViewById(R.id.toolbar);
        maLHay.setVisibility(View.VISIBLE);

        toolbar.setVisibility(View.VISIBLE);

        if (isFirst) {
            secButton.setTextColor(mContext.getColor(R.color.grey));
            firstButton.setOnClickListener(v -> {
                firstButton.setTextColor(primaryColDark);
                if (!isFirst)
                    secButton.setTextColor(ediary.blackish);
                maLHay.setVisibility(View.GONE);

                toolbar.setVisibility(View.VISIBLE);
                if (halvesFrame.getVisibility() == View.GONE)
                    halvesFrame.setVisibility(View.VISIBLE);

                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.halves_frame, new fyAct(), "fyAct").addToBackStack("MainFrag").commit();
            });
            secButton.setOnClickListener(v -> {
                if (isFirst)
                    Snackbar.make(v, "Please complete First Year", Snackbar.LENGTH_SHORT).show();
            });
        } else {
            secButton.setOnClickListener(v -> {
                secButton.setTextColor(primaryColDark);
                firstButton.setTextColor(blackish);
                maLHay.setVisibility(View.GONE);

                toolbar.setVisibility(View.VISIBLE);
                if (halvesFrame.getVisibility() == View.GONE)
                    halvesFrame.setVisibility(View.VISIBLE);

                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.halves_frame, new syAct(), "syAct").addToBackStack("MainFrag").commit();
            });

            firstButton.setOnClickListener(view1 -> {
                secButton.setTextColor(blackish);
                firstButton.setTextColor(primaryColDark);
                maLHay.setVisibility(View.GONE);

                toolbar.setVisibility(View.VISIBLE);
                if (halvesFrame.getVisibility() == View.GONE)
                    halvesFrame.setVisibility(View.VISIBLE);

                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.halves_frame, new fyAct(), "fyAct").addToBackStack("MainFrag").commit();
            });
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.popBackStack("MainFrag", 0);
        }
    }
}