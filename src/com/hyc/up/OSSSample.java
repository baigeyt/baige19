package com.hyc.up;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.extend.DeleteFilePic;
import com.hyc.baige.MainActivity;
import com.hyc.bean.ImgInfo;

public class OSSSample {

	private Context context;
	// 上传地址
	String accessUrl = "oss-cn-shenzhen.aliyuncs.com";
	// AliYun OSS对象
	private OSS oss;
	// 文件唯一标识，随便你怎么写·
	// 就是buketName，这个就是OSS服务空间名
	private String buketName = "sdk-baige";
	private String uploadFilePath;
	private ImgInfo imgInfo;
	private String object;
	private SQLiteDatabase dbWriter;
	private Handler scInfoHandler;
	private Handler screenProtect;

	public OSSSample(Context context, String uploadFilePath, OSS oss, ImgInfo imgInfo, String object,Handler scInfoHandler) {
		this.context = context;
		this.uploadFilePath = uploadFilePath;
		this.oss = oss;
		this.imgInfo = imgInfo;
		this.object = object;
		this.scInfoHandler = scInfoHandler;
	}
	public OSSSample(Context context, String uploadFilePath, OSS oss, ImgInfo imgInfo, String object,SQLiteDatabase dbWriter,Handler scInfoHandler,Handler screenProtect) {
		this.context = context;
		this.uploadFilePath = uploadFilePath;
		this.oss = oss;
		this.imgInfo = imgInfo;
		this.object = object;
		this.dbWriter = dbWriter;
		this.scInfoHandler = scInfoHandler;
		this.screenProtect = screenProtect;
	}

	public OSSSample() {

	}

	public void upload() throws ClientException, ServiceException {
		// 构造上传请求
		PutObjectRequest put = new PutObjectRequest(buketName, object,
				uploadFilePath);

		// 异步上传时可以设置进度回调
		put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
			@Override
			public void onProgress(PutObjectRequest request, long currentSize,
					long totalSize) {
				Log.d("PutObject", "currentSize: " + currentSize
						+ " totalSize: " + totalSize);
			}
		});

		OSSAsyncTask task = oss.asyncPutObject(put,
				new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
					@Override
					public void onSuccess(PutObjectRequest request,
							PutObjectResult result) {
						scInfoHandler.sendEmptyMessage(120);
						new UploadFile().uploadFile(imgInfo, context, object,System.currentTimeMillis() / 1000);
						File file = new File(uploadFilePath);
						if (file.exists()) {
							DeleteFilePic.delete(file);
						}

						//发消息让屏保隐藏
						Message message = new Message();
						message.what = 0;
						if(screenProtect!=null){
							screenProtect.sendMessageDelayed(message, 60000);
						}

						MainActivity.upallcount++;
						Intent intent = new Intent(
								"jason.broadcast.action");
						context.sendBroadcast(intent);
						Log.e("PutObject", "UploadSuccess");
					}

					@Override
					public void onFailure(PutObjectRequest request,
							ClientException clientExcepion,
							ServiceException serviceException) {
						
						//发消息让屏保隐藏
						Message message = new Message();
						message.what = 0;
						if(screenProtect!=null){
							screenProtect.sendMessageDelayed(message, 60000);
						}
						
						scInfoHandler.sendEmptyMessage(121);
						ContentValues values = new ContentValues();
						values.put("type", imgInfo.getType());
						values.put("cardno", imgInfo.getCardno());
						values.put("alluploadpaths", uploadFilePath);
						values.put("timecode", System.currentTimeMillis() / 1000);
						dbWriter.insert("allpaths", null, values);
						// 请求异常
						if (clientExcepion != null) {
							// mHandler.removeMessages(0x123);
							// Message msg = new Message();
							// msg.what = 0x123;
							// mHandler.sendMessageDelayed(msg, 3000);
							// 本地异常如网络异常等
							clientExcepion.printStackTrace();
						}
						if (serviceException != null) {
							// 服务异常
							Log.e("ErrorCode", serviceException.getErrorCode());
							Log.e("RequestId", serviceException.getRequestId());
							Log.e("HostId", serviceException.getHostId());
							Log.e("RawMessage",
									serviceException.getRawMessage());
						}
					}
				});
	}

	public void sycupload(Long time) {
		// 构造上传请求
		PutObjectRequest put = new PutObjectRequest("sdk-baige", object,
				uploadFilePath);

		// 文件元信息的设置是可选的
		// ObjectMetadata metadata = new ObjectMetadata();
		// metadata.setContentType("application/octet-stream");// 设置content-type
		// metadata.setContentMD5(BinaryUtil.calculateBase64Md5(uploadFilePath));
		// // 校验MD5
		// put.setMetadata(metadata);
		try {

			PutObjectResult putResult = oss.putObject(put);

			Log.d("PutObject", "UploadSuccess");
			
			scInfoHandler.sendEmptyMessage(120);
			File file = new File(uploadFilePath);
			new UploadFile().uploadFile(imgInfo, context, object,time);
			if (file != null) {
				DeleteFilePic.delete(file);
			}
//			Db db;
//			SQLiteDatabase dbWriter;
//			db = new Db(context);
//			dbWriter = db.getWritableDatabase();
			dbWriter.delete("allpaths",
					"alluploadpaths=?",
					new String[] {uploadFilePath});
//			dbWriter.close();
//			db.close();
			MainActivity.upallcount++;
			Log.e("EEEEEEEEEEEEEEEEEEEE",
					String.valueOf(MainActivity.upallcount));
			Intent intent = new Intent("jason.broadcast.action");
			context.sendBroadcast(intent);

			Log.d("ETag", putResult.getETag());
			Log.d("RequestId", putResult.getRequestId());
		} catch (ClientException e) {
			// 本地异常如网络异常等
			scInfoHandler.sendEmptyMessage(121);
			e.printStackTrace();
		} catch (ServiceException e) {
			scInfoHandler.sendEmptyMessage(121);
			// 服务异常
			Log.e("RequestId", e.getRequestId());
			Log.e("ErrorCode", e.getErrorCode());
			Log.e("HostId", e.getHostId());
			Log.e("RawMessage", e.getRawMessage());
		}

	}
	

	public File getDir() {
		// 得到SD卡根目录
		File dir = Environment.getExternalStorageDirectory();
		if (dir.exists()) {
			return dir;
		} else {
			dir.mkdirs();
			return dir;
		}
	}

	public void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { 
				InputStream inStream = new FileInputStream(oldPath); 
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; 
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("copyFile");
			e.printStackTrace();

		}

	}

}
