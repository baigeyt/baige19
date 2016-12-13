package com.hyc.db;

import java.io.File;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class DBManagerCard {
	private SQLiteDatabase db;
	public static int type;

	public void creatDB() {
		File dbf = new File(getDir() + "/baige/db");
		if (!dbf.exists()) {
			dbf.mkdirs();
		}
		db = SQLiteDatabase.openOrCreateDatabase(dbf.toString() + "/"
				+ "card.db", null);
		db.execSQL("DROP TABLE IF EXISTS card");
		db.execSQL("create table if not exists card (cardno INTEGER PRIMARY KEY)");
		System.out.println("------db.creatDB-----------");
	}

	public void openDB() {
		File dbf = new File(getDir() + "/baige/db");
		if (!dbf.exists()) {
			dbf.mkdirs();
		}
		db = SQLiteDatabase.openOrCreateDatabase(dbf.toString() + "/"
				+ "card.db", null);
		System.out.println("------db.openDB-----------");
	}

	public void insert(String card) {
		if (card != null) {
			ContentValues values = new ContentValues();
			values.put("cardno", card);
			db.insert("card", null, values);
		}
	}

	public String query(String val) {
		System.out.println(val);
		String cardq = "c";
		String[] columns = { "cardno" };
		String[] selectionArgs = { val };
//		db.execSQL("create table if not exists card (cardno INTEGER PRIMARY KEY)");
		Cursor c = db.query("card", columns, "cardno=?", selectionArgs, null,
				null, null);
		while (c.moveToNext()) {
			cardq = c.getString(c.getColumnIndex("cardno"));
		}
		c.close();
		return cardq;
	}

	public void closeDB() {
		db.close();
	}
	
	public void delete(){
		db.delete("card", null, null);
	}
	
	public void deletePre(String car){
		db.delete("card", "cardno=?", new String[]{car});
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