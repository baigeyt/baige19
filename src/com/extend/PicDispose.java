package com.extend;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.hyc.baige.R;

import android.content.Context;
import android.os.Environment;
/*
 * 把资源文件的图片写到本地SD卡**/
public class PicDispose {
	
	public static void copyToSD(Context context) {
		InputStream is = null;
		FileOutputStream fos = null;

		try {
			File dbFile = new File(Environment.getExternalStorageDirectory()
					+ "/baige/LOGOFile/1.jpg");
			if (!dbFile.exists()) {
				is = context.getResources().openRawResource(
						R.drawable.kongbai1);
				fos = new FileOutputStream(dbFile);

				byte[] buffer = new byte[8 * 1024];// 8K
				while (is.read(buffer) > 0)// >
				{
					fos.write(buffer);
				}
			}

		} catch (Exception e) {

		} finally {
			try {
				if (is != null) {
					is.close();
				}

				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}

	}


}
