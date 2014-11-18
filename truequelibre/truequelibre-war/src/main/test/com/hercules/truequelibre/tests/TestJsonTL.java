package com.hercules.truequelibre.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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
			item = new ItemTL("MLA524113275","1000");
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
	
	private void assertItem(JsonObject json, ItemTL item){
		assertEquals(json.get("itemId").getAsLong(), item.id.longValue());
		assertEquals(json.get("name").getAsString(),item.name);
		assertEquals(json.get("img").getAsString(),item.image);
		assertEquals(json.get("idRefML").getAsString(),item.idRefML);
		assertEquals(json.get("owner").getAsString(),item.owner);
		assertEquals(json.get("dateCreated").getAsLong(),item.created);
	}
	
	@Test
	public void obtenerJsonItem() {
		ItemTL item = obtenerItem();
		JsonObject json = JsonTL.jsonifyItem(item);
		assertItem(json, item);
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
	public void obtenerJsonTrade() {
		ItemTL item = obtenerItem();
		TradeTL trade = obtenerTrade(item);
		JsonObject jsonTrade = JsonTL.jsonifyTrade(trade);
		assertEquals(jsonTrade.get("state").getAsInt(), PENDING);
		assertEquals(jsonTrade.get("date").getAsInt(), trade.date);
		assertItem(jsonTrade.get("wantedItem").getAsJsonObject(), item);
		assertItem(jsonTrade.get("offeredItem").getAsJsonObject(), item);
	} 

}
