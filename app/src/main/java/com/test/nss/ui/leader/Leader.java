package com.test.nss.ui.leader;

import android.animation.ObjectAnimator;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.test.nss.DataBaseHelper;
import com.test.nss.DatabaseAdapter;
import com.test.nss.R;
import com.test.nss.api.RetrofitClient;
import com.test.nss.ediary;
import com.test.nss.ui.onClickInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class Leader extends Fragment {

    TextView toolbarTitle;
    List<AdapterDataLeader> dataLeaderList, dataLeaderListAll;
    onClickInterface onClickInterface, onClickInterface2;
    View root;
    RecyclerView recViewLeader, recViewLeaderAll;
    CardView leader, leaderAll;

    TextView isEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_leader, container, false);
        dataLeaderList = addActData();
        dataLeaderListAll = addActAllData();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbarTitle = requireActivity().findViewById(R.id.titleTool);
        toolbarTitle.setText(getString(R.string.menu_leader));
        isEmpty = root.findViewById(R.id.isEmpty);

        leader = root.findViewById(R.id.volAct);
        leaderAll = root.findViewById(R.id.volActAll);

        recViewLeader = root.findViewById(R.id.vecLeaderList);
        recViewLeaderAll = root.findViewById(R.id.vecLeaderListAll);

        if (dataLeaderList.isEmpty())
            isEmpty.setText("Yay!\nNo Activities\nPending to approve");

        leader.setOnClickListener(view1 -> {
            if (dataLeaderList.isEmpty()) {
                isEmpty.setVisibility(View.VISIBLE);
                isEmpty.setText("Yay!\nNo Activities\nPending to approve");
            } else
                isEmpty.setVisibility(View.GONE);
            recViewLeaderAll.setVisibility(View.GONE);
            recViewLeader.setVisibility(View.VISIBLE);
        });

        leaderAll.setOnClickListener(view1 -> {
            isEmpty.setVisibility(View.GONE);
            recViewLeaderAll.setVisibility(View.VISIBLE);
            recViewLeader.setVisibility(View.GONE);
        });


        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        onClickInterface = abc -> {
            leaderAll.setVisibility(View.GONE);
            leader.setVisibility(View.GONE);
            new Handler().postDelayed(() -> {
                Log.e("here", "onViewCreated: " + abc);
                ModifyVolunteer modifyVolunteer = new ModifyVolunteer();
                Bundle args = new Bundle();
                args.putString("thisVec", abc);
                modifyVolunteer.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.detailsModify, modifyVolunteer, "ModifyVolunteer").addToBackStack("Leader").commit();
                recViewLeader.setVisibility(View.GONE);
            }, 100);
        };

        onClickInterface2 = abc1 -> {
            leaderAll.setVisibility(View.GONE);
            leader.setVisibility(View.GONE);
            Call<ResponseBody> getVecDet = RetrofitClient.getInstance().getApi().volActVec("Token "+ ediary.AUTH_TOKEN, abc1);
            getVecDet.enqueue(new Callback<ResponseBody>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            JSONArray j = new JSONArray(response.body().string());

                            if (j.length() >= 0) {
                                DatabaseAdapter mDbHelper = new DatabaseAdapter(requireContext());
                                mDbHelper.createDatabase();
                                mDbHelper.open();
                                deleteData("VolVecActAll");

                                for (int i = 0; i < j.length(); i++) {
                                    mDbHelper.insertVolVecAllAct(
                                            j.getJSONObject(i).getInt("id"),
                                            "",
                                            j.getJSONObject(i).getString("Date"),
                                            j.getJSONObject(i).getInt("Hours"),
                                            j.getJSONObject(i).getString("VEC"),
                                            j.getJSONObject(i).getString("ActivityName"),
                                            j.getJSONObject(i).getString("AssignedActivityName"),
                                            j.getJSONObject(i).getString("State")
                                    );
                                }
                                mDbHelper.setApproved("VolVecActAll");
                                mDbHelper.close();
                            }
                        } catch (JSONException | IOException e) {
                            Log.e("Failed", e.toString());
                            e.printStackTrace();
                        }
                        //Log.e("here", "onViewCreated: " + abc1);
                        ViewVolunteer viewVolunteer = new ViewVolunteer();
                        Bundle args = new Bundle();
                        args.putString("thisVec2", abc1);
                        viewVolunteer.setArguments(args);
                        fragmentManager.beginTransaction().replace(R.id.detailsModify, viewVolunteer, "ViewVolunteer").addToBackStack("Leader").commit();
                    }
                    else if (response.errorBody() != null) {
                        try {
                            Log.e("Here", "onResponse: "+response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else
                        Snackbar.make(view, "Device offline", Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        };

        MyListAdapterLeader leaderActDataAdapter = new MyListAdapterLeader(dataLeaderList, requireContext(), onClickInterface);
        MyListAdapterLeader leaderActAllDataAdapter = new MyListAdapterLeader(dataLeaderListAll, requireContext(), onClickInterface2);

        recViewLeader.setLayoutManager(new LinearLayoutManager(requireContext()));
        recViewLeader.setAdapter(leaderActDataAdapter);

        recViewLeaderAll.setLayoutManager(new LinearLayoutManager(requireContext()));
        recViewLeaderAll.setAdapter(leaderActAllDataAdapter);

        //Log.e("Here", "onViewCreated: "+dataLeaderListAll.get(0).getvolVec() + dataLeaderListAll.get(0).getVolName() + dataLeaderListAll.get(0).getvolVec());
    }

    public List<AdapterDataLeader> addActData() {
        ArrayList<AdapterDataLeader> data3 = new ArrayList<>();

        DatabaseAdapter mDbHelper = new DatabaseAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();
        Cursor c3 = mDbHelper.getVec();

        while (c3.moveToNext()) {
            Cursor c = mDbHelper.getVol(c3.getString(c3.getColumnIndex("VEC")));
            c.moveToFirst();
            data3.add(new AdapterDataLeader(
                    c3.getString(c3.getColumnIndex("VEC")),
                    c.getString(c.getColumnIndex("First_name"))
            ));
        }
        mDbHelper.close();
        return data3;
    }

    public List<AdapterDataLeader> addActAllData() {
        ArrayList<AdapterDataLeader> data3 = new ArrayList<>();

        DatabaseAdapter mDbHelper = new DatabaseAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();
        Cursor c3 = mDbHelper.getVecAll();

        while (c3.moveToNext()) {
            Cursor c = mDbHelper.getVolAll(c3.getString(c3.getColumnIndex("VEC")));
            c.moveToFirst();
            data3.add(new AdapterDataLeader(
                    c3.getString(c3.getColumnIndex("VEC")),
                    c.getString(c.getColumnIndex("First_name"))
            ));
        }
        mDbHelper.close();
        return data3;
    }

    private void deleteData(String table) {
        DatabaseAdapter mDbHelper2 = new DatabaseAdapter(requireContext());
        mDbHelper2.createDatabase();
        mDbHelper2.open();
        DataBaseHelper mDb2 = new DataBaseHelper(requireContext());
        SQLiteDatabase m = mDb2.getWritableDatabase();
        m.execSQL("DELETE FROM " + table);
        mDbHelper2.close();
        m.close();
        mDb2.close();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            //fm.beginTransaction().remove(Objects.requireNonNull(fm.findFragmentByTag("Leader"))).commit();
            //fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.popBackStack("Leader", 0);
        }
    }
}