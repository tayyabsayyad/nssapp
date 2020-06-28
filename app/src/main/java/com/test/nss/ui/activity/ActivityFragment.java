package com.test.nss.ui.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.test.nss.R;
import com.test.nss.ediary;
import com.test.nss.ui.main.MainFragment;

import java.util.Objects;

public class ActivityFragment extends Fragment {

    private ActivityViewModel activityViewModel;
    Button firstButton;
    Button secButton;
    ImageView home;
    View line;
    TextView nssFY;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        activityViewModel =
                ViewModelProviders.of(this).get(ActivityViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_activity, container, false);
        //final TextView textView = root.findViewById(R.id.text_activity);
        firstButton = (Button) root.findViewById(R.id.firstButton);
        secButton = (Button) root.findViewById(R.id.secButton);
        home = root.findViewById(R.id.homeButton);
        line = root.findViewById(R.id.line_main);
        nssFY = root.findViewById(R.id.nssFY);

        Typeface google_sans_bold = Typeface.createFromAsset(requireActivity().getAssets(), "fonts/google_sans_bold.ttf");
        nssFY.setTypeface(google_sans_bold);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), "Help needed", Toast.LENGTH_SHORT).show();
            }
        });

        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstButton.setTextColor(Color.WHITE);
                firstButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
                secButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.blackish));
                secButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent));
                line.setVisibility(View.VISIBLE);
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
            }
        });
        activityViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }
}