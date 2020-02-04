package com.example.servicesms;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String messageBody;
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                messageBody = smsMessage.getMessageBody();
                Log.d("REceived",messageBody);
                Toast.makeText(context.getApplicationContext(),messageBody,Toast.LENGTH_LONG).show();

//                NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"CHID")
//                        .setSmallIcon(R.drawable.ic_launcher_background)
//                        .setContentTitle("Are You Safe")
//                        .setPriority(NotificationCompat.PRIORITY_HIGH)
//                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
//
//                Notification notification = builder.build();
//
//                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from();
//                Log.d("Notification R90",messageBody);
//                notificationManagerCompat.notify(891,notification);
            }
        }
    }


    private void showNotification(Context context){
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context,"Channel ID")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
       // mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(10, mBuilder.build());

    }
}