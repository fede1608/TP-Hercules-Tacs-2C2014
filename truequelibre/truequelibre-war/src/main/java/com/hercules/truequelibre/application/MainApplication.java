package com.hercules.truequelibre.application;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import com.hercules.truequelibre.domain.ItemTL;
import com.hercules.truequelibre.domain.TradeTL;
import com.googlecode.objectify.ObjectifyService;
import com.hercules.truequelibre.resources.FriendsResource;
import com.hercules.truequelibre.resources.ItemListResource;
import com.hercules.truequelibre.resources.ItemNewsFeedResource;
import com.hercules.truequelibre.resources.ItemSingleResource;
import com.hercules.truequelibre.resources.ParametersShowerResource;
import com.hercules.truequelibre.resources.SearchResource;
import com.hercules.truequelibre.resources.PendingTradesResource;
import com.hercules.truequelibre.resources.SingleTradeResource;
import com.hercules.truequelibre.resources.UsersResource;

/**
 * <p>Clase principal del backend</p> 
 * <p>Registra las clases a persistir en la base de datos</p>
 */
public class MainApplication extends Application {

	//Las clases a persistir deben registrarse en el punto de inicio de la aplicacion
	static{
		ObjectifyService.register(ItemTL.class);
		ObjectifyService.register(TradeTL.class);
	}
	
	public MainApplication() {
		super();
	}

	public MainApplication(Context parentContext) {
		super(parentContext);
	}

	/**
	 * Carga los endpoints de la aplicacion al router, a los cuales el usuario puede acceder desde el browser
	 * @return Router
	 */
	@SuppressWarnings("deprecation")
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.setDefaultMatchingMode(Template.MODE_STARTS_WITH);
		router.setRoutingMode(Router.BEST);

		router.attach("/parametersshower", ParametersShowerResource.class);//todo eliminar
		router.attach("/search", SearchResource.class);
		router.attach("/users/{userId}",UsersResource.class);
		router.attach("/users/{userId}/items",ItemListResource.class);
		router.attach("/users/{userId}/items/{itemId}",ItemSingleResource.class);
		router.attach("/friends",FriendsResource.class);
		router.attach("/pendingTrades",PendingTradesResource.class);
		router.attach("/pendingTrades/{tradeId}",SingleTradeResource.class);
		router.attach("/feed",ItemNewsFeedResource.class);
		
		Restlet mainpage = new Restlet() {
			
		};
		router.attach("", mainpage);

		return router;
	}

}