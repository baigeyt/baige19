package com.hyc.rec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import com.hyc.baige.Login;
import com.hyc.bean.InterWeb;
import com.hyc.bean.Stu;
import com.hyc.db.DBManagerStu;
public class RecOneCard {
	InterWeb interWeb = new InterWeb();
	private String ic_String;
	public static String name = null;
	public static int classid;
	public static String type = null;
	DBManagerStu dbManager;

	public Stu receiveDate(long physicsno) {
		Stu stu = new Stu();
		dbManager = new DBManagerStu();
		dbManager.openDB();
		
		try {
			URL five_url = new URL(interWeb.getURL_ONEIC() + physicsno);
			Log.e("onecard", Login.accesstoken + "" + physicsno);
			System.out.println(interWeb.getURL_ONEIC()+physicsno);
			HttpURLConnection five_urlConnection = (HttpURLConnection) five_url
					.openConnection();
			five_urlConnection.setRequestMethod("GET");// 设置请求的方式
			five_urlConnection.setReadTimeout(5000);// 设置超时的时间
			five_urlConnection.setConnectTimeout(5000);// 设置链接超时的时间
			// 设置请求的头
			// 获取响应的状态码 404 200 505 302
			
			Log.e("00000", "请求后的返回响应码："+five_urlConnection.getResponseCode());
			if (five_urlConnection.getResponseCode() == 200) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								five_urlConnection.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					ic_String += line;
				}
				System.out.println("ic_String:"+ic_String); 
				JSONObject jsonObject = new JSONObject(ic_String.substring(4)); 
				if (jsonObject.getInt("errcode") == 0) {
					JSONObject object = new JSONObject(ic_String.substring(4)).getJSONObject("data");
					System.out.println("object:"+object.toString());					
						if (object.getString("cardno") != "null") {
							stu.setCardno(object.getString("cardno"));
							if (object.getString("type") != "null") {
								stu.setType(object.getInt("type"));
								if(object.getInt("type")==6 || object.getInt("type")==7){
									JSONObject teacherObject = object.getJSONObject("owner");
									if (teacherObject.getString("name") != "null") {
										stu.setName(teacherObject.getString("name"));
										if(object.getInt("type")==6){
											//stu.setClassname("园长");
											stu.setClassname(teacherObject
													.getString("position"));
										}
										if(object.getInt("type")==7){
											//stu.setClassname("老师");
											stu.setClassname(teacherObject
													.getString("position"));
										}
											System.out.println("单卡接口...教师"+teacherObject
													.getString("position"));
											dbManager.insert(stu);									
									}
								}else{									
								JSONObject childObject = object.getJSONObject("child");
								if (childObject.getString("name") != "null") {
									stu.setName(childObject.getString("name"));
									if (childObject.getString("classname") != "null") {
										stu.setClassname(childObject.getString("classname"));
//										System.out.println("Cardno---" + stu.getCardno()
//												+ "Type---" + stu.getType() + "Name---"
//												+ stu.getName() + "Classid---"
//												+ stu.getClassname());
										dbManager.insert(stu);
									}
								}
								}
							}
						}
				}
				ic_String = null;
				dbManager.closeDB();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}catch (NetworkOnMainThreadException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return stu;
	}
}