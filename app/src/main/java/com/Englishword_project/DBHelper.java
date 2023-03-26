package com.Englishword_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Login.db";
    private Connection connection;

    public DBHelper(Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("DROP TABLE users");
        MyDB.execSQL("create Table users(username TEXT primary key, password TEXT,kullanıcı_yas TEXT, kullanıcı_meslek TEXT,pdfdata TEXT,Pdfdata_kolay_words TEXT,Pdfdata_orta_words TEXT,Pdfdata_zor_words TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
    }

    public Boolean insertData(String username, String password,String kullanıcı_yas,String kullanıcı_meslek,String pdfdata,String Pdfdata_kolay_words,String Pdfdata_orta_words,String Pdfdata_zor_words){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        //Bunu bir defa çalıştırdık ki tablomuzu düzeltelim
        //onCreate(MyDB);

        //HATTA BUNU FARKLI BİR ŞEKİLDE           USERNAME VE ŞİFREYE SONRADAN DATA EKLEMEK İÇİN DE KULLANABLİRİZ

        ContentValues contentValues= new ContentValues();

        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("pdfdata",pdfdata);
        contentValues.put("Pdfdata_kolay_words",Pdfdata_kolay_words);
        contentValues.put("Pdfdata_orta_words",Pdfdata_orta_words);
        contentValues.put("Pdfdata_zor_words",Pdfdata_zor_words);
        contentValues.put("kullanıcı_yas",kullanıcı_yas);
        contentValues.put("kullanıcı_meslek",kullanıcı_meslek);
       // contentValues.put("anket_durumu",anket_durumu);


/*
        if(pdfdata!=null)
        {
            //Cursor cursor = MyDB.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[]{username, password});


            if (cursor.moveToFirst()) {
                contentValues.put("pdfdata",pdfdata);
                MyDB.update("users", contentValues, "username = ?", new String[]{cursor.getString(0)});
            }
            cursor.close();
            MyDB.close();


        }




  //Eğer id ekleyecek olursak
        if (cursor.moveToFirst()) {
            values.put("age", age);
            db.update("users", values, "id = ?", new String[]{cursor.getString(0)});
        }

 */


        long result = MyDB.insert("users", null, contentValues);
        if(result==-1) return false;
        else
            return true;
    }
    public int getUserAnketState(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        int anket_durumu = 0;
        Cursor cursor = db.query("users", new String[] { "anket_durumu" }, "username = ?", new String[] { username }, null, null, null);
        if (cursor.moveToFirst()) {
            anket_durumu = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return anket_durumu;
    }


    public Boolean checkusername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[] {username,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public Boolean addOne(String pdfdata){

        SQLiteDatabase MyDB =this.getWritableDatabase();

        ContentValues contentValues =new ContentValues();
       // Cursor cursor = MyDB.rawQuery("DELETE FROM my_table WHERE username = username", new String[] {username});

        contentValues.put("pdfdata",pdfdata);

        //Şu hali ile çalışıyor

        String query = "INSERT INTO mytable (username, password, pdfdata) VALUES (?, ?, ?);";
        String[] args = {"John Doe", "30", pdfdata};
        MyDB.execSQL(query, args);



        String updateQuery = "UPDATE users SET pdfdata = ? WHERE username = ?";


        long result =MyDB.insert("users",null,contentValues);

        if(result==-1) return false;
        else
            return true;
    }


}