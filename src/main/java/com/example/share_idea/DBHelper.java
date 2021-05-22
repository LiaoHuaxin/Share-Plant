package com.example.share_idea;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    // 版本
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Step_database.db";
    // Database Table Name
    private static final String TABLE_NAME = "Step1";
    //Column Name
    private static final String KEY_ID = "id";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_IMG = "image";

    //新增資料表
    public static final String IMAGE_EX = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+KEY_CONTENT+ " TEXT," + KEY_IMG+" BLOB );";
    private final Context context;

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }
    public void insertData(String content, byte[] image){
        //寫法1
//        SQLiteDatabase db = this.getWritableDatabase();

//        String sql = "INSERT INTO Step_storage VALUES(NULL, ?, ?)";
//        SQLiteStatement statement = db.compileStatement(sql);
//        statement.clearBindings();
//
//        statement.bindString(1,content);
//        statement.bindBlob(2, image);
//
//        statement.executeInsert();

        //寫法2
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CONTENT,content);
        contentValues.put(KEY_IMG,image);

        long result = db.insert(TABLE_NAME, null,contentValues);
        if (result == -1){
            Toast.makeText(context,"Failed", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context,"Successfully", Toast.LENGTH_SHORT).show();
        }
    }
    public int updateData(String id, String content, byte[] image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Values = new ContentValues();
        Values.put(KEY_CONTENT, content);
        Values.put(KEY_IMG, image);

        return db.update(TABLE_NAME, Values, KEY_ID + "=" + id, null);
    }
    //deleteData
    public int deleteData(int id){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(TABLE_NAME, KEY_ID + "=" + id, null);
    }
    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    public void deleteAll() {
        String sql = "DELETE FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        //long result = db.delete(TABLE_NAME, null, null);
        db.execSQL(sql);
//        if (result == -1){
//            Toast.makeText(context,"Failed", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            Toast.makeText(context,"Successfully", Toast.LENGTH_SHORT).show();
//        }
    }
    public void deleteTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
    public void CreateTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(IMAGE_EX);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(IMAGE_EX);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
