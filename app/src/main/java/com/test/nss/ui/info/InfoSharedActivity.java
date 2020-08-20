package com.test.nss.ui.info;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rahul.bounce.library.BounceTouchListener;
import com.test.nss.R;
import com.test.nss.ui.onClickInterface;

import java.util.ArrayList;

public class InfoSharedActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView imageSource;
    ArrayList<AdapterDataInfo> adapterDataInfo = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);
        recyclerView = findViewById(R.id.recInfo);
        imageSource = findViewById(R.id.imageSource);

        adapterDataInfo.add(new AdapterDataInfo("Hayden Cordeiro", "Web Developer", "https://github.com/haydencordeiro", ""));
        adapterDataInfo.add(new AdapterDataInfo("Malvika Shetty", "Web Developer", "https://github.com/MalvikaShetty", ""));
        adapterDataInfo.add(new AdapterDataInfo("Manasi", "Web Developer", "", ""));
        adapterDataInfo.add(new AdapterDataInfo("Priyal Vaz", "Designer", "", ""));
        adapterDataInfo.add(new AdapterDataInfo("Ramola", "Contributor", "", ""));
        adapterDataInfo.add(new AdapterDataInfo("Satyam Sharma", "App Developer", "https://github.com/Dixzz", ""));
        adapterDataInfo.add(new AdapterDataInfo("Shawn Louis", "Web Developer", "https://github.com/Shawn1912", ""));
        adapterDataInfo.add(new AdapterDataInfo("Shivam Singh", "Contributor", "", "https://www.linkedin.com/in/shivaam-singh-12450b1b2"));
        adapterDataInfo.add(new AdapterDataInfo("Tayyabali Sayyad", "Mentor", "https://github.com/tayyabsayyad", ""));

        onClickInterface onClickInterface;
        onClickInterface = abc -> {
            if (!abc.equals("")) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(abc));
                startActivity(i);
            }
        };

        MyListAdapterInfo adapterInfo = new MyListAdapterInfo(adapterDataInfo, InfoSharedActivity.this, onClickInterface);
        recyclerView.setLayoutManager(new LinearLayoutManager(InfoSharedActivity.this));
        recyclerView.setAdapter(adapterInfo);

        BounceTouchListener bounceTouchListener = new BounceTouchListener(recyclerView);
        bounceTouchListener.setOnTranslateListener(new BounceTouchListener.OnTranslateListener() {
            @Override
            public void onTranslate(float translation) {
                if (translation <= 0) {
                    bounceTouchListener.setMaxAbsTranslation(Math.max(80, -99));
                }

            }
        });

        recyclerView.setOnTouchListener(bounceTouchListener);
    }

    public void clickSource(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://github.com/tayyabsayyad/nssapp"));
        startActivity(i);
    }
}