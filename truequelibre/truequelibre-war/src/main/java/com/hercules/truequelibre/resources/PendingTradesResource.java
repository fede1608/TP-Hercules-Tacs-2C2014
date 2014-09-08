package com.hercules.truequelibre.resources;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.hercules.truequelibre.domain.ItemTL;
import com.hercules.truequelibre.helpers.FacebookDataCollector;
import com.google.gson.JsonArray;
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
import com.restfb.types.User;
import com.hercules.truequelibre.helpers.JsonTL;;

public class PendingTradesResource extends ServerResource{

	public PendingTradesResource()
	{
		super();
	}	
	public PendingTradesResource(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
	}

	@Override
	protected Representation get() throws ResourceException {
		Series<Cookie> cookies = getCookies();	
		String token = cookies.getValues("accessToken");
		User user=FacebookDataCollector.getInstance().findUserWithRest(token);
		
		List<ItemTL> items = ofy().load().type(ItemTL.class)
		.filter("owner", user.getId())
		.filter("exchanged", false).list();
		JsonObject message= new JsonObject();
		message.addProperty("id", user.getId()); 
		message.addProperty("name", user.getName());
		JsonArray jsonItems = new JsonArray();
		for (ItemTL item:items){
			jsonItems.add(JsonTL.jsonifyItemWithRequests(item));
		}
		message.add("itemsForRequest", jsonItems);
		return new StringRepresentation(message.toString(), MediaType.TEXT_PLAIN);
	}
	
	
	

	
	
}
