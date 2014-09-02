package com.hercules.truequelibre;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;
import com.googlecode.objectify.cmd.Query;
import com.hercules.truequelibre.FbProperties;

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
import com.restfb.DefaultJsonMapper;
import com.restfb.Facebook;
import com.restfb.FacebookClient;
import com.restfb.JsonMapper;
import com.restfb.Parameter;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonObject;
import com.restfb.types.Page;
import com.restfb.types.Post;
import com.restfb.types.Url;
import com.restfb.types.User;

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
		
		String friends = this.findFacebookFriendsUsingRest(token);
		message += friends;		
		return new StringRepresentation(message, MediaType.TEXT_PLAIN);
	}
	
	public String findFacebookFriendsUsingRest(String facebookAccessToken){
		 			  
		  final FacebookClient facebookClient;
		 // FbProperties prop = FbProperties.getInstance();
		  UsuarioDAO usuario = null;
		  String resultadoPersistido = "\n";
		  
		  String appId= "595790490538541";
		  String appSecret= "ee3c67442fbbd654ed67bd7722cf26b9";
		//  String generatedAccessToken = new DefaultFacebookClient().obtainAppAccessToken(appId,appSecret).getAccessToken();
		  LoggedInFacebookClient fb2= new LoggedInFacebookClient(appId,appSecret);
		  facebookClient = new DefaultFacebookClient(facebookAccessToken);
		  User user = facebookClient.fetchObject("me", User.class);
		  
		  String userName =   user.getFirstName();
		  if (userName == null){
		  userName = user.getLastName(); 
		  }
		 
		  Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class,
				  Parameter.with("fields", "id,first_name,last_name,name,gender"));
		  System.out.println("Count of my friends: " + myFriends.getData().size());
		  
		  Connection<Post> myFeed = facebookClient.fetchConnection("me/feed", Post.class);
//		  Post firstFeedPost = myFeed.getData().get(0);
		  String myFacebookFriendList="Los amigos de " +userName +" "+ user.getLastName()+ "\ncon Id: "+user.getId()+ "\nson:\n";
		  for(User friend: myFriends.getData()){
		  System.out.println("Friends id and name: "+friend.getId()+" , "+friend.getName());   
		    myFacebookFriendList += friend.getName()+"\n";
		    //persistencia de amigos
		  usuario = new UsuarioDAO(friend.getId(), friend.getName());
		  ofy().save().entity(usuario).now();
		  }
		  
		  //recuperacion del ultimo amigo almacenado
		  Key<UsuarioDAO> clave = Key.create(UsuarioDAO.class, usuario.id);
		  Result<UsuarioDAO> result = ofy().load().key(clave);
		  UsuarioDAO fetched1 = result.now();
		  resultadoPersistido += "\nUltimo resultado persistido recuperado:\n"+fetched1.id+"\n";
		  resultadoPersistido += fetched1.name+"\n";
		  myFacebookFriendList += resultadoPersistido;
		  
		  //recuperacion de articulos obtenidos en api/search
		  String resultadoPersistidoDeSearch = "\nResultados persistidos de api/search?query=criterio\n";
		  Query<ArticuloDAO> q = ofy().load().type(ArticuloDAO.class);
		  for(ArticuloDAO art: q){
			  resultadoPersistidoDeSearch +="\n"+art.nombre;
		  }
		  if(q.count() == 0) resultadoPersistidoDeSearch += "Ingresa primero en algun api/search?query=criterioDeBusqueda";
		  myFacebookFriendList += resultadoPersistidoDeSearch;
		  
		  return myFacebookFriendList;
	}

	
	
}
