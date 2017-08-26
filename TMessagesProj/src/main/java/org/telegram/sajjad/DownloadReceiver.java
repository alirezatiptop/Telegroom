package org.telegram.sajjad;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.File;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by sajjadlp on 8/23/2017.
 */

public class DownloadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            long updateId = pref.getLong("update_file_id",0);
            long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (updateId == reference) {
                String fileName = pref.getString("update_file_name",  "");

                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        + "/" + fileName);

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                i.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(i);
            }
        } catch (Exception e){
            Toast.makeText(context.getApplicationContext(), "Error at opening apk", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
}