package com.test.nss;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.test.nss.api.RetrofitClient;
import com.test.nss.ui.info.InfoSharedActivity;
import com.test.nss.ui.work.AdapterDataWork;
import com.test.nss.ui.work.WorkDetailsFirstFrag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class ediary extends AppCompatActivity {

    public static int primaryColDark;
    public static int blackish;
    public static int transparent;
    public static int primaryCol;
    public static int primaryColLight;
    public static int red;
    public static int green;
    public static int kesar;
    public static int sbColorText;
    public static int black;
    public static int white;
    public static int blackGrey;

    public static String AUTH_TOKEN;
    public static String VEC;
    public static int isLeader;
    public static int leaderId;
    public static String name;
    public static boolean isFirst;
    public static boolean isSec;
    public static int isNight = 0;
    static int whichAvatar = 0;

    List<AdapterDataWork> dataWorkList;
    AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    ImageView logout;
    FragmentManager fm;
    CheckConn checkConn;
    IntentFilter z;
    ImageView imageView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ediary);
        context = ediary.this;
        imageView = findViewById(R.id.switchdark);

        isNight = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        Log.e("is night", "" + isNight);

        if (isNight == Configuration.UI_MODE_NIGHT_NO) {
            imageView.setImageResource(R.drawable.ic_light);
        } else {
            imageView.setImageResource(R.drawable.ic_dark);
        }

        SharedPreferences sharedPreferences = getSharedPreferences("KEY", MODE_PRIVATE);

        AUTH_TOKEN = sharedPreferences.getString("AUTH_TOKEN", "");
        VEC = sharedPreferences.getString("VEC", "");

        Call<ResponseBody> call2 = RetrofitClient.getInstance().getApi().isLeader("Token " + AUTH_TOKEN);
        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                SharedPreferences shareit = getSharedPreferences("KEY", MODE_PRIVATE);
                SharedPreferences.Editor eddy = shareit.edit();
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONArray j2 = new JSONArray(response.body().string());
                        if (j2.length() > 0) {
                            eddy.putInt("isLeader", 1);
                            eddy.putInt("leaderId", j2.getJSONObject(0).getInt("id"));
                            isLeader = 1;
                            leaderId = j2.getJSONObject(0).getInt("id");
                        } else {
                            eddy.putInt("isLeader", 0);
                            isLeader = 0;
                        }
                        Log.e("HH", "onResponse: " + isLeader);
                        eddy.apply();

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.errorBody() != null) {
                    try {
                        Log.e("Error", "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        Log.e("AA", "" + isLeader + name);
        //TODO:
        //if (isNetworkAvailable()) {
        //} else {
        isLeader = sharedPreferences.getInt("isLeader", 0);
        leaderId = sharedPreferences.getInt("leaderId", 0);
        //}

        fm = getSupportFragmentManager();

        DatabaseAdapter mdb = new DatabaseAdapter(context);
        mdb.createDatabase();
        mdb.open();
        Cursor c = mdb.getRegDetails(VEC);
        c.moveToFirst();
        name = c.getString(c.getColumnIndex("First_name")) + " " + c.getString(c.getColumnIndex("Last_name"));
        isFirst = c.getString(c.getColumnIndex("State")).equals("First Year");
        isSec = c.getString(c.getColumnIndex("State")).equals("Second Year");
        mdb.close();

        View v = findViewById(android.R.id.content);
        Snackbar s;
        if (isLeader == 1) {
            s = Snackbar.make(v, "Welcome Leader: " + name, Snackbar.LENGTH_SHORT);
            TextView tv = s.getView().findViewById(com.google.android.material.R.id.snackbar_text);
            tv.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_leader_24, 0, 0, 0);
            Typeface t = ResourcesCompat.getFont(context, R.font.nunito_semibold);
            tv.setTypeface(t);
            tv.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.snackbar_icon_padding));

        } else {
            s = Snackbar.make(v, "Welcome: " + name, Snackbar.LENGTH_SHORT);
            TextView tv = s.getView().findViewById(com.google.android.material.R.id.snackbar_text);
            Typeface t = ResourcesCompat.getFont(context, R.font.nunito_semibold);
            tv.setTypeface(t);
        }

        s.setTextColor(context.getColor(R.color.white));
        s.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        s.show();
        blackish = context.getColor(R.color.blackish);
        transparent = context.getColor(R.color.transparent);
        primaryCol = context.getColor(R.color.colorPrimary);
        primaryColLight = context.getColor(R.color.colorPrimaryLight);
        primaryColDark = context.getColor(R.color.colorPrimaryDark);
        red = context.getColor(R.color.red);
        green = context.getColor(R.color.greenNic);
        kesar = context.getColor(R.color.kesar);
        sbColorText = context.getColor(R.color.sbColorText);
        black = context.getColor(R.color.black);
        white = context.getColor(R.color.white);
        blackGrey = context.getColor(R.color.blackGrey);

        //Toast.makeText(context, AUTH_TOKEN, Toast.LENGTH_SHORT).show();
        drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        logout = findViewById(R.id.logoutbutton);

        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_main,
                R.id.nav_acti, R.id.nav_work, R.id.nav_camp, R.id.nav_leader, R.id.nav_help)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setItemIconTintList(null);

        View headerView = navigationView.getHeaderView(0);
        imageView = headerView.findViewById(R.id.imageView);

        whichAvatar = sharedPreferences.getInt("avatar", 0);
        switch (whichAvatar) {
            case 1:
                imageView.setImageResource(R.drawable.ic_women_1);
                break;
            case 2:
                imageView.setImageResource(R.drawable.ic_women_2);
                break;
            case 3:
                imageView.setImageResource(R.drawable.ic_women_3);
                break;
            case 4:
                imageView.setImageResource(R.drawable.ic_women_4);
                break;
            case 5:
                imageView.setImageResource(R.drawable.ic_man_1);
                break;
            case 6:
                imageView.setImageResource(R.drawable.ic_man_2);
                break;
            case 7:
                imageView.setImageResource(R.drawable.ic_man_3);
                break;
            case 8:
                imageView.setImageResource(R.drawable.ic_man_4);
                break;
            default:
            case 0:
                imageView.setImageResource(R.drawable.ic_man_0);
                break;
        }

        Menu m = navigationView.getMenu().getItem(0).getSubMenu();
        if (isLeader == 1)
            m.getItem(4).setVisible(true);

        else if (isLeader == 0)
            m.getItem(4).setVisible(false);

        m = navigationView.getMenu();
        m.getItem(1).getSubMenu().getItem(1).setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getTitle().toString().equals(getString(R.string.info))) {
                Intent o = new Intent(context, InfoSharedActivity.class);

                /*View v1 = findViewById(R.id.imageView);
                Pair[] pair = new Pair[1];
                pair[0] = new Pair<>(v1, "trans");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ediary.context, pair);*/


                //startActivity(o, options.toBundle());
                startActivity(o);
            }
            return true;
        });

        TextView navUsername = headerView.findViewById(R.id.nameHeader);
        TextView navVec = headerView.findViewById(R.id.vecNoHeader);
        navVec.setText(VEC);
        navUsername.setText(name);

        imageView.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.delDialog);
            builder.setMessage("Choose Avatar");
            builder.setView(R.layout.avatar_input);

            builder.setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });
            builder.show();
        });

        logout.setOnClickListener(view -> {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(context, R.style.delDialog);
            builder2.setMessage("Do you want to logout?");

            builder2.setNegativeButton("No", (dialog, which) -> {
                dialog.dismiss();
            });

            builder2.setPositiveButton("Yes", (dialog, which) -> {
                dialog.dismiss();

                SharedPreferences shareit = getSharedPreferences("KEY", MODE_PRIVATE);
                SharedPreferences.Editor eddy = shareit.edit();
                eddy.putInt("logged", 0);
                eddy.putInt("isLeader", 0);
                eddy.putInt("leaderId", 0);
                eddy.apply();

                Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show();
                fm.popBackStackImmediate();

                Call<Void> helpData = RetrofitClient.getInstance().getApi().delToken("Token " + ediary.AUTH_TOKEN);
                helpData.enqueue(new Callback<Void>() {
                    @Override
                    @EverythingIsNonNull
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                        }
                    }

                    @Override
                    @EverythingIsNonNull
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("Ediary:logout", t.toString());
                    }
                });
                Intent i = new Intent(context, startActivity.class);
                startActivity(i);
                finish();
                Toast.makeText(context, "Logged Out!", Toast.LENGTH_SHORT).show();
                AUTH_TOKEN = "";
            });

            builder2.show();
        });

        Log.e("Here", "onCreate: " + isFirst + isLeader);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void add() {
        WorkDetailsFirstFrag wf = new WorkDetailsFirstFrag(context);
        dataWorkList = wf.firstHalfWorkData();

        if (!dataWorkList.isEmpty()) {

            Call<ResponseBody> insertHourZero = RetrofitClient.getInstance().getApi().insertHour(
                    "Token " + AUTH_TOKEN,
                    Integer.parseInt(dataWorkList.get(0).getCompHours()),
                    Integer.parseInt(dataWorkList.get(0).getRemHours()),
                    ediary.VEC,
                    121,
                    "Area Based Level One",
                    Password.PASS,
                    30
            );
            insertHourZero.enqueue(new Callback<ResponseBody>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.errorBody() != null) {
                        try {
                            JSONObject j = new JSONObject(response.errorBody().string());
                            Log.e("Error here", j.toString());
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            Log.e("error", e.toString());
                        }
                    } else if (response.isSuccessful())
                        Log.i("Done ", "added hours to web");
                }

                @Override
                @EverythingIsNonNull
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });

            Call<ResponseBody> insertHourOne = RetrofitClient.getInstance().getApi().insertHour(
                    "Token " + ediary.AUTH_TOKEN,
                    Integer.parseInt(dataWorkList.get(1).getCompHours()),
                    Integer.parseInt(dataWorkList.get(1).getRemHours()),
                    ediary.VEC,
                    122,
                    "Area Based Level Two",
                    Password.PASS,
                    31
            );
            insertHourOne.enqueue(new Callback<ResponseBody>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                }

                @Override
                @EverythingIsNonNull
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

            Call<ResponseBody> insertHourTwo = RetrofitClient.getInstance().getApi().insertHour(
                    "Token " + ediary.AUTH_TOKEN,
                    Integer.parseInt(dataWorkList.get(2).getCompHours()),
                    Integer.parseInt(dataWorkList.get(2).getRemHours()),
                    ediary.VEC,
                    11,
                    "College Level",
                    Password.PASS,
                    32
            );
            insertHourTwo.enqueue(new Callback<ResponseBody>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                }

                @Override
                @EverythingIsNonNull
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

            Call<ResponseBody> insertHourThr = RetrofitClient.getInstance().getApi().insertHour(
                    "Token " + ediary.AUTH_TOKEN,
                    Integer.parseInt(dataWorkList.get(3).getCompHours()),
                    Integer.parseInt(dataWorkList.get(3).getRemHours()),
                    ediary.VEC,
                    13,
                    "University Level",
                    Password.PASS,
                    29
            );

            insertHourThr.enqueue(new Callback<ResponseBody>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                }

                @Override
                @EverythingIsNonNull
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    public void setWom1(View view) {
        imageView.setImageResource(R.drawable.ic_women_1);
        whichAvatar = 1;
        SharedPreferences sharedPreferences = getSharedPreferences("KEY", MODE_PRIVATE);

        SharedPreferences.Editor eddy2 = sharedPreferences.edit();
        eddy2.putInt("avatar", whichAvatar);
        eddy2.apply();
    }

    public void setWom2(View view) {
        imageView.setImageResource(R.drawable.ic_women_2);
        whichAvatar = 2;
        SharedPreferences sharedPreferences = getSharedPreferences("KEY", MODE_PRIVATE);

        SharedPreferences.Editor eddy2 = sharedPreferences.edit();
        eddy2.putInt("avatar", whichAvatar);
        eddy2.apply();
    }

    public void setWom3(View view) {
        imageView.setImageResource(R.drawable.ic_women_3);
        whichAvatar = 3;
        SharedPreferences sharedPreferences = getSharedPreferences("KEY", MODE_PRIVATE);

        SharedPreferences.Editor eddy2 = sharedPreferences.edit();
        eddy2.putInt("avatar", whichAvatar);
        eddy2.apply();
    }

    public void setWom4(View view) {
        imageView.setImageResource(R.drawable.ic_women_4);
        whichAvatar = 4;
        SharedPreferences sharedPreferences = getSharedPreferences("KEY", MODE_PRIVATE);

        SharedPreferences.Editor eddy2 = sharedPreferences.edit();
        eddy2.putInt("avatar", whichAvatar);
        eddy2.apply();
    }


    public void setMan1(View view) {
        imageView.setImageResource(R.drawable.ic_man_1);
        whichAvatar = 5;
        SharedPreferences sharedPreferences = getSharedPreferences("KEY", MODE_PRIVATE);

        SharedPreferences.Editor eddy2 = sharedPreferences.edit();
        eddy2.putInt("avatar", whichAvatar);
        eddy2.apply();
    }

    public void setMan2(View view) {
        imageView.setImageResource(R.drawable.ic_man_2);
        whichAvatar = 6;
        SharedPreferences sharedPreferences = getSharedPreferences("KEY", MODE_PRIVATE);

        SharedPreferences.Editor eddy2 = sharedPreferences.edit();
        eddy2.putInt("avatar", whichAvatar);
        eddy2.apply();
    }

    public void setMan3(View view) {
        imageView.setImageResource(R.drawable.ic_man_3);
        whichAvatar = 7;
        SharedPreferences sharedPreferences = getSharedPreferences("KEY", MODE_PRIVATE);

        SharedPreferences.Editor eddy2 = sharedPreferences.edit();
        eddy2.putInt("avatar", whichAvatar);
        eddy2.apply();
    }

    public void setMan4(View view) {
        imageView.setImageResource(R.drawable.ic_man_4);
        whichAvatar = 8;
        SharedPreferences sharedPreferences = getSharedPreferences("KEY", MODE_PRIVATE);

        SharedPreferences.Editor eddy2 = sharedPreferences.edit();
        eddy2.putInt("avatar", whichAvatar);
        eddy2.apply();
    }

    public void setMode(View view) {
        ImageView imageView = findViewById(R.id.switchdark);

        RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setInterpolator(new MyBounceInterpolator(2, 8));
        animation.setDuration(1500);
        imageView.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isNight == Configuration.UI_MODE_NIGHT_NO) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkConn = new CheckConn();
        z = new IntentFilter();
        z.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(checkConn, z);
    }

    @Override
    protected void onStop() {
        add();
        unregisterReceiver(checkConn);
        super.onStop();
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        add();
        //unregisterReceiver(checkConn);
    }*/

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}