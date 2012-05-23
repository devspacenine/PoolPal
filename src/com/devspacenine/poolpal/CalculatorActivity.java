package com.devspacenine.poolpal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.devspacenine.poolpal.contentprovider.PoolPalContent;
import com.devspacenine.poolpal.database.PoolTable;
import com.devspacenine.poolpal.fragment.CalculatorFragment;
import com.devspacenine.poolpal.object.Pool;

public class CalculatorActivity extends SherlockFragmentActivity implements
	LoaderManager.LoaderCallbacks<Cursor>, OnClickListener {

	// Cursor for pools
	private Pool mPool;
	private SharedPreferences mPoolPreferences;
	private ImageView mPoolImage;
	private Bitmap mPoolImageBitmap;

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

    	mPoolPreferences = getSharedPreferences(PoolPal.PREFS_POOL, 0);

    	ActionBar ab = getSupportActionBar();
    	ab.setHomeButtonEnabled(true);

    	setContentView(R.layout.calculator);
		// Setup cursor loader and adapter for the pool spinner
		getSupportLoaderManager().initLoader(PoolPal.POOL_LOADER, null, this);

    	// Load the calculator fragment
		CalculatorFragment frag = CalculatorFragment.newInstance();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.calculator, frag);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();

    }

    @Override
	public void onPause() {

		getContentResolver().update(
				Uri.withAppendedPath(PoolPalContent.POOLS_CONTENT_URI, Long.toString(mPool.getId())),
				mPool.getContentValues(), null, null);
		super.onPause();
	}

    @Override
    public void onRestart() {

    	getSupportLoaderManager().initLoader(PoolPal.POOL_LOADER, null, this);
    	super.onRestart();
    }

    @Override
	public void onClick(View v) {

		Intent intent = new Intent(this, EditPoolActivity.class);
		intent.putExtra(PoolPal.EXTRA_PAGE, R.layout.edit_pool);
		startActivity(intent);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

    	super.onCreateOptionsMenu(menu);

    	// Inflate the menu from XML
    	MenuInflater inflater = getSupportMenuInflater();
    	inflater.inflate(R.menu.calculator_menu, menu);

    	MenuItem alerts = menu.getItem(0);
    	alerts.setVisible(true);
    	alerts.setEnabled(true);

    	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

    	switch(item.getItemId()) {
    	case android.R.id.home:
    		finish();
    		return true;

    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }

    public void populateInfo() {

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
		TextView volume = (TextView) findViewById(R.id.volume);
		if(volume != null) {
			volume.setText(mPool.getVolumeString());
		}

		findViewById(R.id.pool).setOnClickListener(this);
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
				// Set the details of the PoolView
				populateInfo();
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
