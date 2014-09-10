package com.hercules.truequelibre.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.appengine.repackaged.com.google.common.base.Function;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.hercules.truequelibre.mlsdk.Meli;

//Clase a persistir en la base de datos utilizando objectify
@Entity
public class ItemTL {
	@Id 
	public Long id; 
	
	@Index
	public String idRefML;
	public String name;
	@Index
	public String owner;
	public String image;
	@Index 
	Boolean exchanged = false;
	public Date created;

	public ItemTL(){
		
	}
	public ItemTL(String id, String owner) throws ItemNotExistsException{
		this.idRefML = id; 
		this.owner = owner;
		this.cacheNameImage();
		created = new Date();
	}
	
	private void cacheNameImage() throws ItemNotExistsException {
		try {
			JsonObject item = new Meli().get("items/" + this.idRefML);
			JsonArray pics= item.getAsJsonArray("pictures");
			this.image=pics.get(0).getAsJsonObject().get("url").getAsString();
			this.name= item.get("title").getAsString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(this.name == null) throw new ItemNotExistsException(this.idRefML);
	}

	public static class Deref {
	    public static class Func<T> implements Function<Ref<T>, T> {
	        public static Func<Object> INSTANCE = new Func<Object>();

	        public T apply(Ref<T> ref) {
	            return deref(ref);
	        }
	    }

	    public static <T> T deref(Ref<T> ref) {
	        return ref == null ? null : ref.get();
	    }

	    @SuppressWarnings({ "unchecked", "rawtypes" })
	    public static <T> List<T> deref(List<Ref<T>> reflist) {
	        return Lists.transform(reflist, (Func)Func.INSTANCE);
	    }
	}
	
	@Override
	public boolean equals(Object o){
		return ((ItemTL)o).id==this.id;
	}

	public void setUser(String userId) {
		this.owner = userId;
	}
}
