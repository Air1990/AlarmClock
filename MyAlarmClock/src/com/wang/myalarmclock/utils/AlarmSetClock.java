package com.wang.myalarmclock.utils;

import java.util.Calendar;
import java.util.List;

import com.wang.myalarmclock.AlarmService;
import com.wang.myalarmclock.db.AlarmDBManager;
import com.wang.myalarmclock.model.AlarmModel;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmSetClock extends BroadcastReceiver {
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String TIME_HOUR = "timeHour";
	public static final String TIME_MINUTE = "timeMinute";
	public static final String TONE = "alarmTone";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		setAlarms(context);
	}

	/**
	 * set all alarms
	 * 
	 * @param context
	 */
	public static void setAlarms(Context context) {
		cancelAlarms(context);

		AlarmDBManager manager = new AlarmDBManager(context);

		List<AlarmModel> alarms = manager.getAlarms();
		if (alarms != null) {
			for (AlarmModel alarm : alarms) {
				chooseTime(context, alarm);
			}
		}
	}

	/**
	 * choose when to set alarm
	 * 
	 * @param alarm
	 * @param context
	 */
	public static int chooseTime(Context context, AlarmModel alarm) {
		int firstRemindDay = -1;
		if (alarm.isEnable()) {

			PendingIntent pIntent = createPendingIntent(context, alarm);

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
			calendar.set(Calendar.MINUTE, alarm.getMinute());
			calendar.set(Calendar.SECOND, 00);

			// Find next time to set
			final int nowDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
			final int nowHour = Calendar.getInstance()
					.get(Calendar.HOUR_OF_DAY);
			final int nowMinute = Calendar.getInstance().get(Calendar.MINUTE);
			boolean alarmSet = false;

			// First check if it's later in the week
			for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; ++dayOfWeek) {
				if (alarm.getRepeating_day(dayOfWeek - 1)
						&& dayOfWeek >= nowDay
						&& !(dayOfWeek == nowDay && alarm.getHour() < nowHour)
						&& !(dayOfWeek == nowDay && alarm.getHour() == nowHour && alarm
								.getMinute() <= nowMinute)) {
					calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
					if (!alarmSet) {
						firstRemindDay = dayOfWeek - nowDay;
					}
					setAlarm(context, calendar, pIntent);
					alarmSet = true;
					break;
				}
			}

			// Else check if it's earlier in the week
			if (!alarmSet) {
				for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; ++dayOfWeek) {
					if (alarm.getRepeating_day(dayOfWeek - 1)
							&& dayOfWeek <= nowDay) {
						calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
						calendar.add(Calendar.WEEK_OF_YEAR, 1);
						if (!alarmSet) {
							firstRemindDay = 7 - nowDay + dayOfWeek;
						}
						setAlarm(context, calendar, pIntent);
						alarmSet = true;
						break;
					}
				}
			}
			// if user have no choose the repeat
			if (!alarmSet) {
				if (alarm.getHour() > nowHour
						|| (alarm.getHour() == nowHour && alarm.getMinute() > nowMinute)) {
					calendar.set(Calendar.DAY_OF_WEEK, nowDay);
					setAlarm(context, calendar, pIntent);
					alarmSet = true;
					firstRemindDay = 0;
				} else {
					calendar.set(Calendar.DAY_OF_WEEK, (nowDay + 1) % 7);
					if ((nowDay + 1) > 7) {
						calendar.add(Calendar.WEEK_OF_YEAR, 1);
					}
					setAlarm(context, calendar, pIntent);
					alarmSet = true;
					firstRemindDay = 1;
				}
			}
		}
		return firstRemindDay;
	}

	/**
	 * set alarm
	 * 
	 * @param context
	 * @param calendar
	 * @param pi
	 */
	@SuppressLint("NewApi")
	private static void setAlarm(Context context, Calendar calendar,
			PendingIntent pi) {
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			alarmManager.setExact(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), pi);
		} else {
			alarmManager.set(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), pi);
		}
	}

	/**
	 * cancel all alarms
	 * 
	 * @param context
	 */
	public static void cancelAlarms(Context context) {
		AlarmDBManager manager = new AlarmDBManager(context);

		List<AlarmModel> alarms = manager.getAlarms();

		if (alarms != null) {
			for (AlarmModel alarm : alarms) {
				cancelAlarm(context, alarm);
			}
		}
	}

	/**
	 * cancel a alarm
	 * 
	 * @param context
	 * @param alarm
	 */
	public static void cancelAlarm(Context context, AlarmModel alarm) {
		if (alarm.isEnable()) {
			PendingIntent pIntent = createPendingIntent(context, alarm);

			AlarmManager alarmManager = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(pIntent);
		}

	}

	private static PendingIntent createPendingIntent(Context context,
			AlarmModel model) {
		Intent intent = new Intent(context, AlarmService.class);
		intent.putExtra(ID, model.getId());
		intent.putExtra(NAME, model.getName());
		intent.putExtra(TIME_HOUR, model.getHour());
		intent.putExtra(TIME_MINUTE, model.getMinute());
		intent.putExtra(TONE, model.getTone());

		return PendingIntent.getService(context, (int) model.getId(), intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}

}
