package com.application.mybalancediary.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

import com.application.mybalancediary.R;


public class ProfileFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    Button edit_profile;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_profile, container, false);
        final TextView name = root.findViewById(R.id.user_name);
        final TextView user_age = root.findViewById(R.id.user_age);
        final TextView user_gender = root.findViewById(R.id.user_gender);
        final TextView user_bmi = root.findViewById(R.id.user_bmi);
        final TextView weight = root.findViewById(R.id.weight);
        final TextView height = root.findViewById(R.id.height);
        final TextView calories = root.findViewById(R.id.calories);
        edit_profile = root.findViewById(R.id.edit_profile);
        final TextView edit_goal = root.findViewById(R.id.goals);
        final TextView edit_workout = root.findViewById(R.id.Workout);

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();
        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String Name = "" + ds.child("name").getValue();
                    String age = "" + ds.child("age").getValue();
                    String gender = "" + ds.child("gender").getValue();
                    String Weight = "" + ds.child("weight").getValue();
                    String Height = "" + ds.child("height").getValue();
                    String BMI = "" + ds.child("bmi").getValue();
                    String Calories = "" + ds.child("bmr").getValue();
                    String Work = "" + ds.child("workout").getValue();
                    String Goal = "" + ds.child("goals").getValue();
                    name.setText(Name);
                    user_age.setText(age);
                    weight.setText(Weight);
                    height.setText(Height);
                    user_gender.setText(gender);
                    user_bmi.setText(BMI);
                    calories.setText(Calories);
                    edit_goal.setText(Goal);
                    edit_workout.setText(Work);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return root;
    }
}

