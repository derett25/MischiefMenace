package com.linesb.mischief.Connections;

import java.util.TimerTask;

import com.linesb.networkmessages.NetworkPing;
import com.linesb.networkmessages.NetworkPing.NetworkPingMessage;

/**
* TimerTask class used to send keepalive messages
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class PingMessageTask extends TimerTask {
	
	private Connection m_connection;
	private String m_nickname;
	
	public PingMessageTask(Connection sender, String nickname) {
		m_connection = sender;
		m_nickname = nickname;
	}

	@Override
	public void run() {
		m_connection.sendMessage(new NetworkPing(NetworkPingMessage.PING, m_nickname));
	}
}
