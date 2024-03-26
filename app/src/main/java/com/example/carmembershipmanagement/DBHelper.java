package com.example.carmembershipmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "CarData.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table CarData (number Text primary key, registered Text, expired Text, lastUsed Text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop Table if exists CarData");
    }

    public Boolean insertCarData(String carNumber, String created, String expired) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("number", carNumber);
        contentValues.put("registered", created);
        contentValues.put("expired", expired);
        contentValues.put("lastUsed", "");
        long result = db.insert("CarData", null, contentValues);
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public Boolean updateCarData(String carNumber, String created, String expired, String lastUsed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("registered", created.toString());
        contentValues.put("expired", expired.toString());
        contentValues.put("lastUsed", lastUsed.toString());
        Cursor cursor = db.rawQuery("Select * from CarData where number = ?", new String[]{carNumber});
        if (cursor.getCount() > 0) {
            long result = db.update("CarData", contentValues, "number = ?", new String[]{carNumber});
            if (result == -1) {
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }

    public Boolean updateLastUsed(String carNumber, String created, String expired, String lastUsed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("registered", created);
        contentValues.put("expired", expired);
        contentValues.put("lastUsed", lastUsed);
        Cursor cursor = db.rawQuery("Select * from CarData where number = ?", new String[]{carNumber});
        if (cursor.getCount() > 0) {
            long result = db.update("CarData", contentValues, "number = ?", new String[]{carNumber});
            if (result == -1) {
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from CarData", null);
        return cursor;
    }

    public Cursor getDataByNumber(String carNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from CarData where number LIKE ?", new String[]{"%" + carNumber + "%"});
        return cursor;
    }

    public void deleteWhole() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from CarData");
    }

    public void deleteSpecificRow(String carNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from CarData where number = ?", new String[]{carNumber});
    }
}
