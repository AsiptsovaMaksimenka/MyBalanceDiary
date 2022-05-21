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

import com.application.mybalancediary.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class InputWeightDateActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private TextView textDateBox;
    private TextView textWeightBox;
    private TextView textTimeBox;
    private Button btnLogEntry;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_input_weight);


        intent = getIntent();



        btnLogEntry = (Button) findViewById(R.id.buttonSubmitWeightDate);
        btnLogEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dateString = textDateBox.getText().toString();
                String weightString = textWeightBox.getText().toString();
                String timeString = textTimeBox.getText().toString();
                if(dateString.length() == 0 || weightString.length() == 0 || timeString.length() == 0){
                    openSubmitAlertDialog();
                }
                else{
                    float weight = Float.parseFloat(textWeightBox.getText().toString());
                    int time = Integer.parseInt(textTimeBox.getText().toString());
                    SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
                    Date dateObj = new Date(textDateBox.getText().toString());
                    String formattedDate = simple.format(dateObj);


                    Log.d("outputformat",formattedDate);
                    Log.d("outputlogentry",("@START@" + dateString+ "@" + weight + "@" + time + "@END@"));
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
