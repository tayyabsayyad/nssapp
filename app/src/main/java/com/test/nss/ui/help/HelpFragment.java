package com.test.nss.ui.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.test.nss.R;
import com.test.nss.ui.main.MainFragment;

public class HelpFragment extends Fragment {

    View root;
    Toolbar toolbar;
    Button home;
    LinearLayout emailNoPO;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_help, container, false);
        toolbar = root.findViewById(R.id.toolbar);
        home = root.findViewById(R.id.homeButton);
        emailNoPO = root.findViewById(R.id.emailNoPO);
        emailNoPO.setVisibility(View.GONE);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        home.setOnClickListener((View.OnClickListener) view1 -> {
            MainFragment mainFragment = new MainFragment();
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, mainFragment, mainFragment.getTag()).commit();
            toolbar.setTitle(getString(R.string.main_frag));
        });
        toolbar.setVisibility(View.GONE);
    }
}