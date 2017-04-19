package com.kadirkertis.orfo.services;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.kadirkertis.orfo.data.DbTaskParams;
import com.kadirkertis.orfo.data.DbTasks;


/**
 * Created by Kadir Kertis on 16.3.2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    public AlarmReceiver() {
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        new DbTasks(context).execute(new DbTaskParams(DbTasks.TASK_EMPTY_CART));
    }

}
