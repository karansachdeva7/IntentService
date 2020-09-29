package com.example.intentservice;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
//import android.support.v4.app.NotificationCompat;
import android.util.Log;

import static com.example.intentservice.App.CHANNEL_ID;

public class ExampleIntentService extends IntentService {
    private static final String TAG = "ExampleIntentService";
    private PowerManager.WakeLock wakeLock;

    public ExampleIntentService() {
        super("ExampleIntentService");
        setIntentRedelivery(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "ExampleApp:Wakelock");
        wakeLock.acquire(); //will keep the CPU on
        Log.d(TAG, "Wakelock acquired");
        Notification notification = displayNotification(this);
        startForeground(1, notification);
    }

    public static Notification displayNotification(Context context) {
        createNotificationChannel(context);
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Live Recording")
                .setContentText("In progress...")
                .setSmallIcon(R.drawable.micvector)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
        return notification;
    }

    public static void createNotificationChannel(Context context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Live Recording";
            String description = "Live Recording Temp";
            int importance =  NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent");
        String input = intent.getStringExtra("inputExtra");
        for (int i = 0; i < 10; i++) {
            Log.d(TAG, input + " - " + i);
            SystemClock.sleep(1000);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        wakeLock.release();
        Log.d(TAG, "Wakelock released");
    }
}