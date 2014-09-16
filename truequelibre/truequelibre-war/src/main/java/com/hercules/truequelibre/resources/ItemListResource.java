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

public class ItemListResource extends ParameterGathererTemplateResource {

	public ItemListResource() {
		super();
	}

	public ItemListResource(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.APPLICATION_JSON));
	}

	@Override
	protected Representation get() throws ResourceException {
		String token = getCookies().getValues("accessToken");
		JsonObject json = new JsonObject();
		try {
			if (FacebookDataCollector.getInstance().informationCanBeShown(
					token, this.requestedUser())) {
				List<ItemTL> items = ofy().load().type(ItemTL.class)
						.filter("owner", this.requestedUser())
						.filter("exchanged", false).list();
				json = new JsonObject();

				json.add("items", JsonTL.jsonifyItemList(items));
			} else {
				json = JsonTL
						.jsonifyError("Usuario no corresponde con el token o no es su amigo en facebook");
			}
		} catch (FacebookOAuthException e) {

			json = JsonTL
					.jsonifyError("el token esta desactualizado, por favor actualicelo");
		}
		return new StringRepresentation(json.toString(),
				MediaType.APPLICATION_JSON);
	}

	@Override
	public Representation post(Representation entity) {
		// Obtener los datos enviados por post
		Form form = new Form(entity);
		String uid = this.requestedUser();
		String itemId = form.getFirstValue("itemId");
		String tokenfb = getCookies().getValues("accessToken");// form.getFirstValue("token");
		JsonObject message = new JsonObject();
		try {
			User userfb = FacebookDataCollector.getInstance().findUserWithRest(
					tokenfb);

			if (creatingItemInOtherUser(uid, userfb)) {// autenticar
				message=JsonTL.jsonifyError(
						"Un usuario no puede crear un item para otro usuario.");

			} else {
				try {
					if (ofy().load().type(ItemTL.class)
							.filter("idRefML", itemId).count() > 0) {
						message=JsonTL.jsonifyError("Ya ha agregado ese item.");
					} else {
						ItemTL item = new ItemTL(itemId, uid);
						long id = DBHandler.getInstance().save(item);
						message=JsonTL.jsonifyInfo("El item se agrego correctamente");
						message.addProperty("itemId", id);
					}
				} catch (ItemNotExistsException ex) {
					message=JsonTL.jsonifyError( ex.getMessage());
				}
			}
		} catch (FacebookOAuthException e) {

			message = JsonTL
					.jsonifyError("el token esta desactualizado, por favor actualicelo");
		}catch (Exception ex){
			message= JsonTL.jsonifyError(ex.getMessage());
		}

		return new StringRepresentation(message.toString(),
				MediaType.APPLICATION_JSON);

	}

	private boolean creatingItemInOtherUser(String uid, User userfb) {
		return !this.isTheUser(uid, userfb);
	}

	private boolean isTheUser(String uid, User userfb) {
		return FacebookDataCollector.getInstance().isTheUser(userfb, uid);
	}

}