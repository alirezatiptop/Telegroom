package org.telegram.Adel.NewAdd;

import org.telegram.Adel.Setting;
import org.telegram.messenger.MessagesController;

public class hideChannelController
{
	public static void add(Long id)
	{
				Setting.setChannelHideList(m);
	}

	public static void add(String user)
	{
		String m = Setting.getChannelHideList();
		m = m + "-" + String.valueOf(user);
		Setting.setChannelHideList(m);

		try
		{
			MessagesController.getInstance().sortDialogs(null); // Adel
		} catch (Exception ignored)
		{
			ignored.printStackTrace();
		}
	}

	public static void addBot(String user)
	{
		String m = Setting.getChannelHideList();
		m = m + "-" + String.valueOf(user);
		Setting.setChannelHideList(m);
	}

	public static void remove(String user)
	{
		String m = Setting.getChannelHideList();
		m = m.replace(user, "");
		Setting.setChannelHideList(m);
	}

	
	public static Boolean is(Long id)
	{
		
		boolean m = Setting.getChannelHideList().toLowerCase().contains(String.valueOf(id));
		return m;
	}
}
