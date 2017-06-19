package com.lensent.wakeup;



import com.umeng.analytics.MobclickAgent;

import wakeup.guide.GuideActivity;
import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;



public class Welcome extends  Activity implements Runnable {


	SharedPreferences.Editor editor;
	Notification notification;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		addNotificaction();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().setBackgroundDrawableResource(R.color.trans);

		}
		new Thread(this).start();
		Thread thread = new Thread();
		
	}

	public void run() {
		try {
			
			Thread.sleep(2000);

		
   
			SharedPreferences sp=getBaseContext() .getSharedPreferences("first",
	 				Context.MODE_WORLD_READABLE);
		String time= sp.getString("first", "").toString();
		System.out.println(time);
		String s1="first";
		
		if(time.equals(s1)){

       	   Intent intent = new Intent (Welcome.this,Main.class);
       	   startActivity(intent);
		}else
		{

	       	   Intent intent = new Intent (Welcome.this,GuideActivity.class);
	       	   startActivity(intent);
		}

	

		} catch (InterruptedException e) {

		}
		finish();

	}
	@SuppressWarnings("deprecation")
	private void addNotificaction() {
	
		
Intent inten1t =new   Intent (Welcome.this,Myservice.class);
startService(inten1t);

	}
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		}
		public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		}
}
