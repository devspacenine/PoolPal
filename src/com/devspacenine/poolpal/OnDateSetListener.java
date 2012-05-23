package com.devspacenine.poolpal;

import java.util.Calendar;

import com.devspacenine.poolpal.fragment.DatePickerDialogFragment;

public interface OnDateSetListener {
	/**
     * this method is called when a date was selected by the user
     * @param view			the caller of the method
     *
     */
    public void onDateSet(DatePickerDialogFragment view, Calendar selectedDate);
}
