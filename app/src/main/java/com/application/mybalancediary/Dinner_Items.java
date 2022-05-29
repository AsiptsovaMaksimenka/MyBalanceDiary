package com.application.mybalancediary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class Dinner_Items extends AppCompatActivity {
    ArrayAdapter vectorAdapter;
    private ListView  list;
    String eat;
    Date date = new Date();
    Vector<String> vector_list = new Vector();
    @SuppressLint("SimpleDateFormat")
    String today= new SimpleDateFormat("yyyy-MM-dd").format(date);
    private DatabaseReference getCaloriesRef(String ref) {
        return FirebaseDatabase.getInstance().getReference("Dinner").child(today)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ref);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dinner_items);
        list=findViewById(R.id.listViewDinner);
        Vector calories_dinner= FoodDinnerAdapter.calories_dinner;
        Vector proteins_dinner= FoodDinnerAdapter.proteins_dinner;
        Vector fats_dinner= FoodDinnerAdapter.fats_dinner;
        Vector carbs_dinner= FoodDinnerAdapter.carbs_dinner;
        FirebaseDatabase.getInstance().getReference("Dinner").child(today)
                .orderByKey().equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    eat=String.valueOf(ds.child("NameDinner").getValue());
                    String[] strArr = eat.split(",");
                    Vector<String> tmp=new Vector<String>();
                    vector_list.clear();
                    for(String str:strArr)
                        if(str!="")
                            vector_list.add(str);
                    vectorAdapter = new ArrayAdapter(Dinner_Items.this,android.R.layout.simple_list_item_checked, vector_list);
                    list.setAdapter(vectorAdapter);
                    list.invalidateViews();
                    list.refreshDrawableState();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                vector_list.remove(position);
                String newTimeInDB="";
                for(String str: vector_list)
                    newTimeInDB+=str+",";
                vectorAdapter.notifyDataSetChanged();
                getCaloriesRef("NameDinner").setValue(newTimeInDB);
                vectorAdapter.notifyDataSetChanged();
                Float cal_dinner=Float.parseFloat(String.valueOf(calories_dinner.get(position)));
                calories_dinner.remove(position);
                Float pr_dinner=Float.parseFloat(String.valueOf(proteins_dinner.get(position)));
                proteins_dinner.remove(position);
                Float ft_dinner=Float.parseFloat(String.valueOf(fats_dinner.get(position)));
                fats_dinner.remove(position);
                Float cr_dinner=Float.parseFloat(String.valueOf(carbs_dinner.get(position)));
                carbs_dinner.remove(position);
                vectorAdapter.notifyDataSetChanged();
                list.invalidateViews();
                list.refreshDrawableState();
                FoodDinnerAdapter.total_cal-=cal_dinner;
                FoodDinnerAdapter.total_proteins-=pr_dinner;
                FoodDinnerAdapter.total_fats-=ft_dinner;
                FoodDinnerAdapter.total_carb-=cr_dinner;
                getCaloriesRef("total").setValue(Math.round(FoodDinnerAdapter.total_cal*10.0 ) / 10.0);
                getCaloriesRef("totalfats").setValue(Math.round(FoodDinnerAdapter.total_fats*10.0 ) / 10.0);
                getCaloriesRef("totalcarbs").setValue(Math.round(FoodDinnerAdapter.total_carb*10.0 ) / 10.0);
                getCaloriesRef("totalprotein").setValue(Math.round(FoodDinnerAdapter.total_proteins*10.0 ) / 10.0);
                return true;
            }
        });
    }

}