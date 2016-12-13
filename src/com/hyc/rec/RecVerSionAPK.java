package com.hyc.rec;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.extend.CurrentVersion;
import com.hyc.bean.APKInfo;
import com.hyc.bean.InterWeb;

public class RecVerSionAPK {

	InterWeb interWeb = new InterWeb();
	private String apkVersion;

	public APKInfo getAPKVersion(Context context) {
		URL five_url;
		APKInfo apkInfo = new APKInfo();
		try {
			five_url = new URL(interWeb.getURL_APKVersion());
			HttpURLConnection five_urlConnection = (HttpURLConnection) five_url
					.openConnection();
			five_urlConnection.setRequestMethod("GET");// 设置请求的方式
			five_urlConnection.setReadTimeout(5000);// 设置超时的时间
			five_urlConnection.setConnectTimeout(5000);// 设置链接超时的时间
			// 设置请求的头
			System.out.println("获取apk信息接口");
			System.out.println(interWeb.getURL_APKVersion());
			// 获取响应的状态码 404 200 505 302
			if (five_urlConnection.getResponseCode() == 200) {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								five_urlConnection.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					apkVersion += line;
				}
				System.out.println(apkVersion);

				if (apkVersion != null) {
					JSONObject object = new JSONObject(apkVersion.substring(4));

					System.out.println(object.getInt("errcode") + "");
					if (object.getInt("errcode") == 0) {
						System.out.println("有新版的apk要更新");

					if (object.getJSONArray("data") != null) {
						

							JSONArray arrayStr = object.getJSONArray("data");
							// 返回的数据是按照apk创建时间排序，所以最新版是第一个
							JSONObject mVersionVN = new JSONObject(
									arrayStr.getString(0));
							String interVersion = mVersionVN.getString("VN");
							String nativeVersion = new CurrentVersion()
									.getVersionName(context);

							String interVersionArr[] = interVersion.split("\\.");
							String nativeVersionArr[] = nativeVersion.split("\\.");
							if (interVersionArr[3].equals(nativeVersionArr[3])) {
								String interVerNum = interVersionArr[0] + "."
										+ interVersionArr[1] + "."
										+ interVersionArr[2];
								String nativeVerNum = nativeVersionArr[0] + "."
										+ nativeVersionArr[1] + "."
										+ nativeVersionArr[2];
								if (!interVerNum.equals(nativeVerNum)) {
									apkInfo.setApkVN(mVersionVN.getString("VN"));

									JSONObject mVersionRes = new JSONObject(
											mVersionVN.getString("resource"));
 
									apkInfo.setApkUrl(mVersionRes.getString("url"));
									apkInfo.setApkTime(mVersionRes
											.getString("time"));

									System.out.println("apkVersion VN:"
											+ mVersionVN.getString("VN") + "  url:"
											+ mVersionRes.getString("url")
											+ "  time+"
											+ mVersionRes.getString("time"));
								}
							}
							return apkInfo;
							
						}						
					}else {
						System.out.println("没有新的apk");
					}
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

}
