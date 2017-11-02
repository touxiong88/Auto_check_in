package com.touchclick;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class StayWackup
        extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent) {

        AirTouchJNI.stayWackupFromJNI();//保持屏幕常亮

        Log.d(TAG,"Stay Wackup");
        Intent i = new Intent(context, AirTouchService.class);
        context.startService(i);
    }
}
