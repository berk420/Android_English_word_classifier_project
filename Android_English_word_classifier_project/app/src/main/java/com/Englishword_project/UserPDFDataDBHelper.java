package com.Englishword_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserPDFDataDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyApp.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_PDF_DATA = "PDF_veri";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PDF_DATA = "pdf_data";
    private static final String COLUMN_PDF_WORD_EASY = "KOLAY_KELİME";
    private static final String COLUMN_PDF_WORD_MEDIUM= "ORTA_KELİME";
    private static final String COLUMN_PDF_WORD_HARD = "ZOR_KELİME";


    public UserPDFDataDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Kullanıcı PDF verileri tablosunu oluşturma sorgusu
        String CREATE_PDF_DATA_TABLE = "CREATE TABLE " + TABLE_PDF_DATA +
                "(" +
                COLUMN_ID + " VARCHAR(10)," +
                COLUMN_PDF_DATA + " TEXT," +
                COLUMN_PDF_WORD_EASY + " TEXT," +
                COLUMN_PDF_WORD_MEDIUM + " TEXT," +
                COLUMN_PDF_WORD_HARD + " TEXT," +
                "FOREIGN KEY(" + COLUMN_ID + ") REFERENCES " + DBHelper.TABLE_USERS + "(" + DBHelper.COLUMN_ID + ")" +
                ")";

        db.execSQL(CREATE_PDF_DATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Tabloyu yeniden oluşturma sorgusu
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PDF_DATA);
        onCreate(db);
    }

    // PDF verisi ekleme metodu
    public boolean addPDFData(int userId, String pdfData,String KOLAY_KELİME,String ORTA_KELİME,String ZOR_KELİME) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        onUpgrade(db,2,3);
        values.put(COLUMN_ID, userId);
        values.put(COLUMN_PDF_DATA, pdfData);
        values.put(COLUMN_PDF_WORD_EASY,KOLAY_KELİME);
        values.put(COLUMN_PDF_WORD_MEDIUM,ORTA_KELİME);
        values.put(COLUMN_PDF_WORD_HARD,ZOR_KELİME);

        long result = db.insert(TABLE_PDF_DATA, null, values);
        return result != -1;
    }
}
