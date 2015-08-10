package com.pispower.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.provider.DocumentsContract;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

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

		String realPath;
		if (Build.VERSION.SDK_INT < 11)
			realPath = getRealPathFromURI_BelowAPI11(context, uri);

			// SDK >= 11 && SDK < 19
		else if (Build.VERSION.SDK_INT < 19)
			realPath = getRealPathFromURI_API11to18(context, uri);

			// SDK > 19 (Android 4.4)
		else
			realPath = getRealPathFromURI_API19(context, uri);

		return  realPath;
	}


	@SuppressLint("NewApi")
	public static String getRealPathFromURI_API19(Context context, Uri uri){
		String filePath = "";
		String wholeID = DocumentsContract.getDocumentId(uri);

		// Split at colon, use second item in the array
		String id = wholeID.split(":")[1];

		String[] column = { MediaStore.Video.Media.DATA };

		// where id is equal to
		String sel = MediaStore.Video.Media._ID + "=?";

		Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				column, sel, new String[]{ id }, null);

		int columnIndex = cursor.getColumnIndex(column[0]);

		if (cursor.moveToFirst()) {
			filePath = cursor.getString(columnIndex);
		}
		cursor.close();
		return filePath;
	}

	@SuppressLint("NewApi")
	public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
		String[] proj = { MediaStore.Video.Media.DATA };
		String result = null;

		CursorLoader cursorLoader = new CursorLoader(
				context,
				contentUri, proj, null, null, null);
		Cursor cursor = cursorLoader.loadInBackground();

		if(cursor != null){
			int column_index =
					cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
			cursor.moveToFirst();
			result = cursor.getString(column_index);
		}
		return result;
	}

	public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri){
		String[] proj = { MediaStore.Video.Media.DATA };
		Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
		int column_index
				= cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
}
