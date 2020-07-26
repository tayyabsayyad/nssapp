package com.test.nss.ui.activity;

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

public class ActivityFragment extends Fragment {

    View root;
    Toolbar toolbar;
    Button firstButton;
    Button secButton;

    FragmentManager fm;
    TextView toolbarTitle;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_activity, container, false);
        toolbarTitle = requireActivity().findViewById(R.id.titleTool);
        toolbarTitle.setText(getString(R.string.menu_activity));

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
        toolbar.setVisibility(View.VISIBLE);

        firstButton.setOnClickListener(v -> {
            firstButton.setTextColor(primaryColDark);
            secButton.setTextColor(blackish);

            fm.beginTransaction().replace(R.id.act_list, new FirstHalfFrag()).addToBackStack("ActivityFrag").commit();
        });

        secButton.setOnClickListener(v -> {
            secButton.setTextColor(blackish);
            firstButton.setTextColor(primaryColDark);

            fm.beginTransaction().replace(R.id.act_list, new SecHalfFrag()).addToBackStack("ActivityFrag").commit();
        });
    }

    public void onDetach() {
        super.onDetach();
        if (fm.getBackStackEntryCount() > 0) {
            //Log.e("ActivityFragment", "onDetach: " + fm.getBackStackEntryCount());
            fm.popBackStack("ActivityFrag", 0);
        }
    }
}