package com.extend;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class ComprssBitmap {
	public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {
	             final int heightRatio = Math.round((float) height/ (float) reqHeight);
	             final int widthRatio = Math.round((float) width / (float) reqWidth);
	             inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }
	        return inSampleSize;
	}
	
	// 根据路径获得图片并压缩，返回bitmap用于显示
	public  void getSmallBitmap(String filePath) {
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;

	        // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, 30, 50);

	        // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;

	    saveMyBitmap(BitmapFactory.decodeFile(filePath, options),filePath);  
	    }
	
	
	public void saveMyBitmap(Bitmap mBitmap,String bitName)  {
        File f = new File(bitName);
        FileOutputStream fOut = null;
        try {
                fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        mBitmap.recycle();
        mBitmap =  null;
        try {
                fOut.flush();
        } catch (IOException e) {
                e.printStackTrace();
        }
        try {
                fOut.close();
        } catch (IOException e) {
                e.printStackTrace();
        }
}
}
