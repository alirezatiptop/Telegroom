package org.telegram.Adel;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;

public class CommentActivity extends BaseActivity
		implements INewComment
{
	LinearLayout actionBar;
	ImageView    imgBack;
	TextView     txtTitle;

	TextView txt1, txt2;
	EditText edtComment;
	Button btn1;

	// ------------------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);

		// Find Views
		FindViews();

		// Set Fonts
		SetFonts(AndroidUtilities.getTypeface(""));

		// Set Theme and title
		actionBar.setBackgroundColor(ThemeChanger.getcurrent().getActionbarcolor());
		txtTitle.setText(LocaleController.getString("AppName", R.string.AppName));
		txtTitle.setTypeface(AndroidUtilities.getTypeface(""));

		Initialize();
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
		edtComment = (EditText) findViewById(R.id.edtComment);
		btn1 = (Button) findViewById(R.id.btn1);

		// Set imgBack onClickListener
		imgBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				finish();
			}
		});

		// Set btn1 onClickListener
		btn1.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				String comment = edtComment.getText().toString();

				if (comment.length() > 4)
				{
					NewComment(comment);
				}
				else
				{
					finish();
				}
			}
		});
	}

	@Override
	public void SetFonts(Typeface typeface)
	{
		super.SetFonts(typeface);

		txt1.setTypeface(typeface);
		txt2.setTypeface(typeface);
		edtComment.setTypeface(typeface);
		btn1.setTypeface(typeface);
	}

	private void Initialize()
	{
		if (LocaleController.getInstance().getCurrentLanguageName().equals("فارسی"))
		{
			txt1.setGravity(Gravity.RIGHT);
			txt2.setGravity(Gravity.RIGHT);
			edtComment.setGravity(Gravity.RIGHT);
		}
		else
		{
			txt1.setGravity(Gravity.LEFT);
			txt2.setGravity(Gravity.LEFT);
			edtComment.setGravity(Gravity.LEFT);
		}

		txt1.setText(LocaleController.getString("Comment", R.string.Comment));
		txt2.setText(LocaleController.getString("CommentText1", R.string.CommentText1));
		btn1.setText(LocaleController.getString("Comment", R.string.Comment));
		edtComment.setHint(LocaleController.getString("Type", R.string.Type));
	}

	// ------------------------------------------------------------
	public void NewComment(String comment)
	{
		ShowLoadLayout();

		new WebService(this).execute("NewComment", comment);
	}

	@Override
	public void onNewCommentCompleted(final String response)
	{
		if (response.equals(BaseApplication.EXCEPTION))
		{
			progressBar.setVisibility(View.GONE);
			txtLoad.setText("مشکلی پیش اومد، لطفا مجددا تلاش کنید");
			btnTryAgain.setText("تلاش مجدد");
			btnTryAgain.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					ShowMainLayout();
				}
			});
			btnTryAgain.setVisibility(View.VISIBLE);
		}
		else if (response.equals(BaseApplication.ANDROID_EXCEPTION))
		{
			progressBar.setVisibility(View.GONE);
			txtLoad.setText("ارتباط با سرور برقرار نشد، تنظیمات اینترنت را بررسی کنید");
			btnTryAgain.setText("تلاش مجدد");
			btnTryAgain.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					ShowMainLayout();
				}
			});
			btnTryAgain.setVisibility(View.VISIBLE);
		}
		else if (response.equals(BaseApplication.OK))
		{
			finish();
		}
	}
}
