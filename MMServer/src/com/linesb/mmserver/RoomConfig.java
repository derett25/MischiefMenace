package com.linesb.mmserver;

/**
* Class that keeps track of the current configuration of a room.
* 
*
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class RoomConfig {
	
	private int m_maxScore;
	private int m_maxPlayers;
	
	public RoomConfig(int maxScore, int maxPlayers) {
		m_maxScore = maxScore;
		m_maxPlayers = maxPlayers;
	}
	
	public int getMaxScore() {
		return m_maxScore;
	}
	
	public int getMaxPlayers() {
		return m_maxPlayers;
	}
}
