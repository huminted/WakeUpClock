package com.lensent.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AlarmService  extends Service {

	
		
		public void onCreate(Intent  intent) {   
	      
			System.out.println(
					intent.getExtras().getInt("alarm"));
			
	    }   
	    
	   
	    public IBinder onBind(Intent intent) {
			return null;   
	        // 一个客户端通过bindService()绑定到这个service   
	          
	    }   
	    public boolean onUnbind(Intent intent) {
			return false;   
	        // 所有的客户端使用unbindService()解除了绑定    
	         
	    }   
	    public void onRebind(Intent intent) {   
	        // 一个客户端在调用onUnbind()之后，正使用bindService()绑定到service   
	    }   
	    public void onDestroy() {   
	        // service不再被使用并将被销毁   
	    }   

		
		
		
		
		



	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	

