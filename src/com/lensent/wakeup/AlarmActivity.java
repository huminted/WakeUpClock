package com.lensent.wakeup;
import java.util.Calendar;

import com.lensent.service.AlarmService;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.domob.android.ads.DomobAdEventListener;
import cn.domob.android.ads.DomobAdManager.ErrorCode;
import cn.domob.android.ads.DomobAdView;
import com.skyfishjy.library.RippleBackground;;

public class AlarmActivity<handler, handler1> extends Activity implements
		OnClickListener {
	MediaPlayer alarmMusic;
	Vibrator vibrator;
	SensorManager sensorManager;
	SoundPool soundPool;
	Parameters parameters;
	Camera camera;
	Button bool;
	Intent intent;
	TextView tv;

	private RelativeLayout rel;
	private static final String TAG = "AlarmActivity";
	private static final int SENSOR_SHAKE = 10;

	private Button btnBlow = null;
	private TextView tvDisplay = null;

	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

	private boolean stop = false;
	DomobAdView mAdview;
	private RecordThread recordThread = null;
	CheckBox cb;
	public static final String PUBLISHER_ID = "56OJw9a4uNK6f6NRmT";
	public static final String InlinePPID = "16TLuyhlAp-X4NUEpXH1lqmi";
	private static final String LOCK_TAG = null;
	HomeKeyEventBroadCastReceiver receiver;
	RippleBackground rippleBackground;
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().setBackgroundDrawableResource(R.color.trans);

		}
		

		receiver = new HomeKeyEventBroadCastReceiver();
		registerReceiver(receiver, new IntentFilter(
				Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

		AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND) ;
		int current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

		audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
		audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
		audioManager.setMicrophoneMute(false);          
		audioManager.setSpeakerphoneOn(true);//使用扬声器外放，即使已经插入耳机  
		setVolumeControlStream(AudioManager.STREAM_MUSIC);//控制声音的大小  
		audioManager.setMode(AudioManager.STREAM_ALARM);   
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, max/2, AudioManager.FLAG_PLAY_SOUND);
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		audioService.setStreamVolume(AudioManager.STREAM_MUSIC, 15, 15);

		KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		KeyguardLock kl = km.newKeyguardLock("unLock");
		kl.disableKeyguard();
		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.ACQUIRE_CAUSES_WAKEUP
						| PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
		wl.acquire(); // Open
		wl.release();

		Intent  intent   =new  Intent(AlarmActivity.this,AlarmService.class);
		startService(intent);		
		
		TextView notice = (TextView) findViewById(R.id.notice);

		SharedPreferences sp = getSharedPreferences("notice",
				Context.MODE_WORLD_READABLE);

		final String s2 = sp.getString("notice", "").toString();
		notice.setText(s2);

		btnBlow = (Button) findViewById(R.id.blow);
		//TODO
		 rippleBackground=(RippleBackground)findViewById(R.id.content);
		 
		tvDisplay = (TextView) findViewById(R.id.tvDisplay);
		btnBlow.setOnClickListener(this);
	
		tvDisplay.setText("   点击按钮开始");

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);


		rel = (RelativeLayout) this.findViewById(R.id.rel);
		Animation anim = AnimationUtils.loadAnimation(AlarmActivity.this,
				R.anim.myanim);
		rel.startAnimation(anim);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);

		SharedPreferences SP = getSharedPreferences("vibrator",
				Context.MODE_PRIVATE);
		if (SP.getBoolean("vibrator", false)) {

			vibrator.vibrate(new long[] { 0, 0, 0 }, 0);

		} else {
			vibrator.vibrate(new long[] { 1000, 1200, 1400, 1600, 1800, 2000,
					2000, 2200 }, 0);

		}

		AudioManager audioService1 = (AudioManager) getSystemService(AUDIO_SERVICE);
		audioService1.setStreamVolume(AudioManager.STREAM_MUSIC, 15, 15);
		MyApp myApp = (MyApp) getApplication();
		if (myApp.getUri() == null) {
			alarmMusic = MediaPlayer.create(getApplicationContext(),
					R.raw.first);
		
		} else {
			alarmMusic = MediaPlayer.create(getApplicationContext(),
					myApp.getUri());
		
		
		}

		alarmMusic.start();

		alarmMusic.setLooping(true);
		alarmMusic.setWakeMode(getApplicationContext(),
				PowerManager.PARTIAL_WAKE_LOCK);
		
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (sensorManager != null) {// 注册监听器
			sensorManager.registerListener(sensorEventListener,
					sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					SensorManager.SENSOR_DELAY_NORMAL);
			// 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (sensorManager != null) {// 取消监听器
			sensorManager.unregisterListener(sensorEventListener);
		}
	}

	/**
	 * 重力感应监听
	 */
	int o=0;
	private SensorEventListener sensorEventListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			// 传感器信息改变时执行该方法
			float[] values = event.values;
			float x = values[0]; // x轴方向的重力加速度，向右为正
			float y = values[1]; // y轴方向的重力加速度，向前为正
			float z = values[2]; // z轴方向的重力加速度，向上为正

		
			
         o++;
			int fl = (int) (x + y + z);

