package com.hyc.db;

import java.io.File;

import com.hyc.bean.Company;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * 学校或企业信息数据库
 * */
public class DBManagerCompany {
	private SQLiteDatabase db;

	public void creatDB() {
		File dbf = new File(getDir() + "/baige/db");
		if (!dbf.exists()) {
			dbf.mkdirs();
		}
		db = SQLiteDatabase.openOrCreateDatabase(dbf.toString() + "/"
				+ "company.db", null);
		// db.execSQL("DROP TABLE IF EXISTS stu");
		db.execSQL("create table if not exists company (_id INTEGER PRIMARY KEY,schoolid integer, name VARCHAR not null,qq VARCHAR,mobile VARCHAR,email VARCHAR,province VARCHAR,city VARCHAR,district VARCHAR,address VARCHAR,content VARCHAR)");
		System.out.println("------DBManagerCompany.creatDB-----------");
	}

	public void openDB() {
		File dbf = new File(getDir() + "/baige/db");
		if (!dbf.exists()) {
			dbf.mkdirs();
		}
		db = SQLiteDatabase.openOrCreateDatabase(dbf.toString() + "/"
				+ "company.db", null);
		System.out.println("------DBManagerCompany.openDB-----------");
	}

	public void insert(Company company) {
		if (company != null) {
			ContentValues values = new ContentValues();
			values.put("schoolid", company.getSchoolid());
			values.put("name", company.getName());
			values.put("qq", company.getQq());
			values.put("mobile", company.getMobile());
			values.put("email", company.getEmail());
			values.put("province", company.getProvince());
			values.put("city", company.getCity());
			values.put("district", company.getDistrict());
			values.put("address", company.getAddress());
			values.put("content", company.getContent());
			db.insert("company", "_id", values);
			System.out.println("------DBManagerCompany.insert-----------");

		}
	}

	public Company query() {
		Company company = new Company();
		String[] columns = { "schoolid", "name", "qq", "mobile", "email",
				"province", "city", "district", "address", "content" };
		String[] selectionArgs = { "1" };
		Cursor c = db.query("company", columns, "_id=?", selectionArgs, null,
				null, null);

		while (c.moveToNext()) {
			System.out.println("0000000");
			company.setSchoolid(c.getInt(c.getColumnIndex("schoolid")));
			company.setName(c.getString(c.getColumnIndex("name")));
			company.setQq(c.getString(c.getColumnIndex("qq")));
			company.setMobile(c.getString(c.getColumnIndex("mobile")));
			company.setEmail(c.getString(c.getColumnIndex("email")));
			company.setProvince(c.getString(c.getColumnIndex("province")));
			company.setCity(c.getString(c.getColumnIndex("city")));
			company.setDistrict(c.getString(c.getColumnIndex("district")));
			company.setAddress(c.getString(c.getColumnIndex("address")));
			company.setContent(c.getString(c.getColumnIndex("content")));
		}
		c.close();
		System.out.println("------DBManagerCompany.query-----------");

		return company;
	}
	
	public String queryName() {
		String cardq = "c";
		String[] columns = { "name" };
		Cursor c = db.query("company", columns,null,
				null, null, null, null);
		while (c.moveToNext()) {
			cardq = c.getString(c.getColumnIndex("name"));
		}
		c.close();
		return cardq;
	}
	
	public void deleteSchoolInfo(){
		db.delete("company", null, null);
	}

	public void deleteDB() {
		db.execSQL("DROP TABLE IF EXISTS company");
	}
	
	public void closeDb(){
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
