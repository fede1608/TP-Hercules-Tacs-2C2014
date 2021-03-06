package com.hercules.truequelibre.resources;

import static com.googlecode.objectify.ObjectifyService.ofy;
import java.util.List;
import java.util.Map;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hercules.truequelibre.domain.ItemTL;
import com.hercules.truequelibre.helpers.FacebookDataCollector;
import com.hercules.truequelibre.helpers.JsonTL;

public class ItemNewsFeedResource extends ParameterGathererTemplateResource {

	public ItemNewsFeedResource() {
		super();
	}

	public ItemNewsFeedResource(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.APPLICATION_JSON));
	}

	@Override
	protected Representation get() throws ResourceException {
		String token = getCookies().getValues("accessToken");
		JsonObject json = JsonTL.getResponse();//new JsonObject();
		try{
		Map<String,String> friends = FacebookDataCollector.getInstance().getFriendsAsHashMap(token);
		User user=FacebookDataCollector.getInstance().findUserWithRest(token);
		friends.put(user.getId(), user.getName());
		List<ItemTL> items = ofy().load().type(ItemTL.class)
					.filter("owner in", friends.keySet())
					.filter("exchanged", false)
					.order("-created").limit(10)
					.list();
		
		JsonArray jsonedItemlist=JsonTL.jsonifyItemList(items,friends);
		json.add("items", jsonedItemlist);
		json.addProperty("itemCount", jsonedItemlist.size());
		
		}catch (FacebookOAuthException e) {

			json = JsonTL
					.jsonifyError("el token esta desactualizado, por favor actualicelo",JsonTL.UNAUTHORIZED);
		}
		return new StringRepresentation(json.toString(), MediaType.APPLICATION_JSON);
	}



}