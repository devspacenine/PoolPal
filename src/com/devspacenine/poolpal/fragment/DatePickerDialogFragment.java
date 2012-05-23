/*
 * Copyright (C) 2011 Daniel Berndt - Codeus Ltd  -  DateSlider
 *
 * Class for setting up the dialog and initialsing the underlying
 * ScrollLayouts
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.devspacenine.poolpal.fragment;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.devspacenine.poolpal.OnDateSetListener;
import com.devspacenine.poolpal.PoolPal;
import com.devspacenine.poolpal.R;
import com.devspacenine.poolpal.view.SliderContainer;
import com.devspacenine.poolpal.view.SliderContainer.OnTimeChangeListener;
import com.devspacenine.poolpal.widget.PoolDataAdapter;
import com.devspacenine.poolpal.widget.TaskDataAdapter;
import com.devspacenine.poolpal.widget.labeler.TimeLabeler;

/**
 * A Dialog subclass that hosts a SliderContainer and a couple of buttons,
 * displays the current time in the header, and notifies an observer
 * when the user selectes a time.
 */
public class DatePickerDialogFragment extends InputDialogFragment {

	public static DatePickerDialogFragment newInstance(Bundle args) {

		DatePickerDialogFragment frag = new DatePickerDialogFragment();
		frag.setArguments(args);
		return frag;
	}

    protected OnDateSetListener mOnDateSetListener;
    protected Calendar mInitDate;
    protected TextView mTitleText;
    protected SliderContainer mContainer;

    @Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);

		// Make sure the calling fragment or activity implements OnDecisionMadeListener
		if(getArguments().containsKey(FRAGMENT_TAG)) {
			Fragment frag = ((FragmentActivity)activity).getSupportFragmentManager().findFragmentByTag(
					getArguments().getString(FRAGMENT_TAG));
			try {
				mOnDateSetListener = (OnDateSetListener) frag;
			}catch(ClassCastException e) {
				throw new ClassCastException(
						frag + " must implement the OnDecisionMadeListener interface.");
			}
		}else{
			try {
				mOnDateSetListener = (OnDateSetListener) activity;
			}catch(ClassCastException e) {
				throw new ClassCastException(
						activity + " must implement the OnDecisionMadeListener interface.");
			}
		}
	}

    /**
     * Set up the dialog with all the views and their listeners
     */
    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        if(!mValues.containsKey(TaskDataAdapter.VALUE_DATE)
				|| mValues.getSerializable(TaskDataAdapter.VALUE_DATE) == null) {
        	mInitDate = null;
			mValues.putSerializable(TaskDataAdapter.VALUE_DATE, Calendar.getInstance());
		}else{
			mInitDate = (Calendar) ((Calendar)mValues.getSerializable(TaskDataAdapter.VALUE_DATE)).clone();
		}

        if(!mValues.containsKey(TaskDataAdapter.VALUE_DATE_MIN)) {
			mValues.putSerializable(TaskDataAdapter.VALUE_DATE_MIN, Calendar.getInstance());
		}

		if(!mValues.containsKey(TaskDataAdapter.VALUE_DATE_INTERVAL)
				|| mValues.getInt(TaskDataAdapter.VALUE_DATE_INTERVAL) < 1) {
			mValues.putInt(TaskDataAdapter.VALUE_DATE_INTERVAL, 1);
		}
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {

		Bundle args = getArguments();

		mRequestCode = args.getInt(REQUEST_CODE);

		View v;
		v = inflater.inflate(R.layout.input_dialog, container, false);

		// Set the layout of the view stub
		ViewStub stub = (ViewStub) v.findViewById(R.id.stub);
		stub.setLayoutResource(args.getInt(LAYOUT));
		mInputView = (ViewGroup) stub.inflate();

		// Set the title
		mTitleText = (TextView) v.findViewById(R.id.title);
		mTitleText.setText(args.getString(TITLE));

		// Set the prompt
		mPrompt = (TextView) v.findViewById(R.id.prompt);
		mPrompt.setVisibility(View.VISIBLE);
		mPrompt.setText(args.getString(DETAILS));

		if(!(mValues.containsKey(PoolDataAdapter.VALUE))
				|| mValues.getString(PoolDataAdapter.VALUE) == null) {
			mValues.putString(PoolDataAdapter.VALUE, "");
		}

		mCancelButton = (TextView) v.findViewById(R.id.cancel);
		mConfirmButton = (TextView) v.findViewById(R.id.confirm);

		mCancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				negativeDecision();
			}
		});

		mConfirmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				positiveDecision();
			}
		});

		Calendar initTime = (Calendar) mValues.getSerializable(TaskDataAdapter.VALUE_DATE);
		Calendar minTime = (Calendar) mValues.getSerializable(TaskDataAdapter.VALUE_DATE_MIN);
        Calendar maxTime = (Calendar) mValues.getSerializable(TaskDataAdapter.VALUE_DATE_MAX);
        int interval = mValues.getInt(TaskDataAdapter.VALUE_DATE_INTERVAL);

        if (interval > 1) {
        	int minutes = initTime.get(Calendar.MINUTE);
    		int diff = ((minutes+interval/2)/interval)*interval - minutes;
    		initTime.add(Calendar.MINUTE, diff);
        }

		mContainer = (SliderContainer) mInputView;
		mContainer.setOnTimeChangeListener(onTimeChangeListener);
        mContainer.setMinuteInterval(interval);
        mContainer.setTime(initTime);
        if (minTime!=null) mContainer.setMinTime(minTime);
        if (maxTime!=null) mContainer.setMaxTime(maxTime);

		return v;
    }

    @Override
    public void negativeDecision() {
    	super.negativeDecision();
    }

    @Override
    public void positiveDecision() {
    	mOnDateSetListener.onDateSet(this, getTime());
    	super.positiveDecision();
    }

    public void setTime(Calendar c) {
        mContainer.setTime(c);
    }

    private OnTimeChangeListener onTimeChangeListener = new OnTimeChangeListener() {

        public void onTimeChange(Calendar time) {
            setTitle();
        }
    };

    @Override
    public void onSaveInstanceState(Bundle out) {

        super.onSaveInstanceState(out);
        out.putSerializable(PoolPal.EXTRA_TIME, getTime());
    }

    /**
     * @return The currently displayed time
     */
    protected Calendar getTime() {
        return mContainer.getTime();
    }

    /**
     * This method sets the title of the dialog
     */
    protected void setTitle() {
        if (mTitleText != null) {
            final Calendar c = getTime();
            switch(getArguments().getInt(LAYOUT)) {
            case R.layout.date_slider:
	            mTitleText.setText(mCtx.getString(R.string.dateSliderTitle) +
	                    String.format(": %te. %tB %tY", c, c, c));
	            break;

            case R.layout.month_year_date_slider:
            	mTitleText.setText(mCtx.getString(R.string.dateSliderTitle) +
                        String.format(": %tB %tY",c,c));
            	break;

            case R.layout.time_slider:
            	mTitleText.setText(String.format("Selected Time: %tR",getTime()));
            	break;

            case R.layout.date_time_slider:
            	int minute = c.get(Calendar.MINUTE)/TimeLabeler.MINUTEINTERVAL*TimeLabeler.MINUTEINTERVAL;
                mTitleText.setText(String.format("Selected DateTime: %te/%tm/%ty %tH:%02d",
                        c,c,c,c,minute));
                break;

	        default:
	        	break;
            }
        }
    }
}
