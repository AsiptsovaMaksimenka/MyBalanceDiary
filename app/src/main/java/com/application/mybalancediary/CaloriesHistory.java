package com.application.mybalancediary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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


public class CaloriesHistory extends Fragment {

    LineGraphSeries<DataPoint> series,seriesGoal;
    GraphView graph;
    Button btnLast30,btnLast7,btnAll;
    Float AllCalories=0.0f,caloriesDaily=0.0f;
    int count=0;
    List<Float> UserCalories = new ArrayList<Float>();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.activity_calories_history, container, false);
        graph = root.findViewById(R.id.graph);
        btnLast7 =root.findViewById(R.id.buttonLast7);
        btnLast30=root.findViewById(R.id.buttonLast30);
        btnAll=root.findViewById(R.id.buttonAll);
        TextView CalSum=root.findViewById(R.id.CaloriesSummary);
        TextView CalPer=root.findViewById(R.id.CaloriesPercent);
        root.findViewById(R.id.achive).setVisibility(View.INVISIBLE);
        graph.setBackgroundColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Calories,kcal");
        FirebaseDatabase.getInstance().getReference("TotalPerDay")
                .orderByKey().equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot parentDS : dataSnapshot.getChildren()) {
                    Iterator<DataSnapshot> items = parentDS.getChildren().iterator();
                    int counter = 0;
                    while (items.hasNext()) {
                        DataSnapshot item = items.next();
                        String total = item.child("TotalPerDay").getValue().toString();
                        UserCalories.add(Float.parseFloat(total));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        FirebaseDatabase.getInstance().getReference("Users")
                .orderByChild("email")
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            caloriesDaily = Float.valueOf(String.valueOf(ds.child("bmr").getValue()));
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
                if (UserCalories.size() < 8) {
                    btnLast7.setEnabled(false);
                    Toast.makeText(getActivity(), "You don't spend enough time here ", Toast.LENGTH_SHORT).show();
                    root.findViewById(R.id.achive).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.green).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.blue).setVisibility(View.INVISIBLE);
                    CalSum.setText(" ");
                    CalPer.setText("");

                } else {
                    root.findViewById(R.id.achive).setVisibility(View.INVISIBLE);
                    graph.removeAllSeries();
                    graph.setBackgroundColor(Color.WHITE);
                    graph.getGridLabelRenderer().setHorizontalAxisTitle("The last 7 days");
                    graph.getGridLabelRenderer().setVerticalAxisTitle("Calories,kcal");
                    graph.getGridLabelRenderer().setTextSize(30);
                    graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                    graph.getGridLabelRenderer().setGridColor(Color.BLUE);
                    graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLUE);
                    graph.getGridLabelRenderer().setVerticalLabelsColor(Color.BLUE);
                    graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);
                    graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
                    seriesGoal = new LineGraphSeries<DataPoint>();
                    seriesGoal.setColor(Color.GREEN);
                    seriesGoal.setDrawDataPoints(true);
                    seriesGoal.setDrawBackground(true);
                    seriesGoal.setThickness(8);
                    series = new LineGraphSeries<DataPoint>();
                    series.setColor(Color.BLUE);
                    series.setDrawDataPoints(true);
                    series.setDrawBackground(true);
                    series.setThickness(10);
                    List<Float> dataFloat = new ArrayList<>();
                    dataFloat.clear();
                    AllCalories = 0.0f;
                    dataFloat.addAll(UserCalories);
                    int number_of_values_in_the_graph=7;
                    for (int i = 2;i<number_of_values_in_the_graph+2;i++) {
                        int index=dataFloat.size()-i;
                        AllCalories += dataFloat.get(index);
                        float y = dataFloat.get(index);
                        series.appendData(new DataPoint(i+1, y), false, 7);
                    }
                    for (int i = 2;i<number_of_values_in_the_graph+2;i++) {
                        float y = caloriesDaily;
                        seriesGoal.appendData(new DataPoint(i+1, y), false, 7);
                    }
                    CalSum.setText("You ate " + String.valueOf(Math.round(AllCalories*10.0)/10.0)+" kcal out of " +String.valueOf(Math.round((caloriesDaily*7)*10.0)/10.0)+"kcal");
                    CalPer.setText("This is about "+ String.valueOf(Math.round(((AllCalories*100)/(caloriesDaily*7))*10.0)/10.0)+" % of the norm");
                    if(AllCalories>=(caloriesDaily*7))
                        root.findViewById(R.id.achive).setVisibility(View.VISIBLE);
                    graph.addSeries(series);
                    graph.addSeries(seriesGoal);
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

        btnLast30.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (UserCalories.size() < 31) {
                    btnLast30.setEnabled(false);
                    Toast.makeText(getActivity(), "You don't spend enough time here ", Toast.LENGTH_SHORT).show();
                    root.findViewById(R.id.achive).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.green).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.blue).setVisibility(View.INVISIBLE);
                    CalSum.setText(" ");
                    CalPer.setText("");

                } else {
                    root.findViewById(R.id.achive).setVisibility(View.INVISIBLE);
                    graph.removeAllSeries();
                    graph.setBackgroundColor(Color.WHITE);
                    graph.getGridLabelRenderer().setHorizontalAxisTitle("The last 30 days");
                    graph.getGridLabelRenderer().setVerticalAxisTitle("Calories,kcal");
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
                    String[] arr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
                    List<String> dataString = Arrays.asList(arr);
                    List<Float> dataFloat = new ArrayList<>();
                    dataFloat.clear();
                    AllCalories = 0.0f;
                    dataFloat.addAll(UserCalories);
                    int number_of_values_if_the_graph=30;
                    for (int i = 2; i <number_of_values_if_the_graph+2 ; i++) {
                        int index=dataFloat.size()-i;
                        AllCalories += dataFloat.get(index);
                        float y = dataFloat.get(index);
                        series.appendData(new DataPoint(i+1, y), true, 30);
                    }
                    for (int i = 2;i<number_of_values_if_the_graph+2;i++) {
                        float y = caloriesDaily;
                        seriesGoal.appendData(new DataPoint(i+1, y), false, 30);
                    }
                    graph.addSeries(seriesGoal);
                    CalSum.setText("You ate " + String.valueOf(Math.round(AllCalories*10.0)/10.0)+" kcal out of " +String.valueOf(Math.round((caloriesDaily*30)*10.0)/10.0)+"kcal");
                    CalPer.setText("This is about "+ String.valueOf(Math.round(((AllCalories*100)/(caloriesDaily*30))*10.0)/10.0)+" % of the norm");
                    if(AllCalories>=(caloriesDaily*30))
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
                if (UserCalories.size() < 1) {
                    btnAll.setEnabled(false);
                    Toast.makeText(getActivity(), "You don't spend enough time here ", Toast.LENGTH_SHORT).show();
                    root.findViewById(R.id.achive).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.green).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.blue).setVisibility(View.INVISIBLE);
                    CalSum.setText(" ");
                    CalPer.setText("");

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
                    AllCalories = 0.0f;
                    count=0;
                    dataFloat.addAll(UserCalories);
                    int number_of_values_if_the_graph=dataFloat.size()-1;
                    for (int i = 2; i <number_of_values_if_the_graph+2 ; i++) {
                        int index=dataFloat.size()-i;
                        AllCalories += dataFloat.get(index);
                        float y = dataFloat.get(index);
                        count++;
                        series.appendData(new DataPoint(i+1, y), true, 10);
                    }
                    for (int i = 2;i<number_of_values_if_the_graph+2;i++) {
                        float y = caloriesDaily;
                        seriesGoal.appendData(new DataPoint(i+1, y), false, 10);
                    }
                    graph.addSeries(seriesGoal);
                    root.findViewById(R.id.green).setVisibility(View.VISIBLE);
                    root.findViewById(R.id.blue).setVisibility(View.VISIBLE);
                    CalSum.setText("You ate " + String.valueOf(Math.round(AllCalories*10.0)/10.0)+" kcal out of " +String.valueOf(Math.round((caloriesDaily*count)*10.0)/10.0)+"kcal");
                    CalPer.setText("This is about "+ String.valueOf(Math.round(((AllCalories*100)/(caloriesDaily*count))*10.0)/10.0)+" % of the norm");
                    if(AllCalories>=(caloriesDaily*count))
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




        return root;
    }
}