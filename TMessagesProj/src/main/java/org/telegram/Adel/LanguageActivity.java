package org.telegram.Adel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.support.widget.LinearLayoutManager;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.DrawerLayoutContainer;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.DrawerLayoutAdapter;

import org.telegram.ui.WallpapersActivity;

public class LanguageActivity extends Activity implements ActionBarLayout.ActionBarLayoutDelegate
{

	private static ArrayList<BaseFragment> mainFragmentsStack = new ArrayList<>();
	protected DrawerLayoutContainer                   drawerLayoutContainer;
	private   ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;
	private   ActionBarLayout                         actionBarLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		AndroidUtilities.checkDisplaySize(this, getResources().getConfiguration());
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setBackgroundDrawableResource(R.drawable.transparent);
		super.onCreate(savedInstanceState);

		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0)
		{
			AndroidUtilities.statusBarHeight = getResources().getDimensionPixelSize(resourceId);
		}

		actionBarLayout = new ActionBarLayout(this);
		drawerLayoutContainer = new DrawerLayoutContainer(this);

		setContentView(drawerLayoutContainer, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


//		Theme.loadWallpaper();

		drawerLayoutContainer.addView(new LinearLayout(this), LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

//		actionBarLayout.addFragmentToStack(new LanguageSelectActivity());

		try
		{
			String os1 = Build.DISPLAY;
			String os2 = Build.USER;
			if (os1 != null)
			{
				os1 = os1.toLowerCase();
			}
			else
			{
				os1 = "";
			}
			if (os2 != null)
			{
				os2 = os1.toLowerCase();
			}
			else
			{
				os2 = "";
			}
			if (os1.contains("flyme") || os2.contains("flyme"))
			{
				AndroidUtilities.incorrectDisplaySizeFix = true;
				final View view = getWindow().getDecorView().getRootView();
				view.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener()
				{
					@Override
					public void onGlobalLayout()
					{
						
		drawerLayoutContainer.addView(actionBarLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		RecyclerListView sideMenu = new RecyclerListView(this);
		sideMenu.setBackgroundColor(Theme.getColor(Theme.key_chats_menuBackground));
		sideMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
		sideMenu.setAdapter(new DrawerLayoutAdapter(this));
		drawerLayoutContainer.setDrawerLayout(sideMenu);

		FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) sideMenu.getLayoutParams();
		Point                    screenSize   = AndroidUtilities.getRealScreenSize();
		layoutParams.width = AndroidUtilities.isTablet() ? AndroidUtilities.dp(320) : Math.min(AndroidUtilities.dp(320), Math.min(screenSize.x, screenSize.y) - AndroidUtilities.dp(56));
		layoutParams.height = LayoutHelper.MATCH_PARENT;
		sideMenu.setLayoutParams(layoutParams);

		//				drawerLayoutContainer.setAllowOpenDrawer(false, false);
		drawerLayoutContainer.setParentActionBarLayout(actionBarLayout);

		actionBarLayout.setDrawerLayoutContainer(drawerLayoutContainer);
		actionBarLayout.init(mainFragmentsStack);
		actionBarLayout.setDelegate(this);
						int height = view.getMeasuredHeight();
						if (Build.VERSION.SDK_INT >= 21)
						{
							height -= AndroidUtilities.statusBarHeight;
						}
						if (height > AndroidUtilities.dp(100) && height < AndroidUtilities.displaySize.y && height + AndroidUtilities.dp(100) > AndroidUtilities.displaySize.y)
						{
							AndroidUtilities.displaySize.y = height;
							FileLog.e("fix display size y to " + AndroidUtilities.displaySize.y);
						}
					}
				});
			}
		} catch (Exception e)
		{
			FileLog.e(e);
		}

		presentFragment(new LanguageSelectActivity(), true, true);
	}

	public void presentFragment(BaseFragment fragment)
	{
		actionBarLayout.presentFragment(fragment);
	}

	public boolean presentFragment(final BaseFragment fragment, final boolean removeLast, boolean forceWithoutAnimation)
	{
		return actionBarLayout.presentFragment(fragment, removeLast, forceWithoutAnimation, true);
	}

	public ActionBarLayout getActionBarLayout()
	{
		return actionBarLayout;
	}

	@Override
	protected void onDestroy()
	{
		Theme.destroyResources();

		try
		{
			if (onGlobalLayoutListener != null)
			{
				final View view = getWindow().getDecorView().getRootView();
				if (Build.VERSION.SDK_INT < 16)
				{
					view.getViewTreeObserver().removeGlobalOnLayoutListener(onGlobalLayoutListener);
				}
				else
				{
					view.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
				}
			}
		} catch (Exception e)
		{
			FileLog.e(e);
		}
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		try
		{
			super.onSaveInstanceState(outState);
			BaseFragment lastFragment = null;

			if (!actionBarLayout.fragmentsStack.isEmpty())
			{
				lastFragment = actionBarLayout.fragmentsStack.get(actionBarLayout.fragmentsStack.size() - 1);
			}

			if (lastFragment != null)
			{
				Bundle args = lastFragment.getArguments();
				if (lastFragment instanceof ChatActivity && args != null)
				{
					outState.putBundle("args", args);
					outState.putString("fragment", "chat");
				}
				else if (lastFragment instanceof SettingsActivity)
				{
					outState.putString("fragment", "settings");
				}
				else if (lastFragment instanceof GroupCreateFinalActivity && args != null)
				{
					outState.putBundle("args", args);
					outState.putString("fragment", "group");
				}
				else if (lastFragment instanceof WallpapersActivity)
				{
					outState.putString("fragment", "wallpapers");
				}
				else if (lastFragment instanceof ProfileActivity && ((ProfileActivity) lastFragment).isChat() && args != null)
				{
					outState.putBundle("args", args);
					outState.putString("fragment", "chat_profile");
				}
				else if (lastFragment instanceof ChannelCreateActivity && args != null && args.getInt("step") == 0)
				{
					outState.putBundle("args", args);
					outState.putString("fragment", "channel");
				}
				else if (lastFragment instanceof ChannelEditActivity && args != null)
				{
					outState.putBundle("args", args);
					outState.putString("fragment", "edit");
								
								
		drawerLayoutContainer.addView(actionBarLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		RecyclerListView sideMenu = new RecyclerListView(this);
		sideMenu.setBackgroundColor(Theme.getColor(Theme.key_chats_menuBackground));
		sideMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
		sideMenu.setAdapter(new DrawerLayoutAdapter(this));
		drawerLayoutContainer.setDrawerLayout(sideMenu);

		FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) sideMenu.getLayoutParams();
		Point                    screenSize   = AndroidUtilities.getRealScreenSize();
		layoutParams.width = AndroidUtilities.isTablet() ? AndroidUtilities.dp(320) : Math.min(AndroidUtilities.dp(320), Math.min(screenSize.x, screenSize.y) - AndroidUtilities.dp(56));
		layoutParams.height = LayoutHelper.MATCH_PARENT;
		sideMenu.setLayoutParams(layoutParams);

		//				drawerLayoutContainer.setAllowOpenDrawer(false, false);
		drawerLayoutContainer.setParentActionBarLayout(actionBarLayout);

		actionBarLayout.setDrawerLayoutContainer(drawerLayoutContainer);
		actionBarLayout.init(mainFragmentsStack);
		actionBarLayout.setDelegate(this);
				}
				lastFragment.saveSelfArgs(outState);
			}
		} catch (Exception e)
		{
			FileLog.e(e);
		}
	}

	@Override
	public void onBackPressed()
	{
		//		if (drawerLayoutContainer.isDrawerOpened())
		//		{
		//			drawerLayoutContainer.closeDrawer(false);
		//		}
		//		else
		{
			actionBarLayout.onBackPressed();
		}
	}

	@Override
	public void onLowMemory()
	{
		super.onLowMemory();
		actionBarLayout.onLowMemory();
	}



	@Override
	public boolean onPreIme()
	{
		return false;
	}

	@Override
	public boolean needPresentFragment(BaseFragment fragment, boolean removeLast, boolean forceWithoutAnimation, ActionBarLayout layout)
	{
		return true;
	}

	@Override
	public boolean needAddFragmentToStack(BaseFragment fragment, ActionBarLayout layout)
	{
		return true;
	}

	@Override
	public boolean needCloseLastFragment(ActionBarLayout layout)
	{
		if (layout.fragmentsStack.size() <= 1)
		{
			finish();
			return false;
		}

		return true;
	}

	@Override
	public void onRebuildAllFragments(ActionBarLayout layout)
	{
		Intent intent = new Intent(this, LaunchActivity.class);
		startActivity(intent);
		finish();
	}
}
