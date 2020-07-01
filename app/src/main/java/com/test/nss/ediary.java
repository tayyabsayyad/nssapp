package com.test.nss;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

public class ediary extends AppCompatActivity {

    AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ediary);
        drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        logout = findViewById(R.id.logoutbutton);

        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_main,
                R.id.nav_acti, R.id.nav_work, R.id.nav_camp)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ediary.this, startActivity.class);
                startActivity(i);
                //MainFragment mainFragment = new MainFragment();
                //FragmentManager fragmentManager = getSupportFragmentManager();
                //fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, mainFragment, mainFragment.getTag()).commit();
                finish();
                Toast.makeText(ediary.this, "Logged Out!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void click(View view) {
        Toast.makeText(ediary.this, "Clicked First year", Toast.LENGTH_SHORT).show();
    }

    public void click2(View view) {
        Toast.makeText(ediary.this, "Clicked second year", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}