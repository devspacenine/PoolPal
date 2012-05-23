package com.devspacenine.poolpal.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devspacenine.poolpal.OnDecisionListener;
import com.devspacenine.poolpal.PoolPal;
import com.devspacenine.poolpal.R;
import com.devspacenine.poolpal.contentprovider.PoolPalContent;
import com.devspacenine.poolpal.database.PoolTable;
import com.devspacenine.poolpal.object.Pool;

public abstract class InputDialogFragment extends DialogFragment
	implements LoaderManager.LoaderCallbacks<Cursor> {

	public static final String REQUEST_CODE = "request_code";
	public static final String TITLE = "title";
	public static final String DETAILS = "details";
	public static final String LAYOUT = "layout";
	public static final String FRAGMENT_TAG = "fragment_tag";
	public static final String VALUES = "values";

	public static Bundle createItem(int requestCode, String title, String details,
			int layout, Bundle values, String fragmentTag) {
		Bundle args = new Bundle();
		args.putInt(REQUEST_CODE, requestCode);
		args.putString(TITLE, title);
		args.putString(DETAILS, details);
		args.putInt(LAYOUT, layout);
		args.putBundle(VALUES, values);
		if(fragmentTag != null) {
			args.putString(FRAGMENT_TAG, fragmentTag);
		}
		return args;
	}

	protected OnDecisionListener mListener;
	protected Bundle mValues;
	protected FragmentActivity mCtx;
	protected Pool mPool;
	private SharedPreferences mPoolPreferences;

	// View references
	protected ViewGroup mInputView;
	protected TextView mPrompt;
	protected TextView mCancelButton;
	protected TextView mConfirmButton;
	protected int mRequestCode;

	public static Object newInstance(Class<?> clss, FragmentActivity context,
			Bundle args) {

		Fragment frag = Fragment.instantiate(context, clss.getName(), null);
		frag.setArguments(args);

		return clss.cast(frag);
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);

		// Make sure the calling fragment or activity implements OnDecisionMadeListener
		if(getArguments().containsKey(FRAGMENT_TAG)) {
			Fragment frag = ((FragmentActivity)activity).getSupportFragmentManager().findFragmentByTag(
					getArguments().getString(FRAGMENT_TAG));
			try {
				mListener = (OnDecisionListener) frag;
			}catch(ClassCastException e) {
				throw new ClassCastException(
						frag + " must implement the OnDecisionMadeListener interface.");
			}
		}else{
			try {
				mListener = (OnDecisionListener) activity;
			}catch(ClassCastException e) {
				throw new ClassCastException(
						activity + " must implement the OnDecisionMadeListener interface.");
			}
		}
	}

	@Override
	public void onActivityCreated(Bundle savedState) {

		super.onActivityCreated(savedState);
		mCtx = getActivity();
		mPoolPreferences = mCtx.getSharedPreferences(PoolPal.PREFS_POOL, 0);
		getLoaderManager().initLoader(PoolPal.POOL_LOADER, null, this);
	}

	@Override
	public void onCreate(Bundle savedState) {

		super.onCreate(savedState);

		setStyle(STYLE_NO_FRAME, R.style.Theme_PoolPal_Transparent);
		setCancelable(true);

		mCtx = getActivity();

		mRequestCode = getArguments().getInt(REQUEST_CODE);
		mValues = getArguments().getBundle(VALUES);
	}

	@Override
	public void onCancel(DialogInterface dialog) {

		super.onCancel(dialog);
		negativeDecision();
	}

	protected void negativeDecision() {

		mListener.onNegativeDecision(mRequestCode);
		dismiss();
	}

	protected void positiveDecision() {

		mListener.onPositiveDecision(mRequestCode, mValues);
		dismiss();
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
			}else{
				Log.e("DSN", "Could not load pool from database.");
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
