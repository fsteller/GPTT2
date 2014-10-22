package com.credomatic.grpod.ecommerce.android.gptt.testingtool.app;

import android.annotation.TargetApi;
import android.app.ActivityGroup;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.credomatic.grpod.ecommerce.android.gptt.testingtool.R;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.activityGPTT;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.services.HTTPService;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.utilities.net.NetHttp;

/**
 * Created by fhernandezs on 09/06/2014.
 */
public class AppGPTT extends Application {

    private static final String TAG = AppGPTT.class.getSimpleName();
    private static final long[] VIBRATE = {0, 100, 200, 200, 100, 300};

    //<editor-fold desc="Variables">

    private int httpServicePort = -1;
    private boolean isHttpServiceRunning = false;
    private NotificationManager notificationManager;
    private SharedPreferences pref;

    //</editor-fold>

    //<editor-fold desc="Public Methods">

    //<editor-fold desc="Http Service Methods">

    public void startHttpServer() {
        try {
            Log.i(TAG, "Starting Http Server");
            final Intent httpServiceIntent = new Intent(this, HTTPService.class);
            startService(httpServiceIntent);
            isHttpServiceRunning = true;
        } catch (final Exception e) {
            Log.e(TAG, "Error during http service initialization", e);
            stopHttpServer();
        }
    }

    public void stopHttpServer() {
        try {
            Log.i(TAG, "Stopping Http Server");
            final Intent httpServiceIntent = new Intent(this, HTTPService.class);
            stopService(httpServiceIntent);
        } catch (final Exception e) {
            Log.e(TAG, "Error stopping Http service", e);
        }finally {
            isHttpServiceRunning = false;
        }
    }

    public boolean isHttpServiceRunning() {
        return isHttpServiceRunning;
    }

    public int getHttpServerPort() {
        if (httpServicePort == -1)
            httpServicePort = getSharedPreferences().
                    getInt(Constants.PREF_SERVER_PORT, Constants.DEFAULT_SERVER_PORT);

        return httpServicePort;
    }

    public String getHttpServerAddress() {

        return String.format("http://%s:%s%s",
                NetHttp.getLocalIpV4Address(),
                getHttpServerPort(),
                getResources().getString(R.string.trxResponsePattern));
    }

    //</editor-fold>
    //<editor-fold desc="Notification Methods">

    public void sendNotificationMessage(final String message, final int id, final int flags) {

        final Intent defaultIntent = new Intent(this, activityGPTT.class);
        final boolean isVibrate = getBooleanPreference(Constants.PREF_VIBRATE, true);
        final boolean isPlaySound = getBooleanPreference(Constants.PREF_PLAYSOUND, true);
        final String notificationSound = getStringPreference(Constants.PREF_RINGTONE, "");
        final PendingIntent pendingDefaultIntent = PendingIntent.getActivity(this, 0, defaultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final PendingIntent pendingCancelIntent = PendingIntent.getActivity(this, 0, defaultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        final PendingIntent pendingGoIntent = PendingIntent.getActivity(this, 0, defaultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (notificationManager == null)
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        defaultIntent.putExtra(Constants.GPTT_MESSAGE, message);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            displayMessage_JELLY_BEAN(this, notificationManager, id, flags,
                    pendingDefaultIntent,
                    pendingCancelIntent,
                    pendingGoIntent,
                    message,
                    isVibrate,
                    isPlaySound,
                    notificationSound);
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
            displayMessage_HONEYCOMB(this, notificationManager, id, flags, pendingDefaultIntent, message, isVibrate, isPlaySound, notificationSound);
        }
    }

    public void cancelNotification(final int id) {
        if (notificationManager != null) {
            notificationManager.cancel(id);
            notificationManager = null;
        }
    }

    //</editor-fold>
    //<editor-fold desc="Preferences Methods">

    public String getStringPreference(final String preferenceKey, final String defaultValue) {
        return getSharedPreferences().getString(preferenceKey, defaultValue);
    }

    public Boolean getBooleanPreference(final String preferenceKey, final boolean defaultValue) {
        return getSharedPreferences().getBoolean(preferenceKey, defaultValue);
    }

    //</editor-fold>

    //</editor-fold>
    //<editor-fold desc="Private Methods">

    private SharedPreferences getSharedPreferences() {
        if (pref == null)
            pref = PreferenceManager.getDefaultSharedPreferences(this);

        return pref;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void displayMessage_JELLY_BEAN(final Context context,
                                                  final NotificationManager notifyManager,
                                                  final int id,
                                                  final int flags,
                                                  final PendingIntent defaultIntent,
                                                  final PendingIntent cancelIntent,
                                                  final PendingIntent openIntent,
                                                  final String message,
                                                  final boolean isVibrate,
                                                  final boolean isPlaySound,
                                                  final String notificationSound) {

        final String title = context.getString(R.string.message_title);
        final Notification notification = new Notification.Builder(context)
                .addAction(android.R.drawable.ic_menu_delete, "Stop", cancelIntent)
                .addAction(android.R.drawable.ic_menu_info_details, "Open", openIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(defaultIntent)
                .setContentText(message)
                .setContentTitle(title)
                .setAutoCancel(true)
                .build();

        notification.defaults = Notification.DEFAULT_LIGHTS;
        notification.flags |= flags;

        if (isVibrate)
            notification.vibrate = VIBRATE;

        if (isPlaySound && notificationSound.length() > 0)
            notification.sound = Uri.parse(notificationSound);

        notifyManager.notify(id, notification);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void displayMessage_HONEYCOMB(final Context context, final NotificationManager notifyManager, final int id, final int flags, final PendingIntent intent, final String message, final boolean isVibrate, final boolean isPlaysound, final String notificationSound) {

        final String title = context.getString(R.string.message_title);
        final Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(intent)
                .getNotification();


        notification.defaults = Notification.DEFAULT_LIGHTS;
        notification.flags |= flags;

        if (isVibrate)
            notification.vibrate = VIBRATE;

        if (isPlaysound && !notificationSound.isEmpty())
            notification.sound = Uri.parse(notificationSound);

        notifyManager.notify(id, notification);
    }

    //</editor-fold>
}
