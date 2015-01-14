package com.pispower.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkInspection {

	/**
	 * 检查是否存在网络连接,存在返回为true，否则返回为false
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isExistingAnyNetwork(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINFO = connectivityManager.getActiveNetworkInfo();
		if (networkINFO == null || networkINFO.isConnected() == false) {
			return false;
		}
		return true;
	}

}
