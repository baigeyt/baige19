package com.hyc.up;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hyc.baige.MainActivity;
import com.hyc.bean.InterWeb;
import com.hyc.bean.Stu;
import com.hyc.bean.UpRecord;
import com.hyc.db.DBManagerStu;

public class ReUpRecord {
	private JSONObject operator;
	private String typeToString;
	private int type;
	private Stu stu;
	private String reulse;
	private JSONObject jsonobject;
	InterWeb interWeb = new InterWeb();
	private String errcode = "22";
	private DBManagerStu dbManagerstu = new DBManagerStu();

	public UpRecord upLoadRecord(Context context, String cardno,String object) {

		operator = new JSONObject();
		dbManagerstu.openDB();
		stu = dbManagerstu.query(cardno + "");
		type = stu.getType();
		try {
			// TODO
			operator.put("resourceid", "0");
			operator.put("resourcekey", object);
			operator.put("recordtime", System.currentTimeMillis() / 1000);
			operator.put("usertype", type);
			operator.put("iccardno", cardno);
			operator.put("remark", "remark");
			typeToString = operator.toString();

			System.out.println("typeToString-------" + typeToString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			URL url_post = new URL(interWeb.getURL_UploadRecord());
			System.out.println(url_post);
			// url.openConnection()打开网络链接
			HttpURLConnection urlConnection = (HttpURLConnection) url_post
					.openConnection();
			urlConnection.setRequestMethod("POST");// 设置请求的方式
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setReadTimeout(5000);// 设置超时的时间
			urlConnection.setConnectTimeout(5000);// 设置链接超时的时间
			// 设置请求的头
			urlConnection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			urlConnection.setRequestProperty("Authorization",
					interWeb.getURL_ReUploadRecord());
			urlConnection.setRequestProperty("Content-Length",
					String.valueOf(typeToString.getBytes().length));
			urlConnection.setDoOutput(true);
			// 4.向服务器写入数据
			urlConnection.getOutputStream().write(typeToString.getBytes());

			System.out.println("888888888888888888888888888");
			System.out.println("1111111111111111111111111111111");

			if (urlConnection.getResponseCode() == 200) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(urlConnection.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					reulse += line;
				}
				reader.close();
				urlConnection.disconnect();
				try {
					jsonobject = new JSONObject(reulse.substring(4));
					errcode = jsonobject.getString("errcode");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (errcode.equals("0")) {
//					MainActivity.upallcount++;
					Intent intent = new Intent(MainActivity.action);
					context.sendBroadcast(intent);
				}
			} else {
				Log.e("UploadRecord", urlConnection.getResponseCode() + "");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
