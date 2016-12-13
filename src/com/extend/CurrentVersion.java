package com.extend;

import android.content.Context;

public class CurrentVersion {
	private String packageName = "com.hyc.baige";

	// 获取versionName
	public String getVersionName(Context context) {
		String versionName = "";
		try {
			versionName = context.getPackageManager().getPackageInfo(
					packageName, 0).versionName;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return versionName;

	}

	// 获取versionCode
	public int getVersionCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager()
					.getPackageInfo(packageName, 0).versionCode;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return verCode;

	}

}
