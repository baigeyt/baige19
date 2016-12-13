package com.hyc.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.util.Log;

public class GetDeviceID {
	Context mContext;

	public GetDeviceID() {
		super();
	}

	public GetDeviceID(Context context) {
		mContext = context;
		Log.v("ttt", "mContext" + mContext);
	}

	public String getID1() {
		String android_id = Secure.getString(mContext.getContentResolver(),
				Secure.ANDROID_ID);

		return android_id;

	}

	public String getID2() {
		String ANDROID_ID = Settings.System.getString(
				mContext.getContentResolver(), Settings.System.ANDROID_ID);
		return ANDROID_ID;

	}

	/*
	 * 获取MAC地址 ====》
	 */

	public InetAddress getLocalInetAddress() {
		InetAddress ip = null;
		try {
			Enumeration<NetworkInterface> en_netInterface = NetworkInterface
					.getNetworkInterfaces();
			while (en_netInterface.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) en_netInterface
						.nextElement();
				Enumeration<InetAddress> en_ip = ni.getInetAddresses();
				while (en_ip.hasMoreElements()) {
					ip = en_ip.nextElement();
					if (!ip.isLoopbackAddress()
							&& ip.getHostAddress().indexOf(":") == -1)
						break;
					else
						ip = null;
				}

				if (ip != null) {
					break;
				}
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ip;
	}

	public String getMacAddress() /* throws UnknownHostException */{
		String strMacAddr = null;
		try {
			InetAddress ip = getLocalInetAddress();

			byte[] b = NetworkInterface.getByInetAddress(ip)
					.getHardwareAddress();
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < b.length; i++) {
				if (i != 0) {
					buffer.append(':');
				}

				String str = Integer.toHexString(b[i] & 0xFF);
				buffer.append(str.length() == 1 ? 0 + str : str);
			}
			strMacAddr = buffer.toString().toUpperCase();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return strMacAddr;
	}

	/*
	 * 获取MAC地址 《====
	 */

}
