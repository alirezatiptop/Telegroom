package org.telegram.Adel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import org.telegram.Adel.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import org.telegram.Adel.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.telegram.SQLite.SQLiteCursor;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.support.widget.GridLayoutManager;
import org.telegram.messenger.support.widget.RecyclerView;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ShareDialogCell;
import org.telegram.ui.Components.CheckBoxSquare;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.Switch;
import org.telegram.ui.StickersActivity;

public class PouyaShare extends BottomSheet
{

	private FrameLayout           frameLayout;
	private TextView              doneButtonBadgeTextView;
	private TextView              doneButtonTextView;
	private LinearLayout          doneButton;
	private EditText              nameTextView;
	private View                  shadow;
	private RecyclerListView      gridView;
	private GridLayoutManager     layoutManager;
	private ShareDialogsAdapter   listAdapter;
	private ShareSearchAdapter    searchAdapter;
	private ArrayList             sendingMessageObject;
	private EmptyTextProgressView searchEmptyView;
	private Drawable              shadowDrawable;
	private HashMap<Long, TLRPC.TL_dialog> selectedDialogs = new HashMap<>();
	private boolean                      forwardNoName;
	private boolean                      caption;
	private TLRPC.TL_exportedMessageLink exportedMessageLink;
	private boolean                      loadingLink;
	private boolean                      copyLinkOnEnd;

	private boolean isPublicChannel;

	private int     scrollOffsetY;
	private int     topBeforeSwitch;
	private boolean proForward;

	public PouyaShare(final Context context, final ArrayList<MessageObject> msgs, boolean z, boolean z2, boolean publicChannel, final OnDoneListener sonDoneListener, final SendDelegate sendDelegate)
	{
		super(context, true);
		this.proForward = z2;
		this.forwardNoName = z;
		shadowDrawable = context.getResources().getDrawable(R.drawable.sheet_shadow);

		sendingMessageObject = msgs;
		searchAdapter = new ShareSearchAdapter(context);
		isPublicChannel = publicChannel;

		if (publicChannel)
		{
			loadingLink = true;
			TLRPC.TL_channels_exportMessageLink req = new TLRPC.TL_channels_exportMessageLink();
			req.id = msgs.get(0).getId();
			req.channel = MessagesController.getInputChannel(msgs.get(0).messageOwner.to_id.channel_id);
			ConnectionsManager.getInstance().sendRequest(req, new RequestDelegate()
			
		}

		containerView = new FrameLayout(context)
		{

			private boolean ignoreLayout = false;

			@Override
			public boolean onInterceptTouchEvent(MotionEvent ev)
			{
				if (ev.getAction() == MotionEvent.ACTION_DOWN && scrollOffsetY != 0 && ev.getY() < scrollOffsetY)
				{
					dismiss();
					return true;
				}
				return super.onInterceptTouchEvent(ev);
			}

			@Override
			public boolean onTouchEvent(MotionEvent e)
			{
				return !isDismissed() && super.onTouchEvent(e);
			}

			@Override
			protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
			{
				int height = MeasureSpec.getSize(heightMeasureSpec);
				if (Build.VERSION.SDK_INT >= 21)
				{
					height -= AndroidUtilities.statusBarHeight;
				}
				int size        = Math.max(searchAdapter.getItemCount(), listAdapter.getItemCount());
				int contentSize = AndroidUtilities.dp(48) + Math.max(3, (int) Math.ceil(size / 4.0f)) * AndroidUtilities.dp(100) + backgroundPaddingTop;
				int padding     = contentSize < height ? 0 : height - (height / 5 * 3) + AndroidUtilities.dp(8);
				if (gridView.getPaddingTop() != padding)
				{
					ignoreLayout = true;
					gridView.setPadding(0, padding, 0, AndroidUtilities.dp(8));
					ignoreLayout = false;
				}
				super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Math.min(contentSize, height), MeasureSpec.EXACTLY));
			}

			@Override
			protected void onLayout(boolean changed, int left, int top, int right, int bottom)
			{
				super.onLayout(changed, left, top, right, bottom);
				updateLayout();
			}

			@Override
			public void requestLayout()
			{
				if (ignoreLayout)
				{
					return;
				}
				super.requestLayout();
			}

			@Override
			protected void onDraw(Canvas canvas)
			{
				shadowDrawable.setBounds(0, scrollOffsetY - backgroundPaddingTop, getMeasuredWidth(), getMeasuredHeight());
				shadowDrawable.draw(canvas);
			}
		};
		containerView.setWillNotDraw(false);
		containerView.setPadding(backgroundPaddingLeft, 0, backgroundPaddingLeft, 0);

