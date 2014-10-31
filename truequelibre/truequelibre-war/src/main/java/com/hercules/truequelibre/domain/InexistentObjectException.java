package com.hercules.truequelibre.domain;

import com.googlecode.objectify.NotFoundException;

/**
 * Excepcion que se produce cuando un objeto no fue encontrado en la base de datos de la aplicacion
 * <p>En el mensaje de error muestra el id del objeto solicitado</p>
 */
@SuppressWarnings("serial")
public class InexistentObjectException extends NotFoundException{
	private Long id;
	public InexistentObjectException(){
		super();
	}
	public void setId(Long id){
		this.id = id;
	}
	public Long getId(){
		return id;
	}
	public String getMessage(){
		return "Object not found - id:"+ id + "\n";
	}
}
