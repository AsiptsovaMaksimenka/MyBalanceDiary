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
import android.widget.ImageView;
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


public class MacrosHistory extends Fragment {

    LineGraphSeries<DataPoint> seriesP,seriesC,seriesF;
    GraphView graph;
    Button btnLast30,btnLast7,btnAll;
    Float AllP=0.0f,AllC=0.0f,AllF=0.0f,carbs=0f,fats=0f,proteins=0f;
    List<Float> UserProteins = new ArrayList<Float>();
    List<Float> UserCarbs = new ArrayList<Float>();
    List<Float> UserFats = new ArrayList<Float>();
    int countP=0,countF=0,countC=0;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.activity_macros_history, container, false);

        graph = root.findViewById(R.id.graph);
        graph.setBackgroundColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Macros,gr");
        btnLast7 =root.findViewById(R.id.buttonLast7);
        btnLast30=root.findViewById(R.id.buttonLast30);
        btnAll=root.findViewById(R.id.buttonAll);
        TextView proteinsInfo=root.findViewById(R.id.Proteinsinfo);
        TextView carbsInfo=root.findViewById(R.id.CarbsInfo);
        TextView fatsInfo=root.findViewById(R.id.FatsInfo);

        FirebaseDatabase.getInstance().getReference("TotalPerDay")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot parentDS : dataSnapshot.getChildren()) {
                    Iterator<DataSnapshot> items = parentDS.getChildren().iterator();
                    int counter = 0;
                    while (items.hasNext()) {
                        DataSnapshot item = items.next();
                        String totalP = item.child("TotalProteinsPerDay").getValue().toString();
                        UserProteins.add(Float.parseFloat(totalP));
                        String totalF = item.child("TotalFatsPerDay").getValue().toString();
                        UserFats.add(Float.parseFloat(totalF));
                        String totalC = item.child("TotalCarbsPerDay").getValue().toString();
                        UserCarbs.add(Float.parseFloat(totalC));
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
                            carbs = Float.valueOf(String.valueOf(ds.child("carbs").getValue()));
                            fats = Float.valueOf(String.valueOf(ds.child("fats").getValue()));
                            proteins = Float.valueOf(String.valueOf(ds.child("proteins").getValue()));
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
                if (UserProteins.size() < 8 && UserFats.size() < 8 && UserCarbs.size() < 8) {
                    btnLast7.setEnabled(false);
                    Toast.makeText(getActivity(), "You don't spend enough time here ", Toast.LENGTH_SHORT).show();
                    root.findViewById(R.id.achive).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.red).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.green).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.blue).setVisibility(View.INVISIBLE);
                } else {
                    root.findViewById(R.id.red).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.green).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.blue).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.achive).setVisibility(View.INVISIBLE);
                    graph.removeAllSeries();
                    graph.setBackgroundColor(Color.WHITE);
                    graph.getGridLabelRenderer().setHorizontalAxisTitle("The last 7 days");
                    graph.getGridLabelRenderer().setVerticalAxisTitle("Macros,gr");
                    graph.getGridLabelRenderer().setTextSize(30);
                    graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                    graph.getGridLabelRenderer().setGridColor(Color.BLUE);
                    graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLUE);
                    graph.getGridLabelRenderer().setVerticalLabelsColor(Color.BLUE);
                    graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);
                    graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
                    seriesP = new LineGraphSeries<DataPoint>();
                    seriesP.setColor(Color.BLUE);
                    seriesP.setDrawDataPoints(true);
                    seriesP.setDrawBackground(true);
                    seriesP.setThickness(8);
                    seriesC = new LineGraphSeries<DataPoint>();
                    seriesC.setColor(Color.RED);
                    seriesC.setDrawDataPoints(true);
                    seriesC.setDrawBackground(true);
                    seriesC.setThickness(10);
                    seriesF = new LineGraphSeries<DataPoint>();
                    seriesF.setColor(Color.GREEN);
                    seriesF.setDrawDataPoints(true);
                    seriesF.setDrawBackground(true);
                    seriesF.setThickness(10);
                    List<Float> dataFloat = new ArrayList<>();
                    List<Float> dataFloatC = new ArrayList<>();
                    List<Float> dataFloatF = new ArrayList<>();
                    dataFloat.clear();
                    dataFloatC.clear();
                    dataFloatF.clear();
                    AllP = 0.0f;
                    AllC = 0.0f;
                    AllF = 0.0f;
                    dataFloat.addAll(UserProteins);
                    dataFloatC.addAll(UserCarbs);
                    dataFloatF.addAll(UserFats);
                    int number_of_values_in_the_graph=7;
                    for (int i = 2;i<number_of_values_in_the_graph+2;i++) {
                        int index=dataFloat.size()-i;
                        AllP += dataFloat.get(index);
                        float y = dataFloat.get(index);
                        seriesP.appendData(new DataPoint(i+1, y), false, 7);
                    }
                    for (int i = 2;i<number_of_values_in_the_graph+2;i++) {
                        int index=dataFloat.size()-i;
                        AllC += dataFloatC.get(index);
                        float y = dataFloatC.get(index);
                        seriesC.appendData(new DataPoint(i+1, y), false, 7);
                    }
                    for (int i = 2;i<number_of_values_in_the_graph+2;i++) {
                        int index=dataFloat.size()-i;
                        AllF += dataFloatF.get(index);
                        float y = dataFloatF.get(index);
                        seriesF.appendData(new DataPoint(i+1, y), false, 7);
                    }
                    root.findViewById(R.id.red).setVisibility(View.VISIBLE);
                    root.findViewById(R.id.green).setVisibility(View.VISIBLE);
                    root.findViewById(R.id.blue).setVisibility(View.VISIBLE);

                    proteinsInfo.setText("All proteins " + String.valueOf(Math.round(AllP*10.0)/10.0)+" out of " +String.valueOf(Math.round((proteins*7)*10.0)/10.0)+ "("+ String.valueOf(Math.round(((AllP*100)/(proteins*7))*10.0)/10.0)+" % of the norm)");
                    fatsInfo.setText("All fats " + String.valueOf(Math.round(AllF*10.0)/10.0)+" out of " +String.valueOf(Math.round((fats*7)*10.0)/10.0)+ "("+ String.valueOf(Math.round(((AllF*100)/(fats*7))*10.0)/10.0)+" % of the norm)");
                    carbsInfo.setText("All carbs " + String.valueOf(Math.round(AllC*10.0)/10.0)+" out of " +String.valueOf(Math.round((carbs*7)*10.0)/10.0)+ "("+ String.valueOf(Math.round(((AllC*100)/(carbs*7))*10.0)/10.0)+" % of the norm)");
                    if(AllP>=(proteins*7) && AllF>=(fats*7) && AllC>=(carbs*7) )
                        root.findViewById(R.id.achive).setVisibility(View.VISIBLE);
                    graph.addSeries(seriesP);
                    graph.addSeries(seriesC);
                    graph.addSeries(seriesF);
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
                if (UserProteins.size() < 31 && UserFats.size() < 31 && UserCarbs.size() < 31) {
                    btnLast30.setEnabled(false);
                    Toast.makeText(getActivity(), "You don't spend enough time here ", Toast.LENGTH_SHORT).show();
                    root.findViewById(R.id.achive).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.red).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.green).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.blue).setVisibility(View.INVISIBLE);
                    proteinsInfo.setText("");
                    carbsInfo.setText("");
                    fatsInfo.setText("");

                } else {
                    proteinsInfo.setText("");
                    carbsInfo.setText("");
                    fatsInfo.setText("");
                    root.findViewById(R.id.red).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.green).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.blue).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.achive).setVisibility(View.INVISIBLE);
                    graph.removeAllSeries();
                    graph.setBackgroundColor(Color.WHITE);
                    graph.getGridLabelRenderer().setHorizontalAxisTitle("The last month");
                    graph.getGridLabelRenderer().setVerticalAxisTitle("Macros,gr");
                    graph.getGridLabelRenderer().setTextSize(30);
                    graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                    graph.getGridLabelRenderer().setGridColor(Color.BLUE);
                    graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLUE);
                    graph.getGridLabelRenderer().setVerticalLabelsColor(Color.BLUE);
                    graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);
                    graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
                    seriesP = new LineGraphSeries<DataPoint>();
                    seriesP.setColor(Color.BLUE);
                    seriesP.setDrawDataPoints(true);
                    seriesP.setDrawBackground(true);
                    seriesP.setThickness(8);
                    seriesC = new LineGraphSeries<DataPoint>();
                    seriesC.setColor(Color.RED);
                    seriesC.setDrawDataPoints(true);
                    seriesC.setDrawBackground(true);
                    seriesC.setThickness(10);
                    seriesF = new LineGraphSeries<DataPoint>();
                    seriesF.setColor(Color.GREEN);
                    seriesF.setDrawDataPoints(true);
                    seriesF.setDrawBackground(true);
                    seriesF.setThickness(10);
                    List<Float> dataFloat = new ArrayList<>();
                    List<Float> dataFloatC = new ArrayList<>();
                    List<Float> dataFloatF = new ArrayList<>();
                    dataFloat.clear();
                    dataFloatC.clear();
                    dataFloatF.clear();
                    AllP = 0.0f;
                    AllC = 0.0f;
                    AllF = 0.0f;
                    dataFloat.addAll(UserProteins);
                    dataFloatC.addAll(UserCarbs);
                    dataFloatF.addAll(UserFats);
                    int number_of_values_if_the_graph=30;
                    for (int i = 2; i <number_of_values_if_the_graph+2 ; i++) {
                        int index=dataFloat.size()-i;
                        AllP += dataFloat.get(i);
                        float y = dataFloat.get(i);
                        seriesP.appendData(new DataPoint(i+1, y), false, 30);
                    }
                    for (int i = 2; i <number_of_values_if_the_graph+2 ; i++) {
                        int index=dataFloat.size()-i;
                        AllC += dataFloatC.get(index);
                        float y = dataFloatC.get(index);
                        seriesC.appendData(new DataPoint(i+1, y), false, 30);
                    }
                    for (int i = 2; i <number_of_values_if_the_graph+2 ; i++) {
                        int index=dataFloat.size()-i;
                        AllF += dataFloatF.get(index);
                        float y = dataFloatF.get(index);
                        seriesF.appendData(new DataPoint(i+1, y), false, 30);
                    }
                    root.findViewById(R.id.red).setVisibility(View.VISIBLE);
                    root.findViewById(R.id.green).setVisibility(View.VISIBLE);
                    root.findViewById(R.id.blue).setVisibility(View.VISIBLE);
                    proteinsInfo.setText("All proteins " + String.valueOf(Math.round(AllP*10.0)/10.0)+" out of " +String.valueOf(Math.round((proteins*30)*10.0)/10.0)+ "("+ String.valueOf(Math.round(((AllP*100)/(proteins*30))*10.0)/10.0)+" % of the norm)");
                    fatsInfo.setText("All fats " + String.valueOf(Math.round(AllF*10.0)/10.0)+" out of " +String.valueOf(Math.round((fats*30)*10.0)/10.0)+ "("+ String.valueOf(Math.round(((AllF*100)/(fats*30))*10.0)/10.0)+" % of the norm)");
                    carbsInfo.setText("All carbs " + String.valueOf(Math.round(AllC*10.0)/10.0)+" out of " +String.valueOf(Math.round((carbs*30)*10.0)/10.0)+ "("+ String.valueOf(Math.round(((AllC*100)/(carbs*30))*10.0)/10.0)+" % of the norm)");
                    if(AllP>=(proteins*30) && AllF>=(fats*30) && AllC>=(carbs*30) )
                        root.findViewById(R.id.achive).setVisibility(View.VISIBLE);
                    graph.addSeries(seriesP);
                    graph.addSeries(seriesC);
                    graph.addSeries(seriesF);
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
                if (UserProteins.size() < 1 && UserFats.size() < 1 && UserCarbs.size() < 1) {
                    btnAll.setEnabled(false);
                    Toast.makeText(getActivity(), "You don't spend enough time here ", Toast.LENGTH_SHORT).show();
                    root.findViewById(R.id.achive).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.red).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.green).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.blue).setVisibility(View.INVISIBLE);

                } else {
                    root.findViewById(R.id.achive).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.red).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.green).setVisibility(View.INVISIBLE);
                    root.findViewById(R.id.blue).setVisibility(View.INVISIBLE);
                    graph.removeAllSeries();
                    graph.setBackgroundColor(Color.WHITE);
                    graph.getGridLabelRenderer().setHorizontalAxisTitle("All time");
                    graph.getGridLabelRenderer().setVerticalAxisTitle("Macros,gr");
                    graph.getGridLabelRenderer().setTextSize(30);
                    graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                    graph.getGridLabelRenderer().setGridColor(Color.BLUE);
                    graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLUE);
                    graph.getGridLabelRenderer().setVerticalLabelsColor(Color.BLUE);
                    graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);
                    graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
                    seriesP = new LineGraphSeries<DataPoint>();
                    seriesP.setColor(Color.BLUE);
                    seriesP.setDrawDataPoints(true);
                    seriesP.setDrawBackground(true);
                    seriesP.setThickness(8);
                    seriesC = new LineGraphSeries<DataPoint>();
                    seriesC.setColor(Color.RED);
                    seriesC.setDrawDataPoints(true);
                    seriesC.setDrawBackground(true);
                    seriesC.setThickness(10);
                    seriesF = new LineGraphSeries<DataPoint>();
                    seriesF.setColor(Color.GREEN);
                    seriesF.setDrawDataPoints(true);
                    seriesF.setDrawBackground(true);
                    seriesF.setThickness(10);
                    List<Float> dataFloat = new ArrayList<>();
                    List<Float> dataFloatC = new ArrayList<>();
                    List<Float> dataFloatF = new ArrayList<>();
                    dataFloat.clear();
                    dataFloatC.clear();
                    dataFloatF.clear();
                    AllP = 0.0f;
                    AllC = 0.0f;
                    AllF = 0.0f;
                    countF=0;
                    countC=0;
                    countP=0;
                    dataFloat.addAll(UserProteins);
                    dataFloatC.addAll(UserCarbs);
                    dataFloatF.addAll(UserFats);
                    int number_of_values_if_the_graph=dataFloat.size()-1;
                    for (int i = 2; i <number_of_values_if_the_graph+2 ; i++) {
                        int index=dataFloat.size()-i;
                        AllP += dataFloat.get(index);
                        countP++;
                        float y = dataFloat.get(index);
                        seriesP.appendData(new DataPoint(i+1, y), false, 10);
                    }
                    for (int i = 2; i <number_of_values_if_the_graph+2 ; i++) {
                        int index=dataFloat.size()-i;
                        AllC += dataFloatC.get(index);
                        float y = dataFloatC.get(index);
                        countC++;
                        seriesC.appendData(new DataPoint(i+1, y), false, 10);
                    }
                    for (int i = 2; i <number_of_values_if_the_graph+2 ; i++) {
                        int index=dataFloat.size()-i;
                        AllF += dataFloatF.get(index);
                        float y = dataFloatF.get(index);
                        countF++;
                        seriesF.appendData(new DataPoint(i+1, y), false, 10);
                    }
                    root.findViewById(R.id.red).setVisibility(View.VISIBLE);
                    root.findViewById(R.id.green).setVisibility(View.VISIBLE);
                    root.findViewById(R.id.blue).setVisibility(View.VISIBLE);
                    proteinsInfo.setText("All proteins " + String.valueOf(Math.round(AllP*10.0)/10.0)+" out of " +String.valueOf(Math.round((proteins*countP)*10.0)/10.0)+ "("+ String.valueOf(Math.round(((AllP*100)/(proteins*countP))*10.0)/10.0)+" % of the norm)");
                    fatsInfo.setText("All fats " + String.valueOf(Math.round(AllF*10.0)/10.0)+" out of " +String.valueOf(Math.round((fats*countF)*10.0)/10.0)+ "("+ String.valueOf(Math.round(((AllF*100)/(fats*countF))*10.0)/10.0)+" % of the norm)");
                    carbsInfo.setText("All carbs " + String.valueOf(Math.round(AllC*10.0)/10.0)+" out of " +String.valueOf(Math.round((carbs*countC)*10.0)/10.0)+ "("+ String.valueOf(Math.round(((AllC*100)/(carbs*countC))*10.0)/10.0)+" % of the norm)");
                    if(AllP>=(proteins*7) && AllF>=(fats*7) && AllC>=(carbs*7) )
                        root.findViewById(R.id.achive).setVisibility(View.VISIBLE);
                    graph.addSeries(seriesP);
                    graph.addSeries(seriesC);
                    graph.addSeries(seriesF);
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