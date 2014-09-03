package com.hercules.truequelibre;

import static com.googlecode.objectify.ObjectifyService.ofy;
import com.googlecode.objectify.cmd.Query;

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
	public void saveUser(UserTL user){

		ofy().save().entity(user).now();

		
	}
	public UserTL getUser(String userId) throws InexistentUserException{
		UserTL fetched= ofy().load().type(UserTL.class).id(userId).now();
		if(fetched==null){
			InexistentUserException ex= new InexistentUserException();
			ex.setUser(userId);
			throw ex;
		}
		return fetched;
	}
	public void addItem(UserTL user, ItemTL item) throws InexistentUserException,Exception{
		try{
			UserTL fetched = this.getUser(user.id);
			fetched.addItem(item);
			this.saveUser(fetched);
		}catch(InexistentUserException ex){
			throw ex;			
		}catch(Exception e){
			throw e;
		}
	}
}
