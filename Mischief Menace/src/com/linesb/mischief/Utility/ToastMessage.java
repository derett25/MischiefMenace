package com.linesb.mischief.Utility;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
* Android toast message wrapper
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public abstract class ToastMessage {
	
	public static void makeText (Activity activity, final Context context, final CharSequence text) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(context, text, Toast.LENGTH_LONG).show();
			}
			
		});
	}
	
}
