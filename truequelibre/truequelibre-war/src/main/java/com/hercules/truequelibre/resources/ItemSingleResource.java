package com.hercules.truequelibre.resources;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Iterator;
import java.util.List;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.googlecode.objectify.Key;
import com.hercules.truequelibre.domain.ItemNotExistsException;
import com.hercules.truequelibre.domain.ItemTL;
import com.hercules.truequelibre.domain.TradeTL;
import com.hercules.truequelibre.domain.UsersDontMatchException;
import com.hercules.truequelibre.helpers.DBHandler;
import com.hercules.truequelibre.helpers.FacebookDataCollector;
import com.hercules.truequelibre.helpers.GenDBHandler;
import com.hercules.truequelibre.helpers.ItemDBHandler;
import com.hercules.truequelibre.helpers.JsonTL;
import com.hercules.truequelibre.mlsdk.Meli;

public class ItemSingleResource extends ParameterGathererTemplateResource {

	public ItemSingleResource() {
		super();
	}

	public ItemSingleResource(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.APPLICATION_JSON));
	}

	@Override
	protected Representation get() throws ResourceException {
		String token = getCookies().getValues("accessToken");
		JsonObject json = new JsonObject();
		try {
			if (FacebookDataCollector.getInstance().informationCanBeShown(
					token, this.requestedUser())) {

				ItemTL item = ofy().load().type(ItemTL.class)
								.id(Long.parseLong(this.requestedItem(),10)).now();
				if (item != null) {
					if (item.owner.equalsIgnoreCase(this.requestedUser())) {
						json = JsonTL.jsonifyItem(item);
						
					} else {
						json = JsonTL
								.jsonifyError("Item pertenece a otro usuario.");
					}
				} else {
					json = JsonTL.jsonifyError("Item no existe");
				}
			} else {
				json = JsonTL.jsonifyError("No tienes permisos necesarios");
			}
		} catch (FacebookOAuthException e) {

			json = JsonTL
					.jsonifyError("el token esta desactualizado, por favor actualicelo");
		}


		return new StringRepresentation(json.toString(), MediaType.APPLICATION_JSON);
	}

	@Override
	protected Representation delete() throws ResourceException {
		String token = getCookies().getValues("accessToken");
		JsonObject json = new JsonObject();
		try {
			if (FacebookDataCollector.getInstance().isTheUser(token,
					this.requestedUser())) {
				ItemTL item = ofy().load().type(ItemTL.class)
						.id(Long.parseLong(this.requestedItem(),10)).now();
				if (item != null) {
					if (item.owner.equalsIgnoreCase(this.requestedUser())) {
						
						ofy().delete()
						.key(Key.create(ItemTL.class, item.id))
						.now();
						
					} else {
						json = JsonTL
								.jsonifyError("Item pertenece a otro usuario.");
					}
				} else {
					json = JsonTL.jsonifyError("Item no existe");
				}
			} else {
				
				json = JsonTL.jsonifyError("DELETE DE TRADE REQUEST EN PROCESO");
			}
		} catch (FacebookOAuthException e) {

			json = JsonTL
					.jsonifyError("el token esta desactualizado, por favor actualicelo");
		}


		return new StringRepresentation(json.toString(), MediaType.APPLICATION_JSON);
	}

	@Override
	public Representation post(Representation entity) {
		// Obtener los datos enviados por post
		Form form = new Form(entity);
		String uid = (String) this.getRequest().getAttributes().get("userId");
		String tokenfb = getCookies().getValues("accessToken");// form.getFirstValue("token");
		User userfb = FacebookDataCollector.getInstance().findUserWithRest(
				tokenfb);

		JsonObject message = new JsonObject();
		try{
		if (requestingTrade(uid, userfb)&&FacebookDataCollector.getInstance().isAFriend(tokenfb, uid)) {
			
		//	ItemDBHandler itemDBHandler = new ItemDBHandler();
			
			ItemTL wantedItem = ofy().load().type(ItemTL.class).id(Long.parseLong(this.requestedItem(),10)).now();
			if(!uid.equals(wantedItem.owner)){
				throw new UsersDontMatchException(uid,this.requestedItem());
			}
			String offeredItemId = form.getFirstValue("offeredItemId");

			ItemTL offeredItem = ofy().load().type(ItemTL.class)
					.id(Long.parseLong(offeredItemId,10)).now();
			if(!userfb.getId().equals(offeredItem.owner)){
				throw new UsersDontMatchException(userfb.getId(),offeredItemId);
			}
			
			TradeTL trade = new TradeTL(offeredItem, wantedItem);
			
			message.addProperty("wantedItemId",this.requestedItem());
			message.addProperty("offeredItemId",offeredItemId);
			message.addProperty("offeredItem",offeredItem != null? offeredItem.name:"null");
			message.addProperty("wantedItem",wantedItem != null? wantedItem.name:"null");
			if(wantedItem!=null && offeredItem != null)
			{
				DBHandler.getInstance().save(trade);
				message.addProperty("success","El pedido de trueque se ha registrado con éxito");
			}
		}else{
			message=JsonTL.jsonifyError("ha ocurrido un error en la creacion del trato, ya sea porque se creo desde su propio usuario o un error inesperado");
		}
		}catch (UsersDontMatchException e){
			message.addProperty("error",e.getMessage());
		}

		return new StringRepresentation(message.toString(),
				MediaType.APPLICATION_JSON);

	}

	private boolean requestingTrade(String uid, User userfb) {
		return !this.isTheUser(uid, userfb) && this.requestedItem() != null && !this.requestedItem().isEmpty();
	}

	private boolean isTheUser(String uid, User userfb) {
		return FacebookDataCollector.getInstance().isTheUser(userfb, uid);
	}

}