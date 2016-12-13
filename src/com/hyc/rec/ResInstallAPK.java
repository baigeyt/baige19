package com.hyc.rec;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Environment;
import android.os.Handler;

public class ResInstallAPK {
	public File getFileFromServer(String path,Handler schooleInfo) {

		try {
			// 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				URL url;
				url = new URL(path);				
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(5000);
				// 获取到文件的大小
				if(conn.getResponseCode()==200){
					InputStream is = conn.getInputStream();
					File file = new File(Environment.getExternalStorageDirectory(),
							"baige.apk");
					FileOutputStream fos = new FileOutputStream(file);
					BufferedInputStream bis = new BufferedInputStream(is);
					byte[] buffer = new byte[1024];
					int len;
					while ((len = bis.read(buffer)) != -1) {
						fos.write(buffer, 0, len);										
					}
					System.out.println("下载下载apk");
					fos.close();
					bis.close();
					is.close();
					
					schooleInfo.sendEmptyMessageDelayed(6, 3000);
					return file;	
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
