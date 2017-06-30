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
        SimpleDateFormat weekFormat =new SimpleDateFormat("EEEE");
        SimpleDateFormat hourFormat =new SimpleDateFormat("hh");
        Log.d(TAG, weekFormat.format(date));
        Log.d(TAG, hourFormat.format(date));
        //com.dayhr:pushservice com.dayhr/.home.activity.MainActivity
        //startDayhrActivity();
        try {
            sleep(1);//1豪秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AirTouchJNI.stayWackupFromJNI();//保持屏幕常亮
        if(!("星期天".equals(weekFormat.format(date)))){
            if(("08".equals(hourFormat.format(date))))
                AirTouchJNI.clickFromJNI();}



        Log.d(TAG,"CHECK IN");
        Intent i = new Intent(context, AirTouchService.class);
        context.startService(i);
    }
}
