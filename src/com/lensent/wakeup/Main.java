package com.lensent.wakeup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import tools.PureNetUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;

public class Main extends Activity {
	public static final String APP_ID = "wxd365b4861fe68d0a";

	Button start;
	Button setTime;
	AlarmManager aManager;
	Calendar currentTime = Calendar.getInstance();
	Vibrator virator;
	Intent intent;
	EditText ed;
	TimePickerDialog tp;
	String time1String = null;
	String time2String = null;
	String time3String = null;
	Calendar date;
	TextView timetext;
	TextView setTime2;
	TextView setTime3;
	Parameters params;
	
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	static TextView temp;

	static TextView weather;

	static TextView city,maincityname,maintemp;
	Button seaButton;
	EditText adress;

	private LayoutAnimationController lac;
	private ScaleAnimation sa;
	private SlidingMenu slidingmenu;

	protected String Url;
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	
	private void updata() {
		  Toast.makeText(getApplicationContext(), "已经是最新版",Toast.LENGTH_LONG).show();
		
	}

	@SuppressLint("InlinedApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clock);

		slidingmenu = new SlidingMenu(this);
		slidingmenu.setMode(SlidingMenu.LEFT);
		slidingmenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingmenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingmenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		slidingmenu.setMenu(R.layout.slidingmenu);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().setBackgroundDrawableResource(R.color.green);

		}
		
		final SharedPreferences cityname = getSharedPreferences("city",
				Context.MODE_PRIVATE);
		if (cityname.getString("city", "").toString() != null) {
			
			params = new Parameters();
			params.add("cityname",cityname.getString("city", null) );
			params.add("dtype", "json");
			GetTodayTemperatureByCity(cityname.getString("city", ""));
			
		}else {

				
		}
		   		
		
		maincityname=(TextView) findViewById(R.id.mian_cityname);
		maintemp=(TextView) findViewById(R.id.maintemp);				
		temp=(TextView) findViewById(R.id.temp);
		weather=(TextView) findViewById(R.id.weather1);
		city=(TextView) findViewById(R.id.city);
		adress=(EditText) findViewById(R.id.adress);
		seaButton=(Button) findViewById(R.id.search_btn);
		seaButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				final SharedPreferences cityname = getSharedPreferences("city",
						Context.MODE_PRIVATE);
				
				Editor ed = cityname.edit();
			    ed.putString("city", adress.getText().toString());
				ed.commit();
				System.out.println(cityname.getString("city", null));
				
				params = new Parameters();
				params.add("cityname",cityname.getString("city", null) );
				params.add("dtype", "json");
				
				GetTodayTemperatureByCity( adress.getText().toString());
				
				System.out.println(adress.getText().toString());
				city.setText(adress.getText());
				temp.setText(temp.getText());
				
				maincityname.setText(city.getText());
				maintemp.setText(temp.getText());
                
//				System.out.println(GetTodayTemperatureByCity(adress.getText().toString()));
				Toast.makeText(getApplicationContext(), "天气搜索成功",Toast.LENGTH_SHORT).show();
			}
		});
		adress.setOnKeyListener( new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				
				if(keyCode == KeyEvent.KEYCODE_ENTER){  
					  
					InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
					  
					if(imm.isActive()){  
					  
					imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0 );  
					  
					}  
					  
					return true;  
					  
					}  
					  
					return false;  
				
					  
				
			}
		});
		
		
		
