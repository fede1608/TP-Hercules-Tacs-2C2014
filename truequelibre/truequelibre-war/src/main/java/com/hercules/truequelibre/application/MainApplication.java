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

import com.hercules.truequelibre.TradeResource;
import com.hercules.truequelibre.domain.ItemTL;
import com.googlecode.objectify.ObjectifyService;
import com.hercules.truequelibre.resources.FriendsResource;
import com.hercules.truequelibre.resources.ItemsResource;
import com.hercules.truequelibre.resources.ParametersShowerResource;
import com.hercules.truequelibre.resources.SearchResource;
import com.hercules.truequelibre.resources.UsuarioResource;


public class MainApplication extends Application {

	//Las clases a persistir deben registrarse en el punto de inicio de la aplicacion
	static{
		ObjectifyService.register(ItemTL.class);
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

		router.attach("/search", SearchResource.class);

		router.attach("/parametersshower", ParametersShowerResource.class);

		router.attach("/users/{userId}",UsuarioResource.class);
		
		router.attach("/users/{userId}/items",ItemsResource.class);
		router.attach("/users/{userId}/items/{itemId}",ItemsResource.class);

		router.attach("	api/users/{userId}/items/{itemId}/exchange",TradeResource.class);
		
		router.attach("/friends",FriendsResource.class);
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