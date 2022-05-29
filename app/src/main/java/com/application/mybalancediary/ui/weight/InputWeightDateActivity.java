package com.application.mybalancediary.ui.weight;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.application.mybalancediary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class InputWeightDateActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private TextView textDateBox;
    private TextView textWeightBox;
    private TextView textTimeBox;
    private Button btnLogEntry;
    Date date = new Date();
   String today= new SimpleDateFormat("yyyy-MM-dd").format(date);
  //  String today="2022-05-1";

    private DatabaseReference getWeightRef(String ref) {
        return FirebaseDatabase.getInstance().getReference().child("Input_Weight")
                .child(today)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ref);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_input_weight);

        btnLogEntry = (Button) findViewById(R.id.buttonSubmitWeightDate);
        btnLogEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dateString = textDateBox.getText().toString();
                String weightString = textWeightBox.getText().toString();
                if(dateString.length() == 0 || weightString.length() == 0 ){
                    openSubmitAlertDialog();
                }
                else{
                    float weight = Float.parseFloat(textWeightBox.getText().toString());
                    SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
                    Date dateObj = new Date(textDateBox.getText().toString());
                    String formattedDate = simple.format(dateObj);
                    getWeightRef("New_Weight").setValue(Math.round(weight*10.0)/10.0);
                    getWeightRef("New_Date").setValue(formattedDate);
                    Intent intent = new Intent();
                    intent.putExtra("goal" , weight);
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });


        textDateBox = (TextView) findViewById(R.id.dateBox);
        textDateBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragmentObj = new DatePickerFragment();
                datePickerFragmentObj.show(getSupportFragmentManager(),"date picker frag tag");
            }
        });

        textWeightBox = (TextView) findViewById(R.id.weightBox);
        textWeightBox.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        textTimeBox = (TextView) findViewById(R.id.dateBox);
        textTimeBox.setInputType(InputType.TYPE_CLASS_NUMBER);

    }

    private void openSubmitAlertDialog() {
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,month);
        cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        String currentDateString = DateFormat.getDateInstance().format(cal.getTime());
        textDateBox = findViewById(R.id.dateBox);
        textDateBox.setText(currentDateString);
    }
}
