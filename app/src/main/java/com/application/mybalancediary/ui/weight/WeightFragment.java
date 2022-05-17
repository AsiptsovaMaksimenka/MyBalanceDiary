package com.application.mybalancediary.ui.weight;

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

    LineGraphSeries<DataPoint> series;
    GraphView graph;

    private TextView statCurWt;
    private TextView statNetWt;
    private TextView statGoalWt;

    private FragmentProfileBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        final View root = inflater.inflate(R.layout.fragment_weight_track, container, false);
//        return root;
//    }
}