package com.test.nss.ui.work;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.test.nss.DataBaseHelper;
import com.test.nss.DatabaseAdapter;
import com.test.nss.R;
import com.test.nss.api.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static android.content.Context.MODE_PRIVATE;
import static com.test.nss.ediary.AUTH_TOKEN;
import static com.test.nss.ediary.VEC;


public class WorkDetailsFirstFrag extends Fragment {
    static boolean isCont = false;
    public Context context;
    View root;
    FloatingActionButton notif;
    FragmentManager fm;
    List<AdapterDataWork> workListData;

    public WorkDetailsFirstFrag(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_work_details_first, container, false);
        notif = root.findViewById(R.id.notif);
        workListData = firstHalfWorkData();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerViewWork = root.findViewById(R.id.firstWorkRec);
        hideFab();
        WorkDataAdapter adapterWork = new WorkDataAdapter(workListData, context);
        recyclerViewWork.setHasFixedSize(true);
        recyclerViewWork.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewWork.setAdapter(adapterWork);

        Cursor c2;
        DatabaseAdapter m = new DatabaseAdapter(context);
        int clgComp;
        int univComp;
        int areaCompOne;
        int areaCompTwo;
        int areaLvlOne;
        int areaLvlTwo;
        m.createDatabase();
        m.open();

        c2 = m.getHoursDet("College", 1);
        c2.moveToFirst();
        clgComp = c2.getInt(c2.getColumnIndex("HoursWorked"));

        c2 = m.getHoursDet("University", 1);
        c2.moveToFirst();
        univComp = c2.getInt(c2.getColumnIndex("HoursWorked"));

        c2 = m.getHoursDet("Area Based 1", 1);
        c2.moveToFirst();
        areaCompOne = c2.getInt(c2.getColumnIndex("HoursWorked"));
        areaLvlOne = c2.getInt(c2.getColumnIndex("HoursWorked"));

        c2 = m.getHoursDet("Area Based 1", 1);
        c2.moveToFirst();
        areaCompTwo = c2.getInt(c2.getColumnIndex("HoursWorked"));
        areaLvlTwo = c2.getInt(c2.getColumnIndex("HoursWorked"));
        m.close();

        SharedPreferences sharedPreferences = context.getSharedPreferences("KEY", MODE_PRIVATE);
        isCont = sharedPreferences.getBoolean("isCont", false);

