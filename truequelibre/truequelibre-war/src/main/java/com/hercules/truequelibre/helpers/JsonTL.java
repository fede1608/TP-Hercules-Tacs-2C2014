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
import com.hercules.truequelibre.mlsdk.Meli;

/**
 * Clase para crear y manipular los JSON utilizados en el dominio de la aplicacion
 */
public class JsonTL {

	public static final int OK = 200;
	public static final int NOT_FOUND = 404;
	public static final int UNAUTHORIZED = 401;

	/**
	 * Convierte un item a formato JSON
	 * @param item : Item a convertir
	 * @return JSON del item, JSON vacio si se pasa un item en null
	 */
	public static JsonObject jsonifyItem(ItemTL item){
		JsonObject json = new JsonObject();
		if(item==null) return json;
		json.addProperty("itemId", item.id);
		json.addProperty("idRefML", item.idRefML);
		json.addProperty("name", item.name);
		json.addProperty("img", item.image);
		json.addProperty("owner", item.owner);
		json.addProperty("dateCreated", item.created);
		return json;
	}
	
	/**
	 * Convierte a JSON los items resultado de la busqueda
	 * @param item : Item a convertir
	 * @return Json del item con id, imagen en formato largo y nombre
	 */
	public static JsonObject jsonifySearchItem(JsonObject item){	
		JsonObject searchItem = new JsonObject();
		searchItem.add("id", item.get("id"));
		searchItem.addProperty("img", Meli.getImageLarge(item.get("thumbnail").getAsString()));
		searchItem.add("name", item.get("title"));
		return searchItem;
	}
	
	/**
	 * Devuelve un error en formato JSON para el status 'NOT FOUND' (404)
	 * @param error : mensaje de error
	 * @return Devuelve un jsonifyError(mensaje de error, status 404)
	 */
	public static JsonObject jsonifyError(String error){
		return jsonifyError(error,NOT_FOUND);
	}
	
	/**
	 * Devuelve un error en formato JSON con un mensaje representativo y el codigo de status
	 * @param error : mensaje de error
	 * @param status : codigo de status
	 * @return JSON de error con el status y el mensaje de error
	 */
	public static JsonObject jsonifyError(String error, int status){
		JsonObject json = new JsonObject();
		json.addProperty("status", status);
		json.addProperty("error", error);
		return json;
	}
	
	/**
	 * Devuelve un JSON con status 'OK' (200)
	 * @return JSON con status OK
	 */
	public static JsonObject getResponse(){
		JsonObject response = new JsonObject();
		response.addProperty("status", OK);
		return response;
	}
	
	/**
	 * Devuelve un JSON con status 'OK' (200) y con un mensaje de informacion
	 * @param info
	 * @return JSON con status OK y mensaje de info
	 */
	public static JsonObject jsonifyInfo(String info){
		JsonObject json = getResponse();
		json.addProperty("info", info);
		return json;
	}
	
	/**
	 * Devuelve un JsonArray a partir de una lista de items
	 * @param items : Lista de items
	 * @return JsonArray con cada item convertido a JSON
	 */
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

	/**
	 * Devuelve un JSON de una solicitud de intercambio
	 * @param trade : solicitud de intercambio
	 * @return JSON de la solicitud
	 */
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

	/**
	 * Devuelve un JsonArray a partir de una lista de solicitudes de intercambio
	 * @param list : lista de solicitudes de intercambio
	 * @return JsonArray de cada solicitud de intercambio convertido a JSON
	 */
	public static JsonArray tradesToJsonArray(List<TradeTL> list){
		JsonArray jsonTrades = new JsonArray();

		for(TradeTL trade : list)
		{
			jsonTrades.add(JsonTL.jsonifyTrade(trade));
		}
		return jsonTrades;
	}
	/**
	 * Devuelve un JSON con informacion de una fecha
	 * @param date : Fecha a convertir a JSON
	 * @return JSON de la fecha
	 */
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

	/**
	 * Devuelve un JsonArray con informacion de una lista de items y sus respectivos duenios
	 * @param items : lista de items
	 * @param friends : mapa de contactos amigos con formato String,String 
	 * @return JsonArray con los items con duenio
	 */
	public static JsonArray jsonifyItemList(List<ItemTL> items, Map<String, String> friends) {
		Iterator<ItemTL> iterator = items.iterator();
		JsonArray requestList = new JsonArray();
		while (iterator.hasNext()) {
			requestList.add(JsonTL.jsonifyItem(iterator.next(),friends));
		}
		return requestList;
	}

	/**
	 * Devuelve un JSON del item con su respectivo dueño
	 * @param i : item a convertir
	 * @param friends : mapa de contactos con formato String, String
	 * @return JSON del item con duenio
	 */
	private static JsonObject jsonifyItem(ItemTL i, Map<String, String> friends) {
		JsonObject jsonItem= JsonTL.jsonifyItem(i);
		jsonItem.addProperty("ownerName", friends.get(i.owner));
		return jsonItem;
	}

	/**
	 * 
	 * @param trades
	 * @param friends
	 * @return
	 */
	public static JsonElement tradesToJsonArray(
			List<TradeTL> trades, Map<String, String> friends) {
		JsonArray jsonTrades = new JsonArray();

		for(TradeTL trade : trades)
		{
			jsonTrades.add(JsonTL.jsonifyTrade(trade,friends));
		}
		return jsonTrades;
	}

	/**
	 * Devuelve un JSON con informacion de la solicitud de intercambio
	 * @param trade : solicitud de intercambio
	 * @param friends : mapa de contactos amigos con formato String, String
	 * @return JSON con informacion de la solicitud de intercambio
	 */
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
