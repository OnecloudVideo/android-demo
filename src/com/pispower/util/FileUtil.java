package com.pispower.util;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileUtil {

	public static void deleteAllFiles(File file) {
		if (!file.exists()) {
			Log.e(file.getAbsolutePath(), "file is not exist");
			return;
		}
		if (file.isFile()) {
			if (file.delete()) {
				Log.i(file.getName(), "delete");
			}
			return;
		}
		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			for (int i = 0; i < childFiles.length; i++) {
				deleteAllFiles(childFiles[i]);
			}
			file.delete();
			Log.i(file.getName(), "delete");
		}
	}

	public static String getApplicationPrivateFileDir(Context context) {
		String tempDir = null;
		String state = Environment.getExternalStorageState();
		if (!Environment.MEDIA_MOUNTED.equals(state)
				&& !Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			Log.i("ExternalStorageState", state);
			return null;
		}
		File dir = context.getExternalFilesDir(null);
		if (dir == null) {
			Log.i("dir", "is null");
			tempDir = context.getFilesDir().getAbsolutePath();
		} else {
			Log.i(dir.getAbsolutePath(), " is not null");
			tempDir = dir.getAbsolutePath();
		}
		return tempDir;
	}

}
