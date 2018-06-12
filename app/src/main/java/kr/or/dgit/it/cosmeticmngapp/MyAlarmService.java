package kr.or.dgit.it.cosmeticmngapp;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

public class MyAlarmService extends IntentService {
    private static final String ACTION_FOO = "kr.or.dgit.it.cosmeticmngapp.action.FOO";
    private static final String ACTION_BAZ = "kr.or.dgit.it.cosmeticmngapp.action.BAZ";

    private static final String EXTRA_PARAM1 = "kr.or.dgit.it.cosmeticmngapp.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "kr.or.dgit.it.cosmeticmngapp.extra.PARAM2";

    public MyAlarmService() {
        super("MyAlarmService");
    }

    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyAlarmService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyAlarmService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("TAG", "MyAlarmService: ");
        Intent intent1 = new Intent();
        intent1.setAction("kr.or.dgit.it.cosmeticmngapp.PUSH_ALARM");
        sendBroadcast(intent1);

        /*if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }*/
    }

    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
