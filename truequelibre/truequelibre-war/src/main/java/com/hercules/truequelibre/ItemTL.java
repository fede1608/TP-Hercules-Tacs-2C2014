package com.hercules.truequelibre;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.repackaged.com.google.common.base.Function;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

//Clase a persistir en la base de datos utilizando objectify
@Entity
public class ItemTL {
	@Id Long id; //con autogenerado
	@Index String nombre;
	String idDuenio;
	@Load List<Ref<ItemTL>> solicitudesDeIntercambio = new ArrayList<Ref<ItemTL>>();
	
	public ItemTL(){
	}
	
	public ItemTL(String nombre){
		this.nombre = nombre;
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
	
	public void agregarSolicitud (Ref<ItemTL> item){
		this.solicitudesDeIntercambio.add(item);
	}
	
	public List<ItemTL> getSolicitudes() { 
		return Deref.deref(solicitudesDeIntercambio); 
	}
	
	@Override
	public boolean equals(Object o){
		return ((ItemTL)o).id==this.id;
	}

	public void setUser(String userId) {
		this.idDuenio = userId;
	}
}