        Log.e("AA", "" + isCont);
        if (clgComp >= 20 && univComp >= 20) {
            if (areaCompOne >= 20 && areaCompTwo >= 20 && areaCompOne + areaCompTwo >= areaLvlOne + areaLvlTwo && !isCont) {
                revealFab();
                notif.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.delDialog);
                        builder.setTitle("Congratulations!");
                        builder.setMessage("Do you want to continue for next year?");
                        builder.setCancelable(false);

                        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface2, int i) {
                                AlertDialog.Builder b = new AlertDialog.Builder(context, R.style.delDialog);
                                b.setMessage("Are you sure?");
                                SharedPreferences shareit = context.getSharedPreferences("KEY", MODE_PRIVATE);
                                SharedPreferences.Editor eddy = shareit.edit();

                                b.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        //dialogInterface2.dismiss();
                                        Call<ResponseBody> selfRegContinue = RetrofitClient.getInstance().getApi().putContinue("Token " + AUTH_TOKEN);
                                        selfRegContinue.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            @EverythingIsNonNull
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.isSuccessful() && response.body() != null) {
                                                    try {
                                                        isCont = true;
                                                        eddy.putBoolean("isCont", true);
                                                        eddy.apply();
                                                        Log.e("Done", response.body().string());
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                } else if (response.errorBody() != null) {
                                                    try {
                                                        Log.e("Done", response.errorBody().string());
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                            }
                                        });
                                        hideFab();
                                    }
                                });
                                b.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        //dialogInterface2.cancel();
                                        isCont = false;
                                        eddy.putBoolean("isCont", false);
                                        eddy.apply();
                                    }
                                });
                                //eddy.apply();
                                b.show();
                            }
                        });
                        builder.show();
                    }
                });
            }
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        fm = requireActivity().getSupportFragmentManager();
        //fm.popBackStack("WorkFrag", 0);
    }

    public List<AdapterDataWork> firstHalfWorkData() {
        ArrayList<AdapterDataWork> data = new ArrayList<>();

        DatabaseAdapter m = new DatabaseAdapter(context);
        m.createDatabase();
        m.open();
        int areaCompOne = m.getSumHours("First Year Area Based One");
        int areaCompTwo = m.getSumHours("First Year Area Based Two");
        int clgComp = m.getSumHours("First Year College");
        int univComp = m.getSumHours("First Year University");

        int areaLvlOne = m.getHours("Area Based Level One");
        int areaLvlTwo = m.getHours("Area Based Level Two");
        int clgLvl = m.getHours("College Level");
        int univLvl = m.getHours("University Level");

        m.close();
        //Log.i("TAG", "firstHalfWorkData: " + areaCompTwo);
        int areaRemOneHours;
        int areaRemTwoHours;
        int univRemHours;
        int clgRemHours;

        if (areaCompOne >= 1 && areaLvlOne - areaCompOne > 0)
            areaRemOneHours = areaLvlOne - areaCompOne;
        else if (areaCompOne == 0)
            areaRemOneHours = areaLvlOne;
        else
            areaRemOneHours = Integer.parseInt("00");

        if (areaCompTwo >= 1 && areaLvlTwo - areaCompTwo > 0)
            areaRemTwoHours = areaLvlTwo - areaCompTwo;
        else if (areaCompTwo == 0)
            areaRemTwoHours = areaLvlTwo;
        else
            areaRemTwoHours = Integer.parseInt("00");

        if (clgComp >= 1 && clgLvl - clgComp > 0)
            clgRemHours = clgLvl - clgComp;
        else if (clgComp == 0)
            clgRemHours = clgLvl;
        else
            clgRemHours = Integer.parseInt("00");

        if (univComp >= 1 && univLvl - univComp > 0)
            univRemHours = univLvl - univComp;
        else if (univComp == 0)
            univRemHours = univLvl;
        else
            univRemHours = Integer.parseInt("00");

        deleteData("WorkHoursFy");
        m = new DatabaseAdapter(context);
        m.createDatabase();
        m.open();
        m.insertWork(
                VEC,
                "Area Based 1",
                areaLvlOne,
                areaCompOne,
                areaRemOneHours,
                1
        );
        m.insertWork(
                VEC,
                "Area Based 2",
                areaLvlTwo,
                areaCompTwo,
                areaRemTwoHours,
                1
        );
        m.insertWork(
                VEC,
                "University",
                univLvl,
                univComp,
                univRemHours,
                1
        );
        m.insertWork(
                VEC,
                "College",
                clgLvl,
                clgComp,
                clgRemHours,
                1
        );
        m.close();

        data.add(new AdapterDataWork("Area Based 1", String.valueOf(areaLvlOne), String.valueOf(areaCompOne), String.valueOf(areaRemOneHours)));
        data.add(new AdapterDataWork("Area Based 2", String.valueOf(areaLvlTwo), String.valueOf(areaCompTwo), String.valueOf(areaRemTwoHours)));
        data.add(new AdapterDataWork("University", String.valueOf(univLvl), String.valueOf(univComp), String.valueOf(univRemHours)));
        data.add(new AdapterDataWork("College", String.valueOf(clgLvl), String.valueOf(clgComp), String.valueOf(clgRemHours)));

        return data;
    }

    private void revealFab() {
        View v = root.findViewById(R.id.notif);
        int x = v.getWidth() / 2;
        int y = v.getHeight() / 2;

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

    private void hideFab() {
        View v = root.findViewById(R.id.notif);
        int x = v.getWidth() / 2;
        int y = v.getHeight() / 2;

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

    public void deleteData(String table) {
        DatabaseAdapter mDbHelper2 = new DatabaseAdapter(context);
        mDbHelper2.createDatabase();
        mDbHelper2.open();
        DataBaseHelper mDb2 = new DataBaseHelper(context);
        SQLiteDatabase m = mDb2.getWritableDatabase();
        m.execSQL("DELETE FROM " + table);
        mDbHelper2.close();
        m.close();
        mDb2.close();
    }
}