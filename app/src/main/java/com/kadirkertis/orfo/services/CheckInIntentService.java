package com.kadirkertis.orfo.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.ui.main.MainActivity;
import com.kadirkertis.domain.utils.Constants;

/**
 * Created by Kadir Kertis on 15.3.2017.
 */

public class CheckInIntentService extends IntentService {

    private static final String TAG =CheckInIntentService.class.getSimpleName() ;

    public CheckInIntentService(){
        super(TAG);
    }
    public CheckInIntentService(String name) {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if(geofencingEvent.hasError()){
            Toast.makeText(getApplicationContext(), geofencingEvent.getErrorCode(),Toast.LENGTH_SHORT).show();
            return;
        }

        int transitionType = geofencingEvent.getGeofenceTransition();

        if(transitionType == Geofence.GEOFENCE_TRANSITION_EXIT){
            Intent userExitPlaceIntent = new Intent(Constants.ACTION_USER_EXIT_PLACE);
            LocalBroadcastManager.getInstance(this).sendBroadcast(userExitPlaceIntent);
            sendNotification("Exit Geofence");
        }else if(transitionType == Geofence.GEOFENCE_TRANSITION_DWELL){
            Intent userStillAtPlaceIntent = new Intent(Constants.ACTION_USER_AT_PLACE);
            LocalBroadcastManager.getInstance(this).sendBroadcast(userStillAtPlaceIntent);
            sendNotification("Still at geofence");
        }

    }

    private void sendNotification(String notificationDetails){
        Intent notificationIntent = new Intent(getApplicationContext(),MainActivity.class);

        //Construct a task stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        //Add the main activity to the task stack as the parent
        stackBuilder.addParentStack(MainActivity.class);

        //Push the content intent on to the stack
        stackBuilder.addNextIntent(notificationIntent);

        //Get a pending intent containing the entire back stack
        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText("Click Notification to open app")
                .setContentIntent(notificationPendingIntent);

        //Dismiss notification when the user touches it
        builder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify(0,builder.build());
    }
}
