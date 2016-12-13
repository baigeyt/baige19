package com.hyc.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.hyc.bean.ICCardTime;

public class DBManagerICCardTime {
	private SQLiteDatabase db;

	public void creatDB() {
		File dbf = new File(getDir() + "/baige/db");
		if (!dbf.exists()) {
			dbf.mkdirs();
		}
		db = SQLiteDatabase.openOrCreateDatabase(dbf.toString() + "/"
				+ "cardtime.db", null);
		db.execSQL("create table if not exists cardtime (_id INTEGER PRIMARY KEY,starttime VARCHAR not null,endtime VARCHAR not null,typetime VARCHAR not null)");
		System.out.println("------DBManagerICCardTime.creatDB-----------");
	}

	public void insert(ICCardTime cardTime) {
		if (cardTime != null) {
			ContentValues values = new ContentValues();
			values.put("endtime", cardTime.getEndtime());
			values.put("starttime", cardTime.getStarttime());
			values.put("typetime", cardTime.getTimetype());
			db.insert("cardtime", "_id", values);
		}
		System.out.println("------DBManagerICCardTime.insert-----------");

	}

	public List<ICCardTime> query() {
		List<ICCardTime> list = new ArrayList<ICCardTime>();
		String[] columns = { "_id", "starttime", "endtime", "typetime" };
		ICCardTime cardTime = null;

		Cursor c = db.query("cardtime", columns, null, null, null, null, null);
		Log.v("进到query()111", c.getCount() + "");

		while (c.moveToNext()) {
			Log.v("进到query()222", c.getString(c.getColumnIndex("starttime")));

			cardTime = new ICCardTime();
			cardTime.setStarttime(c.getString(c.getColumnIndex("starttime")));
			cardTime.setEndtime(c.getString(c.getColumnIndex("endtime")));
			cardTime.setTimetype(c.getString(c.getColumnIndex("typetime")));
			list.add(cardTime);

		}
		System.out.println("------DBManagerICCardTime.query-----------");
		c.close();
		return list;

	}

	public void deleteData() {
		db.delete("cardtime", null, null);
		System.out.println("------DBManagerICCardTime.deleteData-----------");

	}
	public void updataData(ICCardTime cardTime,String str) {
		String[] columns = {str};
		ContentValues values = new ContentValues();
		values.put("endtime", cardTime.getEndtime());
		values.put("starttime", cardTime.getStarttime());
		values.put("typetime", cardTime.getTimetype());
		db.update("cardtime", values,"typetime=?", columns);
		System.out.println("------DBManagerICCardTime.updata-----------");
	
	}
	
	public void closeDB(){
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
