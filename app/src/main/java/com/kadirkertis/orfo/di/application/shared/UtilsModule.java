package com.kadirkertis.orfo.di.application.shared;

import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;

import com.kadirkertis.domain.services.UserTrackingService;
import com.kadirkertis.device.location.UserTrackingServiceImpl;
import com.kadirkertis.device.notifications.Notifications;
import com.kadirkertis.device.notifications.NotificationsImpl;
import com.kadirkertis.orfo.di.application.AppScope;
import com.kadirkertis.orfo.utils.ActivityUtils;
import com.kadirkertis.orfo.utils.ActivityUtilsImpl;
import com.patloew.rxlocation.RxLocation;
import com.tbruyelle.rxpermissions2.RxPermissions;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Kadir Kertis on 11/6/2017.
 */
@Module
public final class UtilsModule {

    @Provides
    @AppScope
    ActivityUtils provideActivityUtils(){
        return new ActivityUtilsImpl();
    }

    @Provides
    @AppScope
    NotificationManagerCompat provideNotificationsManagerCompat(Context context){
        return NotificationManagerCompat.from(context);
    }

    @Provides
    @AppScope
    Notifications provideNotifications(NotificationManagerCompat notificationManagerCompat){
        return new NotificationsImpl(notificationManagerCompat);
    }

    @Provides
    @AppScope
    RxLocation  provideRxLocation(Context context){
        return new RxLocation(context);
    }

}
