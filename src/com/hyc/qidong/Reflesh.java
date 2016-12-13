package com.hyc.qidong;

import com.hyc.baige.MainActivity;
import com.hyc.baige.MyService;
import com.hyc.baige.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class Reflesh extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.firstscreen);
		super.onCreate(savedInstanceState);
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
//						MainActivity.intent = new Intent(Reflesh.this, MyService.class);
//						startService(MainActivity.intent);
						Intent mainIntent = new Intent(
								Reflesh.this, MainActivity.class);
						Reflesh.this.startActivity(mainIntent);
						
						Reflesh.this.finish();
					}
				}, 2000);// 2000为间隔的时间-毫秒
			}
		}, 50);

	}

}

