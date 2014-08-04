package com.linesb.mmserver;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Iterator;

import com.linesb.networkmessages.GameMessage;
import com.linesb.networkmessages.GameState;
import com.linesb.networkobject.GameCard;
import com.linesb.networkobject.GameCard.CardType;
import com.linesb.networkobject.GamePlayer;

/**
* Representing a game session.
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class GameRoom {
	
	private String m_name;
	private DatagramSocket m_socket;
	private CardState m_cardState;
	private PlayerState m_playerState;
	private CurrentState m_state;
	private GameTask m_task;
	private RoomConfig m_config;
	
	private static long ROUND_TIME = 60000;
	private static long IDLE_TIME = 5000;
	
	public enum CurrentState {
		NEW_GAME,
		IDLE,
		PLAYER_SELECTING,
		CARDLEADER_SELECTING
	}
	
	public GameRoom(String name, DatagramSocket socket) {
		m_name = name;
		m_socket = socket;
		m_state = CurrentState.NEW_GAME;
		m_config = new RoomConfig(10, 10);
		m_playerState = new PlayerState(m_config);
		m_cardState = new CardState(m_playerState);
		m_task = new GameTask(this);
	}
	
	public synchronized boolean addPlayer(ClientConnection client) {
		if (m_playerState.addPlayer(client)) {
			m_playerState.broadcastState(this, m_socket);
			return true;
		}
		return false;
	}
	
	public synchronized Player getPlayer(ClientConnection client) {
		return m_playerState.getPlayer(client);
	}
	
	public synchronized void removePlayer(ClientConnection client) {
		Player p = m_playerState.removePlayer(this, client);
		if (p != null) {
			if (m_playerState.getPlayers().size() < 3) {
				newRound();
			} else if (m_playerState.getCardLeader() != null && m_playerState.getCardLeader().equals(p)) {
				if (m_state.equals(CurrentState.CARDLEADER_SELECTING)) {
					m_cardState.finalizePlay();
					m_cardState.pickRandomWinner();
					cardLeaderCheck();
				}
				newRound();
			} else if (m_state.equals(CurrentState.PLAYER_SELECTING) && m_playerState.finishedSelect()) {
				m_cardState.finalizePlay();
				checkCards();
			}
			m_playerState.broadcastState(this, m_socket);
		}
	}
	
	public synchronized void processMessage(GameMessage message, ClientConnection client) {
		switch(message.getGameMessageType()) {
			case START_GAME:
				if (m_playerState.getRoomLeader() != null) {
					if (m_state.equals(CurrentState.NEW_GAME) && m_playerState.getRoomLeader().isClient(client)) {
						m_playerState.setPlayerFlags(m_state);
						newRound();
					}
				}
				break;
			case PLAY_CARD:
				Player player = getPlayer(client);
				if (player != null) {
					if (m_state.equals(CurrentState.PLAYER_SELECTING)) {
						if (player.isSelecting() && !m_playerState.getCardLeader().equals(player)) {
							GameCard card = player.getCard(message.getIndex());
							if (card != null) {
								if (card.getCardType().equals(CardType.BLANK_CARD)) {
									card.setContent(message.getContent());
								}
								m_cardState.addCard(card, player);
								player.setSelecting(false);
								player.removeCard(card);
								if (m_playerState.finishedSelect()) {
									m_cardState.finalizePlay();
									checkCards();
								}
								m_playerState.broadcastState(this, m_socket);
							}
						}
					}
				}
				break;
			case SELECT_CARD:
				Player cardLeader = getPlayer(client);
				if (m_state.equals(CurrentState.CARDLEADER_SELECTING)) {
					if (cardLeader.isSelecting() && m_playerState.getCardLeader().equals(cardLeader)) {
						if (m_cardState.pickWinner(message.getIndex())) {
							cardLeaderCheck();
							m_playerState.broadcastState(this, m_socket);
						}
					}
				}
				break;
			default:
			case LEAVE_GAME:
				removePlayer(client);
				break;
		}
	}

	private void cardLeaderCheck() {
		m_state = CurrentState.IDLE;
		Player winnerPlayer = m_playerState.getWinner();
		if (winnerPlayer == null) {
			m_task.schedule(IDLE_TIME);
		} else {
			m_playerState.clear();
			m_cardState.clear();
			winnerPlayer.setWinner(true);
			m_state = CurrentState.NEW_GAME;
		}
	}

	public synchronized boolean hasName(String name) {
		return getName().toLowerCase().equals(name.toLowerCase());
	}
	
	public synchronized int getPlayers() {
		return m_playerState.getPlayers().size();
	}
	
	public synchronized int getMaxPlayers() {
		return m_config.getMaxPlayers();
	}
	
	public synchronized String getName() {
		return m_name;
	}
	
	public synchronized GameState getState(ClientConnection client) {
		ArrayList<GamePlayer> players = new ArrayList<GamePlayer>();
		Player p;
		Player pc = null;
		for(Iterator<Player> itr = m_playerState.getPlayers().iterator(); itr.hasNext();) {
			p = itr.next();
			boolean cardLeader = (m_playerState.getCardLeader() != null && p.isClient(m_playerState.getCardLeader().getClient())) ? true : false;
			boolean roomLeader = (m_playerState.getRoomLeader() != null && p.isClient(m_playerState.getRoomLeader().getClient())) ? true : false;
			
			players.add(new GamePlayer(p.getName(), p.getScore(), cardLeader, p.isSelecting(), roomLeader, p.hasWon()));
			if (p.isClient(client)) {
				pc = p;
			}
		}
		
		return new GameState(players, pc.getCards(), m_cardState.getCards(), m_cardState.getPhraseCard(), m_state.equals(CurrentState.NEW_GAME));
	}
	
	public synchronized void expiredCheck() {
		switch (m_state) {
			default:
			case IDLE:
				newRound();
				break;
			case PLAYER_SELECTING:
				m_cardState.finalizePlay();
				checkCards();
				m_playerState.broadcastState(this, m_socket);
				break;
			case CARDLEADER_SELECTING:
				if (m_cardState.getCards().size() > 0) {
					m_cardState.finalizePlay();
					m_cardState.pickRandomWinner();
					cardLeaderCheck();
					m_playerState.broadcastState(this, m_socket);
				}
				break;
		}
	}

	public synchronized void checkCards() {
		if (m_cardState.getCards().size() != 0 && m_cardState.getCards().size() < 2) {
			m_cardState.pickRandomWinner();
			cardLeaderCheck();
		} else if (m_cardState.getCards().size() >= 2) {
			m_state = CurrentState.CARDLEADER_SELECTING;
			m_playerState.setPlayerFlags(m_state);
			m_task.schedule(ROUND_TIME);
		} else {
			m_cardState.returnCards();
			m_state = CurrentState.IDLE;
			m_task.schedule(IDLE_TIME);
		}
	}

	public synchronized void newRound() {
		if (m_playerState.getPlayers().size() >= 3) {
			m_cardState.returnCards();
			m_cardState.clearPlayedCards();
			m_state = CurrentState.PLAYER_SELECTING;
			m_playerState.selectCardLeader();
			m_cardState.dealCards();
			m_cardState.addPhraseCard();
			m_playerState.setPlayerFlags(m_state);
			m_task.schedule(ROUND_TIME);
		} else {
			m_state = CurrentState.NEW_GAME;
			m_playerState.clear();
			m_cardState.clear();
			m_playerState.setPlayerFlags(m_state);
		}
		m_playerState.broadcastState(this, m_socket);
	}
}
