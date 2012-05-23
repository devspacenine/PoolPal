package com.devspacenine.poolpal.widget;

import java.util.HashMap;
import java.util.LinkedList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devspacenine.poolpal.EditPoolActivity;
import com.devspacenine.poolpal.PoolPal;
import com.devspacenine.poolpal.R;
import com.devspacenine.poolpal.fragment.AddressInputDialogFragment;
import com.devspacenine.poolpal.fragment.ChoiceDialogFragment;
import com.devspacenine.poolpal.fragment.DatePickerDialogFragment;
import com.devspacenine.poolpal.fragment.InputDialogFragment;
import com.devspacenine.poolpal.fragment.TextInputDialogFragment;
import com.devspacenine.poolpal.object.Task;

public class TaskDataAdapter extends DataAdapter {

	// Request Codes
	public static final int SET_TITLE = R.id.task_title;
	public static final int SET_DATE = R.id.task_date;
	public static final int SET_REPITITION = R.id.task_repitition;
	public static final int SET_MESSAGE = R.id.task_message;
	public static final int SET_NEXT_ON_COMPLETION = R.id.task_next_on_completion;
	public static final int SET_REMINDERS = R.id.task_reminders;
	public static final int SET_NEW_REMINDER = R.id.task_new_reminder;
	public static final int SET_LATE_REMINDERS = R.id.task_late_reminders;
	public static final int SET_TIMER_DURATION = R.id.task_timer_duration;
	public static final int SET_TIMER_NOTIFICATION = R.id.task_timer_notification;
	public static final int SET_TIMER_MESSAGE = R.id.task_timer_message;

	// Value tags
	public static final String VALUE_TITLE = "title";
	public static final String VALUE_REPITITION = "repitition";
	public static final String VALUE_REPITITION_SCALE = "repitition_scale";
	public static final String VALUE_DATE = "date";
	public static final String VALUE_DATE_MIN = "date_min";
	public static final String VALUE_DATE_MAX = "date_max";
	public static final String VALUE_DATE_INTERVAL = "date_interval";
	public static final String VALUE_MESSAGE = "message";
	public static final String VALUE_NEXT_ON_COMPLETION = "next_on_completion";
	public static final String VALUE_REMINDERS = "reminders";
	public static final String VALUE_NEW_REMINDER = "new_reminder";
	public static final String VALUE_LATE_REMINDERS = "late_reminders";
	public static final String VALUE_TIMER_DURATION = "timer_duration";
	public static final String VALUE_TIMER_NOTIFICATION = "timer_notification";
	public static final String VALUE_TIMER_MESSAGE = "timer_message";

	private Task mTask;

	public TaskDataAdapter(FragmentActivity context, Task task, LinkedList<Bundle> data,
			int resource) {
		super(context, task.getPool(), data, resource);
		mTask = task;
	}

	public TaskDataAdapter(FragmentActivity context, Task task, LinkedList<Bundle> data,
			int resource, String fragmentTag) {
		super(context, task.getPool(), data, resource, fragmentTag);
		mTask = task;
	}

	protected void init() {
		// Construct a map of ids to possible settings layouts
		mLayouts = new HashMap<Integer, Integer>();
		mLayouts.put(SET_TITLE, R.layout.text_input);
		mLayouts.put(SET_DATE, R.layout.date_time_slider);
		mLayouts.put(SET_MESSAGE, R.layout.text_input);
		mLayouts.put(SET_REPITITION, R.layout.spinner_input);
		mLayouts.put(SET_REPITITION, R.layout.spinner_input);
		mLayouts.put(SET_FAVORITE, R.layout.spinner_input);
		mLayouts.put(SET_ADDRESS, R.layout.address_input);
	}

	public void setTask(Task task) {
		mTask = task;
	}

