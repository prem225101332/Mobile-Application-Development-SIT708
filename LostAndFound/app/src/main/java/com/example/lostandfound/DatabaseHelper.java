package com.example.lostandfound;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "lostandfound.db";
    private static final int DB_VERSION = 5;
    public static final String TABLE = "items";

    public static final String COL_ID       = "id";
    public static final String COL_TYPE     = "type";
    public static final String COL_NAME     = "name";
    public static final String COL_PHONE    = "phone";
    public static final String COL_DESC     = "description";
    public static final String COL_DATE     = "date";
    public static final String COL_LOCATION = "location";
    public static final String COL_CATEGORY = "category";
    public static final String COL_IMAGE    = "image";
    public static final String COL_LAT      = "latitude";
    public static final String COL_LNG      = "longitude";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE + " (" +
                COL_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TYPE     + " TEXT, " +
                COL_NAME     + " TEXT, " +
                COL_PHONE    + " TEXT, " +
                COL_DESC     + " TEXT, " +
                COL_DATE     + " TEXT, " +
                COL_LOCATION + " TEXT, " +
                COL_CATEGORY + " TEXT, " +
                COL_IMAGE    + " TEXT, " +
                COL_LAT      + " REAL DEFAULT 0.0, " +
                COL_LNG      + " REAL DEFAULT 0.0)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public long insertItem(LostFoundItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TYPE,     item.getType());
        cv.put(COL_NAME,     item.getName());
        cv.put(COL_PHONE,    item.getPhone());
        cv.put(COL_DESC,     item.getDescription());
        cv.put(COL_DATE,     item.getDate());
        cv.put(COL_LOCATION, item.getLocation());
        cv.put(COL_CATEGORY, item.getCategory());
        cv.put(COL_IMAGE,    item.getImagePath());
        cv.put(COL_LAT,      item.getLatitude());
        cv.put(COL_LNG,      item.getLongitude());
        return db.insert(TABLE, null, cv);
    }

    // GET ALL
    public List<LostFoundItem> getAllItems() {
        List<LostFoundItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                "SELECT * FROM " + TABLE + " ORDER BY " + COL_ID + " DESC", null);
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursorToItem(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }

    public List<LostFoundItem> getItemsByCategory(String category) {
        List<LostFoundItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                "SELECT * FROM " + TABLE + " WHERE " + COL_CATEGORY +
                " = ? ORDER BY " + COL_ID + " DESC",
                new String[]{category});
            if (cursor.moveToFirst()) {
                do { list.add(cursorToItem(cursor)); } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }

    public LostFoundItem getItemById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                "SELECT * FROM " + TABLE + " WHERE " + COL_ID + " = ?",
                new String[]{String.valueOf(id)});
            if (cursor.moveToFirst()) {
                return cursorToItem(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    private LostFoundItem cursorToItem(Cursor cursor) {
        LostFoundItem item = new LostFoundItem();
        item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
        item.setType(cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE)));
        item.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)));
        item.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)));
        item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESC)));
        item.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)));
        item.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COL_LOCATION)));
        item.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY)));
        item.setImagePath(cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE)));

        int latIdx = cursor.getColumnIndex(COL_LAT);
        int lngIdx = cursor.getColumnIndex(COL_LNG);
        item.setLatitude(latIdx >= 0 ? cursor.getDouble(latIdx) : 0.0);
        item.setLongitude(lngIdx >= 0 ? cursor.getDouble(lngIdx) : 0.0);

        return item;
    }
}
