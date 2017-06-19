package com.lensent.service;





import java.util.Timer;
import java.util.TimerTask;

import com.lensent.wakeup.R;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

public class Widget extends  AppWidgetProvider{

	public static final Intent intent = null;
SharedPreferences  sp;
	int i1,cha;
	@Override
	public void onReceive(Context context, Intent intent) {

	sp =context.getSharedPreferences("cha",
                Context.MODE_PRIVATE);  
                       cha =	sp.getInt("cha",0);
  System.out.println("sp"+cha);
//		i1=	  intent.getExtras().getInt("timer");
//        Log.i("Recevier1", "接收到:"+i1);  
	
  	  
		super.onReceive(context, intent);
	}
	
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		
		super.onDeleted(context, appWidgetIds);
	}
	

	public void onDisabled(Context context, Intent intent) {
		
		
	
		// TODO Auto-generated method stub
		super.onDisabled(context);
	}
	
	
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		   
        Timer timer = new Timer();  
        timer.scheduleAtFixedRate(new MyTime(context,appWidgetManager), 1, 86400000);  
	} 
	

    class MyTime extends TimerTask{  

        AppWidgetManager appWidgetManager;  
        ComponentName thisWidget;  
    	final RemoteViews rv;  
        @SuppressWarnings("unused")
		public MyTime(Context context,AppWidgetManager appWidgetManager){  
            this.appWidgetManager = appWidgetManager;  
           rv = new RemoteViews(context.getPackageName(),R.layout.widget);  
          
              
            thisWidget = new ComponentName(context,Widget.class);  
        }  
        public void run() {     
        
                           cha =	sp.getInt("cha",0);
                           System.out.println(cha);
                                    cha--;
   
                      rv.setTextViewText(R.id.tv,"距离设定的时间还有"+String.valueOf(cha)+"天" );
                      
            appWidgetManager.updateAppWidget(thisWidget,rv);  
              
        }  
          
    }  
	
    public          RemoteViews getRemoteViews(Context context) {
    	
		return null;  
    	
    }
}
