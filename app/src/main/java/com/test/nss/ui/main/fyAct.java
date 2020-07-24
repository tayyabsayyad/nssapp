package com.test.nss.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.nss.MyButtonClickListener;
import com.test.nss.R;
import com.test.nss.SwipeHelper;
import com.test.nss.TestAdapter;
import com.test.nss.api.RetrofitClient;
import com.test.nss.startActivity;
import com.test.nss.ui.onClickInterface2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static com.test.nss.ediary.blackish;
import static com.test.nss.ediary.primaryCol;
import static com.test.nss.ediary.primaryColDark;

public class fyAct extends Fragment {

    View root;
    Button univ;
    Button area;
    Button clg;

    LinearLayout mainFy;
    MyButtonClickListener myButtonClickListener;

    Context mContext;

    RecyclerView univRecFy;
    RecyclerView areaRecFy;
    RecyclerView clgRecFy;

    List<AdapterDataMain> univListDataFy;
    List<AdapterDataMain> areaDataMainFy;
    List<AdapterDataMain> clgListDataFy;

    onClickInterface2 onClickInterface2;

    Button add;
    LinearLayout fragFy;

    int whichAct;
    int act;
    int newHours = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_fy_act, container, false);
        mContext = requireContext();

        // add data dynamically

        univ = root.findViewById(R.id.main_univ_fy);
        area = root.findViewById(R.id.main_area_fy);
        clg = root.findViewById(R.id.main_clg_fy);

        mainFy = root.findViewById(R.id.main_fy);
        add = root.findViewById(R.id.add);

        univRecFy = root.findViewById(R.id.univRecFy);
        areaRecFy = root.findViewById(R.id.areaRecFy);
        clgRecFy = root.findViewById(R.id.hoursRecFy);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragFy = root.findViewById(R.id.frag_fy);

        univ.setOnClickListener(v -> {
            whichAct = 13;
            act = 0;
            mainFy.setVisibility(View.VISIBLE);
            univRecFy.setVisibility(View.VISIBLE);
            areaRecFy.setVisibility(View.GONE);
            clgRecFy.setVisibility(View.GONE);
            add.setVisibility(View.VISIBLE);

            //univ.setBackgroundColor(primaryCol);
            univ.setTextColor(primaryColDark);
            //area.setBackgroundColor(transparent);
            area.setTextColor(Color.BLACK);
            //clg.setBackgroundColor(transparent);
            clg.setTextColor(Color.BLACK);
        });

        area.setOnClickListener(v -> {
            whichAct = 12;
            act = 1;
            mainFy.setVisibility(View.VISIBLE);
            add.setVisibility(View.VISIBLE);

            univRecFy.setVisibility(View.GONE);
            areaRecFy.setVisibility(View.VISIBLE);
            clgRecFy.setVisibility(View.GONE);

            //area.setBackgroundColor(primaryColDark);
            area.setTextColor(primaryColDark);
            //univ.setBackgroundColor(transparent);
            univ.setTextColor(Color.BLACK);
            //clg.setBackgroundColor(transparent);
            clg.setTextColor(Color.BLACK);
        });

        clg.setOnClickListener(v -> {
            mainFy.setVisibility(View.VISIBLE);

            whichAct = 11;
            act = 2;
            add.setVisibility(View.VISIBLE);
            univRecFy.setVisibility(View.GONE);
            areaRecFy.setVisibility(View.GONE);
            clgRecFy.setVisibility(View.VISIBLE);

            //clg.setBackgroundColor(primaryColDark);
            clg.setTextColor(primaryColDark);
            //univ.setBackgroundColor(transparent);
            univ.setTextColor(Color.BLACK);
            //area.setBackgroundColor(transparent);
            area.setTextColor(Color.BLACK);
        });

        clgListDataFy = addAct("First Year College");
        areaDataMainFy = addAct("First Year Area Based");
        univListDataFy = addAct("First Year University");

        onClickInterface2 = actID -> {
            Toast.makeText(mContext, "" + actID, Toast.LENGTH_SHORT).show();

        };

        RecyclerView recyclerViewUniv = root.findViewById(R.id.univRecFy);
        MyListAdapter adapterUniv = new MyListAdapter(univListDataFy, mContext, onClickInterface2);
        recyclerViewUniv.setHasFixedSize(true);
        recyclerViewUniv.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewUniv.setAdapter(adapterUniv);

        RecyclerView recyclerViewArea = root.findViewById(R.id.areaRecFy);
        MyListAdapter adapterArea = new MyListAdapter(areaDataMainFy, mContext, onClickInterface2);
        recyclerViewArea.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewArea.setAdapter(adapterArea);

        RecyclerView recyclerViewHours = root.findViewById(R.id.hoursRecFy);
        MyListAdapter adapterHours = new MyListAdapter(clgListDataFy, mContext, onClickInterface2);


        SwipeHelper swipeHelper = new SwipeHelper(mContext, recyclerViewHours) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Edit",
                        R.drawable.ic_back_24,
                        primaryCol,
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                ///Log.e("HMm", "E");
                                int actID = Integer.parseInt(clgListDataFy.get(viewHolder.getAdapterPosition()).getId());
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.inputDialog);
                                View viewInflated = LayoutInflater.from(mContext).inflate(R.layout.hours_input_layout, (ViewGroup) view, false);

                                EditText input = (EditText) viewInflated.findViewById(R.id.input);
                                builder.setView(viewInflated);

                                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        if (!input.getText().toString().trim().equals("")) {
                                            newHours = Integer.parseInt(input.getText().toString());

                                            if (newHours >= 1 && newHours <= 10) {
                                                adapterHours.notifyDataSetChanged();
                                                TestAdapter mdb = new TestAdapter(mContext);
                                                mdb.createDatabase();
                                                mdb.open();
                                                mdb.setDetails(newHours, actID);
                                                mdb.setSyncActDetails(0, actID);
                                                mdb.close();
                                                adapterHours.notifyDataSetChanged();
                                                Call<ResponseBody> putHours = RetrofitClient.getInstance().getApi().putHour(
                                                        "Token " + startActivity.AUTH_TOKEN,
                                                        newHours,
                                                        String.valueOf(actID));
                                                putHours.enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    @EverythingIsNonNull
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        if (response.isSuccessful() && response.body() != null)
                                                            Log.e("YA", "DONE");
                                                        else if (response.errorBody() != null) {
                                                            try {
                                                                Log.e("Error", "" + response.errorBody().string());
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    @EverythingIsNonNull
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                        Log.e("Error:onFailure", t.toString());
                                                    }
                                                });
                                            } else
                                                Toast.makeText(requireContext(), "Please enter atleast\nan hour between 1 to 10", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                if (viewInflated.getParent() != null)
                                    ((ViewGroup) viewInflated.getParent()).removeView(viewInflated);
                                builder.show();
                            }
                        }
                ));
            }
        };

        recyclerViewHours.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewHours.setAdapter(adapterHours);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeHelper);
        itemTouchHelper.attachToRecyclerView(recyclerViewHours);

        add.setOnClickListener(view1 -> {
            onDetach();

            mainFy.setVisibility(View.GONE);
            fragFy.setVisibility(View.GONE);
            univRecFy.setVisibility(View.GONE);
            areaRecFy.setVisibility(View.GONE);
            clgRecFy.setVisibility(View.GONE);
            univ.setBackgroundColor(Color.TRANSPARENT);
            clg.setBackgroundColor(Color.TRANSPARENT);
            area.setBackgroundColor(Color.TRANSPARENT);

            univ.setTextColor(blackish);
            area.setTextColor(blackish);
            clg.setTextColor(blackish);

            AddDetailsActivity detailsActivity = new AddDetailsActivity();
            Bundle args = new Bundle();
            args.putInt("whichAct", whichAct);
            args.putInt("act", act);
            detailsActivity.setArguments(args);

            Log.e("AA", "" + whichAct);
            Log.e("AA", "" + act);

            FragmentManager fm = requireActivity().getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.halves_frame, detailsActivity, "AddDetailsActivity").addToBackStack("fyAct").commit();

        });
    }

    // TODO: Check this
    @Override
    public void onDetach() {
        super.onDetach();
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.e("fyAct", "onDetach: " + fm.getBackStackEntryCount());
            fm.popBackStackImmediate("fyAct", 0);
            fm.popBackStack("fyAct", 0);
        }
    }

    public List<AdapterDataMain> addAct(String whichAct) {

        ArrayList<AdapterDataMain> data = new ArrayList<>();

        TestAdapter mDbHelper = new TestAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();

        Cursor c = mDbHelper.getActList(whichAct);
        Log.e("SSS", "" + c.getCount());
        while (c.moveToNext()) {

            data.add(new AdapterDataMain(
                    c.getString(c.getColumnIndex("Date")),
                    c.getString(c.getColumnIndex("ActivityName")),
                    c.getString(c.getColumnIndex("HoursWorked")),
                    c.getString(c.getColumnIndex("actID")))
            );
        }
        mDbHelper.close();
        return data;
    }
}