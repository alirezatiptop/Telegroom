package org.telegram.Adel;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.readystatesoftware.viewbadger.BadgeView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.tgnet.TLRPC;

import java.util.ArrayList;

public class TabSetting
{
	private static ArrayList<TabModel> l = new ArrayList<>();
	private static ArrayList<BadgeView> badges;
	private static boolean              justrunned;

	public static void GetTabs(TabLayout tabHost, ArrayList<BadgeView> newBadges)
	{
		if (newBadges != null)
		{
			badges = newBadges;
		}

		ArrayList<TabModel> l = getTabModels();
		for (int i = 0; i < l.size(); i++)
		{
			LayoutInflater inflater = (LayoutInflater) tabHost.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View           chatTab  = inflater.inflate(R.layout.tab_customview, null);
			ImageView      Img      = (ImageView) chatTab.findViewById(R.id.img);

			Img.setImageResource(l.get(i).getUnselectedIcon());
			Img.setScaleType(ImageView.ScaleType.FIT_CENTER);

			TabLayout.Tab t = tabHost.newTab().setCustomView(Img);
			tabHost.addTab(t, false);

			if (newBadges != null)
			{
				BadgeView badge = new BadgeView(tabHost.getContext(), tabHost.getTabAt(i).getCustomView());eView.POSITION_BOTTOM_RIGHT);
				badge.hide();
				badges.add(badge);
			}
		}
	}

	public static void setBadges(ArrayList<BadgeView> newBadges)
	{
		badges = newBadges;
		setBadges();
	}

	public static void setBadges()
	{
		try
		{
			if (justrunned)
			{
				return;
			}

			justrunned = true;

			ChangeBadge(GetTAbPostition("favor"), TabSetting.CountUnread(MessagesController.getInstance().dialogsFavoriteOnly));
			ChangeBadge(GetTAbPostition("all"), TabSetting.CountUnread(MessagesController.getInstance().dialogs));

			justrunned = false;
		} catch (Exception ignored)
		{
			justrunned = false;
		}
	}

	private static void ChangeBadge(int id, Unread unread)
	{
		if (id == -1)
		{
			return;
		}

		if (badges.size() > id)
		{
			badges.get(id).show();
			badges.get(id).setText(unread.Count > 999 ? "+999" : "" + unread.Count);

			if (unread.Count == 0) // hide
			{
				badges.get(id).hide();
			}
			else // show
			{
				if (unread.hasUnmute)
				{
					badges.get(id).setBadgeBackgroundColor(0xff4ECC5E);
				}
				else
				{
					badges.get(id).setBadgeBackgroundColor(Color.GRAY);
				}

				badges.get(id).setBackgroundDrawable(null);
				badges.get(id).show();
			}
		}
	}

	private static Unread CountUnread(ArrayList<TLRPC.TL_dialog> dialogs)
	{
		Unread unread = new Unread();
		for (int i = 0; i < dialogs.size(); i++)
		{
			TLRPC.TL_dialog dialog = dialogs.get(i);}
			}
		}

		return unread;
	}

	// ------------------------------------------------------------------------------
	private static int GetTAbPostition(String name)
	{
		ArrayList<TabModel> s = getTabModels();
		for (int i = 0; i < s.size(); i++)
		{
			if (s.get(i).getId().toLowerCase().equals(name.toLowerCase()))
			{
				return i;
			}
		}
		return -1;
	}

	public static void setTabIcon(TabLayout.Tab tab, int icon)
	{
		((ImageView) tab.getCustomView().findViewById(R.id.img)).setImageResource(icon);
	}

	public static int getUnselectedIcon(int id)
	{
		return l.get(id).getUnselectedIcon();
	}

	public static int getSelectedIcon(int id)
	{
		return l.get(id).getSelectedIcon();
	}

	public static ArrayList<TabModel> getTabModels()
	{
		l = new ArrayList<>();

		else // English
		{
			if (Setting.TabisShowed("all"))
			{
				l.add(new TabModel("all", R.string.AllChats));
			}
			if (Setting.TabisShowed("contact"))
			{
				l.add(new TabModel("contact", R.string.Contacts));
			}
			if (Setting.TabisShowed("bot"))
			{
				l.add(new TabModel("bot", R.string.Bot));
			}
			if (Setting.TabisShowed("favor"))
			{
				l.add(new TabModel("favor", R.string.Favorites));
			}
			//		if (Setting.TabisShowed("sgroup"))
			//		{
			//			l.add(new TabModel("sgroup", R.string.SuperGroups));
			//		}
		}

		return l;
	}

	private static class Unread
	{
		public int     Count;
		public boolean hasUnmute;

		public Unread()
		{
			Count = 0;
			hasUnmute = false;
		}
	}
}
