package com.hercules.truequelibre.helpers;

import static com.googlecode.objectify.ObjectifyService.ofy;
import com.googlecode.objectify.NotFoundException;
import com.hercules.truequelibre.domain.InexistentObjectException;

public class GenDBHandler<T> extends DBHandler {
	
	private Class<T> objectType;
	
    public GenDBHandler(Class<T> objectType)
	{
		this.objectType = objectType;
	}
	
	public T get( Long objId) throws InexistentObjectException{
		
		T fetched = null;


		try {
			System.out.println("tipo: " + objectType!= null? objectType.getSimpleName() : "null");
			System.out.println("objId: " + objId.toString());
			 fetched = (T) ofy().load().type(objectType).id(objId).safe();
		} catch(NotFoundException ex){
			InexistentObjectException excepcion = (InexistentObjectException) ex;
			excepcion.setId(objId);
			throw excepcion;
		}
		return fetched;
	}
	
}