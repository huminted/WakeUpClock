package com.lensent.wakeup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TabHost;

public class AlarmList extends Activity {

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().setBackgroundDrawableResource(R.color.trans);

		}
	
		tabHost   =  (TabHost) findViewById(android.R.id.tabhost);	
		tabHost.setup();
		tabHost.addTab(tabHost.newTabSpec("alarm").setIndicator("闹钟").setContent(R.id.tabalarm));
		
	}

	


	
	
	private  TabHost  tabHost;

}
