package org.telegram.Adel;

import android.graphics.Color;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.telegram.messenger.ApplicationLoader;

public class ThemeHelper
{
	private static String loadJSONFromAsset()
	{
		String json = null;
		try
		{
			InputStream is     = ApplicationLoader.applicationContext.getAssets().open("color.json");
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

	public static ArrayList<Theme> getThemes()
	{
		ArrayList<Theme> themes = new ArrayList<>();
		try
		{
			JSONArray jary = new JSONArray(loadJSONFromAsset());
			for (int i = 0; i <= jary.length(); i++)
			{
				JSONObject jb = jary.getJSONObject(i);
				themes.add(new Theme(jb.getInt("id"), jb.getString("name"), Color.parseColor(jb.getString("actionbar")), Color.parseColor(jb.getString("tab"))));
			}
		} catch (JSONException e)
		{
			e.printStackTrace();
		}

		return themes;
	}
}
