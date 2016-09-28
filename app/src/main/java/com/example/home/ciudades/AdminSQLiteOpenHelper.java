package com.example.home.ciudades;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Home on 22/08/2016.
 */
public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    public AdminSQLiteOpenHelper(Context context, String nombre, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, nombre, factory, version);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ciudad(id INTEGER PRIMARY KEY AUTOINCREMENT, ciudad TEXT, img TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionActual) {
        db.execSQL("DROP TABLE IF EXISTS ciudad");
        db.execSQL("CREATE TABLE ciudad(id INTEGER PRIMARY KEY AUTOINCREMENT, ciudad TEXT, img TEXT)");
    }
}
