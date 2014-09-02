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
import com.hercules.truequelibre.FacebookDataCollector;

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
		return new StringRepresentation(message, MediaType.TEXT_PLAIN);
	}



	private String requestedUser(){
		return (String) this.getRequest().getAttributes().get("userId");
	}

	
}