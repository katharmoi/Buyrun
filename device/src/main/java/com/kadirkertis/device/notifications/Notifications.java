package com.kadirkertis.device.notifications;

import android.app.Notification;

/**
 * Created by Kadir Kertis on 11/22/2017.
 */

public interface Notifications {

    void showNotification(int notificationId, Notification notification);

    void updateNotification(int notificationId, Notification notification);

    void hideNotification(int notificationId);

    void hideNotifications();
}
