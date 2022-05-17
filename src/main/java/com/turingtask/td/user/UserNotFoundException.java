package com.turingtask.td.user;

public class UserNotFoundException extends Exception {

	public UserNotFoundException(String identityDocument) {
		super("Could not find user with identity document: " + identityDocument);
	}

	
	
}
