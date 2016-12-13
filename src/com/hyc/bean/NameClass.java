package com.hyc.bean;

public class NameClass {

	String Name;
	String Class;
	int type;
	long cardno;
	boolean temp;
	String path;
	String stuNo;
	
	public void setName(String Name){
		this.Name = Name;
	}
	public String getName(){
		
		return Name;
	}
	
	public void setClasses(String Class){
		this.Class = Class;
	}
	public String getClasses(){
		
		return Class;
	}
	
	public void setCardno(long cardno){
		this.cardno = cardno;
	}
	public long getCardno(){
		
		return cardno;
	}
	
	public void setTemp(boolean temp){
		this.temp = temp;
	}
	public boolean getTemp(){
		
		return temp;
	}
	
	public void setType(int type){
		this.type = type;
	}
	public int getType(){
		
		return type;
	}
public void setPath(String path) {
	this.path = path;
}

public String getPath() {
	return path;
}

public void setStuNo(String stuNo) {
	this.stuNo = stuNo;
}
public String getStuNo() {
	return stuNo;
}

}
