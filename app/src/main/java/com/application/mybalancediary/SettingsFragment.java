package com.application.mybalancediary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SettingsFragment extends PreferenceFragment {

    DatabaseReference databaseReference;
    private FirebaseAuth mAuth ;
    FirebaseUser currentUser;
    FirebaseAuth fAuth;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.root_preferences);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String user_name = prefs.getString("user_name", "");
        String weight_kg = prefs.getString("weight_kg", "");
        String height_kg = prefs.getString("height_kg", "");
        String cycle_length=prefs.getString("cycle_length","");
        String period_length=prefs.getString("period_length","");
        String change_email=prefs.getString("change_email","");

        updateProfile(user_name,weight_kg,height_kg,change_email);
    }
public void updateProfile(String _user_name,String _weight_kg,String _height_kg,String _change_email)
{
    String userID = currentUser.getUid();
    databaseReference = FirebaseDatabase.getInstance().getReference("Users");
    databaseReference.child(userID).child("name").setValue(_user_name);
    databaseReference.child(userID).child("weight").setValue(_weight_kg);
    databaseReference.child(userID).child("height").setValue(_height_kg);
    databaseReference.child(userID).child("email").setValue(_change_email);


}
}

// boolean period_tracker = preferences.getBoolean("period_tracking", false);
//  boolean water_tracker = preferences.getBoolean("water_tracking", false);
