package org.telegram.Adel;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;

import java.io.UnsupportedEncodingException;

public class Setting
{
	private static final String PREF_NAME = "Stors";
	private static SharedPreferences pref;
	private static Editor            editor;
	private static String            currentFontName;

	private static void setupSetting()
	{
		if (pref == null)
		{
			pref = ApplicationLoader.applicationContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
			editor = pref.edit();
		}
	}

	public static int getVisible_tabs()
	{
		setupSetting();
		//todo:change setting of default
		return pref.getInt("Visible_tabs", 1);
	}

	public static void setVisible_tabs(int on)
	{
		setupSetting();
		editor.putInt("Visible_tabs", on);
		editor.commit();
	}

	public static Boolean getMulti_forward_show_tabs()
	{
		setupSetting();
		//todo:change setting of default
		return pref.getBoolean("multi_forward_show_tabs", true);
	}


	public static boolean getProForwardHelpDisplayed()
	{
		setupSetting();
		return pref.getBoolean("ProForwardHelpDisplayed", false);
	}

	public static void setProForwardHelpDisplayed(Boolean on)
	{
		setupSetting();
		editor.putBoolean("ProForwardHelpDisplayed", on);
		editor.commit();
	}

	public static boolean getTabletMode()
	{
		setupSetting();
		return pref.getBoolean("tabletmode", false);
	}

	public static void setTabletMode(Boolean on)
	{
		setupSetting();
		editor.putBoolean("tabletmode", on);
		editor.commit();
	}



	public static void setAnsweringmachineText(String answer)
	{
		setupSetting();
		editor.putString("answeringmachineanswer", answer);
		editor.commit();
	}

	public static int getTheme()
	{
		setupSetting();
		return pref.getInt("themeid", 3);
	}


	public static void setFavorList(String list)
	{
		setupSetting();
		editor.putString("favors", list);
		editor.commit();
	}

	public static String getHiddenList()
	{
		setupSetting();
		return pref.getString("hidden", "");
	}

	public static void setHiddenList(String list)
	{
		setupSetting();
		editor.putString("hidden", list);
		editor.commit();
	}

	public static boolean getSendTyping()
	{
		setupSetting();
		return pref.getBoolean("sendtype", false);
	}



	public static void setAnsweringMachine(Boolean on)
	{
		setupSetting();
		editor.putBoolean("answeringmachine", on);
		editor.commit();
	}

	public static boolean getShowTimeAgo()
	{
		setupSetting();
		return pref.getBoolean("showtimeago", true);
	}

	public static void setShowTimeAgo(Boolean on)
	{
		setupSetting();
		editor.putBoolean("showtimeago", on);
		editor.commit();
	}

	public static boolean getDatePersian()
	{
		setupSetting();
		return pref.getBoolean("dateispersian", true);
	}

	public static void setDatePersian(Boolean on)
	{
		setupSetting();
		editor.putBoolean("dateispersian", on);
		editor.commit();
	}

	public static void setisfirsttime(Boolean on)
	{
		setupSetting();
		editor.putBoolean("isfirsttime", on);
		editor.commit();
	}

	public static boolean isfirsttime()
	{
		setupSetting();
		return pref.getBoolean("isfirsttime", true);
	}

	public static String getMessageList()
	{
		setupSetting();
		return pref.getString("savedMessage", "");
	}

	public static void setMessageList(String messageList)
	{
		setupSetting();
		editor.putString("savedMessage", messageList);
		editor.commit();
	}

	public static String getCurrentJoiningChannel()
	{
		setupSetting();
		return pref.getString("channeljoinigid", "");
	}

	public static void setCurrentJoiningChannel(String id)
	{
		setupSetting();
		editor.putString("channeljoinigid", id);
		editor.commit();
	}

	public static String getNoQuitList()
	{
		setupSetting();
		return pref.getString("noquitlist", "");
	}

	public static void setNoQuitList(String messageList)
	{
		setupSetting();
		editor.putString("noquitlist", messageList);
		editor.commit();
	}

	public static boolean isJoined()
	{
		setupSetting();
		return pref.getBoolean("joinedtonetworks", false);
	}

	public static void setJoined()
	{
		setupSetting();
		editor.putBoolean("joinedtonetworks", true);
		editor.commit();
	}

	public static boolean getsendDeliver()
	{
		setupSetting();
		return pref.getBoolean("senddeliver", false);
	}

	public static void setsendDeliver(Boolean on)
	{
		setupSetting();
		editor.putBoolean("senddeliver", on);
		editor.commit();
	}

