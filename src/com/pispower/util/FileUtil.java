package com.pispower.util;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileUtil {

	/**
	 * 删除文件夹及其所属的子文件夹或文件
	 * 
	 * @param file
	 */
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

	/**
	 * 获取应用程序私有文件目录的字符串表示，首先会尝试获取外部的文件目录，如果可以获取
	 * 则返回，否则会去获取内部的文件目录。
	 * 
	 * @param context
	 * @return
	 */
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
