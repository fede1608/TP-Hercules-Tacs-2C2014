package com.hercules.truequelibre.tests;

import static org.junit.Assert.*;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.googlecode.objectify.ObjectifyService;
import com.hercules.truequelibre.domain.InexistentObjectException;
import com.hercules.truequelibre.domain.ItemNotExistsException;
import com.hercules.truequelibre.domain.ItemTL;
import com.hercules.truequelibre.domain.TradeTL;
import com.hercules.truequelibre.helpers.DBHandler;

import org.junit.*;

public class TestGAE {

	private final LocalServiceTestHelper datastoreHelper =
			new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	
	private final LocalServiceTestHelper memcacheHelper =
		    new LocalServiceTestHelper(new LocalMemcacheServiceTestConfig());
	
	private String idMLItem = "MLA528604866";	//id de una video camara samsung TODO reemplazar por una busqueda y obtener el ID del primer resultado
	
	static{
		ObjectifyService.register(ItemTL.class);
		ObjectifyService.register(TradeTL.class);
	}

	@Before
	public void setUp() {
		datastoreHelper.setUp();
		memcacheHelper.setUp();
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
	
	/**
	 * Test de memcache (cache de memoria)
	 */
	private void testMemcache() {
	    MemcacheService ms = MemcacheServiceFactory.getMemcacheService();
	    assertFalse(ms.contains("clavePrueba"));
	    ms.put("clavePrueba", "valorDePrueba");
	    assertTrue(ms.contains("clavePrueba"));
	}

	@Test
	public void testInsertDataStore1() {
		testDatastore();
	}

	@Test
	public void testInsertDataStore2() {
		testDatastore();
	}
	
	@Test
	public void testInsertmemcache1() {
		testMemcache();
	}

	@Test
	public void testInsertmemcache2() {
		testMemcache();
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
	
	@Test
	public void testGuardarRecuperarItemTL(){
		ItemTL item = recuperarItem(insertarItem());
		assertEquals(idMLItem ,item.idRefML);
	}
	
	@Test
	public void testGuardarRecuperarItemTL2(){
		ItemTL item = recuperarItem(insertarItem());
		assertEquals(idMLItem ,item.idRefML);
	}
	
}
