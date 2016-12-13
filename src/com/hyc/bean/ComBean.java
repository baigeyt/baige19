package com.hyc.bean;

/**
 *´®¿ÚÊý¾Ý
 */
public class ComBean {
		public byte[] bRec=null;
		public ComBean(String sPort,byte[] buffer,int size){
			bRec=new byte[size];
			for (int i = 0; i < size; i++)
			{
				bRec[i]=buffer[i];
			}
		}
}