package com.test.nss.ui.help;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.nss.DatabaseAdapter;
import com.test.nss.R;

import java.util.ArrayList;
import java.util.List;

public class HelpFragment extends Fragment {

    View root;
    Toolbar toolbar;

    TextView namePo;
    TextView emailPo;
    TextView contactPo;
    TextView clgPo;

    TextView toolbarTitle;
    ImageView nssLogo;
    CardView contactUs;

    List<AdapterDataHelp> dataLeaderHelpList;
    RecyclerView recyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_help, container, false);

        toolbar = requireActivity().findViewById(R.id.toolbar);
        dataLeaderHelpList = addHelpData();

        //toolbar.setVisibility(View.GONE);
        nssLogo = requireActivity().findViewById(R.id.nss_logo);

        nssLogo.setVisibility(View.GONE);
        toolbarTitle = requireActivity().findViewById(R.id.titleTool);
        toolbarTitle.setText(getString(R.string.menu_help));

        recyclerView = root.findViewById(R.id.leaderRecDet);
        MyListAdapterHelp campActDataAdapter = new MyListAdapterHelp(dataLeaderHelpList, requireContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(campActDataAdapter);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contactUs = root.findViewById(R.id.contactUs);
        Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.shake);
        contactUs.startAnimation(animation);

        emailPo = root.findViewById(R.id.poEmail);
        contactPo = root.findViewById(R.id.poNo);
        namePo = root.findViewById(R.id.poName);
        clgPo = root.findViewById(R.id.poClg);

        DatabaseAdapter mDbHelper = new DatabaseAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactUs.startAnimation(animation);
            }
        });
        if (mDbHelper.getHelpData().size() > 0) {
            clgPo.setText(mDbHelper.getHelpData().get(0));
            namePo.setText(mDbHelper.getHelpData().get(1));
            emailPo.setText(String.format(getString(R.string.email) + " %s", mDbHelper.getHelpData().get(2)));
            contactPo.setText(String.format(getString(R.string.contact_no) + " +%s", mDbHelper.getHelpData().get(3)));
        } else {
            emailPo.setText(getString(R.string.email));
            contactPo.setText(getString(R.string.contact_no));
        }
        mDbHelper.close();
    }

    public List<AdapterDataHelp> addHelpData() {
        ArrayList<AdapterDataHelp> data3 = new ArrayList<>();

        DatabaseAdapter mDbHelper = new DatabaseAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();
        Cursor c3 = mDbHelper.getLeaders();

        while (c3.moveToNext()) {
            data3.add(new AdapterDataHelp(
                    c3.getString(c3.getColumnIndex("Name")),
                    "Email: "+c3.getString(c3.getColumnIndex("Email")),
                    "Contact No: "+c3.getString(c3.getColumnIndex("Contact")),
                    c3.getString(c3.getColumnIndex("CollegeName"))
            ));
        }
        mDbHelper.close();
        return data3;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        nssLogo.setVisibility(View.VISIBLE);
    }
}