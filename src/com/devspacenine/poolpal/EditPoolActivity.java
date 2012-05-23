package com.devspacenine.poolpal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.devspacenine.poolpal.contentprovider.PoolPalContent;
import com.devspacenine.poolpal.database.PoolTable;
import com.devspacenine.poolpal.fragment.ChartsFragment;
import com.devspacenine.poolpal.fragment.InputDialogFragment;
import com.devspacenine.poolpal.fragment.PoolInfoFragment;
import com.devspacenine.poolpal.fragment.PoolTasksFragment;
import com.devspacenine.poolpal.fragment.SetImageDialogFragment;
import com.devspacenine.poolpal.fragment.TutorialFragment;
import com.devspacenine.poolpal.object.Pool;
import com.devspacenine.poolpal.object.PoolAddress;
import com.devspacenine.poolpal.widget.PoolDataAdapter;
import com.devspacenine.poolpal.widget.ProfileTabsAdapter;
import com.devspacenine.poolpal.widget.SettingsSectionAdapter;

public class EditPoolActivity extends SherlockFragmentActivity implements OnDecisionListener,
	LoaderManager.LoaderCallbacks<Cursor>, TempDataListener {

	// Fragment tags
	public static final String CONTINUE_EDITING_FRAGMENT = "continue_editing";

	TabHost mTabHost;
    ViewPager  mViewPager;
    ProfileTabsAdapter mTabsAdapter;
    TabWidget mTabWidget;

	private ImageView mPoolImage;
	private Bitmap mPoolImageBitmap;
	private int mLayout;
	private SettingsSectionAdapter mAdapter;
	private ListView mListView;
	private Pool mPool;
	private SharedPreferences mPoolPreferences;
	private boolean mPopulated;
	private int mRefreshIndex;
	private int mRefreshTop;

	private Bundle mTempData;

	@Override
	public void onAttachedToWindow() {

		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}

	@Override
	public void onCreate(Bundle savedState) {

		super.onCreate(savedState);

		mTempData = new Bundle();
		mPoolPreferences = getSharedPreferences(PoolPal.PREFS_POOL, 0);
		mPopulated = false;
		mRefreshIndex = 0;
		mRefreshTop = 0;
		Resources res = getResources();

		ActionBar ab = getSupportActionBar();
		ab.setSubtitle(R.string.ab_edit_pool);
		ab.setHomeButtonEnabled(true);

		Intent intent = getIntent();

		// Use an extra from the calling intent to determine the layout, defaulting
		// to the top layout if not extra exists
		mLayout = intent.getIntExtra(PoolPal.EXTRA_PAGE, R.layout.edit_pool);
		setContentView(mLayout);

		// Setup the target layout
		if(mLayout == R.layout.init_pool) {
			mListView = (ListView) findViewById(R.id.settings);
			ab.setTitle(R.string.ab_new_pool);
			mPool = new Pool();
			populate(mLayout);
		}else{
			getSupportLoaderManager().initLoader(PoolPal.POOL_LOADER, null, this);
			mTabHost = (TabHost)findViewById(android.R.id.tabhost);
			mTabHost.setup();
            mViewPager = (ViewPager)findViewById(R.id.pager);
            mTabWidget = (TabWidget) findViewById(android.R.id.tabs);

	        mTabsAdapter = new ProfileTabsAdapter(this, mTabHost, mViewPager);

	        mTabsAdapter.addTab(res.getString(R.string.btn_info), ProfileTabsAdapter.TAB_INFO,
	                PoolInfoFragment.class, mTabWidget);
	        mTabsAdapter.addTab(res.getString(R.string.btn_tasks), ProfileTabsAdapter.TAB_TASKS,
	                PoolTasksFragment.class, mTabWidget);
	        mTabsAdapter.addTab(res.getString(R.string.btn_charts), ProfileTabsAdapter.TAB_CHARTS,
	                ChartsFragment.class, mTabWidget);

	        if(savedState != null) {
	        	mTabHost.setCurrentTabByTag(savedState.getString("tab"));
	        }else{
	        	mTabHost.setCurrentTab(0);
	        }
		}
	}

	public void replaceTab(String tag, Class<?> clss, Bundle args) {
		mTabsAdapter.replaceTab(tag, clss, args);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch(requestCode) {
		case PoolPal.SET_IMAGE:
			if(resultCode == SherlockFragmentActivity.RESULT_OK) {
				try {
					// Create the returned bitmap. Image capture will return it as an
					// extra, and the gallery returns a uri
					if(data.hasExtra("data")) {
						mPoolImageBitmap = (Bitmap) data.getExtras().get("data");
						if(mTempData.containsKey(PoolPal.EXTRA_IMAGE_URI)) {
							mPool.setImage(mTempData.getString(PoolPal.EXTRA_IMAGE_URI));
							mTempData.remove(PoolPal.EXTRA_IMAGE_URI);
						}else{
							throw new IOException("Returning Activity/Fragment must save the captured "
									+ "image URI with the TempDataListener interface.");
						}
					}else{
		                InputStream stream = getContentResolver().openInputStream(
		                        data.getData());
		                mPoolImageBitmap = BitmapFactory.decodeStream(stream);
		                stream.close();
		                mPool.setImage(data.getData().toString());
					}

					// Save the new image URI to the database
	                getContentResolver().update(
		    				Uri.withAppendedPath(PoolPalContent.POOLS_CONTENT_URI, Long.toString(mPool.getId())),
		    				mPool.getContentValues(), null, null);

	                // Update the imageview with the new bitmap and appropriate tags
	                mPoolImage.setImageBitmap(mPoolImageBitmap);
	                mPoolImage.setTag(R.id.key_uri, mPool.getImage());
	                mPoolImage.setTag(R.id.key_set, Boolean.toString(true));
	            } catch (FileNotFoundException e) {
	                e.printStackTrace();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {

		if(mLayout == R.layout.init_pool) {
			finish();
		}else{
			mTabsAdapter.onBack();
		}
	}

	public void setImage(View v) {

		if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			// Create bundle of arguments for the fragment
			Bundle args = new Bundle();
			args.putInt(InputDialogFragment.REQUEST_CODE, PoolDataAdapter.SET_IMAGE);
			args.putString(InputDialogFragment.TITLE,
					getResources().getString(R.string.title_set_image));
			args.putString(InputDialogFragment.DETAILS,
					getResources().getString(R.string.prompt_set_image));
			args.putInt(InputDialogFragment.LAYOUT, R.layout.options_input);

			SetImageDialogFragment frag = SetImageDialogFragment.newInstance(args);
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add(frag, PoolPal.INPUT_DIALOG);
			ft.commitAllowingStateLoss();
		}else{
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			startActivityForResult(intent, PoolPal.SET_IMAGE);
		}
	}

	public void populate(int page) {

		Resources res = getResources();

		if(!mPopulated) {
			// Set references to pool image
			mPoolImage = (ImageView) findViewById(R.id.photo);
			if(mPoolImage != null) {
				mPoolImageBitmap = ((BitmapDrawable)mPoolImage.getDrawable()).getBitmap();
				if(mPool.hasImage()) {
					try {
						InputStream stream = getContentResolver().openInputStream(
		                        Uri.parse(mPool.getImage()));
						mPoolImageBitmap = BitmapFactory.decodeStream(stream);
		                stream.close();
		                mPoolImage.setImageBitmap(mPoolImageBitmap);
		                mPoolImage.setTag(R.id.key_uri, mPool.getImage());
		                mPoolImage.setTag(R.id.key_set, Boolean.toString(true));
					}catch(FileNotFoundException e) {
						e.printStackTrace();
					}catch(IOException e2) {
						e2.printStackTrace();
					}

				}
			}

			TextView name = (TextView) findViewById(R.id.nickname);
			if(name != null) {
				name.setText(mPool.getName());
			}
			TextView location = (TextView) findViewById(R.id.location);
			if(location != null) {
				location.setText(mPool.getLocationText());
			}
			TextView health = (TextView) findViewById(R.id.health);
			if(health != null) {
				health.setText(R.string.health_perfect);
			}
			ImageView health_icon = (ImageView) findViewById(R.id.health_icon);
			if(health_icon != null) {
				health_icon.setImageResource(R.drawable.ic_small_heart_green);
			}

			switch(page) {
			case R.layout.init_pool:
				LinkedList<Bundle> basic_data = new LinkedList<Bundle>();

				basic_data.add(PoolDataAdapter.createItem(res.getString(R.string.lbl_name),
						res.getString(R.string.details_name),
						PoolDataAdapter.ACTION_SET,
						PoolDataAdapter.SET_NAME,
						mPool.hasName()));
				basic_data.add(PoolDataAdapter.createItem(res.getString(R.string.lbl_dimensions),
						res.getString(R.string.details_dimensions),
						PoolDataAdapter.ACTION_SET,
						PoolDataAdapter.SET_DIMENSIONS,
						mPool.hasDimensions()));
				basic_data.add(PoolDataAdapter.createItem(res.getString(R.string.lbl_traffic),
						res.getString(R.string.details_traffic),
						PoolDataAdapter.ACTION_SET,
						PoolDataAdapter.SET_TRAFFIC,
						mPool.hasTraffic()));
				basic_data.add(PoolDataAdapter.createItem(res.getString(R.string.lbl_address),
						res.getString(R.string.details_address),
						PoolDataAdapter.ACTION_SET,
						PoolDataAdapter.SET_ADDRESS,
						mPool.hasAddress()));

				mAdapter = new SettingsSectionAdapter(this, mPool);
				mAdapter.addSection(res.getString(R.string.title_pool_details),
						new PoolDataAdapter(this, mPool, basic_data, R.layout.edit_settings_item));

				mListView.setAdapter(mAdapter);
				break;

			default:
				break;
			}
			mPopulated = true;
		}

		if(mRefreshIndex > 0 || mRefreshTop > 0) {
			mListView.setSelectionFromTop(mRefreshIndex, mRefreshTop);
			mRefreshIndex = 0;
			mRefreshTop = 0;
		}
	}

	@Override
	public void onPositiveDecision(int requestCode, Bundle values) {

		if(mLayout == R.layout.init_pool) {
			String val = values.getString(PoolDataAdapter.VALUE);
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

			case PoolDataAdapter.SET_ADDRESS:
				Address address = (Address) values.getParcelable(PoolDataAdapter.VALUE_ADDRESS);

				if(!val.equals("") && address != null && PoolAddress.isComplete(address)) {
					mPool.setAddress(address);
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

				populate(mLayout);
			}

			if(mAdapter.allItemsSet()) {
				// Insert the address in the database and update the pool's
				// address object with the new id
				mPool.getAddress().insert(this);

				// Insert the pool in the database and update it with the new id
				mPool.insert(this);

				// Update the default pool id if this is the first pool
				if(mPoolPreferences.getLong(PoolPal.PREFS_ACTIVE_POOL_ID, 0) == 0 && mPool.getId() > 0) {
					SharedPreferences.Editor editor = mPoolPreferences.edit();
					editor.putLong(PoolPal.PREFS_ACTIVE_POOL_ID, mPool.getId());
					editor.putBoolean(PoolPal.PREFS_FIRST_APP_LAUNCH, false);
					editor.commit();
				}

				Bundle data = new Bundle();
				data.putInt(TutorialFragment.DATA_TITLE, R.string.title_continue_editing);
				data.putInt(TutorialFragment.DATA_DETAILS, R.string.continue_editing);
				data.putInt(TutorialFragment.DATA_CANCEL, R.string.finish);
				data.putInt(TutorialFragment.DATA_CONFIRM, R.string.add_more);
				TutorialFragment frag = TutorialFragment.newInstance(
						TutorialFragment.CONTINUE_EDITING,
						R.layout.tutorial_dialog,
						data,
						null);

				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	    		ft.add(frag, CONTINUE_EDITING_FRAGMENT);
	    		ft.commit();
	    		return;
			}
		}else{
			switch(requestCode) {
			default:
				break;
			}
		}
	}

	@Override
	public void onNegativeDecision(int requestCode) {
		if(mListView != null) {
			// Reset the populate variables to refresh any changed values
			mRefreshIndex = mListView.getFirstVisiblePosition();
			View v = mListView.getChildAt(0);
			mRefreshTop = (v == null) ? 0 : v.getTop();
		}
		mPopulated = false;

		populate(mLayout);
	}

	public Pool setAccessory(int pos, boolean checked) {

		mPool.setAccessory(pos, checked);
		getContentResolver().update(
			Uri.withAppendedPath(PoolPalContent.POOLS_CONTENT_URI, Long.toString(mPool.getId())),
			mPool.getContentValues(), null, null);
		return mPool;
	}

	public Pool toggle(int target) {

		mPool.toggle(target);
		getContentResolver().update(
			Uri.withAppendedPath(PoolPalContent.POOLS_CONTENT_URI, Long.toString(mPool.getId())),
			mPool.getContentValues(), null, null);
		return mPool;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		switch(id) {
		case PoolPal.POOL_LOADER:
			CursorLoader loader = new CursorLoader(this,
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
				mPool = new Pool(this, cursor);
				populate(mLayout);
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

	@Override
	public void saveTempData(Bundle data) {
		mTempData.putAll(data);
	}
}
