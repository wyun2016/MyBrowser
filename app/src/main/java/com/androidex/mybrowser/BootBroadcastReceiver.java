package com.androidex.mybrowser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2016/10/15.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String ACTION = "android.intent.action.BOOT_COMPLETED";
        if (intent.getAction().equals(ACTION)) {
            Intent mainActivityIntent = new Intent(context, MainActivity.class);  // 要启动的Activity
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainActivityIntent);
        }
    }
}
