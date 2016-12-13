package com.hyc.rec;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hyc.bean.Company;
import com.hyc.bean.InterWeb;
import com.hyc.db.DBManagerCompany;
import com.hyc.db.DBManagerStu;

public class RecSchoolInfo {
	private String ic_String;
	private String time_String = null;
	private String main_String = null;
	public static int logoId;
	public static String schoolName;
	public static String qq;
	public static String mobile;
	public static String email;
	public static String province;
	public static String city;
	public static String district;
	public static String address;
	public static String content;
	public static String errcode=null;
	public static int Id;
	private final static String LOGO_PATH = Environment
			.getExternalStorageDirectory() + "/baige/LOGOFile/0.jpg";
	InterWeb interWeb = new InterWeb();
	
	Handler myHandler;
	private DBManagerCompany dbcompany = new DBManagerCompany();
	private Company com = new Company();
	public RecSchoolInfo(Handler handler) {
		myHandler = handler;

	}

	public RecSchoolInfo() {

	}
	
	public void receiveSchInfo() {
		URL five_url; 
		try {
			five_url = new URL(interWeb.getURL_SchoolInfo());
			System.out.println("receiveSchInfo:"+interWeb.getURL_SchoolInfo());
			HttpURLConnection five_urlConnection = (HttpURLConnection) five_url
					.openConnection();
			five_urlConnection.setRequestMethod("GET");// 设置请求的方式
			five_urlConnection.setReadTimeout(5000);// 设置超时的时间
			five_urlConnection.setConnectTimeout(5000);// 设置链接超时的时间
			// 设置请求的头
			System.out.println(interWeb.getURL_SchoolInfo());
			// 获取响应的状态码 404 200 505 302
			System.out.println("receiveSchInfo():"
					+ five_urlConnection.getResponseCode());
			if (five_urlConnection.getResponseCode() == 200) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								five_urlConnection.getInputStream()));
				String line;
				System.out.println(" RecSchoolInfo ");
				while ((line = reader.readLine()) != null) {
					ic_String += line;
				}
				System.out.println(ic_String);
				JSONObject jsonNow = new JSONObject(ic_String.substring(4))
						.getJSONObject("company");
				String LOGO_string = jsonNow.getString("logoid");
				if (LOGO_string.equals("0"))
					logoId = jsonNow.getInt("logoid");
				else {
					JSONArray array = jsonNow.getJSONArray("logoid");
					JSONObject object = array.getJSONObject(0);
					logoId = object.getInt("id");
				}

				Id = jsonNow.getInt("id");
				schoolName = jsonNow.getString("name");
				qq = jsonNow.getString("qq");
				mobile = jsonNow.getString("mobile");
				email = jsonNow.getString("email");
				province = jsonNow.getString("province");
				city = jsonNow.getString("city");
				district = jsonNow.getString("district");
				address = jsonNow.getString("address");
				content = jsonNow.getString("content");
				
				
				
				System.out.println(logoId + schoolName + qq + mobile + email
						+ province + city + district + address + content);
				
				if (city != null&&myHandler!=null) {
					Message message = new Message();
					message.what = 2;
					Bundle bun=new Bundle();
					bun.putString("district", district);
					bun.putString("city", city);
					message.setData(bun);
					Log.e("aaa", "天气");
					myHandler.sendMessage(message); 
				}
				
