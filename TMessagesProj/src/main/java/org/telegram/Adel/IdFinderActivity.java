package org.telegram.Adel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.v4.internal.view.SupportMenu;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import org.telegram.Adel.EditText;
import org.telegram.Adel.TextView;

import java.util.regex.Pattern;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Peer;
import org.telegram.tgnet.TLRPC.TL_contacts_resolveUsername;
import org.telegram.tgnet.TLRPC.TL_contacts_resolvedPeer;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.BaseFragment;

public class IdFinderActivity extends BaseFragment
{
	public View createView(Context context)
	{
		boolean z;
		if (VERSION.SDK_INT < 21 || AndroidUtilities.isTablet())
		{
			z = false;
		}
		else
		{
			z = true;
		}
erTitle", R.string.IdFinderTitle));
		actionBar.setActionBarMenuOnItemClick(new ActionBarMenuOnItemClick()
		{
			public void onItemClick(int id)
			{
				super.onItemClick(id);
				if (id == -1)
				{
					finishFragment();
				}
			}
		});
		View view = LayoutInflater.from(getParentActivity()).inflate(R.layout.id_finder_layout, null, false);

		final TextView resultTextView = (TextView) view.findViewById(R.id.id_finder_result);
//		resultTextView.setTypeface(AndroidUtilities.getTypeface(null)); // Adel

		final TextView helpTextView = (TextView) view.findViewById(R.id.id_finder_help);
//		helpTextView.setTypeface(AndroidUtilities.getTypeface(null)); // Adel

		final Button openButton = (Button) view.findViewById(R.id.id_finder_open);
		openButton.setTypeface(AndroidUtilities.getTypeface(null)); // Adel

		EditText editText = (EditText) view.findViewById(R.id.id_finder_username);
//		editText.setTypeface(AndroidUtilities.getTypeface(null)); // Adel
		editText.addTextChangedListener(new TextWatcher()
		{
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				final String username = s.toString().replace("@", "");
				if (TextUtils.isEmpty(username) || username.equals(""))
				{
					resultTextView.setText("");
				}
				else if (IdFinderActivity.this.validate(username))
				{
					TL_contacts_resolveUsername tl_contacts_resolveUsername = new TL_contacts_resolveUsername();
					tl_contacts_resolveUsername.username = username;
					resultTextView.setText(LocaleController.getString("SearchingId", R.string.SearchingId));
					resultTextView.setTextColor(-16776961);
					openButton.setEnabled(false);
					ConnectionsManager.getInstance().sendRequest(tl_contacts_resolveUsername, new RequestDelegate()
					{
						public void run(TLObject response, final TL_error error)
						{
							if (error != null)
							{
								AndroidUtilities.runOnUIThread(new Runnable()
								{
									public void run()
									{
										resultTextView.setText(LocaleController.formatString("IdFinderErrorTelgeram", R.string.IdFinderErrorTelgeram, error.text, Integer.valueOf(error.code)));
										resultTextView.setTextColor(SupportMenu.CATEGORY_MASK);
										openButton.setEnabled(false);
									}
								});
								return;
.IdFinderFindOk, username, LocaleController.getString("IdFinderUser", R.string.IdFinderUser));
							}
							final String finalResult = result;
							AndroidUtilities.runOnUIThread(new Runnable()
							{
								public void run()
								{
									resultTextView.setText(finalResult);
									resultTextView.setTextColor(Color.parseColor("#268e40"));
									openButton.setEnabled(true);
									openButton.setOnClickListener(new OnClickListener()
									{
										public void onClick(View v)
										{
											Intent intent = new Intent();
											intent.setAction("android.intent.action.VIEW");
											intent.setPackage(BuildConfig.APPLICATION_ID);
											intent.setData(Uri.parse("https://telegram.me/" + username));
											IdFinderActivity.this.getParentActivity().startActivity(intent);
										}
									});
								}
							});
						}
					});
				}
				else
				{
					resultTextView.setText(LocaleController.getString("IdSyntaxError", R.string.IdSyntaxError));
					resultTextView.setTextColor(SupportMenu.CATEGORY_MASK);
					openButton.setEnabled(false);
				}
			}

			public void afterTextChanged(Editable s)
			{
			}
		});
		
									}
							String result = "";
							Peer   peer   = ((TL_contacts_resolvedPeer) response).peer;
							if (peer.channel_id != 0)
							{
								result = LocaleController.formatString("IdFinderFindOk", R.string.IdFinderFindOk, username, LocaleController.getString("IdFinderChannel", R.string.IdFinderChannel));
							}
							else if (peer.chat_id != 0)
							{
								result = LocaleController.formatString("IdFinderFindOk", R.string.IdFinderFindOk, username, LocaleController.getString("IdFinderBot", R.string.IdFinderBot));
							}
							else if (peer.user_id != 0)
							{
								result = LocaleController.formatString("IdFinderFindOk", R.string

		fragmentView = view;
		return fragmentView;
	}

	private boolean validate(String username)
	{
		return Pattern.compile("^[a-z0-9_]{5,30}$").matcher(username).matches();
	}
}
