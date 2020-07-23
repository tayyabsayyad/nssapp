package com.test.nss;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.test.nss.api.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class SignupActivity extends AppCompatActivity {

    AutoCompleteTextView dropdownClg;

    ArrayList<String> clgList;
    Context mContext;

    private EditText fName;
    private EditText fathName;
    private EditText mName;
    private EditText lName;

    private EditText email;
    private EditText vec;
    private TextView vecClgPref;

    private Button signupPost;
    private EditText contactNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mContext = SignupActivity.this;
        vec = findViewById(R.id.vec_in);

        fName = findViewById(R.id.fname);
        fathName = findViewById(R.id.fath_name);
        mName = findViewById(R.id.mom_name);
        lName = findViewById(R.id.last_name);
        email = findViewById(R.id.email_in);
        signupPost = findViewById(R.id.signup_post);
        dropdownClg = findViewById(R.id.dropdown_clg);
        contactNo = findViewById(R.id.contact_no);
        vecClgPref = findViewById(R.id.vec_clg_pref);

        clgList = new ArrayList<>();

        //clgList.add("--- Select ---");
        //dropdownClg.setAdapter(adapter);

        if (isNetworkAvailable()) {
            Call<ResponseBody> clg = RetrofitClient.getInstance().getApi().getClgList();
            clg.enqueue(new Callback<ResponseBody>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            //JSONObject j = new JSONObject(response.body().string());
                            JSONArray jArry = new JSONArray(response.body().string());
                            for (int i = 0; i < jArry.length(); i++) {
                                clgList.add(jArry.getJSONObject(i).getString("CollegeName") + "-" + jArry.getJSONObject(i).getString("CollegeCode"));
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(SignupActivity.this, R.layout.drop_down_start, clgList);
                            dropdownClg.setAdapter(adapter);
                            Log.e("Added college", "");
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                @EverythingIsNonNull
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    clgList.add("");
                }
            });

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(new Date());

            //dropdownClg.setHintTextColor(mContext.getColor(R.color.colorPrimary));
            dropdownClg.setDropDownBackgroundResource(R.drawable.drpdwn_clg_bg);
            //dropdownClg.
            dropdownClg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dropdownClg.showDropDown();
                }
            });
            dropdownClg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String clgCode = adapterView.getItemAtPosition(i).toString();

                    vecClgPref.setText("");
                    clgCode = clgCode.substring(clgCode.indexOf("-") + 1);
                    vecClgPref.append(clgCode);
                }
            });

            signupPost.setOnClickListener(v -> {

                final EditText vec = findViewById(R.id.vec_in);

                if (vec.getText().toString().trim().length()<5 && vec.getText().toString().trim().length()!=5)
                    vec.setError("Enter only 5 digits");

                else if (!isEmpty(fName) && !isEmpty(fathName) &&
                        !isEmpty(mName) && !isEmpty(lName) && !isEmpty(vec) &&
                        !isEmpty(email) && !isEmpty(contactNo) && !vecClgPref.getText().toString().equals("")
                        && !isEmpty(dropdownClg) && vec.getText().toString().trim().length()==5) {
                    Snackbar.make(v, "Signing you up!", Snackbar.LENGTH_SHORT).show();

                    String clgItem = dropdownClg.getText().toString();
                    clgItem = clgItem.substring(0, clgItem.indexOf("-"));
                    Call<ResponseBody> signup = RetrofitClient.getInstance().getApi().signup(
                            "1",
                            date,
                            fName.getText().toString(),
                            fathName.getText().toString(),
                            mName.getText().toString(),
                            lName.getText().toString(),
                            "MH09" + vecClgPref.getText().toString() + vec.getText().toString(),
                            email.getText().toString(),
                            clgItem,
                            "+91" + contactNo.getText().toString()
                    );

                    signup.enqueue(new Callback<ResponseBody>() {
                        @EverythingIsNonNull
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                finish();
                            } else if (response.errorBody() != null) {
                                Log.e("onResponse:error", response.errorBody().toString());
                                try {
                                    JSONObject j = new JSONObject(response.errorBody().string());

                                    Log.e("error", j.toString());
                                    //Toast.makeText(mContext, j.toString(), Toast.LENGTH_SHORT).show();
                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @EverythingIsNonNull
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("SignUp", t.toString());
                        }
                    });
                } else
                    Toast.makeText(mContext, "Please Enter all details", Toast.LENGTH_SHORT).show();
            });
        } else
            Toast.makeText(mContext, "Device is offline", Toast.LENGTH_SHORT).show();
    }

    public boolean isEmpty(EditText e) {
        return e.getText().toString().trim().length() <= 0;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean pingURL(String url) {
        url = url.replaceFirst("^https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            //connection.setConnectTimeout(timeout);
            //connection.setReadTimeout(timeout);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            Log.wtf("AAAA", ""+responseCode);
            return (200 <= responseCode && responseCode <= 399);
        } catch (IOException exception) {
            return false;
        }
    }
}