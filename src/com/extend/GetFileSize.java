package com.extend;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

import android.util.Log;

public class GetFileSize {
	public static final int SIZETYPE_B = 1;
	 public static final int SIZETYPE_KB = 2;
	 public static final int SIZETYPE_MB = 3;
	 public static final int SIZETYPE_GB = 4;

	 
	 public static double getFileOrFilesSize(String filePath, int sizeType) {
	  File file = new File(filePath);
	  long blockSize = 0;
	  try {
	   if (file.isDirectory()) {
	    blockSize = getFileSizes(file);
	   } else {
	    blockSize = getFileSize(file);
	   }
	  } catch (Exception e) {
	   e.printStackTrace();
	  }
	  return FormetFileSize(blockSize, sizeType);
	 }

	 public static String getAutoFileOrFilesSize(String filePath) {
	  File file = new File(filePath);
	  long blockSize = 0;
	  try {
	   if (file.isDirectory()) {
	    blockSize = getFileSizes(file);
	   } else {
	    blockSize = getFileSize(file);
	   }
	  } catch (Exception e) {
	   e.printStackTrace();
	  }
	  return FormetFileSize(blockSize);
	 }

	 private static long getFileSize(File file) throws Exception {
	  long size = 0;
	  if (file.exists()) {
	   FileInputStream fis = null;
	   fis = new FileInputStream(file);
	   size = fis.available();
	  } else {
	   file.createNewFile();
	  }
	  return size;
	 }
	
	 private static long getFileSizes(File f) throws Exception {
	  long size = 0;
	  File flist[] = f.listFiles();
	  for (int i = 0; i < flist.length; i++) {
	   if (flist[i].isDirectory()) {
	    size = size + getFileSizes(flist[i]);
	   } else {
	    size = size + getFileSize(flist[i]);
	   }
	  }
	  return size;
	 }
	
	 private static String FormetFileSize(long fileS) {
	  DecimalFormat df = new DecimalFormat("#.00");
	  String fileSizeString = "";
	  String wrongSize = "0B";
	  if (fileS == 0) {
	   return wrongSize;
	  }
	  if (fileS < 1024) {
	   fileSizeString = df.format((double) fileS) + "B";
	  } else if (fileS < 1048576) {
	   fileSizeString = df.format((double) fileS / 1024) + "KB";
	  } else if (fileS < 1073741824) {
	   fileSizeString = df.format((double) fileS / 1048576) + "MB";
	  } else {
	   fileSizeString = df.format((double) fileS / 1073741824) + "GB";
	  }
	  return fileSizeString;
	 }

	 private static double FormetFileSize(long fileS, int sizeType) {
	  DecimalFormat df = new DecimalFormat("#.00");
	  double fileSizeLong = 0;
	  switch (sizeType) {
	  case SIZETYPE_B:
	   fileSizeLong = Double.valueOf(df.format((double) fileS));
	   break;
	  case SIZETYPE_KB:
	   fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
	   break;
	  case SIZETYPE_MB:
	   fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
	   break;
	  case SIZETYPE_GB:
	   fileSizeLong = Double.valueOf(df
	     .format((double) fileS / 1073741824));
	   break;
	  default:
	   break;
	  }
	  return fileSizeLong;
	 }
	
}
