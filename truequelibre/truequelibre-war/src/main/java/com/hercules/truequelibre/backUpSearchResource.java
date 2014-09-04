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
import com.hercules.truequelibre.mlsdk.Meli;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.MultivaluedMap;

public class backUpSearchResource extends ServerResource {
    
	public backUpSearchResource() {
		super();
	}

	public backUpSearchResource(Context context, Request request, Response response) {
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
		ItemTL itemPersistido = null;
		String Table="";
		try {
	       JsonObject response= m.get("sites/MLA/search", params);
	       JsonArray results=response.getAsJsonArray("results");
	       Table="<TABLE BORDER>"
	       		+ "<TR>"
	       		+ "<TH>ID"
	       		+ "<TH>Name"
	       		+ "<TH>Image"
	       		+ "<TH>Add to my items"
	       		+ "</TR>";
	       for(int i=0; i<results.size();i++){   
	    	   JsonObject item= results.get(i).getAsJsonObject();
	    	   JsonObject searchItem= new JsonObject();
	    	   searchItem.add("id", item.get("id"));
	    	   searchItem.add("img", item.get("thumbnail"));
	    	   searchItem.add("name", item.get("title"));
	    	   Table+="<TR>"
	    	   		+ "<TD>"+item.get("id")+
	    			   "<TD>"+item.get("title")+
	    			   "<TD>"+item.get("thumbnail")+
	    			   "<TD>"+"<button type=\"submit\" "
	    			   		+ "onclick= \"location.href='http://localhost:8080/api/friends'\" formmethod=\"post\">"
	    			   		+ "Click Here"
	    			   		+ "</button>";
	    	   
	    	   //se crea el item a persistir y se almacena la entidad en la base de datos
	    	   itemPersistido = new ItemTL(item.get("title").toString());
	    	   ofy().save().entity(itemPersistido).now();
	    	   search.add(searchItem);
	       }
	       Table+="</TABLE>";

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new StringRepresentation(Table, MediaType.TEXT_HTML);
	}

}