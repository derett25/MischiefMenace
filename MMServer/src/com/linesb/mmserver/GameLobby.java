package com.linesb.mmserver;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import com.linesb.networkmessages.ErrorMessage;
import com.linesb.networkmessages.GameMessage;
import com.linesb.networkmessages.LobbyState;
import com.linesb.networkmessages.MessageBase;
import com.linesb.networkmessages.NetworkMessage;
import com.linesb.networkmessages.ErrorMessage.ErrorCode;
import com.linesb.networkmessages.MessageBase.MessageBaseType;
import com.linesb.networkobject.NetworkGameRoom;

/**
* Representing the game lobby.
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class GameLobby extends TimerTask {
	
	private ArrayList<ClientConnection> m_connectedClients = new ArrayList<ClientConnection>();
	private ArrayList<GameRoom> m_gameRooms = new ArrayList<GameRoom>();
	private DatagramSocket m_socket;
	private Timer m_timer = null;
	
	private final static int MIN_ROOMNAME_LENGTH = 3;
	private final static int MAX_ROOMNAME_LENGTH = 13;
	
	public static final int MAX_PLAYERS = 1000;
	
	public GameLobby(DatagramSocket socket) {
		m_timer = new Timer();
    	m_timer.schedule(this, 0, 30000);
    	m_socket = socket;
	}
	
	public boolean addClient(ClientConnection client) {
		ClientConnection c;
		for(Iterator<ClientConnection> itr = m_connectedClients.iterator(); itr.hasNext();) {
    	    c = itr.next();
    	    if(c.hasName(client.getName())) {
    			return false;
    	    } else if (getClient(client) != null) {
    	    	return false;
    	    }
    	}
		return m_connectedClients.add(client);
	}
	
	public ClientConnection getClient(ClientConnection client) {
		ClientConnection c;
		for(Iterator<ClientConnection> itr = m_connectedClients.iterator(); itr.hasNext();) {
    	    c = itr.next();
    	    if(c.equals(client)) {
    			return c;
    	    }
    	}
		return null;
	}
	
	public int getPlayers() {
		return m_connectedClients.size();
	}
	
	public boolean addRoom(GameRoom room) {
		return m_gameRooms.add(room);
	}
	
	public ArrayList<NetworkGameRoom> getRooms() {
		ArrayList<NetworkGameRoom> rooms = new ArrayList<NetworkGameRoom>();
		GameRoom room;
		for(Iterator<GameRoom> itr = m_gameRooms.iterator(); itr.hasNext();) {
			room = itr.next();
			rooms.add(new NetworkGameRoom(room.getName(), room.getPlayers(), room.getMaxPlayers()));
		}
		return rooms;
	}
	
	public GameRoom getClientRoom(ClientConnection client) {
		GameRoom g;
		for(Iterator<GameRoom> itr = m_gameRooms.iterator(); itr.hasNext();) {
			g = itr.next();
			if (g.getPlayer(client) != null) {
				return g;
			}
		}
		return null;
	}
	
	public GameRoom getRoom(String name) {
		GameRoom room;
		for(Iterator<GameRoom> itr = m_gameRooms.iterator(); itr.hasNext();) {
			room = itr.next();
		    if (room.hasName(name)) {
		    	return room;
		    }
		}
		return null;
	}
	
	public void processMessage(MessageBase message, ClientConnection client) {
		if (message.getMessageType().equals(MessageBaseType.NETWORK_MESSAGE)) {
			NetworkMessage parsedMessage = (NetworkMessage) message;
			switch (parsedMessage.getType()) {
				case CREATE_ROOM:
					GameRoom room = getClientRoom(client);
					if (room == null) {
						String parameters = parsedMessage.getParameters();
						if (!Utility.containsIllegal(parameters) && getRoom(parameters) == null && Utility.isWithinBoundaries(parameters, MIN_ROOMNAME_LENGTH, MAX_ROOMNAME_LENGTH)) {
							room = new GameRoom(parameters, m_socket);
							m_gameRooms.add(room);
							room.addPlayer(client);
						} else {
							client.sendMessage(new ErrorMessage(ErrorCode.ROOM_CREATION_FAILURE), m_socket);
						}
					} else {
						// Send room state
						client.sendMessage(room.getState(client), m_socket);
					}
					break;
				case JOIN_ROOM:
					GameRoom gameRoom = getClientRoom(client);
					if (gameRoom == null) {
						String parameters = parsedMessage.getParameters();
						gameRoom = getRoom(parameters);
						if (gameRoom != null) {
							if (!gameRoom.addPlayer(client)) {
								client.sendMessage(new ErrorMessage(ErrorCode.ROOM_FULL), m_socket);
							}
						} else {
							client.sendMessage(new ErrorMessage(ErrorCode.ROOM_NOTFOUND), m_socket);
						}
					} else {
						// Send room state
						client.sendMessage(gameRoom.getState(client), m_socket);
					}
					break;
				default:
				case UPDATE_STATE:
					client.sendMessage(new LobbyState(this.getRooms()), m_socket);
					break;
			}
		} else if (message.getMessageType().equals(MessageBaseType.GAME_MESSAGE)) {
			GameRoom room = getClientRoom(client);
			if (room != null) {
				GameMessage gameMessage = (GameMessage) message;
				room.processMessage(gameMessage, client);
			}
		}
	}

	@Override
	public void run() {
		ClientConnection c;
		// Loop through the connections every 30 seconds and delete crashed clients
		for(Iterator<ClientConnection> itr = m_connectedClients.iterator(); itr.hasNext();) {
			c = itr.next();
		    // If the client hasn't sent a message in 30 seconds
		    if ((System.currentTimeMillis() / 1000) - 30 > c.getPacketTime()) {
		    	// Remove from room
		    	GameRoom room = getClientRoom(c);
		    	if (room != null) {
		    		room.removePlayer(c);
		    	}
		    	System.out.println("Removed crashed client: " + c.getName());
		    	// Delete it
		    	itr.remove();
		    }
		}
		
		GameRoom room;
		for(Iterator<GameRoom> itr = m_gameRooms.iterator(); itr.hasNext();) {
			room = itr.next();
		    // If the room has no players
		    if (room.getPlayers() == 0) {
		    	// Delete it
		    	System.out.println("Removed empty room: " + room.getName());
		    	itr.remove();
		    }
		}
	}
}
