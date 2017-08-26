package org.telegram.Adel;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public final class BaseApplication
{
	// WS fields
	public static final String OK                = "OK";
	public static final String EXCEPTION         = "EXCEPTION";
	public static final String NOT_EXIST         = "NOT_EXIST";
	public static final String EXIST             = "EXIST";
	public static final String WRONG_USER_PASS   = "WRONG_USER_PASS";
	public static final String ANDROID_EXCEPTION = "ANDROID_EXCEPTION";

	public static void CloseKeyboard(Activity activity)
	{
		// Check if no view has focus
		View view = activity.getCurrentFocus();
		if (view != null)
		{
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
}
