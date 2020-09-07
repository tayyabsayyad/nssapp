package com.test.nss.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;
import com.test.nss.R;

import static com.test.nss.Helper.blackishy;
import static com.test.nss.Helper.isFirst;
import static com.test.nss.Helper.isSec;
import static com.test.nss.Helper.primaryColDark;

public class ActivityFragment extends Fragment {

    View root;
    Toolbar toolbar;
    TextView firstButton;
    TextView secButton;

    FragmentManager fm;
    TextView toolbarTitle;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_activity, container, false);
        toolbarTitle = requireActivity().findViewById(R.id.titleTool);
        toolbarTitle.setText(getString(R.string.menu_activity));

        toolbar = requireActivity().findViewById(R.id.toolbar);
        fm = getChildFragmentManager();
        /*if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.popBackStack(this.toString(), 0);
        }*/

        firstButton = root.findViewById(R.id.firstButton);
        secButton = root.findViewById(R.id.secButton);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setVisibility(View.VISIBLE);

        if (isFirst) {
            secButton.setTextColor(requireContext().getColor(R.color.grey));
            firstButton.setOnClickListener(v -> {
                clear();
                firstButton.setTextColor(primaryColDark);

                fm.beginTransaction().replace(R.id.act_list, FirstHalfFrag.newInstance()).addToBackStack(null).commit();
            });
            secButton.setOnClickListener(v -> {
                if (isFirst)
                    Snackbar.make(v, "Please complete First Year", Snackbar.LENGTH_SHORT).show();
            });
        } else if (isSec) {
            secButton.setOnClickListener(v -> {
                clear();
                secButton.setTextColor(primaryColDark);
                firstButton.setTextColor(blackishy);

                fm.beginTransaction().replace(R.id.act_list, SecHalfFrag.newInstance()).addToBackStack(null).commit();
            });

            firstButton.setOnClickListener(view1 -> {
                clear();
                firstButton.setTextColor(primaryColDark);
                secButton.setTextColor(blackishy);

                fm.beginTransaction().replace(R.id.act_list, FirstHalfFrag.newInstance()).addToBackStack(null).commit();
            });
        } else {
            firstButton.setOnClickListener(v -> {
                clear();
                firstButton.setTextColor(primaryColDark);

                fm.beginTransaction().replace(R.id.act_list, FirstHalfFrag.newInstance()).addToBackStack(null).commit();
            });
        }
    }

    public void clear() {
        Fragment simpleFragment = getChildFragmentManager().findFragmentById(R.id.act_list);

        Log.e("AAA", "clear() called ActivityFrag " + simpleFragment + fm.getBackStackEntryCount());
        if (simpleFragment instanceof FirstHalfFrag || simpleFragment instanceof SecHalfFrag) {
            //((Helper) requireActivity()).closeFragment(simpleFragment);
            fm.popBackStack();
            //simpleFragment.clear();
        }
    }
}