package org.telegram.Adel;

import org.telegram.messenger.MessagesController;

public class GhostPorotocol
{
	public static void toggleGhostPortocol()
	{
		boolean m = !Setting.getGhostMode();

		trun(m);
	}

	public static void update()
	{
		boolean m = Setting.getGhostMode();
		trun(m);
	}

	public static void trun(boolean on)
	{
		Setting.setGhostMode(on);
		Setting.setsendDeliver(on);
		Setting.setSendTyping(on);
		if (on)
		{
			//   NotiFicationMaker.createNotification();
		}
		else
		{
			//  NotiFicationMaker.cancelNotification();
		}
		MessagesController.getInstance().reRunUpdateTimerProc();
	}

}
