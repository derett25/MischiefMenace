package com.linesb.networkmessages;

import java.util.ArrayList;

import com.linesb.networkobject.GameCard;
import com.linesb.networkobject.GamePlayer;

public class GameState extends MessageBase {
	
	private static final long serialVersionUID = 1L;
	private ArrayList<GameCard> m_cards = new ArrayList<GameCard>();
	private ArrayList<GameCard> m_playedCards = new ArrayList<GameCard>();
	private GameCard m_phraseCard;
	private ArrayList<GamePlayer> m_players = new ArrayList<GamePlayer>();
	private boolean m_newGame;
	
	public GameState(ArrayList<GamePlayer> players, ArrayList<GameCard> cards, ArrayList<GameCard> playedCards, GameCard phraseCard, boolean newGame) {
		super(MessageBaseType.GAME_STATE, "Server");
		m_players = players;
		m_cards = cards;
		m_phraseCard = phraseCard;
		m_playedCards = playedCards;
		m_newGame = newGame;
	}
	
	public ArrayList<GameCard> getCards() {
		return m_cards;
	}
	
	public ArrayList<GameCard> getPlayedCards() {
		return m_playedCards;
	}
	
	public ArrayList<GamePlayer> getPlayers() {
		return m_players;
	}
	
	public GameCard getPhraseCard() {
		return m_phraseCard;
	}
	
	public boolean isNewGame() {
		return m_newGame;
	}
}
