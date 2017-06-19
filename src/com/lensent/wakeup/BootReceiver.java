package com.lensent.wakeup;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lensent.service.Foundwifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) 
	{
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
		{
			Intent Intent = new Intent(context, Main.class);
			Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(Intent);
		}
	}
}