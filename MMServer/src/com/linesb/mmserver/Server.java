package com.linesb.mmserver;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* Class that represents a server.
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class Server {
	
    private DatagramSocket m_socket;
    private GameLobby m_lobby;

    public static void main(String[] args){
		if(args.length < 1) {
		    System.err.println("Usage: java Server portnumber");
		    System.exit(-1);
		}
		try {
		    Server instance = new Server(Integer.parseInt(args[0]));
		    instance.listenForClientMessages();
		} catch(NumberFormatException e) {
		    System.err.println("Error: port number must be an integer.");
		    System.exit(-1);
		}
    }

    private Server(int portNumber) {
    	try {
			m_socket = new DatagramSocket(portNumber);
			m_lobby = new GameLobby(m_socket);
			System.out.println("Loading cards...");
			CardManager.loadCards();
			System.out.println("Server running...");
		} catch (SocketException e) {
			e.printStackTrace();
		}
    }

    private void listenForClientMessages() {
    	ExecutorService executor = Executors.newFixedThreadPool(100);
		do {
			byte[] buffer = new byte[64000];
			DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
			try {
				m_socket.receive(receivedPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			RequestThread requestThread = new RequestThread(m_lobby, receivedPacket, m_socket);
	    	executor.execute(requestThread);
		} while (true);
    }
}

