package com.cs442.dliu33.booktogo;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

public class AppService extends Service{

    private Thread thread;

    @Override
    public void onCreate() {

        Log.i("AppService", "onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // If service has already started, it will not restart.
        if (thread != null)
            return Service.START_NOT_STICKY;

        Log.i("AppService", "onStartCommand");

        Notification.Builder builder =
                new Notification.Builder(this);

        stopThread();
        startThread();

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("AppService", "onDestroy");
        stopThread();
        super.onDestroy();
    }

    private void stopThread() {
        try {
            if (thread != null) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread = null;
    }

    private void startThread() {
        thread = new Thread(null,
                new Runnable() {
                    public void run() {
                        checkUpdate();
                    }
                },
                "update");
        thread.start();
    }

    private void checkUpdate() {
        for (;;) {
            try {
                Thread.sleep(5000);
                String newMsg = MainActivity.model.update();
                Log.i("AppService", "newMsg is "+ newMsg);
                if (!newMsg.equals(""))
                    triggerNotification(newMsg);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void triggerNotification(String update) {

        //Triggering a Notification
        String svc = Context.NOTIFICATION_SERVICE;

        NotificationManager notificationManager
                = (NotificationManager) getSystemService(svc);

        Notification.Builder builder =
                new Notification.Builder(this);

        Bitmap myIconBitmap = BitmapFactory.decodeResource(AppService.this.getResources(), R.mipmap.ic_launcher);

        //Applying a custom layout to the Notification status window
        builder.setSmallIcon(R.drawable.ic_notification_small)
                .setLargeIcon(myIconBitmap)
                .setTicker("Notification")
                .setWhen(System.currentTimeMillis())
                //user gets a notification with a sound and vibration
                .setDefaults(Notification.DEFAULT_SOUND|
                        Notification.DEFAULT_VIBRATE)
                .setContentTitle("BookToGo:")
                //It shows the current counting number in the notification details
                .setContentText(String.format("%s", update));

        notificationManager.notify(0, builder.build());
    }

}
