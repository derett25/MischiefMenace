package com.linesb.mischief;

import com.linesb.mischief.Activity.LoginActivity;
import com.linesb.mischief.GUI.GameGUI;
import com.linesb.mischief.Interface.IReceiveListener;
import com.linesb.mischief.Utility.DialogBox;
import com.linesb.networkmessages.ErrorMessage;
import com.linesb.networkmessages.ErrorMessage.ErrorCode;
import com.linesb.networkmessages.GameMessage;
import com.linesb.networkmessages.GameMessage.GameMessageType;
import com.linesb.networkmessages.GameState;
import com.linesb.networkmessages.MessageBase;
import com.linesb.networkmessages.MessageBase.MessageBaseType;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
* Game class handling all received game messages.
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class Game implements IReceiveListener {
	
	private Activity m_activity;
	private String m_nickname;
	private GameGUI m_gameGUI;
	private LocalState m_state;
	private ConnectionState m_connectionState;
	
	public Game(Activity activity, ConnectionState connectionState, GameState gameState, String nickname) {
		m_activity = activity;
		m_connectionState = connectionState;
		m_nickname = nickname;
		m_state = new LocalState(gameState, m_nickname);
		m_gameGUI = new GameGUI(m_activity, m_state, m_connectionState, m_nickname);
		connectionState.setListener(this);
	}
	
	public void finish() {
		m_connectionState.sendMessage(new GameMessage(m_nickname, GameMessageType.LEAVE_GAME));
	}

	@Override
	public void processMessage(MessageBase message) {
		if (message != null) {
			if (message.getMessageType().equals(MessageBaseType.GAME_STATE)) {
				m_state.setState((GameState) message);
				m_gameGUI.setupButtons();
				m_gameGUI.setupCards();
				m_gameGUI.setupText();
			} else if (message.getMessageType().equals(MessageBaseType.ERROR_MESSAGE)) {
				ErrorMessage errorMessage = (ErrorMessage) message;
				if (errorMessage.getCode().equals(ErrorCode.USER_NOTFOUND)) {
					DialogBox.createOkBox("Error", "User not found.", m_activity, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							m_connectionState.finish();
							LoginActivity.show(m_activity.getApplicationContext());
						}
					});
				} else if (errorMessage.getCode().equals(ErrorCode.PING_FAILURE)) {
					DialogBox.createOkBox("Error", "Connection lost.", m_activity, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							m_connectionState.finish();
							LoginActivity.show(m_activity.getApplicationContext());
						}
					});
				}
			}
		} else {
			if (!m_activity.isFinishing()) {
				DialogBox.createOkBox("Connection lost", "The connection was lost.", m_activity, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						m_connectionState.finish();
						LoginActivity.show(m_activity.getApplicationContext());
					}
				});
			}
		}
	}

}
