package com.application.mybalancediary.ui.watertracker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
    Button btnLast30;
    Button btnLast7;
    Float AllWater=0.0f;
    List<Float> UserList = new ArrayList<Float>();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.activity_history_water_fragment, container, false);
        graph = root.findViewById(R.id.graph);
        btnLast7 =root.findViewById(R.id.buttonLast7);
        btnLast30=root.findViewById(R.id.buttonLast30);
        graph.setBackgroundColor(Color.WHITE);
        TextView summary=root.findViewById(R.id.WaterSummary);
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

        btnLast7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                String[] arr ={"1","2","3","4","5","6","7"};
                List<String> dataString = Arrays.asList(arr);
                List<Float>dataFloat= new ArrayList<>();
                dataFloat.clear();
                AllWater=0.0f;
                dataFloat.addAll(UserList);
                for(int i = 0; i < 7; i++){
                    AllWater += dataFloat.get(i);
                    String x=dataString.get(i);
                    float y = dataFloat.get(i);
                    summary.setText(String.valueOf(AllWater));
                    series.appendData(new DataPoint(Double.parseDouble(x),y),false,7);
                }
                graph.addSeries(series);
                graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if(isValueX){
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            return format.format(new Date((long)value));
                        }
                        else {
                            return super.formatLabel(value, isValueX);
                        }
                    }
                });
            }
        });

        btnLast30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                String[] arr ={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30"};
                List<String> dataString = Arrays.asList(arr);
                List<Float>dataFloat= new ArrayList<>();
                dataFloat.clear();
                AllWater=0.0f;
                dataFloat.addAll(UserList);
                for(int i = 0; i < 30; i++){
                    AllWater += dataFloat.get(i);
                    String x=dataString.get(i);
                    float y = dataFloat.get(i);
                    summary.setText(String.valueOf(AllWater));
                    series.appendData(new DataPoint(Double.parseDouble(x),y),true,30);
                }
                graph.addSeries(series);
                graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if(isValueX){
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            return format.format(new Date((long)value));
                        }
                        else {
                            return super.formatLabel(value, isValueX);
                        }
                    }
                });
            }
        });

        return root;
    }
}
