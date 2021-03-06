package com.hercules.truequelibre.resources;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hercules.truequelibre.helpers.JsonTL;
import com.hercules.truequelibre.mlsdk.Meli;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import javax.ws.rs.core.MultivaluedMap;

public class SearchResource extends ServerResource {

	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_LIMIT = 10;

	public SearchResource() {
		super();
	}

	public SearchResource(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.APPLICATION_JSON));
	}	

	@Override
	protected Representation get() throws ResourceException {
		
		
		MultivaluedMap<String, String> params = this.parseParameters();
		JsonObject json = this.getSearchAsJson(params);
		return new StringRepresentation(json.toString(),
				MediaType.APPLICATION_JSON);
	}

	private JsonObject getSearchAsJson(MultivaluedMap<String, String> params) 
	{
		Meli m = new Meli();
		JsonObject json =  JsonTL.getResponse();
		try {
			JsonArray search = new JsonArray();
			JsonObject response = m.get("sites/MLA/search", params);
			JsonArray results = response.getAsJsonArray("results");
			json.addProperty("total", response.get("paging").getAsJsonObject().get("total").getAsLong());
			for (int i = 0; i < results.size(); i++) {
				JsonObject item = results.get(i).getAsJsonObject();
				search.add(JsonTL.jsonifySearchItem(item));
			}
			json.add("search", search);

		} catch (Exception e) {
			json = JsonTL.jsonifyError(e.getMessage());
		}
		return json;
	}

	private MultivaluedMap<String, String> parseParameters() {
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		int limit=(getQuery().getValues("limit") == null ? DEFAULT_LIMIT
				: Integer.parseInt(getQuery().getValues("limit")));
		int page=(getQuery().getValues("page") == null ? DEFAULT_PAGE
				: Integer.parseInt(getQuery().getValues("page")));
		params.add("limit", String.valueOf(limit));// si me dan un limite diferente lo pongo, sino default 10
		params.add("offset", String.valueOf(limit * page));// si me dan un offset diferente lo pongo, sino default 0
		params.add("q", getQuery().getValues("query"));
		return params;
	}

}