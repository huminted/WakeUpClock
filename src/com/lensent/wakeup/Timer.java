package com.lensent.wakeup;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimerTask;

import com.lensent.wakeup.R.id;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Timer extends Activity {
	private int cha;
	private TextView textView,textView1;
	Calendar c = Calendar.getInstance();
	SharedPreferences chaPreferences;
	SharedPreferences sp;
	Editor editor1, editor2;
	EditText ed;
	private int month, year, day;
	TextView middle;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timer);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().setBackgroundDrawableResource(R.color.trans);

		}
		textView = (TextView) findViewById(R.id.e);
		textView1 = (TextView) findViewById(R.id.textView1);
		ed = (EditText) findViewById(R.id.ed);

		final int amonth = c.get(Calendar.MONTH) + 1;
		final int aday = c.get(Calendar.DAY_OF_MONTH);

		DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker1);
		
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		year = calendar.get(Calendar.YEAR); 
		month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
       
		
		
		
		
		
		datePicker.init(year, month, day, new OnDateChangedListener() {
			
			
			@Override
			public void onDateChanged(DatePicker view, int year, int month,
					int day) {
				 
				
		       
				
				Timer.this.month = month + 1;
				Timer.this.day = day;
				int cmonth = (month - amonth) * 30;
				int cday = day - aday;
				Math.abs(cmonth);
				cha = Math.abs(cmonth) + Math.abs(cday);
				textView.setText(String.valueOf(cha));
			}
		});

		Button bt = (Button) findViewById(R.id.bt);
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				System.out.println("选取的月和日" + month + "月" + day + "日");
				System.out.println("当前的月和日" + amonth + "月" + aday + "日");
				System.out.println("距离多少天" + cha);
				// intent.putExtra("timer", String.valueOf(cha))

				Intent intent = new Intent();
				intent.setAction("timer");
				intent.putExtra("timer", cha);
				Timer.this.sendBroadcast(intent);

				chaPreferences = getSharedPreferences("cha",
						Context.MODE_PRIVATE);
				editor1 = chaPreferences.edit();
				editor1.putInt("cha", cha);
				editor1.commit();

				sp = getSharedPreferences("timer", Context.MODE_PRIVATE);
				editor2 = sp.edit();
				String s2 = ed.getText().toString();
				editor2.putString("timer", s2);
				editor2.commit();

				textView.setText(String.valueOf(chaPreferences.getInt("cha", 0)));
				Toast.makeText(getApplicationContext(), "设置完成",Toast.LENGTH_LONG).show();
				finish();

			}
		});

	};

	public void read() {

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