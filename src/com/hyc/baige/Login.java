package com.hyc.baige;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.hyc.bean.AppId;
import com.hyc.db.DBManagerAdvert;
import com.hyc.db.DBManagerBase;
import com.hyc.db.DBManagerStu;
import com.hyc.network.GetDeviceID;
import com.hyc.rec.RecAdvert;
import com.hyc.rec.ReceiveICCard;

public class Login {
	private String appid = "";
	private String appsecret = "";
	public static String accesstoken;
	public static String rel_String;
	private JSONObject jsonobject;
	ReceiveICCard receiveICCard;
	private String errcode = "22";
	int i = 0;
	DBManagerBase base = new DBManagerBase();
	DBManagerStu dbManagerStu = new DBManagerStu();
//	DBManagerAdvert dBManageradvert = new DBManagerAdvert();
	AppId id = new AppId();

	public void myFun(Context context) {
//		dBManageradvert.creatDB();

		// base.openDB();
		base.creatDB();
		dbManagerStu.creatDB();

		id = base.query();
		appid = id.getAppid();
		appsecret = id.getAppsecret();

		 GetDeviceID getDeviceID = new GetDeviceID();
		 accesstoken = getDeviceID.getMacAddress();
		 Log.e("comecome","进入更新数据了10.9");
		 Cursor cursor = dbManagerStu.db.query("stu", null, null, null, null,
		 null, null);
		 int i = 0;
			while (cursor.moveToNext()) {
				i++;
			}
			if (i==0) {
				Log.e("aaaa", "long");
				Log.e("comecome","进入更新数据了1");

				receiveICCard = new ReceiveICCard(context,dbManagerStu);
				receiveICCard.receiveDate();
			}
		 base.closeDB();
		 dbManagerStu.closeDB();

//		String URL_Login = "http://api.360baige.com/index/getAccessToken?appid="
//				+ appid + "&appsecret=" + appsecret;
//		URL five_url;
//		try {
//			five_url = new URL(URL_Login);
//			HttpURLConnection five_urlConnection = (HttpURLConnection) five_url
//					.openConnection();
//			five_urlConnection.setRequestMethod("GET");
//			five_urlConnection.setReadTimeout(5000);
//			five_urlConnection.setConnectTimeout(5000);
//			five_urlConnection.setRequestProperty("Authorization", URL_Login);
//			System.out.println("Login:" + five_urlConnection.getResponseCode());
//			if (five_urlConnection.getResponseCode() == 200) {
//				BufferedReader reader = new BufferedReader(
//						new InputStreamReader(
//								five_urlConnection.getInputStream()));
//				String line;
//				while ((line = reader.readLine()) != null) {
//					rel_String += line;
//				}
//				jsonobject = new JSONObject(rel_String.substring(4));
//				errcode = jsonobject.getString("errcode");
//				accesstoken = jsonobject.getString("accesstoken");
//				rel_String = null;
//			}
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		if (errcode.equals("0")) {
//			switch (i) {
//			case 0:
//
//				Cursor cursor = dbManagerStu.db.query("stu", null, null, null,
//						null, null, null);
//				if (cursor.isAfterLast()) {
//					receiveICCard = new ReceiveICCard();
//					receiveICCard.receiveDate();
//				}
//
//				break;
//			default:
//				break;
//			}
//		} else if (errcode.equals("1")) {
//			rel_String = null;
//		} else if (errcode.equals("-1")) {
//			rel_String = null;
//		}
	}

}
