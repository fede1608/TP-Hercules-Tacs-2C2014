package com.hercules.truequelibre;

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

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new StringRepresentation(search.toString(), MediaType.TEXT_PLAIN);
	}

}