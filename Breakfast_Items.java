package com.application.mybalancediary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Breakfast_Items extends AppCompatActivity {
    ArrayAdapter vectorAdapter;
    private ListView  list;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String eat;
    Vector<String> vector_list=new Vector();

    Date date = new Date();
    String today= new SimpleDateFormat("yyyy-MM-dd").format(date);
    private DatabaseReference getCaloriesRef(String ref) {
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        return mDatabase.child(today).child("Breakfast").child(userId).child(ref);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfast_items);
        list=findViewById(R.id.listView);
        Vector calories_breakfast= FoodBreakfastAdapter.calories_breakfast;
        Vector proteins_breakfast= FoodBreakfastAdapter.proteins_breakfast;
        Vector fats_breakfast= FoodBreakfastAdapter.fats_breakfast;
        Vector carbs_breakfast= FoodBreakfastAdapter.carbs_breakfast;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child(today).child("Breakfast");
        Query queryEat = databaseReference;
        queryEat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    eat=String.valueOf(ds.child("NameBreakfast").getValue());
                    String[] strArr = eat.split(",");
                    Vector<String> tmp=new Vector<String>();
                    vector_list.clear();
                    for(String str:strArr)
                        if(str!="")
                            vector_list.add(str);
                    vectorAdapter = new ArrayAdapter(Breakfast_Items.this,android.R.layout.simple_list_item_checked, vector_list);
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
                getCaloriesRef("NameBreakfast").setValue(newTimeInDB);
                FoodBreakfastAdapter.total_cal-=Float.parseFloat(String.valueOf(calories_breakfast.get(position)));
                calories_breakfast.remove(position);
                FoodBreakfastAdapter.total_proteins-=Float.parseFloat(String.valueOf(proteins_breakfast.get(position)));
                proteins_breakfast.remove(position);
                FoodBreakfastAdapter.total_fats-=Float.parseFloat(String.valueOf(fats_breakfast.get(position)));
                fats_breakfast.remove(position);
                FoodBreakfastAdapter.total_carb-=Float.parseFloat(String.valueOf(carbs_breakfast.get(position)));
                carbs_breakfast.remove(position);
                vectorAdapter.notifyDataSetChanged();
                list.invalidateViews();
                list.refreshDrawableState();
                getCaloriesRef("total").setValue(Math.round(FoodBreakfastAdapter.total_cal*10.0 ) / 10.0);
                getCaloriesRef("totalfats").setValue(Math.round(FoodBreakfastAdapter.total_fats*10.0 ) / 10.0);
                getCaloriesRef("totalcarbs").setValue(Math.round(FoodBreakfastAdapter.total_carb*10.0 ) / 10.0);
                getCaloriesRef("totalprotein").setValue(Math.round(FoodBreakfastAdapter.total_proteins*10.0 ) / 10.0);
                return true;
            }
        });
    }

}