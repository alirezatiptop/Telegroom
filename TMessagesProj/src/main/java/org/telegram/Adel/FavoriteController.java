package org.telegram.Adel;

public class FavoriteController
{
	public static void addToFavor(Long id)
	{
		String m = Setting.getFavorList();
		m = m + "-" + String.valueOf(id);
		Setting.setFavorList(m);
	}

	public static void addToFavor(String user)
	{
		String m = Setting.getFavorList();
		m = m + "-" + String.valueOf(user);
		Setting.setFavorList(m);
	}

	public static Boolean isFavor(String user)
	{
		return Setting.getFavorList().toLowerCase().contains(user);
	}

	public static Boolean isFavor(Long id)
	{
		return Setting.getFavorList().toLowerCase().contains(String.valueOf(id));
	}

	public static boolean IsFaver(Long aLong)
	{
		return isFavor(aLong);
	}

	public static void RemoveFromFavor(long selectedDialog)
	{
		String m = Setting.getFavorList();
		m = m.replace(String.valueOf(selectedDialog), "");
		Setting.setFavorList(m);
	}
}
