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
    LineGraphSeries<DataPoint> series,seriesGoal;
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
                    if (UserList.size() < 8) {
                        btnLast7.setEnabled(false);
                        Toast.makeText(getActivity(), "You don't spend enough time here ", Toast.LENGTH_SHORT).show();
                        root.findViewById(R.id.achive).setVisibility(View.INVISIBLE);
                        root.findViewById(R.id.green).setVisibility(View.INVISIBLE);
                        root.findViewById(R.id.blue).setVisibility(View.INVISIBLE);
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
                        seriesGoal = new LineGraphSeries<DataPoint>();
                        seriesGoal.setColor(Color.GREEN);
                        seriesGoal.setDrawDataPoints(true);
                        seriesGoal.setDrawBackground(true);
                        seriesGoal.setThickness(8);
                        List<Float> dataFloat = new ArrayList<>();
                        dataFloat.clear();
                        AllWater = 0.0f;
                        dataFloat.addAll(UserList);
                        int number_of_values_in_the_graph=7;
                        for (int i = 2;i<number_of_values_in_the_graph+2;i++) {
                            int index=dataFloat.size()-i;
                            AllWater += dataFloat.get(index);
                            float y = dataFloat.get(index);
                            series.appendData(new DataPoint(i+1, y), false, 7);
                        }
                        for (int i = 2;i<number_of_values_in_the_graph+2;i++) {
                            float y =waterDaily;
                            seriesGoal.appendData(new DataPoint(i+1, y), false, 7);
                        }
                        graph.addSeries(seriesGoal);

                        summary.setText("You drank " + String.valueOf(AllWater)+" ml of water out of " +String.valueOf(waterDaily*7)+"ml");
                        percent.setText("This is about "+ String.valueOf(Math.round(((AllWater*100)/(waterDaily*7))*10.0)/10.0)+" % of the norm");
                        if(AllWater>=(waterDaily*7))
                            root.findViewById(R.id.achive).setVisibility(View.VISIBLE);
                        graph.addSeries(series);
                        root.findViewById(R.id.green).setVisibility(View.VISIBLE);
                        root.findViewById(R.id.blue).setVisibility(View.VISIBLE);
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
                        root.findViewById(R.id.achive).setVisibility(View.INVISIBLE);
                        root.findViewById(R.id.green).setVisibility(View.INVISIBLE);
                        root.findViewById(R.id.blue).setVisibility(View.INVISIBLE);
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
                        seriesGoal = new LineGraphSeries<DataPoint>();
                        seriesGoal.setColor(Color.GREEN);
                        seriesGoal.setDrawDataPoints(true);
                        seriesGoal.setDrawBackground(true);
                        seriesGoal.setThickness(8);
                        List<Float> dataFloat = new ArrayList<>();
                        dataFloat.clear();
                        AllWater = 0.0f;
                        count=0;
                        dataFloat.addAll(UserList);
                        int number_of_values_if_the_graph=dataFloat.size()-1;
                        for (int i = 2; i <number_of_values_if_the_graph+2 ; i++) {
                            int index=dataFloat.size()-i;
                            AllWater += dataFloat.get(index);
                            float y = dataFloat.get(index);
                            count++;
                            series.appendData(new DataPoint(i+1, y), true, 10);
                        }
                        for (int i = 2;i<number_of_values_if_the_graph+2;i++) {
                            float y =waterDaily;
                            seriesGoal.appendData(new DataPoint(i+1, y), false, 10);
                        }
                        graph.addSeries(seriesGoal);
                        root.findViewById(R.id.green).setVisibility(View.VISIBLE);
                        root.findViewById(R.id.blue).setVisibility(View.VISIBLE);
                        summary.setText("You drank " + String.valueOf(AllWater)+" ml of water out of " +String.valueOf(waterDaily*count)+"ml");
                        percent.setText("This is about "+ String.valueOf(Math.round(((AllWater*100)/(waterDaily*count))*10.0)/10.0)+" % of the norm");
                        if(AllWater>=(waterDaily*count))
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
                    root.findViewById(R.id.achive).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.green).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.blue).setVisibility(View.INVISIBLE);
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
                    seriesGoal = new LineGraphSeries<DataPoint>();
                    seriesGoal.setColor(Color.GREEN);
                    seriesGoal.setDrawDataPoints(true);
                    seriesGoal.setDrawBackground(true);
                    seriesGoal.setThickness(8);
                    List<Float> dataFloat = new ArrayList<>();
                    dataFloat.clear();
                    AllWater = 0.0f;
                    dataFloat.addAll(UserList);
                    int number_of_values_if_the_graph=30;
                    for (int i = 2; i <number_of_values_if_the_graph+2 ; i++) {
                        int index=dataFloat.size()-i;
                        AllWater += dataFloat.get(index);
                        float y = dataFloat.get(index);
                        series.appendData(new DataPoint(i+1, y), true, 30);
                    }
                    for (int i = 2;i<number_of_values_if_the_graph+2;i++) {
                        float y =waterDaily;
                        seriesGoal.appendData(new DataPoint(i+1, y), false, 30);
                    }
                    graph.addSeries(seriesGoal);
                    summary.setText("You drank " + String.valueOf(AllWater)+" ml of water out of " +String.valueOf(waterDaily)+"ml");
                    percent.setText("This is about "+ String.valueOf(Math.round(((AllWater*100)/(waterDaily*30))*10.0)/10.0)+" % of the norm");
                    if(AllWater>=(waterDaily*30))
                        root.findViewById(R.id.achive).setVisibility(View.VISIBLE);
                    graph.addSeries(series);
                    root.findViewById(R.id.green).setVisibility(View.VISIBLE);
                    root.findViewById(R.id.blue).setVisibility(View.VISIBLE);
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
