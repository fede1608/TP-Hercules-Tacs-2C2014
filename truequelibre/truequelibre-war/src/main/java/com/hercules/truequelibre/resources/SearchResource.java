package com.hercules.truequelibre.resources;

//Se importa estaticamente para poder utilizar los metodos definidos en ofy sin instanciarlo
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hercules.truequelibre.helpers.JsonTL;
import com.hercules.truequelibre.mlsdk.Meli;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import javax.ws.rs.core.MultivaluedMap;

public class SearchResource extends ServerResource {
    
	public SearchResource() {
		super();
	}

	public SearchResource(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.APPLICATION_JSON));
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
		JsonObject json = new JsonObject();
		MultivaluedMap<String,String> params = new MultivaluedMapImpl();
		params.add("limit", (getQuery().getValues("limit")==null?"10":getQuery().getValues("limit")));//si me dan un limite diferente lo pongo, sino default 10
		params.add("q", getQuery().getValues("query"));
		params.add("category", "MLA1000");
		JsonArray search= new JsonArray();
		try {
	       JsonObject response= m.get("sites/MLA/search", params);
	       JsonArray results=response.getAsJsonArray("results");
	       
	       for(int i=0; i<results.size();i++){   
	    	   JsonObject item= results.get(i).getAsJsonObject();
	    	   JsonObject searchItem= new JsonObject();
	    	   searchItem.add("id", item.get("id"));
	    	   searchItem.add("img", item.get("thumbnail"));
	    	   searchItem.add("name", item.get("title"));
	    	   search.add(searchItem);
	       }
	       json.add("search", search);

		} catch (Exception e) {
			json=JsonTL.jsonifyError(e.getMessage());
		}
		return new StringRepresentation(json.toString(), MediaType.APPLICATION_JSON);
	}

}