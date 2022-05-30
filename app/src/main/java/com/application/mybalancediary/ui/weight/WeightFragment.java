package com.application.mybalancediary.ui.weight;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.application.mybalancediary.AlertReceiverWeight;
import com.application.mybalancediary.R;
import com.application.mybalancediary.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.series.LineGraphSeries;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class WeightFragment extends Fragment {

    private Button btnInputWeight;
    private Button btnAlltime;
    private Button btnLast30;
    private Button btnLast7;
    private Button btnSettings;
    private Handler mainhandler = new Handler();
    String newWeight;

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
    LineGraphSeries<DataPoint> series, seriesGoal;
    GraphView graph;

    private TextView statCurWt;
    private TextView statNetWt;
    private TextView statGoalWt;
    public float currentw,ChangeWeight;
    List<Float> UserList = new ArrayList<Float>();
    Date date = new Date();
    String today= new SimpleDateFormat("yyyy-MM-dd").format(date);
   // String today="2022-05-1";



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        final View root = inflater.inflate(R.layout.fragment_weight_track, container, false);
        statCurWt=root.findViewById(R.id.statCurWt);
        statNetWt=root.findViewById(R.id.statNetWt);
        statGoalWt=root.findViewById(R.id.statGoalWt);
        graph = root.findViewById(R.id.graph);
        graph.setBackgroundColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Weight,kg");

        FirebaseDatabase.getInstance().getReference().child("Input_Weight").child(today)
                .orderByKey().equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            try{
                                newWeight = String.valueOf(ds.child("New_Weight").getValue());
                                statNetWt.setText(newWeight);
                            }
                            catch(Exception error) {}
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance().getReference("Input_Weight")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot parentDS : dataSnapshot.getChildren()) {
                    Iterator<DataSnapshot> items = parentDS.getChildren().iterator();
                    int counter = 0;
                    while (items.hasNext()) {
                        DataSnapshot item = items.next();
                        String total = item.child("New_Weight").getValue().toString();
                        UserList.add(Float.parseFloat(total));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        FirebaseDatabase.getInstance().getReference().child("Input_Weight").child(today)
                .orderByKey().equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            try{
                                newWeight = String.valueOf(ds.child("New_Weight").getValue());
                                statNetWt.setText(newWeight);
                            }
                            catch(Exception error) {}
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        FirebaseDatabase.getInstance().getReference("Users").orderByChild("email")
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            currentw = Float.valueOf(String.valueOf(ds.child("weight").getValue()));
                            statCurWt.setText(String.valueOf(currentw));


                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        FirebaseDatabase.getInstance().getReference().child("Weight")
                .orderByKey().equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String goal_date = String.valueOf(ds.child("Goal_Date").getValue());
                    String goal_weight = String.valueOf(ds.child("Goal_Weight").getValue());
                    statGoalWt.setText(goal_weight);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnAlltime=root.findViewById((R.id.buttonAllTime));
        btnAlltime.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (UserList.size() < 1) {
                    btnAlltime.setEnabled(false);
                    Toast.makeText(getActivity(), "You don't spend enough time here ", Toast.LENGTH_SHORT).show();

                } else {
                    graph.removeAllSeries();
                    graph.setBackgroundColor(Color.WHITE);
                    graph.getGridLabelRenderer().setHorizontalAxisTitle("All time");
                    graph.getGridLabelRenderer().setVerticalAxisTitle("Weight,kg");
                    graph.getGridLabelRenderer().setTextSize(30);
                    graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                    graph.getGridLabelRenderer().setGridColor(Color.BLUE);
                    graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLUE);
                    graph.getGridLabelRenderer().setVerticalLabelsColor(Color.BLUE);
                    graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);
                    graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
                    series = new LineGraphSeries<DataPoint>();
                    series.setColor(Color.BLUE);
                    series.setDrawDataPoints(true);
                    series.setDrawBackground(true);
                    series.setThickness(10);

                    seriesGoal = new LineGraphSeries<DataPoint>();
                    seriesGoal.setColor(Color.GREEN);
                    seriesGoal.setDrawDataPoints(true);
                    seriesGoal.setDrawBackground(true);
                    seriesGoal.setThickness(8);

                    List<Float> dataFloat = new ArrayList<>();
                    dataFloat.clear();
                    dataFloat.addAll(UserList);
                    int number_of_values_in_the_graph=dataFloat.size()-1;
                    for (int i = 2;i<number_of_values_in_the_graph+2;i++) {
                        int index=dataFloat.size()-i;

                        float y = dataFloat.get(index);
                        series.appendData(new DataPoint(i+1, y), false, 10);
                    }
                    graph.addSeries(series);

                    for (int i = 2;i<number_of_values_in_the_graph+2;i++) {

                        seriesGoal.appendData(new DataPoint(i+1, currentw), false, 10);
                    }
                    graph.addSeries(seriesGoal);

                    graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                        @Override
                        public String formatLabel(double value, boolean isValueX) {
                            if (isValueX) {
                                return format.format(new Date((long) value));
                            } else {
                                return super.formatLabel(value, isValueX);
                            }
                        }
                    });
                }
            }
        });

        btnLast30=root.findViewById(R.id.buttonLast30);
        btnLast30.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (UserList.size() < 32) {
                    btnLast30.setEnabled(false);
                    Toast.makeText(getActivity(), "You don't spend enough time here ", Toast.LENGTH_SHORT).show();


                } else {
                    graph.removeAllSeries();
                    graph.setBackgroundColor(Color.WHITE);
                    graph.getGridLabelRenderer().setHorizontalAxisTitle("The last 30 days");
                    graph.getGridLabelRenderer().setVerticalAxisTitle("Weight,kg");
                    graph.getGridLabelRenderer().setTextSize(30);
                    graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                    graph.getGridLabelRenderer().setGridColor(Color.BLUE);
                    graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLUE);
                    graph.getGridLabelRenderer().setVerticalLabelsColor(Color.BLUE);
                    graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);
                    graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
                    series = new LineGraphSeries<DataPoint>();
                    series.setColor(Color.BLUE);
                    series.setDrawDataPoints(true);
                    series.setDrawBackground(true);
                    series.setThickness(10);

                    seriesGoal = new LineGraphSeries<DataPoint>();
                    seriesGoal.setColor(Color.GREEN);
                    seriesGoal.setDrawDataPoints(true);
                    seriesGoal.setDrawBackground(true);
                    seriesGoal.setThickness(8);

                    List<Float> dataFloat = new ArrayList<>();
                    dataFloat.clear();
                    dataFloat.addAll(UserList);
                    int number_of_values_in_the_graph=30;
                    for (int i = 2;i<number_of_values_in_the_graph+2;i++) {
                        int index=dataFloat.size()-i;

                        float y = dataFloat.get(index);
                        series.appendData(new DataPoint(i+1, y), false, 7);
                    }
                    graph.addSeries(series);

                    for (int i = 2;i<number_of_values_in_the_graph+2;i++) {

                        seriesGoal.appendData(new DataPoint(i+1, currentw), false, 7);
                    }
                    graph.addSeries(seriesGoal);

                    graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                        @Override
                        public String formatLabel(double value, boolean isValueX) {
                            if (isValueX) {
                                return format.format(new Date((long) value));
                            } else {
                                return super.formatLabel(value, isValueX);
                            }
                        }
                    });
                }
            }
        });

        btnLast7=root.findViewById(R.id.buttonLast7);
        btnLast7.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (UserList.size() < 8) {
                    btnLast7.setEnabled(false);
                    Toast.makeText(getActivity(), "You don't spend enough time here ", Toast.LENGTH_SHORT).show();


                } else {
                  graph.removeAllSeries();
                    graph.setBackgroundColor(Color.WHITE);
                    graph.getGridLabelRenderer().setHorizontalAxisTitle("The last 7 days");
                    graph.getGridLabelRenderer().setVerticalAxisTitle("Weight,kg");
                    graph.getGridLabelRenderer().setTextSize(30);
                    graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                    graph.getGridLabelRenderer().setGridColor(Color.BLUE);
                    graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLUE);
                    graph.getGridLabelRenderer().setVerticalLabelsColor(Color.BLUE);
                    graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);
                    graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
                    series = new LineGraphSeries<DataPoint>();
                    series.setColor(Color.BLUE);
                    series.setDrawDataPoints(true);
                    series.setDrawBackground(true);
                    series.setThickness(10);

                    seriesGoal = new LineGraphSeries<DataPoint>();
                    seriesGoal.setColor(Color.GREEN);
                    seriesGoal.setDrawDataPoints(true);
                    seriesGoal.setDrawBackground(true);
                    seriesGoal.setThickness(8);

                    List<Float> dataFloat = new ArrayList<>();
                    dataFloat.clear();
                    dataFloat.addAll(UserList);
                    int number_of_values_in_the_graph=7;
                    for (int i = 2;i<number_of_values_in_the_graph+2;i++) {
                        int index=dataFloat.size()-i;

                        float y = dataFloat.get(index);
                        series.appendData(new DataPoint(i+1, y), false, 7);
                    }
                    graph.addSeries(series);

                    for (int i = 2;i<number_of_values_in_the_graph+2;i++) {

                        seriesGoal.appendData(new DataPoint(i+1, currentw), false, 7);
                    }
                    graph.addSeries(seriesGoal);

                    graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                        @Override
                        public String formatLabel(double value, boolean isValueX) {
                            if (isValueX) {
                                return format.format(new Date((long) value));
                            } else {
                                return super.formatLabel(value, isValueX);
                            }
                        }
                    });
                }
            }
        });

        btnSettings=root.findViewById(R.id.buttonSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWeightFragment_Setting();
            }
        });

        btnInputWeight=root.findViewById(R.id.buttonInputWeight);
        btnInputWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(statGoalWt.length()==0)
                {
                }
                else
                {
                    openActivity_InputWeightDate();
                }
            }
        });


        return root;
    }

    private void openActivity_InputWeightDate() {
        Intent intent =  new Intent(getContext(), InputWeightDateActivity.class);
        startActivity(intent);
    }

    private void openWeightFragment_Setting() {
        Intent intent = new Intent(getContext(),WeightFragmentSetting.class);
        startActivityForResult(intent, 2);
    }


    private void createNotificationChannel()
    {
        CharSequence name = "weightID";
        String desc = "My Balance Diary";
        int important = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("weightID", name, important);
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
                    Intent intent = new Intent(getActivity(), AlertReceiverWeight.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent,PendingIntent.FLAG_MUTABLE);
                    long timeAtButtonClick = System.currentTimeMillis();
                    long timeSendsInMills = 1000*5;
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