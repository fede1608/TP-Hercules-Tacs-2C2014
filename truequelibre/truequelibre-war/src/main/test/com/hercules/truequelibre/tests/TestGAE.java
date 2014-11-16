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

import org.junit.*;

public class TestGAE {

	private final LocalServiceTestHelper datastoreHelper =
			new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	Meli m = new Meli();
	String idMLItem = " ";
	
	static{
		ObjectifyService.register(ItemTL.class);
		ObjectifyService.register(TradeTL.class);
	}

	@Before
	public void setUp() {
		datastoreHelper.setUp();
	}

	@After
	public void tearDown() {
		datastoreHelper.tearDown();
	}

	// se corre dos veces el test para probar que no se esta leakeando ningun estado a traves de los tests
	/**
	 * Test de datastore (almacenamiento en memoria)
	 */
	private void testDatastore() {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		assertEquals(0, ds.prepare(new Query("prueba")).countEntities(withLimit(10)));
		ds.put(new Entity("prueba"));
		ds.put(new Entity("prueba"));
		assertEquals(2, ds.prepare(new Query("prueba")).countEntities(withLimit(10)));
	}

	@Test
	public void testInsertDataStore1() {
		testDatastore();
	}

	@Test
	public void testInsertDataStore2() {
		testDatastore();
	}
	
	public long insertarItem() {
		ItemTL item;
		long id = 0;
		
		try {
			item = new ItemTL(idMLItem, "usuarioPrueba");
			id = DBHandler.getInstance().save(item);
			assertTrue(true);
		} catch (ItemNotExistsException e) {
			assertFalse(true);
		}
		return id;
	}
	
	public ItemTL recuperarItem(long id){
		ItemTL item = null;
		try{
			item = DBHandler.getInstance().get(ItemTL.class,id);
			assertTrue(true);
		}catch(InexistentObjectException e){
			assertFalse(true);
		}
		return item;
	}
	
	private void getIdMLItem() {
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("limit","1");
		params.add("offset", "0");
		params.add("q", "ipod");
		JsonObject j = null;
		try {
			j = m.get("sites/MLA/search",params);
		} catch (Exception e) {
			assertFalse(true);
		}
		JsonArray results = j.getAsJsonArray("results");
		JsonObject item = results.get(0).getAsJsonObject();
		idMLItem = item.get("id").getAsString();
	}
	
	@Test
	public void testGuardarRecuperarItemTL(){
		getIdMLItem();
		ItemTL item = recuperarItem(insertarItem());
		assertEquals(idMLItem ,item.idRefML);
	}
	
	@Test
	public void testGuardarRecuperarItemTL2(){
		getIdMLItem();
		ItemTL item = recuperarItem(insertarItem());
		assertEquals(idMLItem ,item.idRefML);
	}
	
}
