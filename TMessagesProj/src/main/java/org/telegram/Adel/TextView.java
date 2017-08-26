package org.telegram.Adel;

import android.content.Context;
import android.util.AttributeSet;

import org.telegram.messenger.AndroidUtilities;

public class TextView extends android.support.v7.widget.AppCompatTextView
{
	public TextView(Context context)
	{
		super(context);
		setTypeface(AndroidUtilities.getTypeface(null));
	}

	public TextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setTypeface(AndroidUtilities.getTypeface(null));
	}
}
