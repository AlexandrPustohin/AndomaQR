package com.example.andomaqr.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class AndomaSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "andomadb";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "DOCUMENT";
    public static final String GUID_DOC = "GUID_DOC";
    public static final String SEND = "SEND";

    public AndomaSQLiteOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            String strEx = "CREATE TABLE "+
                    TABLE_NAME+
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    GUID_DOC +" TEXT, "
                    +  SEND+" NUMERIC);";
            //System.out.println(strEx);
            db.execSQL(strEx);
        }
        if (oldVersion < 2) {
//Код добавления нового столбца
        }

    }

    public static void insertDocument(SQLiteDatabase db, String doc ) {
        ContentValues docGuid = new ContentValues();
        docGuid.put("GUID_DOC", doc);//гуид документа
        docGuid.put("SEND", 0);//не отправлено
        db.insert(TABLE_NAME, null, docGuid);
        db.close();
    }

    public static String getDocById(SQLiteDatabase db, int id){
        String res="Какой то результат... ";
        Cursor cursor = db.query(TABLE_NAME,
                            new String[]{"_id", GUID_DOC, SEND},
                    "_id = ?", new String[]{Integer.toString(id)}
                            ,null, null, null);
        if (cursor.moveToFirst()) {
            //Получение данных напитка из курсора
            res = cursor.getString(1);
        }
        cursor.close();
        db.close();
        return res;
    }
}