//		CheckBox cb = (CheckBox) findViewById(R.id.wakeup_cb);
//		final SharedPreferences strong = getSharedPreferences("CB",
//				Context.MODE_PRIVATE);
//		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView,
//					boolean isChecked) {
//
//				Editor ed = strong.edit();
//				ed.putBoolean("CB", isChecked);
//				ed.commit();
//				MediaPlayer alarmMusic = MediaPlayer.create(
//						getApplicationContext(), R.raw.check);
//				alarmMusic.start();
//				if (strong.getBoolean("CB", false)) {
//
//				} else {
//					Toast.makeText(Main.this, "强制叫醒已经打开", Toast.LENGTH_SHORT)
//							.show();
//				}
//
//			}
//		});
//
//		cb.setChecked(strong.getBoolean("CB", false));
//
//		SharedPreferences sp = getSharedPreferences(AlarmView.class.getName(),
//				Context.MODE_PRIVATE);
//		String content = sp.getString("alarmlist", null);
//
//		if (content != null) {
//			String[] timeStrings = content.split(",");
//			for (String string : timeStrings) {
//
//			}
//
//		}

		// 设置分享内容
		mController
				.setShareContent("一款来自生活的极致产品，独辟蹊径  ，追求卓越! "
						+ "下载地址:    "
						+ "http://iwakeup.cn");
		// 设置分享图片, 参数2为图片的url地址
		mController.getConfig().removePlatform(SHARE_MEDIA.RENREN,
				SHARE_MEDIA.DOUBAN);
		mController.setShareMedia(new UMImage(Main.this,
				"http://ww3.sinaimg.cn/large/005H7rS1gw1esz2v6mxtej30yz0ls0ub.jpg"));
		// 设置分享图片，参数2为本地图片的资源引用
		// mController.setShareMedia(new UMImage(getActivity(),
		// R.drawable.ico));


		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this,  "1101189591","sk3F7Aw2YjPtF0zO");
		qqSsoHandler.addToSocialSDK();  
		
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, "1101189591",
                "sk3F7Aw2YjPtF0zO");
        qZoneSsoHandler.addToSocialSDK();
        mController.getConfig().setSsoHandler(new SinaSsoHandler());

		SharedPreferences first1 = getSharedPreferences("first",
				Context.MODE_WORLD_READABLE);
		editor = first1.edit();
		String first = "first";
		editor.putString("first", first);
		System.out.println(first1);
		editor.commit();

		AnalogClock ac = (AnalogClock) findViewById(R.id.AnalogClock);

		sa = new ScaleAnimation(0, 1, 0, 1);
		sa.setDuration(500);
		ac.setAnimation(sa);

		aManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);


	


		Button set = (Button) findViewById(R.id.set);
		set.setOnClickListener(new OnClickListener()

		{
			@Override
			public void onClick(View source) {
//TODO
				Intent intent = new Intent(Main.this,AlarmList.class);
				startActivity(intent);
			}
		});

		 Intent viewIntent = new Intent(this, Main.class);

	        PendingIntent viewPendingIntent =
	                PendingIntent.getActivity(this, 0, viewIntent, 0);


	        NotificationCompat.Builder notificationBuilder =
	                new NotificationCompat.Builder(this)
	                        .setSmallIcon(R.drawable.ico_noti)
	                        .setContentTitle("Test")
	                        .setContentText("醒来正在运行")
	                        .setContentIntent(viewPendingIntent);

	      
		NotificationManager manager = (NotificationManager) this

		.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = new Notification();

		notification.icon = R.drawable.ico_noti;

		notification.tickerText = "醒来";

		notification.flags = Notification.FLAG_NO_CLEAR;

		// notification.audioStreamType =
		// android.media.AudioManager.ADJUST_LOWER;
		Intent intent = new Intent(this, Main.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_ONE_SHOT);

		notification.setLatestEventInfo(this, "醒来", null, pendingIntent);

		manager.notify(1, notification);

		
		ListView listView = (ListView) findViewById(R.id.listView1);
		SimpleAdapter sadpter;
		int[] imageIds = new int[] { R.drawable.new_timer, R.drawable.new_location,
				R.drawable.new_updata, R.drawable.new_share };

		String[] text = new String[] { "计时提醒", "地点提醒", "检查更新", "分享醒来" };
	     

		final List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < imageIds.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("image", imageIds[i]);
			listItem.put("itemname", text[i]);
			listItems.add(listItem);

		}
		sadpter = new SimpleAdapter(this, listItems, R.layout.slid_item,
				new String[] { "image", "itemname" }, new int[] { R.id.imageView1,
						R.id.tvDisplay });
		listView.setAdapter(sadpter);
		listView.setAdapter(sadpter);

		listView.setOnItemClickListener(new OnItemClickListener() {
//TODO
			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long id) {
				switch (position) {
				case 0:
					Intent intent = new Intent(Main.this, Timer.class);
					startActivity(intent);
					SharedPreferences chaPreferences = getSharedPreferences(
							"cha", Context.MODE_PRIVATE);
					editor = chaPreferences.edit();
					editor.putString("cha", null);
					editor.commit();

					break;

				case 1:
					 Intent intent2=new Intent(Main.this,Wifi.class);
					 startActivity(intent2);
					break;
				case 2:
					updata();
					break;
				case 3:
					mController.openShare(Main.this, false);
					
					break;
					

				default:
					break;
				}

			}
		});
	}
	
	private Context mContext;
    //TODO
	
	 public static String excute(String cityName){
		 StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();

	        StrictMode.setThreadPolicy(policy);
	        String url="http://api.jisuapi.com/weather/query?appkey=6f3e4c3db33d0bc7&city="+cityName;
		 
//	        String url=//此处以返回json格式数据示例,所以format=2,以根据城市名称为例,cityName传入中文
//	                "http://v.juhe.cn/weather/index?cityname="+cityName+"&key=a6eaf7e8a7882f25e1af707f5a537fcd";
	        return PureNetUtil.get(url);//通过工具类获取返回数据
//	        a6eaf7e8a7882f25e1af707f5a537fcd
	    }
	 
	 
	 
	 public static String GetTodayTemperatureByCity(String cityString) {
	        final String result=excute(cityString);
	        
	        
	        
	        new Thread(new Runnable(){
	            @Override
	            public void run() {
	                try {
	                	JSONObject dataJson = new JSONObject(result);
	    				JSONObject response = dataJson.getJSONObject("result");
//	    				JSONObject data = response.getJSONObject("today");
	    				temp.setText(response.getString("templow")+"℃"+" ~ "+response.getString("temphigh")+"℃");
	    				weather.setText(response.getString("weather"));
	    				city.setText(response.getString("city"));
//	    				temp.setText("当前气温:" + data.getString("temperature") + "天气："
//	    						+ data.getString("weather") + "城市："
//	    						+ data.getString("city"));
	    				maincityname.setText(response.getString("city"));
	    				maintemp.setText(response.getString("templow")+"℃"+" ~ "+response.getString("temphigh")+"℃");
	    				
	                }
	                catch (Exception ex) {
	                    ex.printStackTrace();
	                }
	            }
	        }).start();
	     
        
			return result;
			
	    }
	

  					public void onFinish() {
  						// TODO Auto-generated method stub
  						Toast.makeText(getApplicationContext(), "finish",
  								Toast.LENGTH_SHORT).show();
  					}

  					
  					public void onFailure(int statusCode,
  							String responseString, Throwable throwable) {
  						// TODO Auto-generated method stub
//  						tv.append(throwable.getMessage() + "\n");
  					}
  				
		
	

@Override 
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    /**使用SSO授权必须添加如下代码 */
    UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
    if(ssoHandler != null){
       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
    }
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
