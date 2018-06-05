package kr.or.dgit.it.cosmeticmngapp.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import kr.or.dgit.it.cosmeticmngapp.db.DBhelper;

public class CosmeticCategoryDAO {
    private String tblName="cosmeticCategory";
    static  final String COL_ID="_id";
    static final String COL_NAME="name";
    static final  String COL_DURATIONY = "durationY";
    static final  String COL_DURATIONM="durationM";
    static final String COL_DURATIOND="durationD";

    private DBhelper helper;
    private SQLiteDatabase db;
    private final Context mCtx;

    public CosmeticCategoryDAO(Context mCtx) {
        this.mCtx = mCtx;
    }


    public void close(){
        if(helper != null){
            helper.close();
        }
    }

    public void open(){
        helper = DBhelper.getInstance(mCtx);
        db = helper.getWritableDatabase();
    }
}
