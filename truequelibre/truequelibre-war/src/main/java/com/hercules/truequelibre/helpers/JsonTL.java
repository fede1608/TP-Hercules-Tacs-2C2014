package com.hercules.truequelibre.helpers;

import java.util.Iterator;
import java.util.List;
import java.util.Date;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

	public static String getImageLarge(String url){ //TODO encontrar un mejor lugar para alojar este metodo
		// el search solo me da el thumbnail, para obtener la imagen con
		// resolucion alta hay que cambiar el
		// formato de imagen thumbnail:
		// http://mla-s2-p.mlstatic.com/18799-MLA20160634057_092014-I.jpg,
		//  a formato de imagen alta resolucion:
		// http://mla-s2-p.mlstatic.com/18799-MLA20160634057_092014-O.jpg
		if(url.length()>5)
			return url.substring(0,url.length() - 5).concat("O.jpg");//cambia la I por la O
		else
			return "http://static.mlstatic.com/org-img/original/MLA/artsinfoto.gif";// en caso que no tenga una imagen 
	}
	
	public static JsonObject jsonifySearchItem(JsonObject item){	
		JsonObject searchItem = new JsonObject();
		searchItem.add("id", item.get("id"));
		searchItem.addProperty("img",getImageLarge(item.get("thumbnail").getAsString()));
		searchItem.add("name", item.get("title"));
		return searchItem;
	}
	public static JsonObject jsonifyError(String error){
		return jsonifyError(error,404);
	}
	public static JsonObject jsonifyError(String error, int status){
		JsonObject json = new JsonObject();
		json.addProperty("status", status);
		json.addProperty("error", error);
		return json;
	}
	public static JsonObject getResponse(){
		JsonObject response = new JsonObject();
		response.addProperty("status", 200);
		return response;
	}
	public static JsonObject jsonifyInfo(String info){
		JsonObject json = new JsonObject();
		json.addProperty("status", 200);
		json.addProperty("info", info);
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
		json.addProperty("date", trade.date);
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

	public static JsonArray jsonifyItemList(List<ItemTL> items,
			Map<String, String> friends) {
		Iterator<ItemTL> iterator = items
				.iterator();
		JsonArray requestList=new JsonArray();
		while (iterator.hasNext()) {
			requestList.add(JsonTL.jsonifyItem(iterator.next(),friends));
		}
		return requestList;
	}

	private static JsonObject jsonifyItem(ItemTL i, Map<String, String> friends) {
		JsonObject jsonItem= JsonTL.jsonifyItem(i);
		jsonItem.addProperty("ownerName", friends.get(i.owner));
		return jsonItem;
	}

	public static JsonElement tradesToJsonArray(
			List<TradeTL> trades, Map<String, String> friends) {
		JsonArray jsonTrades = new JsonArray();

		for(TradeTL trade : trades)
		{
			jsonTrades.add(JsonTL.jsonifyTrade(trade,friends));
		}
		return jsonTrades;
	}

	public static JsonElement jsonifyTrade(TradeTL trade,
			Map<String, String> friends) {
		JsonObject json = new JsonObject();
		JsonObject jsonWantedItem = JsonTL.jsonifyItem(trade.getWantedItem(),friends);
		JsonObject jsonOfferedItem = JsonTL.jsonifyItem(trade.getOfferedItem(),friends);
		json.addProperty("id", trade.id);
		json.add("wantedItem", jsonWantedItem);
		json.add("offeredItem", jsonOfferedItem);
		json.addProperty("state",trade.getState());
		json.addProperty("dateCreated", trade.date);
		return json;
	}

}
