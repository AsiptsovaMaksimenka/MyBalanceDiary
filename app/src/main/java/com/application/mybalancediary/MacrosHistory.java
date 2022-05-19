package com.application.mybalancediary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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


    LineGraphSeries<DataPoint> seriesP;
    LineGraphSeries<DataPoint> seriesC;
    LineGraphSeries<DataPoint> seriesF;
    GraphView graphP;
    GraphView graphC;
    GraphView graphF;
    Button btnLast30,btnLast7;
    Float AllP=0.0f,AllC=0.0f,AllF=0.0f;
    List<Float> UserProteins = new ArrayList<Float>();
    List<Float> UserCarbs = new ArrayList<Float>();
    List<Float> UserFats = new ArrayList<Float>();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.activity_macros_history, container, false);

        graphP = root.findViewById(R.id.graphP);
        graphC = root.findViewById(R.id.graphC);
        graphF = root.findViewById(R.id.graphF);
        btnLast7 =root.findViewById(R.id.buttonLast7);
        btnLast30=root.findViewById(R.id.buttonLast30);
        graphP.setBackgroundColor(Color.WHITE);
        graphC.setBackgroundColor(Color.WHITE);
        graphF.setBackgroundColor(Color.WHITE);

        FirebaseDatabase.getInstance().getReference("TotalPerDay").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot parentDS : dataSnapshot.getChildren()) {
                    Iterator<DataSnapshot> items = parentDS.getChildren().iterator();
                    int counter = 0;
                    while (items.hasNext()) {
                        DataSnapshot item = items.next();
                        String total = item.child("TotalProteinsPerDay").getValue().toString();
                        UserProteins.add(Float.parseFloat(total));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        FirebaseDatabase.getInstance().getReference("TotalPerDay").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot parentDS : dataSnapshot.getChildren()) {
                    Iterator<DataSnapshot> items = parentDS.getChildren().iterator();
                    int counter = 0;
                    while (items.hasNext()) {
                        DataSnapshot item = items.next();
                        String total = item.child("TotalFatsPerDay").getValue().toString();
                        UserFats.add(Float.parseFloat(total));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        FirebaseDatabase.getInstance().getReference("TotalPerDay").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot parentDS : dataSnapshot.getChildren()) {
                    Iterator<DataSnapshot> items = parentDS.getChildren().iterator();
                    int counter = 0;
                    while (items.hasNext()) {
                        DataSnapshot item = items.next();
                        String total = item.child("TotalCarbsPerDay").getValue().toString();
                        UserCarbs.add(Float.parseFloat(total));
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
                if (UserProteins.size() < 7 && UserFats.size() < 7 && UserCarbs.size() < 7) {
                    btnLast7.setEnabled(false);
                    Toast.makeText(getActivity(), "You don't spend enough time here ", Toast.LENGTH_SHORT).show();

                } else {
                    graphP.removeAllSeries();
                    graphP.setBackgroundColor(Color.WHITE);
                    graphP.getGridLabelRenderer().setHorizontalAxisTitle("The last month");
                    graphP.getGridLabelRenderer().setVerticalAxisTitle("Proteins,gr");
                    graphP.getGridLabelRenderer().setTextSize(30);
                    graphP.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                    graphP.getGridLabelRenderer().setGridColor(Color.BLUE);
                    graphP.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLUE);
                    graphP.getGridLabelRenderer().setVerticalLabelsColor(Color.BLUE);
                    graphP.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);
                    graphP.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
                    seriesP = new LineGraphSeries<DataPoint>();
                    seriesP.setColor(Color.BLUE);
                    seriesP.setDrawDataPoints(true);
                    seriesP.setDrawBackground(true);
                    seriesP.setThickness(10);
                    String[] arr = {"1", "2", "3", "4", "5", "6", "7"};
                    List<String> dataString = Arrays.asList(arr);
                    List<Float> dataFloat = new ArrayList<>();
                    dataFloat.clear();
                    AllP = 0.0f;
                    dataFloat.addAll(UserProteins);
                    for (int i = 0; i < 7; i++) {
                        AllP += dataFloat.get(i);
                        String x = dataString.get(i);
                        float y = dataFloat.get(i);
                        //summary.setText(String.valueOf(AllCalories));
                        seriesP.appendData(new DataPoint(Double.parseDouble(x), y), false, 7);
                    }
                    graphP.addSeries(seriesP);
                    graphP.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                        @Override
                        public String formatLabel(double value, boolean isValueX) {
                            if (isValueX) {
                                return format.format(new Date((long) value));
                            } else {
                                return super.formatLabel(value, isValueX);
                            }
                        }
                    });
                    ////////////////////////////////////////////////////
                    graphC.removeAllSeries();
                    graphC.setBackgroundColor(Color.WHITE);
                    graphC.getGridLabelRenderer().setHorizontalAxisTitle("The last month");
                    graphC.getGridLabelRenderer().setVerticalAxisTitle("Carbs,gr");
                    graphC.getGridLabelRenderer().setTextSize(30);
                    graphC.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                    graphC.getGridLabelRenderer().setGridColor(Color.BLUE);
                    graphC.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLUE);
                    graphC.getGridLabelRenderer().setVerticalLabelsColor(Color.BLUE);
                    graphC.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);
                    graphC.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
                    seriesC = new LineGraphSeries<DataPoint>();
                    seriesC.setColor(Color.BLUE);
                    seriesC.setDrawDataPoints(true);
                    seriesC.setDrawBackground(true);
                    seriesC.setThickness(10);
                    String[] arrC = {"1", "2", "3", "4", "5", "6", "7"};
                    List<String> dataStringC = Arrays.asList(arrC);
                    List<Float> dataFloatC = new ArrayList<>();
                    dataFloat.clear();
                    AllC = 0.0f;
                    dataFloat.addAll(UserCarbs);
                    for (int i = 0; i < 7; i++) {
                        AllC += dataFloat.get(i);
                        String x = dataString.get(i);
                        float y = dataFloat.get(i);
                        // summary.setText(String.valueOf(AllC));
                        seriesC.appendData(new DataPoint(Double.parseDouble(x), y), false, 7);
                    }
                    graphC.addSeries(seriesC);
                    graphC.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                        @Override
                        public String formatLabel(double value, boolean isValueX) {
                            if (isValueX) {
                                return format.format(new Date((long) value));
                            } else {
                                return super.formatLabel(value, isValueX);
                            }
                        }
                    });
                    ////////////////////

                    graphF.removeAllSeries();
                    graphF.setBackgroundColor(Color.WHITE);
                    graphF.getGridLabelRenderer().setHorizontalAxisTitle("The last month");
                    graphF.getGridLabelRenderer().setVerticalAxisTitle("Fats,gr");
                    graphF.getGridLabelRenderer().setTextSize(30);
                    graphF.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                    graphF.getGridLabelRenderer().setGridColor(Color.BLUE);
                    graphF.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLUE);
                    graphF.getGridLabelRenderer().setVerticalLabelsColor(Color.BLUE);
                    graphF.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);
                    graphF.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
                    seriesF = new LineGraphSeries<DataPoint>();
                    seriesF.setColor(Color.BLUE);
                    seriesF.setDrawDataPoints(true);
                    seriesF.setDrawBackground(true);
                    seriesF.setThickness(10);
                    String[] arrF = {"1", "2", "3", "4", "5", "6", "7"};
                    List<String> dataStringF = Arrays.asList(arrF);
                    List<Float> dataFloatF = new ArrayList<>();
                    dataFloat.clear();
                    AllF = 0.0f;
                    dataFloat.addAll(UserFats);
                    for (int i = 0; i < 7; i++) {
                        AllF += dataFloat.get(i);
                        String x = dataString.get(i);
                        float y = dataFloat.get(i);
                        //summary.setText(String.valueOf(AllF));
                        seriesF.appendData(new DataPoint(Double.parseDouble(x), y), false, 7);
                    }
                    graphF.addSeries(seriesF);
                    graphF.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
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
            @Override
            public void onClick(View v) {
                if (UserProteins.size() < 30 && UserFats.size() < 30 && UserCarbs.size() < 30) {
                    btnLast30.setEnabled(false);
                    Toast.makeText(getActivity(), "You don't spend enough time here ", Toast.LENGTH_SHORT).show();

                } else {
                    graphP.removeAllSeries();
                    graphP.setBackgroundColor(Color.WHITE);
                    graphP.getGridLabelRenderer().setHorizontalAxisTitle("The last month");
                    graphP.getGridLabelRenderer().setVerticalAxisTitle("Proteins,gr");
                    graphP.getGridLabelRenderer().setTextSize(30);
                    graphP.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                    graphP.getGridLabelRenderer().setGridColor(Color.BLUE);
                    graphP.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLUE);
                    graphP.getGridLabelRenderer().setVerticalLabelsColor(Color.BLUE);
                    graphP.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);
                    graphP.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
                    seriesP = new LineGraphSeries<DataPoint>();
                    seriesP.setColor(Color.BLUE);
                    seriesP.setDrawDataPoints(true);
                    seriesP.setDrawBackground(true);
                    seriesP.setThickness(10);
                    String[] arr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
                    List<String> dataString = Arrays.asList(arr);
                    List<Float> dataFloat = new ArrayList<>();
                    dataFloat.clear();
                    AllP = 0.0f;
                    dataFloat.addAll(UserProteins);
                    for (int i = 0; i < 7; i++) {
                        AllP += dataFloat.get(i);
                        String x = dataString.get(i);
                        float y = dataFloat.get(i);
                        //summary.setText(String.valueOf(AllCalories));
                        seriesP.appendData(new DataPoint(Double.parseDouble(x), y), false, 7);
                    }
                    graphP.addSeries(seriesP);
                    graphP.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                        @Override
                        public String formatLabel(double value, boolean isValueX) {
                            if (isValueX) {
                                return format.format(new Date((long) value));
                            } else {
                                return super.formatLabel(value, isValueX);
                            }
                        }
                    });
                    ////////////////////////////////////////////////////
                    graphC.removeAllSeries();
                    graphC.setBackgroundColor(Color.WHITE);
                    graphC.getGridLabelRenderer().setHorizontalAxisTitle("The last month");
                    graphC.getGridLabelRenderer().setVerticalAxisTitle("Carbs,gr");
                    graphC.getGridLabelRenderer().setTextSize(30);
                    graphC.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                    graphC.getGridLabelRenderer().setGridColor(Color.BLUE);
                    graphC.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLUE);
                    graphC.getGridLabelRenderer().setVerticalLabelsColor(Color.BLUE);
                    graphC.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);
                    graphC.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
                    seriesC = new LineGraphSeries<DataPoint>();
                    seriesC.setColor(Color.BLUE);
                    seriesC.setDrawDataPoints(true);
                    seriesC.setDrawBackground(true);
                    seriesC.setThickness(10);
                    String[] arrC = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
                    List<String> dataStringC = Arrays.asList(arrC);
                    List<Float> dataFloatC = new ArrayList<>();
                    dataFloat.clear();
                    AllC = 0.0f;
                    dataFloat.addAll(UserCarbs);
                    for (int i = 0; i < 30; i++) {
                        AllC += dataFloat.get(i);
                        String x = dataString.get(i);
                        float y = dataFloat.get(i);
                        // summary.setText(String.valueOf(AllC));
                        seriesC.appendData(new DataPoint(Double.parseDouble(x), y), false, 7);
                    }
                    graphC.addSeries(seriesC);
                    graphC.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                        @Override
                        public String formatLabel(double value, boolean isValueX) {
                            if (isValueX) {
                                return format.format(new Date((long) value));
                            } else {
                                return super.formatLabel(value, isValueX);
                            }
                        }
                    });
                    ////////////////////

                    graphF.removeAllSeries();
                    graphF.setBackgroundColor(Color.WHITE);
                    graphF.getGridLabelRenderer().setHorizontalAxisTitle("The last month");
                    graphF.getGridLabelRenderer().setVerticalAxisTitle("Fats,gr");
                    graphF.getGridLabelRenderer().setTextSize(30);
                    graphF.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                    graphF.getGridLabelRenderer().setGridColor(Color.BLUE);
                    graphF.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLUE);
                    graphF.getGridLabelRenderer().setVerticalLabelsColor(Color.BLUE);
                    graphF.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);
                    graphF.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
                    seriesF = new LineGraphSeries<DataPoint>();
                    seriesF.setColor(Color.BLUE);
                    seriesF.setDrawDataPoints(true);
                    seriesF.setDrawBackground(true);
                    seriesF.setThickness(10);
                    String[] arrF = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
                    List<String> dataStringF = Arrays.asList(arrF);
                    List<Float> dataFloatF = new ArrayList<>();
                    dataFloat.clear();
                    AllF = 0.0f;
                    dataFloat.addAll(UserFats);
                    for (int i = 0; i < 30; i++) {
                        AllF += dataFloat.get(i);
                        String x = dataString.get(i);
                        float y = dataFloat.get(i);
                        //summary.setText(String.valueOf(AllF));
                        seriesF.appendData(new DataPoint(Double.parseDouble(x), y), false, 7);
                    }
                    graphF.addSeries(seriesF);
                    graphF.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
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