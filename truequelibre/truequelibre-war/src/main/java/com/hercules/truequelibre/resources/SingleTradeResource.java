package com.hercules.truequelibre.resources;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Map;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.google.gson.JsonObject;
import com.hercules.truequelibre.domain.TradeTL;
import com.hercules.truequelibre.helpers.FacebookDataCollector;
import com.hercules.truequelibre.helpers.JsonTL;
import com.restfb.types.User;

public class SingleTradeResource extends ServerResource {
	public SingleTradeResource() {
		super();
	}

	public SingleTradeResource(Context context, Request request,
			Response response) {
		getVariants().add(new Variant(MediaType.APPLICATION_JSON));
	}

	@Override
	protected Representation get() throws ResourceException {
		JsonObject message = new JsonObject();
		String token = getCookies().getValues("accessToken");
		long id = Long.valueOf((String) this.getRequest().getAttributes()
				.get("tradeId"));
		try {
			User user = FacebookDataCollector.getInstance().findUserWithRest(
					token);
			TradeTL trade = ofy().load().type(TradeTL.class).id(id).now();
			if (trade.wantedItem.owner.equals(user.getId()) || trade.offeredItem.owner.equals(user.getId())) {
				
			} else {
				throw new Exception("El trade solicitado no es del usuario."+trade.wantedItem.owner+" "+trade.offeredItem.owner+" "+user.getId());
			}
			Map<String,String> friends=FacebookDataCollector.getInstance().getFriendsHashMapWithUser(token);
			message.add("trade", JsonTL.jsonifyTrade(trade,friends));
			message.addProperty("status", 200);
		} catch (Exception ex) {
			message = JsonTL.jsonifyError(ex.getMessage());
		}
		return new StringRepresentation(message.toString(),
				MediaType.APPLICATION_JSON);
	}

	@Override
	public Representation post(Representation entity) {
		JsonObject message = new JsonObject();
		String token = getCookies().getValues("accessToken");
		long id = Long.valueOf((String) this.getRequest().getAttributes()
				.get("tradeId"));
		try {
			User user = FacebookDataCollector.getInstance().findUserWithRest(
					token);
			TradeTL trade = ofy().load().type(TradeTL.class).id(id).now();
			if(trade.getState()!=0){
				throw new Exception("El trade solicitado no esta pendiente.");
			}
			if (trade.wantedItem.owner.equals(user.getId())) {
				trade.accept();
				FacebookDataCollector.getInstance().sendNotification(trade.offeredItem.owner, "@["+user.getId()+"] ha aceptado tu solicitud de intercambio.");
			} else {
				throw new Exception(
						"El trade solicitado no es del usuario o no tiene permisos sobre el mismo.");
			}
			message=JsonTL.jsonifyInfo("Se ha aceptado el trade correctamente.");
		} catch (Exception ex) {
			message = JsonTL.jsonifyError(ex.getMessage());
		}
		return new StringRepresentation(message.toString(),
				MediaType.APPLICATION_JSON);
	}

	@Override
	protected Representation delete() throws ResourceException {
		JsonObject message = new JsonObject();
		String token = getCookies().getValues("accessToken");
		long id = Long.valueOf((String) this.getRequest().getAttributes()
				.get("tradeId"));
		try {
			User user = FacebookDataCollector.getInstance().findUserWithRest(
					token);
			TradeTL trade = ofy().load().type(TradeTL.class).id(id).now();
			if(trade.getState()!=0){
				throw new Exception("El trade solicitado no esta pendiente.");
			}
			if (trade.wantedItem.owner.equals(user.getId())) {// Decline
				trade.decline();
				message=JsonTL.jsonifyInfo("Se ha rechazado el trade correctamente.");
			} else if (trade.offeredItem.owner.equals(user.getId())) {// cancel
				trade.cancel();
				message=JsonTL.jsonifyInfo("Se ha cancelado el trade correctamente.");
			} else {
				throw new Exception("El trade solicitado no es del usuario.");
			}

		} catch (Exception ex) {
			message = JsonTL.jsonifyError(ex.getMessage());
		}
		return new StringRepresentation(message.toString(),
				MediaType.APPLICATION_JSON);
	}
}
