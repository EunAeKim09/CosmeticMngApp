package kr.or.dgit.it.cosmeticmngapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

public class AlarmService extends Service {
    public static boolean alarmSound;
    public static boolean alarmVib;

    public AlarmService() {}

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent();
        intent.setAction("kr.or.dgit.it.cosmeticmngapp.PUSH_ALARM");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());

        if(sharedPreferences.getBoolean("alarm",true)){
            if(sharedPreferences.getBoolean("alarmSound",true)){
                alarmSound = true;
            }

            if(sharedPreferences.getBoolean("alarmVib",true)){
                alarmVib = true;
            }

            AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

            Calendar calendar = Calendar.getInstance(); //알람시간 calendar에 set해주기

            int hour = Integer.parseInt(sharedPreferences.getString("alarmTime","9"));
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), hour,0); //시간 set
            calendar.set(Calendar.SECOND, 0);

            am.set(AlarmManager.RTC, calendar.getTimeInMillis(), sender);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
