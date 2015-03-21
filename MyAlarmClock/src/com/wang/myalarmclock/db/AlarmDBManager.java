package com.wang.myalarmclock.db;

import java.util.ArrayList;
import java.util.List;

import com.wang.myalarmclock.model.AlarmModel;
import com.wang.myalarmclock.utils.AlarmUtils;
import com.wang.myalarmclock.utils.AlarmContract.Alarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AlarmDBManager {
	private AlarmDBHelper helper;
	private SQLiteDatabase db;

	public AlarmDBManager(Context context) {
		helper = new AlarmDBHelper(context);
	}

	/**
	 * make up every AlarmModle from c
	 * 
	 * @param c
	 * @return
	 */
	private AlarmModel populateAlarmModle(Cursor c) {
		AlarmModel alarm = new AlarmModel();
		alarm.setId(c.getLong(c.getColumnIndex(Alarm._ID)));
		alarm.setName(c.getString(c.getColumnIndex(Alarm.COLUMN_ALARM_NAME)));
		alarm.setHour(c.getInt(c.getColumnIndex(Alarm.COLUMN_ALARM_HOUR)));
		alarm.setMinute(c.getInt(c.getColumnIndex(Alarm.COLUMN_ALARM_MUNUTE)));
		alarm.setEnable(c.getInt(c.getColumnIndex(Alarm.COLUMN_ALARM_ENABLE)) == 0 ? false
				: true);
		alarm.setTone(c.getString(c.getColumnIndex(Alarm.COLUMN_ALARM_TONE)));

		String[] repeat_days = c.getString(
				c.getColumnIndex(Alarm.COLUMN_ALARM_REPEAT_DAYS)).split(",");
		for (int i = 0; i < 7; i++) {
			alarm.setRepeating_day(i, repeat_days[i].equals("false") ? false
					: true);
		}
		return alarm;
	}

	/**
	 * make up contentValues from alarm
	 * 
	 * @param alarm
	 * @return
	 */
	private ContentValues populateContentValues(AlarmModel alarm) {
		ContentValues value = new ContentValues();
		value.put(Alarm.COLUMN_ALARM_NAME, alarm.getName());
		value.put(Alarm.COLUMN_ALARM_HOUR, alarm.getHour());
		value.put(Alarm.COLUMN_ALARM_MUNUTE, alarm.getMinute());
		value.put(Alarm.COLUMN_ALARM_ENABLE, alarm.isEnable());
		value.put(Alarm.COLUMN_ALARM_TONE, alarm.getTone());
		value.put(Alarm.COLUMN_ALARM_REPEAT_DAYS,
				AlarmUtils.makeRepeatDays(alarm));
		return value;
	}

	/**
	 * get a AlarmModle according id
	 * 
	 * @param id
	 * @return
	 */
	public AlarmModel getAlarm(long id) {
		db = helper.getReadableDatabase();
		Cursor c = db.query(Alarm.TABLE_NAME, null, Alarm._ID + "=?",
				new String[] { id + "" }, null, null, null);
		if (c.moveToNext()) {
			AlarmModel alarm = populateAlarmModle(c);
			db.close();
			return alarm;
		}
		return null;
	}

	/**
	 * get all AlarmModles
	 * 
	 * @return
	 */
	public List<AlarmModel> getAlarms() {
		List<AlarmModel> alarmList = new ArrayList<AlarmModel>();
		db = helper.getReadableDatabase();
		Cursor c = db.query(Alarm.TABLE_NAME, null, null, null, null, null,
				null);
		while (c.moveToNext()) {
			alarmList.add(populateAlarmModle(c));
		}
		db.close();
		if (!alarmList.isEmpty()) {
			return alarmList;
		}
		return null;
	}

	/**
	 * insert a new record
	 * 
	 * @param alarm
	 * @return
	 */
	public long createAlarm(AlarmModel alarm) {
		db = helper.getWritableDatabase();
		long result = db.insert(Alarm.TABLE_NAME, null,
				populateContentValues(alarm));
		db.close();
		return result;
	}

	/**
	 * update a record
	 * 
	 * @param alarm
	 * @return
	 */
	public int updateAlarm(AlarmModel alarm) {
		db = helper.getWritableDatabase();
		int result = db.update(Alarm.TABLE_NAME, populateContentValues(alarm),
				Alarm._ID + "=?",
				new String[] { String.valueOf(alarm.getId()) });
		db.close();
		return result;
	}

	/**
	 * delete a record
	 * 
	 * @param id
	 * @return
	 */
	public int deleteAlarm(long id) {
		db = helper.getWritableDatabase();
		int result = db.delete(Alarm.TABLE_NAME, Alarm._ID + "=?",
				new String[] { String.valueOf(id) });
		db.close();
		return result;
	}
}
