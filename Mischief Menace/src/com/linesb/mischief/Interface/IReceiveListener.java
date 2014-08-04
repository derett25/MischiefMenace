package com.linesb.mischief.Interface;

import com.linesb.networkmessages.MessageBase;

/**
* Message listener interface.
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public interface IReceiveListener {
	public void processMessage(MessageBase message);
}
