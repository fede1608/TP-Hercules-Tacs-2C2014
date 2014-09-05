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
import com.hercules.truequelibre.domain.ItemTL;
import com.hercules.truequelibre.helpers.DBHandler;
import com.hercules.truequelibre.helpers.FacebookDataCollector;
import com.hercules.truequelibre.mlsdk.Meli;

public class ItemsResource extends ParameterGathererTemplateResource {

	public ItemsResource() {
		super();
	}

	public ItemsResource(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
	}

	@Override
	protected Representation get() throws ResourceException {
		String message = null;
		String token = getCookies().getValues("accessToken");
		if (this.requestedItem() == null) {// request a api/users/{userid}/items
			if (FacebookDataCollector.getInstance().isTheUser(token,
					this.requestedUser())) {
				List<ItemTL> items = ofy().load().type(ItemTL.class)
						.filter("owner", this.requestedUser())
						.filter("intercambiado", false).list();
				JsonObject json = new JsonObject();
				JsonArray itemsJson = new JsonArray();
				Iterator<ItemTL> iterator = items.iterator();
				while (iterator.hasNext()) {
					JsonObject item = new JsonObject();
					ItemTL i = iterator.next();
					item.addProperty("id", i.id);
					item.addProperty("name", i.nombre);
					item.addProperty("img", i.imagen);
					item.addProperty("owner", i.owner);
					itemsJson.add(item);
				}
				json.add("items", itemsJson);
				message = json.toString();
			} else {
				// crear json con error usuario no corresponde con el token
			}
		} else {
			List<ItemTL> items = ofy().load().type(ItemTL.class)
					.filter("owner", this.requestedUser())
					.filter("id", this.requestedItem()).list();

			if (!items.isEmpty()) {
				ItemTL item= items.get(0);
				JsonObject json = new JsonObject();
				json.addProperty("itemId", item.id);
				json.addProperty("name", item.nombre);
				json.addProperty("img", item.imagen);
				json.addProperty("owner", item.owner);
				

				message = "Trueque Libre!" + "\n la pagina que ingreso es: "
						+ this.getReference() + "\n con el recurso: "
						+ this.getReference().getBaseRef()
						+ "\n con el numero de usuario: "
						+ this.requestedUser() + "\n con el item pedido: "
						+ this.requestedItem() + "\n";

				try {
					if (FacebookDataCollector.getInstance()
							.informationCanBeShown(token, this.requestedUser())) {
						message += "te puedo mostrar la info del item que es: "
								+ this.requestedItem();
						if (this.itemExists(this.requestedItem())) {
							message += "\n el item pedido existe entre los suyos! \n";
							message += this.itemInfo(this.requestedItem());
						} else {
							message += "\n no tiene el item entre sus items";
						}
					} else {
						message += "la persona no es amigo suyo";
					}
				} catch (FacebookOAuthException e) {
					message = "el token esta desactualizado, por favor actualicelo";
				}
			}
		}else{
			//show error item no existe o pertenece a otro usuario
		}

		return new StringRepresentation(message, MediaType.TEXT_PLAIN);
	}

	public String itemInfo(String requestedItem) {
		JsonObject searchItem = new JsonObject();
		try {
			JsonObject item = new Meli().get("items/" + this.requestedItem());
			searchItem.add("id", item.get("id"));
			searchItem.add("img", item.get("thumbnail"));
			searchItem.add("name", item.get("title"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return searchItem.toString();
	}

	private boolean itemExists(String itemId) {
		return ofy().load().filterKey(Key.create(ItemTL.class, itemId)).count() == 0;
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
		if (!FacebookDataCollector.getInstance().isTheUser(userfb, uid)) {// autenticar
			message.addProperty("error",
					"El usuario no corresponde con el token.");
		} else {
			if (itemExists(itemId)) {
				ItemTL item = new ItemTL(itemId, uid);
				DBHandler.getInstance().save(item);
				message.addProperty("info", "El item se agrego correctamente");
			} else
				message.addProperty("error",
						"El item que quiere ingresar ya esta registrado.");
		}

		return new StringRepresentation(message.toString(),
				MediaType.TEXT_PLAIN);

	}

}