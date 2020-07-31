package com.test.nss.ui.leader;

import android.content.Context;
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.test.nss.R;
import com.test.nss.SwipeHelperRight;
import com.test.nss.TestAdapter;
import com.test.nss.api.RetrofitClient;
import com.test.nss.ediary;

import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static com.test.nss.ediary.leaderId;
import static com.test.nss.ediary.primaryColDark;
import static com.test.nss.ediary.transparent;

public class ModifyVolunteer extends Fragment {

    RecyclerView recViewLeader, recViewVolUniv, recViewVolArea, recViewVolClg;
    List<AdapterDataVolunteer> dataVolListUniv, dataVolListArea, dataVolListClg;
    Button univ, area, clg, back;
    LinearLayout detailsVol;
    CardView cardModify;

    Context context;
    View root, line;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_modify_details, container, false);
        recViewLeader = requireActivity().findViewById(R.id.vecLeaderList);
        dataVolListUniv = addVolActData("First Year University");
        dataVolListArea = ListUtils.union(addVolActData("First Year Area Based One"), addVolActData("First Year Area Based Two"));
        dataVolListClg = addVolActData("First Year College");
        line = requireActivity().findViewById(R.id.line_details_bot);

        context = requireContext();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        detailsVol = root.findViewById(R.id.detailsVol);
        cardModify = root.findViewById(R.id.cardModify);

        univ = root.findViewById(R.id.univ);
        area = root.findViewById(R.id.area);
        clg = root.findViewById(R.id.clg);

        back = root.findViewById(R.id.back);

        recViewVolUniv = root.findViewById(R.id.recVecDetailUniv);
        MyListAdapterVolunteer adapterVolUniv = new MyListAdapterVolunteer(dataVolListUniv, context);

        recViewVolArea = root.findViewById(R.id.recVecDetailArea);
        MyListAdapterVolunteer adapterVolArea = new MyListAdapterVolunteer(dataVolListArea, context);

        recViewVolClg = root.findViewById(R.id.recVecDetailClg);
        MyListAdapterVolunteer adapterVolClg = new MyListAdapterVolunteer(dataVolListClg, context);
        recViewVolClg.setLayoutManager(new LinearLayoutManager(context));
        recViewVolClg.setAdapter(adapterVolClg);

        univ.setOnClickListener(view1 -> {
            detailsVol.setVisibility(View.VISIBLE);
            univ.setTextColor(primaryColDark);
            area.setTextColor(Color.BLACK);
            clg.setTextColor(Color.BLACK);
            recViewVolUniv.setVisibility(View.VISIBLE);
            recViewVolArea.setVisibility(View.GONE);
            recViewVolClg.setVisibility(View.GONE);
        });

        area.setOnClickListener(view1 -> {
            detailsVol.setVisibility(View.VISIBLE);
            area.setTextColor(primaryColDark);
            univ.setTextColor(Color.BLACK);
            clg.setTextColor(Color.BLACK);
            recViewVolArea.setVisibility(View.VISIBLE);
            recViewVolUniv.setVisibility(View.GONE);
            recViewVolClg.setVisibility(View.GONE);
        });

        clg.setOnClickListener(view1 -> {
            detailsVol.setVisibility(View.VISIBLE);
            clg.setTextColor(primaryColDark);
            univ.setTextColor(Color.BLACK);
            area.setTextColor(Color.BLACK);
            recViewVolClg.setVisibility(View.VISIBLE);
            recViewVolUniv.setVisibility(View.GONE);
            recViewVolArea.setVisibility(View.GONE);
        });

        back.setOnClickListener(view12 -> {
            onDetach();
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(Objects.requireNonNull(fragmentManager.findFragmentByTag("ModifyVolunteer")));
            cardModify.setVisibility(View.GONE);
        });

        // UNIV
        SwipeHelperRight swipeHelperRightHoursUniv = new SwipeHelperRight(context, recViewVolUniv) {

            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                TestAdapter mdbF = new TestAdapter(context);
                mdbF.createDatabase();
                mdbF.open();
                Cursor cF = mdbF.getVolState(Integer.parseInt(adapterVolUniv.list.get(viewHolder.getAdapterPosition()).getId()));
                cF.moveToFirst();
                if (
                        !cF.getString(cF.getColumnIndex("State")).equals("Approved") ||
                                cF.getString(cF.getColumnIndex("State")).equals("LeaderDelete") ||
                                cF.getString(cF.getColumnIndex("State")).equals("LeaderModified") ||
                                cF.getString(cF.getColumnIndex("State")).equals("Submitted")
                ) {
                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                                    context,
                                    "",
                                    R.drawable.ic_disapprove_24,
                                    transparent,
                                    pos -> {
                                        String thisVec = getArguments().getString("thisVec");
                                        int p;
                                        if (viewHolder.getAdapterPosition() == -1)
                                            p = viewHolder.getAdapterPosition() + 1;

                                        else if (viewHolder.getAdapterPosition() == dataVolListUniv.size())
                                            p = viewHolder.getAdapterPosition() - 1;

                                        else
                                            p = viewHolder.getAdapterPosition();

                                        Snackbar.make(view, "Deleted: " + thisVec, Snackbar.LENGTH_SHORT).show();
                                        adapterVolUniv.list.set(p, new AdapterDataVolunteer(
                                                        adapterVolUniv.list.get(p).getDate(),
                                                        adapterVolUniv.list.get(p).getAct(),
                                                        adapterVolUniv.list.get(p).getHours(),
                                                        adapterVolUniv.list.get(p).getId(),
                                                        "LeaderDelete"
                                                )
                                        );
                                        adapterVolUniv.notifyDataSetChanged();
                                        adapterVolUniv.notifyItemChanged(p);

                                        TestAdapter mdb = new TestAdapter(context);
                                        mdb.createDatabase();
                                        mdb.open();
                                        mdb.setStateVolAct("LeaderDelete", Integer.parseInt(adapterVolUniv.list.get(p).getId()));
                                        Cursor c = mdb.getActAssigActNameAdmin(adapterVolUniv.list.get(p).getAct());
                                        c.moveToFirst();
                                        Call<ResponseBody> putHour = RetrofitClient.getInstance().getApi().putHour(
                                                "Token " + ediary.AUTH_TOKEN,
                                                Integer.parseInt(adapterVolUniv.list.get(p).getHours()),
                                                thisVec,
                                                c.getInt(c.getColumnIndex("activityType")),
                                                c.getInt(c.getColumnIndex("id")),
                                                5,
                                                Integer.parseInt(adapterVolUniv.list.get(p).getId())
                                        );
                                        mdb.close();
                                        putHour.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            @EverythingIsNonNull
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.errorBody() != null) {
                                                    Log.e("error", "onResponse: " + response.errorBody().toString());
                                                } else if (response.isSuccessful()) {
                                                    Log.e("Done", "onResponse: " + adapterVolUniv.list.get(p).getId());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                            }
                                        });

                                    }
                            )
                    );

                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                                    context,
                                    "",
                                    R.drawable.ic_approve_24,
                                    transparent,
                                    pos -> {
                                        int p;
                                        if (viewHolder.getAdapterPosition() == -1)
                                            p = viewHolder.getAdapterPosition() + 1;

                                        else if (viewHolder.getAdapterPosition() == dataVolListUniv.size())
                                            p = viewHolder.getAdapterPosition() - 1;

                                        else
                                            p = viewHolder.getAdapterPosition();

                                        String thisVec = getArguments().getString("thisVec");
                                        Snackbar.make(view, "Approved: " + thisVec, Snackbar.LENGTH_SHORT).show();
                                        adapterVolUniv.list.set(p, new AdapterDataVolunteer(
                                                        adapterVolUniv.list.get(p).getDate(),
                                                        adapterVolUniv.list.get(p).getAct(),
                                                        adapterVolUniv.list.get(p).getHours(),
                                                        adapterVolUniv.list.get(p).getId(),
                                                        "Approved"
                                                )
                                        );
                                        adapterVolUniv.notifyDataSetChanged();
                                        adapterVolUniv.notifyItemChanged(p);

                                        TestAdapter mdb = new TestAdapter(context);
                                        mdb.createDatabase();
                                        mdb.open();
                                        Cursor c = mdb.getActAssigActNameAdmin(adapterVolUniv.list.get(p).getAct());
                                        mdb.setStateVolAct("Approved", Integer.parseInt(adapterVolUniv.list.get(p).getId()));
                                        c.moveToFirst();
                                        Call<ResponseBody> putHour = RetrofitClient.getInstance().getApi().putApprove(
                                                "Token " + ediary.AUTH_TOKEN,
                                                Integer.parseInt(adapterVolUniv.list.get(p).getHours()),
                                                thisVec,
                                                c.getInt(c.getColumnIndex("activityType")),
                                                c.getInt(c.getColumnIndex("id")),
                                                2,
                                                leaderId,
                                                Integer.parseInt(adapterVolUniv.list.get(p).getId())
                                        );
                                        mdb.close();

                                        putHour.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            @EverythingIsNonNull
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.errorBody() != null) {
                                                    Log.e("error", "onResponse: " + response.errorBody().toString());
                                                } else if (response.isSuccessful()) {
                                                    Log.e("Done", "onResponse: " + adapterVolUniv.list.get(p).getId());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                            }
                                        });

                                    }
                            )
                    );

                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                            context,
                            "",
                            R.drawable.ic_vol_edit_24,
                            transparent,
                            pos -> {
                                int p;
                                if (viewHolder.getAdapterPosition() == -1)
                                    p = viewHolder.getAdapterPosition() + 1;

                                else if (viewHolder.getAdapterPosition() == dataVolListUniv.size())
                                    p = viewHolder.getAdapterPosition() - 1;

                                else
                                    p = viewHolder.getAdapterPosition();

                                Log.e("This", "instantiateUnderlayButton: " + p);
                                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.inputDialog);
                                View viewInflated = LayoutInflater.from(context).inflate(R.layout.hours_input_layout, (ViewGroup) view, false);

                                EditText input = viewInflated.findViewById(R.id.input);
                                builder.setView(viewInflated);

                                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                                    dialog.cancel();
                                    adapterVolUniv.notifyItemChanged(p);
                                });
                                String thisVec = getArguments().getString("thisVec");
                                builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                            dialog.dismiss();
                                            if (!input.getText().toString().trim().equals("")) {
                                                int newHours = Integer.parseInt(input.getText().toString());

                                                //if (!dataVolListUniv.isEmpty())
                                                //  dataVolListUniv.clear();
                                                adapterVolUniv.notifyDataSetChanged();

                                                if (newHours >= 1 && newHours <= 10) {
                                                    adapterVolUniv.list.add(p + 1, new AdapterDataVolunteer(
                                                            adapterVolUniv.list.get(p).getDate(),
                                                            adapterVolUniv.list.get(p).getAct(),
                                                            String.valueOf(newHours),
                                                            adapterVolUniv.list.get(p).getId(),
                                                            "LeaderModified"
                                                    ));

                                                    adapterVolUniv.notifyDataSetChanged();
                                                    dataVolListUniv.remove(p);
                                                    adapterVolUniv.notifyItemInserted(p);

                                                    TestAdapter mdb = new TestAdapter(context);
                                                    mdb.createDatabase();
                                                    mdb.open();
                                                    Cursor c = mdb.getActAssigActNameAdmin(adapterVolUniv.list.get(p).getAct());
                                                    mdb.setStateVolAct("LeaderModified", Integer.parseInt(adapterVolUniv.list.get(p).getId()));
                                                    c.moveToFirst();
                                                    Call<ResponseBody> putHour = RetrofitClient.getInstance().getApi().putHour(
                                                            "Token " + ediary.AUTH_TOKEN,
                                                            Integer.parseInt(adapterVolUniv.list.get(p).getHours()),
                                                            thisVec,
                                                            c.getInt(c.getColumnIndex("activityType")),
                                                            c.getInt(c.getColumnIndex("id")),
                                                            6,
                                                            Integer.parseInt(adapterVolUniv.list.get(p).getId())
                                                    );
                                                    mdb.close();
                                                    putHour.enqueue(new Callback<ResponseBody>() {
                                                        @Override
                                                        @EverythingIsNonNull
                                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                            if (response.isSuccessful()) {
                                                                Snackbar.make(view, "Edited for: " + thisVec, Snackbar.LENGTH_SHORT).show();
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                        }
                                                    });
                                                } else
                                                    Toast.makeText(context, "Please enter in given range", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                );
                                if (viewInflated.getParent() != null)
                                    ((ViewGroup) viewInflated.getParent()).removeView(viewInflated);
                                builder.show();
                            }
                    ));
                    mdbF.close();
                }
            }
        };

        recViewVolUniv.setLayoutManager(new LinearLayoutManager(context));
        recViewVolUniv.setAdapter(adapterVolUniv);

        // AREA
        SwipeHelperRight swipeHelperRightHoursArea = new SwipeHelperRight(context, recViewVolArea) {

            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                TestAdapter mdbF = new TestAdapter(context);
                mdbF.createDatabase();
                mdbF.open();
                Cursor cF = mdbF.getVolState(Integer.parseInt(adapterVolArea.list.get(viewHolder.getAdapterPosition()).getId()));
                cF.moveToFirst();
                if (
                        !cF.getString(cF.getColumnIndex("State")).equals("Approved") ||
                                cF.getString(cF.getColumnIndex("State")).equals("LeaderDelete") ||
                                cF.getString(cF.getColumnIndex("State")).equals("LeaderModified") ||
                                cF.getString(cF.getColumnIndex("State")).equals("Submitted")
                ) {
                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                                    context,
                                    "",
                                    R.drawable.ic_disapprove_24,
                                    transparent,
                                    pos -> {
                                        String thisVec = getArguments().getString("thisVec");
                                        int p;
                                        if (viewHolder.getAdapterPosition() == -1)
                                            p = viewHolder.getAdapterPosition() + 1;

                                        else if (viewHolder.getAdapterPosition() == dataVolListArea.size())
                                            p = viewHolder.getAdapterPosition() - 1;

                                        else
                                            p = viewHolder.getAdapterPosition();

                                        Snackbar.make(view, "Deleted: " + thisVec, Snackbar.LENGTH_SHORT).show();
                                        adapterVolArea.list.set(p, new AdapterDataVolunteer(
                                                adapterVolArea.list.get(p).getDate(),
                                                adapterVolArea.list.get(p).getAct(),
                                                adapterVolArea.list.get(p).getHours(),
                                                adapterVolArea.list.get(p).getId(),
                                                "LeaderDelete"
                                        ));
                                        adapterVolArea.notifyDataSetChanged();
                                        adapterVolArea.notifyItemChanged(p);

                                        TestAdapter mdb = new TestAdapter(context);
                                        mdb.createDatabase();
                                        mdb.open();
                                        Cursor c = mdb.getActAssigActNameAdmin(adapterVolArea.list.get(p).getAct());
                                        mdb.setStateVolAct("LeaderDelete", Integer.parseInt(adapterVolArea.list.get(p).getId()));
                                        c.moveToFirst();
                                        mdb.close();

                                        Call<ResponseBody> putHour = RetrofitClient.getInstance().getApi().putHour(
                                                "Token " + ediary.AUTH_TOKEN,
                                                Integer.parseInt(adapterVolArea.list.get(p).getHours()),
                                                thisVec,
                                                c.getInt(c.getColumnIndex("activityType")),
                                                c.getInt(c.getColumnIndex("id")),
                                                5,
                                                Integer.parseInt(adapterVolArea.list.get(p).getId())
                                        );

                                        putHour.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            @EverythingIsNonNull
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.errorBody() != null) {
                                                    Log.e("error", "onResponse: " + response.errorBody().toString());
                                                } else if (response.isSuccessful()) {
                                                    Log.e("Done", "onResponse: " + adapterVolArea.list.get(p).getId());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                            }
                                        });

                                    }
                            )
                    );

                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                                    context,
                                    "",
                                    R.drawable.ic_approve_24,
                                    transparent,
                                    pos -> {
                                        int p;

                                        if (viewHolder.getAdapterPosition() == -1)
                                            p = viewHolder.getAdapterPosition() + 1;

                                        else if (viewHolder.getAdapterPosition() == dataVolListArea.size())
                                            p = viewHolder.getAdapterPosition() - 1;

                                        else
                                            p = viewHolder.getAdapterPosition();

                                        String thisVec = getArguments().getString("thisVec");
                                        Snackbar.make(view, "Approved: " + thisVec, Snackbar.LENGTH_SHORT).show();
                                        adapterVolArea.list.set(p, new AdapterDataVolunteer(
                                                adapterVolArea.list.get(p).getDate(),
                                                adapterVolArea.list.get(p).getAct(),
                                                adapterVolArea.list.get(p).getHours(),
                                                adapterVolArea.list.get(p).getId(),
                                                "Approved"
                                        ));

                                        TestAdapter mdb = new TestAdapter(context);
                                        mdb.createDatabase();
                                        mdb.open();
                                        Cursor c = mdb.getActAssigActNameAdmin(adapterVolArea.list.get(p).getAct());
                                        c.moveToFirst();
                                        mdb.setStateVolAct("Approved", Integer.parseInt(adapterVolArea.list.get(p).getId()));
                                        mdb.close();

                                        adapterVolArea.notifyDataSetChanged();
                                        adapterVolArea.notifyItemChanged(p);

                                        Call<ResponseBody> putHour = RetrofitClient.getInstance().getApi().putApprove(
                                                "Token " + ediary.AUTH_TOKEN,
                                                Integer.parseInt(adapterVolArea.list.get(p).getHours()),
                                                thisVec,
                                                c.getInt(c.getColumnIndex("activityType")),
                                                c.getInt(c.getColumnIndex("id")),
                                                2,
                                                leaderId,
                                                Integer.parseInt(adapterVolArea.list.get(p).getId())
                                        );

                                        putHour.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            @EverythingIsNonNull
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.errorBody() != null) {
                                                    Log.e("error", "onResponse: " + response.errorBody().toString());
                                                } else if (response.isSuccessful()) {
                                                    Log.e("Done", "onResponse: " + adapterVolArea.list.get(p).getId());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                            }
                                        });

                                    }
                            )
                    );

                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                            context,
                            "",
                            R.drawable.ic_vol_edit_24,
                            transparent,
                            pos -> {
                                int p;
                                if (viewHolder.getAdapterPosition() == -1)
                                    p = viewHolder.getAdapterPosition() + 1;

                                else if (viewHolder.getAdapterPosition() == dataVolListArea.size())
                                    p = viewHolder.getAdapterPosition() - 1;

                                else
                                    p = viewHolder.getAdapterPosition();

                                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.inputDialog);
                                View viewInflated = LayoutInflater.from(context).inflate(R.layout.hours_input_layout, (ViewGroup) view, false);

                                EditText input = viewInflated.findViewById(R.id.input);
                                builder.setView(viewInflated);

                                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                                    dialog.cancel();
                                    adapterVolArea.notifyItemChanged(p);
                                });

                                builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                            dialog.dismiss();
                                            if (!input.getText().toString().trim().equals("")) {
                                                int newHours = Integer.parseInt(input.getText().toString());

                                                if (newHours >= 1 && newHours <= 10) {
                                                    adapterVolArea.list.add(p + 1, new AdapterDataVolunteer(
                                                            adapterVolArea.list.get(p).getDate(),
                                                            adapterVolArea.list.get(p).getAct(),
                                                            String.valueOf(newHours),
                                                            adapterVolArea.list.get(p).getId(),
                                                            "LeaderModified"
                                                    ));
                                                    adapterVolArea.notifyDataSetChanged();
                                                    dataVolListArea.remove(p);
                                                    adapterVolArea.notifyItemChanged(p);

                                                    TestAdapter mdb = new TestAdapter(context);
                                                    mdb.createDatabase();
                                                    mdb.open();
                                                    mdb.setStateVolAct("LeaderModified", Integer.parseInt(dataVolListArea.get(p).getId()));
                                                    mdb.close();
                                                } else
                                                    Toast.makeText(context, "Enter in given range", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                );
                                if (viewInflated.getParent() != null)
                                    ((ViewGroup) viewInflated.getParent()).removeView(viewInflated);
                                builder.show();
                            }
                    ));
                    mdbF.close();
                }
            }
        };
        recViewVolArea.setLayoutManager(new LinearLayoutManager(context));
        recViewVolArea.setAdapter(adapterVolArea);

        // COLLEGE
        SwipeHelperRight swipeHelperRightHoursClg = new SwipeHelperRight(context, recViewVolClg) {

            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                TestAdapter mdbF = new TestAdapter(context);
                mdbF.createDatabase();
                mdbF.open();
                Cursor cF = mdbF.getVolState(Integer.parseInt(adapterVolClg.list.get(viewHolder.getAdapterPosition()).getId()));
                cF.moveToFirst();
                if (
                        !cF.getString(cF.getColumnIndex("State")).equals("Approved") ||
                                cF.getString(cF.getColumnIndex("State")).equals("LeaderDelete") ||
                                cF.getString(cF.getColumnIndex("State")).equals("LeaderModified") ||
                                cF.getString(cF.getColumnIndex("State")).equals("Submitted")
                ) {
                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                                    context,
                                    "",
                                    R.drawable.ic_disapprove_24,
                                    transparent,
                                    pos -> {
                                        String thisVec = getArguments().getString("thisVec");
                                        int p;
                                        if (viewHolder.getAdapterPosition() == -1)
                                            p = viewHolder.getAdapterPosition() + 1;

                                        else if (viewHolder.getAdapterPosition() == dataVolListClg.size())
                                            p = viewHolder.getAdapterPosition() - 1;

                                        else
                                            p = viewHolder.getAdapterPosition();

                                        Snackbar.make(view, "Deleted: " + thisVec, Snackbar.LENGTH_SHORT).show();
                                        adapterVolClg.list.set(p, new AdapterDataVolunteer(
                                                        adapterVolClg.list.get(p).getDate(),
                                                        adapterVolClg.list.get(p).getAct(),
                                                        adapterVolClg.list.get(p).getHours(),
                                                        adapterVolClg.list.get(p).getId(),
                                                        "LeaderDelete"
                                                )
                                        );

                                        TestAdapter mdb = new TestAdapter(context);
                                        mdb.createDatabase();
                                        mdb.open();
                                        Cursor c = mdb.getActAssigActNameAdmin(adapterVolClg.list.get(p).getAct());
                                        c.moveToFirst();
                                        mdb.setStateVolAct("LeaderDelete", Integer.parseInt(dataVolListClg.get(p).getId()));
                                        mdb.close();

                                        adapterVolClg.notifyDataSetChanged();
                                        adapterVolClg.notifyItemChanged(p);

                                        Call<ResponseBody> putHour = RetrofitClient.getInstance().getApi().putHour(
                                                "Token " + ediary.AUTH_TOKEN,
                                                Integer.parseInt(adapterVolClg.list.get(p).getHours()),
                                                thisVec,
                                                c.getInt(c.getColumnIndex("activityType")),
                                                c.getInt(c.getColumnIndex("id")),
                                                5,
                                                Integer.parseInt(adapterVolClg.list.get(p).getId())
                                        );

                                        putHour.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            @EverythingIsNonNull
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.errorBody() != null) {
                                                    Log.e("error", "onResponse: " + response.errorBody().toString());
                                                } else if (response.isSuccessful()) {
                                                    Log.e("Done", "onResponse: " + adapterVolClg.list.get(p).getId());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                            }
                                        });

                                    }
                            )
                    );

                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                                    context,
                                    "",
                                    R.drawable.ic_approve_24,
                                    transparent,
                                    pos -> {
                                        int p;
                                        if (viewHolder.getAdapterPosition() == -1)
                                            p = viewHolder.getAdapterPosition() + 1;

                                        else if (viewHolder.getAdapterPosition() == dataVolListClg.size())
                                            p = viewHolder.getAdapterPosition() - 1;

                                        else
                                            p = viewHolder.getAdapterPosition();

                                        String thisVec = getArguments().getString("thisVec");
                                        Snackbar.make(view, "Approved: " + thisVec, Snackbar.LENGTH_SHORT).show();
                                        adapterVolClg.list.set(p, new AdapterDataVolunteer(
                                                adapterVolClg.list.get(p).getDate(),
                                                adapterVolClg.list.get(p).getAct(),
                                                adapterVolClg.list.get(p).getHours(),
                                                adapterVolClg.list.get(p).getId(),
                                                "Approved"
                                        ));

                                        TestAdapter mdb = new TestAdapter(context);
                                        mdb.createDatabase();
                                        mdb.open();
                                        mdb.setStateVolAct("Approved", Integer.parseInt(dataVolListClg.get(p).getId()));
                                        Cursor c = mdb.getActAssigActNameAdmin(adapterVolClg.list.get(p).getAct());
                                        c.moveToFirst();
                                        mdb.close();
                                        adapterVolClg.notifyDataSetChanged();
                                        adapterVolClg.notifyItemChanged(p);

                                        Call<ResponseBody> putHour = RetrofitClient.getInstance().getApi().putApprove(
                                                "Token " + ediary.AUTH_TOKEN,
                                                Integer.parseInt(adapterVolClg.list.get(p).getHours()),
                                                thisVec,
                                                c.getInt(c.getColumnIndex("activityType")),
                                                c.getInt(c.getColumnIndex("id")),
                                                2,
                                                leaderId,
                                                Integer.parseInt(adapterVolClg.list.get(p).getId())
                                        );

                                        putHour.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            @EverythingIsNonNull
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.errorBody() != null) {
                                                    Log.e("error", "onResponse: " + response.errorBody().toString());
                                                } else if (response.isSuccessful()) {
                                                    Log.e("Done", "onResponse: " + adapterVolClg.list.get(p).getId());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                            }
                                        });

                                    }
                            )
                    );

                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                            context,
                            "",
                            R.drawable.ic_vol_edit_24,
                            transparent,
                            pos -> {
                                int p;
                                Log.e("og", "instantiateUnderlayButton: " + viewHolder.getAdapterPosition());
                                if (viewHolder.getAdapterPosition() == -1)
                                    p = viewHolder.getAdapterPosition() + 1;

                                else if (viewHolder.getAdapterPosition() == dataVolListClg.size())
                                    p = viewHolder.getAdapterPosition() - 1;

                                else
                                    p = viewHolder.getAdapterPosition();

                                Log.e("This", "instantiateUnderlayButton: " + adapterVolClg.list.get(p).getState());
                                Log.e("Here again", "instantiateUnderlayButton: " + p);
                                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.inputDialog);
                                View viewInflated = LayoutInflater.from(context).inflate(R.layout.hours_input_layout, (ViewGroup) view, false);

                                EditText input = viewInflated.findViewById(R.id.input);
                                builder.setView(viewInflated);

                                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                                    dialog.cancel();
                                    adapterVolClg.notifyItemChanged(p);
                                });

                                builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                            dialog.dismiss();

                                            if (!input.getText().toString().trim().equals("")) {
                                                int newHours = Integer.parseInt(input.getText().toString());
                                                Log.e("Here", "instantiateUnderlayButton: " + p);
                                                if (newHours >= 1 && newHours <= 10) {
                                                    adapterVolClg.list.add(p + 1, new AdapterDataVolunteer(
                                                            adapterVolClg.list.get(p).getDate(),
                                                            adapterVolClg.list.get(p).getAct(),
                                                            String.valueOf(newHours),
                                                            adapterVolClg.list.get(p).getId(),
                                                            "LeaderModified"
                                                    ));
                                                    TestAdapter mdb = new TestAdapter(context);
                                                    mdb.createDatabase();
                                                    mdb.open();
                                                    mdb.setStateVolAct("LeaderModified", Integer.parseInt(dataVolListClg.get(p).getId()));
                                                    mdb.close();

                                                    adapterVolClg.notifyDataSetChanged();
                                                    dataVolListClg.remove(p);
                                                    adapterVolClg.notifyItemChanged(p);
                                                } else
                                                    Toast.makeText(context, "Please enter in given range", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                );
                                if (viewInflated.getParent() != null)
                                    ((ViewGroup) viewInflated.getParent()).removeView(viewInflated);
                                builder.show();
                            }
                    ));
                    mdbF.close();
                }
            }
        };

        recViewVolClg.setLayoutManager(new LinearLayoutManager(context));
        recViewVolClg.setAdapter(adapterVolClg);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeHelperRightHoursUniv);
        itemTouchHelper.attachToRecyclerView(recViewVolUniv);

        ItemTouchHelper itemTouchHelper2 = new ItemTouchHelper(swipeHelperRightHoursArea);
        itemTouchHelper2.attachToRecyclerView(recViewVolArea);

        ItemTouchHelper itemTouchHelper3 = new ItemTouchHelper(swipeHelperRightHoursClg);
        itemTouchHelper3.attachToRecyclerView(recViewVolClg);
    }

    public List<AdapterDataVolunteer> addVolActData(String actName) {
        ArrayList<AdapterDataVolunteer> data3 = new ArrayList<>();

        TestAdapter mDbHelper = new TestAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();
        assert getArguments() != null;
        String thisVec = getArguments().getString("thisVec");

        Cursor c0 = mDbHelper.getVolDetails(actName, thisVec);
        int m = c0.getCount();

        c0.moveToFirst();
        while (m > 0) {
            data3.add(new AdapterDataVolunteer(
                    c0.getString(c0.getColumnIndex("Date")),
                    c0.getString(c0.getColumnIndex("AssignedActivityName")),
                    c0.getString(c0.getColumnIndex("Hours")),
                    c0.getString(c0.getColumnIndex("id")),
                    c0.getString(c0.getColumnIndex("State"))
            ));
            c0.moveToNext();
            m = m - 1;
        }
        mDbHelper.close();
        return data3;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        recViewLeader.setVisibility(View.VISIBLE);
        line.setVisibility(View.VISIBLE);
        detailsVol.setVisibility(View.GONE);
        recViewVolUniv.setVisibility(View.GONE);
        recViewVolArea.setVisibility(View.GONE);
        recViewVolClg.setVisibility(View.GONE);
    }
}