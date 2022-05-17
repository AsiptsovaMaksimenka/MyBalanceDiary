package com.application.mybalancediary;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;


public class Snacks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_snacks);
        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.rcfrag_mainSnack, FoodRecyclerViewMainSnacks.newInstance()).commit();
        }
        ActionBar mActionBar = getSupportActionBar();


    }

}
