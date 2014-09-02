package com.hercules.truequelibre;

//Se importa estaticamente para poder utilizar los metodos definidos en ofy sin instanciarlo
import static com.googlecode.objectify.ObjectifyService.ofy;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.hercules.truequelibre.ItemTL;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Result;
import com.hercules.truequelibre.mlsdk.Meli;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import javax.ws.rs.core.MultivaluedMap;



public class SearchResource extends ServerResource {
    
	public SearchResource() {
		super();
	}

	public SearchResource(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
	}
	
	public String stackTraceToString(Throwable e) {
	    StringBuilder sb = new StringBuilder();
	    for (StackTraceElement element : e.getStackTrace()) {
	        sb.append(element.toString());
	        sb.append("\n");
	    }
	    return sb.toString();
	}

	@Override
	protected Representation get() throws ResourceException {
		Meli m = new Meli(7937694478293453L, "UUY3czo96JZDtnsFI2iMt0vIzMBukOtB");
		MultivaluedMap<String,String> params = new MultivaluedMapImpl();
		params.add("limit", "50");
		params.add("q", getQuery().getValues("query"));
		params.add("category", "MLA1000");
		JsonArray search= new JsonArray();
		String resultadoPersistido ="\n";
		ItemTL itemPersistido = null;
		try {
	       JsonObject response= m.get("sites/MLA/search", params);
	       JsonArray results=response.getAsJsonArray("results");
	       
	       for(int i=0; i<results.size();i++){   
	    	   JsonObject item= results.get(i).getAsJsonObject();
	    	   JsonObject searchItem= new JsonObject();
	    	   searchItem.add("id", item.get("id"));
	    	   searchItem.add("img", item.get("thumbnail"));
	    	   searchItem.add("name", item.get("title"));
	    	   //se crea el item a persistir y se almacena la entidad en la base de datos
	    	   itemPersistido = new ItemTL(item.get("id").toString(),item.get("title").toString());
	    	   ofy().save().entity(itemPersistido).now();
	    	   search.add(searchItem);
	       }

		} catch (Exception e) {
			e.printStackTrace();
		}
		Key<ItemTL> clave = Key.create(ItemTL.class, itemPersistido.id); //se crea una clave para acceder al item
		Result<ItemTL> result = ofy().load().key(clave); //la clave era del ultimo item agregado y la carga es asincronica
		ItemTL fetched1 = result.now(); //cuando le mando now() recien ahi instancia el objeto
		resultadoPersistido += fetched1.id+"\n";
		resultadoPersistido += fetched1.nombre+"\n";
		return new StringRepresentation(search.toString()+resultadoPersistido, MediaType.TEXT_PLAIN);
	}

}