package com.hyc.up;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.hyc.bean.UploadFileOss;

public class UploadPicOSS {

	Context mContext;
	UploadFileOss uploadFileOss;;
	private OSS oss;
	private List<String> mlist;
	public UploadPicOSS() {
	}
	public UploadPicOSS(Context context) {
		mContext = context;
	}
	public UploadPicOSS(List<String> list) {
		mlist = list;
	};
	
	// 定时获取OSS凭证
	public void TimesCredentialProvider(final OSS oss) {

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				oss.updateCredentialProvider(new OSSStsTokenCredentialProvider(
						uploadFileOss.getAccessKeyId(), uploadFileOss
								.getAccessKeySecret(), uploadFileOss
								.getSecurityToken()));
			}
		};
		timer.schedule(task, 1800000, 1800000);

	}
}