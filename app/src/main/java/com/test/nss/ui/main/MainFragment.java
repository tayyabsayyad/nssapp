package com.test.nss.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.test.nss.R;
import com.test.nss.ediary;

import static com.test.nss.ediary.blackish;
import static com.test.nss.ediary.primaryColDark;

public class MainFragment extends Fragment {

    View root;
    View line;

    Button firstButton;
    Button secButton;

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

        line = root.findViewById(R.id.line_main_bot);

        toolbar = root.findViewById(R.id.toolbar);
        maLHay = root.findViewById(R.id.malHay);

        firstButton.setOnClickListener(v -> {
            firstButton.setTextColor(primaryColDark);
            secButton.setTextColor(ediary.blackish);
            maLHay.setVisibility(View.GONE);

            line.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            if (halvesFrame.getVisibility() == View.GONE)
                halvesFrame.setVisibility(View.VISIBLE);

            FragmentManager fragmentManager = getChildFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.halves_frame, new fyAct(), "fyAct").addToBackStack("MainFrag").commit();
        });

        secButton.setOnClickListener(v -> {
            secButton.setTextColor(primaryColDark);
            firstButton.setTextColor(blackish);
            maLHay.setVisibility(View.VISIBLE);

            line.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            if (halvesFrame.getVisibility() == View.GONE)
                halvesFrame.setVisibility(View.VISIBLE);

            FragmentManager fragmentManager = getChildFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.halves_frame, new syAct()).addToBackStack("MainFrag").commit();
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = requireActivity().findViewById(R.id.toolbar);
        maLHay.setVisibility(View.VISIBLE);

        //navUsername.setText(m.getString(m.getColumnIndex("VEC")));

        toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        maLHay.setVisibility(View.VISIBLE);
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.popBackStack("MainFrag", 0);
        }
    }
}