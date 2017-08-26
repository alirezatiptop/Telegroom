package org.telegram.Adel.NewAdd;

import android.app.Activity;
import android.content.SharedPreferences;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC;

public class MuteController
{
	public static void muteDialog(int dialog_id)
	{
		
		NotificationsController.updateServerNotificationsSettings(dialog_id);
		NotificationsController.getInstance().removeNotificationsForDialog(dialog_id);
		long                     flags;

		MessagesStorage.getInstance().setDialogFlags(dialog_id, flags);
		editor.commit();
		if (dialog != null)
		{
			dialog.notify_settings = new TLRPC.TL_peerNotifySettings();
			dialog.notify_settings.mute_until = Integer.MAX_VALUE;
		}
		
	}
}
