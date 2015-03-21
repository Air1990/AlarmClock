package com.wang.myalarmclock.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.wang.myalarmclock.AlarmListActivity;
import com.wang.myalarmclock.R;
import com.wang.myalarmclock.model.AlarmModel;
import com.wang.myalarmclock.utils.AlarmUtils;


public class AlarmListAdapter extends BaseAdapter {

	private Context context;
	private List<AlarmModel> alarmList;

	public AlarmListAdapter(Context context, List<AlarmModel> alarms) {
		this.context = context;
		alarmList = alarms;
	}

	public void setData(List<AlarmModel> alarms) {
		alarmList = alarms;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (alarmList != null) {
			return alarmList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (alarmList != null) {
			return alarmList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		if (alarmList != null) {
			return alarmList.get(position).getId();
		}
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (view == null) {
			LayoutInflater inlfater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inlfater.inflate(R.layout.item_alarm_list, parent, false);
		}
		AlarmModel alarm = (AlarmModel) getItem(position);

		TextView time = (TextView) view.findViewById(R.id.time);
		TextView name = (TextView) view.findViewById(R.id.name);
		TextView repeat = (TextView) view.findViewById(R.id.repeat);
		CheckBox isEnable = (CheckBox) view.findViewById(R.id.isEnable);
		time.setText(String.format("%02d : %02d", alarm.getHour(),
				alarm.getMinute()));
		name.setText(alarm.getName());
		repeat.setText(AlarmUtils.getRepeatDays(alarm));
		isEnable.setChecked(alarm.isEnable());

		isEnable.setTag(Long.valueOf(alarm.getId()));
		isEnable.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				((AlarmListActivity) context).setAlarmEnable(
						((Long) buttonView.getTag()).longValue(), isChecked);
			}
		});
		view.setTag(Long.valueOf(alarm.getId()));
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((AlarmListActivity) context)
						.startAlarmDetailsActivity(((Long) v.getTag())
								.longValue());
			}
		});
		
		view.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				((AlarmListActivity) context).deleteAlarm(((Long) v.getTag())
						.longValue());
				return true;
			}
		});
		return view;
	}

}
