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
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.hyc.bean.InterWeb;
import com.hyc.bean.Stu;
import com.hyc.db.DBManagerStu;

public class ReceiveICCard {

	private final static String file_path = Environment
			.getExternalStorageDirectory() + "/baige/TextFile/" + "cardno.txt";
	InterWeb interWeb = new InterWeb();
	private String ic_String = null;
	private JSONObject jsonobject;
	public String errcode = "c";
	private JSONArray jsonArray;
	DBManagerStu dbManager;
	private int count = 0;
	Context mContext;
	
	public ReceiveICCard(Context context,DBManagerStu dbManagerStu){
		mContext = context;
		dbManager = dbManagerStu;
	}

	public void receiveDate() {
		URL five_url;
//		dbManager = new DBManagerStu();	
//		dbManager.creatDB();
		try {
			five_url = new URL(interWeb.getURL_IC());
			Log.e("url", interWeb.getURL_IC());
			HttpURLConnection five_urlConnection = (HttpURLConnection) five_url
					.openConnection();
			five_urlConnection.setRequestMethod("GET");// 设置请求的方式
			five_urlConnection.setReadTimeout(20000);// 设置超时的时间
			five_urlConnection.setConnectTimeout(20000);// 设置链接超时的时间
			// 设置请求的头
			System.out.println("获取学生信息列表url...：" + five_url);
			// 获取响应的状态码 404 200 505 302

			if (five_urlConnection.getResponseCode() == 200) {
				Log.e("comecome","进入更新数据了2");
				Intent iSReflush = new Intent();
				iSReflush.setAction("com.baige.ui.service");
				iSReflush.putExtra("reflush", "1");
				mContext.sendBroadcast(iSReflush);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								five_urlConnection.getInputStream()));
				System.out.println("reader  " + reader);
				String line;
				while ((line = reader.readLine()) != null) {
					ic_String = line;
					System.out.println("ic_String111  " + ic_String);

					if (ic_String != null) {
						json_analysis(ic_String);
					}
					ic_String = null;
				}
				/*
				 * // 保存数据到本地 File file_name = new File(file_path); try { if
				 * (!file_name.exists()) { // 在指定的文件夹中创建文件
				 * file_name.createNewFile(); } } catch (Exception e) { // TODO:
				 * handle exception } FileOutputStream outputStream = new
				 * FileOutputStream(file_name);
				 * outputStream.write(ic_String.getBytes()); if (ic_String1 !=
				 * null) outputStream.write(ic_String1.getBytes());
				 * outputStream.flush(); outputStream.close();
				 */
				/*
				 * if (ic_String != null) { Log.v("tt", "333ic_String");
				 * json_analysis(ic_String); } if (ic_String1 != null) {
				 * Log.v("tt", "444ic_String"); json_analysis(ic_String1); }
				 */
				ic_String = null;
			}
		} catch (Exception e) {
			Log.e("发生异常", "555");
			e.printStackTrace();
			Intent iSMiss = new Intent();
			iSMiss.setAction("com.baige.ui.service");
			iSMiss.putExtra("miss", "2");
			mContext.sendBroadcast(iSMiss);
			
		} 
		ic_String = null;
		dbManager.closeDB();
	}

	private void json_analysis(String string) {
		try {
			System.out.println("ic_String222  " + ic_String);

			jsonobject = new JSONObject(string);
			errcode = jsonobject.getString("errcode");
			System.out.println("string = " + string.length());
			System.out.println("errcode" + errcode);

			if (errcode.equals("0")) {
				jsonArray = new JSONObject(string).getJSONArray("data");
				// System.out.println(jsonArray.length());
				for (int i = 0; i < jsonArray.length(); i++) {
					Stu stu = new Stu();
					JSONObject object = jsonArray.getJSONObject(i);
					count++;
					if (object.getString("cardno") != "null") {
						stu.setCardno(object.getString("cardno"));
						if (object.getString("type") != "null") {
							stu.setType(object.getInt("type"));
							if (object.getInt("type") == 6
									|| object.getInt("type") == 7) {
								JSONObject teacherObject = object
										.getJSONObject("owner");
								if (teacherObject.getString("name") != "null") {
									stu.setName(teacherObject.getString("name"));
									if (object.getInt("type") == 6) {
										//stu.setClassname("园长");
										stu.setClassname(teacherObject
												.getString("position"));
									}
									if (object.getInt("type") == 7) {
										stu.setClassname(teacherObject
												.getString("position"));
										//stu.setClassname("老师");
									}
									dbManager.insert(stu);
								}
							} else {

								JSONObject childObject = object
										.getJSONObject("child");
								if (childObject.getString("name") != "null") {
									stu.setName(childObject.getString("name"));
									if (childObject.getString("classname") != "null") {
										stu.setClassname(childObject
												.getString("classname"));
										/*
										 * System.out.println("Cardno---" +
										 * stu.getCardno() + "Type---" +
										 * stu.getType() + "Name---" +
										 * stu.getName() + "Classid---" +
										 * stu.getClassname());
										 */
										dbManager.insert(stu);
									}
								}
							}
						}
					}
					if(i==jsonArray.length()-1){
						Intent iSMiss = new Intent();
						iSMiss.setAction("com.baige.ui.service");
						iSMiss.putExtra("miss", "2");
						mContext.sendBroadcast(iSMiss);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			if (jsonArray != null) {
				if ((jsonArray.length() - count) > 0) {
					two_analysis();
				}
			}
		}
	}

	private void two_analysis() {
		try {
			System.out.println("ic_String222  " + ic_String);

			for (int j = count; j < jsonArray.length(); j++) {
				Stu stu = new Stu();
				JSONObject object = jsonArray.getJSONObject(j);
				count++;
				if (object.getString("cardno") != "null") {
					stu.setCardno(object.getString("cardno"));
					if (object.getString("type") != "null") {
						stu.setType(object.getInt("type"));
						if (object.getInt("type") == 6
								|| object.getInt("type") == 7) {
							JSONObject teacherObject = object
									.getJSONObject("owner");
							if (teacherObject.getString("name") != "null") {
								stu.setName(teacherObject.getString("name"));
								if (object.getInt("type") == 6) {
									stu.setClassname(teacherObject
											.getString("classname"));
									// stu.setClassname("园长");
								}
								if (object.getInt("type") == 7) {
									// stu.setClassname("老师");
									stu.setClassname(teacherObject
											.getString("classname"));
									System.out.println("职位");
									System.out.println("职位职位..."
											+ teacherObject
													.getString("classname"));
								}
								/*
								 * System.out.println("Cardno---" +
								 * stu.getCardno() + "Type---" + stu.getType() +
								 * "Name---" + stu.getName() + "Classid---" +
								 * stu.getClassname());
								 */
								dbManager.insert(stu);
							}
						} else {

							JSONObject childObject = object
									.getJSONObject("child");
							if (childObject.getString("name") != "null") {
								stu.setName(childObject.getString("name"));
								if (childObject.getString("classname") != "null") {
									stu.setClassname(childObject
											.getString("classname"));
									/*
									 * System.out.println("Cardno---" +
									 * stu.getCardno() + "Type---" +
									 * stu.getType() + "Name---" + stu.getName()
									 * + "Classid---" + stu.getClassname());
									 */
									dbManager.insert(stu);
								}
							}
						}
					}
				}
			}

			ic_String = null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (jsonArray != null) {
				if ((jsonArray.length() - count) > 0) {
					two_analysis();
				}
			}
		}
	}
}
