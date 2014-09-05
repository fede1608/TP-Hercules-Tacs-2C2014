package com.hercules.truequelibre;

import com.hercules.truequelibre.FacebookDataCollector;
import static com.googlecode.objectify.ObjectifyService.ofy;
import com.google.gson.JsonObject;
import com.googlecode.objectify.cmd.Query;
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
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.Post;
import com.restfb.types.User;

public class FriendsResource extends ServerResource{

	public FriendsResource()
	{
		super();
	}	
	public FriendsResource(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
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
			JsonObject j = new JsonObject();
			j.addProperty("error", "El token esta desactualizado, por favor actualicelo");
			message = j.toString();
		}
		return new StringRepresentation(message, MediaType.TEXT_PLAIN);
	}
	
	

	
	
}