	public Task getTask() {
		return mTask;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// get data for this position
		Bundle data = (Bundle) getItem(position);
		int request_code = data.getInt(ITEM_REQUEST_CODE);
		String action = data.getString(ITEM_ACTION);
		String title = data.getString(ITEM_TITLE);
		String details = data.getString(ITEM_DETAILS);

		View v = convertView;
		LayoutInflater inflater = LayoutInflater.from(mCtx);
		if(v == null) {
			if(action.equals(ACTION_TOGGLE)) {
				v = inflater.inflate(R.layout.toggle_setting_item, parent, false);
			}else if(request_code == SET_REMINDER) {
				v = inflater.inflate(R.layout.edit_reminder, parent, false);
			}else{
				v = inflater.inflate(mLayoutResource, parent, false);
			}
		}

		v.setId(request_code);

		// get the views to bind data to
		ViewGroup buttonView = (ViewGroup) v.findViewById(R.id.button);
		TextView titleView = (TextView) buttonView.findViewById(R.id.title);
		TextView valueView = (TextView) buttonView.findViewById(R.id.value);

		// set the title of the setting if present
		if(titleView != null) titleView.setText(title);

		// set the value of the setting
		final Bundle values = mTask.getValuesBundle(mCtx, request_code);

		if(valueView instanceof CheckBox) {
			// this is a toggle setting, set the checked value and the details text
			((CheckBox)valueView).setChecked(values.getBoolean(VALUE));
			TextView detailsView = (TextView) buttonView.findViewById(R.id.details);
			detailsView.setText(details);
		}else{
			String value;
			if(action.equals(ACTION_OPEN) || action.equals(ACTION_ADD)) {
				// this is a link to another settings menu, set the value to null
				value = null;
			}else{
				// this is a text setting, set the text value
				value = (values.containsKey(VALUE)) ? values.getString(VALUE) : "";
			}
			if(value != null && !value.equals("")) {
				if(valueView != null) valueView.setText(value);
			}else{
				// this setting has not been set, set the value as a hint about the action
				if(valueView != null) valueView.setText(details);
			}
		}

		// set the button tags for this setting's action and target layout
		buttonView.setTag(R.id.key_title, title);
		buttonView.setTag(R.id.key_details, details);
		buttonView.setTag(R.id.key_action, action);
		buttonView.setTag(R.id.key_request_code, request_code);

		// set the onClickListener for this button
		buttonView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String _action = v.getTag(R.id.key_action).toString();
				String _title = (String) v.getTag(R.id.key_title);
				String _details = (String) v.getTag(R.id.key_details);
				int requestCode = (Integer) v.getTag(R.id.key_request_code);

				if(_action.equals(ACTION_EDIT) || _action.equals(ACTION_SET)) {
					// This item is an editable value. Create the arguments bundle
					// for the input fragment
					Bundle args = InputDialogFragment.createItem(requestCode,
							_title,
							_details,
							mLayouts.get(requestCode),
							values,
							mFragmentTag);

					InputDialogFragment frag;
					switch(requestCode) {
					case SET_ITEM:
					case SET_FAVORITE:
						frag = ChoiceDialogFragment.newInstance(args);
						break;

					case SET_ADDRESS:
						frag = AddressInputDialogFragment.newInstance(args);
						break;

					case SET_DATE:
						frag = DatePickerDialogFragment.newInstance(args);
						break;
					/*
					case SET_REPITITION:
						frag = RepititionInputDialogFragment.newInstance(args);
						break;

					case SET_TIMER_DURATION:
						frag = DurationInputDialogFragment.newInstance(args);
						break;
					*/
					default:
						frag = TextInputDialogFragment.newInstance(args);
						break;
					}

					FragmentTransaction ft = mCtx.getSupportFragmentManager().beginTransaction();
					ft.add(frag, PoolPal.INPUT_DIALOG);
					ft.commit();

				}else if(_action.equals(ACTION_OPEN)) {

					// This item opens another settings page
					Intent intent = new Intent(mCtx, EditPoolActivity.class);
					intent.putExtra(PoolPal.EXTRA_PAGE, mLayouts.get(requestCode));
					intent.putExtra(PoolPal.EXTRA_POOL, mPool);

					mCtx.startActivity(intent);

				}else if(_action.equals(ACTION_TOGGLE)) {

					// This list item toggles a value
					((CheckBox)v.findViewById(R.id.value)).toggle();
					((EditPoolActivity)mCtx).toggle(requestCode);
				}
			}
		});

		return v;
	}
}
