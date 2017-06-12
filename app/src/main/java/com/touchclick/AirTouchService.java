package com.touchclick;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AirTouchService extends Service
{
    private static final String TAG = "AirTouchService";
    static boolean dayFlag = true;
    static{

        try {
            System.loadLibrary("airTouch");
        }
        catch (UnsatisfiedLinkError ule) {
            System.err.println("WARNING: Could not load lib3djni.so");
            ule.printStackTrace();
        }
    }

    public AirTouchService() {

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        //getservice().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//
        Log.d(TAG, "OnCreate");
        AirTouchJNI.clickFromJNI();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "executed at " + new Date().toString());
                long time=System.currentTimeMillis();
                Date date=new Date(time);
                SimpleDateFormat format=new SimpleDateFormat("EEEE");
//                format=new SimpleDateFormat("EEEE");
                Log.d(TAG,format.format(date));
                if(!("星期天".equals(format.format(date))))AirTouchJNI.clickFromJNI();

            }
        }).start();

        AlarmManager  manager       = (AlarmManager) getSystemService(ALARM_SERVICE);
        final int           anHour        = 60*60* 1000; // 10秒
        int tempHour;
        if(dayFlag){
            tempHour = anHour* 11;
            dayFlag = false;
        }
        else{
            tempHour = anHour* 12;
            dayFlag = true;
        }

        long          triggerAtTime = SystemClock.elapsedRealtime() + tempHour;
        Intent        i             = new Intent(this, AlarmReceiver.class);
        PendingIntent pi            = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }





}
