package com.hyc.rec;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Handler;

import com.hyc.bean.ICCardTime;
import com.hyc.bean.InterWeb;
import com.hyc.db.DBManagerICCardTime;

public class RecICCardTime {

	InterWeb interWeb = new InterWeb();
	private String time_String;
	DBManagerICCardTime dbCardTime;

	public void recCardTime(Handler mHandler) {
		URL five_url;
		try {
			five_url = new URL(interWeb.getURL_ICCardTime());
			HttpURLConnection five_urlConnection = (HttpURLConnection) five_url
					.openConnection();
			five_urlConnection.setRequestMethod("GET");// 设置请求的方式
			five_urlConnection.setReadTimeout(5000);// 设置超时的时间
			five_urlConnection.setConnectTimeout(5000);// 设置链接超时的时间
			// 设置请求的头
			System.out.println("获取打卡时间接口");
			System.out.println(interWeb.getURL_ICCardTime());
			// 获取响应的状态码 404 200 505 302
			if (five_urlConnection.getResponseCode() == 200) {
				dbCardTime = new DBManagerICCardTime();
				dbCardTime.creatDB();
				dbCardTime.deleteData();
				ICCardTime cardTime = new ICCardTime();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								five_urlConnection.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					time_String += line;
				}
				System.out.println(time_String);

				JSONObject object = new JSONObject(time_String.substring(4));
				System.out.println("object  " + object.toString());

				JSONObject jsonIcTime = object.getJSONObject("data");
				System.out.println("jsonIcTime" + jsonIcTime.toString());

				JSONArray arrayStr = jsonIcTime.getJSONArray("8");
				for (int i = 0; i < arrayStr.length(); i++) {
					JSONObject mtime = new JSONObject(arrayStr.getString(i));
					cardTime.setEndtime(mtime.getString("endtime"));
					cardTime.setStarttime(mtime.getString("starttime"));
					cardTime.setTimetype(mtime.getString("timetype"));
					dbCardTime.insert(cardTime);
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}finally{
			mHandler.sendEmptyMessage(222);
		}
	}

}
