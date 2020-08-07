package com.test.nss;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;
import com.test.nss.api.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class startActivity extends AppCompatActivity {

    String AUTH_TOKEN;
    String VEC;

    TextView startReg;
    TextView startSummary;
    TextView startRemember;
    CheckBox startCheck;
    Button loginButton;
    EditText username;
    EditText password;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mContext = startActivity.this;

        startReg = findViewById(R.id.register);
        startSummary = findViewById(R.id.loginSummary);
        startRemember = findViewById(R.id.remember);
        startCheck = findViewById(R.id.startCheck);
        loginButton = findViewById(R.id.loginText);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        Spannable spannable = new SpannableStringBuilder("Need an account?");
        spannable.setSpan(
                new ForegroundColorSpan(Color.BLACK),
                0, 7,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        startReg.setText(spannable);

        startRemember.setOnClickListener(view ->
                startCheck.setChecked(!startCheck.isChecked()));

        Animation animation2 = AnimationUtils.loadAnimation(startActivity.this, R.anim.ne);

        startReg.setOnClickListener(v -> {
            startReg.startAnimation(animation2);
            animation2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Intent m = new Intent(mContext, SignupActivity.class);
                    startActivity(m);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        });

        ActivityCompat.requestPermissions(startActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);

        //AnimatorSet animation = (AnimatorSet) AnimatorInflater.loadAnimator( startActivity.this, R.animator.test);
        int[] loc = new int[2];
        loginButton.getLocationInWindow(loc);

        //Animation animation = AnimationUtils.loadAnimation(startActivity.this, new BounceInterpolator());
        Animation animation = AnimationUtils.loadAnimation(startActivity.this, R.anim.bounce);
        animation.setInterpolator(new MyBounceInterpolator(1, 20));

        //path.addCircle(x,y ,15, Path.Direction.CW);
        loginButton.setOnClickListener(view -> {
            loginButton.startAnimation(animation);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (!isEmpty(username) && !(isEmpty(password))) {
                        Log.e("Start", "onClick: Logging in...");

                        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().login(username.getText().toString(), password.getText().toString());
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                try {
                                    if (response.isSuccessful() && response.body() != null) {
                                        SharedPreferences shareit = getSharedPreferences("KEY", MODE_PRIVATE);
                                        SharedPreferences.Editor eddy = shareit.edit();

                                        Log.e("onResponse", "Logged In");
                                        if (startCheck.isChecked()) {
                                            eddy.putInt("logged", 1);
                                        }
                                        JSONObject j = new JSONObject(response.body().string());
                                        AUTH_TOKEN = j.getString("auth_token");
                                        //Log.e("AUTH_TOKEN", AUTH_TOKEN);
                                        VEC = username.getText().toString();
                                        eddy.putString("AUTH_TOKEN", AUTH_TOKEN);
                                        eddy.putString("VEC", VEC);
                                        eddy.apply();

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
                                                            /*isLeader = 1;
                                                            leaderId = j2.getJSONObject(0).getInt("id");*/
                                                            eddy.putInt("isLeader", 1);
                                                            eddy.putInt("leaderId", j2.getJSONObject(0).getInt("id"));

                                                            //Log.e("in", "onResponse: " + isLeader + leaderId);
                                                        } else {
                                                            eddy.putInt("isLeader", 0);
                                                        }
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

                                        Call<ResponseBody> insertUsers = RetrofitClient.getInstance().getApi().getUserDetail(VEC);
                                        insertUsers.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            @EverythingIsNonNull
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.isSuccessful() && response.body() != null) {
                                                    try {
                                                        JSONObject j2 = new JSONObject(response.body().string());
                                                        JSONArray j = new JSONArray();
                                                        j.put(j2);

                                                        if (j.length() >= 0) {
                                                            DatabaseAdapter mDbHelper = new DatabaseAdapter(startActivity.this);
                                                            mDbHelper.createDatabase();
                                                            mDbHelper.open();
                                                            deleteData("Registration");
                                                            for (int i = 0; i < j.length(); i++) {
                                                                j.getJSONObject(i).getString("State");
                                                                mDbHelper.insertUsers(
                                                                        j.getJSONObject(i).getString("CollegeName"),
                                                                        j.getJSONObject(i).getString("VEC"),
                                                                        j.getJSONObject(i).getString("FirstName"),
                                                                        j.getJSONObject(i).getString("LastName"),
                                                                        j.getJSONObject(i).getString("Email"),
                                                                        j.getJSONObject(i).getString("Contact"),
                                                                        j.getJSONObject(i).getString("State")
                                                                );
                                                            }
                                                            mDbHelper.close();
                                                        }
                                                        Intent i = new Intent(mContext, ediary.class);
                                                        startActivity(i);
                                                        finish();
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
                                        /*Intent i = new Intent(mContext, ediary.class);
                                        startActivity(i);
                                        finish();*/
                                    } else {
                                        if (response.errorBody() != null) {
                                            JSONObject j = new JSONObject(response.errorBody().string());
                                            String m = j.getString("non_field_errors");
                                            Toast.makeText(mContext, "" + m.substring(2, m.length() - 2), Toast.LENGTH_SHORT).show();
                                            username.setText("");
                                            password.setText("");
                                            username.requestFocus();
                                        }
                                    }
                                    //String s = response.errorBody().string();
                                    //Toast.makeText(startActivity.this, s, Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Log.e("Start", "" + e.getMessage());
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                //Toast.makeText(startActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(mContext, "Please Connect to internet", Toast.LENGTH_SHORT).show();
                                Log.e("onFailure", "" + t.getMessage());
                            }
                        });
                    } else {
                        if (isEmpty(username) && isEmpty(password)) {
                            Snackbar.make(view, "Enter the details", Snackbar.LENGTH_SHORT).show();
                            username.setText("");
                            password.setText("");
                            username.requestFocus();
                        } else if (isEmpty(username)) {
                            username.setError("Enter username");
                            username.setText("");
                            username.requestFocus();
                        } else if (isEmpty(password)) {
                            password.setError("Enter password");
                            password.setText("");
                            password.requestFocus();
                        }
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        });
    }

    private boolean isEmpty(EditText e) {
        return e.getText().toString().trim().length() <= 0;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                finish();
                Toast.makeText(mContext, "Permission denied to read External storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void deleteData(String table) {
        DatabaseAdapter mDbHelper2 = new DatabaseAdapter(startActivity.this);
        mDbHelper2.createDatabase();
        mDbHelper2.open();
        DataBaseHelper mDb2 = new DataBaseHelper(startActivity.this);
        SQLiteDatabase m = mDb2.getWritableDatabase();
        m.execSQL("DELETE FROM " + table);
        mDbHelper2.close();
        m.close();
        mDb2.close();
    }
}