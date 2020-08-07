package com.test.nss;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.test.nss.api.RetrofitClient;
import com.test.nss.ui.work.AdapterDataWork;
import com.test.nss.ui.work.WorkDetailsFirstFrag;

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
    public static String AUTH_TOKEN;
    public static String VEC;

    public static int isLeader;
    public static int leaderId;
    public static String name;

    List<AdapterDataWork> dataWorkList;
    AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    ImageView logout;

    FragmentManager fm;
    CheckConn checkConn;
    IntentFilter z;

    public static boolean isFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ediary);

        SharedPreferences sharedPreferences = getSharedPreferences("KEY", MODE_PRIVATE);

        AUTH_TOKEN = sharedPreferences.getString("AUTH_TOKEN", "");
        VEC = sharedPreferences.getString("VEC", "");

        isLeader = sharedPreferences.getInt("isLeader", 0);
        leaderId = sharedPreferences.getInt("leaderId", 0);
        name = sharedPreferences.getString("name", "");

        fm = getSupportFragmentManager();

        blackish = this.getColor(R.color.blackish);
        transparent = this.getColor(R.color.transparent);
        primaryCol = this.getColor(R.color.colorPrimary);
        primaryColLight = this.getColor(R.color.colorPrimaryLight);
        primaryColDark = this.getColor(R.color.colorPrimaryDark);
        red = this.getColor(R.color.red);
        green = this.getColor(R.color.greenNic);
        kesar = this.getColor(R.color.kesar);

        //Toast.makeText(this, AUTH_TOKEN, Toast.LENGTH_SHORT).show();
        drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        logout = findViewById(R.id.logoutbutton);

        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_main,
                R.id.nav_acti, R.id.nav_work, R.id.nav_camp, R.id.nav_leader, R.id.nav_help)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);
        Menu m = navigationView.getMenu();
        if (isLeader == 1)
            m.getItem(4).setVisible(true);

        else if (isLeader == 0)
            m.getItem(4).setVisible(false);

        TextView navUsername = headerView.findViewById(R.id.nameHeader);
        TextView navVec = headerView.findViewById(R.id.vecNoHeader);
        navVec.setText(VEC);

        DatabaseAdapter mdb = new DatabaseAdapter(ediary.this);
        mdb.createDatabase();
        mdb.open();
        Cursor c = mdb.getRegDetails(VEC);
        c.moveToFirst();
        navUsername.setText(String.format("%s %s", c.getString(c.getColumnIndex("First_name")), c.getString(c.getColumnIndex("Last_name"))));
        //Log.e("Here", "onCreate: "+c.getString(c.getColumnIndex("State")));
        isFirst = c.getString(c.getColumnIndex("State")).equals("First Year");
        mdb.close();

        logout.setOnClickListener(view -> {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(ediary.this, R.style.delDialog);
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

                Toast.makeText(ediary.this, "Logged Out", Toast.LENGTH_SHORT).show();
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
                Intent i = new Intent(ediary.this, startActivity.class);
                startActivity(i);
                finish();
                Toast.makeText(ediary.this, "Logged Out!", Toast.LENGTH_SHORT).show();
                AUTH_TOKEN = "";
            });

            builder2.show();
        });

        Log.e("Here", "onCreate: "+isFirst);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void add() {
        WorkDetailsFirstFrag wf = new WorkDetailsFirstFrag(ediary.this);
        dataWorkList = wf.firstHalfWorkData();
        Call<ResponseBody> insertHourZero = RetrofitClient.getInstance().getApi().insertHour(
                "Token " + AUTH_TOKEN,
                Integer.parseInt(dataWorkList.get(0).getCompHours()),
                Integer.parseInt(dataWorkList.get(0).getRemHours()),
                ediary.VEC,
                121,
                "Area Based Level One",
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
                }
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

    @Override
    protected void onResume() {
        super.onResume();
        checkConn = new CheckConn();
        z = new IntentFilter();
        z.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(checkConn, z);
    }

    @Override
    protected void onStop() {
        super.onStop();
        add();
        unregisterReceiver(checkConn);
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