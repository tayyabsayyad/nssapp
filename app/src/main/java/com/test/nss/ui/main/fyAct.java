package com.test.nss.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.test.nss.DatabaseAdapter;
import com.test.nss.R;
import com.test.nss.api.RetrofitClient;
import com.test.nss.ediary;

import org.apache.commons.collections4.ListUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static com.test.nss.R.color.colorPrimaryDark;
import static com.test.nss.ediary.isFirst;
import static com.test.nss.ediary.isLeader;
import static com.test.nss.ediary.primaryColDark;

public class fyAct extends Fragment {

    private static final String TAG = "fyAct";
    View root;
    Button univ;
    Button area;
    Button clg;

    Context mContext;

    RecyclerView univRecFy;
    RecyclerView areaRecFy;
    RecyclerView clgRecFy;

    List<AdapterDataMain> univListDataFy;
    List<AdapterDataMain> areaDataMainFy;
    List<AdapterDataMain> clgListDataFy;

    CardView cardViewMain;

    Button add;
    LinearLayout fragFy;

    LinearLayout actHeaderInput;

    int whichAct;
    int act;
    int newHours = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_fy_act, container, false);
        mContext = requireContext();

        cardViewMain = root.findViewById(R.id.details_main_card);
        actHeaderInput = requireActivity().findViewById(R.id.actHeaderInput);
        actHeaderInput.setVisibility(View.GONE);

        univ = root.findViewById(R.id.main_univ_fy);
        area = root.findViewById(R.id.main_area_fy);
        clg = root.findViewById(R.id.main_clg_fy);

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

            Animation animation = new TranslateAnimation(0, 0, -100, 0);

            animation.setDuration(4000);
            Snackbar.make(view, "Swipe left on activity to modify", Snackbar.LENGTH_SHORT).show();

            Drawable bg = ContextCompat.getDrawable(mContext, R.drawable.ic_circle);
            //bg.setTint(mContext.getColor(blackGrey));
            whichAct = 13;
            act = 0;
            //mainFy.setVisibility(View.VISIBLE);
            univRecFy.setVisibility(View.VISIBLE);
            areaRecFy.setVisibility(View.GONE);
            clgRecFy.setVisibility(View.GONE);
            if (isFirst) {
                add.setVisibility(View.VISIBLE);
                add.startAnimation(animation);
            } else
                add.setVisibility(View.GONE);

            univ.setTextColor(primaryColDark);
            area.setTextColor(Color.BLACK);
            clg.setTextColor(Color.BLACK);
        });

        area.setOnClickListener(v -> {
            whichAct = 12;
            act = 1;
            //mainFy.setVisibility(View.VISIBLE);
            if (isFirst)
                add.setVisibility(View.VISIBLE);
            else
                add.setVisibility(View.GONE);

            univRecFy.setVisibility(View.GONE);
            areaRecFy.setVisibility(View.VISIBLE);
            clgRecFy.setVisibility(View.GONE);

            area.setTextColor(primaryColDark);
            univ.setTextColor(Color.BLACK);
            clg.setTextColor(Color.BLACK);
        });


        clg.setOnClickListener(v -> {
            //mainFy.setVisibility(View.VISIBLE);

            whichAct = 11;
            act = 2;


            univRecFy.setVisibility(View.GONE);
            areaRecFy.setVisibility(View.GONE);
            clgRecFy.setVisibility(View.VISIBLE);

            clg.setTextColor(primaryColDark);
            univ.setTextColor(Color.BLACK);
            area.setTextColor(Color.BLACK);

            if (isFirst)
                add.setVisibility(View.VISIBLE);
            else
                add.setVisibility(View.GONE);
        });

        clgListDataFy = addAct("First Year College");
        //areaDataMainFy = addAct("First Year Area Based One") + addAct("First Year Area Based One");
        areaDataMainFy = ListUtils.union(addAct("First Year Area Based One"), addAct("First Year Area Based Two"));

        univListDataFy = addAct("First Year University");

        // Recycler View Univ
        RecyclerView recyclerViewUniv = root.findViewById(R.id.univRecFy);
        MyListAdapter adapterUniv = new MyListAdapter(univListDataFy, mContext);
        adapterUniv.notifyDataSetChanged();

        ItemTouchHelper.SimpleCallback simpleCallbackUniv = new ItemTouchHelper.SimpleCallback
                (100, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int p;
                if (viewHolder.getAdapterPosition() == -1)
                    p = viewHolder.getAdapterPosition() + 1;

                else if (viewHolder.getAdapterPosition() == univListDataFy.size())
                    p = viewHolder.getAdapterPosition() - 1;
                else
                    p = viewHolder.getAdapterPosition();

                if (isFirst && adapterUniv.list.get(p).getState().equals("Modified") ||
                        isFirst && adapterUniv.list.get(p).getState().equals("Submitted")) {
                    if (direction == ItemTouchHelper.RIGHT) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext, R.style.delDialog);
                        builder2.setMessage("Are you sure you want to delete?");
                        builder2.setCancelable(false);
                        builder2.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        adapterUniv.notifyItemChanged(p);
                                    }
                                }
                        );

                        builder2.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                int actID = Integer.parseInt(univListDataFy.get(p).getId());
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
                                univListDataFy.remove(p);
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
                        });
                        builder2.show();
                    } else if (direction == ItemTouchHelper.LEFT) {
                        int actID = Integer.parseInt(univListDataFy.get(p).getId());
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.inputDialog);
                        View viewInflated = LayoutInflater.from(mContext).inflate(R.layout.hours_input_layout, (ViewGroup) view, false);

                        EditText input = viewInflated.findViewById(R.id.input);
                        builder.setView(viewInflated);

                        builder.setCancelable(false);
                        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                                    dialog.cancel();
                                    adapterUniv.notifyItemChanged(p);
                                }
                        );

                        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            dialog.dismiss();

                            //Log.e("Yes this", adapterArea.list.get(p).getAct());
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

                                    univListDataFy.add(p, new AdapterDataMain(
                                                    adapterUniv.list.get(p).getDate(),
                                                    adapterUniv.list.get(p).getAct(),
                                                    String.valueOf(newHours),
                                                    adapterUniv.list.get(p).getId(),
                                                    adapterUniv.list.get(p).isApproved(),
                                                    "Modified"
                                            )
                                    );

                                    adapterUniv.notifyDataSetChanged();
                                    univListDataFy.remove(p + 1);
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
                                                Snackbar.make(view, "Edited to " + newHours + "hours", Snackbar.LENGTH_SHORT);
                                            else if (response.errorBody() != null) {
                                                try {
                                                    Toast.makeText(requireContext(), "onResponse: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
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
                                } else {
                                    Toast.makeText(requireContext(), "Please enter atleast\nan hour between 1 to 10", Toast.LENGTH_SHORT).show();
                                    adapterUniv.notifyItemChanged(p);
                                }
                            } else
                                adapterUniv.notifyItemChanged(p);
                        });

                        if (viewInflated.getParent() != null)
                            ((ViewGroup) viewInflated.getParent()).removeView(viewInflated);
                        builder.show();

                    }
                } else if (univListDataFy.get(viewHolder.getAdapterPosition()).getState().equals("Approved")) {

                    if (isLeader != 1) {
                        DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                        mdb.createDatabase();
                        mdb.open();
                        Cursor c = mdb.getActLeaderId(Integer.parseInt(univListDataFy.get(p).getId()));
                        c.moveToFirst();
                        int leadId = c.getInt(c.getColumnIndex("Approved_by"));

                        Snackbar sb = Snackbar.make(view, "Approved By: " + mdb.getLeaderName(leadId), Snackbar.LENGTH_LONG)
                                .setTextColor(mContext.getColor(colorPrimaryDark));
                        sb.getView().setBackgroundColor(mContext.getColor(R.color.colorPrimaryLight));
                        mdb.close();
                        sb.show();
                    } else {
                        DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                        mdb.createDatabase();
                        mdb.open();
                        Cursor c = mdb.getActLeaderId(Integer.parseInt(univListDataFy.get(p).getId()));
                        c.moveToFirst();

                        if (c.getString(c.getColumnIndex("Approved_by")).equals("null")) {
                            Snackbar sb = Snackbar.make(view, "Approved By: PO", Snackbar.LENGTH_LONG)
                                    .setTextColor(mContext.getColor(colorPrimaryDark));
                            sb.getView().setBackgroundColor(mContext.getColor(R.color.colorPrimaryLight));
                            sb.show();
                        } else {
                            int leadId = c.getInt(c.getColumnIndex("Approved_by"));
                            Snackbar sb = Snackbar.make(view, "Approved By: " + mdb.getLeaderName(leadId), Snackbar.LENGTH_LONG)
                                    .setTextColor(mContext.getColor(colorPrimaryDark));
                            sb.getView().setBackgroundColor(mContext.getColor(R.color.colorPrimaryLight));
                            sb.show();
                        }
                        mdb.close();
                    }
                    adapterUniv.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                int p;
                if (viewHolder.getAdapterPosition() == -1)
                    p = viewHolder.getAdapterPosition() + 1;
                else if (viewHolder.getAdapterPosition() == univListDataFy.size())
                    p = viewHolder.getAdapterPosition() - 1;
                else
                    p = viewHolder.getAdapterPosition();
                if (isFirst && adapterUniv.list.get(p).getState().equals("Modified") ||
                        isFirst && adapterUniv.list.get(p).getState().equals("Submitted")) {
                    new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addSwipeRightActionIcon(R.drawable.ic_del_24)
                            .addSwipeLeftActionIcon(R.drawable.ic_edit_24)
                            .addBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
                            .create()
                            .decorate();
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                } else if (univListDataFy.get(p).getState().equals("Approved")) {
                    new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addSwipeRightActionIcon(R.drawable.ic_eye_24)
                            .addSwipeLeftActionIcon(R.drawable.ic_eye_24)
                            .addBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
                            .create()
                            .decorate();
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };
        adapterUniv.notifyDataSetChanged();

        recyclerViewUniv.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewUniv.setAdapter(adapterUniv);

        // Recycler View Area
        RecyclerView recyclerViewArea = root.findViewById(R.id.areaRecFy);
        MyListAdapter adapterArea = new MyListAdapter(areaDataMainFy, mContext);
        adapterArea.notifyDataSetChanged();

        ItemTouchHelper.SimpleCallback simpleCallbackArea = new ItemTouchHelper.SimpleCallback
                (100, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int p;
                if (viewHolder.getAdapterPosition() == -1)
                    p = viewHolder.getAdapterPosition() + 1;

                else if (viewHolder.getAdapterPosition() == areaDataMainFy.size())
                    p = viewHolder.getAdapterPosition() - 1;
                else
                    p = viewHolder.getAdapterPosition();

                if (isFirst && adapterArea.list.get(viewHolder.getAdapterPosition()).getState().equals("Modified") ||
                        isFirst && adapterArea.list.get(viewHolder.getAdapterPosition()).getState().equals("Submitted")) {
                    if (direction == ItemTouchHelper.RIGHT) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext, R.style.delDialog);
                        builder2.setMessage("Are you sure you want to delete?");
                        builder2.setCancelable(false);
                        builder2.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        adapterArea.notifyItemChanged(p);
                                    }
                                }
                        );
                        builder2.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                int actID = Integer.parseInt(areaDataMainFy.get(p).getId());
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
                                areaDataMainFy.remove(p);
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
                        });
                        builder2.show();
                    } else if (direction == ItemTouchHelper.LEFT) {
                        int actID = Integer.parseInt(areaDataMainFy.get(p).getId());
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.inputDialog);
                        View viewInflated = LayoutInflater.from(mContext).inflate(R.layout.hours_input_layout, (ViewGroup) view, false);

                        EditText input = viewInflated.findViewById(R.id.input);
                        builder.setView(viewInflated);
                        builder.setCancelable(false);

                        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                                    dialog.cancel();
                                    adapterArea.notifyItemChanged(p);
                                }
                        );

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

                                    areaDataMainFy.add(p, new AdapterDataMain(
                                                    adapterArea.list.get(p).getDate(),
                                                    adapterArea.list.get(p).getAct(),
                                                    String.valueOf(newHours),
                                                    adapterArea.list.get(p).getId(),
                                                    adapterArea.list.get(p).isApproved(),
                                                    "Modified"
                                            )
                                    );

                                    adapterArea.notifyDataSetChanged();
                                    areaDataMainFy.remove(p + 1);
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
                                                Snackbar.make(view, "Edited to " + newHours + "hours", Snackbar.LENGTH_SHORT);
                                            else if (response.errorBody() != null) {
                                                try {
                                                    Toast.makeText(requireContext(), "onResponse: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
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
                                } else {
                                    Toast.makeText(requireContext(), "Please enter atleast\nan hour between 1 to 10", Toast.LENGTH_SHORT).show();
                                    adapterArea.notifyItemChanged(p);
                                }
                            } else
                                adapterArea.notifyItemChanged(p);
                        });

                        if (viewInflated.getParent() != null)
                            ((ViewGroup) viewInflated.getParent()).removeView(viewInflated);
                        builder.show();
                    }
                } else if (areaDataMainFy.get(viewHolder.getAdapterPosition()).getState().equals("Approved")) {

                    if (isLeader != 1) {
                        DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                        mdb.createDatabase();
                        mdb.open();
                        Cursor c = mdb.getActLeaderId(Integer.parseInt(areaDataMainFy.get(p).getId()));
                        c.moveToFirst();
                        int leadId = c.getInt(c.getColumnIndex("Approved_by"));

                        Snackbar sb = Snackbar.make(view, "Approved By: " + mdb.getLeaderName(leadId), Snackbar.LENGTH_LONG)
                                .setTextColor(mContext.getColor(colorPrimaryDark));
                        sb.getView().setBackgroundColor(mContext.getColor(R.color.colorPrimaryLight));
                        mdb.close();
                        sb.show();
                    } else {
                        DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                        mdb.createDatabase();
                        mdb.open();
                        Cursor c = mdb.getActLeaderId(Integer.parseInt(areaDataMainFy.get(p).getId()));
                        c.moveToFirst();

                        if (c.getString(c.getColumnIndex("Approved_by")).equals("null")) {
                            Snackbar sb = Snackbar.make(view, "Approved By: PO", Snackbar.LENGTH_LONG)
                                    .setTextColor(mContext.getColor(colorPrimaryDark));
                            sb.getView().setBackgroundColor(mContext.getColor(R.color.colorPrimaryLight));
                            sb.show();
                        } else {
                            int leadId = c.getInt(c.getColumnIndex("Approved_by"));
                            Snackbar sb = Snackbar.make(view, "Approved By: " + mdb.getLeaderName(leadId), Snackbar.LENGTH_LONG)
                                    .setTextColor(mContext.getColor(colorPrimaryDark));
                            sb.getView().setBackgroundColor(mContext.getColor(R.color.colorPrimaryLight));
                            sb.show();
                        }
                        mdb.close();
                    }
                    adapterArea.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                int p;
                if (viewHolder.getAdapterPosition() == -1)
                    p = viewHolder.getAdapterPosition() + 1;
                else if (viewHolder.getAdapterPosition() == areaDataMainFy.size())
                    p = viewHolder.getAdapterPosition() - 1;
                else
                    p = viewHolder.getAdapterPosition();
                if (isFirst && adapterArea.list.get(p).getState().equals("Modified") ||
                        isFirst && adapterArea.list.get(p).getState().equals("Submitted")) {
                    new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addSwipeRightActionIcon(R.drawable.ic_del_24)
                            .addSwipeLeftActionIcon(R.drawable.ic_edit_24)
                            .addBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
                            .create()
                            .decorate();
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                } else if (areaDataMainFy.get(p).getState().equals("Approved")) {
                    new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addSwipeRightActionIcon(R.drawable.ic_eye_24)
                            .addSwipeLeftActionIcon(R.drawable.ic_eye_24)
                            .addBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
                            .create()
                            .decorate();
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };

        recyclerViewArea.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewArea.setAdapter(adapterArea);
        adapterArea.notifyDataSetChanged();

        // Recycler View College
        RecyclerView recyclerViewHours = root.findViewById(R.id.hoursRecFy);
        MyListAdapter adapterClg = new MyListAdapter(clgListDataFy, mContext);
        adapterClg.notifyDataSetChanged();

        ItemTouchHelper.SimpleCallback simpleCallbackHours = new ItemTouchHelper.SimpleCallback
                (100, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int p;
                if (viewHolder.getAdapterPosition() == -1)
                    p = viewHolder.getAdapterPosition() + 1;

                else if (viewHolder.getAdapterPosition() == clgListDataFy.size())
                    p = viewHolder.getAdapterPosition() - 1;
                else
                    p = viewHolder.getAdapterPosition();

                if (isFirst && adapterClg.list.get(viewHolder.getAdapterPosition()).getState().equals("Modified") ||
                        isFirst && adapterClg.list.get(viewHolder.getAdapterPosition()).getState().equals("Submitted")) {
                    if (direction == ItemTouchHelper.RIGHT) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext, R.style.delDialog);
                        builder2.setMessage("Are you sure you want to delete?");
                        builder2.setCancelable(false);
                        builder2.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        adapterClg.notifyItemChanged(p);
                                    }
                                }
                        );

                        builder2.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                int actID = Integer.parseInt(clgListDataFy.get(p).getId());
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
                                clgListDataFy.remove(p);
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
                        });
                        builder2.show();
                    } else if (direction == ItemTouchHelper.LEFT) {
                        int actID = Integer.parseInt(clgListDataFy.get(p).getId());
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.inputDialog);
                        View viewInflated = LayoutInflater.from(mContext).inflate(R.layout.hours_input_layout, (ViewGroup) view, false);

                        EditText input = viewInflated.findViewById(R.id.input);
                        builder.setView(viewInflated);
                        builder.setCancelable(false);

                        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                                    dialog.cancel();
                                    adapterClg.notifyItemChanged(p);
                                }
                        );

                        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            dialog.dismiss();

                            //Log.e("Yes this", adapterClg.list.get(p).getAct());
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

                                    clgListDataFy.add(p, new AdapterDataMain(
                                                    adapterClg.list.get(p).getDate(),
                                                    adapterClg.list.get(p).getAct(),
                                                    String.valueOf(newHours),
                                                    adapterClg.list.get(p).getId(),
                                                    adapterClg.list.get(p).isApproved(),
                                                    "Modified"
                                            )
                                    );

                                    adapterClg.notifyDataSetChanged();
                                    clgListDataFy.remove(p + 1);
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
                                                Snackbar.make(view, "Edited to " + newHours + "hours", Snackbar.LENGTH_SHORT);
                                            else if (response.errorBody() != null) {
                                                try {
                                                    Toast.makeText(requireContext(), "onResponse: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
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
                                } else {
                                    Toast.makeText(requireContext(), "Please enter atleast\nan hour between 1 to 10", Toast.LENGTH_SHORT).show();
                                    adapterClg.notifyItemChanged(p);
                                }
                            } else
                                adapterClg.notifyItemChanged(p);
                        });

                        if (viewInflated.getParent() != null)
                            ((ViewGroup) viewInflated.getParent()).removeView(viewInflated);
                        builder.show();
                    }
                } else if (clgListDataFy.get(viewHolder.getAdapterPosition()).getState().equals("Approved")) {

                    if (isLeader != 1) {
                        DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                        mdb.createDatabase();
                        mdb.open();
                        Cursor c = mdb.getActLeaderId(Integer.parseInt(clgListDataFy.get(p).getId()));
                        c.moveToFirst();
                        int leadId = c.getInt(c.getColumnIndex("Approved_by"));

                        Snackbar sb = Snackbar.make(view, "Approved By: " + mdb.getLeaderName(leadId), Snackbar.LENGTH_LONG)
                                .setTextColor(mContext.getColor(colorPrimaryDark));
                        sb.getView().setBackgroundColor(mContext.getColor(R.color.colorPrimaryLight));
                        mdb.close();
                        sb.show();
                    } else {
                        DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                        mdb.createDatabase();
                        mdb.open();
                        Cursor c = mdb.getActLeaderId(Integer.parseInt(clgListDataFy.get(p).getId()));
                        c.moveToFirst();

                        if (c.getString(c.getColumnIndex("Approved_by")).equals("null")) {
                            Snackbar sb = Snackbar.make(view, "Approved By: PO", Snackbar.LENGTH_LONG)
                                    .setTextColor(mContext.getColor(colorPrimaryDark));
                            sb.getView().setBackgroundColor(mContext.getColor(R.color.colorPrimaryLight));
                            sb.show();
                        } else {
                            int leadId = c.getInt(c.getColumnIndex("Approved_by"));
                            Snackbar sb = Snackbar.make(view, "Approved By: " + mdb.getLeaderName(leadId), Snackbar.LENGTH_LONG)
                                    .setTextColor(mContext.getColor(colorPrimaryDark));
                            sb.getView().setBackgroundColor(mContext.getColor(R.color.colorPrimaryLight));
                            sb.show();
                        }
                        mdb.close();
                    }
                    adapterClg.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                int p;
                if (viewHolder.getAdapterPosition() == -1)
                    p = viewHolder.getAdapterPosition() + 1;
                else if (viewHolder.getAdapterPosition() == clgListDataFy.size())
                    p = viewHolder.getAdapterPosition() - 1;
                else
                    p = viewHolder.getAdapterPosition();
                if (isFirst && adapterClg.list.get(p).getState().equals("Modified") ||
                        isFirst && adapterClg.list.get(p).getState().equals("Submitted")) {
                    new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addSwipeRightActionIcon(R.drawable.ic_del_24)
                            .addSwipeLeftActionIcon(R.drawable.ic_edit_24)
                            .addBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
                            .create()
                            .decorate();
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                } else if (clgListDataFy.get(p).getState().equals("Approved")) {
                    new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addSwipeRightActionIcon(R.drawable.ic_eye_24)
                            .addSwipeLeftActionIcon(R.drawable.ic_eye_24)
                            .addBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
                            .create()
                            .decorate();
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };
        adapterClg.notifyDataSetChanged();

        recyclerViewHours.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewHours.setAdapter(adapterClg);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallbackHours);
        itemTouchHelper.attachToRecyclerView(recyclerViewHours);

        ItemTouchHelper itemTouchHelper2 = new ItemTouchHelper(simpleCallbackArea);
        itemTouchHelper2.attachToRecyclerView(recyclerViewArea);

        ItemTouchHelper itemTouchHelper3 = new ItemTouchHelper(simpleCallbackUniv);
        itemTouchHelper3.attachToRecyclerView(recyclerViewUniv);

        add.setOnClickListener(view1 -> {
            onDetach();

            //mainFy.setVisibility(View.GONE);
            fragFy.setVisibility(View.GONE);
            univRecFy.setVisibility(View.GONE);
            areaRecFy.setVisibility(View.GONE);
            clgRecFy.setVisibility(View.GONE);
            cardViewMain.setVisibility(View.GONE);

            AddDetailsActivity detailsActivity = new AddDetailsActivity();
            Bundle args = new Bundle();
            Log.e(TAG, "onViewCreated: " + whichAct);
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    // TODO: Check this
    @Override
    public void onDetach() {
        super.onDetach();

        FragmentManager fm = requireActivity().getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            //Log.e("fyAct", "onDetach: " + fm.getBackStackEntryCount());
            //fm.popBackStackImmediate("fyAct", 0);
            //fm.popBackStack("fyAct", 0);
        }
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