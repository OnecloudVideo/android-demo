package com.pispower.util;

import android.content.Context;
import android.util.DisplayMetrics;

public class DpPxConvertion {
	/**
	 * 把dip值转换为pix像素值
	 * 
	 * @param dp
	 * @param context
	 * @return 像素
	 */
	public static int dpToPx(int dp, Context context) {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		int px = Math.round(dp
				* (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return px;
	}

	/**
	 * 把pix像素值转换为dip值
	 * 
	 * @param px
	 * @param context
	 * @return dip值
	 */
	public static int pxToDp(int px, Context context) {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		int dp = Math.round(px
				/ (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return dp;
	}

}
