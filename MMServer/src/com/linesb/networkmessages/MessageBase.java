package com.linesb.networkmessages;

import java.io.Serializable;

public abstract class MessageBase implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MessageBaseType m_messageBaseType;
	private String m_user;
	
	public enum MessageBaseType {
		NETWORK_MESSAGE,
		ERROR_MESSAGE,
		NETWORK_PING,
		LOBBY_STATE,
		GAME_STATE,
		GAME_MESSAGE
	}
	
	public MessageBase(MessageBaseType messageBaseType, String username) {
		m_messageBaseType = messageBaseType;
		m_user = username;
	}
	
	public MessageBaseType getMessageType() {
		return m_messageBaseType;
	}
	
	public String getUser() {
		return m_user;
	}

}