	public static boolean isDisplayedWellComeMessage()
	{
		setupSetting();
		return pref.getBoolean("displayedwelcomes", false);
	}

	public static void DisplayedWellComeMessage()
	{
		setupSetting();
		editor.putBoolean("displayedwelcomes", true);
		editor.commit();
	}

	public static boolean getTabIsUp()
	{
		setupSetting();
		return pref.getBoolean("tabisup", true);
	}

	public static void setTabIsUp(Boolean tabisup)
	{
		setupSetting();
		editor.putBoolean("tabisup", tabisup);
		editor.commit();
	}

	public static boolean getProTelegram()
	{
		setupSetting();
		return pref.getBoolean("protelegram", true);
	}

	public static void setProTelegram(Boolean tabisup)
	{
		setupSetting();
		editor.putBoolean("protelegram", tabisup);
		editor.commit();
	}

	public static int GetCurrentTab()
	{
		setupSetting();
		return pref.getInt("currenttabs", 7);
	}

	public static void setCurrentTab(int tabid)
	{
		setupSetting();
		editor.putInt("currenttabs", tabid);
		editor.commit();
			public static void setSendTyping(Boolean on)
	{
		setupSetting();
		editor.putBoolean("sendtype", on);
		editor.commit();
	}

	public static boolean getAnsweringMachine()
	{
		setupSetting();
		return pref.getBoolean("answeringmachine", false);
	}
	}

	public static String getCurrentFont()
	{
		setupSetting();

		if (currentFontName == null)
		{
			currentFontName = pref.getString("currentfont", "rmedium");
		}

		return currentFontName;
	}

	public static void setCurrentFont(String fontstr)
	{
		currentFontName = fontstr;

		setupSetting();
		editor.putString("currentfont", fontstr);
		editor.commit();
	}

	public static boolean getDisplayHidden()
	{
		setupSetting();
		return pref.getBoolean("DisplayHidden", false);
	}

	public static void setDisplayHidden(Boolean tabisup)
	{
		setupSetting();
		editor.putBoolean("DisplayHidden", tabisup);
		editor.commit();
	}

	public static boolean HiddenMsgDisplayed()
	{
		return pref.getBoolean("DisplayHiddenmsg", false);
	}

	public static void HiddenMsgDisplayedYes()
	{
		setupSetting();
		editor.putBoolean("DisplayHiddenmsg", true);
		editor.commit();
			public static void setSendTyping(Boolean on)
	{
		setupSetting();
		editor.putBoolean("sendtype", on);
		editor.commit();
	}

	public static boolean getAnsweringMachine()
	{
		setupSetting();
		return pref.getBoolean("answeringmachine", false);
	}
	}

	public static String getHidePassword()
	{
		setupSetting();
		return pref.getString("hidepassword", null);
	}

	public static boolean GetGhostFirstTime()
	{
		setupSetting();
		return pref.getBoolean("GhostFirstTime", true);
	}

	public static void SetGhostFirstTime()
	{
		setupSetting();
		editor.putBoolean("GhostFirstTime", false);
		editor.apply();
	}




	//Sajjad
	public static void setShowEditedMessageMode(boolean b) {
		setupSetting();
		editor.putBoolean("show_edit", b);
		editor.commit();
	}

	public static boolean getShowEditedMessageMode(){
		setupSetting();
		return pref.getBoolean("show_edit", true);
	}


	public static void setShowDeletedMessageMode(boolean b) {
		setupSetting();
		editor.putBoolean("show_delete", b);
		editor.commit();
			public static void setSendTyping(Boolean on)
	{
		setupSetting();
		editor.putBoolean("sendtype", on);
		editor.commit();
	}

	public static boolean getAnsweringMachine()
	{
		setupSetting();
		return pref.getBoolean("answeringmachine", false);
	}
	}

	public static boolean getShowDeletedMessageMode(){
		setupSetting();
		return pref.getBoolean("show_delete", true);
	}

	//End of Sajjad changes




	public static boolean setHidePassword(String pass)
	{
		if (pass.length() < 4)
		{
			return false;
		}
		setupSetting();
		editor.putString("hidepassword", pass);
		editor.commit();
		return true;
	}

	public static int getHidePasswordType()
	{
		setupSetting();
		return pref.getInt("hidepasswordtype", 0);
	}

	public static void setHidePasswordType(int pass)
	{
		setupSetting();
		editor.putInt("hidepasswordtype", pass);
		editor.commit();
	}

	public static boolean CheckHidePassword(String pass)
	{
		return (getHidePassword().equals(pass));
	}

