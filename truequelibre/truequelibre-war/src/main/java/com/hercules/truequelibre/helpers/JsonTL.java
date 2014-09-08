package com.hercules.truequelibre.helpers;

import java.util.Iterator;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hercules.truequelibre.domain.ItemTL;

public class JsonTL {

	public static JsonObject jsonifyItem(ItemTL item){
		JsonObject json = new JsonObject();
		if(item==null) return json;//devuelve json vacio si el item pasado es null
		json.addProperty("itemId", item.id);
		json.addProperty("idRefML", item.idRefML);
		json.addProperty("name", item.name);
		json.addProperty("img", item.image);
		json.addProperty("owner", item.owner);
		return json;
	}
	
	public static JsonObject jsonifyError(String error){
		JsonObject json = new JsonObject();
		json.addProperty("error", error);
		return json;
	}
	public static JsonArray jsonifyItemList(List<ItemTL> items){
		Iterator<ItemTL> iterator = items
				.iterator();
		JsonArray requestList=new JsonArray();
		while (iterator.hasNext()) {
			requestList.add(JsonTL
					.jsonifyItem(iterator.next()));
		}
		return requestList;
	}
	public static JsonObject jsonifyItemWithRequests(ItemTL item){
		JsonObject jsonItem=JsonTL.jsonifyItem(item);
		jsonItem.add("SentTradeRequests", JsonTL.jsonifyItemList(item.getWishlist())); //no se si se puede usar this en la misma clase
		jsonItem.add("RecievedTradeRequests", JsonTL.jsonifyItemList(item.getTradeRequests()));
		return jsonItem;
	}
	
}
