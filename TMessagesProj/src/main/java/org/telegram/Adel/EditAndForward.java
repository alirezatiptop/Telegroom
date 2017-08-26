package org.telegram.Adel;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils.TruncateAt;
import android.text.style.CharacterStyle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.mp4parser.authoring.tracks.h265.NalUnitTypes;

import java.util.ArrayList;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.R;
import org.telegram.messenger.support.widget.helper.ItemTouchHelper;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.TLRPC.Message;
import org.telegram.tgnet.TLRPC.MessageMedia;
import org.telegram.tgnet.TLRPC.TL_message;
import org.telegram.tgnet.TLRPC.TL_messageMediaAudio_layer45;
import org.telegram.tgnet.TLRPC.TL_messageMediaContact;
import org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC.TL_messageMediaDocument_old;
import org.telegram.tgnet.TLRPC.TL_messageMediaEmpty;
import org.telegram.tgnet.TLRPC.TL_messageMediaGeo;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PhotoFilterView;
import org.telegram.ui.Components.SizeNotifierFrameLayout;

public class EditAndForward extends BaseFragment
{
	public    FrameLayout           frameLayout;
	protected ChatActivityEnterView enterView;
	private   MessageObject         messageObject;

	public EditAndForward(MessageObject messageObject)
	{
		this.messageObject = new MessageObject(m389a(messageObject.messageOwner, messageObject), null, true);
		this.messageObject.photoThumbs = messageObject.photoThumbs;
	}

	private Message m389a(Message message, MessageObject messageObject)
	{
		if (message == null)
		{
			return null;
		}
		Message message2 = new Message();
		if (message instanceof TL_message)
		{
			message2 = new TL_message();
		}
		else if (message instanceof TL_message_secret)
		{
			message2 = new TL_message_secret();
		}
		message2.id = message.id;
		message2.from_id = message.from_id;
		message2.to_id = message.to_id;

		if (message2.message != null)
		{
			message2.message = message.message;
		}
		else if (messageObject.messageText != null)
		{
			message2.message = messageObject.messageText.toString();
		}
		if (message.media != null)
		{
			message2.media = m390a(message.media);
		}
		message2.flags = message.flags;
		message2.mentioned = message.mentioned;
		message2.media_unread = message.media_unread;
		message2.out = message.out;
		message2.unread = message.unread;
		message2.destroyTime = message.destroyTime;
		message2.layer = message.layer;
		message2.seq_in = message.seq_in;
		message2.seq_out = message.seq_out;
		message2.replyMessage = message.replyMessage;
		return message2;
	}

	private MessageMedia m390a(MessageMedia messageMedia)
	{
		MessageMedia tL_messageMediaUnsupported_old = messageMedia instanceof TL_messageMediaUnsupported_old ? new TL_messageMediaUnsupported_old() : messageMedia instanceof TL_messageMediaAudio_layer45 ? new TL_messageMediaAudio_layer45() : messageMedia instanceof TL_messageMediaPhoto_old ? new TL_messageMediaPhoto_old() : messageMedia instanceof TL_messageMediaUnsupported ? new TL_messageMediaUnsupported() : messageMedia instanceof TL_messageMediaEmpty ? new TL_messageMediaEmpty() : messageMedia instanceof TL_messageMediaVenue ? new TL_messageMediaVenue() : messageMedia instanceof TL_messageMediaVideo_old ? new TL_messageMediaVideo_old() : messageMedia instanceof TL_messageMediaDocument_old ? new TL_messageMediaDocument_old() : messageMedia instanceof TL_messageMediaDocument ? new TL_messageMediaDocument() : messageMedia instanceof TL_messageMediaContact ? new TL_messageMediaContact() : messageMedia instanceof TL_messageMediaPhoto ? new TL_messageMediaPhoto() : messageMedia instanceof TL_messageMediaVideo_layer45 ? new TL_messageMediaVideo_layer45() : messageMedia instanceof TL_messageMediaWebPage ? new TL_messageMediaWebPage() : messageMedia instanceof TL_messageMediaGeo ? new TL_messageMediaGeo() : new MessageMedia();
		tL_messageMediaUnsupported_old.bytes = messageMedia.bytes;
		tL_messageMediaUnsupported_old.caption = messageMedia.caption;
		tL_messageMediaUnsupported_old.photo = messageMedia.photo;
		tL_messageMediaUnsupported_old.audio_unused = messageMedia.audio_unused;
		tL_messageMediaUnsupported_old.last_name = messageMedia.last_name;
		tL_messageMediaUnsupported_old.user_id = messageMedia.user_id;
		tL_messageMediaUnsupported_old.webpage = messageMedia.webpage;
		return tL_messageMediaUnsupported_old;
	}

