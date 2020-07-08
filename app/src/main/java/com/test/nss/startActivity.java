package com.test.nss;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class startActivity extends AppCompatActivity {

    public static String AUTH_TOKEN;

    TextView startReg;
    TextView startSummary;
    TextView startRemember;
    CheckBox startCheck;
    Button loginButton;
    AutoCompleteTextView username;
    EditText password;

    ArrayList<String> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        users = new ArrayList<String>();

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

        startRemember.setOnClickListener(view ->
                startCheck.setChecked(!startCheck.isChecked()));

        SharedPreferences sharedPreferences = getSharedPreferences("KEY", MODE_PRIVATE);
        String getName = sharedPreferences.getString("BKEY", "");
        users.add(getName);

        username.setDropDownWidth(400);
        username.setDropDownHorizontalOffset(55);
        username.setDropDownBackgroundResource(R.drawable.account_roundbg);

        username.setAdapter(new ArrayAdapter<>(this, R.layout.drop_down_start, users));

        loginButton.setOnClickListener(view -> {
            if (!isEmpty(username) && !(isEmpty(password))) {
                Log.e("Start", "onClick: Logging in...");

                Call<ResponseBody> call = RetrofitClient.getInstance().getApi().login(username.getText().toString(), password.getText().toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        try {
                            if (response.isSuccessful() && response.body() != null) {
                                if (startCheck.isChecked()) {
                                    SharedPreferences shareit = getSharedPreferences("KEY", MODE_PRIVATE);
                                    SharedPreferences.Editor eddy = shareit.edit();
                                    eddy.putString("BKEY", username.getText().toString());
                                    eddy.apply();
                                }
                                Log.e("onResponse", "Logged In");
                                JSONObject j = new JSONObject(response.body().string());
                                AUTH_TOKEN = j.getString("auth_token");
                                Log.e("AUTH_TOKEN", AUTH_TOKEN);
                                Intent i = new Intent(startActivity.this, ediary.class);
                                startActivity(i);
                                finish();
                                String t = "Token " + AUTH_TOKEN;
                                Log.e("SS", t);
                                Call<ResponseBody> detail = RetrofitClient.getInstance().getApi().getAct(t);

                                detail.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                        Log.e("detail", "Success");
                                        if (response.isSuccessful() && response.body() != null) {
                                            Log.e("onRs", "Success");
                                            try {
                                                Log.e("asasas", response.body().string());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            //Log.e("onRs", response.message());
                                            //Log.e("onRs", response.body().toString());
                                        } else if (response.errorBody() != null) {
                                            try {
                                                Toast.makeText(startActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                            /*try {
                                                JSONObject j = new JSONObject(response.body().string());
                                                Log.e("Kuch to aara",j.toString());
                                            } catch (JSONException | IOException e) {
                                                e.printStackTrace();
                                            }*/
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Log.e("detail", "Failed" + t.getMessage());
                                    }
                                });
                            } else {
                                if (response.errorBody() != null) {
                                    JSONObject j = new JSONObject(response.errorBody().string());
                                    String m = j.getString("non_field_errors");
                                    Toast.makeText(startActivity.this, "" + m.substring(2, m.length() - 2), Toast.LENGTH_SHORT).show();
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
                    public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(startActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("onFailure", "" + t.getMessage());
                    }
                });
            } else {
                if (isEmpty(username) && isEmpty(password)) {
                    Snackbar.make(view, "Enter the details", Snackbar.LENGTH_SHORT).show();
                    username.requestFocus();
                    username.setText("");
                    password.setText("");
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

        loginButton.setOnLongClickListener(v -> {
            Intent i = new Intent(startActivity.this, ediary.class);
            startActivity(i);
            return true;
        });
    }

    private boolean isEmpty(EditText e) {
        return e.getText().toString().trim().length() <= 0;
    }
}