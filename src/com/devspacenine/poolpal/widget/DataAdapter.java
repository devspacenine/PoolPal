package com.devspacenine.poolpal.widget;

import java.util.HashMap;
import java.util.LinkedList;

import com.devspacenine.poolpal.R;
import com.devspacenine.poolpal.object.Pool;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class DataAdapter extends BaseAdapter {

	// Action tags
	public static final String ACTION_SET = "action_set";
	public static final String ACTION_EDIT = "action_edit";
	public static final String ACTION_OPEN = "action_open";
	public static final String ACTION_CHOOSE = "action_choose";
	public static final String ACTION_TOGGLE = "action_toggle";
	public static final String ACTION_TOGGLE_SMALL = "action_toggle_small";
	public static final String ACTION_ADD = "action_add";
	public static final String ACTION_NONE = "action_none";

	// Request codes
	public static final int SET_POOL = R.id.set_pool;
	public static final int SET_ADDRESS = R.id.set_address;
    public static final int SET_TASK = R.id.set_task;
    public static final int SET_TIMER = R.id.set_timer;
    public static final int SET_ITEM = R.id.set_item;
    public static final int SET_FAVORITE = R.id.set_favorite;
    public static final int SET_ORDER_ITEM = R.id.set_order_item;
    public static final int SET_REMINDER = R.id.set_reminder;
    public static final int SET_TREATMENT = R.id.set_treatment;
    public static final int SET_WATER_TEST = R.id.set_water_test;

	// data map keys
	public static final String ITEM_TITLE = "title";
	public static final String ITEM_LABELS = "labels";
	public static final String ITEM_DETAILS = "details";
	public static final String ITEM_ACTION = "action";
	public static final String ITEM_REQUEST_CODE = "request_code";
	public static final String ITEM_SET = "set";

	// Value tags
	public static final String VALUE = "value";
	public static final String VALUE_CHOICE = "choice";
	public static final String VALUE_SET = "set";
	public static final String VALUE_ADDRESS = "address";
	public static final String VALUE_GEOCODED = "geocoded";
	public static final String VALUE_POOL = "pool";
	public static final String VALUE_TASK = "task";
	public static final String VALUE_TIMER = "timer";
	public static final String VALUE_ITEM = "item";
	public static final String VALUE_FAVORITE = "favorite";
	public static final String VALUE_ORDER_ITEM = "order_item";
	public static final String VALUE_REMINDER = "reminder";
	public static final String VALUE_TREATMENT = "treatment";
	public static final String VALUE_WATER_TEST = "water_test";

	public static Bundle createItem(String[] labels, String action, int requestCode) {

		Bundle item = new Bundle();
		item.putStringArray(ITEM_LABELS, labels);
		item.putString(ITEM_ACTION, action);
		item.putInt(ITEM_REQUEST_CODE, requestCode);
		item.putString(ITEM_TITLE, "");
		item.putString(ITEM_DETAILS, "");
		return item;
	}

	public static Bundle createItem(String title, String details,
			String action, int requestCode, boolean set) {

		Bundle item = new Bundle();
		item.putString(ITEM_TITLE, title);
		item.putString(ITEM_DETAILS, details);
		item.putString(ITEM_ACTION, action);
		item.putInt(ITEM_REQUEST_CODE, requestCode);
		item.putBoolean(ITEM_SET, set);
		return item;
	}

	public static Bundle createItem(String title, String details,
			String action, int requestCode) {

		Bundle item = new Bundle();
		item.putString(ITEM_TITLE, title);
		item.putString(ITEM_DETAILS, details);
		item.putString(ITEM_ACTION, action);
		item.putInt(ITEM_REQUEST_CODE, requestCode);
		return item;
	}

	protected LinkedList<Bundle> mData;
	protected FragmentActivity mCtx;
	protected int mLayoutResource;
	protected HashMap<Integer, Integer> mLayouts;
	protected Pool mPool;
	protected String mFragmentTag;

	public DataAdapter(FragmentActivity context, Pool pool, LinkedList<Bundle> data,
			int resource) {

		mData = data;
		mCtx = context;
		mLayoutResource = resource;
		mPool = pool;
		mFragmentTag = null;
		init();
	}

	public DataAdapter(FragmentActivity context, Pool pool, LinkedList<Bundle> data,
			int resource, String fragmentTag) {

		mData = data;
		mCtx = context;
		mLayoutResource = resource;
		mPool = pool;
		mFragmentTag = fragmentTag;
		init();
	}

	protected abstract void init();

	public void setPool(Pool pool) {
		mPool = pool;
	}

	public Pool getPool() {
		return mPool;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mData.get(position).getInt(ITEM_REQUEST_CODE);
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);
}
