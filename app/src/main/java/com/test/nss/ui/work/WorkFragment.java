package com.test.nss.ui.work;

import android.os.Bundle;
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

import static com.test.nss.ediary.blackish;
import static com.test.nss.ediary.isFirst;
import static com.test.nss.ediary.primaryColDark;

public class WorkFragment extends Fragment {

    View root;
    Toolbar toolbar;
    TextView firstButton;
    TextView secButton;
    FragmentManager fm;
    TextView toolbarTitle;
    TextView hoursInfo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_work, container, false);

        toolbarTitle = requireActivity().findViewById(R.id.titleTool);
        toolbarTitle.setText(getString(R.string.menu_work_hours));

        hoursInfo = root.findViewById(R.id.hoursInfo);

        firstButton = root.findViewById(R.id.firstButton);
        secButton = root.findViewById(R.id.secButton);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = requireActivity().findViewById(R.id.toolbar);

        toolbar.setVisibility(View.VISIBLE);
        fm = requireActivity().getSupportFragmentManager();

        if (isFirst) {
            secButton.setTextColor(requireContext().getColor(R.color.grey));
            firstButton.setOnClickListener(v -> {
                firstButton.setTextColor(primaryColDark);

                hoursInfo.setVisibility(View.GONE);
                fm.beginTransaction().replace(R.id.work_details, new WorkDetailsFirstFrag(requireContext())).addToBackStack("WorkFrag").commit();
            });
            secButton.setOnClickListener(v -> {
                if (isFirst)
                    Snackbar.make(v, "Please complete First Year", Snackbar.LENGTH_SHORT).show();
            });
        } else {
            secButton.setOnClickListener(v -> {
                secButton.setTextColor(primaryColDark);
                firstButton.setTextColor(blackish);

                hoursInfo.setVisibility(View.GONE);
                fm.beginTransaction().replace(R.id.work_details, new WorkDetailsSecFrag()).addToBackStack("WorkFrag").commit();
            });

            firstButton.setOnClickListener(view1 -> {
                firstButton.setTextColor(primaryColDark);
                secButton.setTextColor(blackish);

                hoursInfo.setVisibility(View.GONE);
                fm.beginTransaction().replace(R.id.work_details, new WorkDetailsFirstFrag(requireContext())).addToBackStack("WorkFrag").commit();
            });
        }
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