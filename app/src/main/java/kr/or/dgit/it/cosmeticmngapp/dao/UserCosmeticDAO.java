package kr.or.dgit.it.cosmeticmngapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.or.dgit.it.cosmeticmngapp.db.DBhelper;
import kr.or.dgit.it.cosmeticmngapp.dto.UserCosmetic;

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
    private static final String TAG = "UserDao";

    private SQLiteDatabase db;
    private final Context mCtx;

    public UserCosmeticDAO(Context mCtx) {
        this.mCtx = mCtx;
        open();
    }

    public void open() throws SQLException {
        if (db==null) {
            db = DBhelper.getInstance(mCtx).getDb();
        }
    }

    public void insertItem(UserCosmetic item){
        ContentValues row = new ContentValues();
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

    public void deleteItemByAll(){
        db.delete(TABLE_NAME,null,null);
    }

    /*public void deleteItemBySelectId(HashMap<Object,String> map){
        db.delete();
    }*/

    public void updateItem(UserCosmetic item){
        ContentValues row = new ContentValues();
        row.put(COL_NAME, item.getName());
        row.put(COL_IMG, item.getImg());
        row.put(COL_OPENDATE, item.getOpenDate());
        row.put(COL_ENDDATE, item.getEndDate());
        row.put(COL_MEMO, item.getMemo());
        row.put(COL_CATE_ID, item.getCate_id());
        db.update(TABLE_NAME, row, COL_ID +"=?", new String[]{String.valueOf(item.get_id())});
    }

    public void updateFavoriteItem(UserCosmetic item){
        ContentValues row = new ContentValues();
        row.put(COL_FAVORITE, item.getFavorite());
        db.update(TABLE_NAME, row, COL_ID +"=?", new String[]{String.valueOf(item.get_id())});
    }

    public Cursor selectItemAll(String selection, String[] selectionArgs){
        Cursor mCursor = db.query(TABLE_NAME, COLUMNS, selection, selectionArgs,null,null,null);
        return mCursor;
    }

    public Cursor selectCateName(String selection, String[] selectionArgs){
        Cursor mCursor = db.query("cosmeticCategory", new String[]{"name"}, selection, selectionArgs,null,null,null);
        return mCursor;
    }

    public List<UserCosmetic> selectAllUserCosmetic(){
        List<UserCosmetic> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select _id, name, img, openDate, endDate, memo, favorite, cate_id from userCosmetic", null);

        if(cursor.moveToFirst()){
            do{
                UserCosmetic userCosmetic = new UserCosmetic();
                userCosmetic.set_id(Integer.parseInt(cursor.getString(0)));
                userCosmetic.setName(cursor.getColumnName(1));
                userCosmetic.setImg(cursor.getColumnName(2));
                userCosmetic.setOpenDate(cursor.getColumnName(3));
                userCosmetic.setEndDate(cursor.getColumnName(4));
                userCosmetic.setMemo(cursor.getColumnName(5));
                userCosmetic.setFavorite(Integer.parseInt(cursor.getColumnName(6)));
                userCosmetic.setCate_id(Integer.parseInt(cursor.getColumnName(7)));

                list.add(userCosmetic);
            }while (cursor.moveToNext());
        }

        return list;
    }
}
