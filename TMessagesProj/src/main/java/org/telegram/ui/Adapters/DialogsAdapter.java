/*
 * This is the source code of Telegram for Android v. 3.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2017.
 */

package org.telegram.ui.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.telegram.Adel.HiddenController;
import org.telegram.Adel.Setting;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.support.widget.RecyclerView;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Components.RecyclerListView;

import java.util.ArrayList;

public class DialogsAdapter extends RecyclerListView.SelectionAdapter
{
	// Adel
	public static final String UNREAD      = "unread";
	public static final String FAVOR       = "favor";
	public static final String BOT         = "bot";
	public static final String CHANNEL     = "channel";
	public static final String ALL         = "all";
	public static final String SUPPERGROUP = "sgroup";
	public static final String CONTACT     = "contact";
	public static final String GROUP       = "ngroup";

	// Adel
	public String  categoryId;
	public boolean hiddenMode;

	private Context mContext;
	private int     dialogsType;
	private long    openedDialogId;
	private int     currentCount;

	public DialogsAdapter(Context context, int type)
	{
		mContext = context;
		dialogsType = type;
	}

	public void setOpenedDialogId(long id)
	{
		openedDialogId = id;
	}



	// Adel
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
		}

		return reth;
	}

	public ArrayList<TLRPC.TL_dialog> getDialogsArray()
	{
		ArrayList<TLRPC.TL_dialog> ret = new ArrayList<>();

		if (dialogsType == 0)
		{
			// Adel
			switch (categoryId)
			{
				case ALL:
					ret = MessagesController.getInstance().dialogs;
					break;
				case CHANNEL:
					ret = MessagesController.getInstance().dialogsChannelOnly;
					break;
				case GROUP:
					ret = MessagesController.getInstance().dialogsJustGroupsOnly;
					break;
			}
		}
		else if (dialogsType == 1)
		{
			ret = MessagesController.getInstance().dialogsServerOnly;
		}
		else if (dialogsType == 2)
		{
			ret = MessagesController.getInstance().dialogsGroupsOnly;
		}

		return ret;
	}

	@Override
	public int getItemCount()
	{
		int count = getDialogsArray().size();
		if (count == 0 && MessagesController.getInstance().loadingDialogs)
		{
			return 0;
		}
		
		{
			count++;
		}
		currentCount = count;
		return count;
	}

	public TLRPC.TL_dialog getItem(int i)
	{
		ArrayList<TLRPC.TL_dialog> arrayList = getDialogsArray();
		if (i < 0 || i >= arrayList.size())
		{
			return null;
		}
		return arrayList.get(i);
	}

	@Override
	public void onViewAttachedToWindow(RecyclerView.ViewHolder holder)
	{
		if (holder.itemView instanceof DialogCell)
		{
			RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType);
		}
	}

	@Override
	public boolean isEnabled(RecyclerView.ViewHolder holder)
	{
		return holder.getItemViewType() != 1;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
	{
		View view = null;
		if (viewType == 0)
		{
			view = new DialogCell(mContext, this); // Adel
		}
		else if (viewType == 1)
		{
			view = new LoadingCell(mContext);
		}
		view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
		return new RecyclerListView.Holder(view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i)
	{
		if (viewHolder.getItemViewType() == 0)
		{
			DialogCell cell = (DialogCell) viewHolder.itemView;
			cell.useSeparator = (i != getItemCount() - 1);
			TLRPC.TL_dialog dialog = getItem(i);

			cell.setDialog(dialog, i, dialogsType);
		}
	}

	@Override
	public int getItemViewType(int i)
	{
		if (i == getDialogsArray().size())
		{
			return 1;
		}
		return 0;
	}
}
