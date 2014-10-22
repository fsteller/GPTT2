package com.credomatic.grpod.ecommerce.android.gptt.testingtool.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.credomatic.grpod.ecommerce.android.gptt.testingtool.R;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.app.AppGPTT;
import com.credomatic.grpod.ecommerce.android.gptt.testingtool.webserver.WebServer;

/**
 * Created by fhernandezs on 09/06/2014.
 */
public class HTTPService extends Service {

    private static final String TAG = HTTPService.class.getSimpleName();
    private static final int NOTIFICATION_STARTED_ID = 1;

    private WebServer server = null;
    private AppGPTT context = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Creating http service");
        context = (AppGPTT) getApplication();
        server = new WebServer(context);
    }

    @Override
    public void onDestroy() {
        if(server.isRunning())
            server.stopThread();
        context.cancelNotification(NOTIFICATION_STARTED_ID);
        super.onDestroy();
        Log.i(TAG, "Http service destroyed");
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        final boolean isRunning = server.isRunning();
        if (!isRunning) {
            server.startThread();
            final String notificationText = getResources().getString(R.string.notification_started_text);
            context.sendNotificationMessage(notificationText, NOTIFICATION_STARTED_ID,
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Notification.FLAG_FOREGROUND_SERVICE |
                    Notification.FLAG_ONLY_ALERT_ONCE |
                    Notification.FLAG_AUTO_CANCEL );
            Log.i(TAG, "Http service started at: " + server.getServerAddress());
        }
        return isRunning ? START_FLAG_RETRY : START_STICKY;
    }

    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }
}
