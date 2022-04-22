package com.application.mybalancediary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Vector;

public class Breakfast_Items extends AppCompatActivity {
    ArrayAdapter vectorAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfast_items);
        ListView list=findViewById(R.id.listView);

       Vector vector_list = FoodBreakfastAdapter.nameBreakfast;
       vectorAdapter = new ArrayAdapter(Breakfast_Items.this,android.R.layout.simple_list_item_checked, vector_list);
        list.setAdapter(vectorAdapter);
        list.invalidateViews();
        list.refreshDrawableState();
    }
}