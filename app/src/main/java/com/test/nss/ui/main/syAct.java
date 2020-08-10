package com.test.nss.ui.main;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
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
import com.test.nss.DatabaseAdapter;
import com.test.nss.R;
import com.test.nss.SwipeHelperRight;
import com.test.nss.api.RetrofitClient;
import com.test.nss.ediary;

import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static com.test.nss.ediary.isLeader;
import static com.test.nss.ediary.primaryColDark;
import static com.test.nss.ediary.transparent;

public class syAct extends Fragment {

    private static final String TAG = "syAct";
    View root;
    Button univ;
    Button area;
    Button clg;

    //LinearLayout mainSy;
    Context mContext;

    RecyclerView univRecSy;
    RecyclerView areaRecSy;
    RecyclerView clgRecSy;

    CardView cardViewMain;

    List<AdapterDataMain> univListDataSy;
    List<AdapterDataMain> areaDataMainSy;
    List<AdapterDataMain> clgListDataSy;

    Button add;
    int whichAct;
    int act;
    int newHours = 0;

    LinearLayout actHeaderInput;

    LinearLayout fragSy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_sy_act, container, false);
        mContext = requireContext();

        cardViewMain = root.findViewById(R.id.details_main_card2);
        actHeaderInput = requireActivity().findViewById(R.id.actHeaderInput);
        actHeaderInput.setVisibility(View.GONE);

        univ = root.findViewById(R.id.main_univ_sy);
        area = root.findViewById(R.id.main_area_sy);
        clg = root.findViewById(R.id.main_clg_sy);

        //mainSy = root.findViewById(R.id.main_sy);
        add = root.findViewById(R.id.add2);

        univRecSy = root.findViewById(R.id.univRecSy);
        areaRecSy = root.findViewById(R.id.areaRecSy);
        clgRecSy = root.findViewById(R.id.hoursRecSy);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragSy = root.findViewById(R.id.frag_sy);

        univ.setOnClickListener(v -> {
            Animation animation = new TranslateAnimation(0, 0, -100, 0);

            animation.setDuration(4000);
            add.startAnimation(animation);
            Snackbar.make(view, "Swipe left on activity to modify", Snackbar.LENGTH_SHORT).show();

            whichAct = 23;
            //mainSy.setVisibility(View.VISIBLE);
            univRecSy.setVisibility(View.VISIBLE);
            areaRecSy.setVisibility(View.GONE);
            clgRecSy.setVisibility(View.GONE);
            add.setVisibility(View.VISIBLE);

            univ.setTextColor(primaryColDark);
            area.setTextColor(Color.BLACK);
            clg.setTextColor(Color.BLACK);
        });

        area.setOnClickListener(v -> {
            whichAct = 22;
            //mainSy.setVisibility(View.VISIBLE);
            add.setVisibility(View.VISIBLE);

            univRecSy.setVisibility(View.GONE);
            areaRecSy.setVisibility(View.VISIBLE);
            clgRecSy.setVisibility(View.GONE);

            area.setTextColor(primaryColDark);
            univ.setTextColor(Color.BLACK);
            clg.setTextColor(Color.BLACK);
        });

        clg.setOnClickListener(v -> {
//            mainSy.setVisibility(View.VISIBLE);

            whichAct = 21;
            add.setVisibility(View.VISIBLE);
            univRecSy.setVisibility(View.GONE);
            areaRecSy.setVisibility(View.GONE);
            clgRecSy.setVisibility(View.VISIBLE);

            clg.setTextColor(primaryColDark);
            univ.setTextColor(Color.BLACK);
            area.setTextColor(Color.BLACK);
        });

        clgListDataSy = addAct("Second Year College");
        areaDataMainSy = ListUtils.union(addAct("Second Year Area Based One"), addAct("Second Year Area Based Two"));

        univListDataSy = addAct("Second Year University");

        // UNIV
        RecyclerView recyclerViewUniv = root.findViewById(R.id.univRecSy);
        MyListAdapter adapterUniv = new MyListAdapter(univListDataSy, mContext);

        SwipeHelperRight swipeHelperRightUniv = new SwipeHelperRight(mContext, recyclerViewUniv) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                if (adapterUniv.list.get(viewHolder.getAdapterPosition()).getState().equals("Submitted")||
                        adapterUniv.list.get(viewHolder.getAdapterPosition()).getState().equals("Modified")){
                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                            mContext,
                            "Edit",
                            R.drawable.ic_edit_24,
                            transparent,
                            pos -> {

                                int p;
                                if (viewHolder.getAdapterPosition() == -1)
                                    p = viewHolder.getAdapterPosition() + 1;

                                else if (viewHolder.getAdapterPosition() == univListDataSy.size())
                                    p = viewHolder.getAdapterPosition() - 1;
                                else
                                    p = viewHolder.getAdapterPosition();

                                int actID = Integer.parseInt(univListDataSy.get(p).getId());
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.inputDialog);
                                View viewInflated = LayoutInflater.from(mContext).inflate(R.layout.hours_input_layout, (ViewGroup) view, false);

                                EditText input = viewInflated.findViewById(R.id.input);
                                builder.setView(viewInflated);

                                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                                    dialog.cancel();
                                    adapterUniv.notifyItemChanged(p);
                                });

                                builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                    dialog.dismiss();

                                    if (!input.getText().toString().trim().equals("")) {
                                        newHours = Integer.parseInt(input.getText().toString());

                                        if (newHours >= 1 && newHours <= 10) {
                                            String actName = adapterUniv.list.get(p).getAct();

                                            DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                                            mdb.createDatabase();
                                            mdb.open();
                                            mdb.setDetails(newHours, "Modified", actID);

                                            Cursor c = mdb.getActAssigActNameAdmin(actName);
                                            c.moveToFirst();

                                            univListDataSy.add(p, new AdapterDataMain(
                                                            adapterUniv.list.get(p).getDate(),
                                                            adapterUniv.list.get(p).getAct(),
                                                            String.valueOf(newHours),
                                                            adapterUniv.list.get(p).getId(),
                                                            adapterUniv.list.get(p).isApproved(),
                                                            "Modified"
                                                    )
                                            );

                                            adapterUniv.notifyDataSetChanged();
                                            univListDataSy.remove(p + 1);
                                            adapterUniv.notifyItemInserted(p);

                                            //TODO: Offline mode needs to be checked
                                            //mdb.setSyncActDetails(0, actID);

                                            Call<ResponseBody> putHours = RetrofitClient.getInstance().getApi().putHour(
                                                    "Token " + ediary.AUTH_TOKEN,
                                                    newHours,
                                                    ediary.VEC,
                                                    Integer.parseInt(c.getString(c.getColumnIndex("activityType"))),
                                                    Integer.parseInt(c.getString(c.getColumnIndex("id"))),
                                                    3,

                                                    actID
                                            );
                                            c.close();
                                            mdb.close();
                                            putHours.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                @EverythingIsNonNull
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if (response.isSuccessful())
                                                        Snackbar.make(view, "Edited to "+newHours+"hours", Snackbar.LENGTH_SHORT);
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
                                });

                                if (viewInflated.getParent() != null)
                                    ((ViewGroup) viewInflated.getParent()).removeView(viewInflated);
                                builder.show();
                            }
                    ));
                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                            mContext,
                            "",
                            R.drawable.ic_del_24,
                            transparent,
                            pos -> {
                                int p;
                                if (viewHolder.getAdapterPosition() == -1)
                                    p = viewHolder.getAdapterPosition() + 1;

                                else if (viewHolder.getAdapterPosition() == univListDataSy.size())
                                    p = viewHolder.getAdapterPosition() - 1;
                                else
                                    p = viewHolder.getAdapterPosition();

                                int actID = Integer.parseInt(univListDataSy.get(p).getId());
                                String actName = adapterUniv.list.get(p).getAct();

                                DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                                mdb.createDatabase();
                                mdb.open();

                                Cursor c2 = mdb.getActAssigActNameAdmin(actName);
                                c2.moveToFirst();

                                Call<ResponseBody> putHours = RetrofitClient.getInstance().getApi().putHour(
                                        "Token " + ediary.AUTH_TOKEN,
                                        Integer.parseInt(adapterUniv.list.get(p).getHours()),
                                        ediary.VEC,
                                        Integer.parseInt(c2.getString(c2.getColumnIndex("activityType"))),
                                        Integer.parseInt(c2.getString(c2.getColumnIndex("id"))),
                                        4,
                                        actID
                                );

                                mdb.dropDetails(actID);
                                univListDataSy.remove(p);
                                adapterUniv.notifyItemRemoved(p);

                                putHours.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    @EverythingIsNonNull
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    }

                                    @Override
                                    @EverythingIsNonNull
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                                mdb.close();
                            }
                    ));
                } else if (univListDataSy.get(viewHolder.getAdapterPosition()).getState().equals("Approved")) {
                underlayButtons.add(new UnderlayButton(
                        mContext,
                        "",
                        R.drawable.ic_eye_24,
                        transparent,
                        new UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                int p;
                                if (viewHolder.getAdapterPosition() == -1)
                                    p = viewHolder.getAdapterPosition() + 1;

                                else if (viewHolder.getAdapterPosition() == univListDataSy.size())
                                    p = viewHolder.getAdapterPosition() - 1;
                                else
                                    p = viewHolder.getAdapterPosition();

                                if (isLeader != 1) {
                                    DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                                    mdb.createDatabase();
                                    mdb.open();
                                    Cursor c = mdb.getActLeaderId(Integer.parseInt(univListDataSy.get(p).getId()));
                                    c.moveToFirst();
                                    int leadId = c.getInt(c.getColumnIndex("Approved_by"));

                                    Snackbar sb = Snackbar.make(view, "Approved By: " + mdb.getLeaderName(leadId), Snackbar.LENGTH_LONG)
                                            .setTextColor(mContext.getColor(R.color.colorPrimaryDark));
                                    sb.getView().setBackgroundColor(mContext.getColor(R.color.colorPrimaryLight));
                                    mdb.close();
                                    sb.show();
                                } else {
                                    DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                                    mdb.createDatabase();
                                    mdb.open();
                                    Cursor c = mdb.getActLeaderId(Integer.parseInt(univListDataSy.get(p).getId()));
                                    c.moveToFirst();

                                    if (c.getString(c.getColumnIndex("Approved_by")).equals("null")) {
                                        Snackbar sb = Snackbar.make(view, "Approved By: PO", Snackbar.LENGTH_LONG)
                                                .setTextColor(mContext.getColor(R.color.colorPrimaryDark));
                                        sb.getView().setBackgroundColor(mContext.getColor(R.color.colorPrimaryLight));
                                        sb.show();
                                    } else {
                                        int leadId = c.getInt(c.getColumnIndex("Approved_by"));
                                        Snackbar sb = Snackbar.make(view, "Approved By: " + mdb.getLeaderName(leadId), Snackbar.LENGTH_LONG)
                                                .setTextColor(mContext.getColor(R.color.colorPrimaryDark));
                                        sb.getView().setBackgroundColor(mContext.getColor(R.color.colorPrimaryLight));
                                        sb.show();
                                    }
                                    mdb.close();
                                }
                                adapterUniv.notifyItemChanged(p);
                            }
                        }
                    ));
                }
            }
        };

        recyclerViewUniv.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewUniv.setAdapter(adapterUniv);

        // AREA
        RecyclerView recyclerViewArea = root.findViewById(R.id.areaRecSy);
        MyListAdapter adapterArea = new MyListAdapter(areaDataMainSy, mContext);

        SwipeHelperRight swipeHelperRightArea = new SwipeHelperRight(mContext, recyclerViewArea) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                if (adapterArea.list.get(viewHolder.getAdapterPosition()).getState().equals("Submitted")||
                        adapterArea.list.get(viewHolder.getAdapterPosition()).getState().equals("Modified")){
                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                            mContext,
                            "Edit",
                            R.drawable.ic_edit_24,
                            transparent,
                            pos -> {

                                int p;
                                if (viewHolder.getAdapterPosition() == -1)
                                    p = viewHolder.getAdapterPosition() + 1;

                                else if (viewHolder.getAdapterPosition() == areaDataMainSy.size())
                                    p = viewHolder.getAdapterPosition() - 1;
                                else
                                    p = viewHolder.getAdapterPosition();

                                int actID = Integer.parseInt(areaDataMainSy.get(p).getId());
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.inputDialog);
                                View viewInflated = LayoutInflater.from(mContext).inflate(R.layout.hours_input_layout, (ViewGroup) view, false);

                                EditText input = viewInflated.findViewById(R.id.input);
                                builder.setView(viewInflated);

                                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

                                builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                    dialog.dismiss();

                                    //Log.e("Yes this", adapterArea.list.get(p).getAct());
                                    if (!input.getText().toString().trim().equals("")) {
                                        newHours = Integer.parseInt(input.getText().toString());

                                        if (newHours >= 1 && newHours <= 10) {
                                            String actName = adapterArea.list.get(p).getAct();

                                            DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                                            mdb.createDatabase();
                                            mdb.open();
                                            mdb.setDetails(newHours, "Modified", actID);

                                            Cursor c = mdb.getActAssigActNameAdmin(actName);
                                            c.moveToFirst();

                                            areaDataMainSy.add(p, new AdapterDataMain(
                                                            adapterArea.list.get(p).getDate(),
                                                            adapterArea.list.get(p).getAct(),
                                                            String.valueOf(newHours),
                                                            adapterArea.list.get(p).getId(),
                                                            adapterArea.list.get(p).isApproved(),
                                                            "Modified"
                                                    )
                                            );

                                            adapterArea.notifyDataSetChanged();
                                            areaDataMainSy.remove(p + 1);
                                            adapterArea.notifyItemInserted(p);

                                            //TODO: Offline mode needs to be checked
                                            //mdb.setSyncActDetails(0, actID);

                                            Call<ResponseBody> putHours = RetrofitClient.getInstance().getApi().putHour(
                                                    "Token " + ediary.AUTH_TOKEN,
                                                    newHours,
                                                    ediary.VEC,
                                                    Integer.parseInt(c.getString(c.getColumnIndex("activityType"))),
                                                    Integer.parseInt(c.getString(c.getColumnIndex("id"))),
                                                    3,

                                                    actID
                                            );
                                            c.close();
                                            mdb.close();
                                            putHours.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                @EverythingIsNonNull
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if (response.isSuccessful())
                                                        Snackbar.make(view, "Edited to "+newHours+"hours", Snackbar.LENGTH_SHORT);
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
                                });

                                if (viewInflated.getParent() != null)
                                    ((ViewGroup) viewInflated.getParent()).removeView(viewInflated);
                                builder.show();
                            }
                    ));

                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                            mContext,
                            "",
                            R.drawable.ic_del_24,
                            transparent,
                            pos -> {
                                int p;
                                if (viewHolder.getAdapterPosition() == -1)
                                    p = viewHolder.getAdapterPosition() + 1;

                                else if (viewHolder.getAdapterPosition() == areaDataMainSy.size())
                                    p = viewHolder.getAdapterPosition() - 1;
                                else
                                    p = viewHolder.getAdapterPosition();

                                //Log.e("Damn here it is:", "onClick: " + p + viewHolder.getAdapterPosition());

                                int actID = Integer.parseInt(areaDataMainSy.get(p).getId());
                                String actName = adapterArea.list.get(p).getAct();

                                DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                                mdb.createDatabase();

                                mdb.open();

                                Cursor c2 = mdb.getActAssigActNameAdmin(actName);
                                c2.moveToFirst();

                                Call<ResponseBody> putHours = RetrofitClient.getInstance().getApi().putHour(
                                        "Token " + ediary.AUTH_TOKEN,
                                        Integer.parseInt(adapterArea.list.get(p).getHours()),
                                        ediary.VEC,
                                        Integer.parseInt(c2.getString(c2.getColumnIndex("activityType"))),
                                        Integer.parseInt(c2.getString(c2.getColumnIndex("id"))),
                                        4,
                                        actID
                                );

                                mdb.dropDetails(actID);
                                areaDataMainSy.remove(p);
                                adapterArea.notifyItemRemoved(p);

                                putHours.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    @EverythingIsNonNull
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    }

                                    @Override
                                    @EverythingIsNonNull
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                                mdb.close();
                            }
                    ));
                } else if (areaDataMainSy.get(viewHolder.getAdapterPosition()).getState().equals("Approved")) {
                    underlayButtons.add(new UnderlayButton(
                            mContext,
                            "",
                            R.drawable.ic_eye_24,
                            transparent,
                            new UnderlayButtonClickListener() {
                                @Override
                                public void onClick(int pos) {
                                    int p;
                                    if (viewHolder.getAdapterPosition() == -1)
                                        p = viewHolder.getAdapterPosition() + 1;

                                    else if (viewHolder.getAdapterPosition() == areaDataMainSy.size())
                                        p = viewHolder.getAdapterPosition() - 1;
                                    else
                                        p = viewHolder.getAdapterPosition();

                                    if (isLeader != 1) {
                                        DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                                        mdb.createDatabase();
                                        mdb.open();
                                        Cursor c = mdb.getActLeaderId(Integer.parseInt(areaDataMainSy.get(p).getId()));
                                        c.moveToFirst();
                                        int leadId = c.getInt(c.getColumnIndex("Approved_by"));

                                        Snackbar sb = Snackbar.make(view, "Approved By: " + mdb.getLeaderName(leadId), Snackbar.LENGTH_LONG)
                                                .setTextColor(mContext.getColor(R.color.colorPrimaryDark));
                                        sb.getView().setBackgroundColor(mContext.getColor(R.color.colorPrimaryLight));
                                        mdb.close();
                                        sb.show();
                                    } else {
                                        DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                                        mdb.createDatabase();
                                        mdb.open();
                                        Cursor c = mdb.getActLeaderId(Integer.parseInt(areaDataMainSy.get(p).getId()));
                                        c.moveToFirst();

                                        if (c.getString(c.getColumnIndex("Approved_by")).equals("null")) {
                                            Snackbar sb = Snackbar.make(view, "Approved By: PO", Snackbar.LENGTH_LONG)
                                                    .setTextColor(mContext.getColor(R.color.colorPrimaryDark));
                                            sb.getView().setBackgroundColor(mContext.getColor(R.color.colorPrimaryLight));
                                            sb.show();
                                        } else {
                                            int leadId = c.getInt(c.getColumnIndex("Approved_by"));
                                            Snackbar sb = Snackbar.make(view, "Approved By: " + mdb.getLeaderName(leadId), Snackbar.LENGTH_LONG)
                                                    .setTextColor(mContext.getColor(R.color.colorPrimaryDark));
                                            sb.getView().setBackgroundColor(mContext.getColor(R.color.colorPrimaryLight));
                                            sb.show();
                                        }
                                        mdb.close();
                                    }
                                    adapterArea.notifyItemChanged(p);
                                }
                            }
                    ));
                }
            }
        };


        recyclerViewArea.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewArea.setAdapter(adapterArea);

        // CLG
        RecyclerView recyclerViewHours = root.findViewById(R.id.hoursRecSy);
        MyListAdapter adapterClg = new MyListAdapter(clgListDataSy, mContext);

        SwipeHelperRight swipeHelperRightHours = new SwipeHelperRight(mContext, recyclerViewHours) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                if (adapterClg.list.get(viewHolder.getAdapterPosition()).getState().equals("Submitted")||
                        adapterClg.list.get(viewHolder.getAdapterPosition()).getState().equals("Modified")){
                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                            mContext,
                            "",
                            R.drawable.ic_edit_24,
                            transparent,
                            pos -> {
                                int p;
                                if (viewHolder.getAdapterPosition() == -1)
                                    p = viewHolder.getAdapterPosition() + 1;

                                else if (viewHolder.getAdapterPosition() == univListDataSy.size())
                                    p = viewHolder.getAdapterPosition() - 1;
                                else
                                    p = viewHolder.getAdapterPosition();

                                int actID = Integer.parseInt(clgListDataSy.get(p).getId());
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.inputDialog);
                                View viewInflated = LayoutInflater.from(mContext).inflate(R.layout.hours_input_layout, (ViewGroup) view, false);

                                EditText input = viewInflated.findViewById(R.id.input);
                                builder.setView(viewInflated);

                                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

                                builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                    dialog.dismiss();

                                    if (!input.getText().toString().trim().equals("")) {
                                        newHours = Integer.parseInt(input.getText().toString());

                                        if (newHours >= 1 && newHours <= 10) {
                                            String actName = adapterClg.list.get(p).getAct();

                                            DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                                            mdb.createDatabase();
                                            mdb.open();
                                            mdb.setDetails(newHours, "Modified", actID);

                                            Cursor c = mdb.getActAssigActNameAdmin(actName);
                                            c.moveToFirst();

                                            clgListDataSy.add(p, new AdapterDataMain(
                                                    adapterClg.list.get(p).getDate(),
                                                    adapterClg.list.get(p).getAct(),
                                                    String.valueOf(newHours),
                                                    adapterClg.list.get(p).getId(),
                                                    adapterClg.list.get(p).isApproved(),
                                                    "Modified"
                                            ));

                                            adapterClg.notifyDataSetChanged();
                                            clgListDataSy.remove(p + 1);
                                            adapterClg.notifyItemInserted(p);

                                            //TODO: Offline mode needs to be checked
                                            //mdb.setSyncActDetails(0, actID);

                                            Call<ResponseBody> putHours = RetrofitClient.getInstance().getApi().putHour(
                                                    "Token " + ediary.AUTH_TOKEN,
                                                    newHours,
                                                    ediary.VEC,
                                                    Integer.parseInt(c.getString(c.getColumnIndex("activityType"))),
                                                    Integer.parseInt(c.getString(c.getColumnIndex("id"))),
                                                    3,

                                                    actID
                                            );

                                            c.close();
                                            mdb.close();
                                            putHours.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                @EverythingIsNonNull
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if (response.isSuccessful())
                                                        Snackbar.make(view, "Edited to "+newHours+"hours", Snackbar.LENGTH_SHORT);
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
                                });

                                if (viewInflated.getParent() != null)
                                    ((ViewGroup) viewInflated.getParent()).removeView(viewInflated);
                                builder.show();
                            }
                    ));

                    underlayButtons.add(new SwipeHelperRight.UnderlayButton(
                            mContext,
                            "",
                            R.drawable.ic_del_24,
                            transparent,
                            pos -> {
                                int p;
                                if (viewHolder.getAdapterPosition() == -1)
                                    p = viewHolder.getAdapterPosition() + 1;

                                else if (viewHolder.getAdapterPosition() == clgListDataSy.size())
                                    p = viewHolder.getAdapterPosition() - 1;
                                else
                                    p = viewHolder.getAdapterPosition();

                                int actID = Integer.parseInt(clgListDataSy.get(p).getId());
                                String actName = adapterClg.list.get(p).getAct();

                                DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                                mdb.createDatabase();

                                mdb.open();

                                Cursor c2 = mdb.getActAssigActNameAdmin(actName);
                                c2.moveToFirst();

                                Call<ResponseBody> putHours = RetrofitClient.getInstance().getApi().putHour(
                                        "Token " + ediary.AUTH_TOKEN,
                                        Integer.parseInt(adapterClg.list.get(p).getHours()),
                                        ediary.VEC,
                                        Integer.parseInt(c2.getString(c2.getColumnIndex("activityType"))),
                                        Integer.parseInt(c2.getString(c2.getColumnIndex("id"))),
                                        4,
                                        actID
                                );

                                mdb.dropDetails(actID);
                                clgListDataSy.remove(p);
                                adapterClg.notifyItemRemoved(p);

                                putHours.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    @EverythingIsNonNull
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    }

                                    @Override
                                    @EverythingIsNonNull
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                                mdb.close();
                        }
                    ));
                } else if (clgListDataSy.get(viewHolder.getAdapterPosition()).getState().equals("Approved")) {
                    underlayButtons.add(new UnderlayButton(
                            mContext,
                            "",
                            R.drawable.ic_eye_24,
                            transparent,
                            new UnderlayButtonClickListener() {
                                @Override
                                public void onClick(int pos) {
                                    int p;
                                    if (viewHolder.getAdapterPosition() == -1)
                                        p = viewHolder.getAdapterPosition() + 1;

                                    else if (viewHolder.getAdapterPosition() == clgListDataSy.size())
                                        p = viewHolder.getAdapterPosition() - 1;
                                    else
                                        p = viewHolder.getAdapterPosition();

                                    if (isLeader != 1) {
                                        DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                                        mdb.createDatabase();
                                        mdb.open();
                                        Cursor c = mdb.getActLeaderId(Integer.parseInt(clgListDataSy.get(p).getId()));
                                        c.moveToFirst();
                                        int leadId = c.getInt(c.getColumnIndex("Approved_by"));

                                        Snackbar sb = Snackbar.make(view, "Approved By: " + mdb.getLeaderName(leadId), Snackbar.LENGTH_LONG)
                                                .setTextColor(mContext.getColor(R.color.colorPrimaryDark));
                                        sb.getView().setBackgroundColor(mContext.getColor(R.color.colorPrimaryLight));
                                        mdb.close();
                                        sb.show();
                                    } else {
                                        DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                                        mdb.createDatabase();
                                        mdb.open();
                                        Cursor c = mdb.getActLeaderId(Integer.parseInt(clgListDataSy.get(p).getId()));
                                        c.moveToFirst();

                                        if (c.getString(c.getColumnIndex("Approved_by")).equals("null")) {
                                            Snackbar sb = Snackbar.make(view, "Approved By: PO", Snackbar.LENGTH_LONG)
                                                    .setTextColor(mContext.getColor(R.color.colorPrimaryDark));
                                            sb.getView().setBackgroundColor(mContext.getColor(R.color.colorPrimaryLight));
                                            sb.show();
                                        } else {
                                            int leadId = c.getInt(c.getColumnIndex("Approved_by"));
                                            Snackbar sb = Snackbar.make(view, "Approved By: " + mdb.getLeaderName(leadId), Snackbar.LENGTH_LONG)
                                                    .setTextColor(mContext.getColor(R.color.colorPrimaryDark));
                                            sb.getView().setBackgroundColor(mContext.getColor(R.color.colorPrimaryLight));
                                            sb.show();
                                        }
                                        mdb.close();
                                    }

                                    adapterClg.notifyItemChanged(p);
                                }
                            }
                    ));
                }
            }
        };
        
        recyclerViewHours.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewHours.setAdapter(adapterClg);
        
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeHelperRightHours);
        itemTouchHelper.attachToRecyclerView(recyclerViewHours);

        ItemTouchHelper itemTouchHelper2 = new ItemTouchHelper(swipeHelperRightArea);
        itemTouchHelper2.attachToRecyclerView(recyclerViewArea);

        ItemTouchHelper itemTouchHelper3 = new ItemTouchHelper(swipeHelperRightUniv);
        itemTouchHelper3.attachToRecyclerView(recyclerViewUniv);

        add.setOnClickListener(view1 -> {
            Log.e("AAAAAA", "" + whichAct);
            //mainSy.setVisibility(View.GONE);
            fragSy.setVisibility(View.GONE);
            univRecSy.setVisibility(View.GONE);
            areaRecSy.setVisibility(View.GONE);
            clgRecSy.setVisibility(View.GONE);
            cardViewMain.setVisibility(View.GONE);
            AddDetailsActivity detailsActivity = new AddDetailsActivity();
            Bundle args = new Bundle();
            args.putInt("whichAct", whichAct);
            args.putInt("act", act);
            detailsActivity.setArguments(args);

            FragmentManager fm = requireActivity().getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.halves_frame, detailsActivity, "AddDetailsActivity").addToBackStack("fyAct").commit();
            adapterArea.notifyDataSetChanged();
            adapterClg.notifyDataSetChanged();
            adapterUniv.notifyDataSetChanged();
        });
    }

    public List<AdapterDataMain> addAct(String whichAct) {

        ArrayList<AdapterDataMain> data = new ArrayList<>();

        DatabaseAdapter mDbHelper = new DatabaseAdapter(mContext);
        mDbHelper.createDatabase();
        mDbHelper.open();

        Cursor c = mDbHelper.getActList(whichAct);
        //Log.e("SSSHHH", "" + c.getCount());
        while (c.moveToNext()) {
            //Log.e("This", c.getString(c.getColumnIndex("ActivityCode")));

            data.add(new AdapterDataMain(
                            c.getString(c.getColumnIndex("Date")),
                            c.getString(c.getColumnIndex("ActivityName")),
                            c.getString(c.getColumnIndex("HoursWorked")),
                            c.getString(c.getColumnIndex("actID")),
                            c.getInt(c.getColumnIndex("If_Approved")),
                            c.getString(c.getColumnIndex("State"))
                    )
            );
        }
        mDbHelper.close();
        return data;
    }
}