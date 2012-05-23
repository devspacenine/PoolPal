package com.devspacenine.poolpal.widget;

import java.util.HashMap;
import java.util.LinkedList;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.devspacenine.poolpal.EditPoolActivity;
import com.devspacenine.poolpal.PoolPal;
import com.devspacenine.poolpal.R;
import com.devspacenine.poolpal.fragment.AddressInputDialogFragment;
import com.devspacenine.poolpal.fragment.ChoiceDialogFragment;
import com.devspacenine.poolpal.fragment.InputDialogFragment;
import com.devspacenine.poolpal.fragment.SetDimensionsDialogFragment;
import com.devspacenine.poolpal.fragment.TaskSettingsFragment;
import com.devspacenine.poolpal.fragment.TextInputDialogFragment;
import com.devspacenine.poolpal.object.Pool;

public class PoolDataAdapter extends DataAdapter {

	// Request Codes
	public static final int SET_IMAGE = R.id.pool_image;
	public static final int SET_NAME = R.id.pool_name;
	public static final int SET_DIMENSIONS = R.id.pool_dimensions;
	public static final int SET_TRAFFIC = R.id.pool_traffic;
	public static final int SET_POOL_LOCALE = R.id.pool_locale;
	public static final int SET_FINISH = R.id.pool_finish;
	public static final int SET_SANITIZER = R.id.pool_sanitizer;
	public static final int SET_PUMP = R.id.pool_pump;
	public static final int SET_FILTER = R.id.pool_filter;
	public static final int SET_CLEANER = R.id.pool_cleaner;
	public static final int SET_GENERAL_MAINTENANCE_SCHEDULE = R.id.pool_general_maintenance;
	public static final int SET_WATER_TESTS_SCHEDULE = R.id.pool_water_tests;
	public static final int SET_FILTER_MAINTENANCE_SCHEDULE = R.id.pool_filter_maintenance;
	public static final int SET_CUSTOM_TASK_SETTINGS = R.id.pool_custom_tasks;
	public static final int SET_ACCESSORIES = R.id.pool_accessories;
	public static final int SET_TILING = R.id.pool_tiling;
	public static final int SET_WEATHER_NOTIFICATIONS = R.id.pool_weather_notifications;
	public static final int SET_WATER_TEST_REMINDERS = R.id.pool_water_test_reminders;
	public static final int SET_FILTER_REMINDERS = R.id.pool_filter_reminders;
	public static final int SET_SAFETY_NOTIFICATIONS = R.id.pool_safety_notifications;
	public static final int SET_MAINTENANCE_REMINDERS = R.id.pool_maintenance_reminders;
	public static final int SET_CUSTOM_NOTIFICATIONS = R.id.pool_custom_notifications;
	public static final int SET_COUPON_NOTIFICATIONS = R.id.pool_coupon_notifications;

	// Value tags
	public static final String VALUE_IMAGE = "image";
	public static final String VALUE_NAME = "name";
	public static final String VALUE_MIN_DEPTH = "min_depth";
	public static final String VALUE_MAX_DEPTH = "max_depth";
	public static final String VALUE_VOLUME = "volume";
	public static final String VALUE_PUMP_BRAND = "pump_brand";
	public static final String VALUE_PUMP_MODEL = "pump_model";
	public static final String VALUE_CLEANER_BRAND = "cleaner_brand";
	public static final String VALUE_CLEANER_MODEL = "cleaner_model";

	public PoolDataAdapter(FragmentActivity context, Pool pool, LinkedList<Bundle> data,
			int resource) {
		super(context, pool, data, resource);
	}

	public PoolDataAdapter(FragmentActivity context, Pool pool, LinkedList<Bundle> data,
			int resource, String fragmentTag) {
		super(context, pool, data, resource, fragmentTag);
	}

