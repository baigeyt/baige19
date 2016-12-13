package com.hyc.rec;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.hyc.bean.InterWeb;
import com.hyc.network.GetDeviceID;

public class RecWeather {
	private String typeToString;
	JSONObject operator;
	private JSONObject jsonobject;
	private String reulse;
	private String errcode = "22";
	public static String wendu = "暂无";
	public static String ganmao = "好好学习，天天向上！";
	public static String fengxiang = "暂无";
	public static String fengli = "暂无";
	public static String type = "暂无";
	public static String high = "暂无";

	public String request(String city) {
		String accesstoken = null;
		operator = new JSONObject();
		InterWeb interWeb = new InterWeb();


		try {
			operator.put("city", city);
			typeToString = operator.toString();

			GetDeviceID getDeviceID = new GetDeviceID();

			accesstoken = getDeviceID.getMacAddress();
			System.out.println("accesstoken" + accesstoken);
		//	five_url = new URL(interWeb.getURL_SchoolInfo());


			System.out.println("城市：" + city);
			URL url_post;
			url_post = new URL(interWeb.getURL_Weather()+city);
			System.out.println(url_post + "uuuuuuuuuuuuu");
			HttpURLConnection urlConnection = (HttpURLConnection) url_post
					.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setReadTimeout(5000);
			urlConnection.setConnectTimeout(5000);

			urlConnection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			urlConnection.setRequestProperty("Authorization",
					"http://api.figool.cn/index/weather?accesstoken="
							+ accesstoken);
			urlConnection.setRequestProperty("Content-Length",
					String.valueOf(typeToString.getBytes().length));
			urlConnection.setDoOutput(true);

			urlConnection.getOutputStream().write(typeToString.getBytes());
			if (urlConnection.getResponseCode() == 200) {
				InputStreamReader isr = new InputStreamReader(
						urlConnection.getInputStream(), "utf-8");
				BufferedReader reader = new BufferedReader(isr);
				String line;
				while ((line = reader.readLine()) != null) {
					reulse += line;

					System.out.println("获取到天气");
				}

				jsonobject = new JSONObject(reulse.substring(4));
				errcode = jsonobject.getString("errcode");

				if (errcode.equals("0")) {
					JSONObject jsonNow = new JSONObject(reulse.substring(4))
							.getJSONObject("now");
					wendu = jsonNow.getString("wendu");
					ganmao = jsonNow.getString("ganmao");
					fengxiang = jsonNow.getString("fengxiang");
					fengli = jsonNow.getString("fengli");
					type = jsonNow.getString("type");
					high = jsonNow.getString("high");
					System.out.println(wendu + ganmao + fengxiang + fengli
							+ type + high);
				}
				reader.close();
				urlConnection.disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return reulse;
	}
}
