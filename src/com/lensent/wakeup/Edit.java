package com.lensent.wakeup;

import java.io.File;
import java.util.Calendar;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Edit extends Activity {

	protected static final String PREFS_NAME = null;
	TextView tv;
	Button bt;
	AlarmManager  	alarmManager;
	//定义类型
    private static final int RingtongButton=0;
    private static final int AlarmButton=1;
    private static final int NotificationButton=2;

    
    //铃声文件夹
    private String strRingtongFolder="/sdcard/media/ringtones";
    private String strAlarmFolder="/sdcard/media/alarms";
    private String strNotificationFolder="/sdcard/media/notifications";

	SharedPreferences.Editor editor;
    Context mContext = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
       super.setContentView(R.layout.edit);
       
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().setBackgroundDrawableResource(R.color.trans);

		}
          final      EditText   notice =(EditText) findViewById(R.id.shaketv); 
       
       
          CheckBox  cb  = (CheckBox) findViewById(R.id.vibrator_cb);
	       final SharedPreferences strong = 
	     getSharedPreferences("vibrator",Context.MODE_PRIVATE );
	       cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	    	   @Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	    	
		         Editor  ed =   strong.edit();
		         ed.putBoolean("vibrator", isChecked);
	    		  ed.commit();
	    		    MediaPlayer	  alarmMusic = MediaPlayer.create(getApplicationContext(),
	    					R.raw.check);
	    						alarmMusic.start();
	    		  if (strong.getBoolean("vibrator", false)) {
	    		
					
				} else {
					  Toast.makeText(Edit.this, "已经打开", Toast.LENGTH_SHORT).show();
				}
	    		  
	    		 
	    		
			}
		} ) ;	
		  
	       
	      cb.setChecked(strong.getBoolean("vibrator", false));
    
   	
 
      // 防打扰 
     final  SharedPreferences bother = 
    		     getSharedPreferences("bother",Context.MODE_PRIVATE );
      CheckBox  cb1  = (CheckBox) findViewById(R.id.nobother_cb);
      cb1.setOnCheckedChangeListener(new  OnCheckedChangeListener() {
    	
    	  Calendar c;  
    	  long chour;
    	  long cminute;
    	  long  bhour;
			long   bminute;
			long    htime ;
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			 Editor  ed =   bother.edit();
	         ed.putBoolean("bother", isChecked);
    		  ed.commit();
    		   MediaPlayer	  alarmMusic = MediaPlayer.create(getApplicationContext(),
   					R.raw.check);
   						alarmMusic.start();
   						
  			
  		
    		  if (bother.getBoolean("bother", false)) {
			   	   
				    final		 AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
				    		
//				    		 audioService.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
			    		
				            audioService.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
				    		 audioService.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0);
                                
				    		
				    		 
				              	
				    		 Calendar currentTime  = Calendar.getInstance();
				    		 final Calendar calendar = Calendar.getInstance();
							 new  TimePickerDialog(Edit.this, new TimePickerDialog.OnTimeSetListener() {
								
								@Override
								public void onTimeSet(TimePicker  tp,  int  hourOfDay,  int  minute) {
									

									calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
									calendar.set(Calendar.MINUTE, minute);
									calendar.set(Calendar.SECOND, 0);
									
									    
									
									calendar.set(Calendar.MILLISECOND, 0);
								int	ahour =  hourOfDay;
								 int	aminute = minute;
								 SharedPreferences  hoursp = getSharedPreferences("hour", Context.MODE_PRIVATE);
								
									  SharedPreferences  minutesp = getSharedPreferences("minute", Context.MODE_PRIVATE);	
									
						 chour =	hoursp.getLong("hour", 0);
						 cminute =	minutesp.getLong("minute", 0);
						
						 bhour = ahour-chour;
						  bminute= aminute-cminute;
				   htime = bhour*60*60*1000+bminute*60*1000;
						
						
								}
							},currentTime.get(Calendar.HOUR_OF_DAY) ,currentTime.get(Calendar.MINUTE) 
						      , false ).show();
							 Toast.makeText(Edit.this, "防打扰已经打开,请选择你的睡眠起始时间", Toast.LENGTH_LONG).show();
							 final Handler handler = new Handler();  
						     Runnable runnable = new Runnable(){  
						         @Override  
						         public void run() {  
						        	 audioService.setStreamVolume(AudioManager.STREAM_RING, 8, 0);
									 audioService.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 8, 0);
									  audioService.setStreamVolume(AudioManager.STREAM_MUSIC, 8, 0);
									
									handler.postDelayed(this,htime);  
						         }   
						     };   
						     handler.postDelayed(runnable, 50);// 打开定时器，执行操作  	 
							 
							 

				   	   
					} else {
						 AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
						 audioService.setStreamVolume(AudioManager.STREAM_RING, 8, 0);
						 audioService.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 8, 0);
						 Toast.makeText(Edit.this, "防打扰已经关闭", Toast.LENGTH_SHORT).show();
					}
    		 
		}
	});
      cb1.setChecked(bother.getBoolean("bother", false));
      

       final Intent intent =getIntent();
     
	final	String str= intent.getStringExtra("time").toString();
		 	System.out.println( "取值"+str);
		 	
		 	EditText  et  =(EditText) findViewById(R.id.shaketv);
		final 	String  s1 =  et.getText().toString();
		
    

	       Button   choose  = (Button) findViewById(R.id.choose);
	       choose.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				 if(isFolder(strAlarmFolder)){
	                 Intent intent=new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
	                 intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
	                 intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "设置闹铃铃声");
	                 startActivityForResult(intent, AlarmButton);
	                
				}
				
			}
	    	   
	       });
       
       
       TextView timetv= (TextView) findViewById(R.id.timetv);
       
       timetv.setText(str);

       Button be = (Button) findViewById(R.id.back);
       be.setOnClickListener(new   OnClickListener() {
    	 
		@Override
		public void onClick(View v) {
			finish();
			Intent  BACK= new  Intent(Edit.this,AlarmList.class);
			startActivity(BACK);
			
			SharedPreferences noticesp=getSharedPreferences("notice",
	 				Context.MODE_WORLD_READABLE);
		editor = noticesp.edit();
		String s2=notice.getText().toString();
			editor.putString("notice", s2);
			editor.commit();
			
			
			
			
			  Toast.makeText(Edit.this, "我们将信息已经保存", Toast.LENGTH_SHORT).show();
		
		}	
	});	
       
	}
	
	
	private boolean isFolder(String strFolder){
        boolean tmp = false;
            File f1 = new File(strFolder);
            if (!f1.exists())
            {
                    if (f1.mkdirs())
                    {
                            tmp = true;
                    }
                    else
                    {
                            tmp = false;
                    }
            }
            else
            {
                    tmp = true;
            }
            return tmp;
}
	 MyApp myApp;

	 @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
             if(resultCode!=RESULT_OK){
                     return;
             }
             switch(requestCode){
                   
                     case AlarmButton:
                             try {
                                      //得到我们选择的铃声
                                    
                                    	  Uri pickedUri=data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                                          myApp  =(MyApp) getApplication();
                                          myApp.setUri(pickedUri);

                                //将我们选择的铃声选择成默认
                                      if(pickedUri!=null){
                                    
                                              RingtoneManager.setActualDefaultRingtoneUri(Edit.this, RingtoneManager.TYPE_ALARM, pickedUri);
                                      }
                             } catch (Exception e) {
                                     e.printStackTrace();
                             }
                             break;
                    
             }
             super.onActivityResult(requestCode, resultCode, data);
  
	
	
}

	 @Override  
	    public boolean onKeyDown(int keyCode, KeyEvent event)  
	    {  
	        if (keyCode == KeyEvent.KEYCODE_BACK )  
	        {  
	            // 创建退出对话框  
	            AlertDialog isExit = new AlertDialog.Builder(this).create();  
	            // 设置对话框标题  
	            isExit.setTitle("编辑提示");  
	            // 设置对话框消息  
	            isExit.setMessage("确定要退出吗");  
	            // 添加选择按钮并注册监听  
	            isExit.setButton("确定", listener);  
	            isExit.setButton2("取消", listener);  
	            // 显示对话框  
	            isExit.show();  
	  
	        }  
	          
	        return false;  
	          
	    }  
	    /**监听对话框里面的button点击事件*/  
	    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()  
	    {  
	        public void onClick(DialogInterface dialog, int which)  
	        {  
	            switch (which)  
	            {  
	            case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序  
	                finish();  
	                break;  
	            case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框  
	                break;  
	            default:  
	                break;  
	            }  
	        }  
	    };    
	

	    public void onResume() {
	    	super.onResume();
	    	MobclickAgent.onResume(this);
	    	}
	    	public void onPause() {
	    	super.onPause();
	    	MobclickAgent.onPause(this);
	    	}
	    
	  

}
		
			
			
			