package com.test.nss;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.test.nss.api.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class CheckConn extends BroadcastReceiver {

    Context context1;
    static boolean isSyncAct = false;
    static boolean isSyncCamp = false;

    public static <V> Integer getKey(Map<Integer, String> map, String value) {
        for (Integer key : map.keySet()) {
            if (value.equals(map.get(key))) {
                return key;
            }
        }
        return null;
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

    @Override
    public void onReceive(Context context, Intent intent) {
        context1 = context;
        NetworkInfo.State wifiState;
        NetworkInfo.State mobileState;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiState = Objects.requireNonNull(cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState();
        mobileState = Objects.requireNonNull(cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState();

        boolean a = NetworkInfo.State.CONNECTED == wifiState;
        boolean b = NetworkInfo.State.CONNECTED == mobileState;

        if (a || b) {
            Log.e("CheckConn", "Internet");
            Call<ResponseBody> helpData = RetrofitClient.getInstance().getApi().getPoData("Token " + startActivity.AUTH_TOKEN);
            helpData.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            JSONArray j = new JSONArray(response.body().string());

                            if (j.length() >= 0) {
                                TestAdapter mDbHelper = new TestAdapter(context);
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
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Log.e("onFailure", t.toString());
                }
            });
            //NetworkInfo.State.CONNECTED != wifiState

            Call<ResponseBody> campList = RetrofitClient.getInstance().getApi().getCampList("Token " + startActivity.AUTH_TOKEN);
            campList.enqueue(new Callback<ResponseBody>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            JSONArray j = new JSONArray(response.body().string());

                            if (j.length() >= 0) {
                                TestAdapter mDbHelper = new TestAdapter(context);
                                mDbHelper.createDatabase();
                                mDbHelper.open();
                                deleteData("CampActivityList");
                                for (int i = 0; i < j.length(); i++) {
                                    mDbHelper.insertCampActList(
                                            j.getJSONObject(i).getString("CampActivityName"),
                                            j.getJSONObject(i).getString("id")
                                    );
                                }
                                mDbHelper.close();
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                @EverythingIsNonNull
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("Failed:campList", t.toString());
                }
            });

            Call<ResponseBody> clg = RetrofitClient.getInstance().getApi().getClgList();
            clg.enqueue(new Callback<ResponseBody>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            //JSONObject j = new JSONObject(response.body().string());
                            JSONArray j = new JSONArray(response.body().string());
                            if (j.length() >= 0) {
                                TestAdapter mDbHelper = new TestAdapter(context);
                                mDbHelper.createDatabase();
                                mDbHelper.open();
                                deleteData("CollegeNames");
                                for (int i = 0; i < j.length(); i++) {
                                    mDbHelper.insertClgList(
                                            j.getJSONObject(i).getString("CollegeCode"),
                                            j.getJSONObject(i).getString("CollegeName")
                                    );
                                }
                                //mDbHelper.getCampDetails();
                                mDbHelper.close();
                            }
                            //ArrayAdapter<String> adapter = new ArrayAdapter<>(SignupActivity.this, R.layout.drop_down_start, clgList);
                            //dropdownClg.setAdapter(adapter);
                            Log.e("Added college", "");
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                @EverythingIsNonNull
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });


            Call<ResponseBody> campDetails = RetrofitClient.getInstance().getApi().getCampDetails("Token " + startActivity.AUTH_TOKEN);
            campDetails.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            JSONArray j = new JSONArray(response.body().string());
                            //Log.e("insert", j.getJSONObject(0).getString("CollegeName"));

                            if (j.length() >= 0) {
                                TestAdapter mDbHelper = new TestAdapter(context);
                                mDbHelper.createDatabase();
                                mDbHelper.open();
                                deleteData("CampDetails");// Scan for sync state
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
                                //mDbHelper.getCampDetails();
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
                    Log.e("onFailure:campDetails", t.toString());
                }
            });

            TestAdapter mDbHelper = new TestAdapter(context);
            mDbHelper.createDatabase();
            mDbHelper.open();

            //Cursor c2 = mDbHelper.getActId("");
            //c.moveToFirst();
            Map<Integer, String> actIdHash = new HashMap<>();
            actIdHash.put(11, "First Year University");
            actIdHash.put(121, "First Year Area Based One");
            actIdHash.put(122, "First Year Area Based Two");
            actIdHash.put(13, "First Year College");
            actIdHash.put(21, "Second Year University");
            actIdHash.put(221, "Second Year Area Based One");
            actIdHash.put(222, "Second Year Area Based Two");
            actIdHash.put(23, "Second Year College");

            Cursor c = mDbHelper.getActListOff();
            //Log.e("AAAA", ""+c.getCount());
            //c.moveToFirst();
            if (c.getCount() > 0) {
                Log.wtf("AAAAA", "Entering");

                c.moveToFirst();

                int count = c.getCount();
                while (count>0) {
                    Cursor c2 = mDbHelper.getActAssigActNameAdmin(c.getString(c.getColumnIndex("ActivityName")));
                    c2.moveToFirst();

                    Call<ResponseBody> insertActOff = RetrofitClient.getInstance().getApi().sendActList(
                            "Token " + startActivity.AUTH_TOKEN,
                            c.getString(c.getColumnIndex("VEC")),
                            c2.getInt(c2.getColumnIndex("id")),
                            //c.getInt(c.getColumnIndex("ActivityCode")),
                            c.getInt(c.getColumnIndex("HoursWorked")),
                            c.getString(c.getColumnIndex("Date")),
                            getKey(actIdHash, c.getString(c.getColumnIndex("ActivityCode"))),
                            1
                    );

                    insertActOff.enqueue(new Callback<ResponseBody>() {
                        @Override
                        @EverythingIsNonNull
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                isSyncAct = true;
                            } else if (response.errorBody() != null) {
                                try {
                                    Log.e("Error", response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Log.e("error", e.toString());
                                }
                                isSyncAct = false;
                            } else {
                                isSyncAct = false;
                            }
                        }

                        @Override
                        @EverythingIsNonNull
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("ERROR", t.toString());
                        }
                    });
                c.moveToNext();
                //c2.moveToNext();*/
                    count = count - 1;
                }
            }
            if (isSyncAct) {
                mDbHelper.setSync("DailyActivity", 1);
                Toast.makeText(context, "Database Synced", Toast.LENGTH_SHORT).show();
            }
            mDbHelper.close();

            Call<ResponseBody> insertAct = RetrofitClient.getInstance().getApi().getDailyAct("Token " + startActivity.AUTH_TOKEN);
            insertAct.enqueue(new Callback<ResponseBody>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            JSONArray j = new JSONArray(response.body().string());

                            if (j.length() >= 0) {
                                TestAdapter mDbHelper = new TestAdapter(context);
                                mDbHelper.createDatabase();
                                mDbHelper.open();
                                deleteData("DailyActivity");
                                for (int i = 0; i < j.length(); i++) {
                                    mDbHelper.insertAct(
                                            j.getJSONObject(i).getString("VEC"),
                                            j.getJSONObject(i).getString("ActivityName"),
                                            j.getJSONObject(i).getString("id"),
                                            j.getJSONObject(i).getString("Date"),
                                            j.getJSONObject(i).getString("AssignedActivityName"),
                                            j.getJSONObject(i).getString("Hours"),
                                            j.getJSONObject(i).getString("State"),
                                            1
                                    );
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
                @EverythingIsNonNull
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

            mDbHelper.createDatabase();
            mDbHelper.open();
            Cursor c2 = mDbHelper.getCampActListOff(0);
            c2.moveToFirst();

            if (c2.getCount() > 0) {

                int cu = c2.getCount();
                c2.moveToFirst();

                while (cu > 0) {
                    Cursor c3 = mDbHelper.getCampId(c2.getString(c2.getColumnIndex("CampActivityTitle")));
                    c3.moveToFirst();
                    Call<ResponseBody> sendCampDetails = RetrofitClient.getInstance().getApi().sendCampDetail(
                            "Token " + startActivity.AUTH_TOKEN,
                            "DBIT",
                            c2.getString(c2.getColumnIndex("CampActivityDescription")),
                            c2.getInt(c2.getColumnIndex("CampDay")),
                            startActivity.VEC,
                            "1",
                            c3.getString(c3.getColumnIndex("CampId"))
                    );
                    sendCampDetails.enqueue(new Callback<ResponseBody>() {
                        @Override
                        @EverythingIsNonNull
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.wtf("Easy", "done");
                            if (response.isSuccessful() && response.body() != null) {
                                Log.e("Success", "Ye");
                                isSyncCamp = true;
                            } else if (response.errorBody() != null) {
                                try {
                                    Log.e("error:", response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                isSyncCamp = false;
                            } else
                                isSyncCamp = false;
                        }

                        @Override
                        @EverythingIsNonNull
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                    c2.moveToNext();
                    cu = cu - 1;
                }
            }
            if (isSyncCamp)
                mDbHelper.setSync("CampActivities", 1);
            mDbHelper.close();

            Call<ResponseBody> campListAll = RetrofitClient.getInstance().getApi().getCampActListAll("Token " + startActivity.AUTH_TOKEN);
            campListAll.enqueue(new Callback<ResponseBody>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            JSONArray j = new JSONArray(response.body().string());

                            if (j.length() >= 0) {
                                TestAdapter mDbHelper = new TestAdapter(context);
                                mDbHelper.createDatabase();
                                mDbHelper.open();
                                deleteData("CampActivities");
                                for (int i = 0; i < j.length(); i++) {
                                    mDbHelper.insertCampActListAll(
                                            j.getJSONObject(i).getInt("id"),
                                            j.getJSONObject(i).getString("CollegeName"),
                                            j.getJSONObject(i).getString("State"),
                                            j.getJSONObject(i).getString("CampActivityTitle"),
                                            j.getJSONObject(i).getString("CampActivityDescription"),
                                            j.getJSONObject(i).getString("Day"),
                                            startActivity.VEC,
                                            1
                                    );
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
                @EverythingIsNonNull
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("onFailure:campListAll", t.toString());
                }
            });

            Call<ResponseBody> actList = RetrofitClient.getInstance().getApi().getActList("Token " + startActivity.AUTH_TOKEN);
            actList.enqueue(new Callback<ResponseBody>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            JSONArray j = new JSONArray(response.body().string());

                            if (j.length() >= 0) {
                                TestAdapter mDbHelper = new TestAdapter(context);
                                mDbHelper.createDatabase();
                                mDbHelper.open();
                                deleteData("ActivityListByAdmin");
                                for (int i = 0; i < j.length(); i++) {
                                    mDbHelper.insertActAdmin(
                                            j.getJSONObject(i).getInt("id"),
                                            j.getJSONObject(i).getString("CollegeName"),
                                            j.getJSONObject(i).getString("ActivityName"),
                                            j.getJSONObject(i).getString("AssignedActivityName"),
                                            j.getJSONObject(i).getString("AssignedHours"),
                                            j.getJSONObject(i).getString("AssignedDate")
                                    );
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
                @EverythingIsNonNull
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("onFailure:campListAll", t.toString());
                }
            });

            Call<ResponseBody> insertUsers = RetrofitClient.getInstance().getApi().insertUsers("Token " + startActivity.AUTH_TOKEN);
            insertUsers.enqueue(new Callback<ResponseBody>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            JSONArray j = new JSONArray(response.body().string());

                            if (j.length() >= 0) {
                                TestAdapter mDbHelper = new TestAdapter(context);
                                mDbHelper.createDatabase();
                                mDbHelper.open();
                                deleteData("Registration");
                                for (int i = 0; i < j.length(); i++) {
                                    mDbHelper.insertUsers(
                                            j.getJSONObject(i).getString("CollegeName"),
                                            j.getJSONObject(i).getString("VEC"),
                                            j.getJSONObject(i).getString("FirstName"),
                                            j.getJSONObject(i).getString("LastName"),
                                            j.getJSONObject(i).getString("Email"),
                                            j.getJSONObject(i).getString("Contact")
                                    );
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
                @EverythingIsNonNull
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

            Call<ResponseBody> insertLeaders = RetrofitClient.getInstance().getApi().insertLeaders("Token " + startActivity.AUTH_TOKEN);
            insertLeaders.enqueue(new Callback<ResponseBody>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            JSONArray j = new JSONArray(response.body().string());

                            if (j.length() >= 0) {
                                TestAdapter mDbHelper = new TestAdapter(context);
                                mDbHelper.createDatabase();
                                mDbHelper.open();
                                deleteData("Leaders");
                                for (int i = 0; i < j.length(); i++) {
                                    mDbHelper.insertLeaders(
                                            j.getJSONObject(i).getString("VEC"),
                                            j.getJSONObject(i).getString("CollegeName")
                                    );
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
                @EverythingIsNonNull
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } else if (wifiState != null && mobileState != null) {
            Log.e("CheckConn", "No net");
        }
    }
}