package com.example.servicesms;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class SmsService extends Service {

    SmsReceiver smsReceiver = new SmsReceiver();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("Service Demo ", " " + Thread.currentThread().getId());
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerReceiver(smsReceiver,new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        //Toast.makeText(this, "Check", Toast.LENGTH_SHORT).show();
        Log.i("Service Demo ", " " + Thread.currentThread().getId());
        unregisterReceiver(smsReceiver);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"Destroyed Sms",Toast.LENGTH_LONG).show();
        Log.i("R9 SMS receiver","Destroyed");
    }




}