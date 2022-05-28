package com.application.mybalancediary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.ActionBar;

import com.application.mybalancediary.ui.home.HomeFragment;


public class Breakfast extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfast);
        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.rcfrag_main, FoodRecyclerViewMainBreakfast.newInstance()).commit();
        }
        ActionBar mActionBar = getSupportActionBar();

    }

}
