package com.hercules.truequelibre.resources;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
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
		JsonObject json=new JsonObject();
		String token = getCookies().getValues("accessToken");
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
		return new StringRepresentation(json.toString(), MediaType.APPLICATION_JSON);
	}
	
}