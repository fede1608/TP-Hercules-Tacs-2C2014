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

	public SearchResource() {
		super();
	}

	public SearchResource(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.APPLICATION_JSON));
	}

	public String stackTraceToString(Throwable e) {
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement element : e.getStackTrace()) {
			sb.append(element.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public String getImageLarge(String url){
		return url.length()>5?url.substring(0,url.length() - 5).concat("O.jpg"):"http://static.mlstatic.com/org-img/original/MLA/artsinfoto.gif";
	}

	@Override
	protected Representation get() throws ResourceException {
		Meli m = new Meli();
		JsonObject json = new JsonObject();
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		int limit=(getQuery().getValues("limit") == null ? 10
				: Integer.parseInt(getQuery().getValues("limit")));
		int page=(getQuery().getValues("page") == null ? 0
				: Integer.parseInt(getQuery().getValues("page")));
		params.add("limit", String.valueOf(limit));// si me dan un limite diferente lo pongo, sino
													// default 10
		params.add("offset", String.valueOf(limit * page));// si me dan un offset diferente lo pongo, sino
													// default 10
		params.add("q", getQuery().getValues("query"));
		JsonArray search = new JsonArray();
		try {
			JsonObject response = m.get("sites/MLA/search", params);
			JsonArray results = response.getAsJsonArray("results");
			json.addProperty("total", response.get("paging").getAsJsonObject().get("total").getAsLong());
			for (int i = 0; i < results.size(); i++) {
				JsonObject item = results.get(i).getAsJsonObject();
				JsonObject searchItem = new JsonObject();
				searchItem.add("id", item.get("id"));
				// el search solo me da el thumbnail, yo quiero la imagen con
				// resolucion
				// alta, fuck ML, formato de iamgen thumbnail .....sad-I.jpg,
				// formato de
				// imagen alta resolucion .....sad-O.jpg
				searchItem.addProperty("img",getImageLarge(item.get("thumbnail").getAsString()));
				searchItem.add("name", item.get("title"));
				
				search.add(searchItem);
			}
			json.add("search", search);

		} catch (Exception e) {
			json = JsonTL.jsonifyError(e.getMessage());
		}
		return new StringRepresentation(json.toString(),
				MediaType.APPLICATION_JSON);
	}

}