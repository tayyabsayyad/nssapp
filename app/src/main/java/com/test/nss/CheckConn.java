package com.test.nss;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

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

    @Override
    public void onReceive(Context context, Intent intent) {

        NetworkInfo.State wifiState;
        NetworkInfo.State mobileState;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiState = Objects.requireNonNull(cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState();
        mobileState = Objects.requireNonNull(cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState();

        if (wifiState != null && NetworkInfo.State.CONNECTED != wifiState
                && NetworkInfo.State.CONNECTED == mobileState) {
            Log.e("CheckConn", "Internet");

            Log.e("TOK", ""+startActivity.AUTH_TOKEN);
            Call<ResponseBody> helpData = RetrofitClient.getInstance().getApi().getHelpData("Token " + startActivity.AUTH_TOKEN);
            helpData.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            JSONArray j = new JSONArray(response.body().string());
                            Log.e("startAct:" + "arrayLen", "" + j.length());
                                /*Log.e(TAG, j.getJSONObject(0).getString("Post"));
                                Log.e(TAG, j.getJSONObject(0).getString("Full_Name"));
                                Log.e(TAG, j.getJSONObject(0).getString("Contact"));*/
                            if (j.length() > 0) {
                                TestAdapter mDbHelper = new TestAdapter(context);
                                mDbHelper.createDatabase();
                                mDbHelper.open();

                                for (int i = 0; i < j.length(); i++) {
                                    mDbHelper.insertHelpData(
                                            j.getJSONObject(i).getString("Post"),
                                            j.getJSONObject(i).getString("Full_Name"),
                                            j.getJSONObject(i).getString("Post_Email"),
                                            j.getJSONObject(i).getString("Contact"));
                                }
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
                    Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("onFailure", "" + t.getMessage());
                }
            });
        } else if (wifiState != null && mobileState != null && NetworkInfo.State.CONNECTED != wifiState) {
            Log.e("CheckConn", "No net");
        }
    }
}