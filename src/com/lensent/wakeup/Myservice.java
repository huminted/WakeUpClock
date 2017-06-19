package com.lensent.wakeup;

import java.util.Timer;
import java.util.TimerTask;

import com.lensent.wakeup.R;
import com.lensent.wakeup.R.raw;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class Myservice extends Service {
	
	@Override   
	public int onStartCommand(Intent intent, int flags, int startId) {
	        return START_STICKY;   
	}
	
	@Override
	public void onCreate() {

		 show();
		// startTimer();
	}

	public void onDestroy() {
		
		Intent localIntent = new Intent();
		localIntent.setClass(this, Myservice.class);
		this.startService(localIntent);

	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	
	public void  show(){

		NotificationManager manager = (NotificationManager) this

				.getSystemService(Context.NOTIFICATION_SERVICE);

				Notification notification = new Notification();


				notification.icon = R.drawable.ico_noti;

				notification.tickerText = "醒来 wakeup";

				
				notification.flags = Notification.FLAG_NO_CLEAR;

				// notification.audioStreamType =
				// android.media.AudioManager.ADJUST_LOWER;
				Intent intent = new Intent(this, Main.class);
				PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
						intent, PendingIntent.FLAG_ONE_SHOT);

				notification.setLatestEventInfo(this, "醒来", null, pendingIntent);

				manager.notify(1, notification);

		
	}
	
	
	
	
	
	
	
	
	
	
}