		frameLayout = new FrameLayout(context);
		frameLayout.setBackgroundColor(0xffffffff);
		frameLayout.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				return true;
			}
		});

		
		{
			@Override
			public void onClick(View v)
			{
				if (selectedDialogs.isEmpty() && isPublicChannel)
				{
					if (loadingLink)
					{
						copyLinkOnEnd = true;
						Toast.makeText(PouyaShare.this.getContext(), LocaleController.getString("Loading", R.string.Loading), Toast.LENGTH_SHORT).show();
					}
					else
					{
						copyLink(PouyaShare.this.getContext());
					}
					dismiss();
				}
			
				if (sonDoneListener != null)
				{
					try
					{
						sonDoneListener.onDone();
					} catch (Exception e)
					{

					}
				}

			}
		});

		doneButtonBadgeTextView = new TextView(context);
		
		doneButtonTextView.setCompoundDrawablePadding(AndroidUtilities.dp(8));
		doneButtonTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
		doneButton.addView(doneButtonTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_VERTICAL));


		final CheckBoxSquare selectAllcheckBox = new CheckBoxSquare(context, false);
		selectAllcheckBox.setClickable(true);
		selectAllcheckBox.setPadding(0, AndroidUtilities.dp(2.0f), 0, 0);
		selectAllcheckBox.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				int i = 0;
				if (selectAllcheckBox.isChecked())
				{
					selectedDialogs.clear();
					selectAllcheckBox.setChecked(false, true);
				}
				else
				{
					while (i < gridView.getAdapter().getItemCount())
					{
						TLRPC.TL_dialog item = gridView.getAdapter() == listAdapter ? listAdapter.getItem(i) : searchAdapter.getItem(i);
						selectedDialogs.put(Long.valueOf(item.id), item);
						i++;
					}
					selectAllcheckBox.setChecked(true, true);
				}
				if (gridView.getAdapter() == listAdapter)
				{
					listAdapter.notifyDataSetChanged();
				}
				else
				{
					searchAdapter.notifyDataSetChanged();
				}
				updateSelectedCount();
			}
		});
		this.frameLayout.addView(selectAllcheckBox, LayoutHelper.createFrame(18, 18.0f, 19, 10.0f, 0.0f, 0.0f, 24));


		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		TextView textView = new TextView(context);
		doneButtonBadgeTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
		doneButtonBadgeTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
		doneButtonBadgeTextView.setTextColor(Theme.getColor(Theme.key_dialogBadgeText));
		doneButtonBadgeTextView.setGravity(Gravity.CENTER);
		doneButtonBadgeTextView.setBackgroundResource(R.drawable.bluecounter);
		doneButtonBadgeTextView.setMinWidth(AndroidUtilities.dp(23));
		doneButtonBadgeTextView.setPadding(AndroidUtilities.dp(8), 0, AndroidUtilities.dp(8), AndroidUtilities.dp(1));
		doneButton.addView(doneButtonBadgeTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, 23, Gravity.CENTER_VERTICAL, 0, 0, 10, 0));

		doneButtonTextView = new TextView(context);
		doneButtonTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		doneButtonTextView.setGravity(Gravity.CENTER);
		linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2, 1.0f, 16));
		Switch Switch1 = new Switch(context);
		Switch1.setDuplicateParentStateEnabled(false);
		Switch1.setFocusable(false);
		Switch1.setFocusableInTouchMode(false);
		Switch1.setClickable(true);
		Switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b)
			{
				forwardNoName = !b;
			}
		});
		//forwardnoname
		Switch1.setChecked(!false);
		linearLayout.addView(Switch1, LayoutHelper.createLinear(-1, -2, 1));
	
		Switch Switch2 = new Switch(context);
		Switch2.setDuplicateParentStateEnabled(false);
		Switch2.setFocusable(false);
		Switch2.setFocusableInTouchMode(false);
		Switch2.setClickable(true);
		Switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b)
			{
				caption = b;
			}
		});
		// Switch2.setOnCheckedChangeListener(new C15346(Switch1));
		//todo:setting
		boolean z4 = true;//(true && this.forwardNoName) ? false : true;
		Switch2.setChecked(z4);
		linearLayout.addView(Switch2, LayoutHelper.createLinear(-1, -2, 1));
		frameLayout.addView(linearLayout, LayoutHelper.createFrame(48, 48.0f, 19, 81.0f, 0.0f, 0.0f, 24));


		// ImageView imageView = new ImageView(context);
		// imageView.setImageResource(R.drawable.search_share);
		// imageView.setScaleType(ImageView.ScaleType.CENTER);
		// imageView.setPadding(0, AndroidUtilities.dp(2), 0, 0);


		nameTextView = new EditText(context);
		nameTextView.setHint(LocaleController.getString("ShareSendTo", R.string.ShareSendTo));
		nameTextView.setMaxLines(1);
		nameTextView.setSingleLine(true);
		doneButtonBadgeTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
		doneButtonBadgeTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
		doneButtonBadgeTextView.setTextColor(Theme.getColor(Theme.key_dialogBadgeText));
		doneButtonBadgeTextView.setGravity(Gravity.CENTER);
		doneButtonBadgeTextView.setBackgroundResource(R.drawable.bluecounter);
		doneButtonBadgeTextView.setMinWidth(AndroidUtilities.dp(23));
		doneButtonBadgeTextView.setPadding(AndroidUtilities.dp(8), 0, AndroidUtilities.dp(8), AndroidUtilities.dp(1));
		doneButton.addView(doneButtonBadgeTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, 23, Gravity.CENTER_VERTICAL, 0, 0, 10, 0));

		doneButtonTextView = new TextView(context);
		doneButtonTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		doneButtonTextView.setGravity(Gravity.CENTER);
		nameTextView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
