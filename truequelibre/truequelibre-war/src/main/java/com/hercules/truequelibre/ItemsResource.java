package com.hercules.truequelibre;

//import com.hercules.truequelibre.FbProperties;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Cookie;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.User;
import org.restlet.util.Series;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hercules.truequelibre.FacebookDataCollector;
import com.hercules.truequelibre.mlsdk.Meli;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.MultivaluedMap;
import com.hercules.truequelibre.ParameterGathererTemplateResource;

public class ItemsResource extends ParameterGathererTemplateResource {
	
	
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
			+ "\n con el item pedido: " + this.requestedItem()
			+ "\n";
		
		Series<Cookie> cookies = getCookies();	
		String token = cookies.getValues("accessToken");
		try{
			if (FacebookDataCollector.getInstance().informationCanBeShown(token, this.requestedUser())){
				message += "te puedo mostrar la info del item que es: " + this.requestedItem();
				if (this.itemExists()){
					message+= "\n el item pedido existe entre los suyos! \n";
					message+=this.itemInfo(this.requestedItem());
				}else{
					message += "\n no tiene el item entre sus items";
				}
			}else{
				message += "la persona no es amigo suyo";
			}
		}catch(FacebookOAuthException e){
			message = "el token esta desactualizado, por favor actualicelo";
		}
		
		return new StringRepresentation(message, MediaType.TEXT_PLAIN);
	}

	public String itemInfo(String requestedItem) {
		JsonObject searchItem= new JsonObject();
		try {
	       JsonObject item= new Meli().get("items/"+this.requestedItem());
	       searchItem.add("id", item.get("id"));
	       searchItem.add("img", item.get("thumbnail"));
	       searchItem.add("name", item.get("title"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return searchItem.toString();
		//return search.toString();
	}

	private boolean itemExists(){
		return true; // hecho trivial para probar si anda
	}
	
    @Post
    public Representation post(Representation entity) {  
		Representation result = null;  
        // Obtener los datos enviados por post
        Form form = new Form(entity); 
        String uid = form.getFirstValue("userId");  
        String tokenfb = form.getFirstValue("token");  
 
        //todo autenticar, obtener user desde la db, agregar item y guardar
        return result;  
    }
	
}