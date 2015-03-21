package com.wang.myalarmclock.test;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.wang.myalarmclock.db.AlarmDBHelper;
import com.wang.myalarmclock.utils.AlarmContract.Alarm;

public class Test extends AndroidTestCase {
	private AlarmDBHelper helper;
	private SQLiteDatabase db;

	public void create() {
		helper = new AlarmDBHelper(getContext());
		db = helper.getWritableDatabase();
		long result=db.insert(Alarm.TABLE_NAME, null, makeupContentValues());
		db.close();
		if(result>0){
			Log.i("--->>>", "-->>insert!");
		}
	}

	private ContentValues makeupContentValues() {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put(Alarm.COLUMN_ALARM_NAME, "haha");
		values.put(Alarm.COLUMN_ALARM_HOUR, 22);
		values.put(Alarm.COLUMN_ALARM_MUNUTE, 22);
		values.put(Alarm.COLUMN_ALARM_ENABLE, true);
		values.put(Alarm.COLUMN_ALARM_REPEAT_DAYS, "true,true,true,true,true,true,true");
		values.put(Alarm.COLUMN_ALARM_TONE, "I'll always...");
		return values;
	}
	public void deleteAll(){
		helper = new AlarmDBHelper(getContext());
		db = helper.getWritableDatabase();
		long result=db.delete(Alarm.TABLE_NAME, null, null);
		db.close();
		if(result>0){
			Log.i("--->>>", "-->>delete!");
		}
	}
}