//		nameTextView.setTypeface(AndroidUtilities.getTypeface(null)); // Adel
		frameLayout.addView(nameTextView, LayoutHelper.createFrame(AndroidUtilities.dp(40), LayoutHelper.MATCH_PARENT, Gravity.TOP | Gravity.RIGHT, 52, 2, 96, 48));
		nameTextView.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{

			}

			@Override
			public void afterTextChanged(Editable s)
			{
				String text = nameTextView.getText().toString();
				if (text.length() != 0)
				{
				
					if (searchEmptyView != null)
					{
						searchEmptyView.setText(LocaleController.getString("NoResult", R.string.NoResult));
					}
				}
				else
				{
					if (gridView.getAdapter() != listAdapter)
					{
						int top = getCurrentTop();
						searchEmptyView.setText(LocaleController.getString("NoChats", R.string.NoChats));
						gridView.setAdapter(listAdapter);
						listAdapter.notifyDataSetChanged();
						if (top > 0)
						{
							layoutManager.scrollToPositionWithOffset(0, -top);
						}
					}
				}
				if (searchAdapter != null)
				{
					searchAdapter.searchDialogs(text);
				}
			}
		});
		final TabLayout tabHost = new TabLayout(context);
	
		TabSetting.GetTabs(tabHost, null);
		final ViewGroup test   = (ViewGroup) (tabHost.getChildAt(0));//tabs is your Tablayout
		int             tabLen = test.getChildCount();
		for (int i = 0; i < tabLen; i++)
		{
			View v = test.getChildAt(i);
		
			v.setLayoutParams(x);
		}

		tabHost.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
		{
			@Override
			public void onTabSelected(TabLayout.Tab tab)
			{
				TabSetting.setTabIcon(tab, TabSetting.getSelectedIcon(tab.getPosition()));

				listAdapter.currentTab = tab.getPosition();
				listAdapter.dialogs = listAdapter.getDialogs();
				listAdapter.notifyDataSetChanged();
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab)
			{
				TabSetting.setTabIcon(tab, TabSetting.getUnselectedIcon(tab.getPosition()));
			}

			@Override
			public void onTabReselected(TabLayout.Tab tab)
			{
			}
		});

		gridView = new RecyclerListView(context);
		
		gridView.setVerticalScrollBarEnabled(false);
		gridView.addItemDecoration(new RecyclerView.ItemDecoration()
		{
			@Override
			public void getItemOffsets(android.graphics.Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
			{
				Holder holder = (Holder) parent.getChildViewHolder(view);
				if (holder != null)
				{
					int pos = holder.getAdapterPosition();
					outRect.left = pos % 4 == 0 ? 0 : AndroidUtilities.dp(4);
					outRect.right = pos % 4 == 3 ? 0 : AndroidUtilities.dp(4);
				}
				else
				{
					outRect.left = AndroidUtilities.dp(4);
					outRect.right = AndroidUtilities.dp(4);
				}
			}
		});
		containerView.addView(gridView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP | Gravity.LEFT, 0, 95, 0, 0));
		gridView.setAdapter(listAdapter = new ShareDialogsAdapter(context));
		gridView.setGlowColor(0xfff5f6f7);
		gridView.setOnTouchListener(new OnSwipeTouchListener(context)
		gridView.setTag(13);
		gridView.setPadding(0, 0, 0, AndroidUtilities.dp(8));
		gridView.setClipToPadding(false);
		gridView.setLayoutManager(layoutManager = new GridLayoutManager(getContext(), 4));
		gridView.setHorizontalScrollBarEnabled(false);
		{
			public void onSwipeRight()
			{

				//Log.e("move","right");
				int current = tabHost.getSelectedTabPosition();
				if (current == 0)
				{
					current = tabHost.getTabCount() - 1;
				}
				else
				{
					current--;
				}
				tabHost.getTabAt(current).select();
			}

			public void onSwipeLeft()
			{
				//Log.e("move","left");
				int current = tabHost.getSelectedTabPosition();
				if (current == tabHost.getTabCount() - 1)
				{
					current = 0;
				}
				else
				{
					current++;
				}
				tabHost.getTabAt(current).select();
			}
		});
		gridView.setOnItemClickListener(new RecyclerListView.OnItemClickListener()
		{
			@Override
			public void onItemClick(View view, int position)
			{
				TLRPC.TL_dialog dialog;
				else
				{
					dialog = searchAdapter.getItem(position);
				}
				if (dialog == null)
				{
					return;
				}
				ShareDialogCell cell = (ShareDialogCell) view;
				if (selectedDialogs.containsKey(dialog.id))
				{
					selectedDialogs.remove(dialog.id);
					cell.setChecked(false, true);
				}
				else
				{
					selectedDialogs.put(dialog.id, dialog);
					cell.setChecked(true, true);
				}
				updateSelectedCount();
			}
		});
		gridView.setOnScrollListener(new RecyclerView.OnScrollListener()
		{
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy)
			{
				updateLayout();
			}
		});

		searchEmptyView = new EmptyTextProgressView(context);
		searchEmptyView.setOnTouchListener(new OnSwipeTouchListener(context)
		{
			public void onSwipeRight()
			{

				//Log.e("move","right");
				int current = tabHost.getSelectedTabPosition();
				if (current == 0)
				{
					current = tabHost.getTabCount() - 1;
				}
				else
				{
					current--;
				}
				tabHost.getTabAt(current).select();
			}

			public void onSwipeLeft()
			{
				//Log.e("move","left");
				int current = tabHost.getSelectedTabPosition();
				if (current == tabHost.getTabCount() - 1)
				{
					current = 0;
				}
				else
				{
					current++;
				}
				tabHost.getTabAt(current).select();
			}
		});
		searchEmptyView.setText(LocaleController.getString("NoChats", R.string.NoChats));
	
		// frameLayout.addView(tabHost, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 40, Gravity.LEFT | Gravity.TOP,0,48,0,0));

		shadow = new View(context);
		shadow.setBackgroundResource(R.drawable.header_shadow);
		containerView.addView(shadow, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 3, Gravity.TOP | Gravity.LEFT, 0, 48, 0, 0));

		if (LocaleController.getCurrentLanguageName().equals("فارسی"))
		{
			tabHost.getTabAt(TabSetting.getTabModels().size() - 1).select();
		}
		else
		{
			tabHost.getTabAt(0).select();
		}
	
		updateSelectedCount();
		//        containerView.addView(tabHost, LayoutHelper.createFrame(-1, 48.0f, 51, 0.0f, 48.0f, 0.0f, 0.0f));
	}

	public PouyaShare(Context context, ArrayList<MessageObject> arrayList)
	{
		this(context, arrayList, false, false, false, null);
	}

	public PouyaShare(Context context, ArrayList<MessageObject> arrayList, boolean z)
	{
		this(context, arrayList, z, false, false, null);
	}

	public PouyaShare(Context context, ArrayList<MessageObject> arrayList, boolean z, boolean z2, boolean z3, OnDoneListener onDoneListener)
	{
		this(context, arrayList, z, z2, z3, onDoneListener, null);
	}

	public PouyaShare(Context mContext, MessageObject messageObject, boolean publicChannel)
	{
		this(mContext, new ArrayList(Arrays.asList(new MessageObject[]{messageObject})), publicChannel);
	}

	private int getCurrentTop()
	{
		if (gridView.getChildCount() != 0)
		{
			View   child  = gridView.getChildAt(0);
			Holder holder = (Holder) gridView.findContainingViewHolder(child);
			if (holder != null)
			{
				return gridView.getPaddingTop() - (holder.getAdapterPosition() == 0 && child.getTop() >= 0 ? child.getTop() : 0);
			}
		}
		return -1000;
	}

	@Override
	protected boolean canDismissWithSwipe()
	{
		return false;
	}

	@SuppressLint("NewApi")
	private void updateLayout()
	{
		if (gridView.getChildCount() <= 0)
		{
			return;
		}
		
	}

	private void copyLink(Context context)
	{
		if (exportedMessageLink == null)
		{
			return;
		}
		try
		{
			android.content.ClipboardManager clipboard = (android.content.ClipboardManager) ApplicationLoader.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE);
			android.content.ClipData         clip      = android.content.ClipData.newPlainText("label", exportedMessageLink.link);
			clipboard.setPrimaryClip(clip);
			Toast.makeText(context, LocaleController.getString("LinkCopied", R.string.LinkCopied), Toast.LENGTH_SHORT).show();
		} catch (Exception e)
		{
			FileLog.e("tmessages", e);
			View   child     = gridView.getChildAt(0);
		Holder holder    = (Holder) gridView.findContainingViewHolder(child);
		int    top       = child.getTop() - AndroidUtilities.dp(8);
		int    newOffset = top > 0 && holder != null && holder.getAdapterPosition() == 0 ? top : 0;
		if (scrollOffsetY != newOffset)
		{
			gridView.setTopGlowOffset(scrollOffsetY = newOffset);
			frameLayout.setTranslationY(scrollOffsetY);
			shadow.setTranslationY(scrollOffsetY);
			searchEmptyView.setTranslationY(scrollOffsetY);
			containerView.invalidate();
		}
		}
	}

	public void updateSelectedCount()
	{
		if (selectedDialogs.isEmpty())
		{
			doneButtonBadgeTextView.setVisibility(View.GONE);
			if (!isPublicChannel)
			{
				doneButtonTextView.setTextColor(Theme.getColor(Theme.key_dialogTextGray4));
				doneButton.setEnabled(false);
				doneButtonTextView.setText(LocaleController.getString("Send", R.string.Send).toUpperCase());
			}
		
		}
		else
		{
			doneButtonTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			
			doneButtonTextView.setText(LocaleController.getString("Send", R.string.Send).toUpperCase());
		}
	}

	public interface OnDoneListener
	{
		void onDone();
	}

	public interface SendDelegate
	{
		void send(TLRPC.TL_dialog dialog, Long l, boolean z);
	}

	private class Holder extends RecyclerView.ViewHolder
	{

		public Holder(View itemView)
		{
			super(itemView);
		}
	}

	private class ShareDialogsAdapter extends RecyclerView.Adapter
	{
		public static final String UNREAD      = "unread";
		public static final String FAVOR       = "favor";
		public static final String BOT         = "bot";
		public static final String CHANNEL     = "channel";
		public static final String ALL         = "all";
		public static final String SUPPERGROUP = "sgroup";
		public static final String CONTACT     = "contact";
		public static final String GROUP       = "ngroup";

		private boolean hiddenMode;

		private Context context;
		private int     currentCount;
		private int                        currentTab = 0;
		private ArrayList<TLRPC.TL_dialog> dialogs    = new ArrayList<>();

		public ShareDialogsAdapter(Context context)
		{
			this.context = context;
			hiddenMode = Setting.GetHiddenMode();

			if (LocaleController.getCurrentLanguageName().equals("فارسی")) // Adel
			{
				dialogs = getDialogs(TabSetting.getTabModels().size() - 1);
			}
			else
			{
				dialogs = getDialogs(0);
			}

			this.notifyDataSetChanged();
		}

		public ArrayList<TLRPC.TL_dialog> getDialogs(int tab)
		{
			currentTab = tab;
			return getDialogs();
		}

		public ArrayList<TLRPC.TL_dialog> getDialogs()
		{
			String                     categoryId = TabSetting.getTabModels().get(currentTab).getId();
			ArrayList<TLRPC.TL_dialog> ret        = new ArrayList<>();

			switch (categoryId)
			{
				case ALL:
					ret = MessagesController.getInstance().dialogs;
					break;
				case CHANNEL:
					ret = scanchannels(MessagesController.getInstance().dialogsChannelOnly);
					break;
				case GROUP:
					ret = MessagesController.getInstance().dialogsJustGroupsOnly;
					break;
				case CONTACT:
					ret = MessagesController.getInstance().dialogsContactOnly;
					break;
				case FAVOR:
					ret = MessagesController.getInstance().dialogsFavoriteOnly;
					break;
				case BOT:
					ret = MessagesController.getInstance().dialogsBotOnly;
					break;
			}

			return HidePRoccess(ret);

			yList;
		}

		private ArrayList<TLRPC.TL_dialog> scanchannels(ArrayList<TLRPC.TL_dialog> dialogsChannelOnly)
		{
			ArrayList<TLRPC.TL_dialog> m = new ArrayList<>();
			for (int i = 0; i < dialogsChannelOnly.size(); i++)
			{
				TLRPC.Chat chat = MessagesController.getInstance().getChat((int) -dialogsChannelOnly.get(i).id);
				if (chat != null && (chat.creator || (chat.admin_rights != null && chat.admin_rights.post_messages)))
				{
					m.add(dialogsChannelOnly.get(i));
				}
			}
			return m;
		}

		private ArrayList<TLRPC.TL_dialog> HidePRoccess(ArrayList<TLRPC.TL_dialog> ret)
		{
			ArrayList<TLRPC.TL_dialog> reth = new ArrayList<>();
			for (int i = 0; i < ret.size(); i++)
			{
				if (hiddenMode)
				{
					if (HiddenController.isHidden(ret.get(i).id))
					{
						reth.add(ret.get(i));
					}
				}
				else
				{
					if (!HiddenController.isHidden(ret.get(i).id))
					{
						reth.add(ret.get(i));
					}
				}
				//                    ArrayList arrayList = new ArrayList();
			//                    PouyaShare shareAlert = PouyaShare.this;
			//
			//                    if (currentTab== 7) {
			//                        Iterator it = ContactsController.getInstance().contacts.iterator();
			//                        while (it.hasNext()) {
			//                            TLRPC.TL_contact tL_contact = (TLRPC.TL_contact) it.next();
			//                            TLRPC.TL_dialog tL_dialog = new TLRPC.TL_dialog();
			//                            tL_dialog.id = (long) tL_contact.user_id;
			//                            arrayList.add(tL_dialog);
			//                        }
			//                        return arrayList;
			//                    }
			//                    for (int i = 0; i < MessagesController.getInstance().dialogsServerOnly.size(); i++) {
			//                        TLRPC.TL_dialog tL_dialog2 = MessagesController.getInstance().dialogsServerOnly.get(i);
			//
			//                        if (currentTab == 8) {
			//                            arrayList.add(tL_dialog2);
			//                        } else if (currentTab== 0) {
			//                            //favor
			//                            if (FavoriteController.isFavor(Long.valueOf(tL_dialog2.id))) {
			//                                arrayList.add(tL_dialog2);
			//                            }
			//                        } else if (currentTab== 4) {
			//                            //dontknow
			//                           TLRPC.User  r4 = MessagesController.getInstance().getUser(Integer.valueOf((int) tL_dialog2.id));
			//                            if (!(r4 == null || r4.bot)) {
			//                                arrayList.add(tL_dialog2);
			//                            }
			//                        } else if (currentTab == 6) {
			//                            if ((tL_dialog2 instanceof TLRPC.TL_dialog) && tL_dialog2.id < 0 && !DialogObject.isChannel(tL_dialog2)) {
			//                                arrayList.add(tL_dialog2);
			//                            }
			//                        } else if (currentTab== 5) {
			//
			//                            //supergroup
			//                            TLRPC.Chat r4 = MessagesController.getInstance().getChat(Integer.valueOf(-((int) tL_dialog2.id)));
			//                            if (r4 != null && ChatObject.isChannel(r4) && r4.megagroup) {
			//                                arrayList.add(tL_dialog2);
			//                            }
			//                        } else if (currentTab == 3) {
			//                            TLRPC.Chat r4 = MessagesController.getInstance().getChat(Integer.valueOf(-((int) tL_dialog2.id)));
			//                            if (!(r4 == null || !ChatObject.isChannel(r4) || r4.megagroup)) {
			//                                arrayList.add(tL_dialog2);
			//                            }
			//                        } else if (currentTab == 1) {
			//                            TLRPC.User  r4 = MessagesController.getInstance().getUser(Integer.valueOf((int) tL_dialog2.id));
			//                            if (r4 != null && r4.bot) {
			//                                arrayList.add(tL_dialog2);
			//                            }
			//                        } else if (currentTab== 4) {
			//                            if (!(tL_dialog2 instanceof TLRPC.TL_dialog) || tL_dialog2.id >= 0 || DialogObject.isChannel(tL_dialog2)) {
			//                                TLRPC.Chat   r4 = MessagesController.getInstance().getChat(Integer.valueOf(-((int) tL_dialog2.id)));
			//                                if (r4 != null && ChatObject.isChannel(r4) && r4.megagroup) {
			//                                    arrayList.add(tL_dialog2);
			//                                }
			//                            } else {
			//                                arrayList.add(tL_dialog2);
			//                            }
			//                        }
			//                    }
			//                    return arra

			}

			return reth;
		}

		@Override
		public int getItemCount()
		{
			return dialogs.size();
		}

		public TLRPC.TL_dialog getItem(int i)
		{
			if (i < 0 || i >= dialogs.size())
			{
				return null;
			}
			return dialogs.get(i);
		}

		@Override
		public long getItemId(int i)
		{
			return i;
		}

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
		{
			View view = new ShareDialogCell(context);
			view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, AndroidUtilities.dp(100)));
			return new Holder(view);
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
		{
			ShareDialogCell cell   = (ShareDialogCell) holder.itemView;
			TLRPC.TL_dialog dialog = getItem(position);
			cell.setDialog((int) dialog.id, selectedDialogs.containsKey(dialog.id), null);
			
			
			
			//                    ArrayList arrayList = new ArrayList();
			//                    PouyaShare shareAlert = PouyaShare.this;
			//
			//                    if (currentTab== 7) {
			//                        Iterator it = ContactsController.getInstance().contacts.iterator();
			//                        while (it.hasNext()) {
			//                            TLRPC.TL_contact tL_contact = (TLRPC.TL_contact) it.next();
			//                            TLRPC.TL_dialog tL_dialog = new TLRPC.TL_dialog();
			//                            tL_dialog.id = (long) tL_contact.user_id; MessagesController.getInstance().dialogsServerOnly.get(i);
			//                        } else if (currentTab== 4) {
			//                            if (!(tL_dialog2 instanceof TLRPC.TL_dialog) || tL_dialog2.id >= 0 || DialogObject.isChannel(tL_dialog2)) {
			//                                TLRPC.Chat   r4 = MessagesController.getInstance().getChat(Integer.valueOf(-((int) tL_dialog2.id)));
			//                                if (r4 != null && ChatObject.isChannel(r4) && r4.megagroup) {
			//                                    arrayList.add(tL_dialog2);
			//                                }
			//                            } else {
			//                                arrayList.add(tL_dialog2);
			//                            }
			//                        }
			//                    }
			//                    return arra
		}

		@Override
		public int getItemViewType(int i)
		{
			return 0;
		}
	}

	public class ShareSearchAdapter extends RecyclerView.Adapter
	{

		private Context context;
		private Timer   searchTimer;
		private ArrayList<DialogSearchResult> searchResult = new ArrayList<>();
		private String lastSearchText;
		private int reqId = 0;
		private int lastReqId;
		private int lastSearchId = 0;

		private boolean hiddenMode;

		public ShareSearchAdapter(Context context)
		{
			this.context = context;
			hiddenMode = Setting.GetHiddenMode();
		}

		private void searchDialogsInternal(final String query, final int searchId)
		{
			MessagesStorage.getInstance().getStorageQueue().postRunnable(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						String search1 = query.trim().toLowerCase();
						if (search1.length() == 0)
						{
							lastSearchId = -1;
							updateSearchResults(new ArrayList<DialogSearchResult>(), lastSearchId);
							return;
						}
						String search2 = LocaleController.getInstance().getTranslitString(search1);
						if (search1.equals(search2) || search2.length() == 0)
						{
							search2 = null;
						}
						String search[] = new String[1 + (search2 != null ? 1 : 0)];
						search[0] = search1;
						if (search2 != null)
						{
							search[1] = search2;
						}

						ArrayList<Integer> usersToLoad = new ArrayList<>();
						ArrayList<Integer> chatsToLoad = new ArrayList<>();
						int                resultCount = 0;

						HashMap<Long, DialogSearchResult> dialogsResult = new HashMap<>();
						SQLiteCursor                      cursor        = MessagesStorage.getInstance().getDatabase().queryFinalized("SELECT did, date FROM dialogs ORDER BY date DESC LIMIT 400");
						while (cursor.next())
						{
							long               id                 = cursor.longValue(0);
							DialogSearchResult dialogSearchResult = new DialogSearchResult();
							dialogSearchResult.date = cursor.intValue(1);
							dialogsResult.put(id, dialogSearchResult);

							int lower_id = (int) id;
							int high_id  = (int) (id >> 32);
							if (lower_id != 0 && high_id != 1)
							{
								if (lower_id > 0)
								{
									if (!usersToLoad.contains(lower_id))
									{
										usersToLoad.add(lower_id);
									}
								}
								else
								{
									if (!chatsToLoad.contains(-lower_id))
									{
										chatsToLoad.add(-lower_id);
									}
								}
							}
						}
						cursor.dispose();

						if (!usersToLoad.isEmpty())
						{
							cursor = MessagesStorage.getInstance().getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, status, name FROM users WHERE uid IN(%s)", TextUtils.join(",", usersToLoad)));
							while (cursor.next())
							{
								
						}

						if (!chatsToLoad.isEmpty())
						{
							cursor = MessagesStorage.getInstance().getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, name FROM chats WHERE uid IN(%s)", TextUtils.join(",", chatsToLoad)));
							while (cursor.next())
							{
								String name  = cursor.stringValue(1);
								String tName = LocaleController.getInstance().getTranslitString(name);
								if (name.equals(tName))
								{
									tName = null;
								}
								for (int a = 0; a < search.length; a++)
								{
									String q = search[a];
									if (name.startsWith(q) || name.contains(" " + q) || tName != null && (tName.startsWith(q) || tName.contains(" " + q)))
									{
										NativeByteBuffer data = cursor.byteBufferValue(0);
										if (data != null)
										{
											TLRPC.Chat chat = TLRPC.Chat.TLdeserialize(data, data.readInt32(false), false);
											data.reuse();
											if (!(chat == null || ChatObject.isNotInChat(chat) || ChatObject.isChannel(chat) && !chat.creator && !(chat.admin_rights != null && chat.admin_rights.post_messages) && !chat.megagroup))
											{
												DialogSearchResult dialogSearchResult = dialogsResult.get(-(long) chat.id);
												dialogSearchResult.name = AndroidUtilities.generateSearchName(chat.title, null, q);
												dialogSearchResult.object = chat;
												dialogSearchResult.dialog.id = -chat.id;
												resultCount++;
											}
											String name  = cursor.stringValue(2);
								String tName = LocaleController.getInstance().getTranslitString(name);
								if (name.equals(tName))
								{
									tName = null;
								}
								String username    = null;
								int    usernamePos = name.lastIndexOf(";;;");
								if (usernamePos != -1)
								{
									username = name.substring(usernamePos + 3);
								}
								int found = 0;
								for (String q : search)
								{
									if (name.startsWith(q) || name.contains(" " + q) || tName != null && (tName.startsWith(q) || tName.contains(" " + q)))
									{
										found = 1;
									}
									else if (username != null && username.startsWith(q))
									{
										found = 2;
									}
									if (found != 0)
									{
										NativeByteBuffer data = cursor.byteBufferValue(0);
										if (data != null)
										{
											TLRPC.User user = TLRPC.User.TLdeserialize(data, data.readInt32(false), false);
											data.reuse();
											DialogSearchResult dialogSearchResult = dialogsResult.get((long) user.id);
											if (user.status != null)
											{
												user.status.expires = cursor.intValue(1);
											}
											if (found == 1)
											{
												dialogSearchResult.name = AndroidUtilities.generateSearchName(user.first_name, user.last_name, q);
											}
											else
											{
												dialogSearchResult.name = AndroidUtilities.generateSearchName("@" + user.username, null, "@" + q);
											}
											dialogSearchResult.object = user;
											dialogSearchResult.dialog.id = user.id;
											resultCount++;
										}
										break;
									}
								}
							}
							cursor.dispose();
										}
										break;
									}
								}
							}
							cursor.dispose();
						}

						ArrayList<DialogSearchResult> searchResults = new ArrayList<>(resultCount);
						for (DialogSearchResult dialogSearchResult : dialogsResult.values())
						{
							if (dialogSearchResult.object != null && dialogSearchResult.name != null)
							{
								searchResults.add(dialogSearchResult);
							}
						}

						cursor = MessagesStorage.getInstance().getDatabase().queryFinalized("SELECT u.data, u.status, u.name, u.uid FROM users as u INNER JOIN contacts as c ON u.uid = c.uid");
						while (cursor.next())
						{
							int uid = cursor.intValue(3);
							if (dialogsResult.containsKey((long) uid))
							{
								continue;
							}
							String name  = cursor.stringValue(2);
							String tName = LocaleController.getInstance().getTranslitString(name);
							if (name.equals(tName))
							{
								tName = null;
							}
							String name  = cursor.stringValue(2);
								String tName = LocaleController.getInstance().getTranslitString(name);
								if (name.equals(tName))
								{
									tName = null;
								}
								String username    = null;
								int    usernamePos = name.lastIndexOf(";;;");
								if (usernamePos != -1)
								{
									username = name.substring(usernamePos + 3);
								}
								int found = 0;
								for (String q : search)
								{
									if (name.startsWith(q) || name.contains(" " + q) || tName != null && (tName.startsWith(q) || tName.contains(" " + q)))
									{
										found = 1;
									}
									else if (username != null && username.startsWith(q))
									{
										found = 2;
									}
									if (found != 0)
									{
										NativeByteBuffer data = cursor.byteBufferValue(0);
										if (data != null)
										{
											TLRPC.User user = TLRPC.User.TLdeserialize(data, data.readInt32(false), false);
											data.reuse();
											DialogSearchResult dialogSearchResult = dialogsResult.get((long) user.id);
											if (user.status != null)
											{
												user.status.expires = cursor.intValue(1);
											}
											if (found == 1)
											{
												dialogSearchResult.name = AndroidUtilities.generateSearchName(user.first_name, user.last_name, q);
											}
											else
											{
												dialogSearchResult.name = AndroidUtilities.generateSearchName("@" + user.username, null, "@" + q);
											}
											dialogSearchResult.object = user;
											dialogSearchResult.dialog.id = user.id;
											resultCount++;
										}
										break;
									}
								}
							}
							cursor.dispose();
							String username    = null;
							int    usernamePos = name.lastIndexOf(";;;");
							if (usernamePos != -1)
							{
								username = name.substring(usernamePos + 3);
							}
							int found = 0;
							for (String q : search)
							{
								if (name.startsWith(q) || name.contains(" " + q) || tName != null && (tName.startsWith(q) || tName.contains(" " + q)))
								{
									found = 1;
								}
								else if (username != null && username.startsWith(q))
								{
									found = 2;
								}
								
							}
						}
						cursor.dispose();

						Collections.sort(searchResults, new Comparator<DialogSearchResult>()
						{
							@Override
							public int compare(DialogSearchResult lhs, DialogSearchResult rhs)
							{
								if (lhs.date < rhs.date)
								{
									return 1;
								}
								else if (lhs.date > rhs.date)
								{
									return -1;
								}
								return 0;
							}
						});

						updateSearchResults(searchResults, searchId);
					} catch (Exception e)
					{
						FileLog.e("tmessages", e);
					}
				}
			});
		}

		private void updateSearchResults(final ArrayList<DialogSearchResult> result, final int searchId)
		{
			AndroidUtilities.runOnUIThread(new Runnable()
			{
				@Override
				public void run()
				{
					if (searchId != lastSearchId)
					{
						return;
					}
					
					boolean becomeEmpty = !searchResult.isEmpty() && result.isEmpty();
					boolean isEmpty     = searchResult.isEmpty() && result.isEmpty();
					if (becomeEmpty)
					{
						topBeforeSwitch = getCurrentTop();
					}
					searchResult = HidePRoccess(result);
					notifyDataSetChanged();
					if (!isEmpty && !becomeEmpty && topBeforeSwitch > 0)
					{
						layoutManager.scrollToPositionWithOffset(0, -topBeforeSwitch);
						topBeforeSwitch = -1000;
					}
				}
			});
		}

		public void searchDialogs(final String query)
		{
			if (query != null && lastSearchText != null && query.equals(lastSearchText))
			{
				return;
			}
			lastSearchText = query;
			try
			{
				if (searchTimer != null)
				{
					searchTimer.cancel();
					searchTimer = null;
				}
			} catch (Exception e)
			{
				FileLog.e("tmessages", e);
			}
			if (query == null || query.length() == 0)
			{
				searchResult.clear();
				topBeforeSwitch = getCurrentTop();
				notifyDataSetChanged();
			}
			else
			{
				final int searchId = ++lastSearchId;
				searchTimer = new Timer();
				searchTimer.schedule(new TimerTask()
				{
					@Override
					public void run()
					{
						try
						{
							cancel();
							searchTimer.cancel();
							searchTimer = null;
						} catch (Exception e)
						{
							FileLog.e("tmessages", e);
						}
						searchDialogsInternal(query, searchId);
					}
				}, 200, 300);
			}
		}

		private ArrayList<DialogSearchResult> HidePRoccess(ArrayList<DialogSearchResult> ret)
		{
			ArrayList<DialogSearchResult> reth = new ArrayList<>();
			return reth;
		}

		@Override
		public int getItemCount()
		{
			return searchResult.size();
		}

		public TLRPC.TL_dialog getItem(int i)
		{
			return searchResult.get(i).dialog;
		}

		@Override
		public long getItemId(int i)
		{
			return i;
		}

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
		{
			View view = new ShareDialogCell(context);
			view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, AndroidUtilities.dp(100)));
			return new Holder(view);
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
		{
			
			cell.setDialog((int) result.dialog.id, selectedDialogs.containsKey(result.dialog.id), result.name);
		}

		@Override
		public int getItemViewType(int i)
		{
			return 0;
		}

		private class DialogSearchResult
		{
			public TLRPC.TL_dialog dialog = new TLRPC.TL_dialog();
			public TLObject     object;
			public int          date;
			public CharSequence name;
		}
	}
}
