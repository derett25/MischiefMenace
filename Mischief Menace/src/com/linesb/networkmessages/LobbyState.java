package com.linesb.networkmessages;

import java.util.ArrayList;
import com.linesb.networkobject.NetworkGameRoom;

public class LobbyState extends MessageBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<NetworkGameRoom> m_rooms = new ArrayList<NetworkGameRoom>();
	
	public LobbyState(ArrayList<NetworkGameRoom> rooms) {
		super(MessageBaseType.LOBBY_STATE, "Server");
		m_rooms = rooms;
	}
	
	public ArrayList<NetworkGameRoom> getRooms() {
		return m_rooms;
	}
}
