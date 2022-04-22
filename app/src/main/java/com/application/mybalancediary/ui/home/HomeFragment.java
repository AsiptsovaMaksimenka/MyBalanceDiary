package com.application.mybalancediary.ui.home;

import static java.lang.Float.parseFloat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
    public Float totalProteinsSn=0.0f,totalFatsSn=0.0f,totalCarbsSn=0.0f;
    public Float totalProteinsBr=0.0f,totalFatsBr=0.0f,totalCarbsBr=0.0f;
    public Float totalProteinsLn=0.0f,totalFatsLn=0.0f,totalCarbsLn=0.0f;
    public Float totalProteinsDn=0.0f,totalFatsDn=0.0f,totalCarbsDn=0.0f;
    public Float br=0.0f, ln=0.0f, dn=0.0f, sn=0.0f;
    public Float userCal=0.0f, userProteins=0.0f,userFats=0.0f,userCarbs=0.0f;
    public Float TotalCal=0.0f, TotalProteins=0.0f,TotalFats=0.0f,TotalCarbs=0.0f;
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
        getTotalPerDayRef("TotalPerDay").setValue(Math.round( (br+ln+dn+sn)*10.0 ) / 10.0);
        getTotalPerDayRef("TotalProteinsPerDay").setValue(Math.round( (totalProteinsBr+totalProteinsLn+totalProteinsDn+totalProteinsSn)*10.0 ) / 10.0);
        getTotalPerDayRef("TotalFatsPerDay").setValue(Math.round( (totalFatsBr+totalFatsLn+totalFatsDn+totalFatsSn)*10.0 ) / 10.0);
        getTotalPerDayRef("TotalCarbsPerDay").setValue(Math.round( (totalCarbsBr+totalCarbsLn+totalCarbsDn+totalCarbsSn)*10.0 ) / 10.0);
        Query queryUser = databaseReference;
        queryUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String proteins = "/ " + ds.child("proteins").getValue();
                    String carbs = "/ " + ds.child("carbs").getValue();
                    String fats = "/ " + ds.child("fats").getValue();
                    total_calories.setText(String.valueOf(ds.child("bmr").getValue()));
                    proteins_total.setText(proteins);
                    fats_total.setText(fats);
                    carbs_total.setText(carbs);
                    userCal= parseFloat(String.valueOf(ds.child("bmr").getValue()));
                    userProteins= parseFloat(String.valueOf(ds.child("proteins").getValue()));
                    userFats= parseFloat(String.valueOf(ds.child("fats").getValue()));
                    userCarbs= parseFloat(String.valueOf(ds.child("carbs").getValue()));

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
                    br= parseFloat(String.valueOf(ds.child("total").getValue()));
                    totalProteinsBr= parseFloat(String.valueOf(ds.child("totalfats").getValue()));
                    totalFatsBr= parseFloat(String.valueOf(ds.child("totalfats").getValue()));
                    totalCarbsBr= parseFloat(String.valueOf(ds.child("totalcarbs").getValue()));
                    breakfast_cal.setText(String.valueOf(ds.child("total").getValue()));
                    breakfast_prot.setText(String.valueOf(ds.child("totalprotein").getValue()));
                    breakfast_fats.setText(String.valueOf(ds.child("totalfats").getValue()));
                    breakfast_carbs.setText(String.valueOf(ds.child("totalcarbs").getValue()));

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
                    ln= parseFloat(String.valueOf(ds.child("total").getValue()));
                    totalProteinsLn= parseFloat(String.valueOf(ds.child("totalprotein").getValue()));
                    totalFatsLn= parseFloat(String.valueOf(ds.child("totalfats").getValue()));
                    totalCarbsLn= parseFloat(String.valueOf(ds.child("totalcarbs").getValue()));
                    lunch_cal.setText(String.valueOf(ds.child("total").getValue()));
                    lunch_prot.setText(String.valueOf(ds.child("totalprotein").getValue()));
                    lunch_fats.setText(String.valueOf(ds.child("totalfats").getValue()));
                    lunch_carbs.setText(String.valueOf(ds.child("totalcarbs").getValue()));

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
                    dn= parseFloat(String.valueOf(ds.child("total").getValue()));
                    totalProteinsDn= Float.parseFloat(String.valueOf(ds.child("totalprotein").getValue()));
                    totalFatsDn= parseFloat(String.valueOf(ds.child("totalfats").getValue()));
                    totalCarbsDn= parseFloat(String.valueOf(ds.child("totalcarbs").getValue()));
                    dinner_cal.setText(String.valueOf(ds.child("total").getValue()));
                    dinner_prot.setText(String.valueOf(ds.child("totalprotein").getValue()));
                    dinner_fats.setText(String.valueOf(ds.child("totalfats").getValue()));
                    dinner_carbs.setText(String.valueOf(ds.child("totalcarbs").getValue()));

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
                    sn= parseFloat(String.valueOf(ds.child("total").getValue()));
                    totalProteinsSn= parseFloat(String.valueOf(ds.child("totalprotein").getValue()));
                    totalFatsSn= parseFloat(String.valueOf(ds.child("totalfats").getValue()));
                    totalCarbsSn= parseFloat(String.valueOf(ds.child("totalcarbs").getValue()));
                    snack_cal.setText(String.valueOf(ds.child("total").getValue()));
                    snack_prot.setText(String.valueOf(ds.child("totalprotein").getValue()));
                    snack_fats.setText(String.valueOf(ds.child("totalfats").getValue()));
                    snack_carbs.setText(String.valueOf(ds.child("totalcarbs").getValue()));

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query queryTotal= databaseTotalPerDayReference;
        queryTotal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    cal_per_day.setText( String.valueOf(ds.child("TotalPerDay").getValue()) );
                    proteins_per_day.setText(String.valueOf(ds.child("TotalProteinsPerDay").getValue()));
                    fats_per_day.setText(String.valueOf(ds.child("TotalFatsPerDay").getValue()));
                    carbs_per_day.setText(String.valueOf(ds.child("TotalCarbsPerDay").getValue()));
                    TotalCal= parseFloat(String.valueOf(ds.child("TotalPerDay").getValue()));
                    TotalProteins= parseFloat(String.valueOf(ds.child("TotalProteinsPerDay").getValue()));
                    TotalFats= parseFloat(String.valueOf(ds.child("TotalFatsPerDay").getValue()));
                    TotalCarbs= parseFloat(String.valueOf(ds.child("TotalCarbsPerDay").getValue()));
                    if (TotalFats>userFats) {
                        fats_progress.setProgress(100);
                        fats_progress.setIndicatorColor(0xFFFF0000);
                    }
                     else
                     {
                    fats_progress.setProgress(Math.round((100 * (TotalFats)) / userFats));

                      }
                }
                if (TotalProteins>userProteins) {
                    proteins_progress.setProgress(100);
                    proteins_progress.setIndicatorColor(0xFFFF0000);
                }
                else
                {
                    proteins_progress.setProgress(Math.round((100 * (TotalProteins)) / userProteins));

                }
                if (TotalCarbs>userCarbs) {
                    carbs_progress.setProgress(100);
                    carbs_progress.setIndicatorColor(0xFFFF0000);
                }
                else
                {
                    carbs_progress.setProgress(Math.round((100 * (TotalCarbs)) / userCarbs));

                }
                if (TotalCal>userCal) {
                    cal_summary.setProgress(100);
                    cal_summary.setIndicatorColor(0xFFFF0000);
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