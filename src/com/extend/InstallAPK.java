package com.extend;

import java.io.DataOutputStream;
import java.io.OutputStream;

import android.os.Environment;

public class InstallAPK {
	
	private String cmd_uninstall = "pm uninstall ";
	String apkLocation = Environment.getExternalStorageDirectory().toString()
			+ "/";
	
	public void onClick_install() {
		String cmd = cmd_uninstall + "com.hyc.baige";
		System.out.println("静默安装命令：" + cmd);
		excuteSuCMD(cmd);
	}
	
	
	//执行shell命令
		protected int excuteSuCMD(String cmd) {
			try {
				Process process = Runtime.getRuntime().exec("su");
				DataOutputStream dos = new DataOutputStream(
						(OutputStream) process.getOutputStream());
				// 部分手机Root之后Library path 丢失，导入library path可解决该问题
				dos.writeBytes((String) "export LD_LIBRARY_PATH=/vendor/lib:/system/lib\n");
				cmd = String.valueOf(cmd);
				dos.writeBytes((String) (cmd + "\n"));
				dos.flush();
				dos.writeBytes("exit\n");
				dos.flush();
				process.waitFor();
				int result = process.exitValue();
				return (Integer) result;
			} catch (Exception localException) {
				localException.printStackTrace();
				return -1;
			}
		}

}
