package com.hercules.truequelibre.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hercules.truequelibre.domain.ItemNotExistsException;
import com.hercules.truequelibre.domain.ItemTL;
import com.hercules.truequelibre.domain.TradeTL;
import com.hercules.truequelibre.helpers.JsonTL;
import com.hercules.truequelibre.mlsdk.Meli;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class TestJsonTL {
	
	private static final int PENDING = 0;
	public static final int OK = 200;
	public static final int NOT_FOUND = 404;
	String idFriend = "1000";
	Meli m = new Meli();
		
	private JsonArray getIdMLItem() {
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("limit","10");
		params.add("offset", "5");
		params.add("q", "ipod");
		JsonObject j = null;
		try {
			j = m.get("sites/MLA/search",params);
		} catch (Exception e) {
			fail();
		}
		return j.getAsJsonArray("results");
	}
	
	private ItemTL obtenerItem(){
		ItemTL item = null;
		try{
			item = new ItemTL("MLA524113275",idFriend);
			item.id = (long) 111;
		}
		catch(ItemNotExistsException e){
			fail();
		}
		
		return item;
	}
	
	private TradeTL obtenerTrade(ItemTL item){
		return new TradeTL(item,item);
	}
	
	private Map<String,String> obtenerAmigos(){
		Map<String, String> friends = new HashMap<String,String>();
		friends.put(idFriend, "pepe");
		return friends;
	}
	
	private List<TradeTL> obtenerTrades(ItemTL item){
		List<TradeTL> trades = new ArrayList<TradeTL>();
		TradeTL trade1 = obtenerTrade(item);
		TradeTL trade2 = obtenerTrade(item);
		trades.add(trade1);
		trades.add(trade2);
		return trades;
	}
	
	private void assertItem(JsonObject json, ItemTL item){
		assertEquals(json.get("itemId").getAsLong(), item.id.longValue());
		assertEquals(json.get("name").getAsString(),item.name);
		assertEquals(json.get("img").getAsString(),item.image);
		assertEquals(json.get("idRefML").getAsString(),item.idRefML);
		assertEquals(json.get("owner").getAsString(),item.owner);
		assertEquals(json.get("dateCreated").getAsLong(),item.created);
	}
	
	private void assertItemConFriends(JsonObject json, ItemTL item){
		assertItem(json,item);
		Map<String,String> friends = obtenerAmigos();
		assertEquals(json.get("ownerName").getAsString(), friends.get(idFriend));
	}
	
	private void assertTrade(JsonObject jsonTrade, TradeTL trade, ItemTL item){
		assertEquals(jsonTrade.get("state").getAsInt(), PENDING);
		assertEquals(jsonTrade.get("date").getAsInt(), trade.date);
		assertItem(jsonTrade.get("wantedItem").getAsJsonObject(), item);
		assertItem(jsonTrade.get("offeredItem").getAsJsonObject(), item);
	}
	
	private void assertTradeConFriends(JsonObject jsonTrade, TradeTL trade, ItemTL item){
		assertEquals(jsonTrade.get("state").getAsInt(), PENDING);
		assertEquals(jsonTrade.get("dateCreated").getAsInt(), trade.date);
		assertItemConFriends(jsonTrade.get("wantedItem").getAsJsonObject(), item);
		assertItemConFriends(jsonTrade.get("offeredItem").getAsJsonObject(), item);
	}
	
	@Test
	public void obtenerJsonItem() {
		ItemTL item = obtenerItem();
		JsonObject json = JsonTL.jsonifyItem(item);
		assertItem(json, item);
	} 
	
	@Test
	public void obtenerJsonItemList() {
		List<ItemTL> items = new ArrayList<ItemTL>();
		ItemTL item1 = obtenerItem();
		ItemTL item2 = obtenerItem();
		items.add(item1);
		items.add(item2);
		JsonArray jsonArray = JsonTL.jsonifyItemList(items);
		assertItem(jsonArray.get(0).getAsJsonObject(), item1);
		assertItem(jsonArray.get(1).getAsJsonObject(), item2);
	} 
	
	@Test
	public void obtenerJsonItemListConFriends() {
		List<ItemTL> items = new ArrayList<ItemTL>();
		ItemTL item1 = obtenerItem();
		ItemTL item2 = obtenerItem();
		Map<String,String> friends = obtenerAmigos();
		items.add(item1);
		items.add(item2);
		JsonArray jsonArray = JsonTL.jsonifyItemList(items, friends);
		assertItemConFriends(jsonArray.get(0).getAsJsonObject(), item1);
		assertItemConFriends(jsonArray.get(1).getAsJsonObject(), item2);
	} 
	
	@Test
	public void obtenerJsonDeSearch() {
		JsonObject json = getIdMLItem().get(0).getAsJsonObject();
		JsonObject jsonSearch = JsonTL.jsonifySearchItem(json);
		assertEquals(jsonSearch.get("id").getAsString(), json.get("id").getAsString());
		assertEquals(jsonSearch.get("name").getAsString(),json.get("title").getAsString());
		assertEquals(jsonSearch.get("img").getAsString(),Meli.getImageLarge(json.get("thumbnail").getAsString()));
	} 
	
	@Test
	public void obtenerJsonDeError() {
		JsonObject jsonError = JsonTL.jsonifyError("Test error");
		assertEquals(jsonError.get("status").getAsInt(),NOT_FOUND);
		assertEquals(jsonError.get("error").getAsString(),"Test error");
	} 
	
	@Test
	public void obtenerJsonStatusOK() {
		JsonObject jsonStatus = JsonTL.getResponse();
		assertEquals(jsonStatus.get("status").getAsInt(),OK);
	} 
	
	@Test
	public void obtenerJsonInfo() {
		JsonObject jsonInfo = JsonTL.jsonifyInfo("Test info");
		assertEquals(jsonInfo.get("status").getAsInt(),OK);
		assertEquals(jsonInfo.get("info").getAsString(),"Test info");
	} 
	
	@SuppressWarnings("deprecation")
	@Test
	public void obtenerJsonDate() {
		Date fecha = new Date();
		JsonObject jsonDate = JsonTL.jsonifyDate(fecha);
		assertEquals(jsonDate.get("day").getAsInt(),fecha.getDate());
		assertEquals(jsonDate.get("month").getAsInt(),fecha.getMonth());
		assertEquals(jsonDate.get("year").getAsInt(),fecha.getYear()+1900);
		assertEquals(jsonDate.get("hours").getAsInt(),fecha.getHours());
		assertEquals(jsonDate.get("minutes").getAsInt(),fecha.getMinutes());
		assertEquals(jsonDate.get("seconds").getAsInt(),fecha.getSeconds());
	} 
	
	@Test
	public void obtenerJsonTrade() {
		ItemTL item = obtenerItem();
		TradeTL trade = obtenerTrade(item);
		JsonObject jsonTrade = JsonTL.jsonifyTrade(trade);
		assertTrade(jsonTrade, trade, item);
	} 
	
	@Test
	public void obtenerJsonTradeConFriends() {
		ItemTL item = obtenerItem();
		TradeTL trade = obtenerTrade(item);
		Map<String,String> friends = obtenerAmigos();
		JsonObject jsonTrade = JsonTL.jsonifyTrade(trade, friends).getAsJsonObject();
		assertTradeConFriends(jsonTrade, trade, item);
	} 
	
	@Test
	public void obtenerJsonTradeList() {
		ItemTL item = obtenerItem();
		List<TradeTL> trades = obtenerTrades(item);
		JsonArray jsonTrade = JsonTL.tradesToJsonArray(trades);
		assertTrade(jsonTrade.get(0).getAsJsonObject(), trades.get(0), item);
		assertTrade(jsonTrade.get(1).getAsJsonObject(), trades.get(1), item);
	}
	
	@Test
	public void obtenerJsonTradeListConFriends() {
		ItemTL item = obtenerItem();
		List<TradeTL> trades = obtenerTrades(item);
		Map<String,String> friends = obtenerAmigos();
		JsonArray jsonTrade = JsonTL.tradesToJsonArray(trades, friends).getAsJsonArray();
		assertTradeConFriends(jsonTrade.get(0).getAsJsonObject(), trades.get(0), item);
		assertTradeConFriends(jsonTrade.get(1).getAsJsonObject(), trades.get(1), item);
	} 
}
