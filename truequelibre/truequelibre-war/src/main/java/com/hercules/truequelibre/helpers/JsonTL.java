package com.hercules.truequelibre.helpers;

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
	
}
