package com.linesb.networkobject;

import java.io.Serializable;

public class GamePlayer implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int m_score;
	private boolean m_cardLeader;
	private boolean m_roomLeader;
	private boolean m_selecting;
	private boolean m_hasWon;
	private String m_name;
	
	public GamePlayer(String name, int score, boolean cardLeader, boolean selecting, boolean roomLeader, boolean haswon) {
		m_name = name;
		m_score = score;
		m_cardLeader = cardLeader;
		m_roomLeader = roomLeader;
		m_selecting = selecting;
		m_hasWon = haswon;
	}
	
	public String getName() {
		return m_name;
	}
	
	public boolean isCardLeader() {
		return m_cardLeader;
	}
	
	public boolean isRoomLeader() {
		return m_roomLeader;
	}
	
	public boolean isSelecting() {
		return m_selecting;
	}
	
	public boolean hasWon() {
		return m_hasWon;
	}
	
	public int getScore() {
		return m_score;
	}

}
