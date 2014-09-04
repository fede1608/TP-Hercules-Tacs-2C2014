package com.hercules.truequelibre;


//import com.hercules.truequelibre.FbProperties;

import static com.googlecode.objectify.ObjectifyService.ofy;
import com.google.gson.JsonObject;
import com.googlecode.objectify.cmd.Query;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Cookie;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.User;
import com.hercules.truequelibre.FacebookDataCollector;


public class FriendsResource extends ServerResource{

	public FriendsResource()
	{
		super();
	}	
	public FriendsResource(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
	}

	@Override
	protected Representation get() throws ResourceException {
		
		String message = "El token de acceso guardado en la cookie es: \n";

		Series<Cookie> cookies = getCookies();	
		String token = cookies.getValues("accessToken");
		message+= token +"\n \n";
		try{
			String friends = this.findFacebookFriendsUsingRest(token);
			message += friends;
		}catch(FacebookOAuthException e){
			JsonObject j = new JsonObject();
			j.addProperty("error", "El token esta desactualizado, por favor actualicelo");
			message = j.toString();
		}
		return new StringRepresentation(message, MediaType.TEXT_PLAIN);
	}
	
	public String findFacebookFriendsUsingRest(String facebookAccessToken){
		 			  
		  final FacebookClient facebookClient;
		  facebookClient = new DefaultFacebookClient(facebookAccessToken);
		  User user = facebookClient.fetchObject("me", User.class);
		  String userName =   user.getFirstName();
		  if (userName == null){
		  userName = user.getLastName(); 
		  }
		 
		  Connection<User> myFriends = FacebookDataCollector.getInstance().getFriends(facebookAccessToken);
		  System.out.println("Count of my friends: " + myFriends.getData().size()); 
		  String myFacebookFriendList="Los amigos de " +userName +" "+ user.getLastName()+ "\ncon Id: "+user.getId()+ "\nson:\n";
		  	  
		  //recuperacion de articulos obtenidos en api/search
		  String resultadoPersistidoDeSearch = "\nResultados persistidos de api/search?query=criterio\n";
		  Query<ItemTL> q = ofy().load().type(ItemTL.class);
		  for(ItemTL art: q){
			  resultadoPersistidoDeSearch +="\n"+art.nombre;
		  }
		  if(q.count() == 0) resultadoPersistidoDeSearch += "Ingresa primero en algun api/search?query=criterioDeBusqueda";
		  myFacebookFriendList += resultadoPersistidoDeSearch;
		  
		  return myFacebookFriendList;
	}

	
	
}
