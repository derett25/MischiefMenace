package com.linesb.mischief.GUI;

import android.content.Context;
import android.content.res.Configuration;

/**
* Utility GUI class.
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public final class UIConfig {
	
	public static String getSizeName(Context context) {
	    int screenLayout = context.getResources().getConfiguration().screenLayout;
	    screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;

	    switch (screenLayout) {
	    case Configuration.SCREENLAYOUT_SIZE_SMALL:
	        return "small";
	    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
	        return "normal";
	    case Configuration.SCREENLAYOUT_SIZE_LARGE:
	        return "large";
	    case 4: // Configuration.SCREENLAYOUT_SIZE_XLARGE is API >= 9
	        return "xlarge";
	    default:
	        return "undefined";
	    }
	}
	
}
