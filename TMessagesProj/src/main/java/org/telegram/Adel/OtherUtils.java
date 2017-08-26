package org.telegram.Adel;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.telegram.messenger.ApplicationLoader;

public class OtherUtils
{
	public static boolean isActivityRunning()
	{
		boolean result = false;
		for (RunningTaskInfo runningTaskInfo : ((ActivityManager) ApplicationLoader.applicationContext.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(Integer.MAX_VALUE))
		{
			if (ApplicationLoader.applicationContext.getPackageName().equalsIgnoreCase(runningTaskInfo.baseActivity.getPackageName()))
			{
				result = true;
			}
		}
		return result;
	}

	public static boolean isMyServiceRunning(Class<?> serviceClass)
	{
		for (RunningServiceInfo service : ((ActivityManager) ApplicationLoader.applicationContext.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE))
		{
			if (serviceClass.getName().equals(service.service.getClassName()))
			{
				return true;
			}
		}
		return false;
	}

	public static void writeStringToFile(File file, String data, Charset charset, boolean append) throws Exception
	{
		String dataToWrite;

		if (append)
		{
			dataToWrite = readFileToString(file, charset) + data;
		}
		else
		{
			dataToWrite = data;
		}

		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(dataToWrite);
		writer.close();
	}



	public static byte[] toByteArray(InputStream inputStream) throws Exception
	{
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		byte[]                data   = new byte[8192];
		while (true)
		{
			
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
		BufferedInputStream  bufferedInputStream  = new BufferedInputStream(inputStream);
		byte[]               bytes                = new byte[8192];
		while (true)
		{
			int c = bufferedInputStream.read(bytes);
			if (c != -1)
			{
				bufferedOutputStream.write(bytes, 0, c);
			}
			else
			{
				bufferedInputStream.close();
				bufferedOutputStream.flush();
				return;
			}
		}
			int nRead = inputStream.read(data);
			if (nRead != -1)
			{
				buffer.write(data, 0, nRead);
			}
			else
			{
				buffer.flush();
				return buffer.toByteArray();
			}
		}
	}



	public static int getMaxViewCount(String viewString)
	{
		int    toZarb;
		String type = viewString.substring(viewString.length() - 1);
		if (type.equals("K"))
		{
			toZarb = 1000;
		}
		else if (type.equals("M"))
		{
			toZarb = 1000000;
		}
		else
		{
			toZarb = 1;
		}
		return Integer.parseInt(viewString.substring(0, viewString.length() - 1).replace(" ", "")) * toZarb;
		
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
		BufferedInputStream  bufferedInputStream  = new BufferedInputStream(inputStream);
		byte[]               bytes                = new byte[8192];
		while (true)
		{
			int c = bufferedInputStream.read(bytes);
			if (c != -1)
			{
				bufferedOutputStream.write(bytes, 0, c);
			}
			else
			{
				bufferedInputStream.close();
				bufferedOutputStream.flush();
				return;
			}
		}
	}
}
