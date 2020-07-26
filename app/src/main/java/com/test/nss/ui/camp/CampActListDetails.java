package com.test.nss.ui.camp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.nss.R;
import com.test.nss.TestAdapter;
import com.test.nss.api.RetrofitClient;
import com.test.nss.startActivity;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampActListDetails extends Fragment {

    View root;
    List<AdapterCampActList> campData;
    RecyclerView recCampList;
    Button go_back;
    LinearLayout camp_main_details;
    ConstraintLayout campActAll;
    Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_camp_activity_all, container, false);
        mContext = requireContext();
        campData = addCampData();
        go_back = root.findViewById(R.id.go_back);
        camp_main_details = requireActivity().findViewById(R.id.camp_main_details);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recCampList = root.findViewById(R.id.recCampListAll);
        CampActListDataAdapter campDataAdapter = new CampActListDataAdapter(campData, mContext);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                switch (direction) {
                    case ItemTouchHelper.RIGHT:
                        int l = viewHolder.getAdapterPosition();

                        AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext, R.style.delDialog);
                        builder2.setTitle("Are you sure?");
                        builder2.setNegativeButton("No", (dialog, which) -> {
                                    dialog.cancel();
                                    campDataAdapter.notifyItemChanged(l);
                                }
                        );

                        builder2.setPositiveButton("Yes", (dialog, which) -> {
                            dialog.dismiss();


                            TestAdapter mdb = new TestAdapter(mContext);
                            mdb.createDatabase();
                            mdb.open();

                            Log.e("asas", "onSwiped: "+campData.get(l).getCampId());
                                    Cursor c = mdb.getCampActListAllById(Integer.parseInt(campData.get(l).getCampId()));
                                    Cursor c2 = mdb.getCampActListId(campData.get(l).getCampTitle());
                                    c.moveToFirst();
                                    c2.moveToFirst();

                                    Call<ResponseBody> putCamp = RetrofitClient.getInstance().getApi().putCamp(
                                            "Token " + startActivity.AUTH_TOKEN,
                                            startActivity.VEC,
                                            c2.getInt(c2.getColumnIndex("CampId")),
                                            c.getString(c.getColumnIndex("CampActivityDescription")),
                                            c.getInt(c.getColumnIndex("CampDay")),
                                            c.getString(c.getColumnIndex("College_Name")),
                                            3,
                                            c.getInt(c.getColumnIndex("id"))
                                    );

                                    mdb.dropDetailsCamp(Integer.parseInt(campData.get(l).getCampId()));
                                    Toast.makeText(mContext, "" + campData.get(l).getCampId(), Toast.LENGTH_SHORT).show();
                                    campData.remove(l);
                                    campDataAdapter.notifyItemRemoved(l);

                                    putCamp.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                                        }
                                    });
                                    mdb.close();
                                }
                        );

                        //if (viewInflated2.getParent() != null)
                        //  ((ViewGroup) viewInflated2.getParent()).removeView(viewInflated2);
                        builder2.show();
                        break;
                    case ItemTouchHelper.LEFT:
                        TestAdapter mdb2 = new TestAdapter(mContext);
                        mdb2.createDatabase();
                        mdb2.open();
                        int p = viewHolder.getAdapterPosition();

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.inputDialog);
                        View viewInflated = LayoutInflater.from(mContext).inflate(R.layout.camp_new_input_layout, (ViewGroup) view, false);

                        EditText inputDesc = (EditText) viewInflated.findViewById(R.id.inputDesc);
                        EditText inputDay = (EditText) viewInflated.findViewById(R.id.inputDay);

                        builder.setView(viewInflated);
                        Log.e("Here", "onSwiped: " + p + " " + campData.get(p).getCampId());

                        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                                    dialog.cancel();
                                    campDataAdapter.notifyItemChanged(p);
                                }
                        );

                        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            String desc = inputDesc.getText().toString().trim();
                            String day = inputDay.getText().toString().trim();

                            if (desc.equals(""))
                                Toast.makeText(mContext, "Please enter Camp Description", Toast.LENGTH_SHORT).show();

                            else if (day.equals("")) {
                                Toast.makeText(mContext, "Please enter Camp Day", Toast.LENGTH_SHORT).show();
                            } else if (Integer.parseInt(day) <= 0 || Integer.parseInt(day) > 7) {
                                Toast.makeText(mContext, "Fill between 1 to 7", Toast.LENGTH_SHORT).show();
                            } else {
                                dialog.dismiss();
                                Log.e("Here", "onSwiped: " + p + " " + campData.get(p).getCampId());
                                mdb2.setDetailsCamp(desc, Integer.parseInt(day), Integer.parseInt(campData.get(p).getCampId()));
                                Cursor c = mdb2.getCampActListAllById(Integer.parseInt(campData.get(p).getCampId()));
                                Cursor c2 = mdb2.getCampActListId(campData.get(p).getCampTitle());
                                c.moveToFirst();
                                c2.moveToFirst();
                                if (c != null) {
                                    campData.add(p, new AdapterCampActList(
                                            c.getString(c.getColumnIndex("CampActivityTitle")),
                                            c.getString(c.getColumnIndex("CampActivityDescription")),
                                            c.getString(c.getColumnIndex("CampDay")),
                                            c.getString(c.getColumnIndex("id"))
                                    ));
                                }
                                campData.remove(p + 1);

                                campDataAdapter.notifyDataSetChanged();

                                c.moveToFirst();
                                c2.moveToFirst();

                                Call<ResponseBody> putCamp = RetrofitClient.getInstance().getApi().putCamp(
                                        "Token " + startActivity.AUTH_TOKEN,
                                        startActivity.VEC,
                                        c2.getInt(c2.getColumnIndex("CampId")),
                                        c.getString(c.getColumnIndex("CampActivityDescription")),
                                        c.getInt(c.getColumnIndex("CampDay")),
                                        c.getString(c.getColumnIndex("College_Name")),
                                        2,
                                        c.getInt(c.getColumnIndex("id"))
                                );
                                mdb2.close();

                                putCamp.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }
                        });

                        //mdb.setDetailsCamp();
                        if (viewInflated.getParent() != null)
                            ((ViewGroup) viewInflated.getParent()).removeView(viewInflated);
                        builder.show();

                        break;
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeRightActionIcon(R.drawable.ic_del_24)
                        .addSwipeLeftActionIcon(R.drawable.ic_edit_24)
                        .addBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        recCampList.setLayoutManager(new LinearLayoutManager(mContext));
        recCampList.setAdapter(campDataAdapter);
        campActAll = root.findViewById(R.id.campActAll);

        go_back.setOnClickListener(view1 -> {
            camp_main_details.setVisibility(View.VISIBLE);
            onDetach();
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recCampList);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        camp_main_details.setVisibility(View.VISIBLE);
        FragmentManager fm = requireActivity().getSupportFragmentManager();

        campActAll.setVisibility(View.GONE);
        if (fm.getBackStackEntryCount() > 0) {
            Log.e("CampFrag", "onDetach: " + fm.getBackStackEntryCount());
            fm.popBackStackImmediate(null, 0);
            fm.popBackStack("CampList", 0);
        }
    }

    public List<AdapterCampActList> addCampData() {
        ArrayList<AdapterCampActList> data2 = new ArrayList<>();

        TestAdapter mDbHelper = new TestAdapter(mContext);
        mDbHelper.createDatabase();
        mDbHelper.open();
        Cursor c2 = mDbHelper.getCampActListAll();
        Log.e("SSS", "" + c2.getCount());

        while (c2.moveToNext()) {
            //Log.e("camp", c2.getString(c2.getColumnIndex("College_name")));
            data2.add(new AdapterCampActList(
                    c2.getString(c2.getColumnIndex("CampActivityTitle")),
                    c2.getString(c2.getColumnIndex("CampActivityDescription")),
                    c2.getString(c2.getColumnIndex("CampDay")),
                    c2.getString(c2.getColumnIndex("id"))
            ));
        }
        mDbHelper.close();
        return data2;
    }
}