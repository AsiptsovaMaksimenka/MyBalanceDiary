package com.application.mybalancediary.ui.calendar;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.application.mybalancediary.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.type.DateTime;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class CalendarFragment extends Fragment {

    private MaterialCalendarView calendarView;
    private Button btnAdd;
    private CalendarFragment calendarFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_calendar, container, false);
//        final CalendarView calendar = root.findViewById(R.id.scrollCalendar);
//        calendar.setDate(System.currentTimeMillis(), false, true);
//        calendarView=root.findViewById(R.id.calendar_card_view);

        btnAdd=root.findViewById(R.id.buttonAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return root;
    }



}