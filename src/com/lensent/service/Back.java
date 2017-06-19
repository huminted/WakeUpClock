package com.lensent.service;

import java.util.Timer;
import java.util.TimerTask;

import com.lensent.wakeup.AlarmActivity;
import com.lensent.wakeup.Main;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;

public class Back extends Service {

	SharedPreferences.Editor editor;

	public void onCreate() {

		SharedPreferences service = getSharedPreferences("backservice",
				Context.MODE_APPEND);
		editor = service.edit();
		String s2 = "backservice";

		editor.putString("backservice", s2);
		editor.commit();

		SharedPreferences wifi_now = getSharedPreferences("back",
				Context.MODE_APPEND);

		final String back = wifi_now.getString("back", "");

		final WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

		final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		final Timer timer = new Timer();
		final TimerTask task = new TimerTask() {

			@Override
			public void run() {

				wifiManager.startScan();
				wifiManager.getScanResults();

				String s1 = wifiManager.getScanResults().toString();
				System.out.println(s1);
				if (s1.indexOf(back) != -1) {

					System.out.println("还在家");

				} else {

					Intent intent = new Intent(Back.this, Wifiremind.class);

					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

					startActivity(intent);

					System.out.println("不在家");
				}

			}

			int i = 1;

		};
		timer.schedule(task, 3000, 1 * 600000000);

	}

	public void OnDestroy() {
		super.onDestroy();
		System.out.println("到家提醒已经关闭");

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
