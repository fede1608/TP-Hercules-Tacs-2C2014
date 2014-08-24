package com.hercules.truequelibre;

import java.io.IOException;
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

import com.mercadolibre.sdk.Meli;
import com.mercadolibre.sdk.MeliException;
import com.ning.http.client.FluentStringsMap;

public class HelloResource extends ServerResource {

	public HelloResource() {
		super();
	}

	public HelloResource(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
	}

	@Override
	protected Representation get() throws ResourceException {
		String message = "Hello World!" + " \n\nTime of request is:"
				+ Calendar.getInstance().getTime().toString();
		
		Meli m = new Meli(7937694478293453L, "UUY3czo96JZDtnsFI2iMt0vIzMBukOtB");
		FluentStringsMap params = new FluentStringsMap();
		params.add("access_token", m.getAccessToken());
		com.ning.http.client.Response response = null;
		try {
			response = m.get("/sites/MLA/hot_items/search?limit=5&category=MLA1000", params);
		} catch (MeliException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			message+=response.getResponseBody();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new StringRepresentation(message, MediaType.TEXT_PLAIN);
	}

}