package com.application.mybalancediary.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.application.mybalancediary.Breakfast;
import com.application.mybalancediary.Breakfast_Items;
import com.application.mybalancediary.Dinner;
import com.application.mybalancediary.Dinner_Items;
import com.application.mybalancediary.Lunch;
import com.application.mybalancediary.Lunch_Items;
import com.application.mybalancediary.R;
import com.application.mybalancediary.ReportsFood;
import com.application.mybalancediary.Snacks;
import com.application.mybalancediary.Snacks_Items;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class HomeFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference databaseCalReference;
    StorageReference storageReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView proteins_edit=root.findViewById(R.id.textView4);
        final TextView proteins_total=root.findViewById(R.id.textView5);
        final TextView fats_edit=root.findViewById(R.id.textView6);
        final TextView fats_total=root.findViewById(R.id.textView7);
        final TextView carbs_edit=root.findViewById(R.id.textView8);
        final TextView carbs_total=root.findViewById(R.id.textView10);
        final TextView total_calories=root.findViewById(R.id.textView2);
        final MaterialButton breakfast_items= root.findViewById(R.id.breakfast_items);
        final CircularProgressIndicator cal_summary=root.findViewById(R.id.circularProgressIndicator);
        final LinearProgressIndicator proteins_progress=root.findViewById(R.id.linearProgressIndicator);
        final LinearProgressIndicator fats_progress=root.findViewById(R.id.linearProgressIndicator2);
        final LinearProgressIndicator carbs_progress=root.findViewById(R.id.linearProgressIndicator3);
        final MaterialButton breakfast= root.findViewById(R.id.breakfast);
        final TextView breakfast_cal=root.findViewById(R.id.breakfast_cal);
        final TextView breakfast_prot=root.findViewById(R.id.brek_prot);
        final TextView breakfast_fats=root.findViewById(R.id.br_fats);
        final TextView breakfast_carbs=root.findViewById(R.id.br_carbs);
        final TextView cal_per_day=root.findViewById(R.id.textView);
        final MaterialButton lunch= root.findViewById(R.id.lunch);
        final MaterialButton lunch_items= root.findViewById(R.id.lunch_items);
        final MaterialButton dinner= root.findViewById(R.id.dinner);
        final MaterialButton dinner_items= root.findViewById(R.id.dinner_items);
        final MaterialButton snack= root.findViewById(R.id.snacks);
        final MaterialButton snack_items= root.findViewById(R.id.snacks_items);
        final MaterialButton reports= root.findViewById(R.id.reports);


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        databaseCalReference=firebaseDatabase.getReference("Calories");
        storageReference = FirebaseStorage.getInstance().getReference();
        Query query = databaseReference;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String Calories = String.valueOf(ds.child("bmr").getValue());
                    String proteins = "/ " + ds.child("proteins").getValue();
                    String carbs = "/ " + ds.child("carbs").getValue();
                    String fats = "/ " + ds.child("fats").getValue();
                    total_calories.setText(Calories);
                    proteins_total.setText(proteins);
                    fats_total.setText(fats);
                    carbs_total.setText(carbs);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query query2 = databaseCalReference;
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {

                    String br_cal=String.valueOf(ds.child("total").getValue());
                    String breakfast_protein=String.valueOf(ds.child("totalprotein").getValue());
                    String breakfast_fat=String.valueOf(ds.child("totalfats").getValue());
                    String breakfast_carb=String.valueOf(ds.child("totalcarbs").getValue());
                    String cal_per_breakfast=String.valueOf(ds.child("total").getValue());
                    breakfast_cal.setText(br_cal);
                    breakfast_prot.setText(breakfast_protein);
                    breakfast_fats.setText(breakfast_fat);
                    breakfast_carbs.setText(breakfast_carb);
                    cal_per_day.setText(cal_per_breakfast);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // food_carbs = Food_MyRecyclerViewAdapter.totalcarbs;
        //food_fat = Food_MyRecyclerViewAdapter.totalfat;
        // food_protein = Food_MyRecyclerViewAdapter.totalprotein;

      /*  if (food_fat > 0) {
            fats_progress.setProgress(Math.round((100 * (food_fat)) / max_fat));
        } else
            fats_progress.setProgress(Math.round((100 * (LoginActivity.user_fat)) / max_fat));
        if (food_fat > 0) {
            fats_edit.setText(String.valueOf(food_fat));
        } else {
            fats_edit.setText(String.valueOf(LoginActivity.user_fat));
        }
        if (food_carbs > 0) {
            carbs_progress.setProgress(Math.round((100 * (food_carbs)) / max_carbs));
        } else
            carbs_progress.setProgress(Math.round((100 * (LoginActivity.user_carbs)) / max_carbs));
        if (food_carbs > 0) {
            carbs_edit.setText(String.valueOf(food_carbs));
        } else {
            carbs_edit.setText(String.valueOf(LoginActivity.user_carbs));
        }

        if (food_protein > 0) {
            proteins_progress.setProgress(Math.round((100 * (food_protein)) / max_protein));
        } else
            proteins_progress.setProgress(Math.round((100 * (LoginActivity.user_protein)) / max_protein));
        if (food_protein > 0) {
            proteins_edit.setText(String.valueOf(food_protein));
        } else {
            proteins_edit.setText(String.valueOf(LoginActivity.user_protein));
        }

       */
        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Breakfast.class);
                startActivity(intent);
            }
        });
        breakfast_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Breakfast_Items.class);
                startActivity(intent);
            }
        });
        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Lunch.class);
                startActivity(intent);
            }
        });
        lunch_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Lunch_Items.class);
                startActivity(intent);
            }
        });
        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Dinner.class);
                startActivity(intent);
            }
        });
        dinner_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Dinner_Items.class);
                startActivity(intent);
            }
        });
        snack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Snacks.class);
                startActivity(intent);
            }
        });
        snack_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Snacks_Items.class);
                startActivity(intent);
            }
        });
        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportsFood.class);
                startActivity(intent);
            }
        });
        return root;
    }
}