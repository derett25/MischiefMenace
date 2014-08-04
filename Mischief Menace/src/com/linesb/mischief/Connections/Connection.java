package com.linesb.mischief.Connections;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingDeque;

import com.linesb.mischief.Interface.IReceiveListener;
import com.linesb.networkmessages.MessageBase;

/**
* Client network message frontend.
* 
*
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class Connection {
	
	private Thread m_sendThread;
	private Thread m_receiveThread;
	private Timer m_timer = null;
	private ServerConnection m_serverConnection;
	private boolean m_running;
	private LinkedBlockingDeque<MessageBase> sendList;
	private IReceiveListener m_listener;
	
	public Connection(ServerConnection connection, IReceiveListener listener) {
		m_serverConnection = connection;
		m_running = true;
		sendList = new LinkedBlockingDeque<MessageBase>();
		m_listener = listener;
	}
	
	public void setupConnection(String nickname) {
		final int PING_SCHEDULE_PERIOD = 5000;
		
		m_sendThread = new Thread(new Runnable() {
			public void run() {
				while(m_running) {
					MessageBase message = m_serverConnection.receive();
					m_listener.processMessage(message);
				}
			}
		});
		
		m_receiveThread = new Thread(new Runnable() {
			public void run() {
				while(m_running) {
					if(sendList.size() > 0) {
						MessageBase message = sendList.removeFirst();
						m_serverConnection.send(message);
					}
				}
			}
		});
		m_sendThread.start();
		m_receiveThread.start();
		
		m_timer = new Timer();
		
		TimerTask pingTask = new PingMessageTask(this, nickname);
		m_timer.scheduleAtFixedRate(pingTask, 0, PING_SCHEDULE_PERIOD);
	}
	
	public boolean sendMessage(MessageBase message) {
		return sendList.add(message);
	}
	
	public void setListener(IReceiveListener listener) {
		m_listener = listener;
	}
	
	public void finish() {
		m_running = false;
		m_timer.cancel();
	}
}
