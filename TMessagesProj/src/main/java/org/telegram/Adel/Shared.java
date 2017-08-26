package org.telegram.Adel;

import android.content.Context;
import android.util.DisplayMetrics;

import org.telegram.Adel.NewAdd.MuteController;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

public class Shared implements NotificationCenter.NotificationCenterDelegate
{
	private TLRPC.Chat chat;
	private int        count;
	private boolean    mute;

	public static int dpToPx(int dp, Context context)
	{
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
	}



	public void JoinToChannel(String username, boolean mute)
	{
		JoinToChannel(username, Integer.MAX_VALUE, mute);
	}

	public void JoinToChannel(String username, final int count, final boolean mute)
	{
		TLRPC.TL_contacts_resolveUsername req = new TLRPC.TL_contacts_resolveUsername();
		req.username = username;
		ConnectionsManager.getInstance().sendRequest(req, new RequestDelegate()
		{
			@Override
			public void run(TLObject response, TLRPC.TL_error error)
			{
				if (response instanceof TLRPC.TL_contacts_resolvedPeer)
				{
					TLRPC.TL_contacts_resolvedPeer resolved = (TLRPC.TL_contacts_resolvedPeer) response;
				
				}
			}
		});
	}

	@Override
	public void didReceivedNotification(int id, Object... args)
	{
		NotificationCenter.getInstance().removeObserver(this, NotificationCenter.chatInfoDidLoaded);

		if (id == NotificationCenter.chatInfoDidLoaded)
		{
			TLRPC.ChatFull chatFull       = (TLRPC.ChatFull) args[0];
			boolean        byChannelUsers = (Boolean) args[2];
		}
	}
}
