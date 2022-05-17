package com.application.mybalancediary;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;


public class Dinner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dinner);
        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.rcfrag_mainDinner, FoodRecyclerViewMainDinner.newInstance()).commit();
        }
        ActionBar mActionBar = getSupportActionBar();


    }

}
