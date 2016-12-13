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

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Environment;

import com.hyc.bean.InterWeb;
import com.hyc.bean.PictureId;
import com.hyc.db.DBManagerSchPic;

public class RecSchoolPicInfo {
	InterWeb interWeb = new InterWeb();
	private String picT_String;
	private final static String ALBUM_PATH = Environment
			.getExternalStorageDirectory() + "/baige/campusPicFile/";
	private String CAMPUSPIC;
	private String resourceid;
	private String time;
	private String mFileName;
	private String table;
	private int Area;
	private int n = 1;
	private Cursor cursor;
	DBManagerSchPic dBManagerSchPic = new DBManagerSchPic();
	PictureId pictureId = new PictureId();
	
	public  RecSchoolPicInfo(int Area){
		this.Area = Area;
	}

	// 获取资源ID
	public void receiveWeather() {
		URL five_url;
		dBManagerSchPic.openDB();
		try {
			five_url = new URL(interWeb.getURL_SchoolPicInfo()+Area);
			HttpURLConnection five_urlConnection = (HttpURLConnection) five_url
					.openConnection();
			five_urlConnection.setRequestMethod("GET");// 设置请求的方式
			five_urlConnection.setReadTimeout(5000);// 设置超时的时间
			five_urlConnection.setConnectTimeout(5000);// 设置链接超时的时间
			// 设置请求的头
			System.out.println(interWeb.getURL_SchoolPicInfo());
			// 获取响应的状态码 404 200 505 302
			if (five_urlConnection.getResponseCode() == 200) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								five_urlConnection.getInputStream()));
				String line;
				System.out.println(" 11111111111111111111111111111 ");
				System.out.println(" Contents of post request ");
				System.out.println(" 11111111111111111111111111111 ");
				while ((line = reader.readLine()) != null) {
					picT_String += line;
				}
				System.out.println(picT_String);
			//	String str = new JSONObject(picT_String.substring(4)).getString("resources");				
				JSONArray jsonNow = new JSONObject(picT_String.substring(4))
						.getJSONArray("resources");
				for (int i = 0; i < jsonNow.length(); i++) {
					// 遍历JSONArray
					JSONObject ject = jsonNow.getJSONObject(i);
					resourceid = ject.getString("id");
					time = ject.getString("time");
					if (resourceid != null) {
						System.out.println(resourceid);
						mFileName = i + ".jpg";
						n = 1;
						//savePic();
						if(Area == 1){
							 table = "first";
						}
						if(Area == 2){
							table = "second";
						}
						if(Area == 3){
							table = "third";
						}
						if(Area == 4){
							table = "four";
						}
						 cursor = dBManagerSchPic.db.query(table, null, null, null, null, null, null);
						if(cursor.moveToFirst())
						  {
							  for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
				 		      {
								  if(resourceid.equals(cursor.getString(cursor.getColumnIndex("id")))){
									  System.out.println("图片ID一样，图片ID一样");
									  if(time.equals(cursor.getString(cursor.getColumnIndex("time")))){
										  System.out.println("图片time一样，图片time一样");
										  n = 0;
										  break;
									  }else{
										  pictureId.setTime(time);
										  ContentValues time_values = new ContentValues();
										  time_values.put("time", pictureId.getTime());
										  dBManagerSchPic.db.update(table, time_values, "id="+resourceid, null);
										  savePic();
										  n = 0;
										  break;
									  }
								  }
				 		      }
							  if(n == 1){
								    savePic();
									pictureId.setId(Integer.parseInt(resourceid));
									pictureId.setTime(time);
									dBManagerSchPic.insert(pictureId, table);
							  }
							  
						  }else{
							savePic();
							pictureId.setId(Integer.parseInt(resourceid));
							pictureId.setTime(time);
							dBManagerSchPic.insert(pictureId, table);
						}
					}
				}
				picT_String = null;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void savePic() {
		try {
			String filePath = null;
			if(Area == 1){
			    filePath = interWeb.getURL_RecResource() + resourceid
					+ "&width=675&height=443";
			    CAMPUSPIC = "campusPic0/";
			}else{
				filePath = interWeb.getURL_RecResource() + resourceid
					+ "&width=330&height=443";
				if(Area == 2)
					CAMPUSPIC = "campusPic1/";
				if(Area == 3)
					CAMPUSPIC = "campusPic2/";
				if(Area == 4)
					CAMPUSPIC = "campusPic3/";
			}
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
				File file = new File(ALBUM_PATH + CAMPUSPIC + mFileName);
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
