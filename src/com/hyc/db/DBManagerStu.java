package com.hyc.db;

import java.io.File;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.hyc.bean.Stu;

public class DBManagerStu {
	public SQLiteDatabase db;
	public static int type;

	public void creatDB() {
		File dbf = new File(getDir() + "/baige/db");
		if (!dbf.exists()) {
			dbf.mkdirs();
		}
		db = SQLiteDatabase.openOrCreateDatabase(dbf.toString() + "/"
				+ "stu.db", null);
		//db.execSQL("DROP TABLE IF EXISTS stu");
		//db.execSQL("create table stu (_id INTEGER PRIMARY KEY, cardno VARCHAR, type INTEGER, name VARCHAR, classname VARCHAR)");
		db.execSQL("create table if not exists stu (_id INTEGER PRIMARY KEY, cardno VARCHAR, type INTEGER, name VARCHAR, classname VARCHAR)");
		db.execSQL("create table if not exists up (_id INTEGER PRIMARY KEY,date VARCHAR)");
		System.out.println("---DBManagerStu---db.creatDB-----------");
	}

	public void openDB() {
		File dbf = new File(getDir() + "/baige/db"); 
		if (!dbf.exists()) {
			dbf.mkdirs();
		}
		db = SQLiteDatabase.openOrCreateDatabase(dbf.toString() + "/"
				+ "stu.db", null);
		System.out.println("---DBManagerStu---db.openDB-----------");
	}
	public void insert(Stu stu) {
		if (stu != null) {
//			db.beginTransaction();
			ContentValues values = new ContentValues();
			values.put("cardno", stu.getCardno());
			values.put("type", stu.getType());
			values.put("name", stu.getName());
			values.put("classname", stu.getClassname());
			db.insert("stu", "_id", values);
//			db.endTransaction();
		}
	}

	public Stu query(String val) {
		Stu stu = new Stu();
		String[] columns = { "cardno", "type", "name", "classname" };
		String[] selectionArgs = { val };
		Cursor c = db.query("stu", columns, "cardno=?", selectionArgs, null,
				null, null);
		/*Cursor c = db.query("stu", columns, "cardno='"+val+"'", null, null,
				null, null);*/
		while (c.moveToNext()) {
			stu.setType(c.getInt(c.getColumnIndex("type")));
			stu.setName(c.getString(c.getColumnIndex("name")));
			stu.setClassname(c.getString(c.getColumnIndex("classname")));
		}
		c.close();
		return stu;
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
	
	public void insertDate(String date) {
		db.delete("up", null, null);
		if (date != null) {
			ContentValues values = new ContentValues();
			values.put("date", date);
			db.insert("up", null, values);
		}
	}
	
	public String queryDate() {
		String date = null;
		Cursor c = db.query("up", null, null, null, null,
				null, null);

		while (c.moveToNext()) {
			date = c.getString(c.getColumnIndex("date"));
		}
		c.close();
		return date;
	}
	
	public void delete(){
		db.delete("stu", null, null);
	}
	
	/*
	 * YT 判断数据库 是否为存在
	 */
	public boolean isBase() {
		File dbf = new File(getDir() + "/baige/db");
		if (dbf.exists()) {
			return true;
		}
		return false;
	}
}