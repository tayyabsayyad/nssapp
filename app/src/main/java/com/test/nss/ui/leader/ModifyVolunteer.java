package com.test.nss.ui.leader;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.test.nss.DatabaseAdapter;
import com.test.nss.R;
import com.test.nss.SwipeHelperRight;
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
    Button univ, area, clg;
    FloatingActionButton back;

    LinearLayout detailsVol;
    CardView cardModify, leader;

    Context context;
    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_modify_details, container, false);
        recViewLeader = requireActivity().findViewById(R.id.vecLeaderList);

        dataVolListUniv = ListUtils.union(addVolActData("First Year University"), addVolActData("Second Year University"));
        dataVolListArea = ListUtils.union(ListUtils.union(addVolActData("First Year Area Based One"), addVolActData("First Year Area Based Two")),
                          ListUtils.union(addVolActData("Second Year Area Based One"), addVolActData("Second Year Area Based Two")));
        dataVolListClg = ListUtils.union(addVolActData("First Year College"), addVolActData("Second Year College"));
        context = requireContext();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        back = root.findViewById(R.id.back);
        detailsVol = root.findViewById(R.id.detailsVol);
        cardModify = root.findViewById(R.id.cardModify);
        leader = requireActivity().findViewById(R.id.volAct);

        univ = root.findViewById(R.id.univ);
        area = root.findViewById(R.id.area);
        clg = root.findViewById(R.id.clg);

        recViewVolUniv = root.findViewById(R.id.recVecDetailUniv);
        MyListAdapterVolunteer adapterVolUniv = new MyListAdapterVolunteer(dataVolListUniv, context);

        recViewVolArea = root.findViewById(R.id.recVecDetailArea);
        MyListAdapterVolunteer adapterVolArea = new MyListAdapterVolunteer(dataVolListArea, context);

        recViewVolClg = root.findViewById(R.id.recVecDetailClg);
        MyListAdapterVolunteer adapterVolClg = new MyListAdapterVolunteer(dataVolListClg, context);
        recViewVolClg.setLayoutManager(new LinearLayoutManager(context));
        recViewVolClg.setAdapter(adapterVolClg);

        revealFab();
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
            hideFab();
            onDetach();
            new Handler().postDelayed(() -> {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(Objects.requireNonNull(fragmentManager.findFragmentByTag("ModifyVolunteer")));
                cardModify.setVisibility(View.GONE);
            }, 250);
        });

        adapterVolUniv.notifyDataSetChanged();
        adapterVolArea.notifyDataSetChanged();
        adapterVolClg.notifyDataSetChanged();

        // UNIV
        SwipeHelperRight swipeHelperRightHoursUniv = new SwipeHelperRight(context, recViewVolUniv) {

            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                DatabaseAdapter mdbF = new DatabaseAdapter(context);
                mdbF.createDatabase();
                mdbF.open();
                    Cursor cF = mdbF.getVolState(Integer.parseInt(adapterVolUniv.list.get(viewHolder.getAdapterPosition()).getId()));
                cF.moveToFirst();
                adapterVolUniv.notifyDataSetChanged();
                if (cF.getString(cF.getColumnIndex("State")).equals("Submitted") || cF.getString(cF.getColumnIndex("State")).equals("Modified")) {
                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                                    context,
                                    "",
                                    R.drawable.ic_disapprove_24,
                                    transparent,
                                    pos -> {
                                        Log.e("Clicked", "instantiateUnderlayButton: ");
                                        int p;
                                        if (viewHolder.getAdapterPosition() == -1)
                                            p = viewHolder.getAdapterPosition() + 1;

                                        else if (viewHolder.getAdapterPosition() == dataVolListUniv.size())
                                            p = viewHolder.getAdapterPosition() - 1;

                                        else
                                            p = viewHolder.getAdapterPosition();

                                        AlertDialog.Builder builder3 = new AlertDialog.Builder(context, R.style.delDialog);

                                        builder3.setTitle("Do you want to delete?");
                                        builder3.setCancelable(false);

                                        builder3.setMessage(dataVolListUniv.get(p).getAct());
                                        builder3.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                adapterVolUniv.notifyItemChanged(p);
                                            }
                                        });

                                        builder3.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //adapterVolUniv.notifyDataSetChanged();
                                                String thisVec = getArguments().getString("thisVec");

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

                                                DatabaseAdapter mdb = new DatabaseAdapter(context);
                                                mdb.createDatabase();
                                                mdb.open();
                                                mdb.setStateVolAct("LeaderDelete", Integer.parseInt(adapterVolUniv.list.get(p).getId()));
                                                Cursor c = mdb.getActAssigActNameAdmin(adapterVolUniv.list.get(p).getAct());
                                                c.moveToFirst();
                                                Call<ResponseBody> putDetailsLeader = RetrofitClient.getInstance().getApi().putDetailsLeader(
                                                        "Token " + ediary.AUTH_TOKEN,
                                                        Integer.parseInt(adapterVolUniv.list.get(p).getHours()),
                                                        thisVec,
                                                        c.getInt(c.getColumnIndex("activityType")),
                                                        c.getInt(c.getColumnIndex("id")),
                                                        5,
                                                        Integer.parseInt(adapterVolUniv.list.get(p).getId())
                                                );
                                                mdb.close();
                                                putDetailsLeader.enqueue(new Callback<ResponseBody>() {
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
                                        });
                                        builder3.show();
                                        //adapterVolUniv.notifyDataSetChanged();
                                    }
                            )
                    );

                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                                    context,
                                "",
                                    R.drawable.ic_approve_24,
                                    transparent,
                                    pos -> {
                                        Log.e("Clicked", "instantiateUnderlayButton: ");

                                        int p;
                                        if (viewHolder.getAdapterPosition() == -1)
                                            p = viewHolder.getAdapterPosition() + 1;

                                        else if (viewHolder.getAdapterPosition() == dataVolListUniv.size())
                                            p = viewHolder.getAdapterPosition() - 1;

                                        else
                                            p = viewHolder.getAdapterPosition();

                                        AlertDialog.Builder builder3 = new AlertDialog.Builder(context, R.style.delDialog);

                                        builder3.setTitle("Do you want to approve?");
                                        builder3.setCancelable(false);

                                        builder3.setMessage(dataVolListUniv.get(p).getAct());
                                        builder3.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                adapterVolUniv.notifyItemChanged(p);
                                            }
                                        });

                                        builder3.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //adapterVolUniv.notifyDataSetChanged();
                                                dialog.dismiss();
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

                                                DatabaseAdapter mdb = new DatabaseAdapter(context);
                                                mdb.createDatabase();
                                                mdb.open();
                                                Cursor c = mdb.getActAssigActNameAdmin(adapterVolUniv.list.get(p).getAct());
                                                mdb.setStateVolAct("Approved", Integer.parseInt(adapterVolUniv.list.get(p).getId()));
                                                c.moveToFirst();
                                                Call<ResponseBody> putDetailsLeader = RetrofitClient.getInstance().getApi().putApprove(
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

                                                putDetailsLeader.enqueue(new Callback<ResponseBody>() {
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
                                        });
                                        builder3.show();
                                        //adapterVolUniv.notifyDataSetChanged();
                                    }
                            )
                    );

                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                            context,
                            "",
                            R.drawable.ic_vol_edit_24,
                            transparent,
                            pos -> {
                                adapterVolUniv.notifyDataSetChanged();
                               // builder3.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                 //   @Override
                                   // public void onClick(DialogInterface dialog3, int which3) {
                                     //   dialog3.dismiss();
                                        int p;
                                        if (viewHolder.getAdapterPosition() == -1)
                                            p = viewHolder.getAdapterPosition() + 1;

                                        else if (viewHolder.getAdapterPosition() == dataVolListUniv.size())
                                            p = viewHolder.getAdapterPosition() - 1;

                                        else
                                            p = viewHolder.getAdapterPosition();

                                        Log.e("This", "instantiateUnderlayButton: " + p);
                                        View viewInflated = LayoutInflater.from(context).inflate(R.layout.hours_input_layout, (ViewGroup) view, false);

                                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.inputDialog);
                                        builder.setCancelable(false);

                                        EditText input = viewInflated.findViewById(R.id.input);
                                        builder.setView(viewInflated);

                                        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                                            dialog.cancel();
                                            adapterVolUniv.notifyItemChanged(p);
                                        });
                                        String thisVec = getArguments().getString("thisVec");
                                        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                            //adapterVolUniv.notifyDataSetChanged();
                                                    dialog.dismiss();
                                                    if (!input.getText().toString().trim().equals("")) {
                                                        int newHours = Integer.parseInt(input.getText().toString());

                                                        //if (!dataVolListUniv.isEmpty())
                                                        //  dataVolListUniv.clear();

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

                                                            DatabaseAdapter mdb = new DatabaseAdapter(context);
                                                            mdb.createDatabase();
                                                            mdb.open();
                                                            Cursor c = mdb.getActAssigActNameAdmin(adapterVolUniv.list.get(p).getAct());
                                                            mdb.setStateVolAct("LeaderModified", Integer.parseInt(adapterVolUniv.list.get(p).getId()));
                                                            c.moveToFirst();
                                                            mdb.setVolActHours(
                                                                    Integer.parseInt(adapterVolUniv.list.get(p).getHours()),
                                                                    c.getInt(c.getColumnIndex("id"))

                                                            );
                                                            Call<ResponseBody> putDetailsLeader = RetrofitClient.getInstance().getApi().putDetailsLeader(
                                                                    "Token " + ediary.AUTH_TOKEN,
                                                                    newHours,
                                                                    thisVec,
                                                                    c.getInt(c.getColumnIndex("activityType")),
                                                                    c.getInt(c.getColumnIndex("id")),
                                                                    6,
                                                                    Integer.parseInt(adapterVolUniv.list.get(p).getId())
                                                            );
                                                            adapterVolUniv.notifyDataSetChanged();
                                                            mdb.close();
                                                            putDetailsLeader.enqueue(new Callback<ResponseBody>() {
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
                DatabaseAdapter mdbF = new DatabaseAdapter(context);
                mdbF.createDatabase();
                mdbF.open();
                Cursor cF = mdbF.getVolState(Integer.parseInt(adapterVolArea.list.get(viewHolder.getAdapterPosition()).getId()));
                cF.moveToFirst();
                adapterVolArea.notifyDataSetChanged();
                if (cF.getString(cF.getColumnIndex("State")).equals("Submitted") || cF.getString(cF.getColumnIndex("State")).equals("Modified")) {
                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                                    context,
                                    "",
                                    R.drawable.ic_disapprove_24,
                                    transparent,
                                    pos -> {

                                        Log.e("Clicked", "instantiateUnderlayButton: ");
                                        int p;
                                        if (viewHolder.getAdapterPosition() == -1)
                                            p = viewHolder.getAdapterPosition() + 1;

                                        else if (viewHolder.getAdapterPosition() == dataVolListArea.size())
                                            p = viewHolder.getAdapterPosition() - 1;

                                        else
                                            p = viewHolder.getAdapterPosition();

                                        AlertDialog.Builder builder3 = new AlertDialog.Builder(context, R.style.delDialog);

                                        builder3.setTitle("Do you want to delete?");
                                        builder3.setCancelable(false);

                                        builder3.setMessage(dataVolListArea.get(p).getAct());
                                        builder3.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                adapterVolArea.notifyItemChanged(p);
                                            }
                                        });

                                        builder3.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //adapterVolArea.notifyDataSetChanged();
                                                String thisVec = getArguments().getString("thisVec");

                                                Snackbar.make(view, "Deleted: " + thisVec, Snackbar.LENGTH_SHORT).show();
                                                adapterVolArea.list.set(p, new AdapterDataVolunteer(
                                                                adapterVolArea.list.get(p).getDate(),
                                                                adapterVolArea.list.get(p).getAct(),
                                                                adapterVolArea.list.get(p).getHours(),
                                                                adapterVolArea.list.get(p).getId(),
                                                                "LeaderDelete"
                                                        )
                                                );
                                                adapterVolArea.notifyDataSetChanged();
                                                adapterVolArea.notifyItemChanged(p);

                                                DatabaseAdapter mdb = new DatabaseAdapter(context);
                                                mdb.createDatabase();
                                                mdb.open();
                                                mdb.setStateVolAct("LeaderDelete", Integer.parseInt(adapterVolArea.list.get(p).getId()));
                                                Cursor c = mdb.getActAssigActNameAdmin(adapterVolArea.list.get(p).getAct());
                                                c.moveToFirst();
                                                Call<ResponseBody> putDetailsLeader = RetrofitClient.getInstance().getApi().putDetailsLeader(
                                                        "Token " + ediary.AUTH_TOKEN,
                                                        Integer.parseInt(adapterVolArea.list.get(p).getHours()),
                                                        thisVec,
                                                        c.getInt(c.getColumnIndex("activityType")),
                                                        c.getInt(c.getColumnIndex("id")),
                                                        5,
                                                        Integer.parseInt(adapterVolArea.list.get(p).getId())
                                                );
                                                mdb.close();
                                                putDetailsLeader.enqueue(new Callback<ResponseBody>() {
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
                                        });
                                        builder3.show();
                                        //adapterVolArea.notifyDataSetChanged();
                                    }
                            )
                    );

                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                                    context,
                                    "",
                                    R.drawable.ic_approve_24,
                                    transparent,
                                    pos -> {
                                        Log.e("Clicked", "instantiateUnderlayButton: ");

                                        int p;
                                        if (viewHolder.getAdapterPosition() == -1)
                                            p = viewHolder.getAdapterPosition() + 1;

                                        else if (viewHolder.getAdapterPosition() == dataVolListArea.size())
                                            p = viewHolder.getAdapterPosition() - 1;

                                        else
                                            p = viewHolder.getAdapterPosition();

                                        AlertDialog.Builder builder3 = new AlertDialog.Builder(context, R.style.delDialog);

                                        builder3.setTitle("Do you want to approve?");
                                        builder3.setCancelable(false);

                                        builder3.setMessage(dataVolListArea.get(p).getAct());
                                        builder3.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                adapterVolArea.notifyItemChanged(p);
                                            }
                                        });

                                        builder3.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //adapterVolArea.notifyDataSetChanged();
                                                dialog.dismiss();
                                                String thisVec = getArguments().getString("thisVec");
                                                Snackbar.make(view, "Approved: " + thisVec, Snackbar.LENGTH_SHORT).show();
                                                adapterVolArea.list.set(p, new AdapterDataVolunteer(
                                                                adapterVolArea.list.get(p).getDate(),
                                                                adapterVolArea.list.get(p).getAct(),
                                                                adapterVolArea.list.get(p).getHours(),
                                                                adapterVolArea.list.get(p).getId(),
                                                                "Approved"
                                                        )
                                                );
                                                adapterVolArea.notifyDataSetChanged();
                                                adapterVolArea.notifyItemChanged(p);

                                                DatabaseAdapter mdb = new DatabaseAdapter(context);
                                                mdb.createDatabase();
                                                mdb.open();
                                                Cursor c = mdb.getActAssigActNameAdmin(adapterVolArea.list.get(p).getAct());
                                                mdb.setStateVolAct("Approved", Integer.parseInt(adapterVolArea.list.get(p).getId()));
                                                c.moveToFirst();
                                                Call<ResponseBody> putDetailsLeader = RetrofitClient.getInstance().getApi().putApprove(
                                                        "Token " + ediary.AUTH_TOKEN,
                                                        Integer.parseInt(adapterVolArea.list.get(p).getHours()),
                                                        thisVec,
                                                        c.getInt(c.getColumnIndex("activityType")),
                                                        c.getInt(c.getColumnIndex("id")),
                                                        2,
                                                        leaderId,
                                                        Integer.parseInt(adapterVolArea.list.get(p).getId())
                                                );
                                                mdb.close();

                                                putDetailsLeader.enqueue(new Callback<ResponseBody>() {
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
                                        });
                                        builder3.show();
                                        //adapterVolArea.notifyDataSetChanged();
                                    }
                            )
                    );

                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                            context,
                            "",
                            R.drawable.ic_vol_edit_24,
                            transparent,
                            pos -> {
                                adapterVolArea.notifyDataSetChanged();
                                // builder3.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                //   @Override
                                // public void onClick(DialogInterface dialog3, int which3) {
                                //   dialog3.dismiss();
                                int p;
                                if (viewHolder.getAdapterPosition() == -1)
                                    p = viewHolder.getAdapterPosition() + 1;

                                else if (viewHolder.getAdapterPosition() == dataVolListArea.size())
                                    p = viewHolder.getAdapterPosition() - 1;

                                else
                                    p = viewHolder.getAdapterPosition();

                                Log.e("This", "instantiateUnderlayButton: " + p);
                                View viewInflated = LayoutInflater.from(context).inflate(R.layout.hours_input_layout, (ViewGroup) view, false);

                                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.inputDialog);
                                builder.setCancelable(false);

                                EditText input = viewInflated.findViewById(R.id.input);
                                builder.setView(viewInflated);

                                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                                    dialog.cancel();
                                    adapterVolArea.notifyItemChanged(p);
                                });
                                String thisVec = getArguments().getString("thisVec");
                                builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                    //adapterVolArea.notifyDataSetChanged();
                                            dialog.dismiss();
                                            if (!input.getText().toString().trim().equals("")) {
                                                int newHours = Integer.parseInt(input.getText().toString());

                                                //if (!dataVolListArea.isEmpty())
                                                //  dataVolListArea.clear();

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
                                                    adapterVolArea.notifyItemInserted(p);

                                                    DatabaseAdapter mdb = new DatabaseAdapter(context);
                                                    mdb.createDatabase();
                                                    mdb.open();
                                                    Cursor c = mdb.getActAssigActNameAdmin(adapterVolArea.list.get(p).getAct());
                                                    mdb.setStateVolAct("LeaderModified", Integer.parseInt(adapterVolArea.list.get(p).getId()));
                                                    c.moveToFirst();
                                                    mdb.setVolActHours(
                                                            Integer.parseInt(adapterVolArea.list.get(p).getHours()),
                                                            c.getInt(c.getColumnIndex("id"))

                                                    );
                                                    Call<ResponseBody> putDetailsLeader = RetrofitClient.getInstance().getApi().putDetailsLeader(
                                                            "Token " + ediary.AUTH_TOKEN,
                                                            newHours,
                                                            thisVec,
                                                            c.getInt(c.getColumnIndex("activityType")),
                                                            c.getInt(c.getColumnIndex("id")),
                                                            6,
                                                            Integer.parseInt(adapterVolArea.list.get(p).getId())
                                                    );
                                                    adapterVolArea.notifyDataSetChanged();
                                                    mdb.close();
                                                    putDetailsLeader.enqueue(new Callback<ResponseBody>() {
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
        recViewVolArea.setLayoutManager(new LinearLayoutManager(context));
        recViewVolArea.setAdapter(adapterVolArea);

        // COLLEGE
        SwipeHelperRight swipeHelperRightHoursClg = new SwipeHelperRight(context, recViewVolClg) {

            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                DatabaseAdapter mdbF = new DatabaseAdapter(context);
                mdbF.createDatabase();
                mdbF.open();
                Cursor cF = mdbF.getVolState(Integer.parseInt(adapterVolClg.list.get(viewHolder.getAdapterPosition()).getId()));
                cF.moveToFirst();
                adapterVolClg.notifyDataSetChanged();
                if (cF.getString(cF.getColumnIndex("State")).equals("Submitted") || cF.getString(cF.getColumnIndex("State")).equals("Modified")) {
                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                                    context,
                                    "",
                                    R.drawable.ic_disapprove_24,
                                    transparent,
                                    pos -> {

                                        Log.e("Clicked", "instantiateUnderlayButton: ");
                                        int p;
                                        if (viewHolder.getAdapterPosition() == -1)
                                            p = viewHolder.getAdapterPosition() + 1;

                                        else if (viewHolder.getAdapterPosition() == dataVolListClg.size())
                                            p = viewHolder.getAdapterPosition() - 1;

                                        else
                                            p = viewHolder.getAdapterPosition();

                                        AlertDialog.Builder builder3 = new AlertDialog.Builder(context, R.style.delDialog);

                                        builder3.setTitle("Do you want to delete?");
                                        builder3.setCancelable(false);

                                        builder3.setMessage(dataVolListClg.get(p).getAct());
                                        builder3.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                adapterVolClg.notifyItemChanged(p);
                                            }
                                        });

                                        builder3.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //adapterVolClg.notifyDataSetChanged();
                                                String thisVec = getArguments().getString("thisVec");

                                                Snackbar.make(view, "Deleted: " + thisVec, Snackbar.LENGTH_SHORT).show();
                                                adapterVolClg.list.set(p, new AdapterDataVolunteer(
                                                                adapterVolClg.list.get(p).getDate(),
                                                                adapterVolClg.list.get(p).getAct(),
                                                                adapterVolClg.list.get(p).getHours(),
                                                                adapterVolClg.list.get(p).getId(),
                                                                "LeaderDelete"
                                                        )
                                                );
                                                adapterVolClg.notifyDataSetChanged();
                                                adapterVolClg.notifyItemChanged(p);

                                                DatabaseAdapter mdb = new DatabaseAdapter(context);
                                                mdb.createDatabase();
                                                mdb.open();
                                                mdb.setStateVolAct("LeaderDelete", Integer.parseInt(adapterVolClg.list.get(p).getId()));
                                                Cursor c = mdb.getActAssigActNameAdmin(adapterVolClg.list.get(p).getAct());
                                                c.moveToFirst();
                                                Call<ResponseBody> putDetailsLeader = RetrofitClient.getInstance().getApi().putDetailsLeader(
                                                        "Token " + ediary.AUTH_TOKEN,
                                                        Integer.parseInt(adapterVolClg.list.get(p).getHours()),
                                                        thisVec,
                                                        c.getInt(c.getColumnIndex("activityType")),
                                                        c.getInt(c.getColumnIndex("id")),
                                                        5,
                                                        Integer.parseInt(adapterVolClg.list.get(p).getId())
                                                );
                                                mdb.close();
                                                putDetailsLeader.enqueue(new Callback<ResponseBody>() {
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
                                        });
                                        builder3.show();
                                        //adapterVolClg.notifyDataSetChanged();
                                    }
                            )
                    );

                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                                    context,
                                    "",
                                    R.drawable.ic_approve_24,
                                    transparent,
                                    pos -> {
                                        Log.e("Clicked", "instantiateUnderlayButton: ");

                                        int p;
                                        if (viewHolder.getAdapterPosition() == -1)
                                            p = viewHolder.getAdapterPosition() + 1;

                                        else if (viewHolder.getAdapterPosition() == dataVolListClg.size())
                                            p = viewHolder.getAdapterPosition() - 1;

                                        else
                                            p = viewHolder.getAdapterPosition();

                                        AlertDialog.Builder builder3 = new AlertDialog.Builder(context, R.style.delDialog);

                                        builder3.setTitle("Do you want to approve?");
                                        builder3.setCancelable(false);

                                        builder3.setMessage(dataVolListClg.get(p).getAct());
                                        builder3.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                adapterVolClg.notifyItemChanged(p);
                                            }
                                        });

                                        builder3.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //adapterVolClg.notifyDataSetChanged();
                                                dialog.dismiss();
                                                String thisVec = getArguments().getString("thisVec");
                                                Snackbar.make(view, "Approved: " + thisVec, Snackbar.LENGTH_SHORT).show();
                                                adapterVolClg.list.set(p, new AdapterDataVolunteer(
                                                                adapterVolClg.list.get(p).getDate(),
                                                                adapterVolClg.list.get(p).getAct(),
                                                                adapterVolClg.list.get(p).getHours(),
                                                                adapterVolClg.list.get(p).getId(),
                                                                "Approved"
                                                        )
                                                );
                                                adapterVolClg.notifyDataSetChanged();
                                                adapterVolClg.notifyItemChanged(p);

                                                DatabaseAdapter mdb = new DatabaseAdapter(context);
                                                mdb.createDatabase();
                                                mdb.open();
                                                Cursor c = mdb.getActAssigActNameAdmin(adapterVolClg.list.get(p).getAct());
                                                mdb.setStateVolAct("Approved", Integer.parseInt(adapterVolClg.list.get(p).getId()));
                                                c.moveToFirst();
                                                Call<ResponseBody> putDetailsLeader = RetrofitClient.getInstance().getApi().putApprove(
                                                        "Token " + ediary.AUTH_TOKEN,
                                                        Integer.parseInt(adapterVolClg.list.get(p).getHours()),
                                                        thisVec,
                                                        c.getInt(c.getColumnIndex("activityType")),
                                                        c.getInt(c.getColumnIndex("id")),
                                                        2,
                                                        leaderId,
                                                        Integer.parseInt(adapterVolClg.list.get(p).getId())
                                                );
                                                mdb.close();

                                                putDetailsLeader.enqueue(new Callback<ResponseBody>() {
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
                                        });
                                        builder3.show();
                                        //adapterVolClg.notifyDataSetChanged();
                                    }
                            )
                    );

                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                            context,
                            "",
                            R.drawable.ic_vol_edit_24,
                            transparent,
                            pos -> {
                                adapterVolClg.notifyDataSetChanged();
                                // builder3.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                //   @Override
                                // public void onClick(DialogInterface dialog3, int which3) {
                                //   dialog3.dismiss();
                                int p;
                                if (viewHolder.getAdapterPosition() == -1)
                                    p = viewHolder.getAdapterPosition() + 1;

                                else if (viewHolder.getAdapterPosition() == dataVolListClg.size())
                                    p = viewHolder.getAdapterPosition() - 1;

                                else
                                    p = viewHolder.getAdapterPosition();

                                Log.e("This", "instantiateUnderlayButton: " + p);
                                View viewInflated = LayoutInflater.from(context).inflate(R.layout.hours_input_layout, (ViewGroup) view, false);

                                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.inputDialog);
                                builder.setCancelable(false);

                                EditText input = viewInflated.findViewById(R.id.input);
                                builder.setView(viewInflated);

                                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                                    dialog.cancel();
                                    adapterVolClg.notifyItemChanged(p);
                                });
                                String thisVec = getArguments().getString("thisVec");
                                builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                    //adapterVolClg.notifyDataSetChanged();
                                            dialog.dismiss();
                                            if (!input.getText().toString().trim().equals("")) {
                                                int newHours = Integer.parseInt(input.getText().toString());

                                                //if (!dataVolListClg.isEmpty())
                                                //  dataVolListClg.clear();

                                                if (newHours >= 1 && newHours <= 10) {
                                                    adapterVolClg.list.add(p + 1, new AdapterDataVolunteer(
                                                            adapterVolClg.list.get(p).getDate(),
                                                            adapterVolClg.list.get(p).getAct(),
                                                            String.valueOf(newHours),
                                                            adapterVolClg.list.get(p).getId(),
                                                            "LeaderModified"
                                                    ));

                                                    adapterVolClg.notifyDataSetChanged();
                                                    dataVolListClg.remove(p);
                                                    adapterVolClg.notifyItemInserted(p);

                                                    DatabaseAdapter mdb = new DatabaseAdapter(context);
                                                    mdb.createDatabase();
                                                    mdb.open();
                                                    Cursor c = mdb.getActAssigActNameAdmin(adapterVolClg.list.get(p).getAct());
                                                    mdb.setStateVolAct("LeaderModified", Integer.parseInt(adapterVolClg.list.get(p).getId()));
                                                    c.moveToFirst();
                                                    mdb.setVolActHours(
                                                            Integer.parseInt(adapterVolClg.list.get(p).getHours()),
                                                            c.getInt(c.getColumnIndex("id"))

                                                    );
                                                    Call<ResponseBody> putDetailsLeader = RetrofitClient.getInstance().getApi().putDetailsLeader(
                                                            "Token " + ediary.AUTH_TOKEN,
                                                            newHours,
                                                            thisVec,
                                                            c.getInt(c.getColumnIndex("activityType")),
                                                            c.getInt(c.getColumnIndex("id")),
                                                            6,
                                                            Integer.parseInt(adapterVolClg.list.get(p).getId())
                                                    );
                                                    adapterVolClg.notifyDataSetChanged();
                                                    mdb.close();
                                                    putDetailsLeader.enqueue(new Callback<ResponseBody>() {
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

        DatabaseAdapter mDbHelper = new DatabaseAdapter(requireContext());
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

        leader.setVisibility(View.VISIBLE);
        requireActivity().findViewById(R.id.volActAll).setVisibility(View.VISIBLE);
        recViewLeader.setVisibility(View.VISIBLE);
        detailsVol.setVisibility(View.GONE);
        recViewVolUniv.setVisibility(View.GONE);
        recViewVolArea.setVisibility(View.GONE);
        recViewVolClg.setVisibility(View.GONE);
    }

    private void revealFab(){
        View v = root.findViewById(R.id.back);
        int x = v.getWidth()/2;
        int y = v.getHeight()/2;

        float finalRad = (float) Math.hypot(x, y);
        Animator animator = ViewAnimationUtils.createCircularReveal(v, x, y, 0, finalRad);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.setVisibility(View.VISIBLE);
            }
        });
        animator.start();
    }

    private void hideFab(){
        View v = root.findViewById(R.id.back);
        int x = v.getWidth()/2;
        int y = v.getHeight()/2;

        float inRad = (float) Math.hypot(x, y);
        Animator animator = ViewAnimationUtils.createCircularReveal(v, x, y, inRad, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.setVisibility(View.GONE);
            }
        });
        animator.start();
    }
}