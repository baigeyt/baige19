package com.extend;

import java.io.File;


 /**
 * 通过文件路径
 * 删除本地文件
 * 
 * 杨滔
 * 2016/4/15
 */
public class DeleteFilePic {
	
	public static void delete(File file) {
		if (file.isFile()) {
			file.delete();
			System.out.println(file);
			return;
		}
		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				System.out.println("-------------------"+file);
				return;
			}
			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i]);
			}
		}
	}
	
	public static void deletelistFiles(File file) {
		if (file.isDirectory()) {
			File childFiles[] = file.listFiles();
			if(childFiles.length>0){
				for (int i = 0; i < childFiles.length; i++) {
					delete(childFiles[i]);
				}
			}
		}
	}   
	
}
