package com.hercules.truequelibre.resources;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.User;
import com.google.gson.JsonObject;
import com.hercules.truequelibre.helpers.FacebookDataCollector;
import com.hercules.truequelibre.helpers.JsonTL;

public class UsersResource extends ParameterGathererTemplateResource {
	
	
	public UsersResource() {
		super();
	}

	public UsersResource(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.APPLICATION_JSON));
	}

	@Override
	protected Representation get() throws ResourceException {
		JsonObject json= JsonTL.getResponse();//new JsonObject();
		String token = getCookies().getValues("accessToken");
		try{
			User user = FacebookDataCollector.getInstance().findUserWithRest(token);
			if(FacebookDataCollector.getInstance().isTheUser(user,this.requestedUser())){
			
			}else{
				if(FacebookDataCollector.getInstance().isAFriend(token, this.requestedUser())){
					user = FacebookDataCollector.getInstance().getFriendData(token, this.requestedUser());
				
				}else{
					json = JsonTL.jsonifyError("No tienes permisos para ver este usuario");
					return new StringRepresentation(json.toString(), MediaType.APPLICATION_JSON);
				}
			}
			json.addProperty("id", this.requestedUser());
			json.addProperty("name",user.getName());
			json.addProperty("profilePic", FacebookDataCollector.getInstance().getUserProfilePic(this.requestedUser()));
		}catch(FacebookOAuthException e) {

			json = JsonTL
					.jsonifyError("El token esta desactualizado. Por favor actualícelo.",JsonTL.UNAUTHORIZED);
		}
		return new StringRepresentation(json.toString(), MediaType.APPLICATION_JSON);
	}
	
}