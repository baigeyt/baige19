package com.hyc.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Db extends SQLiteOpenHelper {
    static  final String dbName = "uploadbaige";
    static final Integer dbVersion = 1;
    SQLiteDatabase dbWirter;
    SQLiteDatabase dbReader;
    public Db(Context context) {
        super(context, dbName, null, dbVersion);
        dbWirter = getWritableDatabase();
        dbReader = getReadableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE filepaths(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "uploadpaths TEXT  DEFAULT\"\")");
        
        db.execSQL("CREATE TABLE allpaths(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "type TEXT  DEFAULT\"\"," +
                "cardno TEXT  DEFAULT\"\"," +
                "timecode TEXT  DEFAULT\"\"," +
//                "name TEXT  DEFAULT\"\"," +
//                "class TEXT  DEFAULT\"\"," +
//                "num TEXT  DEFAULT\"\"," +
//                "display TEXT  DEFAULT\"\"," +
                "alluploadpaths TEXT  DEFAULT\"\")");
        db.execSQL("CREATE TABLE twoup(" +
                "uptwo TEXT  DEFAULT\"\")");


    }
    
    public void insertTwo(String twoUp) {
		dbWirter.delete("twoup", null, null);
		if (twoUp != null) {
			ContentValues values = new ContentValues();
			values.put("uptwo", twoUp);
			dbWirter.insert("twoup", null, values);
		}
	}
    
    public String queryTwo() {
		String two = "c";
		Cursor c = dbReader.query("twoup", null, null, null, null,
				null, null);

		while (c.moveToNext()) {
			two = c.getString(c.getColumnIndex("uptwo"));
		}
		c.close();
		return two;
	}
    
    public void closeDB(){
    	dbWirter.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    /** 
     * @param context 
     * @return 
     */

    public boolean deleteDatabase(Context context) {  
    return context.deleteDatabase("uploadbaige");  
    }  
    
}
