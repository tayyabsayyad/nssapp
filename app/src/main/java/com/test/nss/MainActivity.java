package com.test.nss;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

public class
MainActivity extends AppCompatActivity {
    MotionLayout motionLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        //      | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        //    | View.SYSTEM_UI_FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        motionLayout = findViewById(R.id.actMainMotionLayout);

        SharedPreferences sharedPreferences = getSharedPreferences("KEY", MODE_PRIVATE);

        motionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {
                if (sharedPreferences.getInt("logged", 0) == 1) {
                    Intent intent = new Intent(MainActivity.this, ediary.class);
                    startActivity(intent);
                } else {
                    Intent in = new Intent(MainActivity.this,
                            startActivity.class);
                    startActivity(in);
                }
                finish();
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

            }
        });

        //new Handler().postDelayed(() -> {
        //}, SPLASH_SCREEN_TIME_OUT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        motionLayout.startLayoutAnimation();
    }
}