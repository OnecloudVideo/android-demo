package com.pispower.util;

import android.content.res.Resources;

import com.pispower.R;

public class StatusTransition {
	
	public static String toChinese(String status,Resources resources) {
		if(status.equals(resources.getString(R.string.PROCESSINGENGLISH))){
			return resources.getString(R.string.PROCESSING);
		}
		if(status.equals(resources.getString(R.string.FINISHENGLISH))){
			return resources.getString(R.string.FINISH);
		}
		return resources.getString(R.string.UNKNOWNSTATUS);
	}

}
