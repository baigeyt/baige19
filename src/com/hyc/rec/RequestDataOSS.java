package com.hyc.rec;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.http.RetryableSink;

import org.json.JSONObject;

import android.content.Context;
import com.hyc.bean.InterWeb;
import com.hyc.bean.UploadFileOss;
import com.hyc.network.GetDeviceID;

public class RequestDataOSS {
	private String ic_String;
	List<String> list1 = new ArrayList<String>();
	String typeToString;

	InterWeb interWeb = new InterWeb();
	UploadFileOss uploadFileOSS = new UploadFileOss();

	public List<String> uploadFileOss(Context context,String ID) {
		try {
			System.out.println("xxxxxxxx");

			URL url = new URL(
					"http://api.figool.cn/apk/getOssStsToken?RoleSessionName="+ID+"baige");
			System.out.println(url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");// 设置请求的方式
			conn.setReadTimeout(5000);// 设置超时的时间
			conn.setConnectTimeout(5000);// 设置链接超时的时间
			System.out.println(conn.getResponseCode());
			if (conn.getResponseCode() == 200) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					ic_String += line;
				}

				JSONObject jsonObject = new JSONObject(ic_String.substring(4));
				if (jsonObject.getInt("errcode") == 0) {

					JSONObject object = new JSONObject(ic_String.substring(4))
							.getJSONObject("data");

					uploadFileOSS.setRequestId(object.getString("RequestId"));
					String assumedRoleUser = object
							.getString("AssumedRoleUser");
					 System.out.println("assumedRoleUser" + assumedRoleUser);

					String credentials = object.getString("Credentials");
					if (assumedRoleUser != null) {
						JSONObject object1 = new JSONObject(assumedRoleUser);
						uploadFileOSS.setAssumedRoleId(object1
								.getString("AssumedRoleId"));
						uploadFileOSS.setArn(object1.getString("Arn"));
						
						
				
					}
					if (credentials != null) {
						JSONObject object2 = new JSONObject(credentials);
						uploadFileOSS.setAccessKeyId(object2
								.getString("AccessKeyId"));
						uploadFileOSS.setAccessKeySecret(object2
								.getString("AccessKeySecret"));
						uploadFileOSS.setExpiration(object2
								.getString("Expiration"));
						uploadFileOSS.setSecurityToken(object2
								.getString("SecurityToken"));
						System.out.println(object2
								.getString("AccessKeyId")+object2
								.getString("AccessKeySecret")+object2
								.getString("SecurityToken")+"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
						list1.add(uploadFileOSS.getAccessKeyId());
						list1.add(uploadFileOSS.getAccessKeySecret());
						list1.add(uploadFileOSS.getSecurityToken());
			

					}

				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
      return list1;
	}
}
