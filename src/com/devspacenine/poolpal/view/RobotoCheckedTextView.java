package com.devspacenine.poolpal.view;

import com.devspacenine.poolpal.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

public class RobotoCheckedTextView extends CheckedTextView {

	public RobotoCheckedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		//Typeface.createFromAsset doesn't work in the layout editor. Skipping...
        if (isInEditMode()) {
            return;
        }

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), getStyle(context, attrs, defStyle));
        setTypeface(typeface);
	}

	public RobotoCheckedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		//Typeface.createFromAsset doesn't work in the layout editor. Skipping...
        if (isInEditMode()) {
            return;
        }

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), getStyle(context, attrs, 0));
        setTypeface(typeface);
	}

	public RobotoCheckedTextView(Context context) {
		this(context, null);
	}

	public String getStyle(Context ctx, AttributeSet attrs, int defStyle) {

		if(attrs != null) {
			TypedArray styledAttrs = ctx.obtainStyledAttributes(attrs, R.styleable.RobotoCheckedTextView, 0, defStyle);
	        int style = styledAttrs.getInt(R.styleable.RobotoCheckedTextView_textStyle, 0);

	        styledAttrs.recycle();

	        switch(style) {
			case 7:
			case 6:
				return "Roboto-LightItalic.ttf";
			case 5:
			case 4:
				return "Roboto-Light.ttf";
			case 3:
				return "Roboto-BoldItalic.ttf";
			case 2:
				return "Roboto-Italic.ttf";
			case 1:
				return "Roboto-Bold.ttf";
			case 0:
			default:
				return "Roboto-Regular.ttf";
			}
		}else{
			return "Roboto-Regular.ttf";
		}
	}
}
