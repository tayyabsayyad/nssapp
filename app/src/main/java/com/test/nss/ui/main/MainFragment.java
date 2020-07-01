package com.test.nss.ui.main;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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

public class MainFragment extends Fragment {

    public View root;
    Button firstButton;
    Button secButton;
    ImageView home;
    View line;
    TextView nssFY;
    LinearLayout nssDetails;

    Toolbar toolbar;
    Button mainUniv;
    Button mainArea;
    Button mainClg;
    View mainOne;
    View mainTwo;
    TextView mu;
    TextView dbit;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_main, container, false);

        Typeface typefaceBold = Typeface.createFromAsset(requireActivity().getAssets(), "fonts/google_sans_bold.ttf");
        Typeface typeface = Typeface.createFromAsset(requireActivity().getAssets(), "fonts/google_sans.ttf");

        mu = root.findViewById(R.id.main_mu);
        dbit = root.findViewById(R.id.main_dbit);
        mu.setTypeface(typefaceBold);
        dbit.setTypeface(typeface);

        firstButton = root.findViewById(R.id.firstButton);
        secButton = root.findViewById(R.id.secButton);

        home = root.findViewById(R.id.homeButton);
        line = root.findViewById(R.id.line_main);
        nssFY = root.findViewById(R.id.nssFY);
        nssDetails = root.findViewById(R.id.nss_details);

        toolbar = root.findViewById(R.id.toolbar);

        mainOne = root.findViewById(R.id.main_lineone);
        mainTwo = root.findViewById(R.id.main_linetwo);
        mainUniv = root.findViewById(R.id.main_univ);
        mainArea = root.findViewById(R.id.main_area);
        mainClg = root.findViewById(R.id.main_clg);
        home.setVisibility(View.INVISIBLE);

        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstButton.setTextColor(Color.WHITE);
                firstButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
                secButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.blackish));
                secButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent));
                line.setVisibility(View.VISIBLE);
                nssDetails.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.GONE);
                home.setVisibility(View.VISIBLE);
            }
        });

        secButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secButton.setTextColor(Color.WHITE);
                secButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
                firstButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.blackish));
                firstButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent));
                line.setVisibility(View.VISIBLE);
                nssDetails.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.GONE);
                home.setVisibility(View.VISIBLE);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        home = root.findViewById(R.id.homeButton);
        toolbar = requireActivity().findViewById(R.id.toolbar);
        changeBg();

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainFragment mainFragment = new MainFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

                assert fragmentManager != null;
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, mainFragment, mainFragment.getTag()).commit();
                toolbar.setTitle(getString(R.string.main_frag));
                toolbar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void changeBg() {
        mainUniv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainUniv.setTextColor(Color.WHITE);
                mainUniv.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
                mainClg.setBackgroundColor(Color.TRANSPARENT);
                mainClg.setTextColor(Color.BLACK);
                mainArea.setBackgroundColor(Color.TRANSPARENT);
                mainOne.setVisibility(View.VISIBLE);
                mainTwo.setVisibility(View.VISIBLE);
                mainArea.setTextColor(Color.BLACK);
            }
        });

        mainArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainArea.setTextColor(Color.WHITE);
                mainArea.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
                mainClg.setBackgroundColor(Color.TRANSPARENT);
                mainClg.setTextColor(Color.BLACK);
                mainUniv.setBackgroundColor(Color.TRANSPARENT);
                mainUniv.setTextColor(Color.BLACK);
                mainOne.setVisibility(View.VISIBLE);
                mainTwo.setVisibility(View.VISIBLE);
            }
        });

        mainClg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainClg.setTextColor(Color.WHITE);
                mainClg.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
                mainUniv.setBackgroundColor(Color.TRANSPARENT);
                mainUniv.setTextColor(Color.BLACK);
                mainArea.setBackgroundColor(Color.TRANSPARENT);
                mainArea.setTextColor(Color.BLACK);
                mainOne.setVisibility(View.VISIBLE);
                mainTwo.setVisibility(View.VISIBLE);
            }
        });
    }
}