package com.hercules.truequelibre;

import java.util.Calendar;

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



public class HelloResource extends ServerResource {

	public HelloResource() {
		super();
	}

	public HelloResource(Context context, Request request, Response response) {
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
		String message = "Trueque Libre!" + " \n\nTime of request is:"
				+ Calendar.getInstance().getTime().toString();
		
		Meli m = new Meli(7937694478293453L, "UUY3czo96JZDtnsFI2iMt0vIzMBukOtB");
		MultivaluedMap<String,String> params = new MultivaluedMapImpl();
		params.add("limit", "50");
		params.add("q", "celulares");
		params.add("category", "MLA1000");
		
		try {
	       JsonObject response= m.get("sites/MLA/search", params);
	       JsonArray results=response.getAsJsonArray("results");
	       message+="<table><tr>";
	       for(int i=0; i<results.size();i++){
	    	   if (i % 4 == 0) {//numero es modulo de 4?
	    		   message+="</tr><tr>";
	    	   }
	    	   JsonObject item= results.get(i).getAsJsonObject();
	    	   message+="<td>";
	    	   message+="<img src='"+item.get("thumbnail").getAsString()+"'><b>$"+item.get("price").getAsString()+"</b> "+item.get("title").getAsString();
	    	   message+="</td>";
	       }
	       message+="</tr></table>";
		} catch (Exception e) {
			message+="\n";
			message+=e.getMessage();
			message+="\n";
			message+=stackTraceToString(e);
		}
		return new StringRepresentation(message, MediaType.TEXT_HTML);
	}

}