package com.hyc.db;

import java.io.File;

import com.hyc.bean.AppId;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class DBManagerBase {
	private SQLiteDatabase db;
	public static int type;

	public void creatDB() {
		File dbf = new File(Environment.getExternalStorageDirectory()
				+ "/baige/db");
		if (!dbf.exists()) {
			dbf.mkdirs();
		}
		db = SQLiteDatabase.openOrCreateDatabase(dbf.toString() + "/"
				+ "base.db", null);
		// db.execSQL("DROP TABLE IF EXISTS id");
		// db.execSQL("create table id(id INTEGER PRIMARY KEY AutoIncrement, appid VARCHAR(50), appsecret VARCHAR(50))");

		db.execSQL("create table if not exists id(id INTEGER PRIMARY KEY AutoIncrement, appid VARCHAR(50), appsecret VARCHAR(50))");
		System.out.println("----DBManagerBase--db.creatDB-----------");
	}

	public void openDB() {
		File dbf = new File(Environment.getExternalStorageDirectory()
				+ "/baige/db");
		if (!dbf.exists()) {
			dbf.mkdirs();
		}
		db = SQLiteDatabase.openOrCreateDatabase(dbf.toString() + "/"
				+ "base.db", null);
		System.out.println("---DBManagerBase---db.openDB-----------");
	}

	public void insert(AppId id) {
		if (id != null) {
			ContentValues values = new ContentValues();
			values.put("appid", id.getAppid());
			values.put("appsecret", id.getAppsecret());
			db.insert("id", null, values);
		}
	}

	public AppId query() {
		AppId appId = new AppId();
		String[] columns = { "appid", "appsecret" };
		String[] selectionArgs = { "1" };
		Cursor c = db.query("id", columns, "id=?", selectionArgs, null, null,
				null);
		while (c.moveToNext()) {
			appId.setAppid(c.getString(c.getColumnIndex("appid")));
			appId.setAppsecret(c.getString(c.getColumnIndex("appsecret")));
		}
		c.close();
		return appId;
	}

	public void deleteDB() {
		db.execSQL("DROP TABLE IF EXISTS id");
	}

	public void closeDB() {
		db.close();
	}

}