	protected void init() {
		// Construct a map of ids to possible settings layouts
		mLayouts = new HashMap<Integer, Integer>();
		mLayouts.put(SET_NAME, R.layout.text_input);
		mLayouts.put(SET_IMAGE, R.layout.options_input);
		mLayouts.put(SET_DIMENSIONS, R.layout.dimensions_input);
		mLayouts.put(SET_TRAFFIC, R.layout.spinner_input);
		mLayouts.put(SET_ADDRESS, R.layout.address_input);
		mLayouts.put(SET_POOL_LOCALE, R.layout.spinner_input);
		mLayouts.put(SET_FINISH, R.layout.spinner_input);
		mLayouts.put(SET_SANITIZER, R.layout.spinner_input);
		mLayouts.put(SET_FILTER, R.layout.spinner_input);
		mLayouts.put(SET_PUMP, R.layout.multivalue_input);
		mLayouts.put(SET_CLEANER, R.layout.multivalue_input);
		mLayouts.put(SET_GENERAL_MAINTENANCE_SCHEDULE, R.layout.edit_task_info);
		mLayouts.put(SET_WATER_TESTS_SCHEDULE, R.layout.edit_task_info);
		mLayouts.put(SET_FILTER_MAINTENANCE_SCHEDULE, R.layout.edit_task_info);
		mLayouts.put(SET_CUSTOM_TASK_SETTINGS, R.layout.edit_task_info);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		LayoutInflater inflater = LayoutInflater.from(mCtx);
		if(v == null) {
			v = inflater.inflate(mLayoutResource, parent, false);
		}

		// get data for this position
		Bundle data = (Bundle) getItem(position);
		String action = data.getString(ITEM_ACTION);
		int request_code = data.getInt(ITEM_REQUEST_CODE);

		v.setId(request_code);

		if(action.equals(ACTION_TOGGLE_SMALL)) {
			if(convertView == null) {
				// Build a table of toggle buttons
				TableLayout table = (TableLayout) v.findViewById(R.id.toggle_items);
				String[] options = data.getStringArray(ITEM_LABELS);
				int rows = (options.length / 2) + (options.length % 2);
				for(int i=0; i<rows; i++) {
					TableRow tr = (TableRow) inflater.inflate(R.layout.edit_settings_table_row,
							table, false);

					populateColumn(tr.findViewById(R.id.item1), options, 2*i);

					if(options.length > ((2*i)+1)) {
						populateColumn(tr.findViewById(R.id.item2), options, (2*i)+1);
					}

					// Add the row to the table
					table.addView(tr);
				}
			}
		}else{
			// get the views to bind data to
			RelativeLayout buttonView = (RelativeLayout) v.findViewById(R.id.button);
			TextView titleView = (TextView) buttonView.findViewById(R.id.title);
			TextView valueView = (TextView) buttonView.findViewById(R.id.value);

			// set the title of the setting
			titleView.setText(data.getString(ITEM_TITLE));

			// set the value of the setting
			final Bundle values = mPool.getValuesBundle(mCtx, request_code);

			if(valueView instanceof CheckBox) {
				// this is a toggle setting, set the checked value and the details text
				((CheckBox)valueView).setChecked(values.getBoolean(VALUE));
				TextView detailsView = (TextView) buttonView.findViewById(R.id.details);
				detailsView.setText(data.getString(ITEM_DETAILS));
			}else{
				String value;
				if(action.equals(ACTION_OPEN)) {
					// this is a link to another settings menu, set the value to null
					value = null;
				}else{
					// this is a text setting, set the text value
					value = (values.containsKey(VALUE)) ? values.getString(VALUE) : "";
				}
				if(value != null && !value.equals("")) {
					valueView.setText(value);
				}else{
					// this setting has not been set, set the value as a hint about the action
					valueView.setText(data.getString(ITEM_DETAILS));
				}
			}

			// set the button tags for this setting's action and target layout
			buttonView.setTag(R.id.key_title, data.getString(ITEM_TITLE));
			buttonView.setTag(R.id.key_details, data.getString(ITEM_DETAILS));
			buttonView.setTag(R.id.key_action, data.getString(ITEM_ACTION));
			buttonView.setTag(R.id.key_request_code, request_code);

			if(data.containsKey(ITEM_SET)) {
				buttonView.setTag(R.id.key_set, Boolean.toString(data.getBoolean(ITEM_SET, false)));
				if(data.getString(ITEM_ACTION).equals(ACTION_SET)) {
					ImageView set = (ImageView) v.findViewById(R.id.set);
					set.setVisibility(View.VISIBLE);
					set.setEnabled(data.getBoolean(ITEM_SET, false));
				}
			}

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
						case SET_DIMENSIONS:
							frag = SetDimensionsDialogFragment.newInstance(args);
							break;

						case SET_ADDRESS:
							frag = AddressInputDialogFragment.newInstance(args);
							break;

						case SET_TRAFFIC:
						case SET_POOL_LOCALE:
						case SET_FINISH:
						case SET_FILTER:
						case SET_SANITIZER:
							frag = ChoiceDialogFragment.newInstance(args);
							break;

						default:
							frag = TextInputDialogFragment.newInstance(args);
							break;
						}

						FragmentTransaction ft = mCtx.getSupportFragmentManager().beginTransaction();
						ft.add(frag, PoolPal.INPUT_DIALOG);
						ft.commit();

					}else if(_action.equals(ACTION_OPEN)) {
						// This item opens task settings. Create the arguments bundle
						// for the settings fragment
						Bundle args = TaskSettingsFragment.createItem(requestCode,
								mPool,
								mFragmentTag);
						((EditPoolActivity)mCtx).replaceTab(
								ProfileTabsAdapter.TAB_GENERAL_MAINTENANCE, TaskSettingsFragment.class, args);

					}else if(_action.equals(ACTION_TOGGLE)) {

						// This list item toggles a value
						((CheckBox)v.findViewById(R.id.value)).toggle();
						((EditPoolActivity)mCtx).toggle(requestCode);
					}
				}
			});
		}

		return v;
	}

	private void populateColumn(View col, String[] options, int pos) {

		Resources res = mCtx.getResources();

		// Setup the first column
		col.setId(pos);
		CheckBox value = (CheckBox) col.findViewWithTag(res.getString(R.string.value));
		TextView label = (TextView) col.findViewWithTag(res.getString(R.string.label));

		// Set the current value of this setting
		value.setChecked(mPool.getAccessories()[pos]);
		label.setText(options[pos]);

		// Set a click listener to update this setting
		col.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CheckBox val = (CheckBox) v.findViewWithTag(mCtx.getResources().getString(R.string.value));
				val.toggle();
				mPool = ((EditPoolActivity)mCtx).setAccessory(v.getId(), val.isChecked());
			}
		});
	}
}