package com.hyc.db;

import java.io.File;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class DBManagerAccessToken {
	private SQLiteDatabase db;

	public void creatDB() {
		File dbf = new File(getDir() + "/baige/db");
		if (!dbf.exists()) {
			dbf.mkdirs();
		}
		db = SQLiteDatabase.openOrCreateDatabase(dbf.toString() + "/"
				+ "accesstoken.db", null);
		// db.execSQL("DROP TABLE IF EXISTS accesstoken");
		db.execSQL("create table if not exists accesstoken (_id INTEGER PRIMARY KEY,token VARCHAR not null)");
		System.out.println("------DBManagerAccessToken.creatDB-----------");
	}

	public void insert(String token) {
		ContentValues values = new ContentValues();
		values.put("token", token);
		db.insert("accesstoken", "_id", values);
	}

	
	
	public String query() {
		String token=null;
		String[] columns = { "token" };
		String[] selectionArgs = { "1" };
		Cursor c = db.query("accesstoken", columns, "_id=?", selectionArgs,
				null, null, null);

		while (c.moveToNext()) {
			System.out.println("0000000");
			token = c.getString(c.getColumnIndex("token"));
			return token;
		}
		c.close();
		return token;
	}

	public void delete() {
		db.delete("accesstoken", null, null);
	}
	
	public void closeDB() {
		db.close();
	}
	
	
	private File getDir() {
		File dir = Environment.getExternalStorageDirectory();
		if (dir.exists()) {
			return dir;
		} else {
			dir.mkdirs();
			return dir;
		}
	}
}
