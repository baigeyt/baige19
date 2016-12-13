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

import com.hyc.bean.InterWeb;

public class RecNotice {
	InterWeb interWeb = new InterWeb();
	private String ic_String;
	public static String content = "暂时获取不到公告！";

	public void receiveDate() {
		URL five_url;
		try {
			five_url = new URL(interWeb.getURL_NOTICE());
			HttpURLConnection five_urlConnection = (HttpURLConnection) five_url
					.openConnection();
			five_urlConnection.setRequestMethod("GET");
			five_urlConnection.setReadTimeout(5000);
			five_urlConnection.setConnectTimeout(5000);

			System.out.println(interWeb.getURL_NOTICE());

			if (five_urlConnection.getResponseCode() == 200) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								five_urlConnection.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					ic_String += line;
				}
				System.out.println("ic_String" + ic_String);
				JSONArray jsonNotice = new JSONObject(ic_String.substring(4))
						.getJSONArray("notices");
				System.out.println("jsonNotice");
				JSONObject value = jsonNotice.getJSONObject(0);
				content = value.getString("content");
				System.out.println(content + "000000000");
				if (content.equals("") || content.equals("null")
						|| content == null) {
					content = "学校暂无公告";
				}
 
				ic_String = null;
			}
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (JSONException e) {

			e.printStackTrace();
		}
	}
}
