package com.application.mybalancediary;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;


public class Lunch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch);
        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.rcfrag_mainLunch, FoodRecyclerViewMainLunch.newInstance()).commit();
        }
        ActionBar mActionBar = getSupportActionBar();


    }

}
