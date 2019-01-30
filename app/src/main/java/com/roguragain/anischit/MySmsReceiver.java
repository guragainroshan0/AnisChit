package com.roguragain.anischit;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class MySmsReceiver extends BroadcastReceiver {


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("GOT BROADCAST","BROADCAST");

        // Get the SMS message

        Bundle bundle = intent.getExtras();

        SmsMessage[] msgs;

        String strMessage = "";
        String format = bundle.getString("format");

        //Retrieve the sms message received

        Object[] pdus = (Object[]) bundle.get("pdus");

        //fill the message array

        msgs = new SmsMessage[pdus.length];

        for(int i=0;i<msgs.length;i++) {
            //Check Android Version and use appropriate createFromPdu
            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);


            strMessage += "SMS from " + msgs[i].getOriginatingAddress();
            strMessage += msgs[i].getDisplayMessageBody();
            //strMessage += " :" + msgs[i].getMessageBody()+"\n";
            Toast.makeText(context,strMessage,Toast.LENGTH_SHORT).show();

        }

        Log.d("RECEIVED MESSAGE",strMessage);
    }
}
