package com.hercules.truequelibre;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.gson.JsonObject;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;
import com.googlecode.objectify.cmd.Query;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Cookie;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;
import org.restlet.resource.Post;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.exception.FacebookOAuthException;
//import com.restfb.types.Post;
import com.restfb.types.User;
import com.hercules.truequelibre.FacebookDataCollector;
public class ExchangeResource extends ParameterGathererTemplateResource {
	
	
	@Override
	protected Representation get() throws ResourceException {
		String message = "Trueque Libre!"
			+ "\n con el numero de usuario: " + (String) this.getRequest().getAttributes().get("userId")
			+ "\n";
		Series<Cookie> cookies = getCookies();	
		String token = cookies.getValues("accessToken");
		User user = FacebookDataCollector.getInstance().findUserWithRest(token);
		String userName =   user.getFirstName();
		if (userName == null){
		 userName = user.getLastName();
		}else{
			userName += " " + user.getLastName();
		}
		if(FacebookDataCollector.getInstance().isTheUser(user,this.requestedUser())){
		message += "Bienvenido! Su usuario es " +userName + " y su id de facebook " + user.getId() + "\n";
		}else{
			if(FacebookDataCollector.getInstance().isAFriend(token, this.requestedUser())){
				message += "siii est u amigoo! :D \n";
			}
		message += FacebookDataCollector.getInstance().getFriendData(token, this.requestedUser());
		}
		
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("<html>");
		stringBuilder.append("<head><title>Proponé un trueque "
				+ "</title></head>");
		stringBuilder.append("<body bgcolor=white>");
		stringBuilder.append("<table border=\"0\">");
		stringBuilder.append("<tr>");
		stringBuilder.append("<td>");
				stringBuilder
				.append("<ol><form name=\"search\" "
						+ "action=\"/api/search\""
						+ "method=\"post\">"
						+ "<input type=\"text\" name=\"query\">"
						+ "<input type=\"submit\" value=\"Search\"> </form></a> --> returns search results for input criteria");
		stringBuilder.append("</ol>");
		stringBuilder.append("</td>");
		stringBuilder.append("</tr>");
		stringBuilder.append("</table>");
		stringBuilder.append("</body>");
		stringBuilder.append("</html>");
		
		return new StringRepresentation(message, MediaType.TEXT_PLAIN);
	}
	
	 @Post
	    public Representation post(Representation entity) {  
	        // Obtener los datos enviados por post
	        Form form = new Form(entity); 
	        String uid = this.requestedUser();
	        String itemId= form.getFirstValue("itemId");
	        String tokenfb = getCookies().getValues("accessToken");//form.getFirstValue("token");  
	        User userfb= FacebookDataCollector.getInstance().findUserWithRest(tokenfb);
	        
	        JsonObject message=new JsonObject();
			if(!FacebookDataCollector.getInstance().isTheUser(userfb, uid)){//autenticar
	        	message.addProperty("error", "El usuario no corresponde con el token");
	        	return new StringRepresentation(message.toString(), MediaType.TEXT_PLAIN);
	        }
			UserTL usuario = UserTL.load(uid);
			ItemTL item=new ItemTL(itemId);
			if(!usuario.items.contains(item)){
				usuario.items.add(item);
			}
			usuario.save();
			message.addProperty("info", "El item se agrego correctamente");
	        return new StringRepresentation(message.toString(), MediaType.TEXT_PLAIN);
	        //todo autenticar, obtener user desde la db, agregar item y guardar
	    }
}
