package com.lensent.wakeup;

import com.thinkland.sdk.android.JuheSDKInitializer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

public class MyApp extends Application {
	


	public  Uri uri;
	
	

	   
	@Override
    public void onCreate() {  
		super.onCreate();
		JuheSDKInitializer.initialize(getApplicationContext());
		setUri( uri); 
		
		}

	
	
	// RingUri
	public void setUri(Uri uri  ) {	this.uri=uri;}	
	public Uri getUri()
    { return uri; }
   
	
	

}
