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
	public void saveItem(ItemTL item){

		ofy().save().entity(item).now();
	}
	
	public ItemTL getItem(Long itemId) throws InexistentItemException{
		ItemTL fetched = null;
		try {
			fetched = ofy().load().type(ItemTL.class).id(itemId).safe();
		} catch(NotFoundException ex){
			InexistentItemException excepcion = (InexistentItemException) ex;
			excepcion.setItem(itemId);
			throw excepcion;
		}
		return fetched;
	}

	public void addUser(Long itemId, String userId) throws InexistentItemException {
		try {
			ItemTL fetched = this.getItem(itemId);
			fetched.setUser(userId);
			this.saveItem(fetched);
		} catch (InexistentItemException ex) {
			throw ex;
		}
	}
}
