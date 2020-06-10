package com.amin.notesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDatabase extends SQLiteOpenHelper {

  private Context context;
  private static final String DB_NAME  = "NoteApp";
  private static final int DB_VERSION = 1;
  private static final String TBL_NAME = "Notes";
  private static final String COL_ID = "id";
  private static final String COL_TITLE = "title";
  private static final String COL_DESC = "desc";
  private static final String COL_TIME = "time";
  private static final String COL_DATE = "date";
  private static final String COL_CTIME = "ctime";
  private static final String COL_REMEMBER = "remember";


  private static final String QUERY = "CREATE TABLE IF NOT EXISTS "+TBL_NAME+" ( "+
          COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT , "+
          COL_TITLE+" TEXT , "+
          COL_DESC+" TEXT , "+
          COL_TIME+" TEXT , "+
          COL_DATE+" TEXT , "+
          COL_CTIME+" TEXT ,"+
          COL_REMEMBER+" INTEGER );";

  public MyDatabase(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
    this.context=context;
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    try {
      sqLiteDatabase.execSQL(QUERY);
    }catch (SQLException e){
      e.printStackTrace();
    }

  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

  }

  public long AddNotes(String title,String desc,String time,String date,String ctime,int remember){
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(COL_TITLE,title);
    contentValues.put(COL_DESC,desc);
    contentValues.put(COL_TIME,time);
    contentValues.put(COL_DATE,date);
    contentValues.put(COL_CTIME,ctime);
    contentValues.put(COL_REMEMBER,remember);
    long id = sqLiteDatabase.insert(TBL_NAME,null,contentValues);
    return id;
  }

  public Cursor GetNotes(){
    SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TBL_NAME,null);
    return cursor;
  }

  public Cursor GetRememberNote(int remember){
    SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TBL_NAME+" WHERE "+COL_REMEMBER+" = ?",new String[]{String.valueOf(remember)});
    return cursor;
  }


  public void DeleteNotes(int id){
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    sqLiteDatabase.delete(TBL_NAME,COL_ID+" = ?",new String[]{String.valueOf(id)});
  }

  public void UpdateNote(int id , String title,String desc){
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(COL_TITLE,title);
    contentValues.put(COL_DESC,desc);
    sqLiteDatabase.update(TBL_NAME,contentValues,COL_ID+" = ?",new String[]{String.valueOf(id)});
  }
}
