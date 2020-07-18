package com.test.nss.ui.main;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.test.nss.R;
import com.test.nss.TestAdapter;
import com.test.nss.api.RetrofitClient;
import com.test.nss.startActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class AddDetailsActivity extends Fragment {
    View huh;
    ConstraintLayout constFyAct;
    LinearLayout nssHalvesLinear;
    LinearLayout actInputHeader;
    ArrayList<String> clgList;
    ArrayList<String> actAssignList;
    ArrayList<String> actAssignListId;

    DatePickerDialog.OnDateSetListener onDateSetListener;
    TextView clgCode;
    TextView actId;

    Button addSend;
    Button add;
    Button backActDetail;

    ConstraintLayout campActIn;

    int whichAct;
    int act;
    private TextView actDate;
    private EditText actHour;
    private Spinner drpdownactClg;
    private Spinner drpdownactName;
    private Spinner drpdownactAssignName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        huh = inflater.inflate(R.layout.fragment_add_details_activity, container, false);
        clgList = getClgList();
        actAssignList = getAssignActList();

        constFyAct = requireActivity().findViewById(R.id.constFyAct);
        nssHalvesLinear = requireActivity().findViewById(R.id.nss_halves_Linear);
        actInputHeader = requireActivity().findViewById(R.id.actInputHeader);

        if (getArguments() != null) {
            whichAct = getArguments().getInt("whichAct");
            act = getArguments().getInt("act");
        } else {
            whichAct = -1;
            act = -1;
        }
        Log.e("AA", "" + whichAct);
        Log.e("AA", "" + act);

        backActDetail = huh.findViewById(R.id.backActDetail);

        return huh;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        constFyAct.setVisibility(View.GONE);
        nssHalvesLinear.setVisibility(View.GONE);
        actInputHeader.setVisibility(View.VISIBLE);
        clgCode = huh.findViewById(R.id.clgCode);
        addSend = huh.findViewById(R.id.addSend);
        campActIn = huh.findViewById(R.id.camp_act_in);
        add = requireActivity().findViewById(R.id.add);
        actId = huh.findViewById(R.id.actdetailId);

        actDate = huh.findViewById(R.id.actDate);
        actHour = huh.findViewById(R.id.actHour);

        drpdownactAssignName = huh.findViewById(R.id.drpdown_actAssignName);
        drpdownactClg = huh.findViewById(R.id.drpdown_actClg);
        drpdownactName = huh.findViewById(R.id.drpdown_actName);

        actAssignListId = getAssignActListId();

        final Calendar cal = Calendar.getInstance();
        actDate.setOnClickListener(view1 -> {
            int dd = cal.get(Calendar.DAY_OF_MONTH);
            int mm = cal.get(Calendar.MONTH);
            int yr = cal.get(Calendar.YEAR);

            DatePickerDialog d = new DatePickerDialog(
                    requireContext(),
                    R.style.datePick,
                    onDateSetListener,
                    yr, mm, dd
            );
            if (d.getWindow() != null)
                //d.getWindow().set(new ColorDrawable(requireContext().getColor(R.color.bla)));
                d.show();
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1 = i1 + 1;
                String date = i + "-" + i1 + "-" + i2;
                actDate.setText(date);
            }
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, clgList);
        drpdownactClg.setAdapter(adapter);

        ArrayAdapter<String> a = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, actAssignList);
        drpdownactAssignName.setAdapter(a);

        /*drpdownactAssignName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                actId.setText(actAssignList.get(i));
                Log.e("AA", actAssignList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        String actName = getResources().getStringArray(R.array.valOfActNames)[act];
        addSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!actDate.getText().toString().equals("") && drpdownactAssignName.getSelectedItem() != null
                        && !isEmpty(actHour)
                        //&& !actId.getText().toString().equals("")
                        && drpdownactClg.getSelectedItem() != null
                        && drpdownactName.getSelectedItem() != null) {
                    actId.setText(actAssignListId.get(drpdownactAssignName.getSelectedItemPosition()));
                    TestAdapter mDbHelper = new TestAdapter(requireContext());
                    mDbHelper.createDatabase();
                    mDbHelper.open();

                    Log.e("hmm", "" + whichAct);
                    Log.e("hmm", "" + actDate.getText().toString());
                    Log.e("hmm", "" + drpdownactAssignName.getSelectedItem().toString());
                    Log.e("hmm", "" + actId.getText().toString());

                    mDbHelper.insertAct(
                            startActivity.VEC,
                            whichAct,
                            actDate.getText().toString(),
                            drpdownactAssignName.getSelectedItem().toString(),
                            //actId.getText().toString(),
                            actHour.getText().toString()
                    );
                    mDbHelper.close();
                    if (isNetworkAvailable()) {
                        Log.e("AOO", "" + actName);
                        Log.e("AOO", "" + drpdownactAssignName.getSelectedItem().toString());
                        Log.e("AOO", "" + whichAct);
                        Log.e("AOO", "" + actId.getText().toString());

                        Call<ResponseBody> pushActList = RetrofitClient.getInstance().getApi().sendActList(
                                "Token " + startActivity.AUTH_TOKEN,
                                startActivity.VEC,
                                Integer.parseInt(actId.getText().toString()),// AAA
                                Integer.parseInt(actHour.getText().toString()),
                                actDate.getText().toString(),
                                //drpdownactAssignName.getSelectedItem().toString(),
                                whichAct,
                                1
                        );
                        pushActList.enqueue(new Callback<ResponseBody>() {
                            @Override
                            @EverythingIsNonNull
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    Toast.makeText(requireContext(), "Data Entered", Toast.LENGTH_SHORT).show();

                                    FragmentManager fm = requireActivity().getSupportFragmentManager();
                                    fm.popBackStack("AddDetailsActivity", 0);
                                } else if (response.errorBody() != null) {
                                    try {
                                        Log.e("onResponse:error", response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            @EverythingIsNonNull
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("onFail", t.toString());
                            }
                        });
                    }
                } else
                    Toast.makeText(requireContext(), "Device offline", Toast.LENGTH_SHORT).show();
                constFyAct.setVisibility(View.VISIBLE);
                nssHalvesLinear.setVisibility(View.VISIBLE);
                add.setVisibility(View.GONE);

                actInputHeader.setVisibility(View.GONE);
                campActIn.setVisibility(View.GONE);
            }
        });

        backActDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                constFyAct.setVisibility(View.VISIBLE);
                nssHalvesLinear.setVisibility(View.VISIBLE);

                actInputHeader.setVisibility(View.GONE);
                campActIn.setVisibility(View.GONE);
                add.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        constFyAct.setVisibility(View.VISIBLE);
        nssHalvesLinear.setVisibility(View.VISIBLE);
        add.setVisibility(View.GONE);

        actInputHeader.setVisibility(View.GONE);
        campActIn.setVisibility(View.GONE);
        //FragmentManager fm = requireActivity().getSupportFragmentManager();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean isEmpty(EditText e) {
        return e.getText().toString().trim().length() <= 0;
    }

    public ArrayList<String> getClgList() {
        ArrayList<String> data2 = new ArrayList<>();

        TestAdapter mDbHelper = new TestAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();
        Cursor c2 = mDbHelper.getClgList();
        Log.e("clgList:", "" + c2.getCount());

        while (c2.moveToNext()) {
            data2.add(c2.getString(c2.getColumnIndex("CollegeName")));
        }
        mDbHelper.close();
        return data2;
    }

    public ArrayList<String> getAssignActList() {
        assert getArguments() != null;
        whichAct = getArguments().getInt("whichAct");
        Log.e("Hmmm", "" + whichAct);

        ArrayList<String> data3 = new ArrayList<>();

        TestAdapter mDbHelper = new TestAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();
        Cursor c2 = mDbHelper.getActAssigActName(whichAct);
        Log.e("actAssign:", "" + c2.getCount());

        //c2.moveToFirst();
        while (c2.moveToNext()) {
            data3.add(c2.getString(c2.getColumnIndex("ActivityName")));
        }
        mDbHelper.close();
        return data3;
    }

    public ArrayList<String> getAssignActListId() {
        assert getArguments() != null;
        whichAct = getArguments().getInt("whichAct");

        ArrayList<String> dataId = new ArrayList<>();

        TestAdapter mDbHelper = new TestAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();
        Cursor c2 = mDbHelper.getActAssigActName(whichAct);
        Log.e("actAssign:", "" + c2.getCount());

        //c2.moveToFirst();
        while (c2.moveToNext()) {
            dataId.add(c2.getString(c2.getColumnIndex("id")));
        }
        mDbHelper.close();
        return dataId;
    }
}