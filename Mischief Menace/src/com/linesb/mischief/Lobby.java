package com.linesb.mischief;

import java.net.SocketException;
import java.net.UnknownHostException;

import com.linesb.mischief.Activity.GameActivity;
import com.linesb.mischief.Connections.ServerConnection;
import com.linesb.mischief.GUI.LobbyGUI;
import com.linesb.mischief.Interface.IReceiveListener;
import com.linesb.mischief.Utility.DialogBox;
import com.linesb.mischief.Utility.ToastMessage;
import com.linesb.networkmessages.ErrorMessage;
import com.linesb.networkmessages.GameState;
import com.linesb.networkmessages.LobbyState;
import com.linesb.networkmessages.MessageBase;
import com.linesb.networkmessages.NetworkMessage;
import com.linesb.networkmessages.NetworkPing;
import com.linesb.networkmessages.ErrorMessage.ErrorCode;
import com.linesb.networkmessages.MessageBase.MessageBaseType;
import com.linesb.networkmessages.NetworkMessage.NetworkMessageType;
import com.linesb.networkmessages.NetworkPing.NetworkPingMessage;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
* Lobby message handler.
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class Lobby implements IReceiveListener {
	
	private Activity m_activity;
	private String m_nickname;
	private LobbyGUI m_lobbyGUI;
	private ConnectionState m_connectionState = null;
	
	public Lobby(Activity activity, String nickname) {
		m_activity = activity;
		m_nickname = nickname;
	}
	
	public void setupLobby(final ServerEntry server) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				if (!connectToLobby(server)) {
					m_activity.finish();
				}
			}
		});
		t.start();
	}

	private boolean connectToLobby(ServerEntry server) {
		try {
			ServerConnection serverConnection = new ServerConnection(server.getAddress(), server.getPort());
			serverConnection.send(new NetworkPing(NetworkPingMessage.HANDSHAKE, m_nickname));
			serverConnection.setTimeout(4000);
			MessageBase message = serverConnection.receive();
			if (message != null) {
				if (message.getMessageType().equals(MessageBaseType.LOBBY_STATE)) {
					LobbyState state = (LobbyState) message;
					serverConnection.setTimeout(30000);
					m_connectionState = (ConnectionState) m_activity.getApplicationContext();
					m_connectionState.setupConnection(serverConnection, this, m_nickname);
					m_lobbyGUI = new LobbyGUI(m_activity);
					m_lobbyGUI.setupView(state, m_connectionState, m_nickname);
					return true;
				} else if (message.getMessageType().equals(MessageBaseType.ERROR_MESSAGE)) {
					ErrorMessage error = (ErrorMessage) message;
					if (error.getCode().equals(ErrorCode.LOGIN_FAILURE)) {
						ToastMessage.makeText(m_activity, m_activity.getApplicationContext(),  "Nickname already in use.");
						return false;
					} else if (error.getCode().equals(ErrorCode.SERVER_FULL)) {
						ToastMessage.makeText(m_activity, m_activity.getApplicationContext(),  "Server is full.");
						return false;
					}
				}
			}
		} catch (SocketException e) {
			ToastMessage.makeText(m_activity, m_activity.getApplicationContext(),  "Exception: " + e.getMessage());
			return false;
		} catch (UnknownHostException e) {
			ToastMessage.makeText(m_activity, m_activity.getApplicationContext(),  "Exception: " + e.getMessage());
			return false;
		}
		
		ToastMessage.makeText(m_activity, m_activity.getApplicationContext(),  "Could not connect to the server.");
		return false;
	}
	
	public void finish() {
		if (m_connectionState != null) {
			m_connectionState.finish();
		}
	}
	
	public void updateListener() {
		if (m_connectionState != null) {
			m_connectionState.setListener(this);
			m_connectionState.sendMessage(new NetworkMessage(NetworkMessageType.UPDATE_STATE, m_nickname));
		}
	}

	@Override
	public void processMessage(MessageBase message) {
		if (message != null) {
			if (message.getMessageType().equals(MessageBaseType.LOBBY_STATE)) {
				LobbyState state = (LobbyState) message;
				m_lobbyGUI.clearView();
				m_lobbyGUI.setupView(state, m_connectionState, m_nickname);
			} else if (message.getMessageType().equals(MessageBaseType.GAME_STATE)) {
				GameState state = (GameState) message;
				GameActivity.show(m_activity, state, m_nickname);
			} else if (message.getMessageType().equals(MessageBaseType.ERROR_MESSAGE)) {
				ErrorMessage error = (ErrorMessage) message;
				if (error.getCode().equals(ErrorCode.PING_FAILURE) || error.getCode().equals(ErrorCode.USER_NOTFOUND)) {
					DialogBox.createOkBox("Error", "You have been kicked from the server.", m_activity, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							m_activity.finish();
						}
					});
				} else if (error.getCode().equals(ErrorCode.ROOM_FULL)) {
					DialogBox.createOkBox("Error", "The room is full.", m_activity);
				} else if (error.getCode().equals(ErrorCode.ROOM_NOTFOUND)) {
					DialogBox.createOkBox("Error", "The room does not exist.", m_activity);
				} else if (error.getCode().equals(ErrorCode.ROOM_CREATION_FAILURE)) {
					DialogBox.createOkBox("Error", "Room creation failed.", m_activity);
				}
			}
		} else {
			if (!m_activity.isFinishing()) {
				DialogBox.createOkBox("Connection lost", "The connection was lost.", m_activity, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						m_activity.finish();
					}
				});
			}
		}
	}

}
