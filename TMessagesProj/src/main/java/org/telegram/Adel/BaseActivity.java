package org.telegram.Adel;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.telegram.messenger.R;

public class BaseActivity extends Activity
{
	// Content Views
	public ProgressBar progressBar;
	public View        main_layout, load_layout;
	public TextView txtLoad;
	public Button   btnTryAgain;

	public void FindViews()
	{
		// Content
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		load_layout = findViewById(R.id.load_layout);
		main_layout = findViewById(R.id.main_layout);
		txtLoad = (TextView) findViewById(R.id.txtLoad);
		btnTryAgain = (Button) findViewById(R.id.btnTryAgain);
	}

	public void SetFonts(Typeface typeface)
	{
		// Content
		txtLoad.setTypeface(typeface);
		btnTryAgain.setTypeface(typeface);
	}

	public void ShowMainLayout()
	{
		load_layout.setVisibility(View.INVISIBLE);
		main_layout.setVisibility(View.VISIBLE);
	}

	public void ShowLoadLayout()
	{
		main_layout.setVisibility(View.INVISIBLE);
		load_layout.setVisibility(View.VISIBLE);

		progressBar.setVisibility(View.VISIBLE);
		txtLoad.setText("لطفا منتظر بمانید");
		btnTryAgain.setVisibility(View.GONE);
	}
}
