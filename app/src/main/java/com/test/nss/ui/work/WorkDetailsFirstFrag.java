package com.test.nss.ui.work;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
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


public class WorkDetailsFirstFrag extends Fragment {
    static boolean isCont = false;
    static String c = "College";
    static String u = "University";
    static String a1 = "Area Based 1";
    static String a2 = "Area Based 2";
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

        c2 = m.getHoursDet(c, 1);
        c2.moveToFirst();
        clgComp = c2.getInt(c2.getColumnIndex("HoursWorked"));

        c2 = m.getHoursDet(u, 1);
        c2.moveToFirst();
        univComp = c2.getInt(c2.getColumnIndex("HoursWorked"));

        c2 = m.getHoursDet(a1, 1);
        c2.moveToFirst();
        areaCompOne = c2.getInt(c2.getColumnIndex("HoursWorked"));
        areaLvlOne = c2.getInt(c2.getColumnIndex("HoursWorked"));

        c2 = m.getHoursDet(a2, 1);
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


        Cursor col, univ, area1, area2;
        DatabaseAdapter m = new DatabaseAdapter(context);

        m.createDatabase();
        m.open();

        col = m.getHoursDet(c, 1);
        col.moveToFirst();

        univ = m.getHoursDet(u, 1);
        univ.moveToFirst();

        area1 = m.getHoursDet(a1, 1);
        area1.moveToFirst();

        area2 = m.getHoursDet(a2, 1);
        area2.moveToFirst();

        data.add(new AdapterDataWork(a1, area1.getString(area1.getColumnIndex("TotalHours")),
                area1.getString(area1.getColumnIndex("HoursWorked")), area1.getString(area1.getColumnIndex("RemainingHours"))));


        data.add(new AdapterDataWork(a2, area2.getString(area2.getColumnIndex("TotalHours")),
                area2.getString(area2.getColumnIndex("HoursWorked")), area2.getString(area2.getColumnIndex("RemainingHours"))));


        data.add(new AdapterDataWork(u, univ.getString(univ.getColumnIndex("TotalHours")),
                univ.getString(univ.getColumnIndex("HoursWorked")), univ.getString(univ.getColumnIndex("RemainingHours"))));


        data.add(new AdapterDataWork(c, col.getString(col.getColumnIndex("TotalHours")),
                col.getString(col.getColumnIndex("HoursWorked")), col.getString(col.getColumnIndex("RemainingHours"))));

        m.close();
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
}