package com.devspacenine.poolpal.fragment;

import java.util.LinkedList;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.devspacenine.poolpal.OnDecisionListener;
import com.devspacenine.poolpal.PoolPal;
import com.devspacenine.poolpal.R;
import com.devspacenine.poolpal.contentprovider.PoolPalContent;
import com.devspacenine.poolpal.database.PoolTable;
import com.devspacenine.poolpal.object.Pool;
import com.devspacenine.poolpal.object.PoolAddress;
import com.devspacenine.poolpal.widget.PoolDataAdapter;
import com.devspacenine.poolpal.widget.SettingsSectionAdapter;

public class PoolInfoFragment extends SherlockFragment implements
	LoaderManager.LoaderCallbacks<Cursor>, OnDecisionListener {

	// Fragment tags
	public static final String CONTINUE_EDITING_FRAGMENT = "continue_editing";

	private Pool mPool;
	private ListView mListView;
	private SharedPreferences mPoolPreferences;
	private SettingsSectionAdapter mAdapter;
	private boolean mPopulated;
	private int mRefreshIndex;
	private int mRefreshTop;
	private FragmentActivity mCtx;

	static PoolInfoFragment newInstance(int layout) {
		PoolInfoFragment frag = new PoolInfoFragment();

		Bundle args = new Bundle();
		args.putInt(PoolPal.EXTRA_PAGE, layout);
		frag.setArguments(args);

		return frag;
	}

	@Override
	public void onCreate(Bundle savedState) {

		super.onCreate(savedState);
		mPopulated = false;
		mRefreshIndex = 0;
		mRefreshTop = 0;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mPopulated = false;
	}

	@Override
	public void onActivityCreated(Bundle savedState) {

		super.onActivityCreated(savedState);
		mCtx = getSherlockActivity();
		mPoolPreferences = mCtx.getSharedPreferences(PoolPal.PREFS_POOL, 0);
		getLoaderManager().initLoader(PoolPal.POOL_LOADER, null, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedState) {

		View v = inflater.inflate(R.layout.edit_pool_info, container, false);
		mListView = (ListView) v;
		return v;
	}

	public void populate() {

		Resources res = getResources();

		if(!mPopulated) {
			LinkedList<Bundle> profile_data = new LinkedList<Bundle>();
			LinkedList<Bundle> location_data = new LinkedList<Bundle>();
			LinkedList<Bundle> hardware_data = new LinkedList<Bundle>();
			LinkedList<Bundle> accessories_data = new LinkedList<Bundle>();

			profile_data.add(PoolDataAdapter.createItem(
					res.getString(R.string.lbl_name),
					res.getString(R.string.details_name),
					PoolDataAdapter.ACTION_EDIT,
					PoolDataAdapter.SET_NAME,
					mPool.hasName()));
			profile_data.add(PoolDataAdapter.createItem(
					res.getString(R.string.lbl_dimensions),
					res.getString(R.string.details_dimensions),
					PoolDataAdapter.ACTION_EDIT,
					PoolDataAdapter.SET_DIMENSIONS,
					mPool.hasDimensions()));
			profile_data.add(PoolDataAdapter.createItem(
					res.getString(R.string.lbl_traffic),
					res.getString(R.string.details_traffic),
					PoolDataAdapter.ACTION_EDIT,
					PoolDataAdapter.SET_TRAFFIC,
					mPool.hasTraffic()));

			location_data.add(PoolDataAdapter.createItem(
					res.getString(R.string.lbl_address),
					res.getString(R.string.details_address),
					PoolDataAdapter.ACTION_EDIT,
					PoolDataAdapter.SET_ADDRESS,
					mPool.hasAddress()));
			location_data.add(PoolDataAdapter.createItem(
					res.getString(R.string.lbl_pool_locale),
					res.getString(R.string.details_pool_locale),
					PoolDataAdapter.ACTION_EDIT,
					PoolDataAdapter.SET_POOL_LOCALE,
					mPool.hasPoolLocale()));

			hardware_data.add(PoolDataAdapter.createItem(
					res.getString(R.string.lbl_pool_finish),
					res.getString(R.string.details_finish),
					PoolDataAdapter.ACTION_EDIT,
					PoolDataAdapter.SET_FINISH,
					mPool.hasFinish()));
			hardware_data.add(PoolDataAdapter.createItem(
					res.getString(R.string.lbl_sanitizer),
					res.getString(R.string.details_sanitizer),
					PoolDataAdapter.ACTION_EDIT,
					PoolDataAdapter.SET_SANITIZER,
					mPool.hasSanitizer()));
			hardware_data.add(PoolDataAdapter.createItem(
					res.getString(R.string.lbl_filter),
					res.getString(R.string.details_filter),
					PoolDataAdapter.ACTION_EDIT,
					PoolDataAdapter.SET_FILTER,
					mPool.hasFilter()));
			hardware_data.add(PoolDataAdapter.createItem(
					res.getString(R.string.lbl_pump),
					res.getString(R.string.details_pump),
					PoolDataAdapter.ACTION_EDIT,
					PoolDataAdapter.SET_PUMP,
					mPool.hasPump()));
			hardware_data.add(PoolDataAdapter.createItem(
					res.getString(R.string.lbl_cleaner),
					res.getString(R.string.details_cleaner),
					PoolDataAdapter.ACTION_EDIT,
					PoolDataAdapter.SET_CLEANER,
					mPool.hasCleaner()));

			accessories_data.add(PoolDataAdapter.createItem(
					res.getStringArray(R.array.pool_accessory_labels),
					PoolDataAdapter.ACTION_TOGGLE_SMALL,
					PoolDataAdapter.SET_ACCESSORIES));

			mAdapter = new SettingsSectionAdapter(mCtx, mPool);

			mAdapter.addSection(res.getString(R.string.title_info),
					new PoolDataAdapter(mCtx, mPool, profile_data, R.layout.edit_settings_item,
							getTag()));

			mAdapter.addSection(res.getString(R.string.title_location),
					new PoolDataAdapter(mCtx, mPool, location_data, R.layout.edit_settings_item,
							getTag()));

			mAdapter.addSection(res.getString(R.string.title_hardware),
					new PoolDataAdapter(mCtx, mPool, hardware_data, R.layout.edit_settings_item,
							getTag()));

			mAdapter.addSection(res.getString(R.string.title_accessories),
					new PoolDataAdapter(mCtx, mPool, accessories_data, R.layout.edit_settings_table,
							getTag()));

			mListView.setAdapter(mAdapter);
			mPopulated = true;

			if(mRefreshIndex > 0 || mRefreshTop > 0) {
				mListView.setSelectionFromTop(mRefreshIndex, mRefreshTop);
				mRefreshIndex = 0;
				mRefreshTop = 0;
			}
		}
	}

	@Override
	public void onPositiveDecision(int requestCode, Bundle values) {

		String val = values.getString(PoolDataAdapter.VALUE);
		boolean address_updated = false;
		boolean updated = false;
		switch(requestCode) {
		case PoolDataAdapter.SET_NAME:
			String name = values.getString(PoolDataAdapter.VALUE_NAME);

			if(!val.equals("") && !mPool.getName().equals(name)) {
				mPool.setName(name);
				updated = true;
			}
			break;

		case PoolDataAdapter.SET_DIMENSIONS:
			double min = values.getDouble(PoolDataAdapter.VALUE_MIN_DEPTH);
			double max = values.getDouble(PoolDataAdapter.VALUE_MAX_DEPTH);
			double volume = values.getDouble(PoolDataAdapter.VALUE_VOLUME);


			if(!val.equals("") && !(mPool.getMinDepth() == min
					&& mPool.getMaxDepth() == max && mPool.getVolume() == volume)) {
				mPool.setMinDepth(min);
				mPool.setMaxDepth(max);
				mPool.setVolume(volume);
				updated = true;
			}
			break;

		case PoolDataAdapter.SET_TRAFFIC:
			String traffic = values.getString(PoolDataAdapter.VALUE_CHOICE);

			if(!val.equals("") && !mPool.getTraffic().equals(traffic)) {
				mPool.setTraffic(traffic);
				updated = true;
			}
			break;

		case PoolDataAdapter.SET_POOL_LOCALE:
			String locale = values.getString(PoolDataAdapter.VALUE_CHOICE);

			if(!val.equals("") && !mPool.getPoolLocale().equals(locale)) {
				mPool.setPoolLocale(locale);
				updated = true;
			}
			break;

		case PoolDataAdapter.SET_FINISH:
			String finish = values.getString(PoolDataAdapter.VALUE_CHOICE);

			if(!val.equals("") && !mPool.getFinish().equals(finish)) {
				mPool.setFinish(finish);
				updated = true;
			}
			break;

		case PoolDataAdapter.SET_SANITIZER:
			String sanitizer = values.getString(PoolDataAdapter.VALUE_CHOICE);

			if(!val.equals("") && !mPool.getSanitizer().equals(sanitizer)) {
				mPool.setSanitizer(sanitizer);
				updated = true;
			}
			break;

		case PoolDataAdapter.SET_FILTER:
			String filter = values.getString(PoolDataAdapter.VALUE_CHOICE);

			if(!val.equals("") && !mPool.getFilter().equals(filter)) {
				mPool.setFilter(filter);
				updated = true;
			}
			break;

		case PoolDataAdapter.SET_PUMP:
			String pumpBrand = values.getString(PoolDataAdapter.VALUE_PUMP_BRAND);
			String pumpModel = values.getString(PoolDataAdapter.VALUE_PUMP_MODEL);

			if(!val.equals("") && !(mPool.getPumpBrand().equals(pumpBrand)
					&& mPool.getPumpModel().equals(pumpModel))) {
				mPool.setPumpBrand(pumpBrand);
				mPool.setPumpModel(pumpModel);
				updated = true;
			}
			break;

		case PoolDataAdapter.SET_CLEANER:
			String cleanerBrand = values.getString(PoolDataAdapter.VALUE_CLEANER_BRAND);
			String cleanerModel = values.getString(PoolDataAdapter.VALUE_CLEANER_MODEL);

			if(!val.equals("") && !(mPool.getCleanerBrand().equals(cleanerBrand)
					&& mPool.getCleanerModel().equals(cleanerModel))) {
				mPool.setCleanerBrand(cleanerBrand);
				mPool.setCleanerModel(cleanerModel);
				updated = true;
			}
			break;

		case PoolDataAdapter.SET_ADDRESS:
			Address address = (Address) values.getParcelable(PoolDataAdapter.VALUE_ADDRESS);

			if(!val.equals("") && address != null && PoolAddress.isComplete(address)) {
				mPool.setAddress(address);
				address_updated = true;
				updated = true;
			}
			break;

		default:
			break;
		}

		if(updated) {
			// Reset the populate variables to refresh any changed values
			mRefreshIndex = mListView.getFirstVisiblePosition();
			View v = mListView.getChildAt(0);
			mRefreshTop = (v == null) ? 0 : v.getTop();
			mPopulated = false;

			if(address_updated) {
				mCtx.getContentResolver().update(
					Uri.withAppendedPath(PoolPalContent.ADDRESSES_CONTENT_URI, Long.toString(mPool.getAddress().getId())),
					mPool.getAddress().getContentValues(), null, null);
			}

			mCtx.getContentResolver().update(
				Uri.withAppendedPath(PoolPalContent.POOLS_CONTENT_URI, Long.toString(mPool.getId())),
				mPool.getContentValues(), null, null);
		}
	}

	@Override
	public void onNegativeDecision(int requestCode) {
		// Reset the populate variables to refresh any changed values
		mRefreshIndex = mListView.getFirstVisiblePosition();
		View v = mListView.getChildAt(0);
		mRefreshTop = (v == null) ? 0 : v.getTop();
		mPopulated = false;

		getLoaderManager().initLoader(PoolPal.POOL_LOADER, null, this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (outState.isEmpty()) {
			outState.putBoolean("bug:fix", true);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		switch(id) {
		case PoolPal.POOL_LOADER:
			CursorLoader loader = new CursorLoader(mCtx,
					Uri.withAppendedPath(PoolPalContent.POOLS_CONTENT_URI,
							Long.toString(mPoolPreferences.getLong(PoolPal.PREFS_ACTIVE_POOL_ID, 0))),
							PoolTable.columnProjection(), null, null, null);
			return loader;

		default:
			return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		switch(loader.getId()) {
		case PoolPal.POOL_LOADER:
			cursor.moveToFirst();
			if(!cursor.isAfterLast()) {
				mPool = new Pool(mCtx, cursor);
				populate();
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

		switch(loader.getId()) {
		case PoolPal.POOL_LOADER:
			mPool = null;
			break;

		default:
			break;
		}
	}
}
