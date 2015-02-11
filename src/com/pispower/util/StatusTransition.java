package com.pispower.util;

import android.content.res.Resources;

import com.pispower.R;

public class StatusTransition {
	
	public static String toChinese(String status,Resources resources) {
		if(status.equals(resources.getString(R.string.PROCESSING_ENGLISH))){
			return resources.getString(R.string.PROCESSING);
		}
		if(status.equals(resources.getString(R.string.AUDIT_SUCCESS_ENGLISH))){
			return resources.getString(R.string.AUDIT_SUCCESS);
		}
		if(status.equals(resources.getString(R.string.AUDIT_FAIL_ENGLISH))){
			return resources.getString(R.string.AUDIT_FAIL);
		}
		if(status.equals(resources.getString(R.string.UNAUDIT_ENGLISH))){
			return resources.getString(R.string.UNAUDIT);
		}
		if(status.equals(resources.getString(R.string.FAIL_ENGLISH))){
			return resources.getString(R.string.FAIL);
		}
		
		return resources.getString(R.string.UNKNOWNSTATUS);
	}

}
