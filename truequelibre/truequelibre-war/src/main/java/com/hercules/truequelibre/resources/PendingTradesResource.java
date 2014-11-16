package com.hercules.truequelibre.resources;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;
import java.util.Map;

import com.hercules.truequelibre.domain.TradeTL;
import com.hercules.truequelibre.helpers.FacebookDataCollector;
import com.google.gson.JsonObject;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.User;
import com.hercules.truequelibre.helpers.JsonTL;;

public class PendingTradesResource extends ServerResource{

	private static final int PENDING = 0;
	private static final int ACCEPTED = 1;
	private static final int DECLINED = 2;
	private static final int CANCELLED = 3;
	
	public PendingTradesResource()
	{
		super();
	}	
	public PendingTradesResource(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.APPLICATION_JSON));
	}

	@Override
	protected Representation get() throws ResourceException {
		String token = getCookies().getValues("accessToken");
		int state = this.parseStringState();
		JsonObject message= JsonTL.getResponse();//new JsonObject();
		try{
		User user=FacebookDataCollector.getInstance().findUserWithRest(token);
		Map<String,String> friends=FacebookDataCollector.getInstance().getFriendsHashMapWithUser(token);
		List<TradeTL> pendingOfferedTrades = ofy().load().type(TradeTL.class).order("-date")
				.filter("offeredItem.owner",user.getId())
				.filter("stateManager.current",state).list();
		
		List<TradeTL> pendingReceivedTrades = ofy().load().type(TradeTL.class).order("-date")
				.filter("wantedItem.owner",user.getId())
				.filter("stateManager.current",state).list();
		
		message.add("receivedTradeRequests", JsonTL.tradesToJsonArray(pendingReceivedTrades,friends));
		message.add("sentTradeRequests", JsonTL.tradesToJsonArray(pendingOfferedTrades,friends));
		}catch(FacebookOAuthException e) {

			message = JsonTL
					.jsonifyError("el token esta desactualizado, por favor actualicelo",JsonTL.UNAUTHORIZED);
		}
		return new StringRepresentation(message.toString(), MediaType.APPLICATION_JSON);
	}
	

	/**
	 * Parsea un string para obtener el estado correspondiente 
	 * @param state
	 * @return El estado correspondiente al string, si no existiese uno lanza una excepcion descriptiva. Por defecto devuelve pending.
	 */
	public int parseStringState()
	{
		
		String state = getQuery().getValues("state");
		if(state == null) return PENDING;
			
		int intState= 
			"accepted".equalsIgnoreCase(state) ? ACCEPTED :
			"declined".equalsIgnoreCase(state) ? DECLINED :
			"cancelled".equalsIgnoreCase(state) ? CANCELLED :
			"pending".equalsIgnoreCase(state) ? PENDING: -1;
		
		if( -1 == intState )
		{
			throw new IllegalArgumentException("El estado "+ state + " no existe. Los estados posibles son accepted, declined, cancelled y pending.");
		}
		return intState;
	}
	
	

	
	
}
