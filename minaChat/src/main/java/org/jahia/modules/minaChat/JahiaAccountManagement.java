package org.jahia.modules.minaChat;

import org.apache.vysper.xmpp.addressing.Entity;
import org.apache.vysper.xmpp.authorization.AccountCreationException;
import org.apache.vysper.xmpp.authorization.AccountManagement;

public class JahiaAccountManagement implements AccountManagement{

	public void addUser(Entity username, String password)
			throws AccountCreationException {
		// TODO Auto-generated method stub
		
	}

	public void changePassword(Entity username, String password)
			throws AccountCreationException {
		// TODO Auto-generated method stub
		
	}

	public boolean verifyAccountExists(Entity jid) {
		// TODO Auto-generated method stub
		return false;
	}

}
