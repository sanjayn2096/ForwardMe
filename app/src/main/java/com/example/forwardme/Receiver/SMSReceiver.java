package com.example.forwardme.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                String messageBody = smsMessage.getMessageBody();
                String messageSender = smsMessage.getDisplayOriginatingAddress();
                // Check subject header and forward message if necessary
                checkAndForwardMessage(context, messageBody, messageSender);
            }
        }
    }

    private void checkAndForwardMessage(Context context, String messageBody, String messageSender) {
        // Implement logic to check subject header and forward message
        SharedPreferences prefs = context.getSharedPreferences("ForwardingPrefs", Context.MODE_PRIVATE);
        String forwardNumber = prefs.getString("forwardNumber", null);

        if (forwardNumber != null && messageBody.contains("your_subject_header")) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(forwardNumber, null, messageBody, null, null);
        }
    }
}
