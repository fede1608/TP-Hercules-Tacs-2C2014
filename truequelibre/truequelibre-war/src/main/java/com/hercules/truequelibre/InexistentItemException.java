package com.hercules.truequelibre;

import com.googlecode.objectify.NotFoundException;

@SuppressWarnings("serial")
public class InexistentItemException extends NotFoundException{
	private Long item;
	public InexistentItemException(){
		super();
	}
	public void setItem(Long notFoundItem){
		item = notFoundItem;
	}
	public Long getItem(){
		return item;
	}
	public String getMessage(){
		return "item not found "+ item + "\n";
	}
}
