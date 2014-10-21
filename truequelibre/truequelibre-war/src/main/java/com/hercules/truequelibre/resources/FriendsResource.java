package com.hercules.truequelibre.resources;

import com.hercules.truequelibre.helpers.FacebookDataCollector;
import com.hercules.truequelibre.helpers.JsonTL;
import com.google.gson.JsonObject;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Cookie;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

import com.restfb.exception.FacebookOAuthException;

public class FriendsResource extends ServerResource{

	public FriendsResource()
	{
		super();
	}	
	public FriendsResource(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.APPLICATION_JSON));
	}

	@Override
	protected Representation get() throws ResourceException {
		Series<Cookie> cookies = getCookies();	
		String token = cookies.getValues("accessToken");
		String message="";
		try{
			String friends = FacebookDataCollector.getInstance().findFacebookFriendsUsingRest(token);
			message += friends;
		}catch(FacebookOAuthException e){
			JsonObject j = JsonTL.jsonifyError("El token esta desactualizado, por favor actualicelo", JsonTL.UNAUTHORIZED);
			message = j.toString();
		}
		return new StringRepresentation(message, MediaType.APPLICATION_JSON);
	}
	
	

	
	
}
