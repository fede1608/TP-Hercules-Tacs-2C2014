package com.hercules.truequelibre.application;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;
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
			
			
			@Override
			public void handle(Request request, Response response) {
				

				StringBuilder stringBuilder = new StringBuilder();
				
				stringBuilder.append("<html>");
				stringBuilder.append("<head><title>Hello Application "
						+ "Servlet Page</title></head>");
				stringBuilder.append("<body bgcolor=white>");
				stringBuilder.append("<table border=\"0\">");
				stringBuilder.append("<tr>");
				stringBuilder.append("<td>");
				stringBuilder.append("<h3>available REST calls</h3>");
				stringBuilder
						.append("<ol><form name=\"search\" "
								+ "action=\"/api/search\""
								+ "method=\"get\">"
								+ "<input type=\"text\" name=\"query\">"
								+ "<input type=\"submit\" value=\"Search\"> </form></a> --> returns search results for input criteria");
				stringBuilder.append("</ol>");
				stringBuilder.append("</td>");
				
				stringBuilder
				.append("<ol><form name=\"tradeproposal\" "
						+ "action=\"REPLACED BY JAVASCRIPT"
						+ "method=\"post\">"
						+ "User <input type=\"text\" name=\"userId\">"
						+ "Item <input type=\"text\" name=\"itemId\">"
						+ "<input type=\"button\" value=\"Propose trade\"> </form></a> --> Proposes a trade");
				stringBuilder.append("</ol>");
		
				
				stringBuilder.append("</tr>");
				stringBuilder.append("</table>");
				stringBuilder.append("</body>");
				stringBuilder.append("</html>");

				response.setEntity(new StringRepresentation(stringBuilder
						.toString(), MediaType.TEXT_HTML));
			}
		};
		router.attach("", mainpage);

		return router;
	}

}