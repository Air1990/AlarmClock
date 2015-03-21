package com.wang.myalarmclock.model;

public class AlarmModel {
	public static final int Sun = 0;
	public static final int Mon = 1;
	public static final int Tues = 2;
	public static final int Wed = 3;
	public static final int Thur = 4;
	public static final int Fri = 5;
	public static final int Sat = 6;

	private long id;
	private String name;
	private int hour;
	private int minute;
	private boolean repeating_days[];
	private String tone;
	private boolean enable;

	public AlarmModel() {
		repeating_days = new boolean[7];
	}

	public boolean[] getRepeating_days() {
		return repeating_days;
	}

	public void setRepeating_days(boolean[] repeat) {
		repeating_days = repeat;
	}

	public boolean getRepeating_day(int day) {
		return repeating_days[day];
	}

	public void setRepeating_day(int day, boolean value) {
		repeating_days[day] = value;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public String getTone() {
		return tone;
	}

	public void setTone(String tone) {
		this.tone = tone;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

}
