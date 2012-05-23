package com.devspacenine.poolpal.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devspacenine.poolpal.R;

public class ChartsFragment extends Fragment {

	/**
	 * Inflate and setup the UI
	 */
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {

    	View v = inflater.inflate(R.layout.charts, container, false);

    	return v;
    }

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (outState.isEmpty()) {
			outState.putBoolean("bug:fix", true);
		}
	}
}
