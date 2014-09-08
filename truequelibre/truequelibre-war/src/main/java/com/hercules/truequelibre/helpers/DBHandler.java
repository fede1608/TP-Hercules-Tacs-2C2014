package com.hercules.truequelibre.helpers;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.lang.reflect.ParameterizedType;

import com.googlecode.objectify.NotFoundException;
import com.hercules.truequelibre.domain.InexistentObjectException;
import com.hercules.truequelibre.domain.ItemTL;

public class DBHandler {
	static DBHandler instance=null;
	public DBHandler(){
		super();
	}
	public static DBHandler getInstance(){
		if (instance==null){
			createInstance();
		}
		return instance;
	}
	
	private static void createInstance() {
        if (instance == null) { 
            instance = new DBHandler();
        }
    }
	public void save(Object obj){

		ofy().save().entity(obj).now();
	}
	
	public <T> T get(Class<T> clazz, Long objId) throws InexistentObjectException{
		
		T fetched = null;
		@SuppressWarnings("unchecked")
		Class<T> objectType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

		try {
			 fetched = (T) ofy().load().type(objectType).id(objId).safe();
		} catch(NotFoundException ex){
			InexistentObjectException excepcion = (InexistentObjectException) ex;
			excepcion.setId(objId);
			throw excepcion;
		}
		return fetched;
	}


	public void addUser(Long itemId, String userId) throws InexistentObjectException {
		try {
			ItemTL fetched = this.get(ItemTL.class, itemId);
			fetched.setUser(userId);
			this.save(fetched);
		} catch (InexistentObjectException ex) {
			throw ex;
		}
	}
}
