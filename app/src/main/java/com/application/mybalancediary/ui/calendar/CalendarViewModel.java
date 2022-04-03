package com.application.mybalancediary.ui.calendar;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.mybalancediary.R;

public class CalendarViewModel extends RecyclerView.ViewHolder {

    public final TextView daysOfMonth;
    private final CalendarFragment.OnItemListener onItemListener;

    public CalendarViewModel(@NonNull View itemView, TextView daysOfMonth, CalendarFragment.OnItemListener onItemListener) {
        super(itemView);
        daysOfMonth=itemView.findViewById(R.id.cellDayText);
        this.daysOfMonth = daysOfMonth;
        itemView.setOnClickListener((View.OnClickListener) this);
        this.onItemListener = onItemListener;
    }

    public void onClick(View view)
    {
        onItemListener.onItemClick((getAdapterPosition()), (String) daysOfMonth.getText());
    }
}