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
import com.application.mybalancediary.R;
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
        final CircularProgressIndicator cal_summary=root.findViewById(R.id.circularProgressIndicator);
        final LinearProgressIndicator proteins_progress=root.findViewById(R.id.linearProgressIndicator);
        final LinearProgressIndicator fats_progress=root.findViewById(R.id.linearProgressIndicator2);
        final LinearProgressIndicator carbs_progress=root.findViewById(R.id.linearProgressIndicator3);
        final MaterialButton breakfast= root.findViewById(R.id.breakfast);
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
                    total_calories.setText(Calories);
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
                    String proteins = "of " + ds.child("proteins").getValue();
                    String carbs = "of " + ds.child("carbs").getValue();
                    String fats = "of " + ds.child("fats").getValue();
                    proteins_total.setText(proteins);
                    fats_total.setText(fats);
                    carbs_total.setText(carbs);
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
        return root;
    }
}