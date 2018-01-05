package com.touchclick;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AirTouchService extends Service
{
    private static final String TAG = "AirTouchService";
    private static final String PREFERENCE_PACKAGE = "com.touchclick";
    Context mContext = null;
    static{

            try {
                System.loadLibrary("airTouch");
            }
            catch (UnsatisfiedLinkError ule) {
                System.err.println("WARNING: Could not load airTouch.so");
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
//            getservice().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//
            Log.d(TAG, "OnCreate");
            try {
                mContext = this.createPackageContext(PREFERENCE_PACKAGE, Context.CONTEXT_IGNORE_SECURITY);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            //AirTouchJNI.clickFromJNI();
        }
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "executed at " + new Date().toString());
            }
        }).start();

            SharedPreferences sharedPreferences = mContext.getSharedPreferences("count", MODE_PRIVATE);

            int count = sharedPreferences.getInt("count",0);


            long             time       =System.currentTimeMillis();
    Date             date       =new Date(time);
    SimpleDateFormat weekFormat =new SimpleDateFormat("EEEE");
    SimpleDateFormat hourFormat =new SimpleDateFormat("hh");
    Log.d(TAG, weekFormat.format(date));
    Log.d(TAG, hourFormat.format(date));


    AlarmManager  manager       = (AlarmManager) getSystemService(ALARM_SERVICE);
    final int           halfHour        = 25*60* 1000; // one hour
    long          triggerAtTime = SystemClock.elapsedRealtime() + halfHour;

    Intent        i;
    if(!("星期六".equals(weekFormat.format(date))) ){//星期天
        {
            if(("08".equals(hourFormat.format(date)))||("20".equals(hourFormat.format(date))))
                i = new Intent(this, AlarmReceiver.class);
            else {
                i = new Intent(this,StayWackup.class);
            }
        }
    }else {
        i = new Intent(this,StayWackup.class);
    }


    PendingIntent pi            = PendingIntent.getBroadcast(this, 0, i, 0);
    manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
    return super.onStartCommand(intent, flags, startId);

        }
        //add by bao
    private void startDayhrActivity(){

        String packageName = "";
        String activityName = "com.dayhr";//.home.activity.MainActivity

        try {
/*            Intent        intent = new Intent();
            ComponentName cn     = new ComponentName(packageName, activityName);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cn);
            getApplicationContext().startActivity(intent);*/

/*            PackageManager packageManager = getApplicationContext().getPackageManager();
            Intent         intent         = packageManager.getLaunchIntentForPackage("com.dayhr");
            getApplicationContext().startActivity(intent);*/
            //                ((AirTouchService) mContext).finish();
            android.util.Log.v(TAG, "launcher Dayhr success");
        } catch (Exception e) {
            e.printStackTrace();
            android.util.Log.v(TAG, "launcher Dayhr faild: "+e.toString());
        }

    }
    //add end
    @Override
    public void onDestroy() {
        super.onDestroy();
        CancelAlarm(this);
        Log.d(TAG, "onDestroy");
    }


    public void CancelAlarm(Context context)
    {
        Intent i = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
    }


}