	private void m391a()
	{
		ArrayList arrayList = new ArrayList();
		arrayList.add(this.messageObject);
		showDialog(new PouyaShare(getParentActivity(), arrayList, false, true, false, new PouyaShare.OnDoneListener()
		{
			public void onDone()
			{
				Toast.makeText(getParentActivity(), LocaleController.getString("Sent", R.string.Sent), Toast.LENGTH_SHORT).show();
				finishFragment();
			}
		}));
	}

	private boolean m395c()
	{
		return (this.messageObject.messageOwner == null || this.messageObject.messageOwner.media == null || (this.messageObject.messageOwner.media instanceof TL_messageMediaWebPage) || (this.messageObject.messageOwner.media instanceof TL_messageMediaEmpty)) ? false : true;
	}

	public View createView(Context context)
	{
		View   view;
		String obj = "";
		this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
		this.actionBar.setAllowOverlayTitle(true);
		this.actionBar.setTitle(LocaleController.getString("ProForward", R.string.ProForward));
		this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick()
		{
			public void onItemClick(int i)
			{
				if (i == -1)
				{
					EditAndForward.this.finishFragment();
				}
			}
		});
		this.fragmentView = new ae(this, context);
		SizeNotifierFrameLayout sizeNotifierFrameLayout = (SizeNotifierFrameLayout) this.fragmentView;
		FrameLayout frameLayout = new FrameLayout(context);
		frameLayout.setBackgroundColor(-1);
		if (m395c())
		{
			this.frameLayout.addView(frameLayout, LayoutHelper.createFrame(-1, 48, 48));
		}
		View view2 = new View(context);
		view2.setBackgroundColor(-1513240);
		frameLayout.addView(view2, LayoutHelper.createFrame(-1, 1, 83));
		TextView textView = new TextView(context);
		textView.setMaxLines(1);
		textView.setText(LocaleController.getString("Media", R.string.Media) + " : ");
		frameLayout.addView(textView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 16, 10.0f, 4.0f, 10.0f, 0.0f));
		if (this.enterView != null)
		{
			this.enterView.onDestroy();
		}
		this.enterView = new ChatActivityEnterView(getParentActivity(), sizeNotifierFrameLayout, null, false);
		this.enterView.setDialogId(this.messageObject.getDialogId());
//		this.enterView.getMessageEditText().setMaxLines(m395c() ? 10 : 15);

		sizeNotifierFrameLayout.addView(this.enterView, sizeNotifierFrameLayout.getChildCount() - 1, LayoutHelper.createFrame(-1, -2, 83));
		//this.enterView.setDelegate(new ag(this));
		String          str = null;
		ChatMessageCell messgcell;
		if (m395c())
		{
			messgcell = new ChatMessageCell(getParentActivity());
			String str2 = this.messageObject.messageOwner.media.caption;
			this.messageObject.messageOwner.media.caption = null;
			this.messageObject.caption = null;
			str = str2;
		}
		messgcell.setDelegate(new ChatMessageCell.ChatMessageCellDelegate()
		{
			@Override
			public void didPressedUserAvatar(ChatMessageCell cell, TLRPC.User user)
			{

			}


			@Override
			public void didPressedCancelSendButton(ChatMessageCell cell)
			{

			}

			@Override
			public void didLongPressed(ChatMessageCell cell)
			{

			}

			@Override
			public void didPressedReplyMessage(ChatMessageCell cell, int id)
			{

			}

			@Override
			public void didPressedUrl(MessageObject messageObject, CharacterStyle url, boolean longPress)
			{

			}

			@Override
			public void needOpenWebView(String url, String title, String description, String originalUrl, int w, int h)
			{

			}

			@Override
			public void didPressedImage(ChatMessageCell cell)
			{

			}


			@Override
			public void didPressedOther(ChatMessageCell cell)
			{

			}


			@Override
			public void didPressedInstantButton(ChatMessageCell cell, int type)
			{

			}

			@Override
			public boolean needPlayMessage(MessageObject messageObject)
			{
				return false;
			}

			@Override
			public boolean canPerformActions()
			{
				return false;
			}
		});
		messgcell.setMessageObject(this.messageObject, false, false); // Adel add false, false to params
		this.enterView.setFieldText(str);
		this.enterView.getSendButton().setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				String fieldText = EditAndForward.this.enterView.getFieldText().toString();
				if (fieldText != null && fieldText.length() == 0)
				{
					fieldText = null;
				}
				EditAndForward.this.enterView.closeKeyboard();
				EditAndForward.this.m391a();
			}
		});
		FrameLayout FrameLayout2 = new FrameLayout(context);
		FrameLayout2.setClickable(true);
		txt.setSingleLine(true);
		txt.setEllipsize(TruncateAt.END);
		txt.setMaxLines(1);
		if (m395c())
		{
			txt.setText(LocaleController.getString("MediaCaption", R.string.MediaCaption) + " : ");
//			this.enterView.getMessageEditText().setFilters(new InputFilter[]{new LengthFilter(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION)});
//			this.enterView.getMessageEditText().setHint(LocaleController.getString("MediaCaption", R.string.MediaCaption));
		}
		else
		{
		}
		FrameLayout2.addView(txt, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 16, 10.0f, 4.0f, 10.0f, 0.0f));
		this.enterView.showTopView(true, false);
		return this.fragmentView;
	}
}

