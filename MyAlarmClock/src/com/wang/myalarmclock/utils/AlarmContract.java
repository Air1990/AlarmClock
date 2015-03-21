package com.wang.myalarmclock.utils;

import android.provider.BaseColumns;

public final class AlarmContract {
	public AlarmContract() {
	}

	public static class Alarm implements BaseColumns {
		public static final String TABLE_NAME = "alarm";
		public static final String COLUMN_ALARM_NAME = "name";
		public static final String COLUMN_ALARM_HOUR = "hour";
		public static final String COLUMN_ALARM_MUNUTE = "minute";
		public static final String COLUMN_ALARM_REPEAT_DAYS = "days";
		public static final String COLUMN_ALARM_TONE = "tone";
		public static final String COLUMN_ALARM_ENABLE = "enable";
	}
}
