package com.application.mybalancediary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Vector;

public class Dinner_Items extends AppCompatActivity {
    ArrayAdapter vectorAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dinner_items);
        ListView list=findViewById(R.id.listViewDinner);

        Vector vector_list = FoodDinnerAdapter.nameDinner;
        vectorAdapter = new ArrayAdapter(Dinner_Items.this,android.R.layout.simple_list_item_1, vector_list);
        list.setAdapter(vectorAdapter);
    }
}