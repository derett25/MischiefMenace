package com.linesb.mischief;

import java.util.Iterator;

import com.linesb.networkmessages.GameState;
import com.linesb.networkobject.GamePlayer;

/**
* Local implementation of the game state.
* 
* <P>Adds methods relevant to the local user.
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class LocalState {
	
	private GameState m_state;
	private String m_nickname;
	
	public LocalState(GameState state, String nickname) {
		m_state = state;
		m_nickname = nickname;
	}
	
	public void setState(GameState state) {
		m_state = state;
	}

	public GameState getState() {
		return m_state;
	}
	
	public GamePlayer getWinner() {
		Iterator<GamePlayer> itr = m_state.getPlayers().iterator();
		while (itr.hasNext()) {
			GamePlayer player = itr.next();
			if (player.hasWon()) {
				return player;
			}
		}
		return null;
	}
	
	public boolean isRoomLeader() {
		GamePlayer player;
		for(Iterator<GamePlayer> itr = m_state.getPlayers().iterator(); itr.hasNext();) {
			player = itr.next();
			if (player.getName().equals(m_nickname) && player.isRoomLeader()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isCardLeader() {
		GamePlayer player;
		for(Iterator<GamePlayer> itr = m_state.getPlayers().iterator(); itr.hasNext();) {
			player = itr.next();
			if (player.getName().equals(m_nickname) && player.isCardLeader()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isSelecting() {
		GamePlayer player;
		for(Iterator<GamePlayer> itr = m_state.getPlayers().iterator(); itr.hasNext();) {
			player = itr.next();
			if (player.getName().equals(m_nickname) && player.isSelecting()) {
				return true;
			}
		}
		return false;
	}

	public boolean isEnoughPlayers() {
		return m_state.getPlayers().size() >= 3;
	}
	
}
