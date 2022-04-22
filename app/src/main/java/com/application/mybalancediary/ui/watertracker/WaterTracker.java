package com.application.mybalancediary.ui.watertracker;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.application.mybalancediary.R;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Vector;


public class WaterTracker extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
   public  Vector<String> water_time = new Vector<String>(20);
    ArrayAdapter vectorAdapter;
    private ListView  listView;
    public String totalMl;
    int count = 0;
    int counter_cups=0;
    public static String time, ampm;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_water_tracker, container, false);
        final TextView textView_total = root.findViewById(R.id.totalml);
        final TextView textView_tofill = root.findViewById(R.id.tofillml);
        final CircularProgressIndicator  water_progress=root.findViewById(R.id.waveloadingview);
        root.findViewById(R.id.achive).setVisibility(View.INVISIBLE);
        listView = root.findViewById(R.id.listview_record);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        Query query = databaseReference;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    totalMl =String.valueOf(ds.child("TargetWater").getValue());
                    textView_total.setText(totalMl);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        root.findViewById(R.id.glass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter_cups+=1;
                count = count + 250;

                textView_tofill.setText((String.valueOf(count)));
                water_progress.setProgress(Math.round((100 * (count)) / Integer.parseInt(totalMl)));
                if(Integer.parseInt(textView_tofill.getText().toString())>=Integer.parseInt(textView_total.getText().toString()))
                {
                    root.findViewById(R.id.achive).setVisibility(View.VISIBLE);
                }
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);
                if (currentHour >= 12) {
                    ampm = "PM";
                } else {
                    ampm = "AM";
                }
                time = (String.format("%02d:%02d",currentHour, currentMinute)+ ampm+" - 1 cup");
                water_time.add(time);
                vectorAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, water_time);
                listView.setAdapter(vectorAdapter);
                listView.invalidateViews();
                listView.refreshDrawableState();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                water_time.remove(position);
                vectorAdapter.notifyDataSetChanged();
                count-=250;
                textView_tofill.setText((String.valueOf(count)));
                water_progress.setProgress(Math.round((100 * (count)) / Integer.parseInt(totalMl)));
                if(Integer.parseInt(textView_tofill.getText().toString())>=Integer.parseInt(textView_total.getText().toString()))
                {
                    root.findViewById(R.id.achive).setVisibility(View.VISIBLE);
                }
                vectorAdapter.notifyDataSetChanged();
                return true;
            }
        });

        return root;
    }



}