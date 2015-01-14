package com.pispower.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;


public class ContentUriFileConvertion {

	private static final String TAG = "ContentUriFileConvertion";

	/**
	 * 把Conent形式的Uri转换为字符串
	 *
	 * 
	 * @param uri
	 * @param context
	 * @return 是可以直接使用new File(String str) 形式的字符串
	 */
	public static String convertConentUriToFileString(Uri uri, Context context) {
		String filePath = null;
		Log.d(TAG, "URI = " + uri);
		if (uri != null && "content".equals(uri.getScheme())) {
			Cursor cursor = context
					.getContentResolver()
					.query(uri,
							new String[] { android.provider.MediaStore.Video.VideoColumns.DATA },
							null, null, null);
			cursor.moveToFirst();
			filePath = cursor.getString(0);
			cursor.close();
		} else {
			filePath = uri.getPath();
		}
		Log.d(TAG, "converted file path = " + filePath);
		return filePath;
	}

}
