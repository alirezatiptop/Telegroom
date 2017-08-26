package org.telegram.Adel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;

public class ToolbarDialogCell extends FrameLayout {

	private BackupImageView imageView;
	private TextView nameTextView;
	private AvatarDrawable avatarDrawable = new AvatarDrawable();
	private RectF rect = new RectF();

	private int lastUnreadCount;
	private int countWidth;
	private StaticLayout countLayout;

	private long dialog_id;

	public ToolbarDialogCell(Context context) {
		super(context);

		imageView = new BackupImageView(context);
		imageView.setRoundRadius(AndroidUtilities.dp(27));
		addView(imageView, LayoutHelper.createFrame(40, 40, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0, 0, 0)); // Adel

		nameTextView = new TextView(context);
		nameTextView.setTextColor(Color.WHITE); // Adel
		nameTextView.setTypeface(AndroidUtilities.getTypeface(null)); // Adel
		nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10); // Adel
		nameTextView.setMaxLines(2);
		nameTextView.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
		nameTextView.setLines(2);
		nameTextView.setEllipsize(TextUtils.TruncateAt.END);
		addView(nameTextView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.LEFT | Gravity.BOTTOM, 0, 0, 0, 0));
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(75), MeasureSpec.EXACTLY));
	}

	public void checkUnreadCounter(int mask) {
		if (mask != 0 && (mask & MessagesController.UPDATE_MASK_READ_DIALOG_MESSAGE) == 0 && (mask & MessagesController.UPDATE_MASK_NEW_MESSAGE) == 0) {
			return;
		}
		TLRPC.TL_dialog dialog = MessagesController.getInstance().dialogs_dict.get(dialog_id);
		if (dialog != null && dialog.unread_count != 0) {
			if (lastUnreadCount != dialog.unread_count) {
				lastUnreadCount = dialog.unread_count;
				String countString = String.format("%d", dialog.unread_count);
				countWidth = Math.max(AndroidUtilities.dp(12), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(countString)));
				countLayout = new StaticLayout(countString, Theme.dialogs_countTextPaint, countWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
				if (mask != 0) {
					invalidate();
				}
			}
		} else if (countLayout != null) {
			if (mask != 0) {
				invalidate();
			}
			lastUnreadCount = 0;
			countLayout = null;
		}
	}

	public void update() {
		int uid = (int) dialog_id;
		TLRPC.FileLocation photo = null;
		if (uid > 0) {
			TLRPC.User user = MessagesController.getInstance().getUser(uid);
			avatarDrawable.setInfo(user);
		} else {
			TLRPC.Chat chat = MessagesController.getInstance().getChat(-uid);
			avatarDrawable.setInfo(chat);
		}
	}

	public void setDialog(int uid, boolean counter, CharSequence name) {
		dialog_id = uid;
		TLRPC.FileLocation photo = null;
		if (uid > 0) {
			TLRPC.User user = MessagesController.getInstance().getUser(uid);
			if (name != null) {
				nameTextView.setText(name);
			} else if (user != null) {
				nameTextView.setText(ContactsController.formatName(user.first_name, user.last_name));
			} else {
				nameTextView.setText("");
			}
			avatarDrawable.setInfo(user);
			if (user != null && user.photo != null) {
				photo = user.photo.photo_small;
			}
		} else {
			TLRPC.Chat chat = MessagesController.getInstance().getChat(-uid);
			if (name != null) {
				nameTextView.setText(name);
			} else if (chat != null) {
				nameTextView.setText(chat.title);
			} else {
				nameTextView.setText("");
			}
			avatarDrawable.setInfo(chat);
			if (chat != null && chat.photo != null) {
				photo = chat.photo.photo_small;
			}
		}
		imageView.setImage(photo, "50_50", avatarDrawable);
		if (counter) {
			checkUnreadCounter(0);
		} else {
			countLayout = null;
		}
	}

	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		boolean result = super.drawChild(canvas, child, drawingTime);
		if (child == imageView && countLayout != null) {
			int top = AndroidUtilities.dp(6);
			int left = AndroidUtilities.dp(5.5f);
			int x = left - AndroidUtilities.dp(5.5f);
			rect.set(x, top, x + countWidth + AndroidUtilities.dp(11), top + AndroidUtilities.dp(23));
			canvas.drawRoundRect(rect, 11.5f * AndroidUtilities.density, 11.5f * AndroidUtilities.density, MessagesController.getInstance().isDialogMuted(dialog_id) ? Theme.dialogs_countGrayPaint : Theme.dialogs_countPaint);
			canvas.save();
			canvas.translate(left, top + AndroidUtilities.dp(4));
			countLayout.draw(canvas);
			canvas.restore();
		}
		return result;
	}
}
