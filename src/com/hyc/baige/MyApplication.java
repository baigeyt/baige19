package com.hyc.baige;

import java.io.File;

import android.app.Application;
import android.os.Environment;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		File mainFile = new File(getDir() + "/baige");
		if (!mainFile.exists()) {
			mainFile.mkdirs();
		}
		File picFile = new File(getDir() + "/baige/picFile");
		if (!picFile.exists()) {
			picFile.mkdirs();
		}
		File dataFile = new File(getDir() + "/baige/dataFile");
		if (!dataFile.exists()) {
			dataFile.mkdirs();
		}
		File campusPicFile = new File(getDir() + "/baige/campusPicFile");
		if (!campusPicFile.exists()) {
			campusPicFile.mkdirs();
		}
		File advertpFile = new File(getDir() + "/baige/advertPicFile");
		if (!advertpFile.exists()) {
			advertpFile.mkdirs();
		}
		File advertvFile = new File(getDir() + "/baige/advertVideoFile");
		if (!advertvFile.exists()) {
			advertvFile.mkdirs();
		}
		File logoFile = new File(getDir() + "/baige/LOGOFile");
		if (!logoFile.exists()) {
			logoFile.mkdirs();
		}
		File TextFile = new File(getDir() + "/baige/TextFile");
		if (!TextFile.exists()) {
			TextFile.mkdirs();
		}
		File campusPic0 = new File(getDir() + "/baige/campusPicFile/campusPic0");
		if (!campusPic0.exists()) {
			campusPic0.mkdirs();
		}
		File campusPic1 = new File(getDir() + "/baige/campusPicFile/campusPic1");
		if (!campusPic1.exists()) {
			campusPic1.mkdirs();
		}
		File campusPic2 = new File(getDir() + "/baige/campusPicFile/campusPic2");
		if (!campusPic2.exists()) {
			campusPic2.mkdirs();
		}
		File campusPic3 = new File(getDir() + "/baige/campusPicFile/campusPic3");
		if (!campusPic3.exists()) {
			campusPic3.mkdirs();
		}
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
