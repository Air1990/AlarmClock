package com.wang.myalarmclock.utils;

import java.util.Calendar;

import android.content.Context;
import android.widget.Toast;

import com.wang.myalarmclock.db.AlarmDBManager;
import com.wang.myalarmclock.model.AlarmModel;

public class AlarmUtils {
	/**
	 * when get alarm list,transform boolean[] to String
	 * 
	 * @param alarm
	 * @return
	 */
	public static String getRepeatDays(AlarmModel alarm) {
		StringBuilder repeat_days = new StringBuilder();
		if (alarm.getRepeating_day(AlarmModel.Mon)) {
			repeat_days.append("��һ,");
		}
		if (alarm.getRepeating_day(AlarmModel.Tues)) {
			repeat_days.append("�ܶ�,");
		}
		if (alarm.getRepeating_day(AlarmModel.Wed)) {
			repeat_days.append("����,");
		}
		if (alarm.getRepeating_day(AlarmModel.Thur)) {
			repeat_days.append("����,");
		}
		if (alarm.getRepeating_day(AlarmModel.Fri)) {
			repeat_days.append("����,");
		}
		if (alarm.getRepeating_day(AlarmModel.Sat)) {
			repeat_days.append("����, ");
		}
		if (alarm.getRepeating_day(AlarmModel.Sun)) {
			repeat_days.append("����,");
		}
		if (repeat_days.length() != 0)
			repeat_days.deleteCharAt(repeat_days.length() - 1);
		else
			return "���ظ�";
		return repeat_days.toString();
	}

	/**
	 * when save or update alarm,transform boolean[] to String
	 * 
	 * @param alarm
	 * @return
	 */
	public static String makeRepeatDays(AlarmModel alarm) {
		StringBuilder repeat_days = new StringBuilder();
		for (int i = 0; i < 7; i++) {
			repeat_days.append(String.valueOf(alarm.getRepeating_day(i)) + ",");
		}
		if (repeat_days.length() != 0)
			repeat_days.deleteCharAt(repeat_days.length() - 1);
		return repeat_days.toString();
	}

	public static void showFirstRemindTime(Context context, int day, long id) {
		AlarmDBManager manager = new AlarmDBManager(context);
		AlarmModel model = manager.getAlarm(id);
		int setHour = model.getHour();
		int setMin = model.getMinute();

		long remindDay = day;
		long remindHour = 0, remindMin = 0;
		int nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int nowMin = Calendar.getInstance().get(Calendar.MINUTE);
		String toast = "";

		long allMin = setHour * 60 + setMin - nowHour * 60 - nowMin;
		if (allMin >= 0) {
			remindHour = allMin / 60;
			remindMin = allMin % 60;
		} else {
			remindDay--;
			remindHour = (24 * 60 + allMin) / 60;
			remindMin = (24 * 60 + allMin) % 60;
		}
		if (remindDay > 0)
			toast = remindDay + "�� " + remindHour + "Сʱ " + remindMin + "���Ӻ�����";
		else
			toast = remindHour + "Сʱ " + remindMin + "���Ӻ�����";
		Toast.makeText(context, toast, Toast.LENGTH_LONG).show();
	}
}
