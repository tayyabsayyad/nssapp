package com.test.nss;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

public class startActivity extends AppCompatActivity {

    TextView tvReg;
    TextView tvSummary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        tvReg = findViewById(R.id.register);
        tvSummary = findViewById(R.id.loginSummary);


        Spannable spannable = new SpannableStringBuilder("Need an account?");
        spannable.setSpan(
                new ForegroundColorSpan(Color.BLACK),
                0, 7,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvReg.setText(spannable);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/google_sans.ttf");
        tvSummary.setTypeface(typeface);
    }
}