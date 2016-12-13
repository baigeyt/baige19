package com.hyc.db;

import java.io.File;

import com.hyc.bean.PictureId;
import com.hyc.bean.Stu;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class DBManagerSchPic {

	public SQLiteDatabase db;
	public static int type;

	public void creatDB() {
		File dbf = new File(getDir() + "/baige/db");
		if (!dbf.exists()) {
			dbf.mkdirs();
		}
		db = SQLiteDatabase.openOrCreateDatabase(dbf.toString() + "/"
				+ "schpic.db", null);
		// db.execSQL("DROP TABLE IF EXISTS schpic");
		db.execSQL("create table if not exists first (_id INTEGER PRIMARY KEY,id INTEGER, time TEXT, url TEXT)");
		db.execSQL("create table if not exists second (_id INTEGER PRIMARY KEY,id INTEGER, time TEXT, url TEXT)");
		db.execSQL("create table if not exists third (_id INTEGER PRIMARY KEY,id INTEGER, time TEXT, url TEXT)");
		db.execSQL("create table if not exists four (_id INTEGER PRIMARY KEY,id INTEGER, time TEXT, url TEXT)");
		System.out.println("------db.creatDB-----------");
	}

	public void openDB() {
		File dbf = new File(getDir() + "/baige/db");
		if (!dbf.exists()) {
			dbf.mkdirs();
		}
		db = SQLiteDatabase.openOrCreateDatabase(dbf.toString() + "/"
				+ "schpic.db", null);
		System.out.println("------db.openDB-----------");
	}

	public void insert(PictureId picture, String table) {
		if (picture != null) {
			ContentValues values = new ContentValues();
			values.put("id", picture.getId());
			values.put("time", picture.getTime());
			db.insert(table, null, values);
		}
	}

	public void closeDB() {
		db.close();
	}

	private File getDir() {
		// 得到SD卡根目录
		File dir = Environment.getExternalStorageDirectory();
		if (dir.exists()) {
			return dir;
		} else {
			dir.mkdirs();
			return dir;
		}
	}
}
