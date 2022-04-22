package com.application.mybalancediary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Vector;

public class Snacks_Items extends AppCompatActivity {
    ArrayAdapter vectorAdapter;
    private ListView  list;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference getCaloriesRef(String ref) {
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        return mDatabase.child("Snacks").child(userId).child(ref);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snacks_items);
        list=findViewById(R.id.listViewSnacks);
        Vector vector_list = FoodSnacksAdapter.nameSnack;
        Vector calories_snack= FoodSnacksAdapter.calories_snack;
        Vector proteins_snack= FoodSnacksAdapter.proteins_snack;
        Vector fats_snack= FoodSnacksAdapter.fats_snack;
        Vector carbs_snack= FoodSnacksAdapter.carbs_snack;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        vectorAdapter = new ArrayAdapter(Snacks_Items.this,android.R.layout.simple_list_item_checked, vector_list);
        list.setAdapter(vectorAdapter);
        list.invalidateViews();
        list.refreshDrawableState();
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                vector_list.remove(position);
                Float cal_snack=Float.parseFloat(String.valueOf(calories_snack.get(position)));
                calories_snack.remove(position);
                Float pr_snack=Float.parseFloat(String.valueOf(proteins_snack.get(position)));
                proteins_snack.remove(position);
                Float ft_snack=Float.parseFloat(String.valueOf(fats_snack.get(position)));
                fats_snack.remove(position);
                Float cr_snack=Float.parseFloat(String.valueOf(carbs_snack.get(position)));
                carbs_snack.remove(position);
                vectorAdapter.notifyDataSetChanged();
                list.invalidateViews();
                list.refreshDrawableState();
                FoodSnacksAdapter.total_cal-=cal_snack;
                FoodSnacksAdapter.total_proteins-=pr_snack;
                FoodSnacksAdapter.total_fats-=ft_snack;
                FoodSnacksAdapter.total_carb-=cr_snack;
                getCaloriesRef("total").setValue(Math.round(FoodSnacksAdapter.total_cal*10.0 ) / 10.0);
                getCaloriesRef("totalfats").setValue(Math.round(FoodSnacksAdapter.total_fats*10.0 ) / 10.0);
                getCaloriesRef("totalcarbs").setValue(Math.round(FoodSnacksAdapter.total_carb*10.0 ) / 10.0);
                getCaloriesRef("totalprotein").setValue(Math.round(FoodSnacksAdapter.total_proteins*10.0 ) / 10.0);
                return true;
            }
        });
    }

}