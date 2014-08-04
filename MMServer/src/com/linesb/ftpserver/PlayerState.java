package com.linesb.ftpserver;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Iterator;

import com.linesb.ftpserver.GameRoom.CurrentState;

/**
* Representing the current state of all the players.
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class PlayerState {
	
	private ArrayList<Player> m_players = new ArrayList<Player>();
	private int m_cardLeader = -1;
	private Player m_roomLeader = null;
	private RoomConfig m_config;
	
	public PlayerState(RoomConfig config) {
		m_config = config;
	}
	
	public synchronized boolean addPlayer(ClientConnection client) {
		Player player = new Player(client);
		if (m_players.add(player)) {
			if (m_roomLeader == null) {
				m_roomLeader = player;
			}
			return true;
		}
		return false;
	}
	
	public Player getRoomLeader() {
		return m_roomLeader;
	}
	
	public Player getCardLeader() {
		if (m_cardLeader > -1 && m_cardLeader <= m_players.size() - 1) {
			return m_players.get(m_cardLeader);
		} else {
			return null;
		}
	}
	
	public synchronized Player removePlayer(GameRoom room, ClientConnection client) {
		Player p;
		for(Iterator<Player> itr = m_players.iterator(); itr.hasNext();) {
			p = itr.next();
			if (p.isClient(client)) {
				itr.remove();
				if (m_roomLeader.equals(p)) {
					if (m_players.size() > 0) {
						m_roomLeader = m_players.get(0);
					} else {
						m_roomLeader = null;
					}
				}
				return p;
			}
		}
		return null;
	}
	
	public synchronized ArrayList<Player> getPlayers() {
		return m_players;
	}
	
	public Player getPlayer(ClientConnection client) {
		Player p;
		for(Iterator<Player> itr = m_players.iterator(); itr.hasNext();) {
			p = itr.next();
			if (p.isClient(client)) {
				return p;
			}
		}
		return null;
	}
	
	public Player getWinner() {
		Iterator<Player> itr = m_players.iterator();
		while (itr.hasNext()) {
			Player p = itr.next();
			if (p.getScore() >= m_config.getMaxScore()) {
				return p;
			}
		}
		return null;
	}
	
	public void selectCardLeader() {
		if (m_cardLeader < 0 || m_cardLeader >= m_players.size() - 1) {
			m_cardLeader = 0;
		} else {
			m_cardLeader++;
		}
	}
	
	public boolean finishedSelect() {
		Iterator<Player> itr = m_players.iterator();
		int playersCompleted = 0;
		while (itr.hasNext()) {
			Player p = itr.next();
			if (!p.isSelecting() && m_cardLeader > -1 && !m_players.get(m_cardLeader).equals(p)) {
				playersCompleted++;
			}
		}
		return playersCompleted >= m_players.size() - 1;
	}
	

	public void setPlayerFlags(CurrentState state) {
		Player p;
		for(Iterator<Player> itr = m_players.iterator(); itr.hasNext();) {
			p = itr.next();
			if (state.equals(CurrentState.CARDLEADER_SELECTING)) {
				if (m_players.get(m_cardLeader).equals(p)) {
					p.setSelecting(true);
				} else {
					p.setSelecting(false);
				}
			} else if (state.equals(CurrentState.PLAYER_SELECTING)) {
				if (m_players.get(m_cardLeader).equals(p)) {
					p.setSelecting(false);
				} else {
					p.setSelecting(true);
				}
			} else if (state.equals(CurrentState.NEW_GAME)) {
				p.setSelecting(false);
				m_cardLeader = -1;
				p.setScore(0);
			} else {
				p.setSelecting(false);
			}
		}
	}
	
	public synchronized void broadcastState(GameRoom room, DatagramSocket socket) {
		Iterator<Player> itr = m_players.iterator();
		while (itr.hasNext()) {
			Player p = itr.next();
			p.getClient().sendMessage(room.getState(p.getClient()), socket);
		}
	}

	public void clear() {
		m_cardLeader = -1;
		Iterator<Player> itr = m_players.iterator();
		while (itr.hasNext()) {
			Player p = itr.next();
			p.clearCards();
		}
	}
	
}
