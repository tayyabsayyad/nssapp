package com.test.nss.ui.work;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.test.nss.R;

import static com.test.nss.ediary.blackish;
import static com.test.nss.ediary.primaryColDark;
import static com.test.nss.ediary.transparent;

public class WorkFragment extends Fragment {

    View root;
    Toolbar toolbar;
    Button firstButton;
    Button secButton;
    FragmentManager fm;
    TextView toolbarTitle;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_work, container, false);

        toolbarTitle = requireActivity().findViewById(R.id.titleTool);
        toolbarTitle.setText(getString(R.string.menu_work_hours));

        firstButton = root.findViewById(R.id.firstButton);
        secButton = root.findViewById(R.id.secButton);
        fm = requireActivity().getSupportFragmentManager();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = requireActivity().findViewById(R.id.toolbar);

        toolbar.setVisibility(View.VISIBLE);

        firstButton.setOnClickListener(v -> {
            firstButton.setTextColor(primaryColDark);
            firstButton.setBackgroundColor(transparent);
            secButton.setTextColor(blackish);
            secButton.setBackgroundColor(transparent);

            fm.beginTransaction().replace(R.id.work_details, new WorkDetailsFirstFrag(requireContext())).addToBackStack("MainFrag").commit();
        });

        secButton.setOnClickListener(v -> {
            secButton.setTextColor(primaryColDark);
            secButton.setBackgroundColor(transparent);
            firstButton.setTextColor(blackish);
            firstButton.setBackgroundColor(transparent);

            fm.beginTransaction().replace(R.id.work_details, new WorkDetailsSecFrag()).addToBackStack("MainFrag").commit();
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