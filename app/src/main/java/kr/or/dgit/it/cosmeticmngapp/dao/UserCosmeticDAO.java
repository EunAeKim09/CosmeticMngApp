package kr.or.dgit.it.cosmeticmngapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import kr.or.dgit.it.cosmeticmngapp.db.DBhelper;
import kr.or.dgit.it.cosmeticmngapp.dto.UserCosmeticDTO;

public class UserCosmeticDAO {
    //테이블 이름
    static final String TABLE_NAME = "userCosmetic";
    //컬럼 이름
    static final String COL_ID = "_id";
    static final String COL_NAME = "name";
    static final String COL_IMG = "img";
    static final String COL_OPENDATE = "openDate";
    static final String COL_ENDDATE = "endDate";
    static final String COL_MEMO = "memo";
    static final String COL_FAVORITE = "favorite";
    static final String COL_CATE_ID = "cate_id";

    //selection
    static final String[] COLUMNS =  new String[]{COL_ID, COL_NAME, COL_IMG, COL_OPENDATE, COL_ENDDATE, COL_MEMO, COL_FAVORITE, COL_CATE_ID};

    private DBhelper helper;
    private SQLiteDatabase db;
    private final Context mCtx;

    public UserCosmeticDAO(Context mCtx) {
        this.mCtx = mCtx;
    }

    public void open() throws SQLException {
        helper = DBhelper.getInstance(mCtx);
        db = helper.getWritableDatabase();
    }

    public void close(){
        if (helper != null){
            helper.close();
        }
    }

    public void insertItem(UserCosmeticDTO item){
        ContentValues row = new ContentValues();
        Log.d("values",item.toString());
        row.put(COL_NAME, item.getName());
        row.put(COL_IMG, item.getImg());
        row.put(COL_OPENDATE, item.getOpenDate());
        row.put(COL_ENDDATE, item.getEndDate());
        row.put(COL_MEMO, item.getMemo());
        row.put(COL_CATE_ID, item.getCate_id());
        db.insert(TABLE_NAME, null, row);
    }

    public void deleteItemById(int id){
        db.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void updateItem(UserCosmeticDTO item){
        ContentValues row = new ContentValues();
        row.put(COL_NAME, item.getName());
        row.put(COL_IMG, item.getImg());
        row.put(COL_OPENDATE, item.getImg());
        row.put(COL_ENDDATE, item.getImg());
        row.put(COL_MEMO, item.getImg());
        row.put(COL_CATE_ID, item.getImg());
        db.update(TABLE_NAME, row, COL_ID +"=?", new String[]{String.valueOf(item.get_id())});
    }

    public void updateFavoriteItem(UserCosmeticDTO item){
        ContentValues row = new ContentValues();
        row.put(COL_FAVORITE, item.getFavorite());
        db.update(TABLE_NAME, row, COL_ID +"=?", new String[]{String.valueOf(item.get_id())});
    }

    public Cursor selectItemAll(String selection, String[] selectionArgs){
        Cursor mCursor = db.query(TABLE_NAME, COLUMNS, selection, selectionArgs,null,null,null);
        return mCursor;
    }
}
