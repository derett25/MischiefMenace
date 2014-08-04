package com.linesb.networkmessages;

public class NetworkPing extends MessageBase {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private NetworkPingMessage m_type;
	
	public enum NetworkPingMessage {
		PING,
		HANDSHAKE
	}
	
	public NetworkPing(NetworkPingMessage type, String username) {
		super(MessageBaseType.NETWORK_PING, username);
		m_type = type;
	}
	
	public NetworkPingMessage getType() {
		return m_type;
	}
	
}
