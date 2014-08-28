package com.hercules.truequelibre;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;
import org.restlet.routing.Router;
import org.restlet.routing.Template;

public class HelloApplication extends Application {

	public HelloApplication() {
		super();
	}

	public HelloApplication(Context parentContext) {
		super(parentContext);
	}

	@SuppressWarnings("deprecation")
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.setDefaultMatchingMode(Template.MODE_STARTS_WITH);
		router.setRoutingMode(Router.BEST);

		router.attach("/hello", HelloResource.class);
		router.attach("/hello/{numItem}",HelloResource.class);

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
						.append("<ol><li><a href=\"app/hello\">hello</a> --> returns hello world message "
								+ "and date string</li>");

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