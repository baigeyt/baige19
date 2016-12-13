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
import com.hyc.db.DBManagerAdvert;

public class RecAdvert {
	InterWeb interWeb = new InterWeb();
	private String picT_String;
	private final static String ALBUM_PATH = Environment
			.getExternalStorageDirectory() + "/baige/";
	private String mFileName;
	private String resourceid;
	private String time;
	@SuppressWarnings("unused")
	private boolean save = false;
	private int count;
	private int n = 1;
	DBManagerAdvert dBManageradvert = new DBManagerAdvert();
	PictureId pictureId = new PictureId();

	// ��ȡ��ԴID
	public void receiveAdvert() {
		URL five_url;
		dBManageradvert.openDB();
		try {
			five_url = new URL(interWeb.getURL_RecAdvert());
			HttpURLConnection five_urlConnection = (HttpURLConnection) five_url
					.openConnection();
			five_urlConnection.setRequestMethod("GET");// ��������ķ�ʽ
			five_urlConnection.setReadTimeout(5000);// ���ó�ʱ��ʱ��
			five_urlConnection.setConnectTimeout(5000);// �������ӳ�ʱ��ʱ��
			// ���������ͷ
			System.out.println(interWeb.getURL_RecAdvert());
			// ��ȡ��Ӧ��״̬�� 404 200 505 302
			if (five_urlConnection.getResponseCode() == 200) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								five_urlConnection.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					picT_String += line;
				}
				System.out.println(picT_String);
				JSONArray jsonNow = new JSONObject(picT_String.substring(4))
						.getJSONArray("list");
				for (int i = 0; i < jsonNow.length(); i++) {
					JSONObject object = jsonNow.getJSONObject(i);
					System.out.println(object);
					if (object.getString("type").equals("2")) {
						JSONArray array = object.getJSONArray("resourceids");
						for (int j = 0; j < array.length(); j++) {
							System.out.println("������ͼƬ����");
							n = 1;
							JSONObject ject = array.getJSONObject(j);
							// ����JSONArray
							resourceid = ject.getString("id");
							time = ject.getString("time");
							mFileName = j + ".jpg";
							if (resourceid != null) {
								Cursor cursor = dBManageradvert.db.query("advert", null, null, null, null, null, null);
								if(cursor.moveToFirst())
								  {
									  for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
						 		      {
										  if(resourceid.equals(cursor.getString(cursor.getColumnIndex("id")))){
											  System.out.println("ͼƬIDһ����ͼƬIDһ��");
											  if(time.equals(cursor.getString(cursor.getColumnIndex("time")))){
												  System.out.println("ͼƬtimeһ����ͼƬtimeһ��");
												  n = 0;
												  break;
											  }else{
												  pictureId.setTime(time);
												  ContentValues time_values = new ContentValues();
												  time_values.put("time", pictureId.getTime());
												  dBManageradvert.db.update("advert", time_values, "id="+resourceid, null);
												  saveFile("advertPicFile/");
												  n = 0;
												  break;
											  }
										  }
						 		      }
									  if(n == 1){
										    saveFile("advertPicFile/");
											pictureId.setId(Integer.parseInt(resourceid));
											pictureId.setTime(time);
											dBManageradvert.insert(pictureId);
									  }
									  
								  }else{
									saveFile("advertPicFile/");
									pictureId.setId(Integer.parseInt(resourceid));
									pictureId.setTime(time);
									dBManageradvert.insert(pictureId);
								}
							}
						}
					} else if (object.getString("type").equals("4")) {
						JSONArray array1 = object.getJSONArray("resourceids");
						System.out.println(array1);
						for (int k = 0; k < array1.length(); k++) {
							System.out.println("��������Ƶ����");
							resourceid = array1.getString(k);
							mFileName = resourceid + ".mp4";
							if (resourceid != null) {
								if ((save = fileIsExists(mFileName)) == false) {
									if (count == 0) {
										System.out.println("�������ļ�ɾ��");
										File file = new File(
												Environment.getExternalStorageDirectory()
												+ "/baige/advertVideoFile/");
										delete(file);
									}
									saveFile("advertVideoFile/");
									count = 1;
								} else {
									System.out.println("�ļ��Ѵ��ڣ�����������������������");
								}
							}
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

	public boolean fileIsExists(String file) {
		try {
			File f = new File(Environment.getExternalStorageDirectory()
					+ "/baige/advertVideoFile/" + file);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static void delete(File file) {
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

	private void saveFile(String filepath) {
		try {
			String filePath = interWeb.getURL_RecResource() + resourceid
					+ "&width=1366&height=768";
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
				File file = new File(ALBUM_PATH + filepath + mFileName);
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
