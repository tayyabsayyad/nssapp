package com.test.nss;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.test.nss.api.RetrofitClient;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class startActivity extends AppCompatActivity {

    public static String AUTH_USER;
    TextView startReg;
    TextView startSummary;
    TextView startRemember;
    CheckBox startCheck;
    Button loginButton;
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


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
        Typeface typefaceBold = Typeface.createFromAsset(getAssets(), "fonts/google_sans_bold.ttf");
        startReg.setTypeface(typefaceBold);

        Typeface typefaceReg = Typeface.createFromAsset(getAssets(), "fonts/google_sans.ttf");
        startSummary.setTypeface(typefaceReg);

        startRemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCheck.setChecked(!startCheck.isChecked());
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEmpty(username) && !(isEmpty(password))) {
                    Call<ResponseBody> call = RetrofitClient.getInstance().getApi().login(username.getText().toString(), password.getText().toString());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                            try {
                                if (response.isSuccessful() && response.body() != null) {
                                    Log.e("Success", "Done re");
                                    JSONObject j = new JSONObject(response.body().string());
                                    AUTH_USER = j.getString("auth_token");
                                    Intent i = new Intent(startActivity.this, ediary.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    if (response.errorBody() != null) {
                                        JSONObject j = new JSONObject(response.errorBody().string());
                                        String m = j.getString("non_field_errors");
                                        Toast.makeText(startActivity.this, "" + m.substring(2, m.length() - 2), Toast.LENGTH_SHORT).show();
                                        username.setText("");
                                        password.setText("");
                                        username.requestFocus();
                                    } else {
                                        Snackbar.make(view, "Something went wrong...", Snackbar.LENGTH_SHORT).show();
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
                        public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(startActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                            Log.e("Start", "" + t.getMessage());
                        }
                    });
                } else {
                    if (username.getText().toString().equals("") && password.getText().toString().equals("")) {
                        username.setError("Enter username");
                        password.setError("Enter password");
                    } else if (username.getText().toString().equals("")) {
                        username.setError("Enter username");
                        username.requestFocus();
                    } else if (password.getText().toString().equals("")) {
                        password.setError("Enter password");
                        password.requestFocus();
                    }
                }
            }
        });
    }

    private boolean isEmpty(EditText e) {
        return e.getText().toString().trim().length() <= 0 || e.getText().toString().equals("");
    }
}