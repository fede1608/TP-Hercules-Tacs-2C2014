package com.hercules.truequelibre.resources;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.hercules.truequelibre.domain.ItemTL;
import com.hercules.truequelibre.domain.TradeTL;
import com.hercules.truequelibre.helpers.FacebookDataCollector;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
import org.restlet.util.Series;

import com.restfb.types.User;
import com.hercules.truequelibre.helpers.JsonTL;;

public class TradeRequestsResource extends ServerResource{

	public TradeRequestsResource()
	{
		super();
	}	
	public TradeRequestsResource(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
	}

	//	api/users/{userId}/items/{itemId}/exchange
	@Override
	protected Representation get() throws ResourceException {
		Series<Cookie> cookies = getCookies();	
		String token = cookies.getValues("accessToken");
		User user=FacebookDataCollector.getInstance().findUserWithRest(token);
		
		List<ItemTL> items = ofy().load().type(ItemTL.class)
		.filter("owner", user.getId())
		.filter("intercambiado", false).list();
		JsonObject message= new JsonObject();
		message.addProperty("id", user.getId()); 
		message.addProperty("name", user.getName());
		JsonArray itemsPedidos=new JsonArray();
		for (ItemTL item:items){
			if(item.getSolicitudes().size()>0){
				JsonObject jsonItem=JsonTL.jsonifyItem(item);
				JsonArray intercambios = new JsonArray();
				for (ItemTL offeredItem:item.getSolicitudes()){
					intercambios.add(JsonTL.jsonifyItem(offeredItem));
				}
				jsonItem.addProperty("amount", item.getSolicitudes().size());
				jsonItem.add("peticiones", intercambios);
				itemsPedidos.add(jsonItem);
			}
		}
		message.add("requestedTrades", itemsPedidos);
		return new StringRepresentation(message.toString(), MediaType.TEXT_PLAIN);
	}
	
	@Post
    public Representation post(Representation entity) {  
        // Obtener los datos enviados por post
        Form form = new Form(entity); 
        String uid = "";//this.requestedUser();
        String itemId= form.getFirstValue("itemId");
        String tokenfb = getCookies().getValues("accessToken");//form.getFirstValue("token");  
        User userfb= FacebookDataCollector.getInstance().findUserWithRest(tokenfb);
        
        JsonObject message=new JsonObject();
		if(!FacebookDataCollector.getInstance().isTheUser(userfb, uid)){//autenticar
        	message.addProperty("error", "El usuario no corresponde con el token");
        	return new StringRepresentation(message.toString(), MediaType.TEXT_PLAIN);
        }
		//UserTL usuario = UserTL.load(uid);
		//TradeTL exchange = new TradeTL(usuario, usuario, null, null);
		
        return new StringRepresentation(message.toString(), MediaType.TEXT_PLAIN);
        //todo autenticar, obtener user desde la db, agregar item y guardar
    }
	
	

	
	
}
