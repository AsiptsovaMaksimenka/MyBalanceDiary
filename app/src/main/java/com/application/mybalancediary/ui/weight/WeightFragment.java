package com.application.mybalancediary.ui.weight;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

public class WeightFragment extends Fragment {

    private Button btnInputWeight;
    private Button btnAlltime;
    private Button btnLast30;
    private Button btnLast7;
    private Button btnSettings;

    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");

    //graph create
    LineGraphSeries<DataPoint> series;
    GraphView graph;

    private TextView statCurWt;
    private TextView statNetWt;
    private TextView statGoalWt;
    public float currentw;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        final View root = inflater.inflate(R.layout.fragment_weight_track, container, false);
        statCurWt=root.findViewById(R.id.statCurWt);
        statNetWt=root.findViewById(R.id.statNetWt);
        statGoalWt=root.findViewById(R.id.statGoalWt);


        updateStatsAndChart();
        grahDate(graph, series, 7);

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

        FirebaseDatabase.getInstance().getReference().child("Weight").addValueEventListener(new ValueEventListener() {
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
            @Override
            public void onClick(View view) {
                grahDate(graph, series, -1);
            }
        });

        btnLast30=root.findViewById(R.id.buttonLast30);
        btnLast30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grahDate(graph, series, 30);
            }
        });

        btnLast7=root.findViewById(R.id.buttonLast7);
        btnLast7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grahDate(graph, series, 7);
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
                    openGoalWeightMissingAlertDialog();
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
        startActivityForResult(intent,1);
    }

    private void openGoalWeightMissingAlertDialog() {
        GoalWeightMissingAlertDialog goalWeightMissingAlertDialog = new GoalWeightMissingAlertDialog();
//        goalWeightMissingAlertDialog.show(GoalWeightMissingAlertDialog.class,"goalalerttag");
//        goalWeightMissingAlertDialog(getContext(),GoalWeightMissingAlertDialog.class);
//
//        Intent intent=new Intent(getContext(), GoalWeightMissingAlertDialog.class);
//        startActivityForResult(intent, 1);


    }


    private void openWeightFragment_Setting() {
        Intent intent = new Intent(getContext(),WeightFragmentSetting.class);
        startActivityForResult(intent, 2);
    }

    private void grahDate(GraphView graph, LineGraphSeries<DataPoint> series, int i) {
    }


    private void updateStatsAndChart() {
    }
}