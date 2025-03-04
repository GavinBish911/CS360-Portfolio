package com.example.eventtrackingapplication;

import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SmsHelper {
    public static final int SMS_PERMISSION_REQUEST_CODE = 100;

    public static boolean checkSmsPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestSmsPermission(Context context) {
        ActivityCompat.requestPermissions(
                (android.app.Activity) context,
                new String[]{android.Manifest.permission.SEND_SMS},
                SMS_PERMISSION_REQUEST_CODE
        );
    }

    public static void sendSms(Context context, String phoneNumber, String message) {
        if (!checkSmsPermission(context)) {
            Toast.makeText(context, "SMS permission is not granted", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(context, "SMS sent successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Failed to send SMS: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
