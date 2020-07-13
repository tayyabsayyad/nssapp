package com.test.nss;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;

import com.test.nss.api.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckConn extends BroadcastReceiver {

    Context context1;
    @Override
    public void onReceive(Context context, Intent intent) {
        context1 = context;
        NetworkInfo.State wifiState;
        NetworkInfo.State mobileState;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiState = Objects.requireNonNull(cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState();
        mobileState = Objects.requireNonNull(cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState();

        if (NetworkInfo.State.CONNECTED != wifiState && NetworkInfo.State.CONNECTED == mobileState) {
            Log.e("CheckConn", "Internet");
            //Log.e("TOK", ""+startActivity.AUTH_TOKEN);
            Call<ResponseBody> helpData = RetrofitClient.getInstance().getApi().getHelpData("Token " + startActivity.AUTH_TOKEN);
            helpData.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            JSONArray j = new JSONArray(response.body().string());
                                    /*Log.e(TAG, j.getJSONObject(0).getString("Post"));
                                    Log.e(TAG, j.getJSONObject(0).getString("Full_Name"));
                                    Log.e(TAG, j.getJSONObject(0).getString("Contact"));*/
                            if (j.length() > 0) {
                                TestAdapter mDbHelper = new TestAdapter(context);
                                mDbHelper.createDatabase();
                                mDbHelper.open();
                                deleteData("Help");
                                for (int i = 0; i < j.length(); i++) {
                                    mDbHelper.insertHelpData(
                                            j.getJSONObject(i).getString("Post"),
                                            j.getJSONObject(i).getString("Full_Name"),
                                            j.getJSONObject(i).getString("Post_Email"),
                                            j.getJSONObject(i).getString("Contact"));
                                }
                                mDbHelper.close();
                                mDbHelper.close();
                            }
                        } catch (JSONException | IOException e) {
                            Log.e("Failed", e.toString());
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Log.e("onFailure", t.toString());
                }
            });
        } else if (wifiState != null && mobileState != null && NetworkInfo.State.CONNECTED != wifiState) {
            Log.e("CheckConn", "No net");
        }

        Call<ResponseBody> campDetails = RetrofitClient.getInstance().getApi().getCampDetails("Token " + startActivity.AUTH_TOKEN);
        campDetails.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONArray j = new JSONArray(response.body().string());
                        //Log.e("insert", j.getJSONObject(0).getString("CollegeName"));

                        if (j.length() > 0) {
                            TestAdapter mDbHelper = new TestAdapter(context);
                            mDbHelper.createDatabase();
                            mDbHelper.open();
                            deleteData("CampDetails");
                            for (int i = 0; i < j.length(); i++) {
                                mDbHelper.insertCampDetails(
                                        j.getJSONObject(i).getString("CollegeName"),
                                        j.getJSONObject(i).getString("CampFrom"),
                                        j.getJSONObject(i).getString("CampTo"),
                                        j.getJSONObject(i).getString("CampVenue"),
                                        j.getJSONObject(i).getString("CampPost"),
                                        j.getJSONObject(i).getString("CampTaluka"),
                                        j.getJSONObject(i).getString("CampDistrict")
                                );
                            }
                            mDbHelper.getCampDetails();
                            mDbHelper.close();
                        }
                    } catch (JSONException | IOException e) {
                        Log.e("Failed", e.toString());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("onFailure", t.toString());
            }
        });
    }

    public void deleteData(String table) {
        TestAdapter mDbHelper2 = new TestAdapter(context1);
        mDbHelper2.createDatabase();
        mDbHelper2.open();
        DataBaseHelper mDb2 = new DataBaseHelper(context1);
        SQLiteDatabase m = mDb2.getWritableDatabase();
        m.execSQL("DELETE FROM " + table);
        mDbHelper2.close();
        mDb2.close();
    }
}