package com.hercules.truequelibre;

//Se importa estaticamente para poder utilizar los metodos definidos en ofy sin instanciarlo
import static com.googlecode.objectify.ObjectifyService.ofy;
import java.util.List;
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
import com.googlecode.objectify.Ref;
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
		Meli m = new Meli();
		MultivaluedMap<String,String> params = new MultivaluedMapImpl();
		params.add("limit", "50");
		params.add("q", getQuery().getValues("query"));
		params.add("category", "MLA1000");
		JsonArray search= new JsonArray();
		String resultadoPersistido ="\n";
		ItemTL itemPersistido = null;
		ItemTL itemSolicitud = null;
		ItemTL itemSolicitud2 = null;
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
	    	   itemPersistido = new ItemTL(item.get("id").toString(),item.get("title").toString(), item.get("thumbnail").toString());
	    	   itemSolicitud = new ItemTL("unItemEjemploDeSolicitud");
	    	   itemSolicitud2 = new ItemTL("unItemEjemploDeSolicitud2");
	    	   ofy().save().entity(itemSolicitud).now(); //cuando persisto el objeto me autogenera un valor donde puse @Id
	    	   ofy().save().entity(itemSolicitud2).now();
	    	   Ref<ItemTL> ref1 = Ref.create(Key.create(ItemTL.class,itemSolicitud.id)); //armo unas Refs para que se pueda aplicar get() directamente
	    	   Ref<ItemTL> ref2 = Ref.create(Key.create(ItemTL.class,itemSolicitud2.id));
	    	   itemPersistido.agregarSolicitud(ref1);
	    	   itemPersistido.agregarSolicitud(ref2);
	    	   ofy().save().entity(itemPersistido).now();
	    	   search.add(searchItem);
	       }

		} catch (Exception e) {
			e.printStackTrace();
		}
		Key<ItemTL> clave = Key.create(ItemTL.class, itemPersistido.id); //se crea una clave para acceder al item
		ItemTL fetched1 = ofy().load().key(clave).safe(); //la clave era del ultimo item agregado, safe() me
		resultadoPersistido += fetched1.id+"\n";
		resultadoPersistido += fetched1.nombre+"\n";
		resultadoPersistido += "Lista de solicitudes:\n";
		resultadoPersistido += "Primer item de las solicitudes:\n";
		if(fetched1.solicitudesDeIntercambio.isEmpty()){
			resultadoPersistido += "solicitudesDeIntercambio esta vacio";
		}
		else{
			List<ItemTL> listaSolicitudes =  fetched1.getSolicitudes();
			ItemTL itemRecuperado = listaSolicitudes.get(0);
			resultadoPersistido	+= itemRecuperado.nombre + "\n";
			resultadoPersistido += "Segundo item de las solicitudes:\n";
			itemRecuperado = listaSolicitudes.get(1);
			resultadoPersistido	+= itemRecuperado.nombre + "\n";
		}
		return new StringRepresentation(search.toString()+resultadoPersistido, MediaType.TEXT_PLAIN);
	}

}