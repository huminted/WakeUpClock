package com.lensent.wakeup;

import java.util.Calendar;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmAvtivity_Normal extends Activity {
	Vibrator vibrator;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
       super.setContentView(R.layout.alarm_narmal);
       
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().setBackgroundDrawableResource(R.color.trans);

		}
   	final AlarmManager   am = (AlarmManager) getBaseContext().getSystemService(
			Context.ALARM_SERVICE);
       
	final MediaPlayer alarmMusic;
	vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
	vibrator.vibrate(new long[] { 1000, 1200, 1400, 1600, 1800, 2000, 2000,
			2200 }, 0);
   	
	KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
	KeyguardLock kl = km.newKeyguardLock("unLock");
	kl.disableKeyguard();
	PowerManager pm= (PowerManager) getSystemService(POWER_SERVICE);
	PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
			| PowerManager.SCREEN_DIM_WAKE_LOCK,  "bright");
	wl.acquire();           //Open
	wl.release();
   	
	MyApp   myApp  =(MyApp) getApplication();
	if (myApp.getUri()==null) {
		alarmMusic = MediaPlayer.create(getApplicationContext(),R.raw.alarm);
	} else {
		alarmMusic = MediaPlayer.create(getApplicationContext(),myApp.getUri());
	}
     
	
	alarmMusic.start();
	alarmMusic.setLooping(true);
	alarmMusic.setWakeMode(getApplicationContext(),
			PowerManager.PARTIAL_WAKE_LOCK);
   	
   	
   	TextView   notice =(TextView) findViewById(R.id.notice_narmal);

	SharedPreferences sp=getSharedPreferences("notice",
				Context.MODE_WORLD_READABLE);
          
	final	String s2=sp.getString("notice", "").toString();
	notice.setText(s2);
   	
       
       Button  closeButto =(Button) findViewById(R.id.close);
       closeButto.setOnClickListener( new  OnClickListener() {
		
		@Override
		public void onClick(View v) {
	am.cancel(PendingIntent.getActivity(getBaseContext(),getResultCode(), new Intent(getBaseContext(),AlarmAvtivity_Normal.class), 0));
	  finish();
		alarmMusic.stop();
		vibrator.cancel();
	Toast.makeText(getBaseContext(), "你已经关闭闹钟", Toast.LENGTH_SHORT).show();
		}
	});
       Button  sleepmore =(Button) findViewById(R.id.sleepmore);
       sleepmore.setOnClickListener(new OnClickListener() {
    	 	Calendar c=  Calendar.getInstance();	
		@Override
		public void onClick(View v) {
			alarmMusic.stop();
			vibrator.cancel();
		 	Intent intent =  new Intent(AlarmAvtivity_Normal.this,AlarmAvtivity_Normal.class);
        	PendingIntent pi = PendingIntent.getActivity(AlarmAvtivity_Normal.this,0,intent,0);
		am.setRepeating(AlarmManager.RTC_WAKEUP,
				c.getTimeInMillis(), 5*60*1000, pi);
		  finish();
		Toast.makeText(getBaseContext(), "将在5分钟后提醒你", Toast.LENGTH_SHORT).show();
		}
	});

}

	protected int getResultCode() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
	       
	        return true;
	    } else if(keyCode == KeyEvent.KEYCODE_MENU) {
	        //监控/拦截菜单键
	    } else if(keyCode == KeyEvent.KEYCODE_HOME) {
	        //由于Home键为系统键，此处不能捕获，需要重写onAttachedToWindow()
	    }
	    return super.onKeyDown(keyCode, event);
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
