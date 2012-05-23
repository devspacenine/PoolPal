package com.devspacenine.poolpal.fragment;

import java.util.HashMap;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.devspacenine.poolpal.R;

public class CalculatorFragment extends Fragment implements OnSeekBarChangeListener,
	OnClickListener {

	// Dialog request Codes
	public static final int DIALOG_CHLORINE_INPUT = 1;
	public static final int DIALOG_PH_INPUT = 2;
	public static final int DIALOG_ALKALINITY_INPUT = 3;
	public static final int DIALOG_HARDNESS_INPUT = 4;
	public static final int DIALOG_STABILIZER_INPUT = 5;
	public static final int DIALOG_TEMPERATURE_INPUT = 6;

	// Input tags
	private final String[] INPUTS = {"chlorine", "ph", "alkalinity",
			"hardness", "stabilizer", "temperature"};
	private final String TAG_CHLORINE = "chlorine";
	private final String TAG_PH = "ph";
	private final String TAG_ALKALINITY = "alkalinity";
	private final String TAG_HARDNESS = "hardness";
	private final String TAG_STABILIZER = "stabilizer";
	private final String TAG_TEMPERATURE = "temperature";

	// Saved state tags
	private final String TAG_CHLORINE_VALUE = "chlorine_value";
	private final String TAG_PH_VALUE = "ph_value";
	private final String TAG_ALKALINITY_VALUE = "alkalinity_value";
	private final String TAG_HARDNESS_VALUE = "hardness_value";
	private final String TAG_STABILIZER_VALUE = "stabilizer_value";
	private final String TAG_TEMPERATURE_VALUE = "temperature_value";
	private final String TAG_SELECTED_INPUT = "selected_input";

	private HashMap<String, String[]> mValues;
	private HashMap<String, Integer> mCurrentValues;
	private HashMap<String, Integer> mDefaultValues;
	private HashMap<String, String> mFormats;

	private SeekBar mSeekBar;
	private TextView mValue;
	private ImageView mClear;
	private LinearLayout mSelectedInput;

	public static CalculatorFragment newInstance() {

		CalculatorFragment frag = new CalculatorFragment();

		Bundle args = new Bundle();
		frag.setArguments(args);

		return frag;
	}

	@Override
	public void onCreate(Bundle savedState) {

		super.onCreate(savedState);

		mValues = new HashMap<String, String[]>();
		mCurrentValues = new HashMap<String, Integer>();
		mDefaultValues = new HashMap<String, Integer>();
		mFormats = new HashMap<String, String>();

		Resources res = getResources();
		mValues.put(TAG_CHLORINE, res.getStringArray(R.array.chlorine_values));
		mValues.put(TAG_PH, res.getStringArray(R.array.ph_values));
		mValues.put(TAG_ALKALINITY, res.getStringArray(R.array.alkalinity_values));
		mValues.put(TAG_HARDNESS, res.getStringArray(R.array.hardness_values));
		mValues.put(TAG_STABILIZER, res.getStringArray(R.array.stabilizer_values));
		mValues.put(TAG_TEMPERATURE, res.getStringArray(R.array.temperature_values));

		mDefaultValues.put(TAG_CHLORINE, res.getInteger(R.integer.chlorine_default));
		mDefaultValues.put(TAG_PH, res.getInteger(R.integer.ph_default));
		mDefaultValues.put(TAG_ALKALINITY, res.getInteger(R.integer.alkalinity_default));
		mDefaultValues.put(TAG_HARDNESS, res.getInteger(R.integer.hardness_default));
		mDefaultValues.put(TAG_STABILIZER, res.getInteger(R.integer.stabilizer_default));
		mDefaultValues.put(TAG_TEMPERATURE, res.getInteger(R.integer.temperature_default));

		if(savedState != null) {
			mCurrentValues.put(TAG_CHLORINE, savedState.getInt("chlorine_value"));
			mCurrentValues.put(TAG_PH, savedState.getInt("ph_value"));
			mCurrentValues.put(TAG_ALKALINITY, savedState.getInt("alkalinity_value"));
			mCurrentValues.put(TAG_HARDNESS, savedState.getInt("hardness_value"));
			mCurrentValues.put(TAG_STABILIZER, savedState.getInt("stabilizer_value"));
			mCurrentValues.put(TAG_TEMPERATURE, savedState.getInt("temperature_value"));
		}else{
			mCurrentValues.put(TAG_CHLORINE, -1);
			mCurrentValues.put(TAG_PH, -1);
			mCurrentValues.put(TAG_ALKALINITY, -1);
			mCurrentValues.put(TAG_HARDNESS, -1);
			mCurrentValues.put(TAG_STABILIZER, -1);
			mCurrentValues.put(TAG_TEMPERATURE, -1);
		}

		mFormats.put(TAG_CHLORINE, res.getString(R.string.val_chlorine));
		mFormats.put(TAG_PH, res.getString(R.string.val_ph));
		mFormats.put(TAG_ALKALINITY, res.getString(R.string.val_alkalinity));
		mFormats.put(TAG_HARDNESS, res.getString(R.string.val_hardness));
		mFormats.put(TAG_STABILIZER, res.getString(R.string.val_stabilizer));
		mFormats.put(TAG_TEMPERATURE, res.getString(R.string.val_temperature));
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
		outState.putInt(TAG_CHLORINE_VALUE, mCurrentValues.get(TAG_CHLORINE));
		outState.putInt(TAG_PH_VALUE, mCurrentValues.get(TAG_PH));
		outState.putInt(TAG_ALKALINITY_VALUE, mCurrentValues.get(TAG_ALKALINITY));
		outState.putInt(TAG_HARDNESS_VALUE, mCurrentValues.get(TAG_HARDNESS));
		outState.putInt(TAG_STABILIZER_VALUE, mCurrentValues.get(TAG_STABILIZER));
		outState.putInt(TAG_TEMPERATURE_VALUE, mCurrentValues.get(TAG_TEMPERATURE));
		outState.putInt(TAG_SELECTED_INPUT, mSelectedInput.getId());
	}

	/**
	 * Inflate and setup the UI
	 */
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedState) {

    	View v = inflater.inflate(R.layout.water_chemistry_calculator, container, false);

    	// Get a reference to the seek bar and value, and set an on change listener
		mSeekBar = (SeekBar) v.findViewById(R.id.seek_bar);
		mValue = (TextView) v.findViewById(R.id.value);
		mClear = (ImageView) v.findViewById(R.id.clear);
		mSeekBar.setOnSeekBarChangeListener(this);

    	LinearLayout[] inputs = new LinearLayout[6];
    	inputs[0] = (LinearLayout) v.findViewById(R.id.chlorine);
    	inputs[1] = (LinearLayout) v.findViewById(R.id.ph);
    	inputs[2] = (LinearLayout) v.findViewById(R.id.alkalinity);
    	inputs[3] = (LinearLayout) v.findViewById(R.id.hardness);
    	inputs[4] = (LinearLayout) v.findViewById(R.id.stabilizer);
    	inputs[5] = (LinearLayout) v.findViewById(R.id.temperature);

    	for(int i=0; i<inputs.length; i++) {
    		String[] values = mValues.get(INPUTS[i]);
    		int current_value = mCurrentValues.get(INPUTS[i]);

    		inputs[i].setOnClickListener(this);


    		((TextView)inputs[i].getChildAt(1)).setText(
    				(current_value >= 0) ? values[current_value] : "- - -");
    	}

    	// Reload saved state if possible
    	String tag;
    	if(savedState == null) {
    		// Set the current value and progress of the seek bar
    		mSelectedInput = (LinearLayout)v.findViewById(R.id.chlorine);
    		mSelectedInput.setSelected(true);
    		tag = TAG_CHLORINE;
		}else{
			mSelectedInput = (LinearLayout)v.findViewById(savedState.getInt(TAG_SELECTED_INPUT));
			mSelectedInput.setSelected(true);
			tag = mSelectedInput.getTag().toString().toLowerCase();
		}

    	mSeekBar.setMax(mValues.get(tag).length-1);
		int value = mCurrentValues.get(tag);
		if(value >= 0) {
			mSeekBar.setProgress(mCurrentValues.get(tag));
			mValue.setText(mValues.get(tag)[value]);
			mClear.setVisibility(View.VISIBLE);
		}else{
    		mSeekBar.setProgress(mSeekBar.getMax()/2);
    		mValue.setText("(drag to set)");
    		mClear.setVisibility(View.INVISIBLE);
		}

    	// Setup the clear button
    	mClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				clearValue();
			}
		});

    	return v;
    }

	@Override
	public void onClick(View v) {

		// Unselect the previously selected input before selecting the one just clicked
		mSelectedInput.setSelected(false);
		v.setSelected(true);
		mSelectedInput = (LinearLayout) v;
		String tag = v.getTag().toString().toLowerCase();

		int value = mCurrentValues.get(tag);
		String format = mFormats.get(tag);
		mSeekBar.setMax(mValues.get(tag).length-1);
		if(value >= 0) {
			mSeekBar.setProgress(mCurrentValues.get(tag));
			mValue.setText(String.format(format, mValues.get(tag)[value]));
			mClear.setVisibility(View.VISIBLE);
		}else{
			mSeekBar.setProgress(mSeekBar.getMax()/2);
			mValue.setText("(drag to set)");
			mClear.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {

		// Only react to changes made by the user
		if(fromUser) {
			// Update the value text of the selected input
			String tag = mSelectedInput.getTag().toString().toLowerCase();
			String format = mFormats.get(tag);
			// Set the buttons text
			((TextView)mSelectedInput.getChildAt(1)).setText(String.format(format, mValues.get(tag)[progress]));
			// Set the input window's text
			mValue.setText(String.format(format, mValues.get(tag)[progress]));
			mCurrentValues.put(tag, progress);
			mClear.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	public void clearValue() {

		String tag = mSelectedInput.getTag().toString().toLowerCase();
		mCurrentValues.put(tag, -1);
		mSeekBar.setProgress(mSeekBar.getMax()/2);
		mValue.setText("- - -");
		((TextView)mSelectedInput.getChildAt(1)).setText("- - -");
		mClear.setVisibility(View.INVISIBLE);
	}
}
