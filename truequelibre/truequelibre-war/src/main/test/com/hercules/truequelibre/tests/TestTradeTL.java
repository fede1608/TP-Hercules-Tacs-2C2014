package com.hercules.truequelibre.tests;

import static org.junit.Assert.*;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;

import javax.ws.rs.core.MultivaluedMap;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;
import com.hercules.truequelibre.domain.InexistentObjectException;
import com.hercules.truequelibre.domain.ItemNotExistsException;
import com.hercules.truequelibre.domain.ItemTL;
import com.hercules.truequelibre.domain.TradeTL;
import com.hercules.truequelibre.helpers.DBHandler;
import com.hercules.truequelibre.mlsdk.Meli;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.google.gson.*;
import com.hercules.truequelibre.domain.TradeTL;

import org.junit.*;

public class TestTradeTL {
	
	private static final int PENDING = 0;
	private static final int ACCEPTED = 1;
	private static final int DECLINED = 2;
	private static final int CANCELLED = 3;
	private static final String ID_PEDRO ="317280481786050";
	private static final String ID_JUAN = "281991468665901";
	private static final String ID_ITEMPEDRO ="MLA519532991";
	private static final String ID_ITEMJUAN = "MLA517671331";
	
	TradeTL trade; 
	private final LocalServiceTestHelper datastoreHelper =
			new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	
	@Before
	public void setUp() {
		datastoreHelper.setUp();
		ItemTL offeredItem=null;
		ItemTL wantedItem=null;

		try
		{
			offeredItem=new ItemTL(ID_ITEMJUAN,ID_JUAN);
			offeredItem.id=234l;
		}
		catch(ItemNotExistsException ex)
		{
			//Los id existen,  los puse a mano
		}
		try
		{
			wantedItem = new ItemTL(ID_ITEMPEDRO,ID_PEDRO);
			wantedItem.id=345l;
		}
		catch(ItemNotExistsException ex)
		{
			//Los id existen,  los puse a mano
		}
		trade = new TradeTL(offeredItem,wantedItem);		
	}

	static{
		ObjectifyService.register(ItemTL.class);
		ObjectifyService.register(TradeTL.class);
	}

	@Test
	public void SePuedeAceptarTrade() {
		trade.accept();
		assertEquals(trade.getState(),ACCEPTED);
	} 
	@Test
	public void SePuedeRechazarTrade() {
		trade.decline();
		assertEquals(trade.getState(),DECLINED);
	} 
	
	@Test
	public void SePuedeCancelarTrade() {
		trade.cancel();
		assertEquals(trade.getState(),CANCELLED);
	}
		
	
}