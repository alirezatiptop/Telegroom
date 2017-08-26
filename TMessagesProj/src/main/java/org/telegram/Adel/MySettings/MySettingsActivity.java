package org.telegram.Adel.MySettings;

import android.content.Context;
import android.content.res.Configuration;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import org.telegram.Adel.BaseFragmentAdapter;
import org.telegram.Adel.FontHelper;
import org.telegram.Adel.GhostPorotocol;
import org.telegram.Adel.SelectFontActivity;
import org.telegram.Adel.Setting;
import org.telegram.Adel.ThemeChanger;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Cells.EmptyCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.DialogsActivity;

public class MySettingsActivity extends BaseFragment
{

	private ListView    listView;
	private ListAdapter listAdapter;

	private int rowCount;
	private int newgeramsectionrow;
	private int newgeramsectionrow2;
	private int Graphicsectionrow;
	private int Graphicsectionrow2;

	private int ghostactivate;
	private int sendDeliver;
	private int sendtyping;

	private int DefaultFont;
	private int drawStatus;

	private int TabisUpside;
	private int favor;
	private int bot;
	private int unread;
	private int channel;
	private int sgroup;
	private int ngroup;
	private int contact;
	private int all;

	@Override
	public boolean onFragmentCreate()
	{
		super.onFragmentCreate();

		rowCount = 0;
		newgeramsectionrow = rowCount++;
		newgeramsectionrow2 = rowCount++;

		ghostactivate = rowCount++;
		sendtyping = rowCount++;
		sendDeliver = rowCount++;
		Graphicsectionrow = rowCount++;
		Graphicsectionrow2 = rowCount++;
		DefaultFont = rowCount++;
		drawStatus = rowCount++;
		TabisUpside = rowCount++;
		contact = rowCount++; // Adel
		ngroup = rowCount++; // Adel
		channel = rowCount++; // Adel
		bot = rowCount++; // Adel
		favor = rowCount++; // Adel

		return true;
	}

