package kr.or.dgit.it.cosmeticmngapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import kr.or.dgit.it.cosmeticmngapp.db.DBhelper;
import kr.or.dgit.it.cosmeticmngapp.dto.UserCosmetic;
import kr.or.dgit.it.cosmeticmngapp.dto.UserCosmeticTools;

public class UserCosmeticToolsDAO {
    //테이블 이름
    static final String TABLE_NAME = "userCosmeticTools";
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

    private SQLiteDatabase db;
    private final Context mCtx;

    public UserCosmeticToolsDAO(Context mCtx) {
        this.mCtx = mCtx;
    }

    public void open() throws SQLException {
        db = DBhelper.getInstance(mCtx).getDb();
    }

    public void insertItem(UserCosmeticTools item){
        ContentValues row = new ContentValues();
        row.put(COL_NAME, item.getName());
        row.put(COL_IMG, item.getImg());
        row.put(COL_OPENDATE, item.getImg());
        row.put(COL_ENDDATE, item.getImg());
        row.put(COL_MEMO, item.getImg());
        row.put(COL_CATE_ID, item.getImg());
        db.insert(TABLE_NAME, null, row);
    }

    public void deleteItemById(int id){
        db.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void updateItem(UserCosmeticTools item){
        ContentValues row = new ContentValues();
        row.put(COL_NAME, item.getName());
        row.put(COL_IMG, item.getImg());
        row.put(COL_OPENDATE, item.getImg());
        row.put(COL_ENDDATE, item.getImg());
        row.put(COL_MEMO, item.getImg());
        row.put(COL_CATE_ID, item.getImg());
        db.update(TABLE_NAME, row, COL_ID +"=?", new String[]{String.valueOf(item.get_id())});
    }

    public void updateFavoriteItem(UserCosmeticTools item){
        ContentValues row = new ContentValues();
        row.put(COL_FAVORITE, item.getFavorite());
        db.update(TABLE_NAME, row, COL_ID +"=?", new String[]{String.valueOf(item.get_id())});
    }

    public Cursor selectItemAll(String selection, String[] selectionArgs){
        Cursor mCursor = db.query(TABLE_NAME, COLUMNS, selection, selectionArgs,null,null,null);
        return mCursor;
    }

    public Cursor selectCateName(String selection, String[] selectionArgs){
        Cursor mCursor = db.query("cosmeticToolsCategory", new String[]{"name"}, selection, selectionArgs,null,null,null);
        return mCursor;
    }

    public List<UserCosmeticTools> selectAllUserCosmeticTools(){
        List<UserCosmeticTools> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select _id, name, img, openDate, endDate, memo, favorite, cate_id from userCosmeticTools", null);

        if(cursor.moveToFirst()){
            do{
                UserCosmeticTools item = new UserCosmeticTools();
                item.set_id(Integer.parseInt(cursor.getString(0)));
                item.setName(cursor.getColumnName(1));
                item.setImg(cursor.getColumnName(2));
                item.setOpenDate(cursor.getColumnName(3));
                item.setEndDate(cursor.getColumnName(4));
                item.setMemo(cursor.getColumnName(5));
                item.setFavorite(cursor.getColumnName(6));
                item.setCate_id(cursor.getColumnName(7));

                list.add(item);
            }while (cursor.moveToNext());
        }

        return list;
    }
}