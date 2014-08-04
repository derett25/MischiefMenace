package com.linesb.mischief;

import java.io.Serializable;

/**
* Server entry class.
* 
* <P>Used to populate the login spinner.
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class ServerEntry implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String m_name;
	private String m_address;
	private int m_port;
	
	public ServerEntry(String name, String address, int port) {
		m_name = name;
		m_address = address;
		m_port = port;
	}
	
	public int getPort() {
		return m_port;
	}
	
	public String getAddress() {
		return m_address;
	}
	
	@Override
    public String toString() {
		return m_name;
    }

}
