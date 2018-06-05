package kr.or.dgit.it.cosmeticmngapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DBhelper extends SQLiteOpenHelper{
    private static final  String TAG = DBhelper.class.getSimpleName();
    public static final int DATABASE_VERSION=6;
    private static final String DB_NAME = "datadb.db";
    private final  Context context;
    private static DBhelper instance;

    public synchronized  static DBhelper getInstance(Context context){
        if(instance == null){
            instance = new DBhelper(context);
        }
        return instance;
    }

    public DBhelper(Context context){
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        applySqlFile(db,DATABASE_VERSION,"cosmetic.sql");
    }

    private void applySqlFile(SQLiteDatabase db, int databaseVersion, String fileName) {
        String filename = fileName;

        try(final InputStream inputStream = context.getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));) {
            final StringBuilder statement = new StringBuilder();

            for(String line; (line = reader.readLine()) != null;){
                if(!TextUtils.isEmpty(line) && !line.startsWith("--")){
                    statement.append(line.trim());
                }
                if(line.endsWith(";")){
                    db.execSQL(statement.toString());
                    statement.setLength(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"Could not apply SQL file - >"+e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == DATABASE_VERSION){
            db.execSQL("drop table cosmeticCategory");
            db.execSQL("drop table cosmeticToolsCategory");
            db.execSQL("drop table lensCategory");
            db.execSQL("drop table userCosmetic");
            db.execSQL("drop table userCosmeticTools");
            db.execSQL("drop table userLens");
            onCreate(db);
        }
    }
}
