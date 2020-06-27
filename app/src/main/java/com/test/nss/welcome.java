package com.test.nss;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class welcome extends AppCompatActivity implements View.OnClickListener {

    private Button admin;
    private Button leader;
    private Button volunteer;
    private TextView faq;
    private TextView terms;
    private TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Typeface wel = Typeface.createFromAsset(getAssets(), "fonts/ma_shan_zheng.ttf");
        Typeface google_sans_bold = Typeface.createFromAsset(getAssets(), "fonts/google_sans_bold.ttf");
        Typeface google_sans = Typeface.createFromAsset(getAssets(), "fonts/google_sans.ttf");

        welcome = findViewById(R.id.welcome);
        welcome.setTypeface(wel);

        admin = findViewById(R.id.adminButton);
        admin.setOnClickListener(this);

        leader = findViewById(R.id.leaderButton);
        leader.setOnClickListener(this);

        volunteer = findViewById(R.id.volButton);
        volunteer.setOnClickListener(this);

        faq = findViewById(R.id.faq);
        faq.setTypeface(google_sans_bold);
        faq.setOnClickListener(this);

        terms = findViewById(R.id.terms);
        terms.setTypeface(google_sans);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.adminButton:
            case R.id.leaderButton:
            case R.id.volButton:
                Intent i = new Intent(welcome.this, startActivity.class);
                startActivity(i);
                break;
            case R.id.faq:
                Intent m = new Intent(welcome.this, faq.class);
                startActivity(m);
                break;
        }
    }
}