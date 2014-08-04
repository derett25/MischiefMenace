package com.linesb.networkobject;

import java.io.Serializable;

public class NetworkGameRoom implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String m_name;
	private int m_players;
	private int m_maxPlayers;
	
	public NetworkGameRoom(String name, int players, int maxPlayers) {
		m_name = name;
		m_players = players;
		m_maxPlayers = maxPlayers;
	}
	
	public int getPlayers() {
		return m_players;
	}

	public int getMaxPlayers() {
		return m_maxPlayers;
	}
	
	public String getName() {
		return m_name;
	}
}
