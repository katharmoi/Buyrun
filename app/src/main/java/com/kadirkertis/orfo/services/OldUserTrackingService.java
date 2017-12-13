package com.kadirkertis.orfo.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.kadirkertis.domain.utils.Constants;
import com.kadirkertis.orfo.R;
import com.kadirkertis.orfo.ui.main.MainActivity;
import com.kadirkertis.orfo.ui.review.ReviewActivity;
import com.kadirkertis.orfo.ui.review.ReviewDialogFragment;
import com.kadirkertis.orfo.utils.UserUtilities;

import java.util.Random;

/**
 * Created by Kadir Kertis on 19.3.2017.
 */

public class OldUserTrackingService extends Service
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final String TAG = OldUserTrackingService.class.getSimpleName();
    private static final int outLimit = 10;
    private final IBinder mBinder = new UserTrackingServiceBinder();
    private GoogleApiClient mClient;
    private LocationRequest mLocationRequest;
    private static final long INTERVAL_FAST = 1000;
    private static final long INTERVAL_SLOW = 5 * 1000;
    private int FENCE_RADIUS = Integer.MAX_VALUE; //TODO DEBUG VALUE
    private LatLng mUserLatLng;
    private LatLng mPlaceLatLng;
    private OnLocationDataArrivedListener mListener;
    private SharedPreferences mPrefs;
    private String mCheckedInPlaceId;
    private int outTick = 0;

    public class UserTrackingServiceBinder extends Binder {

        public boolean checkUserAtPlace() {
            return isUserIn();
        }

        public void setOnLocationDataArrivedListener(OnLocationDataArrivedListener listener) {
            if (mListener == null) {
                mListener = listener;
            }

        }

        public void removeOnLocationDataArrivedListener() {
            mListener = null;
        }

        public void checkUserOut() {
            FENCE_RADIUS = -1;
        } //TODO debug only


    }


    @Override
    public void onCreate() {
        super.onCreate();
        buildGoogleApiClient();
        mPrefs = getSharedPreferences(Constants.PREFS_CHECKED_IN_PLACE, MODE_PRIVATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!mClient.isConnected()) {
            mClient.connect();
        }

        double[] latlong = intent.getDoubleArrayExtra(Constants.EXTRA_PLACE_LAT_LONG);
        mCheckedInPlaceId = intent.getStringExtra(Constants.EXTRA_PLACE_ID);
        if (latlong != null) {
            mPlaceLatLng = new LatLng(latlong[0], latlong[1]);
        }

        Log.d(TAG, "TRACKING SERVICE STARTED...");

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mClient.disconnect();
        Log.d(TAG, "TRACKING SERVICE STOPPED...");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(INTERVAL_SLOW);
        mLocationRequest.setFastestInterval(INTERVAL_FAST);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        mClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), getString(R.string.conn_failure), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        mUserLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        //TODO if(checkedIn())
        if (!isUserIn()) {
            if (++outTick == outLimit) {
                String notContent = getString(R.string.notification_user_left_place_content);
                if (mPrefs.getLong(Constants.PREFS_ORDER_TIME, Long.MAX_VALUE) != Long.MAX_VALUE) {
                    notContent = getString(R.string.review_request);
                }
                sendNotification(getString(R.string.notification_user_left_place_title),
                        notContent);
                UserUtilities.checkOutUser(getApplicationContext());
                stopSelf();
            }

        } else {
            outTick = 0;
        }
        if (mListener != null) {
            mListener.onLocationDataArrived();
        }

        Log.d(TAG, "User Location Data Send");
    }

    protected synchronized void buildGoogleApiClient() {
        mClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }


    public boolean isUserIn() {

        Location placeL = new Location("place");
        placeL.setLatitude(mPlaceLatLng.latitude);
        placeL.setLongitude(mPlaceLatLng.longitude);

        Location userL = new Location("user");
        userL.setLatitude(mUserLatLng.latitude);
        userL.setLongitude(mUserLatLng.longitude);
        return placeL.distanceTo(userL) < FENCE_RADIUS;

    }


    public interface OnLocationDataArrivedListener {
        void onLocationDataArrived();
    }

    private void sendNotification(String notificationTitle, String notificationDetails) {
        Intent notificationIntent;
        //if user made an order ,request com.kadirkertis.orfo.review
        //else open main activity
        //Construct a task stack

        if (mPrefs.getLong(Constants.PREFS_ORDER_TIME, Long.MAX_VALUE) != Long.MAX_VALUE) {
            notificationIntent = new Intent(getApplicationContext(), ReviewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(ReviewDialogFragment.ID, mCheckedInPlaceId);
            notificationIntent.putExtras(bundle);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);

        } else {
            notificationIntent = new Intent(getApplicationContext(), MainActivity.class);

        }


        PendingIntent notificationPendingIntent = PendingIntent.getActivity(getApplicationContext(),
                new Random().nextInt(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setColor(Color.RED)
                .setContentTitle(notificationTitle)
                .setContentText(notificationDetails)
                .setContentIntent(notificationPendingIntent);

        builder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify(0, builder.build());
    }

}
