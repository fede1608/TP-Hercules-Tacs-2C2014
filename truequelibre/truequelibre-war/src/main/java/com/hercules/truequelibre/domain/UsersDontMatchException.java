package com.hercules.truequelibre.domain;

/**
 * Excepcion que se produce cuando un item solicitado no se corresponde con el usuario especificado
 */
@SuppressWarnings("serial")
public class UsersDontMatchException extends Exception {
	
	private String requestedId;
	private String requestedItemId;
	
	public UsersDontMatchException(String requested,String requestedItem){
		super();
		requestedId=requested;
		requestedItemId=requestedItem;
	}
	
	public String getMessage(){
		return "el usuario pedido: "+ requestedId + " no es el duenio del item pedido: "+requestedItemId;
	}
}
