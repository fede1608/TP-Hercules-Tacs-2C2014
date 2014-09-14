package com.hercules.truequelibre.resources;

import static com.googlecode.objectify.ObjectifyService.ofy;

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
			message.add("trade", JsonTL.jsonifyTrade(trade));
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
			if (trade.wantedItem.owner == user.getId()) {

			} else {
				throw new Exception(
						"El trade solicitado no es del usuario o no tiene permisos sobre el mismo.");
			}
			message.add("trade", JsonTL.jsonifyTrade(trade));
			message.addProperty("status", 200);
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
			if (trade.wantedItem.owner == user.getId()) {// Reject

			} else if (trade.offeredItem.owner == user.getId()) {// cancel

			} else {
				throw new Exception("El trade solicitado no es del usuario.");
			}
			message.add("trade", JsonTL.jsonifyTrade(trade));
			message.addProperty("status", 200);
		} catch (Exception ex) {
			message = JsonTL.jsonifyError(ex.getMessage());
		}
		return new StringRepresentation(message.toString(),
				MediaType.APPLICATION_JSON);
	}
}
