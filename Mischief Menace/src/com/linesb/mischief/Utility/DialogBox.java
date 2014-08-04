package com.linesb.mischief.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
* Android dialog box wrapper
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public final class DialogBox {
	
	public final static void createConfirmBox(final String title, final String content,
	final Activity m_activity, final OnClickListener listener, final OnClickListener cancelListener) {
		m_activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				new AlertDialog.Builder(m_activity)
		        .setTitle(title)
		        .setMessage(content)
		        .setPositiveButton("Confirm", listener)
		        .setNegativeButton("Cancel", cancelListener)
		        .show();
			}

		});
	}
	
	public final static void createOkBox(final String title, final String content, final Activity m_activity) {
		m_activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				new AlertDialog.Builder(m_activity)
		        .setTitle(title)
		        .setMessage(content)
		        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
		        .show();
			}
			
		});
	}
	
	public final static void createOkBox(final String title, final String content, final Activity m_activity, final OnClickListener listener) {
		m_activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				new AlertDialog.Builder(m_activity)
		        .setTitle(title)
		        .setMessage(content)
		        .setPositiveButton("OK", listener)
		        .show();
			}
			
		});
	}

}
