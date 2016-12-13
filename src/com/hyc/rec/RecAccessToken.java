package com.hyc.rec;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.content.Context;

import com.extend.CurrentVersion;
import com.extend.RC4Kit;
import com.hyc.bean.InterWeb;
import com.hyc.db.DBManagerAccessToken;
import com.hyc.network.GetDeviceID;

/**
 * 获取访问令牌
 * 
 * @author Administrator
 * 
 */
public class RecAccessToken {
	private String typeToString;
	private String ic_String;
	DBManagerAccessToken db;

	InterWeb interWeb = new InterWeb();
	CurrentVersion mCurrentVersion = new CurrentVersion();
	GetDeviceID mGetDeviceID = new GetDeviceID();

	public void postAccessToken(Context mContext) {
		// TODO Auto-generated method stub
		JSONObject mJSONObject = new JSONObject();

		URL url;
		try {
			mJSONObject.put("apkVersion",
					mCurrentVersion.getVersionName(mContext));// 软件版本
			mJSONObject.put("machineType", "5");// 设备类型
			mJSONObject.put("serialKey", mGetDeviceID.getMacAddress());// 设备编码（唯一标识MAC）
			mJSONObject.put("serialSecret", interWeb.getSerialSecret());// 设备密钥（在后台获取）

			System.out.println("版本号："
					+ mCurrentVersion.getVersionName(mContext));
			System.out.println("MAC:" + mGetDeviceID.getMacAddress());
			System.out.println("设备密钥（在后台获取）" + interWeb.getSerialSecret());

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
				System.out.println("获取访问令牌返回200");
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					ic_String += line;
				}
				System.out.println("ic_String  " + ic_String);
				String accessToken = ic_String.substring(4);
				String key = accessToken.substring(0, 32);
				String data = accessToken.substring(32, accessToken.length());
				System.out.println(key + "  " + data);
				String token = RC4Kit.deRC4(data, key);
				System.out.println("访问令牌： " + token);

				// System.out.println("访问令牌： " + accessToken);

				db = new DBManagerAccessToken();
				db.creatDB();
				db.delete();
				db.insert(accessToken);
				db.closeDB();

			}
		} catch (Exception e) {

			e.printStackTrace();

		}

	}
}
