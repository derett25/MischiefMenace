package com.linesb.networkmessages;

public class GameMessage extends MessageBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GameMessageType m_type;
	private int m_index = -1;
	private String m_content;
	
	public enum GameMessageType {
		START_GAME,
		PLAY_CARD,
		SELECT_CARD,
		LEAVE_GAME
	}

	public GameMessage(String username, GameMessageType type) {
		super(MessageBaseType.GAME_MESSAGE, username);
		m_type = type;
	}
	
	public GameMessage(String username, GameMessageType type, int index) {
		super(MessageBaseType.GAME_MESSAGE, username);
		m_type = type;
		m_index = index;
	}
	
	public GameMessage(String username, GameMessageType type, int index, String content) {
		super(MessageBaseType.GAME_MESSAGE, username);
		m_type = type;
		m_index = index;
		m_content = content;
	}
	
	public GameMessageType getGameMessageType() {
		return m_type;
	}
	
	public String getContent() {
		return m_content;
	}
	
	public int getIndex() {
		return m_index;
	}

}
