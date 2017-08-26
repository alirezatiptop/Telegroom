package org.telegram.Adel;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

import org.telegram.Adel.Olds.BaseFragmentAdapter;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.LaunchActivity;

public class SelectFontActivity extends BaseFragment
{
	public  ArrayList<LocaleController.LocaleInfo> searchResult;
	private BaseFragmentAdapter                    listAdapter;
	private ListView                               listView;
	private boolean                                searchWas;
	private boolean                                searching;
	private BaseFragmentAdapter                    searchListViewAdapter;
	private TextView                               emptyTextView;
	private Timer                                  searchTimer;
	// private ArrayList<String> listoffonts=new ArrayList<>();
	private HashMap<String, String>                Hashlist;
	private ArrayList<String>                      fontlisttitle;

	@Override
	public View createView(Context context)
	{
		searching = false;
		searchWas = false;
		Hashlist = FontHelper.LoadFonts();
		fontlisttitle = FontHelper.LoadFontsTitle();
		

		actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick()
		{
			@Override
			public void onItemClick(int id)
			{
				if (id == -1)
				{
					finishFragment();
				}
			}
		});

		listAdapter = new ListAdapter(context);
		fragmentView = new FrameLayout(context);

		LinearLayout emptyTextLayout = new LinearLayout(context);
		emptyTextLayout.setVisibility(View.INVISIBLE);
		emptyTextLayout.setOrientation(LinearLayout.VERTICAL);
		((FrameLayout) fragmentView).addView(emptyTextLayout);
		FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) emptyTextLayout.getLayoutParams();
		layoutParams.width = LayoutHelper.MATCH_PARENT;
		layoutParams.height = LayoutHelper.MATCH_PARENT;
		layoutParams.gravity = Gravity.TOP;
		emptyTextLayout.setLayoutParams(layoutParams);
		emptyTextLayout.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				return true;
			}
		});

		emptyTextView = new TextView(context);
		emptyTextView.setTextColor(0xff808080);
		emptyTextView.setTextSize(20);
		emptyTextView.setGravity(Gravity.CENTER);
		emptyTextView.setText(LocaleController.getString("NoResult", R.string.NoResult));
		emptyTextLayout.addView(emptyTextView);
		LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) emptyTextView.getLayoutParams();
		layoutParams1.width = LayoutHelper.MATCH_PARENT;
		layoutParams1.height = LayoutHelper.MATCH_PARENT;
		layoutParams1.weight = 0.5f;
		emptyTextView.setLayoutParams(layoutParams1);



		listView = new ListView(context);
		listView.setEmptyView(emptyTextLayout);
		listView.setVerticalScrollBarEnabled(false);
		listView.setDivider(null);
		listView.setDividerHeight(0);
		listView.setAdapter(listAdapter);
		((FrameLayout) fragmentView).addView(listView);
		layoutParams = (FrameLayout.LayoutParams) listView.getLayoutParams();
		layoutParams.width = LayoutHelper.MATCH_PARENT;
		layoutParams.height = LayoutHelper.MATCH_PARENT;
		listView.setLayoutParams(layoutParams);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				String value = Hashlist.get(fontlisttitle.get(i));
				
				mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
				System.exit(0);
			}
		});

		return fragmentView;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if (listAdapter != null)
		{
			listAdapter.notifyDataSetChanged();
		}
	}

	private class ListAdapter extends BaseFragmentAdapter
	{
		private Context mContext;

		public ListAdapter(Context context)
		{
			mContext = context;
		}
Setting.setCurrentFont(value);
				parentLayout.rebuildAllFragmentViews(false, true); // Adel Temp
				//  finishFragment();
				Intent        mStartActivity   = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
				int           mPendingIntentId = 123456;
				PendingIntent mPendingIntent   = PendingIntent.getActivity(ApplicationLoader.applicationContext, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
				AlarmManager  mgr              = (AlarmManager) ApplicationLoader.applicationContext.getSystemService(Context.ALARM_SERVICE);
		@Override
		public boolean areAllItemsEnabled()
		{
			return true;
		}

		@Override
		public boolean isEnabled(int i)
		{
			return true;
		}

		@Override
		public int getCount()
		{
			return fontlisttitle.size();
		}

		@Override
		public Object getItem(int i)
		{
			return null;
		}

		@Override
		public long getItemId(int i)
		{
			return i;
		}

		@Override
		public boolean hasStableIds()
		{
			return false;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup)
		{
			if (view == null)
			{
				view = new TextSettingsCell(mContext);
			}
			String key   = fontlisttitle.get(i);
			String value = Hashlist.get(key);
			((TextSettingsCell) view).setText(key, value == Setting.getCurrentFont());
//			((TextSettingsCell) view).setTypeFace(value);

			return view;
		}


		@Override
		public boolean isEmpty()
		{
			return false;
		}
	}
}
