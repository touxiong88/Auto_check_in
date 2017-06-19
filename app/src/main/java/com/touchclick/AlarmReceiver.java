package com.touchclick;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Thread.sleep;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AirTouchService";
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long             time   =System.currentTimeMillis();
        Date             date   =new Date(time);
        SimpleDateFormat format =new SimpleDateFormat("EEEE");

        Log.d(TAG, format.format(date));
        //com.dayhr:pushservice com.dayhr/.home.activity.MainActivity
        //startDayhrActivity();
        try {
            sleep(1);//1豪秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(!("星期天".equals(format.format(date))))AirTouchJNI.clickFromJNI();
        Log.d(TAG,"CHECK IN");
        Intent i = new Intent(context, AirTouchService.class);
        context.startService(i);
    }
}
