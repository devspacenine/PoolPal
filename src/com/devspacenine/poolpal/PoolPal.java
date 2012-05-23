package com.devspacenine.poolpal;

import java.text.SimpleDateFormat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.devspacenine.poolpal.contentprovider.PoolPalContent;
import com.devspacenine.poolpal.database.PoolTable;
import com.devspacenine.poolpal.fragment.TutorialFragment;
import com.devspacenine.poolpal.object.Pool;

public class PoolPal extends SherlockFragmentActivity implements
	LoaderManager.LoaderCallbacks<Cursor> {

	// Loader Ids
	public static final int POOL_LOADER = 1;
	public static final int ADDRESS_LOADER = 2;
	public static final int TASK_LOADER = 3;
	public static final int REMINDER_LOADER = 4;
	public static final int TIMER_LOADER = 5;
	public static final int ITEM_LOADER = 6;
	public static final int ORDER_LOADER = 7;
	public static final int ORDER_ITEM_LOADER = 8;
	public static final int TREATMENT_LOADER = 9;
	public static final int WATER_TEST_LOADER = 10;

	// Request codes
	public static final int SET_IMAGE = 1;

	// Fragment tags
	public static final String WELCOME_FRAGMENT = "welcome";
	public static final String INPUT_DIALOG = "input_dialog";

	// Preference tags
	public static final String PREFS_POOL = "PoolPreferences";
	public static final String PREFS_ACTIVE_POOL_ID = "active_pool_id";
	public static final String PREFS_FIRST_APP_LAUNCH = "first_app_launch";

	// Extra tags
	public static final String EXTRA_POOL = "pool";
	public static final String EXTRA_PAGE = "page";
	public static final String EXTRA_IMAGE_URI = "image_capture_uri";
	public static final String EXTRA_ADDRESSES = "addresses";
	public static final String EXTRA_DATA = "data";
	public static final String EXTRA_ID = "id";
	public static final String EXTRA_TIME = "time";

	// Formatting
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
	public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("h:mm a");

	// Reference to context
	Context mCtx;

	SharedPreferences mPoolPreferences;
	Menu mOptionsMenu;
	Pool mPool;

	@Override
	public void onAttachedToWindow() {

		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedState) {

    	super.onCreate(savedState);

    	mCtx = this;
    	mPoolPreferences = getSharedPreferences(PREFS_POOL, 0);

        getSupportLoaderManager().initLoader(POOL_LOADER, null, this);

    	// Hide the action bar
    	getSupportActionBar().hide();

    	// Show the splash screen for 2 seconds before loading the dashboard
    	try{
    		Thread.sleep(2000);
    	}catch(InterruptedException e) {
    		e.printStackTrace();
    	}
    	setContentView(R.layout.dashboard);
    	((LinearLayout)findViewById(R.id.troubleshoot)).getChildAt(0).setSelected(true);

    	// Calculator button
    	findViewById(R.id.calculator).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mCtx, CalculatorActivity.class);
				if(mPool != null) intent.putExtra(EXTRA_POOL, mPool);
				startActivity(intent);
			}
		});
    }

    @Override
    public void onRestart() {

    	getSupportLoaderManager().initLoader(POOL_LOADER, null, this);
    	super.onRestart();
    }

    @Override
    public void onStart() {

    	if(mPoolPreferences.getBoolean(PREFS_FIRST_APP_LAUNCH, true)) {
    		Bundle data = new Bundle();
    		data.putInt(TutorialFragment.DATA_TITLE, R.string.title_welcome);
    		data.putInt(TutorialFragment.DATA_DETAILS, R.string.introduction);
    		data.putInt(TutorialFragment.DATA_CANCEL, R.string.get_started);
    		data.putInt(TutorialFragment.DATA_CONFIRM, R.string.take_tour);
    		TutorialFragment frag = TutorialFragment.newInstance(
					TutorialFragment.WELCOME,
					R.layout.tutorial_dialog,
					data,
					null);

    		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    		ft.add(frag, WELCOME_FRAGMENT);
    		ft.commitAllowingStateLoss();
		}
    	super.onStart();
    }

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		switch(id) {
		case POOL_LOADER:
			CursorLoader loader = new CursorLoader(this,
					Uri.withAppendedPath(PoolPalContent.POOLS_CONTENT_URI,
							Long.toString(mPoolPreferences.getLong(PREFS_ACTIVE_POOL_ID, 0))),
					PoolTable.columnProjection(), null, null, null);
			return loader;

		default:
			return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		switch(loader.getId()) {
		case POOL_LOADER:
			cursor.moveToFirst();
			if(!cursor.isAfterLast()) {
				mPool = new Pool(this, cursor);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

		switch(loader.getId()) {
		case POOL_LOADER:
			mPool = null;
			break;

		default:
			break;
		}
	}
}
