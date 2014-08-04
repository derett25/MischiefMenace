package com.linesb.mischief.GUI;

import java.util.Iterator;

import com.linesb.mischief.R;
import com.linesb.mischief.ConnectionState;
import com.linesb.mischief.Utility.DialogBox;
import com.linesb.networkmessages.LobbyState;
import com.linesb.networkmessages.NetworkMessage;
import com.linesb.networkmessages.NetworkMessage.NetworkMessageType;
import com.linesb.networkobject.NetworkGameRoom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
* Class that handles the Lobby GUI.
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class LobbyGUI {
	
	private Activity m_activity;
	private LinearLayout m_layout;
	
	public LobbyGUI(Activity activity) {
		m_activity = activity;
	}
	
	public void clearView() {
		m_activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				m_layout.removeAllViews();
			}
		});
	}
	
	private void setupButtons(final ConnectionState connectionState, final String nickname) {
		Button button = (Button) m_activity.findViewById(R.id.refreshButton);
		
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				connectionState.sendMessage(new NetworkMessage(NetworkMessageType.UPDATE_STATE, nickname));
			}
		});
		
		button = (Button) m_activity.findViewById(R.id.createButton);
		
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(m_activity);
				
				alert.setTitle("Enter room name");
				
				final EditText input = new EditText(m_activity);
				alert.setView(input);
				
				alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String value = input.getText().toString();
						if (value.length() >= 3 && value.length() < 14) {
							connectionState.sendMessage(new NetworkMessage(NetworkMessageType.CREATE_ROOM, nickname, value));
						} else if (value.length() > 13) {
							DialogBox.createOkBox("Error", "Room name can't be more than 13 characters.", m_activity);
						} else {
							DialogBox.createOkBox("Error", "Room name must be at least 3 characters.", m_activity);
						}
					}
				});
				
				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				});
				
				alert.show();
			}
		});
	}
	
	public void setupView(final LobbyState state, final ConnectionState connectionState, final String nickname) {
		int buttonWidth;
		int buttonHeight;
		String size = UIConfig.getSizeName(m_activity.getApplicationContext());
		if (size.equals("small")) {
			buttonWidth = 150;
			buttonHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
		} else if (size.equals("large")) {
			buttonWidth = 450;
			buttonHeight = 80;
		} else if (size.equals("xlarge")) {
			buttonWidth = 550;
			buttonHeight = 125;
		} else {
			buttonWidth = 350;
			buttonHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
		}
		final int BUTTON_WIDTH = buttonWidth;
		final int BUTTON_HEIGHT = buttonHeight;
		
		m_activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (m_activity.findViewById(R.layout.activity_lobby) != m_activity.findViewById(android.R.id.content)) {
					m_activity.setContentView(R.layout.activity_lobby);
					m_layout = (LinearLayout) m_activity.findViewById(R.id.roomLayout);
					setupButtons(connectionState, nickname);
				}
				
				ContextThemeWrapper newContext = new ContextThemeWrapper(m_activity, R.style.button_style);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(BUTTON_WIDTH, BUTTON_HEIGHT);
				lp.setMargins(0, 0, 0, 5);
				Iterator<NetworkGameRoom> itr = state.getRooms().iterator();
				while(itr.hasNext()) {
					NetworkGameRoom room = itr.next();
					final String roomName = room.getName();
					Button button = new Button(newContext);
					button.setText(roomName + " (" + room.getPlayers() + "/" + room.getMaxPlayers() + ")");
					button.setTextAppearance(m_activity, R.style.button_style);
					button.setBackgroundResource(R.drawable.button_style);
					button.setLayoutParams(lp);
					
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							connectionState.sendMessage(new NetworkMessage(NetworkMessageType.JOIN_ROOM, nickname, roomName));
						}
					});
					m_layout.addView(button);
				}
			}
		});
	}
}
