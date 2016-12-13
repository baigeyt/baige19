package com.hyc.bean;
/**
 * 封装从百鸽服务器获取的数据
 * 这些数据是用来访问阿里云服务器的参数
 * 这些参数1个小时会失效 需要重复获取
 * 2016/4/15
 * 杨滔 
 */

public class UploadFileOss {
	String RequestId;
	String AssumedRoleId;
	String Arn;
	String AccessKeySecret;
	String AccessKeyId;
	String Expiration;
	String SecurityToken;

	public String getRequestId() {
		return RequestId;
	}
	public void setRequestId(String requestId) {
		RequestId = requestId;
	}
	public String getAssumedRoleId() {
		return AssumedRoleId;
	}
	public void setAssumedRoleId(String assumedRoleId) {
		AssumedRoleId = assumedRoleId;
	}
	public String getArn() {
		return Arn;
	}
	public void setArn(String arn) {
		Arn = arn;
	}
	public String getAccessKeySecret() {
		return AccessKeySecret;
	}
	public void setAccessKeySecret(String accessKeySecret) {
		AccessKeySecret = accessKeySecret;
	}
	public String getAccessKeyId() {
		return AccessKeyId;
	}
	public void setAccessKeyId(String accessKeyId) {
		AccessKeyId = accessKeyId;
	}
	public String getExpiration() {
		return Expiration;
	}
	public void setExpiration(String expiration) {
		Expiration = expiration;
	}
	public String getSecurityToken() {
		return SecurityToken;
	}
	public void setSecurityToken(String securityToken) {
		SecurityToken = securityToken;
	}

}