	@Override
	public View createView(final Context context)
	{

		actionBar.setBackgroundColor(ThemeChanger.getcurrent().getActionbarcolor());
		actionBar.setItemsBackgroundColor(ThemeChanger.getcurrent().getActionbarcolor(), false);
		actionBar.setBackButtonImage(R.drawable.ic_ab_back);

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
		actionBar.setTitle(LocaleController.getString("MySettings", R.string.MySettings));
		if (AndroidUtilities.isTablet())
		{
			actionBar.setOccupyStatusBar(false);
		}

		listAdapter = new ListAdapter(context);

		fragmentView = new FrameLayout(context);
		FrameLayout frameLayout = (FrameLayout) fragmentView;

		listView = new ListView(context);
		listView.setDivider(null);
		listView.setDividerHeight(0);
		listView.setVerticalScrollBarEnabled(false);
		frameLayout.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP | Gravity.LEFT));
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l)
			{
				if (i == ghostactivate)
				{
					boolean send = Setting.getGhostMode();
					GhostPorotocol.toggleGhostPortocol();
					listAdapter.notifyDataSetChanged();
					if (view instanceof TextCheckCell)
					{
						((TextCheckCell) view).setChecked(!send);
					}
					parentLayout.rebuildAllFragmentViews(false);
				}
				else if (i == DefaultFont)
				{
					presentFragment(new SelectFontActivity());
				}
				else if (i == sendtyping)
				{
					boolean send = Setting.getSendTyping();
					Setting.setSendTyping(!send);
					if (view instanceof TextCheckCell)
					{
						((TextCheckCell) view).setChecked(!send);
					}
				}
				else if (i == TabisUpside)
				{
					boolean send = Setting.getTabIsUp();
					Setting.setTabIsUp(!send);
					if (view instanceof TextCheckCell)
					{
						((TextCheckCell) view).setChecked(!send);
					}
					parentLayout.rebuildAllFragmentViews(false);
				}
				else if (i == sendDeliver)
				{
					boolean send = Setting.getsendDeliver();
					Setting.setsendDeliver(!send);
					if (view instanceof TextCheckCell)
					{
						((TextCheckCell) view).setChecked(!send);
					}
				}
				else if (i == favor || i == unread || i == channel || i == sgroup || i == ngroup || i == contact || i == all || i == bot)
				{
					String tabname = "favor";
					if (i == unread)
					{
						tabname = "unread";
					}
					if (i == channel)
					{
						tabname = "channel";
					}
					if (i == sgroup)
					{
						tabname = "sgroup";
					}
					if (i == ngroup)
					{
						tabname = "ngroup";
					}
					if (i == bot)
					{
						tabname = "bot";
					}
					if (i == contact)
					{
						tabname = "contact";
					}
					if (i == all)
					{
						tabname = "all";
					}
					if (view instanceof TextCheckCell)
					{
						((TextCheckCell) view).setChecked(Setting.ToggleTab(tabname));
					}
//					DialogsActivity.RebuildTabs();
					parentLayout.rebuildAllFragmentViews(false);
				}
				else if (i == drawStatus)
				{
					Setting.setDrawStatus(!Setting.getDrawStatus());
					parentLayout.rebuildAllFragmentViews(false);
					listAdapter.notifyDataSetChanged();
				}
			}
		});

		frameLayout.addView(actionBar);
		needLayout();

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
		fixLayout();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		fixLayout();
	}

	private void needLayout()
	{
		FrameLayout.LayoutParams layoutParams;
		int                      newTop = (actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) + ActionBar.getCurrentActionBarHeight();
		if (listView != null)
		{
			layoutParams = (FrameLayout.LayoutParams) listView.getLayoutParams();
			if (layoutParams.topMargin != 0)
			{
				layoutParams.topMargin = 0;
				listView.setLayoutParams(layoutParams);
				//   extraHeightView.setTranslationY(newTop);
			}
		}
	}

	private void fixLayout()
	{
		if (fragmentView == null)
		{
			return;
		}
		fragmentView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
		{
			@Override
			public boolean onPreDraw()
			{
				if (fragmentView != null)
				{
					needLayout();
					fragmentView.getViewTreeObserver().removeOnPreDrawListener(this);
				}
				return true;
			}
		});
	}

	private class ListAdapter extends BaseFragmentAdapter
	{
		private Context mContext;

		public ListAdapter(Context context)
		{
			mContext = context;
		}

		@Override
		public boolean areAllItemsEnabled()
		{
			return false;
		}

		@Override
		public boolean isEnabled(int i)
		{
			return i == drawStatus || i == DefaultFont || i == TabisUpside || i == ghostactivate || i == sendtyping || i == sendDeliver || i == favor || i == bot || i == all || i == channel || i == contact || i == ngroup || i == sgroup || i == unread;
		}

		@Override
		public int getCount()
		{
			return rowCount;
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
			int type = getItemViewType(i);
			if (type == 0)
			{
				if (view == null)
				{
					view = new EmptyCell(mContext);
				}
			}
			else if (type == 1)
			{
				if (view == null)
				{
					view = new ShadowSectionCell(mContext);
				}
			}
			else if (type == 2)
			{
				if (view == null)
				{
					view = new TextSettingsCell(mContext);
				}
				TextSettingsCell textCell = (TextSettingsCell) view;
				if (i == DefaultFont)
				{
					textCell.setTextAndValue(LocaleController.getString("SelectFont", R.string.SelectFont), FontHelper.getFontTitle(Setting.getCurrentFont()), true);
				}
			}
			else if (type == 3)
			{
				if (view == null)
				{
					view = new TextCheckCell(mContext);
				}
				TextCheckCell textCell = (TextCheckCell) view;

				if (i == ghostactivate)
				{
					textCell.setTextAndCheck(LocaleController.getString("GhostMode", R.string.GhostMode), Setting.getGhostMode(), true);
				}
				else if (i == TabisUpside)
				{
					textCell.setTextAndCheck(LocaleController.getString("TabIsUp", R.string.TabIsUp), Setting.getTabIsUp(), true);
				}
				else if (i == sendtyping)
				{
					textCell.setTextAndCheck(LocaleController.getString("HideTypingState", R.string.HideTypingState), Setting.getSendTyping(), true);
				}
				else if (i == sendDeliver)
				{
					textCell.setTextAndCheck(LocaleController.getString("SendDeliverCheck", R.string.SendDeliverCheck), Setting.getsendDeliver(), true);
				}
				else if (i == favor)
				{
					textCell.setTextAndCheck(LocaleController.getString("Favorites", R.string.Favorites), Setting.TabisShowed("favor"), true);
				}
				else if (i == all)
				{
					textCell.setTextAndCheck(LocaleController.getString("AllChats", R.string.AllChats), Setting.TabisShowed("all"), true);
				}
				else if (i == channel)
				{
					textCell.setTextAndCheck(LocaleController.getString("Channels", R.string.Channels), Setting.TabisShowed("channel"), true);
				}
				else if (i == sgroup)
				{
					textCell.setTextAndCheck(LocaleController.getString("SuperGroups", R.string.SuperGroups), Setting.TabisShowed("sgroup"), true);
				}
				else if (i == ngroup)
				{
					textCell.setTextAndCheck(LocaleController.getString("Groups", R.string.Groups), Setting.TabisShowed("ngroup"), true);
				}
				else if (i == bot)
				{
					textCell.setTextAndCheck(LocaleController.getString("Bot", R.string.Bot), Setting.TabisShowed("bot"), true);
				}
				else if (i == contact)
				{
					textCell.setTextAndCheck(LocaleController.getString("Contacts", R.string.Contacts), Setting.TabisShowed("contact"), true);
				}
				else if (i == unread)
				{
					textCell.setTextAndCheck(LocaleController.getString("Unread", R.string.Unread), Setting.TabisShowed("unread"), true);
				}
				else if (i == drawStatus)
				{
					textCell.setTextAndCheck(LocaleController.getString("DrawStatus", R.string.DrawStatus), Setting.getDrawStatus(), true);
				}
			}
			else if (type == 4)
			{
				if (view == null)
				{
					view = new HeaderCell(mContext);
				}
				if (i == newgeramsectionrow2)
				{
					((HeaderCell) view).setText(LocaleController.getString("GhostProtocolSetting", R.string.GhostProtocolSetting));
				}
				else if (i == Graphicsectionrow2)
				{
					((HeaderCell) view).setText(LocaleController.getString("GraphicSetting", R.string.GraphicSetting));
				}
			}
			return view;
		}

		@Override
		public int getItemViewType(int i)
		{
			if (i == newgeramsectionrow || Graphicsectionrow == i)
			{
				return 1;
			}
			else if (drawStatus == i || TabisUpside == i || sendDeliver == i || ghostactivate == i || i == sendtyping || favor == i || bot == i || all == i || contact == i || sgroup == i || ngroup == i || unread == i || channel == i)
			{
				return 3;
			}
			else if (i == DefaultFont)
			{
				return 2;
			}
			else if (newgeramsectionrow2 == i || i == Graphicsectionrow2)
			{
				return 4;
			}
			else
			{
				return 2;
			}
		}

		@Override
		public int getViewTypeCount()
		{
			return 7;
		}

		@Override
		public boolean isEmpty()
		{
			return false;
		}
	}
}
