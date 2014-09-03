package com.hercules.truequelibre;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import static com.googlecode.objectify.ObjectifyService.ofy;


import com.googlecode.objectify.NotFoundException;

@Entity
public class UserTL {
	@Id String id;
	@Index String name;
	List<ItemTL> items = new ArrayList<ItemTL>(); 
	
	public UserTL(){
		
	}
	
	public UserTL(String id){
		this.id = id;
	}
	
	public UserTL(String id, String name){
		this.id = id;
		this.name = name;
	}
	
	
	public void save(){
		ofy().save().entity(this).now();
	}
	
	public static UserTL load(String id){
		try{
			UserTL u = ofy().load().type(UserTL.class).id(id).safe();
			return u;
		}catch(NotFoundException e){
			return new UserTL(id);
		}
		
	}
	
}
