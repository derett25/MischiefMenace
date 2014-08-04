package com.linesb.mmserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.linesb.networkmessages.MessageBase;

/**
* Representing a client connection.
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class ClientConnection {
	
	private final String m_name;
	private final InetAddress m_address;
	private final int m_port;
	// Used to check crashed clients
	private long m_time;

	public ClientConnection(String name, InetAddress address, int port) {
		m_name = name;
		m_address = address;
		m_port = port;
		m_time = System.currentTimeMillis() / 1000;
	}

	public void sendMessage(MessageBase message, DatagramSocket socket) {
		try {
			 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			 ObjectOutputStream os = new ObjectOutputStream(outputStream);
			 os.writeObject(message);
			 byte[] data = outputStream.toByteArray();
			 DatagramPacket sendPacket = new DatagramPacket(data, data.length, m_address, m_port);
			 socket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized boolean hasName(String nickname) {
		return nickname.toLowerCase().equals(m_name.toLowerCase());
	}
	
	public synchronized String getName() {
		return m_name;
	}
	
	public synchronized InetAddress getAddress() {
		return m_address;
	}
	
	public int getPort() {
		return m_port;
	}
	
	public synchronized void setPacketTime(long time) {
		m_time = time;
	}
	
	public synchronized long getPacketTime() {
		return m_time;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof ClientConnection)) {
	        return false;
	    }
		
		ClientConnection connection = (ClientConnection) other;
		
		return this.getAddress().equals(connection.getAddress())
				&& this.getName().equals(connection.getName())
				&& this.getPort() == connection.getPort();
	}

}

