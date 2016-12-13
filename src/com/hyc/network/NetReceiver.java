package com.hyc.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class NetReceiver extends BroadcastReceiver {
	
	BRInteraction brInteraction;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
			boolean isConnected = NetUtils.isNetworkConnected(context);
			System.out.println("����״̬��" + isConnected);
			System.out.println("wifi״̬��" + NetUtils.isWifiConnected(context));
			System.out.println("�ƶ�����״̬��" + NetUtils.isMobileConnected(context));
			System.out.println("�����������ͣ�" + NetUtils.getConnectedType(context));
			if (isConnected) {
				brInteraction.setText("������Ϣ��\n������");
				Toast.makeText(context, "�Ѿ���������", Toast.LENGTH_LONG).show();
			} else {
				brInteraction.setText("������Ϣ��\nδ����");
				Toast.makeText(context, "�Ѿ��Ͽ�����", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public interface BRInteraction {
        public void setText(String content);
    }

    public void setBRInteractionListener(BRInteraction brInteraction) {
        this.brInteraction = brInteraction;
    }
}
