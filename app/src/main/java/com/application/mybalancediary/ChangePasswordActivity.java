package com.application.mybalancediary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ChangePasswordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_change_password);
        final EditText email = findViewById(R.id.current_email);
        final EditText current_password = findViewById(R.id.current_password);
        final EditText new_password = findViewById(R.id.password_new);
        final EditText new_password_repeat = findViewById(R.id.password_new_repeat);
        Button save = findViewById(R.id.Save_password);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    AuthCredential credential = EmailAuthProvider.getCredential(email.getText().toString().trim(), current_password.getText().toString().trim());
                    FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        if (new_password.getText().toString().trim().equals(new_password_repeat.getText().toString().trim())) {
                                            FirebaseAuth.getInstance().getCurrentUser().updatePassword(new_password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(ChangePasswordActivity.this, "Update ", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(ChangePasswordActivity.this, SettingsFragment.class));
                                                    } else {
                                                        Toast.makeText(ChangePasswordActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                }
            }
        });
    }

}