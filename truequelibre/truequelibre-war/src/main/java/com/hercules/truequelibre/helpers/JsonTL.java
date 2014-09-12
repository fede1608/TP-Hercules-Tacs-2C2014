package com.hercules.truequelibre.helpers;

import java.util.Iterator;
import java.util.List;
import java.util.Date;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hercules.truequelibre.domain.ItemTL;
import com.hercules.truequelibre.domain.TradeTL;

public class JsonTL {

	public static JsonObject jsonifyItem(ItemTL item){
		JsonObject json = new JsonObject();
		if(item==null) return json;//devuelve json vacio si el item pasado es null
		json.addProperty("itemId", item.id);
		json.addProperty("idRefML", item.idRefML);
		json.addProperty("name", item.name);
		json.addProperty("img", item.image);
		json.addProperty("owner", item.owner);
		json.addProperty("dateCreated", item.created);
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

	public static JsonObject jsonifyTrade(TradeTL trade) {
		
		JsonObject json = new JsonObject();
		JsonObject jsonWantedItem = JsonTL.jsonifyItem(trade.getWantedItem());
		JsonObject jsonOfferedItem = JsonTL.jsonifyItem(trade.getOfferedItem());
		json.addProperty("id", trade.id);
		json.add("wantedItem", jsonWantedItem);
		json.add("offeredItem", jsonOfferedItem);
		json.addProperty("state",trade.getState());
		json.addProperty("dateCreated", trade.dateCreated);
		return json;
	}
	
	public static JsonArray tradesToJsonArray(List<TradeTL> list){
		JsonArray jsonTrades = new JsonArray();
		
		for(TradeTL trade : list)
		{
			jsonTrades.add(JsonTL.jsonifyTrade(trade));
		}
		return jsonTrades;
	}
	@SuppressWarnings("deprecation")
	public static JsonObject jsonifyDate(Date date){
		JsonObject jsonDate = new JsonObject();
		jsonDate.addProperty("day", date.getDate());
		jsonDate.addProperty("month",date.getMonth());
		jsonDate.addProperty("year", date.getYear()+1900); //por alguna razon el date le resta 1900
		jsonDate.addProperty("hours", date.getHours());
		jsonDate.addProperty("minutes", date.getMinutes());
		jsonDate.addProperty("seconds", date.getSeconds());
		
		
		return jsonDate;
	}
	
}
