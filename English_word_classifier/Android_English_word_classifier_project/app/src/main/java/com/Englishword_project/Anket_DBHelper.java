package com.Englishword_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Anket_DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyApp.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_anket = "Anket";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ALLOW ="onay";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_medenidrm = "medenidrm";
    private static final String COLUMN_gelir = "gelir";
    private static final String COLUMN_bolge = "bolge";

    public Anket_DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Yaş tablosunu oluşturma sorgusu
        String CREATE_AGE_TABLE = "CREATE TABLE " + TABLE_anket +
                "(" +
                COLUMN_ID + " VARCHAR(50) PRIMARY KEY," +
                COLUMN_ALLOW + " VARCHAR(10)," +
                COLUMN_AGE + " NUMBER(100)," +
                COLUMN_medenidrm + " VARCHAR(10)," +
                COLUMN_gelir + " NUMBER(100000)," +
                COLUMN_bolge + " VARCHAR(10)," +
                "FOREIGN KEY(" + COLUMN_ID + ") REFERENCES " + DBHelper.TABLE_USERS + "(" + DBHelper.COLUMN_ID + ")" +
                ")";

        db.execSQL(CREATE_AGE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Yaş tablosunu yeniden oluşturma sorgusu
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_anket);
        onCreate(db);
    }

    // Kullanıcının yaşını ekleme metodu
    public boolean addAgeData(int userId,String onay ,String age,String medenidrm,String gelir,String bolge) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        onUpgrade(db,2,3);//bunu sürekli çalıştıma
        values.put(COLUMN_ID, userId);
        values.put(COLUMN_ALLOW,onay);
        values.put(COLUMN_AGE, age);
        values.put(COLUMN_medenidrm, medenidrm);
        values.put(COLUMN_gelir, gelir);
        values.put(COLUMN_bolge, bolge);
        //return db.insert(TABLE_AGE, null, values);
        long result = db.insert(TABLE_anket, null, values);
        return result != -1; // Ekleme başarılıysa true, aksi halde false döndürülür
    }
}
