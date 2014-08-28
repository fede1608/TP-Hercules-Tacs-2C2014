package com.hercules.truequelibre;


import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

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
		
		return new StringRepresentation(message, MediaType.TEXT_PLAIN);
	}

}