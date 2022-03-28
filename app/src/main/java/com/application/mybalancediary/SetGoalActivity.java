package com.application.mybalancediary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SetGoalActivity extends AppCompatActivity {

    public static float mSeries1 = 0f;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setgoal);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = mAuth.getCurrentUser();
        final String userId = user.getUid();

        final EditText calorieGoal = findViewById(R.id.et2);

        Button setgoal = findViewById(R.id.setgoal);
        setgoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (calorieGoal.getText().toString().length() == 0) {
                    calorieGoal.setError("Set Calorie Goal!");
                    return;
                }


                mDatabase.child("Users").child(userId).child("caloriegoal").setValue(calorieGoal.getText().toString());
               mDatabase.child("Users").child(userId).child("caloriegoal").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mSeries1 = Float.parseFloat(String.valueOf(dataSnapshot.getValue()));
                        Log.d("mSeries1", (String.valueOf(mSeries1)));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                Intent myIntent = new Intent(SetGoalActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
