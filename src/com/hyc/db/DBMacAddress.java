package com.hyc.db;

import java.io.File;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.hyc.bean.MacEntity;

public class DBMacAddress {


	private SQLiteDatabase db;

	public void creatDB() {
		File dbf = new File(getDir() + "/baige/db");
		if (!dbf.exists()) {
			dbf.mkdirs();
		}
		db = SQLiteDatabase.openOrCreateDatabase(dbf.toString() + "/"
				+ "mac.db", null);
		// db.execSQL("DROP TABLE IF EXISTS mac");
		db.execSQL("create table if not exists mac (_id INTEGER PRIMARY KEY,mac_address VARCHAR not null)");
		System.out.println("------DBMacAddress.creatDB-----------");
	}

	public void creatDB_ID() {
		File dbf = new File(getDir() + "/baige/db");
		if (!dbf.exists()) {
			dbf.mkdirs();
		}
		db = SQLiteDatabase.openOrCreateDatabase(dbf.toString() + "/"
				+ "mac.db", null);
		db.execSQL("create table if not exists devicerstb (_id INTEGER PRIMARY KEY,devicers_id VARCHAR not null)");
		System.out.println("------DBMacAddress.creatDB_ID-----------");
	}
	

	public void insert(String mac) {
			ContentValues values = new ContentValues();
			values.put("mac_address", mac);
			db.insert("mac", "_id", values);
	}

	public void insert_ID(MacEntity macEntity) {
		if (macEntity != null) {
			ContentValues values = new ContentValues();
			values.put("devicers_id", macEntity.getDevices());
			db.insert("devicerstb", "_id", values);
		}
	}
	
	public MacEntity query() {
		MacEntity macEntity = new MacEntity();
		String[] columns = { "mac_address" };
		String[] selectionArgs = { "1" };
		Cursor c = db.query("mac", columns, "_id=?", selectionArgs, null, null,
				null);

		while (c.moveToNext()) {
			System.out.println("0000000");
			macEntity.setMac(c.getString(c.getColumnIndex("mac_address")));
		
		}
		c.close();
		return macEntity;
	}
	
	public MacEntity query_ID() {
		MacEntity macEntity = new MacEntity();
		String[] columns = { "devicers_id" };
		String[] selectionArgs = { "1" };
		Cursor c = db.query("devicerstb", columns, "_id=?", selectionArgs, null, null,
				null);

		while (c.moveToNext()) {
			System.out.println("0000000");
			macEntity.setDevices(c.getString(c.getColumnIndex("devicers_id")));
		
		}
		c.close();
		return macEntity;
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
	public void closeDBMac(){
		db.close();
	}


	
}
