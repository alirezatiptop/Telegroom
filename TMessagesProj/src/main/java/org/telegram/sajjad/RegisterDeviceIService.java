package org.telegram.sajjad;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.messenger.UserConfig;

import java.util.HashMap;

/**
 * Created by sajjadlp on 8/24/2017.
 */

public class RegisterDeviceIService extends IntentService {

    public RegisterDeviceIService() {
        super("RegisterDeviceIService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            String url = "http://www.mobodid.com/AdminOperator/telegroom/telegroomregister";
            String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            String phoneModel = Build.MODEL;
            String userId = UserConfig.getClientUserId()+"";
            String securityKey = SimpleKeyMaker.makeDigitString();
            HashMap<String,String> h = new HashMap<String,String>();
            h.put("android_id", androidId);
            h.put("phone_model", phoneModel);
            h.put("user_id", userId);
            h.put("key", securityKey);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = jsonParser.getJSONFromUrlByPost(url,h);
            JSONArray data = jsonObject.getJSONArray("T");//////////////////////////////////////
            if (data.getJSONObject(0).getString("error").equals("0") || data.getJSONObject(0).getString("error").equals("1"))
                PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("device_is_registered", true).commit();

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
