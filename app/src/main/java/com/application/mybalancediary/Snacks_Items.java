package com.application.mybalancediary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Vector;

public class Snacks_Items extends AppCompatActivity {
    ArrayAdapter vectorAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snacks_items);
        ListView list=findViewById(R.id.listViewSnacks);

        Vector vector_list = FoodSnacksAdapter.nameSnack;
        vectorAdapter = new ArrayAdapter(Snacks_Items.this,android.R.layout.simple_list_item_1, vector_list);
        list.setAdapter(vectorAdapter);
    }
}