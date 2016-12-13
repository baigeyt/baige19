package com.hyc.bean;


//刷卡自定义时间实体类
public class ICCardTime {
	//刷卡开始时间
	String starttime;
	//刷卡结束时间
	String endtime;
	//时间类型
	String timetype;
	
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getTimetype() {
		return timetype;
	}
	public void setTimetype(String timetype) {
		this.timetype = timetype;
	}
	
	
}
