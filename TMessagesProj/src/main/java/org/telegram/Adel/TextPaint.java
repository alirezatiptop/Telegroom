package org.telegram.Adel;

import android.graphics.Paint;

import org.telegram.messenger.AndroidUtilities;

public class TextPaint extends android.text.TextPaint
{
	public TextPaint()
	{
		super();
		setTypeface(AndroidUtilities.getTypeface(null));
	}

	public TextPaint(int flags)
	{
		super(flags);
		setTypeface(AndroidUtilities.getTypeface(null));
	}

	public TextPaint(Paint p)
	{
		super(p);
		setTypeface(AndroidUtilities.getTypeface(null));
	}
}
