package kr.or.dgit.it.cosmeticmngapp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import kr.or.dgit.it.cosmeticmngapp.dao.UserCosmeticDAO;
import kr.or.dgit.it.cosmeticmngapp.dao.UserCosmeticToolsDAO;
import kr.or.dgit.it.cosmeticmngapp.dao.UserLensDAO;

public class AlarmReceiver extends BroadcastReceiver {
    private UserCosmeticDAO cosmeticdao;
    private UserCosmeticToolsDAO cosmeticToolsdao;
    private UserLensDAO lensdao;
    NotificationManager manager;
    NotificationCompat.Builder builder = null;
    private PendingIntent pIntent;
    String title = "MNAPP";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAG", "onReceive: AlarmReceiver");
        cosmeticdao = new UserCosmeticDAO(context);
        cosmeticToolsdao = new UserCosmeticToolsDAO(context);
        lensdao = new UserLensDAO(context);

        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(android.R.drawable.ic_notification_overlay);
        builder.setContentTitle(title);

        if(AlarmService.alarmSound&&AlarmService.alarmVib){
            Log.d("TAG", "onReceive: alarmSound");
            builder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE);
        }
        if(AlarmService.alarmSound&&!AlarmService.alarmVib){
            Log.d("TAG", "onReceive: alarmVib");
            builder.setDefaults(Notification.DEFAULT_SOUND);
        }
        if(!AlarmService.alarmSound&&AlarmService.alarmVib){
            Log.d("TAG", "onReceive: alarmVib");
            builder.setDefaults(Notification.DEFAULT_VIBRATE);
        }

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM - dd");
        String getDate = sdf.format(date);

        Intent intent1 = new Intent(context, MainActivity.class);

        int id = 0;
        Cursor cursor = cosmeticdao.selectItemAll("endDate=?", new String[]{getDate});
        while (cursor.moveToNext()){
            id++;
            builder.setContentText(cursor.getString(1)+"의 교체/세척 권장일입니다.");
            builder.setAutoCancel(true);
            pIntent = PendingIntent.getActivity(context, 10, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pIntent);
            manager.notify(id, builder.build());
        }

        cursor = cosmeticToolsdao.selectItemAll("endDate=?", new String[]{getDate});
        while (cursor.moveToNext()){
            id++;
            builder.setContentText(cursor.getString(1)+"의 교체/세척 권장일입니다.");
            builder.setAutoCancel(true);
            pIntent = PendingIntent.getActivity(context, 10, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pIntent);
            manager.notify(id, builder.build());
        }

        cursor = lensdao.selectItemAll("endDate=?", new String[]{getDate});
        while (cursor.moveToNext()){
            id++;
            builder.setContentText(cursor.getString(1)+"의 교체/세척 권장일입니다.");
            builder.setAutoCancel(true);
            pIntent = PendingIntent.getActivity(context, 10, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pIntent);
            manager.notify(id, builder.build());
        }
    }
}
