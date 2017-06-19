package com.lensent.service;

import java.util.TimerTask;
import java.util.Timer;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;

public class Home extends Service {
	SharedPreferences.Editor editor;

	public void onCreate() {

		SharedPreferences homeservice = getSharedPreferences("homeservice",
				Context.MODE_APPEND);
		editor = homeservice.edit();
		String s1 = "homeservice";
		editor.putString("homeservice", s1);
		editor.commit();

		SharedPreferences wifi_now = getSharedPreferences("home",
				Context.MODE_APPEND);

		final String home = wifi_now.getString("home", "");

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

				if (s1.indexOf(home) != -1) {
					Intent intent = new Intent(Home.this, Wifiremind.class);

					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

					startActivity(intent);
					System.out.println("包含");

				} else {

					System.out.println("不包含");
				}

			}

		};
		timer.schedule(task, 3000, 600000000);

	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	public void OnDestroy() {
		super.onDestroy();
		System.out.println("到家提醒已经关闭");

	}

}
