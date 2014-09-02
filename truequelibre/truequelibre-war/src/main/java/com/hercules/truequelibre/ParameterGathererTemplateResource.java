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