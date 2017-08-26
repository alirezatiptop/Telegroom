package org.telegram.Adel.Olds;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import org.telegram.Adel.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import org.telegram.Adel.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.R;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.LayoutHelper;

public class DeleteBySelectContacts extends BaseFragment implements NotificationCenterDelegate
{
	private static final int                 check_all   = 2;
	private static final int                 done_button = 1;
	private              ArrayList<ChipSpan> allSpans    = new ArrayList();
	private int          beforeChangeIndex;
	private CharSequence changeString;
	private int chatType = 0;
	private GroupCreateActivityDelegate delegate;
	private TextView                    emptyTextView;
	private boolean                     ignoreChange;
	private boolean                     isAlwaysShare;
	private boolean isDelete = true;
	private boolean                isNeverShare;
	private LetterSectionsListView listView;
	private ContactsAdapter        listViewAdapter;
	private SearchAdapter          searchListViewAdapter;
	private boolean                searchWas;
	private boolean                searching;
	private HashMap<Integer, ChipSpan> selectedContacts = new HashMap();
	private EditText userSelectEditText;

	public DeleteBySelectContacts(Bundle args)
	{
		super(args);
		chatType = args.getInt("chatType", 0);
		isAlwaysShare = args.getBoolean("isAlwaysShare", false);
		isNeverShare = args.getBoolean("isNeverShare", false);
	}

	public boolean onFragmentCreate()
	{
		NotificationCenter.getInstance().addObserver(this, NotificationCenter.contactsDidLoaded);
		NotificationCenter.getInstance().addObserver(this, NotificationCenter.updateInterfaces);
		NotificationCenter.getInstance().addObserver(this, NotificationCenter.chatDidCreated);
		return super.onFragmentCreate();
	}

	public void onFragmentDestroy()
	{
		super.onFragmentDestroy();
		NotificationCenter.getInstance().removeObserver(this, NotificationCenter.contactsDidLoaded);
		NotificationCenter.getInstance().removeObserver(this, NotificationCenter.updateInterfaces);
		NotificationCenter.getInstance().removeObserver(this, NotificationCenter.chatDidCreated);
	}

	public View createView(final Context context)
	{
		searching = false;
		searchWas = false;

	
		actionBar.setActionBarMenuOnItemClick(new ActionBarMenuOnItemClick()
		{
			public void onItemClick(int id)
			{
				if (id == -1)
				{
					finishFragment();
				}
				else if (id == 1)
				{
					if (!selectedContacts.isEmpty())
					{
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setTitle(LocaleController.getString("DeleteContacts", R.string.DeleteContacts));
						builder.setMessage(LocaleController.getString("DeleteContactsMessage", R.string.DeleteContactsMessage) + " (" + selectedContacts.size() + ")");
						builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener()
						builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialogInterface, int i)
							{
								dialogInterface.dismiss();
							}
						});
						builder.create().show();
					}
				}
				else if (id == 2)
				{
					selectAll();
				}
			}
		});

		ActionBarMenu menu = actionBar.createMenu();
		menu.addItemWithWidth(2, R.drawable.ic_done_all_white_24dp, AndroidUtilities.dp(56.0f)); // Adel
		menu.addItemWithWidth(1, R.drawable.ic_done, AndroidUtilities.dp(56.0f));

		searchListViewAdapter = new SearchAdapter(context, null, false, false, false, false);
		searchListViewAdapter.setCheckedMap(selectedContacts);
		searchListViewAdapter.setUseUserCell(true);
		listViewAdapter = new ContactsAdapter(context, 1, false, null, false);
		listViewAdapter.setCheckedMap(selectedContacts);
		fragmentView = new LinearLayout(context);
		LinearLayout linearLayout = (LinearLayout) fragmentView;
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		AndroidUtilities.clearCursorDrawable(userSelectEditText);
		frameLayout.addView(userSelectEditText, LayoutHelper.createFrame(-1, -2.0f, 51, 10.0f, 0.0f, 10.0f, 0.0f));
		userSelectEditText.setHint(LocaleController.getString("DeleteContactPlaceholder", R.string.DeleteContactPlaceholder));
		if (VERSION.SDK_INT >= 11)
		{
			userSelectEditText.setTextIsSelectable(false);
		}
		userSelectEditText.addTextChangedListener(new TextWatcher()
		{
			public void beforeTextChanged(CharSequence charSequence, int start, int count, int after)
			{
				if (!ignoreChange)
				{
					beforeChangeIndex = userSelectEditText.getSelectionStart();
					changeString = new SpannableString(charSequence);
				}
			}

			public void onTextChanged(CharSequence charSequence, int i, int i2, int i3)
			{
			}

			public void afterTextChanged(Editable editable)
			{
				if (!ignoreChange)
				{
					boolean search           = false;
					int     afterChangeIndex = userSelectEditText.getSelectionEnd();
					if (editable.toString().length() < changeString.toString().length())
					{
						String deletedString = "";
					
						FileLog.e("tmessages", e);
	
						if (deletedString.length() > 0)
						{
							if (searching && searchWas)
							{
								search = true;
							}
							Spannable span = userSelectEditText.getText();
							for (int a = 0; a < allSpans.size(); a++)
							{
								ChipSpan sp = (ChipSpan) allSpans.get(a);
								if (span.getSpanStart(sp) == -1)
								{
									allSpans.remove(sp);
									selectedContacts.remove(Integer.valueOf(sp.uid));
								}
							}
							if (isAlwaysShare || !isNeverShare)
							{
								listView.invalidateViews();
							}
							else
							{
								listView.invalidateViews();
							}
						}
						else
						{
							search = true;
						}
					}
					else
					{
						search = true;
					}
			
				}
			}
		});
		LinearLayout emptyTextLayout = new LinearLayout(context);
		emptyTextLayout.setVisibility(4);
		emptyTextLayout.setOrientation(1);
		linearLayout.addView(emptyTextLayout, LayoutHelper.createLinear(-1, -1));
		emptyTextLayout.setOnTouchListener(new OnTouchListener()
		{
			public boolean onTouch(View v, MotionEvent event)
			{
				return true;
			}
		});
		emptyTextView = new TextView(context);
		emptyTextView.setTextColor(-8355712);
