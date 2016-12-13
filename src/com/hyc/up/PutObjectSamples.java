package com.hyc.up;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

public class PutObjectSamples {
	private OSS oss;
	private String testBucket;
	private String testObject;
	private String uploadFilePath;

	public PutObjectSamples(OSS client, String testBucket, String testObject,
			String uploadFilePath) {
		this.oss = client;
		this.testBucket = testBucket;
		this.testObject = testObject;
		this.uploadFilePath = uploadFilePath;
	}

	// 从本地文件上传，使用非阻塞的异步接口

	public void upload() throws ClientException, ServiceException{
		// 构造上传请求
		PutObjectRequest put = new PutObjectRequest(testBucket, testObject, uploadFilePath);

		// 异步上传时可以设置进度回调
		put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
		    @Override
		    public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
		        Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
		    }
		});

		OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
		    @Override
		    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
		        Log.d("PutObject", "UploadSuccess");
		      //上传成功，返回activity弹个Toas
		    }

		    @Override
		    public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
		    	//上传失败，也返回activity弹个Toast
		    
		        // 请求异常
		        if (clientExcepion != null) {
		            // 本地异常如网络异常等
		            clientExcepion.printStackTrace();
		        }
		        if (serviceException != null) {
		            // 服务异常
		            Log.e("ErrorCode", serviceException.getErrorCode());
		            Log.e("RequestId", serviceException.getRequestId());
		            Log.e("HostId", serviceException.getHostId());
		            Log.e("RawMessage", serviceException.getRawMessage());
		        }
		    }
		});
	}
	//同步上传
	public void sycUpload(){
		// 构造上传请求
		PutObjectRequest put = new PutObjectRequest(testBucket, testObject, uploadFilePath);

		// 文件元信息的设置是可选的
		// ObjectMetadata metadata = new ObjectMetadata();
		// metadata.setContentType("application/octet-stream"); // 设置content-type
		// metadata.setContentMD5(BinaryUtil.calculateBase64Md5(uploadFilePath)); // 校验MD5
		// put.setMetadata(metadata);

		try {

		    PutObjectResult putResult = oss.putObject(put);

		    Log.d("PutObject", "UploadSuccess");

		    Log.d("ETag", putResult.getETag());
		    Log.d("RequestId", putResult.getRequestId());
		} catch (ClientException e) {
			System.out.println("555555555555555555555........");
		    // 本地异常如网络异常等
		    e.printStackTrace();
		} catch (ServiceException e) {
			System.out.println("6666666666666666666........");
		    // 服务异常
		    Log.e("RequestId", e.getRequestId());
		    Log.e("ErrorCode", e.getErrorCode());
		    Log.e("HostId", e.getHostId());
		    Log.e("RawMessage", e.getRawMessage());
		}

	}

}
