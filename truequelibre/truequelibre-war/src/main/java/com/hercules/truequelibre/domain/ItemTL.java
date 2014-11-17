package com.hercules.truequelibre.domain;

import java.util.List;
import com.google.appengine.repackaged.com.google.common.base.Function;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.hercules.truequelibre.mlsdk.Meli;

/**
 * Clase que sirve como entidad en la base de datos para persistir los items de los usuarios
 */
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
	@Index 
	public long created;

	public ItemTL(){}
	
	/**
	 * Crea un nuevo item para persistir en la base de datos
	 * @param id : ID que provee MercadoLibre para identificar al item en su dominio
	 * @param owner : usuario que posee el item
	 * @throws ItemNotExistsException
	 */
	public ItemTL(String id, String owner) throws ItemNotExistsException{
		this.idRefML = id; 
		this.owner = owner;
		this.cacheNameImage();
		created = System.currentTimeMillis() / 1000L;
	}
	
	/**
	 * Obtiene la URL de la imagen y nombre del item de MercadoLibre
	 * @throws ItemNotExistsException
	 */
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

	/**
	 * Clase para obtener la referencia de un objeto cuando se quiere obtener un item a la base de datos
	 */
	public static class Deref {
		/**
		 * Singleton Class, Funcion de tipo T
		 * @param <T> 
		 */
	    public static class Func<T> implements Function<Ref<T>, T> {
	        public static Func<Object> INSTANCE = new Func<Object>();

	        /**
	         * Recibe una referencia a un objeto por parametro y devuelve el resultado de aplicar deref
	         */
	        public T apply(Ref<T> ref) {
	            return deref(ref);
	        }
	    }

	    /**
	     * Pregunta si es null
	     * @param ref : referencia a un objeto
	     * @return si no es nulo, el objeto de la referencia
	     */
	    public static <T> T deref(Ref<T> ref) {
	        return ref == null ? null : ref.get();
	    }

	    /**
	     * Transforma una lista de referencias a objetos a una lista de esos objetos que se referencian
	     * @param reflist
	     * @return la lista de objetos
	     */
	    @SuppressWarnings({ "unchecked", "rawtypes" })
	    public static <T> List<T> deref(List<Ref<T>> reflist) {
	        return Lists.transform(reflist, (Func)Func.INSTANCE);
	    }
	}
	
	public void setUser(String userId) {
		this.owner = userId;
	}
	
	/**
	 * Metodo para saber el estado de intecambio del item
	 * @return 
	 * <p><b>True</b>: si el item ya fue intercambiado</p>
	 * <p><b>False</b>: el item esta disponible para ser intercambiado</p>
	 */
	public boolean isExchanged(){
		return exchanged;
	}
	

}
