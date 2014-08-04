package com.linesb.ftpserver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.linesb.networkmessages.ErrorMessage;
import com.linesb.networkmessages.LobbyState;
import com.linesb.networkmessages.MessageBase;
import com.linesb.networkmessages.NetworkPing;
import com.linesb.networkmessages.ErrorMessage.ErrorCode;
import com.linesb.networkmessages.MessageBase.MessageBaseType;
import com.linesb.networkmessages.NetworkPing.NetworkPingMessage;

/**
* Class used for every new request the server receives.
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class RequestThread implements Runnable {
	
	private GameLobby m_lobby;
	private DatagramPacket m_packet;
	private DatagramSocket m_socket;
	
	private static final int MIN_NICKNAME_LENGTH = 3;
	private static final int MAX_NICKNAME_LENGTH = 20;
	
	public RequestThread(GameLobby lobby, DatagramPacket packet, DatagramSocket socket) {
		m_lobby = lobby;
		m_packet = packet;
		m_socket = socket;
	}

	@Override
	public void run() {
		// Setup message receive
		MessageBase message = null;
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(m_packet.getData());
            ObjectInputStream is = new ObjectInputStream(in);
            try {
            	// Parse the message
            	message = (MessageBase) is.readObject();
			} catch (ClassNotFoundException c) {
				c.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

        if (message != null) {
        	// Create a temporary client
        	ClientConnection client = new ClientConnection(message.getUser(), m_packet.getAddress(), m_packet.getPort());
        	// If the message is of ping-type
        	if (message.getMessageType().equals(MessageBaseType.NETWORK_PING)) {
        		// Parse it to a ping message
        		NetworkPing parsedMessage = (NetworkPing) message;
            	switch (parsedMessage.getType()) {
	            	case HANDSHAKE:
	            		// Try to add the client to the lobby
						if ((m_lobby.getPlayers() + 1) < GameLobby.MAX_PLAYERS) {
							if (!Utility.containsIllegal(message.getUser()) && Utility.isWithinBoundaries(message.getUser(), MIN_NICKNAME_LENGTH, MAX_NICKNAME_LENGTH) && m_lobby.addClient(client)) {
								client.sendMessage(new LobbyState(m_lobby.getRooms()), m_socket);
							} else {
								client.sendMessage(new ErrorMessage(ErrorCode.LOGIN_FAILURE), m_socket);
							}
						} else {
							client.sendMessage(new ErrorMessage(ErrorCode.SERVER_FULL), m_socket);
						}
						break;
					default:
	            	case PING:
	            		ClientConnection pingClient = m_lobby.getClient(client);
	            		if (pingClient != null) {
	            			client.sendMessage(new NetworkPing(NetworkPingMessage.PING, "Server"), m_socket);
	            			GameRoom gameRoom = m_lobby.getClientRoom(client);
	            			if (gameRoom != null) {
	            				if ((System.currentTimeMillis() / 1000) - pingClient.getPacketTime() >= 6) {
	            					pingClient.sendMessage(gameRoom.getState(pingClient), m_socket);
	            				}
	            			}
	            			// Set the ping time to the current local time
	            			pingClient.setPacketTime(System.currentTimeMillis() / 1000);
	            		} else {
	            			client.sendMessage(new ErrorMessage(ErrorCode.PING_FAILURE), m_socket);
	            		}
	            		break;
	            }
        	} else {
        		ClientConnection lobbyClient = m_lobby.getClient(client);
        		if (lobbyClient != null) {
        			m_lobby.processMessage(message, lobbyClient);
        		} else {
        			client.sendMessage(new ErrorMessage(ErrorCode.USER_NOTFOUND), m_socket);
        		}
        	}
        }
	}

}
