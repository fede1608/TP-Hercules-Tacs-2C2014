package com.hercules.truequelibre.domain;


@SuppressWarnings("serial")
public class ItemNotExistsException extends Exception {
	
	private String id;
	
	public ItemNotExistsException(String id){
		super();
		this.setId(id);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getMessage(){
		return "el item de id: "+ this.id + " no existe en MercadoLibre";
	}
}
