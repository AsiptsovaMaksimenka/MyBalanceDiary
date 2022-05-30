package com.application.mybalancediary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlertReceiverWeight extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder nb = new NotificationCompat.Builder(context, "weightID")
                .setContentText("Time to update your weight")
                .setSmallIcon(R.drawable.ic_baseline_monitor_weight_24)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(200, nb.build());
    }
}