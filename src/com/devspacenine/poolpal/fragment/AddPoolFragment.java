package com.devspacenine.poolpal.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.devspacenine.poolpal.R;

public class AddPoolFragment extends SherlockFragment {

	private boolean mDualPane;
	private int mCurFocusedSection;

	@Override
	public void onActivityCreated(Bundle savedState) {

		super.onActivityCreated(savedState);

		// Check to see if we have a frame in which to embed the focus
        // fragment directly in the containing UI.
        View toolsFrame = getActivity().findViewById(R.id.tools);
        mDualPane = toolsFrame != null
                && toolsFrame.getVisibility() == View.VISIBLE;

        if(savedState != null) {
        	// Restore last state for focused section
        	mCurFocusedSection = savedState.getInt("curFocus", 0);
        }

        if(mDualPane) {
        	// In dual pane, show tools UI for the current focus
        	showTool(mCurFocusedSection);
        }
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
		outState.putInt("curFocus", mCurFocusedSection);
	}

	/**
	 * Helper function to show tools for the focused section.
	 */
	void showTool(int index) {
		mCurFocusedSection = index;

		if(mDualPane) {

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.edit_pool, container, false);

		return v;
	}
}
