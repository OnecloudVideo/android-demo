package com.pispower.util;

import java.io.File;

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

}
