package com.example.servicesms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Intent serviceIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serviceIntent = new Intent(getApplicationContext(),SmsService.class);

        Button b = findViewById(R.id.run);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotification(getApplicationContext());
                startService(serviceIntent);

            }
        });
    }
    void showNotification(Context context){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this,"123")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");


        // Gets an instance of the NotificationManager service//

        NotificationManager mNotificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify();

                mNotificationManager.notify(001, mBuilder.build());

    }
}
