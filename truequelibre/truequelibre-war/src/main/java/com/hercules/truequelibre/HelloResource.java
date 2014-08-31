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










import com.hercules.truequelibre.mlsdk.Meli;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import java.io.IOException;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;




public class HelloResource extends ServerResource {

	public HelloResource() {
		super();
	}

	public HelloResource(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
	}

	@Override
	protected Representation get() throws ResourceException {
		String message = "Trueque Libre!" + " \n\nTime of request is:"
				+ Calendar.getInstance().getTime().toString();
		
		Meli m = new Meli(7937694478293453L, "UUY3czo96JZDtnsFI2iMt0vIzMBukOtB");
		MultivaluedMap<String,String> params = new MultivaluedMapImpl();
		//params.add("access_token", m.getAccessToken());
		params.add("limit", "4");
		params.add("category", "MLA1000");
		
		try {
			
	        //message+=(service.path("sites/MLA/hot_items/search").queryParams(params).accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class));
			//JSONResource response = new Resty().json("https://api.mercadolibre.com/sites/MLA/hot_items/search?limit=5&category=MLA1000");
			message+=m.get("sites/MLA/hot_items/search", params).toString();
			message+=m.getAccessToken();
		} catch (Exception e) {
			message+=e.getMessage();
		}
		return new StringRepresentation(message, MediaType.TEXT_PLAIN);
	}

}