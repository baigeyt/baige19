package com.hyc.bean;

import java.util.ArrayList;

public class Data {
	int cardno;
	int physicsno;
	int type;

	ArrayList<Family> familys = new ArrayList<Family>();

	public int getCardno() {
		return cardno;
	}

	public void setCardno(int cardno) {
		this.cardno = cardno;
	}

	public int getPhysicsno() {
		return physicsno;
	}

	public void setPhysicsno(int physicsno) {
		this.physicsno = physicsno;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public ArrayList<Family> getFamilys() {
		return familys;
	}

	public void setFamilys(ArrayList<Family> familys) {
		this.familys = familys;
	}

}
