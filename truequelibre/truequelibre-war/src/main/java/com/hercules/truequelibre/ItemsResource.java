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


public class ItemsResource extends ServerResource {
	
	
	public ItemsResource() {
		super();
	}

	public ItemsResource(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
	}

	@Override
	protected Representation get() throws ResourceException {
		String message = "Trueque Libre!"
			+ "\n la pagina que ingreso es: " + this.getReference()
			+ "\n con el recurso: " + this.getReference().getBaseRef()
			+ "\n con el numero de usuario: " + (String) this.getRequest().getAttributes().get("userId")
			+ "\n con el item pedido" + this.requestedItem()
			+ "\n";
		Series<Cookie> cookies = getCookies();	
		String token = cookies.getValues("accessToken");
		if (FacebookDataCollector.getInstance().informationCanBeShown(token, this.requestedUser())){
			message += "te puedo mostrar la info del item que es: " + this.requestedItem();
			if (this.itemExists()){
				message+= "el item pedido existe entre los suyos!";
			}else{
				message += "no tiene el item entre sus items";
			}
		}else{
			message += "la persona no es amigo suyo";
		}
		
		return new StringRepresentation(message, MediaType.TEXT_PLAIN);
	}

	private String requestedUser(){
		return (String) this.getRequest().getAttributes().get("userId");
	}
	private String requestedItem(){
		return (String) this.getRequest().getAttributes().get("itemId");
	}
	private boolean itemExists(){
		return true; // hecho trivial para probar si anda
	}
	
}