package com.wang.myalarmclock;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wang.myalarmclock.adapter.AlarmListAdapter;
import com.wang.myalarmclock.db.AlarmDBManager;
import com.wang.myalarmclock.model.AlarmModel;
import com.wang.myalarmclock.utils.AlarmSetClock;
import com.wang.myalarmclock.utils.AlarmUtils;

public class AlarmListActivity extends ListActivity {
	private AlarmDBManager dbManager;
	private AlarmListAdapter mAdapter;
	private RelativeLayout alarmAdd;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_list);
		context = this;
		dbManager = new AlarmDBManager(context);
		alarmAdd = (RelativeLayout) this.findViewById(R.id.alarm_add);
		mAdapter = new AlarmListAdapter(context, dbManager.getAlarms());
		setListAdapter(mAdapter);
		alarmAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startAlarmDetailsActivity(-1);
			}
		});
	}

	public void startAlarmDetailsActivity(long id) {
		Intent intent = new Intent(this, AlarmDetailsActivity.class);
		intent.putExtra("id", id);
		startActivityForResult(intent, 100);
		overridePendingTransition(R.anim.push_from_right_in,
				R.anim.push_from_right_out);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			mAdapter.setData(dbManager.getAlarms());
			mAdapter.notifyDataSetChanged();
			if (data.getIntExtra("day", -1) >=0) {
				AlarmUtils.showFirstRemindTime(context,
						data.getIntExtra("day", -1),
						data.getLongExtra("id", -1));
			}
		}
	}

	public void setAlarmEnable(long id, boolean isChecked) {
		AlarmModel alarm = dbManager.getAlarm(id);
		if (isChecked) {
			alarm.setEnable(isChecked);
			dbManager.updateAlarm(alarm);
			int day=AlarmSetClock.chooseTime(context, alarm);
			AlarmUtils.showFirstRemindTime(context, day, id);
		} else {
			alarm.setEnable(isChecked);
			dbManager.updateAlarm(alarm);
			AlarmSetClock.cancelAlarm(context, alarm);
			Toast.makeText(this, alarm.getName() + " 闹钟已取消", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void deleteAlarm(long id) {
		final long mId = id;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("删除闹钟").setMessage("确定要删除闹钟？");
		builder.setNegativeButton("取消", null);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				AlarmSetClock.cancelAlarm(context, dbManager.getAlarm(mId));
				dbManager.deleteAlarm(mId);
				mAdapter.setData(dbManager.getAlarms());
				mAdapter.notifyDataSetChanged();
				Toast.makeText(getApplicationContext(), "删除成功",
						Toast.LENGTH_SHORT).show();
			}
		});
		builder.create().show();
	}
}
