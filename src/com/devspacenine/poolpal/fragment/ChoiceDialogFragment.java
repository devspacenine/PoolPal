package com.devspacenine.poolpal.fragment;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.devspacenine.poolpal.R;
import com.devspacenine.poolpal.widget.PoolDataAdapter;

public class ChoiceDialogFragment extends InputDialogFragment {

	public static ChoiceDialogFragment newInstance(Bundle args) {

		ChoiceDialogFragment frag = new ChoiceDialogFragment();
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {

		final Resources res = getResources();
		Bundle args = getArguments();

		mRequestCode = args.getInt(REQUEST_CODE);

		View v;
		v = inflater.inflate(R.layout.input_dialog, container, false);

		// Set the layout of the view stub
		ViewStub stub = (ViewStub) v.findViewById(R.id.stub);
		stub.setLayoutResource(args.getInt(LAYOUT));
		mInputView = (ViewGroup) stub.inflate();

		// Set the title
		TextView title = (TextView) v.findViewById(R.id.title);
		title.setText(args.getString(TITLE));

		// Set the prompt
		mPrompt = (TextView) v.findViewById(R.id.prompt);
		mPrompt.setVisibility(View.VISIBLE);
		mPrompt.setText(args.getString(DETAILS));

		if(!(mValues.containsKey(PoolDataAdapter.VALUE))) {
			mValues.putString(PoolDataAdapter.VALUE, "");
		}

		mCancelButton = (TextView) v.findViewById(R.id.cancel);
		mConfirmButton = (TextView) v.findViewById(R.id.confirm);

		// Initialize the type tag of the cancel button
		mCancelButton.setTag(R.id.key_type, "normal");
		mConfirmButton.setTag(R.id.key_type, "normal");

		// Set click listeners for the confirm button
		mConfirmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mValues.getString(PoolDataAdapter.VALUE).equals("")) {
					mListener.onNegativeDecision(mRequestCode);
				}else{
					mListener.onPositiveDecision(mRequestCode, mValues);
				}
				dismiss();
				}
		});

		// Setup the layout depending on what value is being set
		switch(args.getInt(REQUEST_CODE)) {
		case PoolDataAdapter.SET_TRAFFIC:

			// Setup a listview of options
			setupOptionsList(res.getStringArray(R.array.pool_traffic_levels));
			break;

		case PoolDataAdapter.SET_POOL_LOCALE:

			// Setup a listview of options
			setupOptionsList(res.getStringArray(R.array.pool_locales));
			break;

		case PoolDataAdapter.SET_FINISH:

			// Setup a listview of options
			setupOptionsList(res.getStringArray(R.array.pool_finishes));
			break;

		case PoolDataAdapter.SET_SANITIZER:

			// Setup a listview of options
			setupOptionsList(res.getStringArray(R.array.pool_sanitizer_systems));
			break;

		case PoolDataAdapter.SET_FILTER:

			// Setup a listview of options
			setupOptionsList(res.getStringArray(R.array.pool_filters));
			break;

		default:
			break;
		}

		return v;
	}

	public void setupOptionsList(String[] options) {

		if(!mValues.containsKey(PoolDataAdapter.VALUE_CHOICE)) {
			mValues.putString(PoolDataAdapter.VALUE_CHOICE, "");
		}
		final ArrayList<String> opts = new ArrayList<String>(Arrays.asList(options));
		ArrayAdapter<String> choices = new ArrayAdapter<String>(mCtx,
				R.layout.spinner_item, opts);
		final int initPosition = (mValues.getString(PoolDataAdapter.VALUE_CHOICE).equals(""))
				? -1 : opts.indexOf(mValues.getString(PoolDataAdapter.VALUE_CHOICE));
		final ListView list = (ListView) mInputView.findViewById(R.id.list);
		list.setAdapter(choices);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				parent.setSelection(position);
				mValues.putString(PoolDataAdapter.VALUE_CHOICE, opts.get(position));
				if(mValues.getString(PoolDataAdapter.VALUE_CHOICE).equals("")) {
					mValues.putString(PoolDataAdapter.VALUE, "");
				}else{
					mValues.putString(PoolDataAdapter.VALUE, opts.get(position));
				}
			}
		});
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		list.setItemsCanFocus(false);
		list.setItemChecked(initPosition, true);
		list.setSelection(initPosition);

		// Make the cancel button reset the selection
		mCancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mValues.putString(PoolDataAdapter.VALUE_CHOICE,
						(initPosition >= 0 && initPosition < opts.size()) ? opts.get(initPosition) : "");
				if(mValues.getString(PoolDataAdapter.VALUE_CHOICE).equals("")) {
					mValues.putString(PoolDataAdapter.VALUE, "");
				}else{
					mValues.putString(PoolDataAdapter.VALUE, opts.get(initPosition));
				}
				list.setItemChecked(initPosition, true);
				list.setSelection(initPosition);
				negativeDecision();
			}
		});
	}
}
