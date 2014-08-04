package com.linesb.ftpserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.linesb.networkobject.GameCard;
import com.linesb.networkobject.GameCard.CardType;

/**
* Class that handles the state of the cards.
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class CardState {
	
	private Map<Player, GameCard> m_playedCards = new HashMap<Player, GameCard>();
	private Map<Player, GameCard> m_queueCards = new HashMap<Player, GameCard>();
	private PlayerState m_players;
	private GameCard m_phraseCard = null;
	
	public CardState(PlayerState players) {
		m_players = players;
	}
	
	public void addCard(GameCard card, Player player) {
		m_queueCards.put(player, card);
	}
	
	public void dealCards() {
		Iterator<Player> itr = m_players.getPlayers().iterator();
		while (itr.hasNext()) {
			Player p = itr.next();
			while (p.getCards().size() < 10) {
				p.addCard(CardManager.getRandomCard(CardType.WHITE_CARD, m_players.getPlayers()));
			}
		}
	}
	
	public void finalizePlay() {
		for (Entry<Player, GameCard> entry : m_queueCards.entrySet()) {
		    m_playedCards.put(entry.getKey(), entry.getValue());
		}
		m_queueCards.clear();
	}
	
	public void clearPlayedCards() {
		for (Entry<Player, GameCard> entry : m_playedCards.entrySet()) {
		    entry.getValue().setWinner(false);
		}
		m_playedCards.clear();
		m_queueCards.clear();
	}
	
	public void addPhraseCard() {
		m_phraseCard = CardManager.getRandomCard(CardType.BLACK_CARD, null);
	}
	
	public GameCard getPhraseCard() {
		return m_phraseCard;
	}
	
	public ArrayList<GameCard> getCards() {
		ArrayList<GameCard> playedCards = new ArrayList<GameCard>();
		if (m_playedCards.size() > 0) {
			for (Entry<Player, GameCard> entry : m_playedCards.entrySet()) {
			    playedCards.add(entry.getValue());
			}
		} else {
			for (Entry<Player, GameCard> entry : m_queueCards.entrySet()) {
			    playedCards.add(new GameCard(CardType.WHITE_CARD, ""));
			}
		}
		return playedCards;
	}
	
	public boolean pickWinner(int index) {
		if (index >= 0 && index < m_playedCards.size()) {
			int i = 0;
			for (Entry<Player, GameCard> entry : m_playedCards.entrySet()) {
			    if (i == index) {
			    	entry.getValue().setWinner(true);
			    	entry.getKey().setScore(entry.getKey().getScore() + 1);
			    	return true;
			    }
			    i++;
			}
		}
		return false;
	}
	
	public void returnCards() {
		for (Entry<Player, GameCard> entry : m_queueCards.entrySet()) {
			Player p = entry.getKey();
			p.addCard(entry.getValue());
		}
		m_queueCards.clear();
	}

	public boolean pickRandomWinner() {
		if (m_playedCards.size() > 0) {
			Random generator = new Random();
			Object[] values = m_playedCards.keySet().toArray();
			int randomValue = generator.nextInt(values.length);
			Player player = (Player) values[randomValue];
			values = m_playedCards.values().toArray();
			GameCard card = (GameCard) values[randomValue];
			player.setScore(player.getScore() + 1);
			card.setWinner(true);
			return true;
		} else {
			return false;
		}
	}

	public void clear() {
		clearPlayedCards();
		m_phraseCard = null;
	}
}
