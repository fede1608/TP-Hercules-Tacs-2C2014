package com.hercules.truequelibre;
import java.util.Calendar;
import java.util.Map;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.mercadolibre.sdk.Meli;
import com.ning.http.client.FluentStringsMap;

public class ParametersShowerResource extends ServerResource {
	
	public ParametersShowerResource() {
		super();
	}

	public ParametersShowerResource(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
	}

	@Override
	protected Representation get() throws ResourceException {
		String message = "Este es otro recurso, ser routea agregando la clase en HelloApplication \n";
		
		String paramValue = getQuery().getValues("userId");
		message+="El parametro userId es: " + paramValue;
		
		Map<String, String> params = getQuery().getValuesMap();
		message+="\n";
		
		for(String key : params.keySet())
		{
			message+= "Nombre: " +key+" ----------- Valor: "+ params.get(key) + "\n";
		}
		
		return new StringRepresentation(message, MediaType.TEXT_PLAIN);
	}

}
