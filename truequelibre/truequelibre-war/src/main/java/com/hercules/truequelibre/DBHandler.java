package com.hercules.truequelibre;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Result;
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
		try{
		ofy().save().entity(user).now();
		}catch (Exception e){
			throw e;
		}
		
	}
	public UserTL getUser(String userId){
		UserTL fetched=null;
		try{
			Query<UserTL> result= ofy().load().type(UserTL.class).filter("id",userId);
			if(result.count()==0){
				fetched = this.createUser(userId);
			}else{
			fetched=result.first().now();
			}
		}catch(Exception e){
			throw e;
		}
		
		return fetched;
	}
	public void addItem(UserTL user, ItemTL item){
		try{
			UserTL fetched = this.getUser(user.id);
			fetched.addItem(item);
			this.saveUser(fetched);
		}catch(Exception e){
			throw e;
		}
	}
	public UserTL createUser(String userID){
		//cuando tengamos para que el token se consiga desde cualquier 
		//lado de la app habria que agarrar los datos y crear un usuario
		
		return null;
	}
	 
}
