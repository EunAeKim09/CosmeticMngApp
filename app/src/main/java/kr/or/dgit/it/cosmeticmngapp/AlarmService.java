package kr.or.dgit.it.cosmeticmngapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AlarmService extends Service {
    public AlarmService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent1 = new Intent();
        intent1.setAction("kr.or.dgit.it.cosmeticmngapp.PUSH_ALARM");
        sendBroadcast(intent1);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
