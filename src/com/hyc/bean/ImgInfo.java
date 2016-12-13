package com.hyc.bean;

import java.io.File;

public class ImgInfo {
	int type;
	long cardno;
	String path;
	String name;
	String className;
	int num;
	String display;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getCardno() {
		return cardno;
	}
	public void setCardno(long cardno) {
		this.cardno = cardno;
	}
	public String getFile() {
		return path;
	}
	public void setFile(String path) {
		this.path = path;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClassName() {
		return className;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getNum() {
		return num;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getDisplay() {
		return display;
	}
	
}
