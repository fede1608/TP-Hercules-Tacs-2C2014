package com.hercules.truequelibre.tests;

import static org.junit.Assert.*;
import javax.ws.rs.core.MultivaluedMap;
import org.junit.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hercules.truequelibre.domain.ItemNotExistsException;
import com.hercules.truequelibre.domain.ItemTL;
import com.hercules.truequelibre.helpers.JsonTL;
import com.hercules.truequelibre.mlsdk.Meli;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class TestJsonTL {
	public static final int OK = 200;
	public static final int NOT_FOUND = 404;
	public static final int UNAUTHORIZED = 401;
	ItemTL item;
	Meli m = new Meli();
		
	
	public JsonObject pasarItemAJson(){
		try{
			item = new ItemTL("MLA524113275","1000");
			item.id = (long) 111;
		}
		catch(ItemNotExistsException e){
			fail();
		}
		return JsonTL.jsonifyItem(item);
	}
	
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
	
	@Test
	public void obtenerJsonItem() {
		JsonObject json = pasarItemAJson();
		assertEquals(json.get("itemId").getAsLong(), item.id.longValue());
		assertEquals(json.get("name").getAsString(),item.name);
		assertEquals(json.get("img").getAsString(),item.image);
		assertEquals(json.get("idRefML").getAsString(),item.idRefML);
		assertEquals(json.get("owner").getAsString(),item.owner);
		assertEquals(json.get("dateCreated").getAsLong(),item.created);
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

}
