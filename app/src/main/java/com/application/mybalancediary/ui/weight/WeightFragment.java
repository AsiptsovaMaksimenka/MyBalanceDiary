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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_weight_track, container, false);
        statCurWt=root.findViewById(R.id.statCurWt);
        statNetWt=root.findViewById(R.id.statNetWt);
        statGoalWt=root.findViewById(R.id.statGoalWt);

        updateStatsAndChart();
        grahDate(graph, series, 7);

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
                    openWeightFragment_GoalWeight();
                }
                else
                {
                    openWeightFragment_InputWeightDate();
                }
            }
        });


        return root;
    }

    private void openWeightFragment_InputWeightDate() {
    }

    private void openWeightFragment_GoalWeight() {
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