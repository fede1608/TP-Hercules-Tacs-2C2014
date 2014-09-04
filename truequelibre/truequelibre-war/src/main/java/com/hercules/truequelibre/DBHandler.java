package com.hercules.truequelibre;

import static com.googlecode.objectify.ObjectifyService.ofy;
import com.googlecode.objectify.NotFoundException;

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
	
	public Object get(Long objId) throws InexistentObjectException{
		Object fetched = null;
		try {
			fetched = ofy().load().type(objId.getClass()).id(objId).safe();
		} catch(NotFoundException ex){
			InexistentObjectException excepcion = (InexistentObjectException) ex;
			excepcion.setId(objId);
			throw excepcion;
		}
		return fetched;
	}

	public void addUser(Long itemId, String userId) throws InexistentObjectException {
		try {
			ItemTL fetched = (ItemTL)this.get(itemId);
			fetched.setUser(userId);
			this.save(fetched);
		} catch (InexistentObjectException ex) {
			throw ex;
		}
	}
}
