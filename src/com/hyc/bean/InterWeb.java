package com.hyc.bean;

import com.hyc.baige.Login;
import com.hyc.baige.MainActivity;
import com.hyc.network.GetDeviceID;

public class InterWeb {
	public String MAC = getMac();
	// private String web = "http://api.360baige.com";
	 private String web1 = "http://api.figool.cn";
	// private String web = "http://api.figool.com";
	private String web = "http://api.360baige.cn";

	private String getMac() {
		GetDeviceID get = new GetDeviceID();
		return get.getMacAddress();

	}
	//获取APK信息
			private String URL_APKVersion = web1 + "/apk/listofapk?accesstoken="+MAC;
	// 打卡时间设置
		private String URL_ICCardTime = web + "/iccardtime/info?accesstoken=" + MAC;
	//学校信息2
	private String URL_SchoolInfoMain = web + "/company/info?accesstoken="
			+ MAC;
	// 广告
	private String URL_RecAdvert = web + "/advertise/list?accesstoken="
			+ MAC + "&type=";
	// 卡数据
	private String URL_IC = web + "/iccard/list?accesstoken="
			+ MAC;
	// 单卡数据
	private String URL_ONEIC = web + "/iccard/one?accesstoken="
			+ MAC + "&physicsno=";
	// 通告
	private String URL_NOTICE = web + "/index/notice?accesstoken="
			+ MAC;
	// 学校信息
	private String URL_SchoolInfo = web + "/company/info?accesstoken="
			+ MAC;
	// 展示图片
	private String URL_SchoolPicInfo = web + "/campus/list?accesstoken="
			+ MAC + "&area=";
	// 天气信息
	private String URL_Weather = web + "/index/weather?accesstoken="
			+ MAC + "&longitude=" + MainActivity.longitude
			+ "&latitude=" + MainActivity.latitude;
	// 上传照片
	private String URL_UploadFile = web + "/resource/upload?accesstoken="
			+ MAC + "&type=2";
	// 上传记录
	private String URL_UploadRecord = web + "/icrecord/add?accesstoken="
			+ MAC;
	// 上传记录
	private String URL_ReUploadRecord = web + "icrecord/update?accesstoken="
			+ MAC;
	// 获取资源
	private String URL_RecResource = web + "/resource/get?accesstoken="
			+ MAC + "&resourceid=";
	
	public String getURL_APKVersion() {
		return URL_APKVersion;
	}

	public void setURL_APKVersion(String uRL_APKVersion) {
		URL_APKVersion = uRL_APKVersion;
	}

	public String getURL_RecAdvert() {
		return URL_RecAdvert;
	}

	public String getURL_ICCardTime() {
		return URL_ICCardTime;
	}

	public void setURL_ICCardTime(String uRL_ICCardTime) {
		URL_ICCardTime = uRL_ICCardTime;
	}

	public String getURL_IC() {
		return URL_IC;
	}

	public String getURL_ONEIC() {
		return URL_ONEIC;
	}

	public String getURL_NOTICE() {
		return URL_NOTICE;
	}

	public String getURL_SchoolInfo() {
		return URL_SchoolInfo;
	}

	public String getURL_SchoolPicInfo() {
		return URL_SchoolPicInfo;
	}

	public String getURL_Weather() {
		return URL_Weather;
	}

	public String getURL_UploadFile() {
		return URL_UploadFile;
	}

	public String getURL_UploadRecord() {
		return URL_UploadRecord;
	}

	public String getURL_ReUploadRecord() {
		return URL_ReUploadRecord;
	}

	public String getURL_RecResource() {
		return URL_RecResource;
	}

	private String URL_OSS = web + "/getOssStsToken?accesstoken=" + getMac()
			+ "&RoleSessionName=timestemp";

	public String getURL_OSS() {
		return URL_OSS;
	}

	public void setURL_OSS(String uRL_OSS) {
		URL_OSS = uRL_OSS;
	}
	 public String getURL_SchoolInfoMain() {
			return URL_SchoolInfoMain;
		}

}
