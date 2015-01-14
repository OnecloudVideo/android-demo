package com.pispower.util;

public class StringUtil {

	/**
	 * 检测输入的字符串是否是空白字符串包括为null的字符串，是返回爲true，否返回false
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

}
