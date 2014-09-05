package com.hercules.truequelibre.domain;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.repackaged.com.google.common.base.Function;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
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
	public String id; 
	public String nombre;
	@Index
	public String owner;
	public String imagen;
	@Index 
	Ref<ItemTL> itemDeseado;
	@Load 
	List<Ref<ItemTL>> solicitudesDeIntercambio = new ArrayList<Ref<ItemTL>>();
	@Index 
	Boolean intercambiado = false;

	public ItemTL(){
		
	}
	public ItemTL(String id, String owner){
		this.id = id; 
		this.owner = owner;
		this.cacheNameImage();
		
	}
	
	private void cacheNameImage() {
		try {
			JsonObject item = new Meli().get("items/" + this.id);
			this.imagen=item.get("thumbnail").getAsString();
			this.nombre= item.get("title").getAsString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
	
	public void agregarSolicitud (ItemTL item){
		Ref<ItemTL> r= Ref.create(item);
		this.solicitudesDeIntercambio.add(r);
	}
	
	public List<ItemTL> getSolicitudes() { 
		return Deref.deref(solicitudesDeIntercambio); 
	}
	
	public void setItemDeseado (ItemTL item){
		this.itemDeseado = Ref.create(item);
	}
	public ItemTL getItemDeseado (){
		return this.itemDeseado==null ? null : this.itemDeseado.get();
	}
	
	@Override
	public boolean equals(Object o){
		return ((ItemTL)o).id==this.id;
	}

	public void setUser(String userId) {
		this.owner = userId;
	}
}
