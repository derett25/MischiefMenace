package com.linesb.networkmessages;

public class NetworkMessage extends MessageBase {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private NetworkMessageType m_type;
	private String m_parameters;
	
	public enum NetworkMessageType {
		JOIN_ROOM,
		UPDATE_STATE,
		CREATE_ROOM
	}
	
	public NetworkMessage(NetworkMessageType type, String username) {
		super(MessageBaseType.NETWORK_MESSAGE, username);
		m_type = type;
	}
	
	public NetworkMessage(NetworkMessageType type, String username, String parameters) {
		super(MessageBaseType.NETWORK_MESSAGE, username);
		m_type = type;
		m_parameters = parameters;
	}
	
	public NetworkMessageType getType() {
		return m_type;
	}
	
	public String getParameters() {
		return m_parameters;
	}

}
