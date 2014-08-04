package com.linesb.networkobject;

import java.io.Serializable;

public class GameCard implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String m_content;
	private boolean m_winner = false;
	private CardType m_type;
	
	public enum CardType {
		BLACK_CARD,
		WHITE_CARD,
		BLANK_CARD
	}
	
	public GameCard(CardType type, String content) {
		m_content = content;
		m_type = type;
	}
	
	public String getContent() {
		return m_content;
	}
	
	public void setContent(String content) {
		m_content = content;
	}
	
	public void setWinner(boolean winner) {
		m_winner = winner;
	}
	
	public boolean hasWon() {
		return m_winner;
	}
	
	public CardType getCardType() {
		return m_type;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof GameCard)) {
	        return false;
	    }
		
		GameCard card = (GameCard) other;
		
		return this.m_content.equals(card.m_content)
				&& this.m_type.equals(card.m_type);
	}
}
