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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hercules.truequelibre.FacebookDataCollector;
import com.hercules.truequelibre.mlsdk.Meli;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.MultivaluedMap;


public class ItemsResource extends ServerResource {
	
	
	public ItemsResource() {
		super();
	}

	public ItemsResource(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
	}

	@Override
	protected Representation get() throws ResourceException {
		Meli m = new Meli(7937694478293453L, "UUY3czo96JZDtnsFI2iMt0vIzMBukOtB");
		String message = "Trueque Libre!"
			+ "\n la pagina que ingreso es: " + this.getReference()
			+ "\n con el recurso: " + this.getReference().getBaseRef()
			+ "\n con el numero de usuario: " + (String) this.getRequest().getAttributes().get("userId")
			+ "\n con el item pedido: " + this.requestedItem()
			+ "\n";
		
		Series<Cookie> cookies = getCookies();	
		String token = cookies.getValues("accessToken");
		if (FacebookDataCollector.getInstance().informationCanBeShown(token, this.requestedUser())){
			message += "te puedo mostrar la info del item que es: " + this.requestedItem();
			if (this.itemExists()){
				message+= "\n el item pedido existe entre los suyos!";
				message+=this.itemInfo(m,this.requestedItem());
			}else{
				message += "\n no tiene el item entre sus items";
			}
		}else{
			message += "la persona no es amigo suyo";
		}
		
		return new StringRepresentation(message, MediaType.TEXT_PLAIN);
	}

	public String itemInfo(Meli m,String requestedItem) {
		JsonArray search= new JsonArray();
		JsonObject algo;
		try {
	       JsonObject response= m.get("/items/"+this.requestedItem());
	       JsonArray results=response.getAsJsonArray("results");
	       algo=response;
	       for(int i=0; i<results.size();i++){
	    	   JsonObject item= results.get(i).getAsJsonObject();
	    	   JsonObject searchItem= new JsonObject();
	    	   searchItem.add("id", item.get("id"));
	    	   searchItem.add("img", item.get("thumbnail"));
	    	   searchItem.add("name", item.get("title"));
	    	   search.add(searchItem);
	       }

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return algo.toString();
		//return search.toString();
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