package org.telegram.Adel;

import java.util.HashMap;

import org.telegram.messenger.R;

public class TabModel
{
	private String id;
	private int    title;
	private int    selectedicon;
	private int    unselectedicon;

	public TabModel(int id, int title, int unselectedicon, int selectedicon)
	{
		this.selectedicon = selectedicon;
		this.unselectedicon = unselectedicon;
	}

	public TabModel(String id, int title)
	{
		this.id = id;
		this.title = title;
		this.selectedicon = geticons().get(id);
		this.unselectedicon = geticons().get(id);
	}

	public static HashMap<String, Integer> getSelectedicons()
	{
		HashMap<String, Integer> icons = new HashMap<>();
		icons.put("favor", R.drawable.ic_star_white_24dp);
		icons.put("bot", R.mipmap.ic_tab_bot_blue);
		icons.put("unread", R.mipmap.ic_tab_timeline);
		icons.put("channel", R.mipmap.ic_tab_channel_blue);
		icons.put("sgroup", R.mipmap.ic_tab_supergroup_blue);
		icons.put("ngroup", R.mipmap.ic_tab_group_blue);
		icons.put("contact", R.mipmap.ic_tab_contact_blue);
		icons.put("all", R.drawable.ic_home_white_24dp);
		return icons;
	}

	public static HashMap<String, Integer> getwhiteicons()
	{
		HashMap<String, Integer> icons = new HashMap<>();
		icons.put("favor", R.drawable.ic_star_gray_24dp);
		icons.put("bot", R.drawable.ic_tab_bot_bluew);
		icons.put("unread", R.drawable.ic_tab_timelinew);
		icons.put("channel", R.drawable.ic_tab_channel_bluew);
		icons.put("sgroup", R.drawable.ic_tab_supergroup_bluew);
		icons.put("ngroup", R.drawable.ic_tab_group_bluew);
		icons.put("contact", R.drawable.ic_tab_contact_bluew);
		icons.put("all", R.drawable.ic_home_gray_24dp);
		return icons;
	}

	public static HashMap<String, Integer> getsmallicon()
	{
		HashMap<String, Integer> icons = new HashMap<>();
		icons.put("favor", R.drawable.ic_star_gray_24dp);
		icons.put("bot", R.drawable.ic_tab_bot_grays);
		icons.put("unread", R.drawable.ic_tab_timeline_greys);
		icons.put("channel", R.drawable.ic_tab_channel_grays);
		icons.put("sgroup", R.drawable.ic_tab_supergroup_grays);
		icons.put("ngroup", R.drawable.ic_tab_group_grays);
		icons.put("contact", R.drawable.ic_tab_contact_grays);
		icons.put("all", R.drawable.ic_home_gray_24dp);
		return icons;
	}

	public static int getSmallicon(int i)
	{
		return getsmallicon().get(i);
	}

	public static int getSmallwhiteicon(int i)
	{
		return getwhiteicons().get(i);
	}

	private HashMap<String, Integer> geticons()
	{
		HashMap<String, Integer> icons = new HashMap<>();
		icons.put("favor", R.drawable.ic_star_gray_24dp);
		icons.put("bot", R.mipmap.ic_tab_bot_gray);
		icons.put("unread", R.mipmap.ic_tab_timeline_grey);
		icons.put("channel", R.mipmap.ic_tab_channel_gray);
		icons.put("sgroup", R.mipmap.ic_tab_supergroup_gray);
		icons.put("ngroup", R.mipmap.ic_tab_group_gray);
		icons.put("contact", R.mipmap.ic_tab_contact_gray);
		icons.put("all", R.drawable.ic_home_gray_24dp);
		return icons;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public int getTitle()
	{
		return title;
	}

	public void setTitle(int title)
	{
		this.title = title;
	}

	public int getSelectedicon()
	{
		return selectedicon;
	}

	public void setSelectedicon(int selectedicon)
	{
		this.selectedicon = selectedicon;
	}

	public int getUnselectedicon()
	{
		return unselectedicon;
	}

	public void setUnselectedicon(int unselectedicon)
	{
		this.unselectedicon = unselectedicon;
	}
}
