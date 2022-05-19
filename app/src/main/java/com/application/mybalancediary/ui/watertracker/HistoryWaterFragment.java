package com.application.mybalancediary.ui.watertracker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.application.mybalancediary.LoginActivity;
import com.application.mybalancediary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class HistoryWaterFragment extends Fragment {
    LineGraphSeries<DataPoint> series;
    GraphView graph;
    Button btnLast30,btnLast7,btnAll;
    Float AllWater=0.0f,waterDaily=0.0f;
    int count=0;
    List<Float> UserList = new ArrayList<Float>();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.activity_history_water_fragment, container, false);
        graph = root.findViewById(R.id.graph);
        btnLast7 =root.findViewById(R.id.buttonLast7);
        btnLast30=root.findViewById(R.id.buttonLast30);
        btnAll=root.findViewById(R.id.buttonAll);
        graph.setBackgroundColor(Color.WHITE);
        TextView summary=root.findViewById(R.id.WaterSummary);
        TextView percent=root.findViewById(R.id.WaterPercent);
        FirebaseDatabase.getInstance().getReference("Water").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot parentDS : dataSnapshot.getChildren()) {
                    Iterator<DataSnapshot> items = parentDS.getChildren().iterator();
                    int counter = 0;
                    while (items.hasNext()) {
                        DataSnapshot item = items.next();
                        String total = item.child("Water").getValue().toString();
                        UserList.add(Float.parseFloat(total));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        FirebaseDatabase.getInstance().getReference("Users").orderByChild("email")
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            waterDaily = Float.valueOf(String.valueOf(ds.child("TargetWater").getValue()));
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            btnLast7.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    if (UserList.size() < 7) {
                        btnLast7.setEnabled(false);
                        Toast.makeText(getActivity(), "You don't spend enough time here ", Toast.LENGTH_SHORT).show();
                        summary.setText(" ");
                        percent.setText("");

                    } else {
                        root.findViewById(R.id.achive).setVisibility(View.INVISIBLE);
                        graph.removeAllSeries();
                        graph.setBackgroundColor(Color.WHITE);
                        graph.getGridLabelRenderer().setHorizontalAxisTitle("The last 7 days");
                        graph.getGridLabelRenderer().setVerticalAxisTitle("Water,ml");
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
                        String[] arr = {"1", "2", "3", "4", "5", "6", "7"};
                        List<String> dataString = Arrays.asList(arr);
                        List<Float> dataFloat = new ArrayList<>();
                        dataFloat.clear();
                        AllWater = 0.0f;
                        dataFloat.addAll(UserList);
                        for (int i = 0; i < 7; i++) {
                            AllWater += dataFloat.get(i);
                            String x = dataString.get(i);
                            float y = dataFloat.get(i);
                            series.appendData(new DataPoint(Double.parseDouble(x), y), false, 7);
                        }
                        summary.setText("You drank " + String.valueOf(AllWater)+" ml of water out of " +String.valueOf(waterDaily*7)+"ml");
                        percent.setText("This is about "+ String.valueOf((AllWater*100)/(waterDaily*7))+" % of the norm");
                        if(AllWater>=waterDaily)
                            root.findViewById(R.id.achive).setVisibility(View.VISIBLE);
                        graph.addSeries(series);
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


            btnAll.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    if (UserList.size() < 1) {
                        btnAll.setEnabled(false);
                        Toast.makeText(getActivity(), "You don't spend enough time here ", Toast.LENGTH_SHORT).show();
                        summary.setText(" ");
                        percent.setText("");

                    } else {
                        root.findViewById(R.id.achive).setVisibility(View.INVISIBLE);
                        graph.removeAllSeries();
                        graph.setBackgroundColor(Color.WHITE);
                        graph.getGridLabelRenderer().setHorizontalAxisTitle("All time");
                        graph.getGridLabelRenderer().setVerticalAxisTitle("Water,ml");
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
                        String[] arr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
                        List<String> dataString = Arrays.asList(arr);
                        List<Float> dataFloat = new ArrayList<>();
                        dataFloat.clear();
                        AllWater = 0.0f;
                        count=0;
                        dataFloat.addAll(UserList);
                        for (int i = 0; i < dataFloat.size(); i++) {
                            AllWater += dataFloat.get(i);
                            count++;
                            String x = dataString.get(i);
                            float y = dataFloat.get(i);
                            series.appendData(new DataPoint(Double.parseDouble(x), y), true, 10);
                        }
                        summary.setText("You drank " + String.valueOf(AllWater)+" ml of water out of " +String.valueOf(waterDaily*count)+"ml");
                        percent.setText("This is about "+ String.valueOf((AllWater*100)/(waterDaily*count))+" % of the norm");
                        if(AllWater>=waterDaily)
                            root.findViewById(R.id.achive).setVisibility(View.VISIBLE);
                        graph.addSeries(series);
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

        btnLast30.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (UserList.size() < 30) {
                    btnLast30.setEnabled(false);
                    Toast.makeText(getActivity(), "You don't spend enough time here ", Toast.LENGTH_SHORT).show();
                    summary.setText(" ");
                    percent.setText("");

                } else {
                    root.findViewById(R.id.achive).setVisibility(View.INVISIBLE);
                    graph.removeAllSeries();
                    graph.setBackgroundColor(Color.WHITE);
                    graph.getGridLabelRenderer().setHorizontalAxisTitle("The last 30 days");
                    graph.getGridLabelRenderer().setVerticalAxisTitle("Water,ml");
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
                    String[] arr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
                    List<String> dataString = Arrays.asList(arr);
                    List<Float> dataFloat = new ArrayList<>();
                    dataFloat.clear();
                    AllWater = 0.0f;
                    count=0;
                    dataFloat.addAll(UserList);
                    for (int i = 0; i < 30; i++) {
                        AllWater += dataFloat.get(i);
                        String x = dataString.get(i);
                        float y = dataFloat.get(i);
                        summary.setText("You drank " + String.valueOf(AllWater)+" ml of water out of " +String.valueOf(waterDaily)+"ml");
                        percent.setText("This is about "+ String.valueOf((AllWater*100)/(waterDaily*30))+" % of the norm");
                        if(AllWater>=waterDaily)
                            root.findViewById(R.id.achive).setVisibility(View.VISIBLE);
                        series.appendData(new DataPoint(Double.parseDouble(x), y), true, 30);
                    }
                    graph.addSeries(series);
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



        return root;
    }
}
