package com.linesb.mischief;

import com.linesb.mischief.Connections.Connection;
import com.linesb.mischief.Connections.ServerConnection;
import com.linesb.mischief.Interface.IReceiveListener;
import com.linesb.networkmessages.MessageBase;

import android.app.Application;

/**
* The current state of the connection
* 
* <P>Android implementation of the singleton pattern.
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class ConnectionState extends Application {
	
	private Connection m_connection = null;
	
	public synchronized void setupConnection(ServerConnection connection, IReceiveListener listener, String nickname) {
		if (m_connection == null) {
			m_connection = new Connection(connection, listener);
			m_connection.setupConnection(nickname);
		}
	}
	
	public synchronized void setListener(IReceiveListener listener) {
		if (m_connection != null) {
			m_connection.setListener(listener);
		}
	}
	
	public synchronized boolean sendMessage(MessageBase message) {
		if (m_connection != null) {
			return m_connection.sendMessage(message);
		} else {
			return false;
		}
	}
	
	public synchronized void finish() {
		if (m_connection != null) {
			m_connection.finish();
			m_connection = null;
		}
	}
}
