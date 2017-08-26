package org.telegram.Adel;

import android.graphics.drawable.Drawable;

public class Theme
{
	public static final String NORMAL   = "normal";
	public static final String BLUE     = "blue";
	public static final String GREY     = "grey";
	public static final String SELECTED = "selected";
	private int      id;
	private String   name;
	private int      actionbarcolor;
	private int      tabLayoutColor;
	private Drawable Floatingbuttondrawble;

	public Theme(int id, String name, int actionbarcolor, int tabLayoutColor)
	{
		this.id = id;
		this.name = name;
		this.actionbarcolor = actionbarcolor;
		this.tabLayoutColor = tabLayoutColor;
		Floatingbuttondrawble = ThemeChanger.getFloating(actionbarcolor);

	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getActionbarcolor()
	{
		return actionbarcolor;
	}

	public void setActionbarcolor(int actionbarcolor)
	{
		this.actionbarcolor = actionbarcolor;
	}

	public int getTabLayoutColor()
	{
		return tabLayoutColor;
	}

	public void setTabLayoutColor(int tabLayoutColor)
	{
		this.tabLayoutColor = tabLayoutColor;
	}

	public Drawable getFloatingbuttondrawble()
	{
		return Floatingbuttondrawble;
	}

	public void setFloatingbuttondrawble(Drawable floatingbuttondrawble)
	{
		Floatingbuttondrawble = floatingbuttondrawble;
	}

}
