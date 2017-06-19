package com.lensent.wakeup;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmView<ViewHolder> extends LinearLayout {
	protected static final android.content.DialogInterface.OnClickListener DialogInterface = null;
	protected static final int flag = 0;
	LayoutInflater layoutInflater;
	protected static final Context context = null;

	protected Class<Edit> act1 = Edit.class;
	SharedPreferences prefreferemces;
	String s1;
	SharedPreferences.Editor editor;
	private LayoutAnimationController lac;
	private ScaleAnimation sa;
	CheckBox cb;
	static long hour;
	static long aminute;
	protected boolean isChecked;
	boolean bl;

	public AlarmView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		// TODO Auto-generated constructor stub
	}

	public AlarmView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();

	}

	public AlarmView(Context context) {

		super(context);

		init();

	}

	private void init() {
		alarmManager = (AlarmManager) getContext().getSystemService(
				Context.ALARM_SERVICE);

	}

	@Override
	protected void onFinishInflate() {

		super.onFinishInflate();

		add = (Button) findViewById(R.id.add);

		LvAlarmList = (ListView) findViewById(R.id.list);

		adapter = new ArrayAdapter<AlarmView.AlarmDate>(getContext(),
				R.layout.list_item);
		LvAlarmList.setAdapter(adapter);

		sa = new ScaleAnimation(0, 1, 0, 1);
		sa.setDuration(500);
		lac = new LayoutAnimationController(sa, 0.5f);
		LvAlarmList.setLayoutAnimation(lac);

		readSaveAlarmList();
		final Calendar calendar = Calendar.getInstance();

		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addAlarm();
			}

		});
		LvAlarmList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						//
						// popup.showAsDropDown(view);
						// popup.showAtLocation(findViewById(R.id.list),Gravity.CENTER,
						// 10,20);

					}

				});

		LvAlarmList
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							final View view, final int position, long id) {

						new AlertDialog.Builder(getContext())
								.setTitle("操作")

								.setItems(
										new CharSequence[] { "删除", "明天不响",
												"后天不响", "后三天不响" },
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												switch (which) {

												// 删除
												case 0:
													deleteAlarm(view, position);
													break;
												// 编辑

												case 1:
													AlarmDate ad = new AlarmDate(
															calendar.getTimeInMillis()
																	+ 48
																	* 60
																	* 60 * 1000);
													deleteAlarm(view, position);

													saveAlarmList();
													adapter.add(ad);
													Toast.makeText(
															getContext(),
															"明天不会响，我将会让你在1天后醒来哟！",
															Toast.LENGTH_LONG)
															.show();
													System.out.println("明天不响");

													break;

												case 2:

													AlarmDate ad1 = new AlarmDate(
															calendar.getTimeInMillis()
																	+ 72
																	* 60
																	* 60 * 1000);
													deleteAlarm(view, position);

													saveAlarmList();
													adapter.add(ad1);
													Toast.makeText(
															getContext(),
															"明后天不会响，我将会让你在2天后醒来哟！",
															Toast.LENGTH_LONG)
															.show();
													System.out.println("明后天不响");
													break;
												case 3:

													AlarmDate ad2 = new AlarmDate(
															calendar.getTimeInMillis()
																	+ 96
																	* 60
																	* 60 * 1000);
													deleteAlarm(view, position);

													saveAlarmList();
													adapter.add(ad2);
													Toast.makeText(
															getContext(),
															"明后外天不会响，我将会让你在3天后醒来哟！",
															Toast.LENGTH_LONG)
															.show();
													System.out
															.println("明后外天不响");

													break;

												default:
													break;

												}
											}

										}).setNegativeButton("取消", null).show();

						return true;
					}

				});

	}

	private void deleteAlarm(View View, final int position) {

		Animation anim = AnimationUtils.loadAnimation(getContext(),
				R.anim.myanim);

		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {

				AlarmDate ad = adapter.getItem(position);
				adapter.remove(adapter.getItem(position));
				alarmManager.cancel(PendingIntent.getActivity(getContext(), ad
						.grtId(),
						new Intent(getContext(), AlarmActivity.class), 0));
				saveAlarmList();

			}

		});
		View.startAnimation(anim);

	}

	private PendingIntent pi;

	private void addAlarm() {

		Calendar c = Calendar.getInstance();
		final Calendar calendar = Calendar.getInstance();
		TimePickerDialog aDialog = new TimePickerDialog(getContext(),
				new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {

						calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
						calendar.set(Calendar.MINUTE, minute);
						calendar.set(Calendar.SECOND, 0);
						calendar.set(Calendar.MILLISECOND, 0);
						Calendar currentTime = Calendar.getInstance();

						if (calendar.getTimeInMillis() <= currentTime
								.getTimeInMillis()) {
							calendar.setTimeInMillis(calendar.getTimeInMillis()
									+ 24 * 60 * 60 * 1000);
						}

						hour = hourOfDay;
						aminute = minute;

						SharedPreferences hoursp = getContext()
								.getSharedPreferences("hour",
										Context.MODE_PRIVATE);
						Editor editor = hoursp.edit();

						editor.putLong("hour", hour);
						editor.commit();

						SharedPreferences minutesp = getContext()
								.getSharedPreferences("minute",
										Context.MODE_PRIVATE);
						Editor editor2 = minutesp.edit();
						editor2.putLong("minute", aminute);
						editor2.commit();

					}

				}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
		aDialog.setCanceledOnTouchOutside(true);
		aDialog.show();

		aDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				long l1 = calendar.getTimeInMillis();
				AlarmDate ad = new AlarmDate(l1);

				adapter.add(ad);
				System.out.println(ad);
				SharedPreferences SP = getContext().getSharedPreferences("CB",
						Context.MODE_PRIVATE);

				if (SP.getBoolean("CB", false)) {
					pi = PendingIntent.getActivity(
							getContext(),
							ad.grtId(),
							new Intent(getContext(), AlarmAvtivity_Normal.class),
							0);
					System.out.println("开");

				} else {
					pi = PendingIntent.getActivity(getContext(), ad.grtId(),
							new Intent(getContext(), AlarmActivity.class), 0);
					System.out.println("关");

				}
				alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
						ad.getTime(), 24 * 60 * 60 * 1000, pi);

				Toast.makeText(getContext(), "醒来", Toast.LENGTH_SHORT).show();
				saveAlarmList();
				s1 = timeFormat(hour) + ":" + timeFormat(aminute);
				Intent intent = new Intent(getContext(), Edit.class);
				intent.putExtra("time", s1);
				getContext().startActivity(intent);
				System.out.println(s1);
				((Activity) getContext()).finish();

			}

		});

	}

	public static String timeFormat(long value) {
		return value >= 10 ? "" + value : "0" + value;

	}

	protected SharedPreferences getSharedPreferences(String string,
			int modePrivate) {
		// TODO Auto-generated method stub
		return null;
	}

	private void saveAlarmList() {
		Editor editor = (Editor) getContext().getSharedPreferences(
				AlarmView.class.getName(), Context.MODE_PRIVATE).edit();
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < adapter.getCount(); i++) {
			sb.append(adapter.getItem(i).getTime()).append(",");
		}

		if (sb.length() > 1) {
			String content = sb.toString().substring(0, sb.length() - 1);
			editor.putString(KEY_ALARM_LIST, content);
			System.out.println(content);
		} else {
			editor.putString(KEY_ALARM_LIST, null);
		}

		editor.commit();

	}

	private void readSaveAlarmList() {
		SharedPreferences sp = getContext().getSharedPreferences(
				AlarmView.class.getName(), Context.MODE_PRIVATE);
		String content = sp.getString(KEY_ALARM_LIST, null);

		if (content != null) {
			String[] timeStrings = content.split(",");
			for (String string : timeStrings) {
				adapter.add(new AlarmDate(Long.parseLong(string)));
			}

		}
	}

	private static final String KEY_ALARM_LIST = "alarmlist";
	private Button add;
	private ListView LvAlarmList;
	private ArrayAdapter<AlarmDate> adapter;
	private AlarmManager alarmManager;

	private static class AlarmDate {

		public AlarmDate(long time) {
			this.time = time;
			date = Calendar.getInstance();
			date.setTimeInMillis(time);
			if (date.get(Calendar.HOUR_OF_DAY) < 10
					& date.get(Calendar.MINUTE) < 10) {
				timeLabel = String.format("        %s:%s",
						"0" + date.get(Calendar.HOUR_OF_DAY),
						"0" + date.get(Calendar.MINUTE));

			} else {
				timeLabel = String.format("        %s:%s",

				date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE));

			}

			if (date.get(Calendar.HOUR_OF_DAY) < 10
					& date.get(Calendar.MINUTE) > 10) {
				timeLabel = String
						.format("        %s:%s",

						"0" + date.get(Calendar.HOUR_OF_DAY),
								date.get(Calendar.MINUTE));

			}
			if (date.get(Calendar.HOUR_OF_DAY) > 10
					& date.get(Calendar.MINUTE) < 10) {
				timeLabel = String
						.format("        %s:%s",

						date.get(Calendar.HOUR_OF_DAY),
								"0" + date.get(Calendar.MINUTE));

			}

		}

		public long getTime() {
			return time;
		}

		public String gettimeLabel() {
			return timeLabel;
		}

		@Override
		public String toString() {
			return gettimeLabel();
		}

		public int grtId() {
			return (int) (getTime() / 1000 / 60);
		}

		private String timeLabel = "" + "0";
		private long time = 0;
		private Calendar date;
	}

}
