package com.application.mybalancediary.ui.calendar;


import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.Calendar;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.application.mybalancediary.R;

public class CalendarFragment extends Fragment  {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        final CalendarView calendar =root.findViewById(R.id.scrollCalendar);
        calendar.setDate(System.currentTimeMillis(),false,true);

        return root;
    }
    }
