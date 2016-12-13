package com.hyc.bean;

public class Parameter {
 byte[] buff;
 int len;
 String result;
 NameClass naClass;
 
 public void setBuff(byte[] buff) {
	this.buff = buff;
}
 
 public byte[] getBuff() {
	return buff;
}
 
 public void setLen(int len) {
	this.len = len;
}
 
 public int getLen() {
	return len;
}
 
 public void setResult(String result) {
	this.result = result;
}
 public String getResult() {
	return result;
}
 public void setNaClass(NameClass naClass) {
	this.naClass = naClass;
}
 public NameClass getNaClass() {
	return naClass;
}
 
}
