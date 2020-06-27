package com.test.nss;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class startActivity extends AppCompatActivity {

    TextView startReg;
    TextView startSummary;
    TextView startRemember;
    CheckBox startCheck;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        startReg = findViewById(R.id.register);
        startSummary = findViewById(R.id.loginSummary);
        startRemember = findViewById(R.id.remember);
        startCheck = findViewById(R.id.startCheck);
        loginButton = findViewById(R.id.loginText);

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
                Intent i = new Intent(startActivity.this, ediary.class);
                startActivity(i);
                finish();
            }
        });
    }
}