				ic_String = null;
				File file = new File(Environment.getExternalStorageDirectory()
						+ "/baige/LOGOFile/0.jpg");
				if (file.exists()) {
					System.out.println("学校图片LOGO已存在");
				} else {
					saveLOGO();
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	 public void receiveSchoolInfoMain(Handler mHandler,String macAddress,Context mContext){

			URL five_url;
			try {
				five_url = new URL("http://api.360baige.cn/company/info?accesstoken="+macAddress);
				HttpURLConnection five_urlConnection = (HttpURLConnection) five_url
						.openConnection();
				five_urlConnection.setRequestMethod("GET");
				five_urlConnection.setReadTimeout(5000);
				five_urlConnection.setConnectTimeout(5000);

				System.out.println(interWeb.getURL_SchoolInfoMain());
				if (five_urlConnection.getResponseCode() == 200) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(
									five_urlConnection.getInputStream()));
					String line;
					System.out.println(" 11111111111111111111111111111 ");
					System.out.println(" Contents of post request ");
					System.out.println(" 11111111111111111111111111111 ");
					while ((line = reader.readLine()) != null) {
						main_String += line;
					}
						JSONObject jsonNow;
						try {
							jsonNow = new JSONObject(main_String.substring(4))
							.getJSONObject("company");
							errcode = new JSONObject(main_String.substring(4)).getString("errcode");
							if(!errcode.equals("0")){
								Message msgCon = new Message();
								msgCon.what =3;
				    			mHandler.sendMessage(msgCon);
							}else{
								String str = jsonNow.getString("logoid");
								if(str.equals("0") || str.equals("null")){
									System.out.println(str);
								}else{
								    JSONArray jsonarray = jsonNow.getJSONArray("logoid");
								    JSONObject ject = jsonarray.getJSONObject(0);
								    logoId = ject.getInt("id");
								}
								Id = jsonNow.getInt("id");
								schoolName = jsonNow.getString("name");
								qq = jsonNow.getString("qq");
								mobile = jsonNow.getString("mobile");
								email = jsonNow.getString("email");
								province = jsonNow.getString("province");
								city = jsonNow.getString("city");
								district = jsonNow.getString("district");
								address = jsonNow.getString("address");
								content = jsonNow.getString("content");	
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				
				System.out.println(logoId + schoolName + qq + mobile + email
						+ province + city + district + address + content);
				dbcompany.creatDB();
				com = dbcompany.query();					
					main_String = null;
				      if(schoolName != null){
				    		if ( !schoolName.equals(dbcompany.queryName())||dbcompany.queryName()==null) {
				    			Log.e("comecome","进入更新数据了0.5");
								dbcompany.deleteSchoolInfo();
								com.setSchoolid(Id);
								com.setName(schoolName);
								com.setQq(qq);
								com.setMobile(mobile);
								com.setEmail(email);
								com.setProvince(province);
								com.setCity(city);
								com.setDistrict(district);
								com.setAddress(address);
								com.setContent(content);
								dbcompany.insert(com); 
								
								Message msgRe = new Message();
				    			msgRe.what =2;
				    			mHandler.sendMessage(msgRe);
							}else{
								Log.e("comecome","进入更新数据了0.8");
								dbcompany.deleteSchoolInfo();
								com.setSchoolid(Id);
								com.setName(schoolName); 
								com.setQq(qq); 
								com.setMobile(mobile);
								com.setEmail(email);
								com.setProvince(province);
								com.setCity(city);
								com.setDistrict(district);
								com.setAddress(address); 
								com.setContent(content);				
								dbcompany.insert(com);
								if(getDate(macAddress, mHandler, mContext)){
									
								}else{
									Log.e("comecome","进入更新数据了1.8");
									Message msgRe = new Message();
					    			msgRe.what =3;
					    			mHandler.sendMessage(msgRe);
								};	
							}
				    		
				      }	
				      dbcompany.closeDb();
				}else{
					Message msgCon = new Message();
					msgCon.what =3;
	 			    mHandler.sendMessage(msgCon);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	 }
	 private boolean getDate(String macAddress,Handler mHandler,Context context){
			URL timeUrl;
			String date = null;
			try {
				Log.e("comecome","进入更新数据了3.0");
				timeUrl = new URL("http://api.360baige.cn/equipment/geteqcurrentdate?accesstoken="+macAddress);
				HttpURLConnection time_urlConnection = (HttpURLConnection) timeUrl 
						.openConnection();
				time_urlConnection.setRequestMethod("GET");
				time_urlConnection.setReadTimeout(5000);
				time_urlConnection.setConnectTimeout(5000);
				if(time_urlConnection.getResponseCode()==200){
					BufferedReader timrReader = new BufferedReader(
							new InputStreamReader(
									time_urlConnection.getInputStream()));
					String time;
					System.out.println(" 11111111111111111111111111111 ");
					System.out.println(" Contents of post request ");
					System.out.println(" 11111111111111111111111111111 ");
					while ((time = timrReader.readLine()) != null) {
						time_String += time;
					}
					Log.e("cuurenttime", time_String);
					if(time_String!=null){
						try {
							JSONObject timeObject = new JSONObject(time_String.substring(4));
							if(timeObject.getInt("errcode")==0){
								date = timeObject.getString("currentdate");
								DBManagerStu dbMStu = new DBManagerStu();
								dbMStu.creatDB();
								 String upDate = dbMStu.queryDate();
								 Log.e("comecome","进入更新数据了3.6");
								 if(upDate==null){
									 dbMStu.insertDate(date);
									 Log.e("comecome","进入更新数据了666");
						    			return true;
								 }else {
									 if(!date.equals(upDate)){
										 Message msgR = new Message();
							    			msgR.what =5;
							    			mHandler.sendMessage(msgR);
							    			dbMStu.insertDate(date);
							    			return true; 
								 }
								 }
								 dbMStu.closeDB();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
	 }

	private void saveLOGO() {
		try {
			String filePath = interWeb.getURL_RecResource() + logoId;
			URL url = new URL(filePath);
			System.out.println(filePath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			if (conn.getResponseCode() == 200) {
				InputStream inStream = conn.getInputStream();
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = -1;
				while ((len = inStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
				outStream.close();
				inStream.close();
				byte[] data = outStream.toByteArray();
				File file = new File(LOGO_PATH);
				FileOutputStream outputStream = new FileOutputStream(file);
				outputStream.write(data);
				outputStream.close();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
