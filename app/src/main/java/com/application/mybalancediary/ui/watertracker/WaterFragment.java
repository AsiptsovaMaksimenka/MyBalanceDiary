package com.application.mybalancediary.ui.watertracker;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.application.mybalancediary.AlertReceiver;
import com.application.mybalancediary.R;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;


public class WaterFragment extends Fragment {
    private Handler mainhandler = new Handler();
    public  Vector<String> water_time = new Vector<String>(20);
    ArrayAdapter vectorAdapter;
    private ListView  listView;
    public String totalMl, Time;
    public static int count = 0,counter_cups=0;
    public static String time, ampm,total;
    Date date = new Date();
    @SuppressLint("SimpleDateFormat")
    String today= new SimpleDateFormat("yyyy-MM-dd").format(date);
    //String today="2022-05-9";
    private DatabaseReference getWaterRef(String ref) {
        return FirebaseDatabase.getInstance().getReference("Water").
                child(today).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ref);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.activity_water_fragment, container, false);
        new Thread(new LabelRunnable()).start();
        final TextView textView_total = root.findViewById(R.id.totalml);
        final TextView textView_tofill = root.findViewById(R.id.tofillml);
        final CircularProgressIndicator  water_progress=root.findViewById(R.id.waveloadingview);
        final TextView cups_text=root.findViewById(R.id.cups);
        root.findViewById(R.id.achive).setVisibility(View.INVISIBLE);
        listView = root.findViewById(R.id.listview_record);
        FirebaseDatabase.getInstance().getReference("Users")
                .orderByChild("email")
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .addValueEventListener(new ValueEventListener() {
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
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                counter_cups+=1;
                count = count + 250;
                getWaterRef("Water").setValue(count);
                getWaterRef("Cups").setValue(counter_cups);
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);
                if (currentHour >= 12)
                    ampm = "PM";
                else
                    ampm = "AM";
                time = (String.format("%02d:%02d", currentHour, currentMinute) + ampm + " - 1 cup");
                Time+=(time+',');
                getWaterRef("Time").setValue(Time);
            }
        });
        FirebaseDatabase.getInstance().getReference("Water").child(today)
                .orderByKey().equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String water = String.valueOf(ds.child("Water").getValue());
                    String cups = String.valueOf(ds.child("Cups").getValue());
                    cups_text.setText(cups);
                    textView_tofill.setText(water);
                    water_progress.setProgress(Math.round((100 * (Integer.parseInt(water)) / Integer.parseInt(totalMl))));
                    if(Integer.parseInt(textView_tofill.getText().toString())>=Integer.parseInt(textView_total.getText().toString()))
                        root.findViewById(R.id.achive).setVisibility(View.VISIBLE);
                    Time=String.valueOf(ds.child("Time").getValue());
                    count=Integer.parseInt(String.valueOf(ds.child("Water").getValue()));
                    String[] strArr = Time.split(",");
                    Vector<String> tmp=new Vector<String>();
                    water_time.clear();
                    for(String str:strArr)
                        if(str!="")
                            water_time.add(str);
                    vectorAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, water_time);
                    listView.setAdapter(vectorAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                water_time.remove(position);
                String newTimeInDB="";
                for(String str: water_time)
                    newTimeInDB+=str+",";
                getWaterRef("Time").setValue(newTimeInDB);
                vectorAdapter.notifyDataSetChanged();
                counter_cups--;
                getWaterRef("Cups").setValue(counter_cups);
                cups_text.setText(String.valueOf(counter_cups));
                count-=250;
                getWaterRef("Water").setValue(count);
                textView_tofill.setText((String.valueOf(count)));
                water_progress.setProgress(Math.round((100 * (count)) / Integer.parseInt(totalMl)));
                if(Integer.parseInt(textView_tofill.getText().toString())>=Integer.parseInt(textView_total.getText().toString()))
                    root.findViewById(R.id.achive).setVisibility(View.VISIBLE);
                vectorAdapter.notifyDataSetChanged();
                return true;
            }
        });

        return root;
    }
    private void createNotificationChannel()
    {
        CharSequence name = "channel";
        String desc = "My Balance Diary";
        int important = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("channelID", name, important);
        channel.setDescription(desc);

        NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

    }
    class LabelRunnable implements Runnable {
        @Override
        public void run() {
            mainhandler.post(new Runnable() {
                @Override
                public void run() {
                    createNotificationChannel();
                    Intent intent = new Intent(getActivity(), AlertReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent,PendingIntent.FLAG_MUTABLE);
                    long timeAtButtonClick = System.currentTimeMillis();
                    long timeSendsInMills = 1000*10;
                    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtButtonClick + timeSendsInMills, pendingIntent);
                }
            });
            try {
                Thread.sleep(7200000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}