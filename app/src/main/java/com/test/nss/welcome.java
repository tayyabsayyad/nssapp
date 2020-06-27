package com.test.nss;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

public class welcome extends AppCompatActivity implements View.OnClickListener {

    private Button admin; //First button in login screen
    private Button leader; //Second Login
    private Button volunteer; //Third Login
    private TextView faq; //Bottom Faq text
    private TextView terms; //Center t&c
    private TextView welcome; //Main Welcome
    private TextView login; //Login title

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Typeface shanFont = Typeface.createFromAsset(getAssets(), "fonts/ma_shan_zheng.ttf");
        Typeface google_sans_bold = Typeface.createFromAsset(getAssets(), "fonts/google_sans_bold.ttf");
        Typeface google_sans = Typeface.createFromAsset(getAssets(), "fonts/google_sans.ttf");

        welcome = findViewById(R.id.welcome);
        welcome.setOnClickListener(this);
        welcome.setTypeface(shanFont);

        login = findViewById(R.id.loginTitle);
        login.setTypeface(google_sans_bold);

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
        terms.setOnClickListener(this);
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
            case R.id.terms:
                RandomColors randomColors = new RandomColors();
                terms.setTextColor(randomColors.getColor());
                break;
            case R.id.welcome:
                int color= ((int)(Math.random()*16777215)) | (0xFF << 24);
                welcome.setTextColor(color);
                break;
            case R.id.faq:
                Intent m = new Intent(welcome.this, faq.class);
                startActivity(m);
                break;
            default:
                break;
        }
    }

    class RandomColors {
        private Stack<Integer> recycle, colors;

        public RandomColors() {
            colors = new Stack<>();
            recycle =new Stack<>();
            recycle.addAll(Arrays.asList(
                    Color.rgb(91,187, 235),
                    ContextCompat.getColor(welcome.this, R.color.one),
                    ContextCompat.getColor(welcome.this, R.color.two),
                    ContextCompat.getColor(welcome.this, R.color.thr),
                    ContextCompat.getColor(welcome.this, R.color.four),
                    ContextCompat.getColor(welcome.this, R.color.five),
                    ContextCompat.getColor(welcome.this, R.color.six),
                    ContextCompat.getColor(welcome.this, R.color.sev)
                    )
            );
        }

        public int getColor() {
            if (colors.size()==0) {
                while(!recycle.isEmpty())
                    colors.push(recycle.pop());
                Collections.shuffle(colors);
            }
            Integer c= colors.pop();
            recycle.push(c);
            return c;
        }
    }
}