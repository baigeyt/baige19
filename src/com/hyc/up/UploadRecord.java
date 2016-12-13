package com.hyc.up;

import java.io.BufferedReader;
import java.io.File;
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

import com.hyc.baige.Login;
import com.hyc.baige.MainActivity;
import com.hyc.bean.ImgInfo;
import com.hyc.bean.InterWeb;
import com.hyc.bean.UpRecord;

public class UploadRecord {
	private JSONObject operator;
	private String typeToString;
	private String reulse;
	private JSONObject jsonobject;
	InterWeb interWeb = new InterWeb();
	private String errcode = "22";	
	

	public UpRecord upLoadRecord(ImgInfo imginfo, Context context, String resourceid,String object,Long time) {

		operator = new JSONObject();
		try {
			operator.put("resourceid", "0");
			operator.put("resourcekey", object);
			operator.put("recordtime", time);
			operator.put("usertype", imginfo.getType());
			operator.put("iccardno", imginfo.getCardno());
			operator.put("remark", "remark");
			typeToString = operator.toString();
			
			System.out.println("typeToString-------"+typeToString);
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
					interWeb.getURL_UploadRecord());
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
				System.out.println(" ============================= ");
				System.out.println(" Contents of post request ");
				System.out.println(" ============================= ");
				while ((line = reader.readLine()) != null) {
					reulse += line;
				}
				System.out.println(reulse+"..............AAAAAAA");
				System.out.println(" ============================= ");
				System.out.println(" Contents of post request ends ");
				System.out.println(" ============================= ");
				try {
					jsonobject = new JSONObject(reulse.substring(4));
					errcode = jsonobject.getString("errcode");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(errcode.equals("0")){
//					delete(imginfo.getFile());
//					MainActivity.upallcount++;
					Log.v("cc","UploadRecord==>"+MainActivity.upallcount+"//");

					Intent intent = new Intent(MainActivity.action); 
					context.sendBroadcast(intent);
				}else if (errcode.equals("2")){
					Login login = new Login();
					login.myFun(context);
				}
				reader.close();
				urlConnection.disconnect();
				System.out.println("1111111111111111111111111111111");
			} else {
				Log.e("UploadRecord", urlConnection.getResponseCode()+"");
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
