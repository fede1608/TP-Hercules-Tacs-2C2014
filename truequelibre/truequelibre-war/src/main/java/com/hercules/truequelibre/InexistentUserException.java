package com.hercules.truequelibre;

@SuppressWarnings("serial")
public class InexistentUserException extends Exception{
	private String user = "";
	public InexistentUserException(){
		super();
	}
	public void setUser(String notFoundUser){
		user=notFoundUser;
	}
	public String getUser(){
		return user;
	}
}
