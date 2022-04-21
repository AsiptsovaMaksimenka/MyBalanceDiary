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
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,databaseBreakfastReference,databaseLunchReference,databaseDinnerReference,databaseSnackReference,databaseTotalPerDayReference;
    StorageReference storageReference;
    public Float totalCaloriesSn=0.0f,totalProteinsSn=0.0f,totalFatsSn=0.0f,totalCarbsSn=0.0f;
    public Float totalCaloriesBr=0.0f,totalProteinsBr=0.0f,totalFatsBr=0.0f,totalCarbsBr=0.0f;
    public Float totalCaloriesLn=0.0f,totalProteinsLn=0.0f,totalFatsLn=0.0f,totalCarbsLn=0.0f;
    public Float totalCaloriesDn=0.0f,totalProteinsDn=0.0f,totalFatsDn=0.0f,totalCarbsDn=0.0f;
    public Float br=0.0f, ln=0.0f, dn=0.0f, sn=0.0f;
    public Float userCal=0.0f, userProteins=0.0f,userFats=0.0f,userCarbs=0.0f;
    public Float TotalCal=0.0f, TotalProteins=0.0f,TotalFats=0.0f,TotalCarbs=0.0f;
    int color = 0xFFFF0000;

    private DatabaseReference getTotalPerDayRef(String ref) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        String userId = user.getUid();
        return mDatabase.child("TotalPerDay").child(userId).child(ref);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView proteins_per_day=root.findViewById(R.id.textView4);
        final TextView proteins_total=root.findViewById(R.id.textView5);
        final TextView fats_per_day=root.findViewById(R.id.textView6);
        final TextView fats_total=root.findViewById(R.id.textView7);
        final TextView carbs_per_day=root.findViewById(R.id.textView8);
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
        final TextView lunch_cal=root.findViewById(R.id.lunch_cal);
        final TextView lunch_prot=root.findViewById(R.id.lunch_prot);
        final TextView lunch_fats=root.findViewById(R.id.lunch_fats);
        final TextView lunch_carbs=root.findViewById(R.id.lunch_carbs);
        final TextView dinner_cal=root.findViewById(R.id.dinner_cal);
        final TextView dinner_prot=root.findViewById(R.id.dinner_prot);
        final TextView dinner_fats=root.findViewById(R.id.dinner_fats);
        final TextView dinner_carbs=root.findViewById(R.id.dinner_carbs);
        final TextView snack_cal=root.findViewById(R.id.snacks_cal);
        final TextView snack_prot=root.findViewById(R.id.snack_prot);
        final TextView snack_fats=root.findViewById(R.id.snack_fats);
        final TextView snack_carbs=root.findViewById(R.id.snack_carbs);
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
        databaseBreakfastReference=firebaseDatabase.getReference("Breakfast");
        databaseLunchReference=firebaseDatabase.getReference("Lunch");
        databaseDinnerReference=firebaseDatabase.getReference("Dinner");
        databaseSnackReference=firebaseDatabase.getReference("Snacks");
        databaseTotalPerDayReference=firebaseDatabase.getReference("TotalPerDay");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        storageReference = FirebaseStorage.getInstance().getReference();
        Query queryUser = databaseReference;
        queryUser.addValueEventListener(new ValueEventListener() {
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
                    userCal=Float.parseFloat(String.valueOf(ds.child("bmr").getValue()));
                    userProteins=Float.parseFloat(String.valueOf(ds.child("proteins").getValue()));
                    userFats=Float.parseFloat(String.valueOf(ds.child("fats").getValue()));
                    userCarbs=Float.parseFloat(String.valueOf(ds.child("carbs").getValue()));

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query queryBreakfast= databaseBreakfastReference;
        queryBreakfast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String br_cal=String.valueOf(ds.child("total").getValue());
                    String breakfast_protein=String.valueOf(ds.child("totalprotein").getValue());
                    String breakfast_fat=String.valueOf(ds.child("totalfats").getValue());
                    String breakfast_carb=String.valueOf(ds.child("totalcarbs").getValue());
                    breakfast_cal.setText(br_cal);
                    breakfast_prot.setText(breakfast_protein);
                    breakfast_fats.setText(breakfast_fat);
                    breakfast_carbs.setText(breakfast_carb);
                    br=Float.parseFloat(br_cal);
                    totalCaloriesBr=Float.parseFloat(br_cal);
                    totalProteinsBr=Float.parseFloat(breakfast_protein);
                    totalFatsBr=Float.parseFloat(breakfast_fat);
                    totalCarbsBr=Float.parseFloat(breakfast_carb);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query queryLunch = databaseLunchReference;
        queryLunch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String ln_cal=String.valueOf(ds.child("total").getValue());
                    String lunch_protein=String.valueOf(ds.child("totalprotein").getValue());
                    String lunch_fat=String.valueOf(ds.child("totalfats").getValue());
                    String lunch_carb=String.valueOf(ds.child("totalcarbs").getValue());
                    lunch_cal.setText(ln_cal);
                    lunch_prot.setText(lunch_protein);
                    lunch_fats.setText(lunch_fat);
                    lunch_carbs.setText(lunch_carb);
                    ln=Float.parseFloat(ln_cal);
                    totalCaloriesLn+=Float.parseFloat(ln_cal);
                    totalProteinsLn=Float.parseFloat(lunch_protein);
                    totalFatsLn=Float.parseFloat(lunch_fat);
                    totalCarbsLn=Float.parseFloat(lunch_carb);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query queryDinner = databaseDinnerReference;
        queryDinner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String dn_cal=String.valueOf(ds.child("total").getValue());
                    String dn_protein=String.valueOf(ds.child("totalprotein").getValue());
                    String dn_fat=String.valueOf(ds.child("totalfats").getValue());
                    String dn_carb=String.valueOf(ds.child("totalcarbs").getValue());
                    dinner_cal.setText(dn_cal);
                    dinner_prot.setText(dn_protein);
                    dinner_fats.setText(dn_fat);
                    dinner_carbs.setText(dn_carb);
                    dn=Float.parseFloat(dn_cal);
                    totalCaloriesDn=Float.parseFloat(dn_cal);
                    totalProteinsDn=Float.parseFloat(dn_protein);
                    totalFatsDn=Float.parseFloat(dn_fat);
                    totalCarbsDn=Float.parseFloat(dn_carb);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query querySnack = databaseSnackReference;
        querySnack.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String sn_cal=String.valueOf(ds.child("total").getValue());
                    String sn_protein=String.valueOf(ds.child("totalprotein").getValue());
                    String sn_fat=String.valueOf(ds.child("totalfats").getValue());
                    String sn_carb=String.valueOf(ds.child("totalcarbs").getValue());
                    snack_cal.setText(sn_cal);
                    snack_prot.setText(sn_protein);
                    snack_fats.setText(sn_fat);
                    snack_carbs.setText(sn_carb);
                    sn=Float.parseFloat(sn_cal);
                    totalCaloriesSn=Float.parseFloat(sn_cal);
                    totalProteinsSn=Float.parseFloat(sn_protein);
                    totalFatsSn=Float.parseFloat(sn_fat);
                    totalCarbsSn=Float.parseFloat(sn_carb);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        getTotalPerDayRef("TotalPerDay").setValue(Math.round( (br+ln+dn+sn)*10.0 ) / 10.0);
        getTotalPerDayRef("TotalProteinsPerDay").setValue(Math.round( (totalProteinsBr+totalProteinsLn+totalProteinsDn+totalProteinsSn)*10.0 ) / 10.0);
        getTotalPerDayRef("TotalFatsPerDay").setValue(Math.round( (totalFatsBr+totalFatsLn+totalFatsDn+totalFatsSn)*10.0 ) / 10.0);
        getTotalPerDayRef("TotalCarbsPerDay").setValue(Math.round( (totalCarbsBr+totalCarbsLn+totalCarbsDn+totalCarbsSn)*10.0 ) / 10.0);
        Query queryTotal= databaseTotalPerDayReference;
        queryTotal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String TotalPerDay=String.valueOf(ds.child("TotalPerDay").getValue());
                    String TotalProteinsPerDay=String.valueOf(ds.child("TotalProteinsPerDay").getValue());
                    String TotalFatsPerDay=String.valueOf(ds.child("TotalFatsPerDay").getValue());
                    String TotalCarbsPerDay=String.valueOf(ds.child("TotalCarbsPerDay").getValue());
                    cal_per_day.setText( TotalPerDay );
                    proteins_per_day.setText(TotalProteinsPerDay);
                    fats_per_day.setText(TotalFatsPerDay);
                    carbs_per_day.setText(TotalCarbsPerDay);
                    TotalCal=Float.parseFloat(String.valueOf(ds.child("TotalPerDay").getValue()));
                    TotalProteins=Float.parseFloat(String.valueOf(ds.child("TotalProteinsPerDay").getValue()));
                    TotalFats=Float.parseFloat(String.valueOf(ds.child("TotalFatsPerDay").getValue()));
                    TotalCarbs=Float.parseFloat(String.valueOf(ds.child("TotalCarbsPerDay").getValue()));
                    if (TotalFats>userFats) {
                        fats_progress.setProgress(100);
                        fats_progress.setIndicatorColor(color);
                    }
                     else
                     {
                    fats_progress.setProgress(Math.round((100 * (TotalFats)) / userFats));

                      }
                }
                if (TotalProteins>userProteins) {
                    proteins_progress.setProgress(100);
                    proteins_progress.setIndicatorColor(color);
                }
                else
                {
                    proteins_progress.setProgress(Math.round((100 * (TotalProteins)) / userProteins));

                }
                if (TotalCarbs>userCarbs) {
                    carbs_progress.setProgress(100);
                    carbs_progress.setIndicatorColor(color);
                }
                else
                {
                    carbs_progress.setProgress(Math.round((100 * (TotalCarbs)) / userCarbs));

                }
                if (TotalCal>userCal) {
                    cal_summary.setProgress(100);
                    cal_summary.setIndicatorColor(color);
                }
                else
                {
                    cal_summary.setProgress(Math.round((100 * (TotalCal)) / userCal));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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