package com.lensent.service;

import com.lensent.wakeup.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Wifiremind extends Activity {

	SharedPreferences.Editor editor;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifiremind);

		SharedPreferences homeservice = getSharedPreferences("homeservice",
				Context.MODE_APPEND);
		final String i = homeservice.getString("homeservice", "").toString();
		editor = homeservice.edit();

		SharedPreferences backservice = getSharedPreferences("backservice",
				Context.MODE_APPEND);
		String i1 = backservice.getString("backservice", "").toString();
		editor = backservice.edit();

		SharedPreferences remind1 = getSharedPreferences("arrived_remind",
				Context.MODE_WORLD_READABLE);
		editor = remind1.edit();
		final String s1 = remind1.getString("arrived_remind", "").toString();

		SharedPreferences remind2 = getSharedPreferences("left_remind",
				Context.MODE_WORLD_READABLE);
		editor = remind2.edit();
		final String s2 = remind2.getString("left_remind", "").toString();

		final TextView tv = (TextView) findViewById(R.id.remind);
		String s3 = "homeservice";
		if (s3.equals(i)) {
			tv.setText(s1);

		}

		else {
			tv.setText(s2);

		}

		Button button = (Button) findViewById(R.id.stopservice);

		button.setOnClickListener(new OnClickListener() {
			private String s3 = "homeservice";

			@Override
			public void onClick(View v) {

				if (s3.equals(i)) {
					
					Intent intent = new Intent(Wifiremind.this, Home.class);
					intent.setAction("com.lensent.service.Back");  
					stopService(intent);
					finish();
					System.out.println("Home关闭按钮");
				}

				else {

					Intent intent1 = new Intent(Wifiremind.this, Back.class);
					intent1.setAction("com.lensent.service.Home");  
					stopService(intent1);
					finish();
					System.out.println("Back关闭按钮");
				}

			}

		});

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