//			System.out.println(x * x + y * y + z * z);
//			System.out.println("加" + fl);
		
			int   d= (int) Math.sqrt(x*x+y*y);
			int m= (int) Math.sqrt(d*d+z*z);
		       	float  t=(float)1/2*m*10;
		       	System.out.println( "d"+ d);
		     	System.out.println( "m"+ m);
		      	System.out.println( "o"+ o);
		     	System.out.println( "t"+ t);
			
			
			if (t>=165 && o>45) {

				Message msg = new Message();
				msg.what = SENSOR_SHAKE;
				handler1.sendMessage(msg);
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
	};

	/**
	 * 动作执行
	 */
	Handler handler1 = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SENSOR_SHAKE:

				Log.i(TAG, "检测到摇晃，执行操作！");
				alarmMusic.stop();
				vibrator.cancel();
				finish();
				Toast.makeText(AlarmActivity.this, "闹钟已经关闭", Toast.LENGTH_LONG)
						.show();
				Intent intent = new Intent();  
			 	 
	            intent.setAction("alarm");  

	            intent.putExtra("alarm","");  
	        	AlarmActivity.this.sendBroadcast(intent);  
				
				
			}

		}

	};

	RelativeLayout mAdContainer;

	BlowHandler handler = new BlowHandler();

	class BlowHandler extends Handler {
		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(2), delayMillis);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// 接收到message后更新UI，并通过isblow停止线程
				tvDisplay.setText("已检测到");
	
				finish();
				vibrator.cancel();
				alarmMusic.stop();

				recordThread.stopRecord();
				update();
				break;
			case 2:

				sleep(100);
				break;
			default:
				break;
			}
		}
	};

	public void onClick(View v) {
		 rippleBackground.startRippleAnimation();
		
		 if (recordThread == null || recordThread.getRecordStatus()) {
			
			tvDisplay.setText("  对着手机大喊吧！！");
			recordThread = new RecordThread(handler, 1); // 点击按钮，启动线程
			recordThread.start();

		} else {
	
			recordThread.stopRecord();
		}
	}

	public void update() {

		handler.sleep(200);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			// 监控/拦截菜单键
		} else if (keyCode == KeyEvent.KEYCODE_HOME) {
			// 由于Home键为系统键，此处不能捕获，需要重写onAttachedToWindow()
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	
	
	class HomeKeyEventBroadCastReceiver extends BroadcastReceiver {
		static final String SYSTEM_REASON = "reason";
		static final String SYSTEM_HOME_KEY = "homekey";// home key
		static final String SYSTEM_RECENT_APPS = "recentapps";// long home key

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
				String reason = intent.getStringExtra(SYSTEM_REASON);
				if (reason != null) {
					if (reason.equals(SYSTEM_HOME_KEY)) {

			        	Intent   intent1 =new Intent(AlarmActivity.this,AlarmActivity.class    );
			        	startActivity(intent1);
						
						
					} else if (reason.equals(SYSTEM_RECENT_APPS)) {
						
						
					}
				}
			}
		}

		
	}

	
	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		System.out.print("destory");

//    	Intent   intent =new Intent(AlarmActivity.this,AlarmActivity.class    );
//    	startActivity(intent);
		super.onDestroy();
	}
	
	
}
