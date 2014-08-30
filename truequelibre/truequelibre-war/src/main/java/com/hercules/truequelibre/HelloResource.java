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

import com.hercules.truequelibre.Meli;
import com.ning.http.client.FluentStringsMap;

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
		FluentStringsMap params = new FluentStringsMap();
		params.add("access_token", m.getAccessToken());
		params.add("limit", "5");
		params.add("category", "MLA1000");
		com.ning.http.client.Response response = null;
		try {
			response = m.get("/sites/MLA/hot_items/search", params);
			message+=response.getResponseBody();
		} catch (Exception e) {
			message+="\n";
			message+=e.getMessage();
			message+="\n";
			message+=stackTraceToString(e);
		}
		return new StringRepresentation(message, MediaType.TEXT_PLAIN);
	}

}