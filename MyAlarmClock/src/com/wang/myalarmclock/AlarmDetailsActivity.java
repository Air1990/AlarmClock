package com.wang.myalarmclock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.wang.myalarmclock.db.AlarmDBManager;
import com.wang.myalarmclock.model.AlarmModel;
import com.wang.myalarmclock.utils.AlarmSetClock;
import com.wang.myalarmclock.utils.AlarmUtils;

public class AlarmDetailsActivity extends Activity implements OnClickListener {
	private AlarmDBManager dbManager;
	private AlarmModel alarmModel;

	private TimePicker timePicker;
	private EditText name;
	private LinearLayout setRepeat;
	private TextView repeat;
	private LinearLayout setTone;
	private TextView tone;
	private FrameLayout cancel;
	private RelativeLayout save;

	private long id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_details);
		dbManager = new AlarmDBManager(this);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		timePicker = (TimePicker) this.findViewById(R.id.timePicker);
		name = (EditText) this.findViewById(R.id.name);
		setRepeat = (LinearLayout) this.findViewById(R.id.set_repeat);
		repeat = (TextView) this.findViewById(R.id.repeat);
		setTone = (LinearLayout) this.findViewById(R.id.set_tone);
		tone = (TextView) this.findViewById(R.id.tone);
		cancel = (FrameLayout) this.findViewById(R.id.alarm_cancel);
		save = (RelativeLayout) this.findViewById(R.id.alarm_save);

		setRepeat.setOnClickListener(this);
		setTone.setOnClickListener(this);
		cancel.setOnClickListener(this);
		save.setOnClickListener(this);

		Intent intent = getIntent();
		id = intent.getExtras().getLong("id");
		if (id == -1) {
			alarmModel = new AlarmModel();
		} else {
			alarmModel = dbManager.getAlarm(id);
			timePicker.setCurrentHour(alarmModel.getHour());
			timePicker.setCurrentMinute(alarmModel.getMinute());
			name.setText(alarmModel.getName());
			repeat.setText(AlarmUtils.getRepeatDays(alarmModel));
			if (alarmModel.getTone() != null) {
				tone.setText(RingtoneManager.getRingtone(this,
						Uri.parse(alarmModel.getTone())).getTitle(this));
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.set_repeat:
			showRepeatSlelectDialog();
			break;
		case R.id.set_tone:
			showToneSelectDialog();
			break;
		case R.id.alarm_cancel:
			finish();
			overridePendingTransition(R.anim.push_from_left_in,
					R.anim.push_from_left_out);
			break;
		case R.id.alarm_save:
			save();
			break;
		}
	}

	/**
	 * show the choose tone dialog
	 */
	private void showToneSelectDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("—°‘Ò¡Â…˘");
		builder.setItems(new String[] { "¡Â…˘", "“Ù¿÷ªÚ¬º“Ù" },
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent;
						if (which == 0) {
							intent = new Intent(
									RingtoneManager.ACTION_RINGTONE_PICKER);
							startActivityForResult(intent, 200);
						} else {
							intent = new Intent(Intent.ACTION_GET_CONTENT);
							intent.setType("audio/*");
							startActivityForResult(
									Intent.createChooser(intent, "—°‘Ò¡Â…˘"), 300);
						}
					}
				});
		builder.create().show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Uri uri = null;
		if (requestCode == 200 && resultCode == RESULT_OK) {
			uri = data
					.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

		} else if (requestCode == 300 && resultCode == RESULT_OK) {
			uri = data.getData();
		} else
			return;
		alarmModel.setTone(uri.toString());

		tone.setText(RingtoneManager.getRingtone(this,
				Uri.parse(alarmModel.getTone())).getTitle(this));
	}

	/**
	 * show the choose repeat day dialog
	 */
	private void showRepeatSlelectDialog() {
		// TODO Auto-generated method stub
		final boolean repeat_days[] = new boolean[7];
		if (id != -1) {
			for (int i = 0; i < 7; i++) {
				repeat_days[i] = alarmModel.getRepeating_day(i);
			}
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("«Î—°‘Ò");
		builder.setMultiChoiceItems(R.array.repeat_days,
				alarmModel.getRepeating_days(),
				new OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						// TODO Auto-generated method stub
						repeat_days[which] = isChecked;
					}
				});
		builder.setNegativeButton("»°œ˚", null);
		builder.setPositiveButton("»∑∂®", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				alarmModel.setRepeating_days(repeat_days);
				repeat.setText(AlarmUtils.getRepeatDays(alarmModel));
			}
		});
		builder.create().show();
	}

	/**
	 * save clock
	 */
	private void save() {
		alarmModel.setHour(timePicker.getCurrentHour().intValue());
		alarmModel.setMinute(timePicker.getCurrentMinute().intValue());
		alarmModel.setName(name.getText().toString());

		int day = -1;
		if (id == -1) {
			alarmModel.setEnable(true);
			long result=dbManager.createAlarm(alarmModel);
			alarmModel.setId(result);
			day = AlarmSetClock.chooseTime(this, alarmModel);
		} else {
			dbManager.updateAlarm(alarmModel);
			if (alarmModel.isEnable()) {
				AlarmSetClock.cancelAlarm(this, alarmModel);
				day = AlarmSetClock.chooseTime(this, alarmModel);
			}
		}
		Intent intent = getIntent();
		intent.putExtra("day", day);
		intent.putExtra("id", alarmModel.getId());
		setResult(RESULT_OK, intent);
		finish();
		overridePendingTransition(R.anim.push_from_right_in,
				R.anim.push_from_right_out);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.push_from_left_in,
				R.anim.push_from_left_out);
		finish();
	}
}
