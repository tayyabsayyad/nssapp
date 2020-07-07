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
import com.test.nss.fyAct;
import com.test.nss.syAct;

public class MainFragment extends Fragment {

    public View root;
    Button firstButton;
    Button secButton;
    ImageView home;
    View line;
    TextView nssFY;
    //LinearLayout nssDetails;

    Toolbar toolbar;
    /*Button mainUniv;
    Button mainArea;
    Button mainClg;
    View mainOne;
    View mainTwo;*/
    TextView mu;
    TextView dbit;
    LinearLayout mainHeader;

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
        //nssDetails = root.findViewById(R.id.nss_details);

        toolbar = root.findViewById(R.id.toolbar);

        mainHeader = root.findViewById(R.id.main_header);

        /*mainOne = root.findViewById(R.id.main_lineone);
        mainTwo = root.findViewById(R.id.main_linetwo);
        mainUniv = root.findViewById(R.id.main_univ);
        mainArea = root.findViewById(R.id.main_area);
        mainClg = root.findViewById(R.id.main_clg);*/

        //home.setVisibility(View.INVISIBLE);

        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainHeader.setVisibility(View.GONE);
                firstButton.setTextColor(Color.WHITE);
                firstButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
                secButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.blackish));
                secButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent));
                line.setVisibility(View.VISIBLE);
                //nssDetails.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.GONE);
                //home.setVisibility(View.VISIBLE);

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.halves_frame, new fyAct()).addToBackStack(this.toString()).commit();
            }
        });

        secButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainHeader.setVisibility(View.GONE);
                secButton.setTextColor(Color.WHITE);
                secButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
                firstButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.blackish));
                firstButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent));
                line.setVisibility(View.VISIBLE);
                //nssDetails.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.GONE);
                //home.setVisibility(View.VISIBLE);
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.halves_frame, new syAct()).addToBackStack(this.toString()).commit();
            }
        });
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
                toolbar.setTitle(getString(R.string.main_frag));
                toolbar.setVisibility(View.VISIBLE);
                if (mainHeader.getVisibility() == View.GONE || mainHeader.getVisibility() == View.INVISIBLE) {
                    mainHeader.setVisibility(View.VISIBLE);
                }
                onDetach();
                resetColor();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private void resetColor() {
        firstButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.blackish));
        firstButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent));
        secButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent));
        secButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.blackish));
        line.setVisibility(View.GONE);
    }
}