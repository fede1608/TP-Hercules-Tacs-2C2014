package com.hercules.truequelibre.resources;

import static com.googlecode.objectify.ObjectifyService.ofy;
import java.util.List;
import com.hercules.truequelibre.domain.TradeTL;
import com.hercules.truequelibre.helpers.FacebookDataCollector;
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
		getVariants().add(new Variant(MediaType.APPLICATION_JSON));
	}

	@Override
	protected Representation get() throws ResourceException {
		Series<Cookie> cookies = getCookies();	
		String token = cookies.getValues("accessToken");
		User user=FacebookDataCollector.getInstance().findUserWithRest(token);
		/*
		List<ItemTL> items = ofy().load().type(ItemTL.class)
		.filter("owner", user.getId())
		.filter("exchanged", false).list();
		JsonObject message= new JsonObject();
		message.addProperty("id", user.getId()); 
		message.addProperty("name", user.getName());
		JsonArray jsonItems = new JsonArray();
		for (ItemTL item:items){
			jsonItems.add(JsonTL.jsonifyItemWithRequests(item));
		}*/
		JsonObject message= new JsonObject();
		

		
		List<TradeTL> pendingOfferedTrades = ofy().load().type(TradeTL.class)
				//.filter("offeringUserId",user.getId())
				.filter("offeredItem.owner",user.getId())
				.filter("state",0).list();
		
		List<TradeTL> pendingReceivedTrades = ofy().load().type(TradeTL.class)
				//.filter("requestedUserId",user.getId())
				.filter("wantedItem.owner",user.getId())
				.filter("state",0).list();
		
		message.add("receivedTradeRequests", JsonTL.tradesToJsonArray(pendingReceivedTrades));
		message.add("sentTradeRequests", JsonTL.tradesToJsonArray(pendingOfferedTrades));
		return new StringRepresentation(message.toString(), MediaType.APPLICATION_JSON);
	}
	
	
	
	
	

	
	
}
