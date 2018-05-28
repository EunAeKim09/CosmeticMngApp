package kr.or.dgit.it.cosmeticmngapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION=1;

    public DBhelper(Context context){
        super(context, "datadb", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String cosCate="create table cosmeticCategory ("+
                "_id integer primary key autoincrement," +
                "name not null," +
                "duration)";

        db.execSQL(cosCate);

        db.execSQL("insert into cosmeticCategory (name,duration) values ('스킨/토너/로션','1년')");
        db.execSQL("insert into cosmeticCategory (name,duration) values ('에센스/세럼/아이크림','6개월')");
        db.execSQL("insert into cosmeticCategory (name,duration) values ('크림','1년')");
        db.execSQL("insert into cosmeticCategory (name,duration) values ('메이크업베이스','6개월')");
        db.execSQL("insert into cosmeticCategory (name,duration) values ('파운데이션','1년')");
        db.execSQL("insert into cosmeticCategory (name,duration) values ('스틱커버/컨실러','1년')");
        db.execSQL("insert into cosmeticCategory (name,duration) values ('파우더/팩트','1년')");
        db.execSQL("insert into cosmeticCategory (name,duration) values ('아이섀도/블러셔','6개월')");
        db.execSQL("insert into cosmeticCategory (name,duration) values ('립스틱/립글로스/틴트','1년')");
        db.execSQL("insert into cosmeticCategory (name,duration) values ('아이라이너','1년')");
        db.execSQL("insert into cosmeticCategory (name,duration) values ('마스카라','6개월')");
        db.execSQL("insert into cosmeticCategory (name,duration) values ('클렌징','1년')");
        db.execSQL("insert into cosmeticCategory (name,duration) values ('자외선 차단제','6개월')");
        db.execSQL("insert into cosmeticCategory (name,duration) values ('네일/애나멜','2년')");
        db.execSQL("insert into cosmeticCategory (name,duration) values ('향수','2017-06-28')");
        db.execSQL("insert into cosmeticCategory (name,duration) values ('팩','1년')");
        db.execSQL("insert into cosmeticCategory (name,duration) values ('염색약','1년')");

        String cosTools="create table cosmeticTools ("+
                "_id integer primary key autoincrement," +
                "name not null," +
                "duration)";

        db.execSQL(cosTools);

        db.execSQL("insert into cosmeticTools (name,duration) values ('퍼프/스펀지','7일')");
        db.execSQL("insert into cosmeticTools (name,duration) values ('립 브러쉬(립용)','7일')");
        db.execSQL("insert into cosmeticTools (name,duration) values ('파운데이션 브러쉬','7일')");
        db.execSQL("insert into cosmeticTools (name,duration) values ('컨실러 브러쉬','7일')");
        db.execSQL("insert into cosmeticTools (name,duration) values ('파우더 브러쉬','21일')");
        db.execSQL("insert into cosmeticTools (name,duration) values ('뷰러','3개월')");

        String lens="create table lensCategory ("+
                "_id integer primary key autoincrement," +
                "name not null," +
                "duration)";

        db.execSQL(lens);

        String userCosmetic="create table userCosmetic ("+
                "_id integer primary key autoincrement," +
                "cate_id," +
                "name not null," +
                "openDate," +
                "endDate," +
                "memo," +
                "favorite," +
                " FOREIGN KEY (cate_id) REFERENCES cosmeticCategory(_id))";
        db.execSQL(userCosmetic);

        String userCosmeticTools="create table userCosmeticTools ("+
                "_id integer primary key autoincrement," +
                "cate_id," +
                "name not null," +
                "openDate," +
                "endDate," +
                "memo," +
                "favorite," +
                " FOREIGN KEY (cate_id) REFERENCES cosmeticTools(_id))";
        db.execSQL(userCosmeticTools);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == DATABASE_VERSION){
            db.execSQL("drop table tb_data");
            onCreate(db);
        }
    }
}