//		emptyTextView.setTypeface(AndroidUtilities.getTypeface(null)); // Adel
		emptyTextView.setTextSize(20.0f);
		emptyTextView.setGravity(17);
		emptyTextView.setText(LocaleController.getString("NoContacts", R.string.NoContacts));
		emptyTextLayout.addView(emptyTextView, LayoutHelper.createLinear(-1, -1, 0.5f));
		emptyTextLayout.addView(new FrameLayout(context), LayoutHelper.createLinear(-1, -1, 0.5f));
		listView.setAdapter(listViewAdapter);
		if (VERSION.SDK_INT >= 11)
		{
			listView.setFastScrollAlwaysVisible(true);
			listView.setVerticalScrollbarPosition(LocaleController.isRTL ? 1 : 2);
		}
		linearLayout.addView(listView, LayoutHelper.createLinear(-1, -1));
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
			{
				User user;
				if (user != null)
				{
					boolean check = true;
					if (selectedContacts.containsKey(Integer.valueOf(user.id)))
					{
						check = false;
						try
						{
							userSelectEditText.setSelection(text.length());
							ignoreChange = false;
						} catch (Exception e)
						{
							FileLog.e("tmessages", e);
						}
					}
					else if (chatType == 0 && selectedContacts.size() == MessagesController.getInstance().maxGroupCount - 1)
					{
						Builder builder = new Builder(getParentActivity());
				
						showDialog(builder.create());
						return;
					}
					else
					{
						ignoreChange = true;
						createAndPutChipForUser(user).uid = user.id;
						ignoreChange = false;
					}
					if (isAlwaysShare || !isNeverShare)
					{
					}
				
					else if (view instanceof UserCell)
					{
						((UserCell) view).setChecked(check, true);
					}
				}
			}
		});
		listView.setOnScrollListener(new OnScrollListener()
		{
			public void onScrollStateChanged(AbsListView absListView, int i)
			{
				boolean z = true;
				if (i == 1)
				{
					AndroidUtilities.hideKeyboard(userSelectEditText);
				}
				if (listViewAdapter != null)
				{
					ContactsAdapter access$1600 = listViewAdapter;
					if (i == 0)
					{
						z = false;
					}
					access$1600.setIsScrolling(z);
				}
			}

			public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{
				if (absListView.isFastScrollEnabled())
				{
					AndroidUtilities.clearDrawableAnimation(absListView);
				}
			}
		});
		return fragmentView;
	}

	public void didReceivedNotification(int id, Object... args)
	{
		if (id == NotificationCenter.contactsDidLoaded)
		{
			if (listViewAdapter != null)
			{
				listViewAdapter.notifyDataSetChanged();
			}
		}
		else if (id == NotificationCenter.chatDidCreated)
		{
			removeSelfFromStack();
		}
	}

	private void updateVisibleRows(int mask)
	{
		if (listView != null)
		{
			int count = listView.getChildCount();
			for (int a = 0; a < count; a++)
			{
				View child = listView.getChildAt(a);
				if (child instanceof UserCell)
				{
					((UserCell) child).update(mask);
				}
			}
		}
	}

	public void setDelegate(GroupCreateActivityDelegate delegate)
	{
	}

	private ChipSpan createAndPutChipForUser(User user)
	{
		View     textView = ((LayoutInflater) ApplicationLoader.applicationContext.getSystemService("layout_inflater")).inflate(R.layout.group_create_bubble, null);
		TextView text     = (TextView) textView.findViewById(R.id.bubble_text_view);
		String   name     = UserObject.getUserName(user);
		if (!(name.length() != 0 || user.phone == null || user.phone.length() == 0))
		{
			name = PhoneFormat.getInstance().format("+" + user.phone);
		}
		Iterator it = allSpans.iterator();
		while (it.hasNext())
		{
			ImageSpan sp = (ImageSpan) it.next();
			ssb.append("<<");
			ssb.setSpan(sp, ssb.length() - 2, ssb.length(), 33);
		}
		userSelectEditText.setText(ssb);
		userSelectEditText.setSelection(ssb.length());
		return span;
	}

	private void selectAll()
	{
		final ProgressDialog progressDialog = new ProgressDialog(getParentActivity());
		progressDialog.setMessage(LocaleController.getString("PleaseWait", R.string.PleaseWait));
		progressDialog.show();
		new Handler().postDelayed(new Runnable()
		{
			public void run()
			{
				selectedContacts.clear();
				int  i;
				User user;
				if (searching && searchWas)
				{
					for (i = 0; i < searchListViewAdapter.getCount(); i++)
					{
						user = (User) searchListViewAdapter.getItem(i);
						if (user != null)
						{
							selectedContacts.put(Integer.valueOf(user.id), null);
						}
					}
				}
		
				if (searching || searchWas)
				{
					searchListViewAdapter.searchDialogs(null);
					searching = false;
					searchWas = false;
				}
				if (listViewAdapter != null)
				{
					listViewAdapter.notifyDataSetChanged();
				}
				if (searchListViewAdapter != null)
				{
					searchListViewAdapter.notifyDataSetChanged();
				}
				progressDialog.dismiss();
			}
		}, 500);
	}

	public interface GroupCreateActivityDelegate
	{
		void didSelectUsers(ArrayList<Integer> arrayList);
	}
}
