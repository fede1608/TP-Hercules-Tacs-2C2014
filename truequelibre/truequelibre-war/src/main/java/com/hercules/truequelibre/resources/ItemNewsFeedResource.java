package com.hercules.truequelibre.resources;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
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

import com.restfb.Connection;
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
		JsonObject json = new JsonObject();
		String uid=FacebookDataCollector.getInstance().findUserWithRest(token).getId();
		List<User> friends=FacebookDataCollector.getInstance().getFriends(token).getData();
		List<String> friendsIds=this.getIdsFromUserList(friends);
		friendsIds.add(uid);
		List<ItemTL> items = ofy().load().type(ItemTL.class)
					.filter("owner in", friendsIds)
					.filter("exchanged", false)
					.order("-created").limit(10)
					.list();
			json = new JsonObject();
			JsonArray jsonedItemlist=JsonTL.jsonifyItemList(items);
			json.add("items", jsonedItemlist);
			json.addProperty("itemCount", jsonedItemlist.size());
		

		return new StringRepresentation(json.toString(), MediaType.APPLICATION_JSON);
	}

	private List<String> getIdsFromUserList(List<User> friends) {
		List<String> ids= new ArrayList<String>();
		
		for(User friend : friends){
			ids.add(friend.getId());
		}
		
		return ids;
	}

}