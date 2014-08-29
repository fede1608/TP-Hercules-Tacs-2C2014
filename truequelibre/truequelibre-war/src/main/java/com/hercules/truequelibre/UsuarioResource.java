package com.hercules.truequelibre;

//import com.hercules.truequelibre.FbProperties;
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
import com.restfb.types.User;
import org.restlet.util.Series;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;


public class UsuarioResource extends ServerResource {
	
	
	public UsuarioResource() {
		super();
	}

	public UsuarioResource(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
	}

	@Override
	protected Representation get() throws ResourceException {
		String message = "Trueque Libre!"
			+ "\n la pagina que ingreso es: " + this.getReference()
			+ "\n con el recurso: " + this.getReference().getBaseRef()
			+ "\n con el numero de usuario: " + (String) this.getRequest().getAttributes().get("userId")
			+ "\n";
		Series<Cookie> cookies = getCookies();	
		String token = cookies.getValues("accessToken");
		User user = this.findUserWithRest(token);
		String userName =   user.getFirstName();
		if (userName == null){
		 userName = user.getLastName();
		}else{
			userName += " " + user.getLastName();
		}
		if(this.isTheUser(user)){
		message += "Bienvenido! Su usuario es " +userName + " y su id de facebook " + user.getId() + "\n";
		}else{
			if(this.isAFriend(token, this.requestedUser())){
				message += "siii est u amigoo! :D \n";
			}
		message += this.getFriendData(token, this.requestedUser());
		}
		return new StringRepresentation(message, MediaType.TEXT_PLAIN);
	}

	private boolean isTheUser(User user) {
		return user.getId().equals(this.requestedUser());
	}

	private User findUserWithRest(String accessToken) {
//		FbProperties fp = FbProperties.getInstance();
		FacebookClient faceClient= new DefaultFacebookClient(accessToken);
		
		return faceClient.fetchObject("me",User.class);
	}
	public String getFriendData(String facebookAccessToken, String friendId){
		  FacebookClient facebookClient = new DefaultFacebookClient(facebookAccessToken);
		  	 
		  Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class,
				  Parameter.with("fields", "id,first_name,last_name,name,gender"));
		  String message="";
		   for(User friend: myFriends.getData()){
			  if (friend.getId().equals(friendId)){
				  message += "Informacion de amigo encontrado, es: " + friend.getName();
				  return message; 
			  }else{
				  message += "analizado el usuario " + friend.getId() + " " + friend.getFirstName() + "\n";
			  }
		  }
		  
		  return message + "el usuario pedido no esta entre sus amigos";
	}
	private String requestedUser(){
		return (String) this.getRequest().getAttributes().get("userId");
	}
	public boolean isAFriend(String facebookAccessToken, String friendId){
		  FacebookClient facebookClient = new DefaultFacebookClient(facebookAccessToken);
		  	 
		  Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class,
				  Parameter.with("fields", "id"));
		  return myFriends.getData().contains(this.requestedUser());
		  
		  
	}
	
}