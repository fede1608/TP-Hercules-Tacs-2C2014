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
import com.hercules.truequelibre.helpers.DBHandler;
import com.hercules.truequelibre.helpers.FacebookDataCollector;
import com.hercules.truequelibre.helpers.GenDBHandler;
import com.hercules.truequelibre.helpers.ItemDBHandler;
import com.hercules.truequelibre.helpers.JsonTL;
import com.hercules.truequelibre.mlsdk.Meli;

public class ItemsResource extends ParameterGathererTemplateResource {

	public ItemsResource() {
		super();
	}

	public ItemsResource(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.APPLICATION_JSON));
	}

	@Override
	protected Representation get() throws ResourceException {
		String token = getCookies().getValues("accessToken");
		JsonObject json = new JsonObject();
		if (this.requestedItem() == null) {// request a api/users/{userid}/items
			if (FacebookDataCollector.getInstance().isTheUser(token,
					this.requestedUser())) {
				List<ItemTL> items = ofy().load().type(ItemTL.class)
						.filter("owner", this.requestedUser())
						.filter("exchanged", false).list();
				json = new JsonObject();
				JsonArray itemsJson = new JsonArray();
				Iterator<ItemTL> iterator = items.iterator();
				while (iterator.hasNext()) {
					JsonObject item = new JsonObject();
					ItemTL i = iterator.next();
					item.addProperty("id", i.id);
					item.addProperty("idRefML", i.idRefML);
					item.addProperty("name", i.name);
					item.addProperty("img", i.image);
					item.addProperty("owner", i.owner);
					itemsJson.add(item);
				}
				json.add("items", itemsJson);
			} else {
				json = JsonTL
						.jsonifyError("Usuario no corresponde con el token");
			}
		} else {
			try {
				if (FacebookDataCollector.getInstance().informationCanBeShown(
						token, this.requestedUser())) {

					ItemTL item = ofy().load().type(ItemTL.class)
							.id(this.requestedItem()).now();
					if (item != null) {
						if (item.owner.equalsIgnoreCase(this.requestedUser())) {
							json = JsonTL.jsonifyItemWithRequests(item);
							
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
		}

		return new StringRepresentation(json.toString(), MediaType.APPLICATION_JSON);
	}

	public String itemInfo(String requestedItem) {
		JsonObject searchItem = new JsonObject();
		try {
			JsonObject item = new Meli().get("items/" + this.requestedItem());
			searchItem.add("idRefML", item.get("id"));
			searchItem.add("img", item.get("thumbnail"));
			searchItem.add("name", item.get("title"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return searchItem.toString();
	}

	@Override
	protected Representation delete() throws ResourceException {
		String token = getCookies().getValues("accessToken");
		JsonObject json = new JsonObject();
		if (this.requestedItem() == null) {// request a api/users/{userid}/items
			json = JsonTL.jsonifyError("No se ha seleccionado un item.");
		} else {
			try {
				if (FacebookDataCollector.getInstance().isTheUser(token,
						this.requestedUser())) {
							ItemTL item = ofy().load().type(ItemTL.class)
							.id(this.requestedItem()).now();
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
					
					TradeTL trade = ofy().load().type(TradeTL.class).filter("wantedItem.id",this.requestedItem()).first().now();
							  /*
							  ofy().delete()
											.key(Key.create(TradeTL.class, trade.id))
											.now();*/
					if(trade != null)
					{
						trade.active =  false;
						json.addProperty("message",trade.toString());
						DBHandler.getInstance().save(trade);
					}
					else
					{
						json = JsonTL.jsonifyError("No hay una solicitud de trueque a eliminar para este item");
					}
					//
				}
			} catch (FacebookOAuthException e) {

				json = JsonTL
						.jsonifyError("el token esta desactualizado, por favor actualicelo");
			}
		}

		return new StringRepresentation(json.toString(), MediaType.APPLICATION_JSON);
	}

	@Override
	public Representation post(Representation entity) {
		// Obtener los datos enviados por post
		Form form = new Form(entity);
		String uid = (String) this.getRequest().getAttributes().get("userId");
		String itemId = form.getFirstValue("itemId");
		String tokenfb = getCookies().getValues("accessToken");// form.getFirstValue("token");
		User userfb = FacebookDataCollector.getInstance().findUserWithRest(
				tokenfb);

		JsonObject message = new JsonObject();
		if (creatingItemInOtherUser(uid, userfb)) {// autenticar
			message.addProperty("error",
					"Un usuario no puede crear un item para otro usuario.");

		} else if (requestingTrade(uid, userfb)) {
			
		//	ItemDBHandler itemDBHandler = new ItemDBHandler();
			
			ItemTL wantedItem = ofy().load().type(ItemTL.class)
					.filter("owner", uid)
					.filter("exchanged",false)
					.filter("idRefML",this.requestedItem()).first().now();
			
			String offeredItemId = form.getFirstValue("offeredItemId");

			ItemTL offeredItem = ofy().load().type(ItemTL.class)
					.filter("owner", userfb.getId())
					.filter("exchanged",false)
					.filter("idRefML",offeredItemId).first().now();
			
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
		} else {
			try {
				ItemTL item = new ItemTL(itemId, uid);
				DBHandler.getInstance().save(item);
				message.addProperty("info", "El item se agrego correctamente");
			} catch (ItemNotExistsException ex) {
				message.addProperty("info", ex.getMessage());
			}
		}

		return new StringRepresentation(message.toString(),
				MediaType.APPLICATION_JSON);

	}

	private boolean requestingTrade(String uid, User userfb) {
		return !this.isTheUser(uid, userfb) && this.requestedItem() != null && !this.requestedItem().isEmpty();
	}

	private boolean creatingItemInOtherUser(String uid, User userfb) {
		return !this.isTheUser(uid, userfb) && (this.requestedItem() == null || this.requestedItem().isEmpty()) ;
	}

	private boolean isTheUser(String uid, User userfb) {
		return FacebookDataCollector.getInstance().isTheUser(userfb, uid);
	}

}