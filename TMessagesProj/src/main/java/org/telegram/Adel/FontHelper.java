package org.telegram.Adel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.telegram.messenger.ApplicationLoader;

public class FontHelper
{
	private static String loadJSONFromAsset()
	{
		String json = null;
		try
		{
			InputStream is     = ApplicationLoader.applicationContext.getAssets().open("fonts/fonts.json");
			int         size   = is.available();
			byte[]      buffer = new byte[size];
			is.read(buffer);
			is.close();
			json = new String(buffer, "UTF-8");
		} catch (IOException ex)
		{
			ex.printStackTrace();
			return null;
		}
		return json;
	}

	public static ArrayList<String> LoadFontsTitle()
	{
		ArrayList<String> m_li = new ArrayList<String>();
		try
		{
			JSONArray m_jArry = new JSONArray(loadJSONFromAsset());
			for (int i = 0; i < m_jArry.length(); i++)

		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		return m_li;
	}

	public static HashMap<String, String> LoadFonts()
	{
		HashMap<String, String> m_li = new HashMap<>();
		try
		{
			JSONArray m_jArry = new JSONArray(loadJSONFromAsset());
			for (int i = 0; i < m_jArry.length(); i++)
			{
				JSONObject jo_inside = m_jArry.getJSONObject(i);

			}
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		return m_li;
	}

	public static String getFontTitle(String value)
	{
		HashMap<String, String> f = LoadFonts();
		for (String o : LoadFonts().keySet())
		{
			if (f.get(o).equals(value))
			{
				return o;
			}
		}
		return null;
	}
}
