package com.linesb.mmserver;

import java.util.ArrayList;

import com.linesb.networkobject.GameCard;

/**
* Representing a player in the game.
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class Player {
	
	private ArrayList<GameCard> m_cards = new ArrayList<GameCard>();
	private ClientConnection m_playerConnection;
	private int m_score = 0;
	private boolean m_winner = false;
	private boolean m_selecting = false;
	
	public Player(ClientConnection connection) {
		m_playerConnection = connection;
	}
	
	public boolean isClient(ClientConnection client) {
		return m_playerConnection.equals(client);
	}
	
	public ClientConnection getClient() {
		return m_playerConnection;
	}
	
	public boolean addCard(GameCard card) {
		return m_cards.add(card);
	}
	
	public boolean hasWon() {
		return m_winner;
	}
	
	public void setWinner(boolean winner) {
		m_winner = winner;
	}
	
	public boolean removeCard(GameCard card) {
		return m_cards.remove(card);
	}
	
	public ArrayList<GameCard> getCards() {
		return m_cards;
	}
	
	public String getName() {
		return m_playerConnection.getName();
	}
	
	public void setScore(int score) {
		m_score = score;
	}
	
	public int getScore() {
		return m_score;
	}
	
	public void setSelecting(boolean select) {
		m_selecting = select;
	}
	
	public boolean isSelecting() {
		return m_selecting;
	}

	public GameCard getCard(int index) {
		if (m_cards.size() > 0 && index >= 0 && index < m_cards.size()) {
			return m_cards.get(index);
		} else {
			return null;
		}
	}

	public void clearCards() {
		m_cards.clear();
	}
}
