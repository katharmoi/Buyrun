package com.kadirkertis.device.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by Kadir Kertis on 11/10/2017.
 */

public class AlarmService {

    private Context context;

    public AlarmService(Context context){
        this.context = context;
    }

    private void setCartAlarm() {
        AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent mAlarmIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES, mAlarmIntent);
    }
}
