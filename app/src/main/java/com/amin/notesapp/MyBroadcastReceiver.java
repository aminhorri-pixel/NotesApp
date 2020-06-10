package com.amin.notesapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyBroadcastReceiver extends BroadcastReceiver {
    AlarmManager alarmManager;
    List<PendingIntent> pendingIntents;
    MyDatabase myDatabase;
    PendingIntent pendingIntent;
    String txtTitleForAlarm;
    boolean isPm = false;
    int RequestCode = 1;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            intent = new Intent(context, AlarmActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


            myDatabase = new MyDatabase(context);
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Cursor cursor = myDatabase.GetRememberNote(1);
            pendingIntents = new ArrayList<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToFirst()) {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String desc = cursor.getString(2);
                String time = cursor.getString(3);
                String date = cursor.getString(4);
                String ctime = cursor.getString(5);
                String remember = cursor.getString(6);


                pendingIntent = PendingIntent.getActivity(context, RequestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                String[] dates = date.split("/");
                String[] times = time.split(":");

                if (Integer.parseInt(times[0]) > 12) {
                    isPm = true;
                }
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, Integer.parseInt(dates[0]));
                calendar.set(Calendar.MONTH, Integer.parseInt(dates[1]));
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dates[2]));
                calendar.set(Calendar.HOUR, isPm ? Integer.parseInt(times[0]) - 12 : Integer.parseInt(times[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(times[1]));
                calendar.set(Calendar.AM_PM, isPm ? 1 : 0);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                pendingIntents.add(pendingIntent);
            }
            isPm = false;
            RequestCode++;
        }
    }

}
