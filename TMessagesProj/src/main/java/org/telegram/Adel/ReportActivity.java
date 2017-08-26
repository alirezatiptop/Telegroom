package org.telegram.Adel;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.browser.Browser;

public class ReportActivity extends BaseActivity
{
	LinearLayout actionBar;
	ImageView    imgBack;
	TextView     txtTitle;

	TextView txt1, txt2, txt21, txt22, txt31, txt32, txt41, txt42;
	Button btnReport;

	// ------------------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);

		// Find Views
		FindViews();

		// Set Fonts
		SetFonts(AndroidUtilities.getTypeface(""));

		// Set Theme and title
		actionBar.setBackgroundColor(ThemeChanger.getcurrent().getActionbarcolor());
		txtTitle.setText(LocaleController.getString("ReportElimination", R.string.ReportElimination));
		txtTitle.setTypeface(AndroidUtilities.getTypeface(""));
	}

	@Override
	public void FindViews()
	{
		super.FindViews();

		actionBar = (LinearLayout) findViewById(R.id.actionBar);
		imgBack = (ImageView) findViewById(R.id.imgBack);
		txtTitle = (TextView) findViewById(R.id.txtTitle);

		txt1 = (TextView) findViewById(R.id.txt1);
		txt2 = (TextView) findViewById(R.id.txt2);
		txt21 = (TextView) findViewById(R.id.txt21);
		txt22 = (TextView) findViewById(R.id.txt22);
		txt31 = (TextView) findViewById(R.id.txt31);
		txt32 = (TextView) findViewById(R.id.txt32);
		txt41 = (TextView) findViewById(R.id.txt41);
		txt42 = (TextView) findViewById(R.id.txt42);
		btnReport = (Button) findViewById(R.id.btnReport);

		// Set imgBack onClickListener
		imgBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				finish();
			}
		});

		btnReport.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Browser.openUrl(ReportActivity.this, "https://t.me/spambot");
			}
		});
	}

	@Override
	public void SetFonts(Typeface typeface)
	{
		super.SetFonts(typeface);

		txt1.setTypeface(typeface);
		txt2.setTypeface(typeface);
		txt21.setTypeface(typeface);
		txt22.setTypeface(typeface);
		txt31.setTypeface(typeface);
		txt32.setTypeface(typeface);
		txt41.setTypeface(typeface);
		txt42.setTypeface(typeface);
		btnReport.setTypeface(typeface);
	}
}
