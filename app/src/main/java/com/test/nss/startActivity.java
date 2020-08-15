package com.test.nss;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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

    LinearLayout linearLayout, linearLayout2;
    Context mContext;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mContext = startActivity.this;

        linearLayout = findViewById(R.id.linearLayout);
        linearLayout2 = findViewById(R.id.linearLayout2);

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
        username.requestFocus();

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
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE},
                1);

        //AnimatorSet animation = (AnimatorSet) AnimatorInflater.loadAnimator( startActivity.this, R.animator.test);
        int[] loc = new int[2];
        loginButton.getLocationInWindow(loc);

        //Animation animation = AnimationUtils.loadAnimation(startActivity.this, new BounceInterpolator());
        Animation animation = AnimationUtils.loadAnimation(startActivity.this, R.anim.bounce);
        animation.setInterpolator(new MyBounceInterpolator(1, 20));

        username.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Animation animation3 = AnimationUtils.loadAnimation(startActivity.this, R.anim.bounce);
                animation3.setInterpolator(new MyBounceInterpolator(0.1, 10));
                linearLayout.startAnimation(animation3);
                return false;
            }
        });

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Animation animation3 = AnimationUtils.loadAnimation(startActivity.this, R.anim.bounce);
                animation3.setInterpolator(new MyBounceInterpolator(0.1, 10));
                linearLayout2.startAnimation(animation3);
                return false;
            }
        });
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

                                        JSONObject j = new JSONObject(response.body().string());
                                        AUTH_TOKEN = j.getString("auth_token");
                                        //Log.e("AUTH_TOKEN", AUTH_TOKEN);
                                        VEC = username.getText().toString();
                                        Log.e("AA", VEC);
                                        eddy.putString("AUTH_TOKEN", AUTH_TOKEN);
                                        eddy.putString("VEC", VEC);
                                        eddy.apply();

                                        Log.e("AA", "AAA" + AUTH_TOKEN);
                                        Call<ResponseBody> insertUsers =
                                                RetrofitClient.getInstance().getApi().getUserDetail("Token " + AUTH_TOKEN);
                                        insertUsers.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            @EverythingIsNonNull
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.isSuccessful() && response.body() != null) {
                                                    try {
                                                        JSONArray j = new JSONArray(response.body().string());

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
                                                                        j.getJSONObject(i).getString("State"),
                                                                        j.getJSONObject(i).getString("IsLeader")
                                                                );
                                                            }
                                                            mDbHelper.close();
                                                        }
                                                        if (startCheck.isChecked()) {
                                                            eddy.putInt("logged", 1);
                                                            eddy.apply();
                                                        }
                                                        if (j.getJSONObject(0).getString("IsLeader").equals("Appointed"))
                                                            eddy.putInt("isLeader", 1);
                                                        else
                                                            eddy.putInt("isLeader", 0);

                                                        eddy.apply();
                                                        Intent i = new Intent(mContext, ediary.class);
                                                        startActivity(i);
                                                        finish();
                                                    } catch (JSONException | IOException e) {
                                                        Log.e("Failed", e.toString());
                                                        e.printStackTrace();
                                                    }
                                                } else if (response.errorBody() != null) {
                                                    try {
                                                        Log.e("Error", "" + response.errorBody().string());
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
                Toast.makeText(mContext, "Permission denied, please give permissions from settings", Toast.LENGTH_SHORT).show();
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