package com.linesb.mischief.Connections;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.linesb.networkmessages.MessageBase;

/**
* Implementation of the server connection.
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class ServerConnection {
	
    private DatagramSocket m_socket = null;
    private InetAddress m_serverAddress = null;
    private int m_serverPort = -1;

    public ServerConnection(String hostName, int port) throws SocketException, UnknownHostException {
    	// Setting up a server connection
		m_serverPort = port;
		m_serverAddress = InetAddress.getByName(hostName);
		m_socket = new DatagramSocket();
    }
    
    public void setTimeout(int timeout) throws SocketException {
		m_socket.setSoTimeout(timeout);
    }

    public MessageBase receive() {
    	MessageBase receivedMessage = null;
    	try {
			byte[] buffer = new byte[64000];
			final DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
			m_socket.receive(receivedPacket);
			ByteArrayInputStream in = new ByteArrayInputStream(receivedPacket.getData());
            ObjectInputStream is = new ObjectInputStream(in);
            receivedMessage = (MessageBase) is.readObject();
		} catch (IOException e) {	
		} catch (ClassNotFoundException c) {
		}
		
		return receivedMessage;
    }

    public boolean send(MessageBase message) {
    	try {
			 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			 ObjectOutputStream os = new ObjectOutputStream(outputStream);
			 os.writeObject(message);
			 byte[] data = outputStream.toByteArray();
			 DatagramPacket sendPacket = new DatagramPacket(data, data.length, m_serverAddress, m_serverPort);
			 m_socket.send(sendPacket);
		} catch (IOException e) {
			return false;
		}
    	
		return true;
    }

}
