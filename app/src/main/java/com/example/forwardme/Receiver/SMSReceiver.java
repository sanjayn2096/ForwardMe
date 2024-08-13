package com.example.forwardme.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.forwardme.Data.ForwardingViewModel;

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ForwardingViewModel viewModel = new ViewModelProvider((ViewModelStoreOwner)context).get(ForwardingViewModel.class);
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                String messageBody = smsMessage.getMessageBody();
                String messageSender = smsMessage.getDisplayOriginatingAddress();
                // Check subject header and forward message if necessary
                checkAndForwardMessage(context, messageBody, messageSender, viewModel);
            }
        }
    }

    private boolean shouldForwardMessage(ForwardingViewModel viewModel, String from, String body) {
        String countryCode = viewModel.getCountryCode().getValue();
        String fromNumber = viewModel.getFromNumber().getValue();
        String completeNumber = countryCode + fromNumber;
        String keywords = viewModel.getKeywords().getValue();
        String criteria = viewModel.getCriteria().getValue() != null ? viewModel.getCriteria().getValue() : "AND";

        boolean fromMatches = completeNumber == null || completeNumber.isEmpty() || from.equals(completeNumber);
        boolean keywordsMatch = keywords == null || keywords.isEmpty() || containsKeyword(keywords, body);

        switch (criteria) {
            case "AND":
                return fromMatches && keywordsMatch;
            case "OR":
                return fromMatches || keywordsMatch;
            default:
                return false;
        }
    }

    private boolean containsKeyword(String keywords, String body) {
        for (String keyword : keywords.split(",")) {
            if (body.toLowerCase().contains(keyword.trim().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private void checkAndForwardMessage(Context context, String messageBody, String messageSender, ForwardingViewModel viewModel) {
        // Implement logic to check subject header and forward message
        SharedPreferences prefs = context.getSharedPreferences("ForwardingPrefs", Context.MODE_PRIVATE);
        String forwardNumber = prefs.getString("forwardNumber", null);

        if (forwardNumber != null && shouldForwardMessage(viewModel, messageSender, messageBody)) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(forwardNumber, null, messageBody, null, null);
        }
    }
}
