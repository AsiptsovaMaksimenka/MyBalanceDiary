package com.application.mybalancediary.ui.watertracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.mybalancediary.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class HistoryWaterFragment extends Fragment {
    private static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
    LineChart lineChart;
    ArrayList<Entry> entries;
    ValueEventListener valueEventListener;
    Date date = new Date();
   // public Vector<Float> date = new Vector<Float>(20);
 //   public Vector<Float> water= new Vector<Float>(20);
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.activity_history_water_fragment, container, false);
      //  lineChart = root.findViewById(R.id.lineChart);
       /* LineDataSet dataSet = new LineDataSet(entries, "Water,ml");
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        dataSet.setColor(Color.RED);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineChart.getDescription().setText("");
        lineChart.getLegend().setEnabled(false);
        lineChart.invalidate();

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(3000f);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(0f);
        xAxis.setLabelCount(7);
        xAxis.setAxisMaximum(600f);
        xAxis.setGranularityEnabled(false);
        xAxis.setValueFormatter(new LineChartXAxisValueFormatter());















        /*mPostReference.addValueEventListener(valueEventListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                yData = new ArrayList<>();
                float i =0;
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    i=i+1;
                    String SV = String.valueOf(ds.child("Water").getValue());
                    Integer SensorValue = Integer.parseInt(SV);
                    yData.add(new Entry(i,SensorValue));
                }
                final LineDataSet lineDataSet = new LineDataSet(yData,"Temp");
                LineData data = new LineData(lineDataSet);
                lineChart.setData(data);
                lineChart.notifyDataSetChanged();
                lineChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        return root;
    }
   /* static class LineChartXAxisValueFormatter extends IndexAxisValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            Date timeMilliseconds = new Date(Float.valueOf(value).longValue());;
            String date = new SimpleDateFormat("dd-MM").format(timeMilliseconds);
            return date;
        }
    }*/
}