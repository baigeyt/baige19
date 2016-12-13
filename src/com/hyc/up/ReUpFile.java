package com.hyc.up;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.hyc.bean.InterWeb;

public class ReUpFile{
	private static final String TAG = "uploadFile";
	private static final int TIME_OUT = 5 * 1000; // 超时时间
	private static final String CHARSET = "utf-8"; // 设置编码
	private static JSONObject jsonobject;
	private static String errcode = "22";
	public static String resourceid;
	private static long tt = 4294967295L;
	static InterWeb interWeb = new InterWeb();
	private static String object; 

	public static void uploadFile(File file, Context context) {
		if (file.exists()) {
			int res = 0;
			String result = null;
			String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
			String PREFIX = "--", LINE_END = "\r\n";
			String CONTENT_TYPE = "multipart/form-data"; // 内容类型
			JSONObject operator = new JSONObject();
			@SuppressWarnings("unused")
			String typeToString;
			String Request_URL = interWeb.getURL_UploadFile();
			System.out.println(Request_URL);
			try {
				URL url = new URL(Request_URL);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setReadTimeout(TIME_OUT);
				conn.setConnectTimeout(TIME_OUT);
				conn.setDoInput(true); // 允许输入流
				conn.setDoOutput(true); // 允许输出流
				conn.setUseCaches(false); // 不允许使用缓存
				conn.setRequestMethod("POST"); // 请求方式
				conn.setRequestProperty("Charset", CHARSET); // 设置编码
				conn.setRequestProperty("connection", "keep-alive");
				conn.setRequestProperty("Content-Type", CONTENT_TYPE
						+ ";boundary=" + BOUNDARY);
				conn.setDoOutput(true);
				if (file != null) {					
					if (res == 200) {
						Log.e(TAG, "request success");
						InputStream input = conn.getInputStream();
						StringBuffer sb1 = new StringBuffer();
						int ss;
						while ((ss = input.read()) != -1) {
							sb1.append((char) ss);
						}
						System.out.println(sb1);
						result = sb1.toString();
						try {
							jsonobject = new JSONObject(sb1.substring(0));
							errcode = jsonobject.getString("errcode");
							resourceid = jsonobject.getString("resourceid");
							ReUpRecord reUpRecord = new ReUpRecord();
							// TODO
							String iccardid = file.getName().substring(0,file.getName().lastIndexOf("."));
							reUpRecord.upLoadRecord(context, Long.toString(tt - Long.parseLong(iccardid)),object);
							if (errcode.equals("0")) {
								delete(file);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						Log.e(TAG, "result : " + result);
					} else {
						Log.e(TAG, "request error");
					}
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void delete(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}
			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i]);
			}
		}
	}
}
