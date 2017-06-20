package com.touchclick;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sunhz on 2017/6/19.
 * 解决取消从 AlarmManager 报警
 * 报警应创建和取消对同一挂起的intent。你的情况在创建两次挂起的意图。
 */

public class AlarmNotifReceiver extends BroadcastReceiver {

    PendingIntent pi;
    AlarmManager  am;
    Intent        i;

    @Override
    public void onReceive(Context context, Intent intent) {
        pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        i = new Intent(context, AlarmNotifReceiver.class);
        //things
    }

    public void SetAlarm(Context context) {

        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10 * 1000, pi);
    }

    public void CancelAlarm(Context context) {
        am.cancel(pi);
    }


}