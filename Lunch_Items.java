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

public class Lunch_Items extends AppCompatActivity {
    ArrayAdapter vectorAdapter;
    private ListView  list;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String eat;
    Vector<String> vector_list =new Vector();
    Date date = new Date();
    String today= new SimpleDateFormat("yyyy-MM-dd").format(date);
    private DatabaseReference getCaloriesRef(String ref) {
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        return mDatabase.child(today).child("Lunch").child(userId).child(ref);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_items);
        list=findViewById(R.id.listViewLunch);
        Vector calories_lunch= FoodLunchAdapter.calories_lunch;
        Vector proteins_lunch= FoodLunchAdapter.proteins_lunch;
        Vector fats_lunch= FoodLunchAdapter.fats_lunch;
        Vector carbs_lunch= FoodLunchAdapter.carbs_lunch;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child(today).child("Lunch");
        Query queryEat = databaseReference;
        queryEat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    eat=String.valueOf(ds.child("NameLunch").getValue());
                    String[] strArr = eat.split(",");
                    Vector<String> tmp=new Vector<String>();
                    vector_list.clear();
                    for(String str:strArr)
                        if(str!="")
                            vector_list.add(str);
                    vectorAdapter = new ArrayAdapter(Lunch_Items.this,android.R.layout.simple_list_item_checked, vector_list);
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
                getCaloriesRef("NameLunch").setValue(newTimeInDB);
                vectorAdapter.notifyDataSetChanged();
                Float cal_lunch=Float.parseFloat(String.valueOf(calories_lunch.get(position)));
                calories_lunch.remove(position);
                Float pr_lunch=Float.parseFloat(String.valueOf(proteins_lunch.get(position)));
                proteins_lunch.remove(position);
                Float ft_lunch=Float.parseFloat(String.valueOf(fats_lunch.get(position)));
                fats_lunch.remove(position);
                Float cr_lunch=Float.parseFloat(String.valueOf(carbs_lunch.get(position)));
                carbs_lunch.remove(position);
                vectorAdapter.notifyDataSetChanged();
                list.invalidateViews();
                list.refreshDrawableState();
                FoodLunchAdapter.total_cal-=cal_lunch;
                FoodLunchAdapter.total_proteins-=pr_lunch;
                FoodLunchAdapter.total_fats-=ft_lunch;
                FoodLunchAdapter.total_carb-=cr_lunch;
                getCaloriesRef("total").setValue(Math.round(FoodLunchAdapter.total_cal*10.0 ) / 10.0);
                getCaloriesRef("totalfats").setValue(Math.round(FoodLunchAdapter.total_fats*10.0 ) / 10.0);
                getCaloriesRef("totalcarbs").setValue(Math.round(FoodLunchAdapter.total_carb*10.0 ) / 10.0);
                getCaloriesRef("totalprotein").setValue(Math.round(FoodLunchAdapter.total_proteins*10.0 ) / 10.0);
                return true;
            }
        });
    }

}