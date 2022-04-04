package com.application.mybalancediary.ui.calendar;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

class CalendarFragment extends RecyclerView.Adapter {

    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;

    public CalendarFragment(ArrayList<String> daysOfMonth, OnItemListener onItemListener) {

        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(com.application.mybalancediary.R.layout.calendar_cell,parent,false);
        ViewGroup.LayoutParams layoutParams=view.getLayoutParams();
        layoutParams.height=(int)(parent.getHeight()*0.166666666);
        return new CalendarViewModel(view, (TextView) onItemListener, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }


    public void onBindViewHolder(@NonNull CalendarViewModel holder, int position) {
        holder.daysOfMonth.setText(daysOfMonth.get(position));
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public interface  OnItemListener
    {
        void onItemClick(int position, String dayText);
    }
}