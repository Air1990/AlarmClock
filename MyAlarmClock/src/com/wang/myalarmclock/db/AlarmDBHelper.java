package com.wang.myalarmclock.db;

import com.wang.myalarmclock.utils.AlarmContract.Alarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmDBHelper extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 2;
	public static final String DATABSE_NAME = "alarmclock.db";

	public static final String DATABASE_CREATE_SQL = "CREATE TABLE "
			+ Alarm.TABLE_NAME + " (" + Alarm._ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Alarm.COLUMN_ALARM_NAME
			+ " TEXT," + Alarm.COLUMN_ALARM_HOUR + " INTEGER,"
			+ Alarm.COLUMN_ALARM_MUNUTE + " INTEGER,"
			+ Alarm.COLUMN_ALARM_REPEAT_DAYS + " TEXT,"
			+ Alarm.COLUMN_ALARM_TONE + " TEXT," + Alarm.COLUMN_ALARM_ENABLE
			+ " BOOLEAN" + ")";
	public static final String DATABASE_DELETE_SQL = "DROP TABLE IF EXISTS"
			+ Alarm.TABLE_NAME;

	public AlarmDBHelper(Context context) {
		super(context, DATABSE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DATABASE_CREATE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(DATABASE_DELETE_SQL);
		onCreate(db);
	}

}
