package com.hercules.truequelibre.resources;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.representation.Variant;
import org.restlet.resource.ServerResource;

public class ParameterGathererTemplateResource extends ServerResource {
	
	
	public ParameterGathererTemplateResource() {
		super();
	}

	public ParameterGathererTemplateResource(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
	}

	protected String requestedUser(){
		return (String) this.getRequest().getAttributes().get("userId");
	}
	protected String requestedItem(){
		return (String) this.getRequest().getAttributes().get("itemId");
	}
	
	
}