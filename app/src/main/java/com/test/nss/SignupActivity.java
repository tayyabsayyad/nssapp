package com.test.nss;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.test.nss.api.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class SignupActivity extends AppCompatActivity {

    Spinner dropdownClg;
    ArrayList<String> clgList;
    Context mContext;

    private EditText fName;
    private EditText fathName;
    private EditText mName;
    private EditText lName;
    private EditText vec;
    private EditText email;
    private Button signupPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mContext = SignupActivity.this;

        fName = findViewById(R.id.fname);
        fathName = findViewById(R.id.fath_name);
        mName = findViewById(R.id.mom_name);
        lName = findViewById(R.id.last_name);
        vec = findViewById(R.id.vec_in);
        email = findViewById(R.id.email_in);
        signupPost = findViewById(R.id.signup_post);

        clgList = new ArrayList<>();
        clgList.add("DBIT");
        clgList.add("RAIT");
        dropdownClg = findViewById(R.id.dropdown_clg);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(SignupActivity.this, android.R.layout.simple_spinner_dropdown_item, clgList);
        dropdownClg.setAdapter(adapter);

        signupPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Signing you up", Toast.LENGTH_SHORT).show();
                if (!isEmpty(fName) && !isEmpty(fathName) &&
                        !isEmpty(mName) && !isEmpty(lName) && !isEmpty(vec) && !isEmpty(email) && dropdownClg.getSelectedItem() != null) {
                    Call<ResponseBody> signup = RetrofitClient.getInstance().getApi().signup(
                            "Token " + "4c64e971437eb17194ca21dce568139f91af6c36",
                            fName.getText().toString(),
                            fathName.getText().toString(),
                            mName.getText().toString(),
                            lName.getText().toString(),
                            vec.getText().toString(),
                            email.getText().toString(),
                            dropdownClg.getSelectedItem().toString()
                    );

                    signup.enqueue(new Callback<ResponseBody>() {
                        @EverythingIsNonNull
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(mContext, "Signed Up", Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (response.errorBody() != null) {
                                Log.e("onResponse:error", response.errorBody().toString());
                                try {
                                    JSONObject j = new JSONObject(response.errorBody().string());
                                    //Log.e("error", j.g
                                    Toast.makeText(mContext, j.toString(), Toast.LENGTH_SHORT).show();
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
            }
        });
    }

    private boolean isEmpty(EditText e) {
        return e.getText().toString().trim().length() <= 0;
    }

}