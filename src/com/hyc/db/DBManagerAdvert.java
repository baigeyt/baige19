package com.hyc.db;

import java.io.File;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.hyc.bean.PictureId;
import com.hyc.bean.Stu;

public class DBManagerAdvert {

	public SQLiteDatabase db;

	public void creatDB() {
		File dbf = new File(getDir() + "/baige/db");
		if (!dbf.exists()) {
			dbf.mkdirs();
		}
		db = SQLiteDatabase.openOrCreateDatabase(dbf.toString() + "/"
				+ "advert.db", null);
	//	db.execSQL("DROP TABLE IF EXISTS stu");
		db.execSQL("create table if not exists advert (id INTEGER PRIMARY KEY, time TEXT)");
		System.out.println("-DBManagerAdvert---db.creatDB-----------");
	}

	public void openDB() {
		File dbf = new File(getDir() + "/baige/db"); 
		if (!dbf.exists()) {
			dbf.mkdirs();
		}
		db = SQLiteDatabase.openOrCreateDatabase(dbf.toString() + "/"
				+ "advert.db", null);
		System.out.println("---DBManagerAdvert---db.openDB-----------");
	}

	public void insert(PictureId picture) {
		if (picture != null) {
			ContentValues values = new ContentValues();
			values.put("id", picture.getId());
			values.put("time", picture.getTime());
			db.insert("advert", null, values);
		}
	}

	public String query(String val) {
		PictureId picture = new PictureId();
		String picturedq = "c";
		String[] columns = { "id", "time" };
		String[] selectionArgs = { val };
		Cursor c = db.query("advert", columns, "id=?", selectionArgs, null,
				null, null);
		/*Cursor c = db.query("stu", columns, "cardno='"+val+"'", null, null,
				null, null);*/
		while (c.moveToNext()) {
			picturedq = c.getString(c.getColumnIndex("id"));
		}
		c.close();
		return picturedq;
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
