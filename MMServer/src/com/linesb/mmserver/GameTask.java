package com.linesb.ftpserver;

import java.util.Timer;
import java.util.TimerTask;

/**
* Representing a round timer.
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class GameTask {
	
	private GameRoom m_room;
	private Timer m_timer;
	
	public GameTask(GameRoom room) {
		m_room = room;
		m_timer = new Timer();
	}
	
	public void schedule(long ms) {
		m_timer.cancel();
		m_timer = new Timer();
		m_timer.schedule(new TimerTask() {
			@Override
			public void run() {
				m_room.expiredCheck();
			}
		}, ms);
	}
	
}
