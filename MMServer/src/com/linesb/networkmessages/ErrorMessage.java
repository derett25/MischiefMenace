package com.linesb.networkmessages;

public class ErrorMessage extends MessageBase {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ErrorCode m_code;
	
	public enum ErrorCode {
		LOGIN_FAILURE,
		SERVER_FULL,
		PING_FAILURE,
		USER_NOTFOUND,
		ROOM_NOTFOUND,
		ROOM_FULL, 
		ROOM_CREATION_FAILURE
	}
	
	public ErrorMessage(ErrorCode code) {
		super(MessageBaseType.ERROR_MESSAGE, "Server");
		m_code = code;
	}
	
	public ErrorCode getCode() {
		return m_code;
	}

}
