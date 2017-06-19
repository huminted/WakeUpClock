package com.lensent.service;

import android.R.string;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class Foundwifi extends Service {
	
	WifiManager wifiManager;
	
	public static final Intent ACTION_START = null;
	


	public void onCreate(){
		

 	 WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE); 

	 WifiInfo wifiInfo = wifiManager.getConnectionInfo();  


  String st=wifiInfo.getSSID();
  SharedPreferences pref = getSharedPreferences("WIFI", Context.MODE_APPEND);
 
	
      String name = pref.getString("WIFI SSID", "");
       System.out.println(name);
		
		if(st.equals(name)){
		System.out.println("开启");
		}
	 
	 else
			 System.out.println("=");
		 
		 
	
	}

	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}

	
	
	public void onDestory(){
		
		 Intent localIntent = new Intent();
	        localIntent.setClass(this, Foundwifi.class);  
	        this.startService(localIntent);
	}
}
