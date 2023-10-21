package com.example.bottomnevigation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    SharedPreferences preferences; //temp database
    SharedPreferences.Editor editor;  //edit or modify the temp database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        preferences = PreferenceManager.getDefaultSharedPreferences(DashboardActivity.this);
        editor = preferences.edit();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nev);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_cntainer, new HomeFragment()).commit();


    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            int id = item.getItemId();
                if (id == R.id.dashboard_cycle) {
            selectedFragment = new cycleFragment();
                }
                if (id == R.id.dashboard_home1){
                    selectedFragment = new HomeFragment();

                }
                if (id==R.id.dashboard_Ecycle){
                    selectedFragment = new ECycleFragment();
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_cntainer,selectedFragment).commit();
            return true;
        }
    };
}