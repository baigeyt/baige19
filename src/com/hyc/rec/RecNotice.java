package com.hyc.rec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.extend.CurrentVersion;
import com.extend.RC4Kit;
import com.hyc.bean.InterWeb;
import com.hyc.db.DBManagerAccessToken;

public class RecNotice {
	private String typeToString;
	DBManagerAccessToken db = new DBManagerAccessToken();
	InterWeb interWeb = new InterWeb();
	private String ic_String;
	public static String content = "暂时获取不到公告！";
	CurrentVersion mCurrentVersion = new CurrentVersion();

	@SuppressWarnings("static-access")
	public void receiveDate(Context mContext) {
		System.out.println("获取通知公告了");
		
		JSONObject mJSONObject = new JSONObject();
		db.creatDB();
		String token = db.query();
		System.out.println("token" + token);
		URL url;
		try {
			mJSONObject.put("accessToken", token);
			mJSONObject.put("apkVersion",
					mCurrentVersion.getVersionName(mContext));
			mJSONObject.put("machineType", "5");
			mJSONObject.put("serialKey", interWeb.getSerialSecret());

			typeToString = mJSONObject.toString();

			url = new URL(interWeb.getURL_NOTICE());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setReadTimeout(5000);
			conn.setConnectTimeout(5000);

			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("Authorization", url.toString());
			conn.setRequestProperty("Content-Length",
					String.valueOf(typeToString.getBytes().length));
			conn.setDoOutput(true);
			// 4.向服务器写入数据
			conn.getOutputStream().write(typeToString.getBytes());

			System.out.println(interWeb.getURL_NOTICE());

			if (conn.getResponseCode() == 200) {
				System.out.println("访问通知公告接口返回200");
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					ic_String += line;
				}
				System.out.println("ic_String1   " + ic_String);
				
				RC4Kit mRC4Kit=new RC4Kit();
				
				ic_String =	mRC4Kit.deRC4(ic_String, "12345678901234567890123456789012");
				
				System.out.println("ic_String2   " + ic_String);

				JSONArray jsonNotice = new JSONObject(ic_String.substring(4))
						.getJSONArray("list");
				System.out.println("jsonNotice");
				JSONObject value = jsonNotice.getJSONObject(0);
				content = value.getString("content");
				System.out.println(content + "000000000");
				if (content.equals("") || content.equals("null")
						|| content == null) {
					content = "学校暂无公告";
				}

				ic_String = null;
			}
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (JSONException e) {

			e.printStackTrace();
		}
	}
}
