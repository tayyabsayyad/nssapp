package com.test.nss;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
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

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            try {
                //Your code goes here
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });

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

        SharedPreferences sharedPreferences = getSharedPreferences("KEY", MODE_PRIVATE);
        //String getName = sharedPreferences.getString("BKEY", "");

        //username.setText(sharedPreferences.getString("BKEY", ""));
        //password.setText(sharedPreferences.getString("AKEY", ""));

        startReg.setOnClickListener(v -> {
            Intent m = new Intent(mContext, SignupActivity.class);
            startActivity(m);
        });

        ActivityCompat.requestPermissions(startActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);

        loginButton.setOnClickListener(view -> {
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
                                if (startCheck.isChecked()) {
                                    eddy.putInt("logged", 1);
                                }

                                Log.e("onResponse", "Logged In");
                                JSONObject j = new JSONObject(response.body().string());
                                AUTH_TOKEN = j.getString("auth_token");
                                Log.e("AUTH_TOKEN", AUTH_TOKEN);
                                VEC = username.getText().toString();
                                eddy.putString("AUTH_TOKEN", AUTH_TOKEN);
                                eddy.putString("VEC", VEC);
                                eddy.apply();
                                Intent i = new Intent(mContext, ediary.class);

                                startActivity(i);
                                finish();
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
        });

        /*loginButton.setOnLongClickListener(v -> {
            Intent m = new Intent(mContext, ediary.class);
            startActivity(m);
            return true;
        });*/
        thread.start();
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
                Toast.makeText(mContext, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }
        }
    }
}