package com.hercules.truequelibre;

import com.googlecode.objectify.NotFoundException;

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
