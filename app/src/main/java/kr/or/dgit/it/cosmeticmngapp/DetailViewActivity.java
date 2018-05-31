package kr.or.dgit.it.cosmeticmngapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DetailViewActivity extends AppCompatActivity {

    private static final String TAG = "DetailViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_detail_view);
        TextView tv = findViewById(R.id.detailTV);

        String num = getIntent().getStringExtra("idNum");
        Log.d(TAG, "Num : "+num);
        /*tv.setText(num);*/
    }
}
