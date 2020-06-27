package com.test.nss.ui.work;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.test.nss.R;

public class WorkFragment extends Fragment {

    private WorkViewModel workViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        workViewModel =
                ViewModelProviders.of(this).get(WorkViewModel.class);
        View root = inflater.inflate(R.layout.fragment_work, container, false);
        final TextView textView = root.findViewById(R.id.text_work);
        workViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}