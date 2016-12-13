package com.hyc.up;

import java.io.File;

import android.content.Context;

import com.extend.DeleteFilePic;
import com.hyc.bean.ImgInfo;
import com.hyc.bean.InterWeb;

public class UploadFile {



public static void uploadFile(ImgInfo imginfo, Context context,String object,Long time){
		
		UploadRecord uploadRecord = new UploadRecord();
		uploadRecord.upLoadRecord(imginfo, context, "0",object,time);

	}
}
