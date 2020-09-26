package com.test.nss.worker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.test.nss.DataBaseHelper;
import com.test.nss.DatabaseAdapter;
import com.test.nss.R;
import com.test.nss.api.RetrofitClient;
import com.test.nss.ediary;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.test.nss.Helper.AUTH_TOKEN;

public class MyWorker extends Worker {
    private static final String uniqueWorkName = "com.test.nss.worker";
    private static final long repeatIntervalMin = 20;
    //private static final long flexIntervalMin = 5;
    private static final int NOTIFY_ID = 1;
    private static final String id = "101";

    private NotificationManager notificationManager;
    private Context context;

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    public static void enqueueSelf() {
        WorkManager.getInstance().enqueueUniquePeriodicWork(uniqueWorkName, ExistingPeriodicWorkPolicy.KEEP, getOwnWorkRequest());
    }

    private static PeriodicWorkRequest getOwnWorkRequest() {
        Log.e("MyWorker", "working");
        return new PeriodicWorkRequest.Builder(
                MyWorker.class, repeatIntervalMin, TimeUnit.MINUTES)
                .build();
    }

    @NonNull
    public Result doWork() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        DatabaseAdapter mdb = new DatabaseAdapter(context);
        mdb.createDatabase();
        mdb.open();
        Call<ResponseBody> insertAct = RetrofitClient.getInstance().getApi().getDailyAct("Token " + AUTH_TOKEN);
        Response<ResponseBody> responseAct = null;

        try {
            responseAct = insertAct.execute();
            if (responseAct.isSuccessful() && responseAct.body() != null) {
                try {
                    JSONArray j = new JSONArray(responseAct.body().string());

                    if (j.length() > 0) {
                        deleteData("DailyActivityTemp");
                        for (int i = 0; i < j.length(); i++) {
                            mdb.insertActAgain(
                                    j.getJSONObject(i).getString("id"),
                                    j.getJSONObject(i).getString("AssignedActivityName"),
                                    j.getJSONObject(i).getString("State")
                            );
                        }
                    }
                } catch (JSONException | IOException e) {
                    Log.e("Failed", e.toString());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mdb.close();

        if (insertAct.isExecuted()) {
            mdb.createDatabase();
            mdb.open();
            Cursor c = mdb.getUpdatedAct();

            if (c.getCount() > 0) {
                if (c.getCount() < 2) {
                    String s;
                    Cursor m = mdb.getActLeaderId(c.getInt(c.getColumnIndex("actID")));
                    int abbBy = m.getInt(m.getColumnIndex("Approved_by"));
                    if (!String.valueOf(abbBy).equals("null")) {
                        s = mdb.getLeaderName(abbBy);
                    } else
                        s = "PO";

                    NotificationCompat.Builder builder2 = getBuilder(id, context.getString(R.string.app_name), c.getString(c.getColumnIndex("State")) + ": " + c.getString(c.getColumnIndex("ActivityName")) + "By " + s);
                    Notification notification2 = builder2.build();
                    notificationManager.notify(NOTIFY_ID, notification2);
                } else {
                    NotificationCompat.Builder builder2 = getBuilder(id, context.getString(R.string.app_name), "Approved: " + c.getCount() + "activities");
                    Notification notification2 = builder2.build();
                    notificationManager.notify(NOTIFY_ID, notification2);
                }
            }
            mdb.close();
        }

        Call<ResponseBody> helpData = RetrofitClient.getInstance().getApi().getPoData("Token " + AUTH_TOKEN);
        Response<ResponseBody> responseHelp = null;

        try {
            responseHelp = helpData.execute();
            if (responseHelp.isSuccessful() && responseHelp.body() != null) {
                try {
                    JSONArray j = new JSONArray(responseHelp.body().string());

                    if (j.length() > 0) {
                        DatabaseAdapter mDbHelper = new DatabaseAdapter(context);
                        mDbHelper.createDatabase();
                        mDbHelper.open();
                        deleteData("Help");
                        for (int i = 0; i < j.length(); i++) {
                            mDbHelper.insertHelpData(
                                    j.getJSONObject(i).getString("CollegeName"),
                                    "Po",
                                    j.getJSONObject(i).getString("PoName"),
                                    j.getJSONObject(i).getString("PoEmail"),
                                    j.getJSONObject(i).getString("PoContact"),
                                    j.getJSONObject(i).getString("PoStartYear"));
                        }
                        mDbHelper.close();
                    }
                } catch (JSONException | IOException e) {
                    Log.e("Failed", e.toString());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mdb.close();
        return responseAct != null && responseHelp != null && responseHelp.isSuccessful() && responseAct.isSuccessful() ? Result.success() : Result.retry();
    }

    @NonNull
    public NotificationCompat.Builder getBuilder(String id, String title, String msg) {
        NotificationCompat.Builder builder;
        Intent intent;
        PendingIntent pendingIntent;

        builder = new NotificationCompat.Builder(context, id);
        intent = new Intent(context, ediary.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = notificationManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                notificationManager.createNotificationChannel(mChannel);
            }
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(context.getString(R.string.congrats))                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setContentText(msg) // required
                    .setAutoCancel(true)
                    .setChannelId(id)
                    .setContentIntent(pendingIntent);
        } else {
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(context.getString(R.string.congrats))                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setContentText(msg) // required
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_DEFAULT);
        }
        return builder;
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