class ae extends SizeNotifierFrameLayout
{
	final /* synthetic */ EditAndForward f433b;
	int f432a;

	public ae(EditAndForward acVar, Context context)
	{
		super(context);
		this.f433b = acVar;

		this.f432a = 0;
	}

	protected void onLayout(boolean z, int i, int i2, int i3, int i4)
	{
		int childCount   = getChildCount();
		int emojiPadding = getKeyboardHeight() <= AndroidUtilities.dp(20.0f) ? this.f433b.enterView.getEmojiPadding() : 0;
		setBottomClip(emojiPadding);
		for (int i5 = 0; i5 < childCount; i5++)
		{
			View childAt = getChildAt(i5);
			if (childAt.getVisibility() != View.GONE)
			{
				int                      i6;
				FrameLayout.LayoutParams layoutParams   = (FrameLayout.LayoutParams) childAt.getLayoutParams();
				int                      measuredWidth  = childAt.getMeasuredWidth();
				int                      measuredHeight = childAt.getMeasuredHeight();
				int                      i7             = layoutParams.gravity;
				if (i7 == -1)
				{
					i7 = 51;
				}
				int i8 = i7 & 112;
				switch ((i7 & 7) & 7)
				{
					case PhotoFilterView.CurvesToolValue.CurvesTypeRed /*1*/:
						i7 = ((((i3 - i) - measuredWidth) / 2) + layoutParams.leftMargin) - layoutParams.rightMargin;
						break;
					case 5: // Adel changed Request.Method.OPTIONS to 5
						i7 = (i3 - measuredWidth) - layoutParams.rightMargin;
						break;
					default:
						i7 = layoutParams.leftMargin;
						break;
				}
				switch (i8)
				{
					case TLRPC.USER_FLAG_PHONE /*16*/:
						i6 = (((((i4 - emojiPadding) - i2) - measuredHeight) / 2) + layoutParams.topMargin) - layoutParams.bottomMargin;
						break;
					case NalUnitTypes.NAL_TYPE_UNSPEC48 /*48*/:
						i6 = layoutParams.topMargin + getPaddingTop();
						break;
					case 80:
						i6 = (((i4 - emojiPadding) - i2) - measuredHeight) - layoutParams.bottomMargin;
						break;
					default:
						i6 = layoutParams.topMargin;
						break;
				}
				if (this.f433b.enterView.isPopupView(childAt))
				{
					i6 = this.f433b.enterView.getBottom();
				}
				childAt.layout(i7, i6, measuredWidth + i7, measuredHeight + i6);
			}
		}
		notifyHeightChanged();
	}

	switch ((i7 & 7) & 7)
				{
					case PhotoFilterView.CurvesToolValue.CurvesTypeRed /*1*/:
						i7 = ((((i3 - i) - measuredWidth) / 2) + layoutParams.leftMargin) - layoutParams.rightMargin;
						break;
					case 5: // Adel changed Request.Method.OPTIONS to 5
						i7 = (i3 - measuredWidth) - layoutParams.rightMargin;
						break;
					default:
						i7 = layoutParams.leftMargin;
						break;
				}
	
	protected void onMeasure(int i, int i2)
	{
		int size  = View.MeasureSpec.getSize(i);
		int size2 = View.MeasureSpec.getSize(i2);
		setMeasuredDimension(size, size2);
		size2 -= getPaddingTop();
		int emojiPadding = getKeyboardHeight() <= AndroidUtilities.dp(20.0f) ? size2 - this.f433b.enterView.getEmojiPadding() : size2;
		int childCount   = getChildCount();
		measureChildWithMargins(this.f433b.enterView, i, 0, i2, 0);
	
			}
		}
	}
}