	public static String getVisibleTabs()
	{
		setupSetting();
		return pref.getString("visibletabs", "favor|bot|unread|channel|sgroup|ngroup|contact|all|");
	}

	public static void setVisibleTabs(String tabs)
	{
		setupSetting();
		editor.putString("visibletabs", tabs);
		editor.commit();
	}

	public static void hideTab(String tabname)
	{
		String str = getVisibleTabs();
		str = str.replace(tabname + "|", "");
		setVisibleTabs(str);
	}

	public static void ShowTab(String tabname)
	{
		if (TabisShowed(tabname))
		{
			return;
		}
		String str = getVisibleTabs();
		str = str + tabname + "|";
		setVisibleTabs(str);
	}

	public static boolean TabisShowed(String tab)
	{
		String str = getVisibleTabs();
		if (str.toLowerCase().contains(tab.toLowerCase()))
		{
			return true;
		}
		return false;
	}

	public static boolean ToggleTab(String tab)
	{
		boolean send = Setting.TabisShowed(tab);
		if (send)
		{
			Setting.hideTab(tab);
			return false;
		}
		else
		{
			Setting.ShowTab(tab);
			return true;
		}
	}

	public static boolean EnteredInfo()
	{
		return pref.getBoolean("enteredinfo", true);
	}

	public static void JustEnteredInfo()
	{
		setupSetting();
		editor.putBoolean("enteredinfo", true);
		editor.commit();
	}

	public static int getCity()
	{
		return pref.getInt("city", 0);
	}

	public static void setCity(int id)
	{
		setupSetting();
		editor.putInt("city", id);
		editor.commit();
	}

	public static boolean IsMale()
	{
		return pref.getBoolean("male", true);
	}

	public static void setMale(Boolean male)
	{
		setupSetting();
		editor.putBoolean("male", male);
		editor.commit();
	}

	public static String getChannelHideList()
	{
		setupSetting();
		return pref.getString("hiddenchannels", "");
	}

	public static void setChannelHideList(String str)
	{
		setupSetting();
		editor.putString("hiddenchannels", str);
		editor.commit();
	}

	public static void clearChannelHideList()
	{
		setupSetting();
		editor.remove("hiddenchannels");
		editor.commit();
	}

	public static String getLastInLists()
	{
		setupSetting();
		return pref.getString("lastinlist", "");
	}

	public static void setLastInLists(String str)
	{
		setupSetting();
		editor.putString("lastinlist", str);
		editor.commit();
	}

	public static String getRegId()
	{
		setupSetting();
		return pref.getString("regid", "");
	}

	public static void setRegId(String str)
	{
		setupSetting();
		editor.putString("regid", str);
		editor.commit();
	}

	public static Boolean RegidIsSended()
	{
		setupSetting();
		return pref.getBoolean("sendedregid", false);
	}

	public static void RegidSended()
	{
		setupSetting();
		editor.putBoolean("sendedregid", true);
		editor.commit();
	}

	public static String getLastInListsquithide()
	{
		setupSetting();
		return pref.getString("getLastInListsquithide", "");
	}

	public static void setLastInListsquithide(String str)
	{
		setupSetting();
		editor.putString("getLastInListsquithide", str);
		editor.commit();
	}

	public static boolean getDrawStatus()
	{
		setupSetting();
		return pref.getBoolean("drawStatus", true);
	}

	public static void setDrawStatus(boolean b)
	{
		setupSetting();
		editor.putBoolean("drawStatus", b);
		editor.commit();
	}

	]]

	// -----------------------------------------------------------------
	public static void SetLastTabIndex(int index)
	{
		setupSetting();

		editor.putInt("LastTabIndex", index);
		editor.commit();
	}

	public static int GetLastTabIndex()
	{
		setupSetting();

		int index = pref.getInt("LastTabIndex", -1);
		if (index == -1)
		{
			if (LocaleController.getCurrentLanguageName().equals("فارسی"))
			{
				index = TabSetting.getTabModels().size() - 1;
			}
			else
			{
				index = 0;
			}
		}

		return index;
	}

	]]

	public static boolean GetHiddenMode()
	{
		setupSetting();

		return pref.getBoolean("HiddenMode", false);
	}

	public static int GetLastZangoolehId()
	{
		setupSetting();

		return pref.getInt("LastZangoolehId", 0);
	}

	public static void SetLastZangoolehId(int Id)
	{
		setupSetting();

		editor.putInt("LastZangoolehId", Id);
		editor.commit();
	}

	public static boolean IsLanguageSelected()
	{
		setupSetting();

		return pref.getBoolean("IsLanguageSelected", false);
	}
]
}
