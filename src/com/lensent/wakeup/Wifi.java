package com.lensent.wakeup;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.lensent.service.Back;
import com.lensent.service.Home;
import com.umeng.analytics.MobclickAgent;

public class Wifi extends Activity {
	SharedPreferences prefreferemces;

	SharedPreferences.Editor editor;
	WifiManager wifiManager;
	WifiInfo wifiInfo;
	List<ScanResult> wifiList;
	private ArrayAdapter<String> adapter;

	private ArrayAdapter<String> adapter1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifi);

		final WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().setBackgroundDrawableResource(R.color.trans);

		}
		SharedPreferences sp = getSharedPreferences(KEY_WIFI_LIST,
				Context.MODE_WORLD_READABLE);
		editor = sp.edit();

		final EditText t1 = (EditText) findViewById(R.id.remindtv1);
		final EditText t2 = (EditText) findViewById(R.id.remindtv2);

		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		Spinner spinner1 = (Spinner) findViewById(R.id.spinner2);

		Button arrive = (Button) findViewById(R.id.arrive_btn);
		arrive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SharedPreferences remind2 = getSharedPreferences(
						"arrived_remind", Context.MODE_WORLD_READABLE);
				editor = remind2.edit();
				String s1 = t2.getText().toString();
				editor.putString("arrived_remind", s1);
				editor.commit();
				Intent intent = new Intent(Wifi.this, Home.class);
				startService(intent);
			}

		});

		Button left = (Button) findViewById(R.id.left_btn);
		left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences remind = getSharedPreferences("left_remind",
						Context.MODE_WORLD_READABLE);
				editor = remind.edit();
				String s2 = t1.getText().toString();
				editor.putString("left_remind", s2);
				editor.commit();

				Intent intent = new Intent(Wifi.this, Back.class);
				startService(intent);
			}

		});

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);

		// 设置下拉列表的风格

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// 将adapter 添加到spinner中

		spinner.setAdapter(adapter);

		wifiList = wifiManager.getScanResults();

		for (ScanResult result : wifiList) {
			// 打印每个热点的SSID
			System.out.println(result.SSID);
			String str = result.SSID;
			adapter.add(str);

			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						final int position, long id) {

					new AlertDialog.Builder(Wifi.this)
							.setTitle("操作")
							.setItems(new CharSequence[] { "保存为常用地址" },
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											switch (which) {

											// TODO 保存
											case 0:

												// TODO
												String content = adapter
														.getItem(position);
												editor.putString(KEY_WIFI_LIST,
														content);
												editor.commit();
												System.out.println("CONTENT"
														+ content);
												adapter1.add(adapter
														.getItem(position));

												break;
											default:
												break;

											}

										}
									}).setNegativeButton("关闭", null).show();

					SharedPreferences pr = getSharedPreferences("back",
							Context.MODE_APPEND);
					String back = adapter.getItem(position);
					editor.putString("back", back);
					editor.commit();

					System.out.println("你选择了" + adapter.getItem(position));
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub

				}

			});

			adapter1 = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item);
			adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// 将adapter 添加到spinner中

			String s2 = sp.getString(KEY_WIFI_LIST, "").toString();
			System.out.println(s2);
			adapter1.add(s2);

			spinner1.setAdapter(adapter1);

			spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {

					SharedPreferences pr = getSharedPreferences("home",
							Context.MODE_APPEND);
					String home = adapter.getItem(position);
					editor.putString("home", home);
					editor.commit();

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub

				}

			});
		}

	}

	private static final String KEY_WIFI_LIST = "wifilist";

	private void savewifiList() {
		SharedPreferences sh = getSharedPreferences(KEY_WIFI_LIST,
				Context.MODE_PRIVATE);

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < adapter1.getCount(); i++) {
			sb.append(adapter1.getItem(i)).append(",");
		}

		if (sb.length() > 1) {
			String content = sb.toString().substring(0, sb.length() - 1);
			editor.putString(KEY_WIFI_LIST, content);
			System.out.println(content);
		} else {
			editor.putString(KEY_WIFI_LIST, null);
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
