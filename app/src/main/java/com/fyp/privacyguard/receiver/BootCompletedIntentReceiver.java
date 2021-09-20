package com.fyp.privacyguard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fyp.privacyguard.service.PhoneLogService;

public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(Intent.ACTION_SCREEN_ON.equals(action)) {
            // start the service
            Intent pushIntent = new Intent(context, PhoneLogService.class);
            context.startService(pushIntent);
        } else if(Intent.ACTION_SCREEN_OFF.equals(action)) {
            // stop the service
            Intent pushIntent = new Intent(context, PhoneLogService.class);
            context.stopService(pushIntent);
        }
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent pushIntent = new Intent(context, PhoneLogService.class);
            context.startService(pushIntent);
        }
    }
}
