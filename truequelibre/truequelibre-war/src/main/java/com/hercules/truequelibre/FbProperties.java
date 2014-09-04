package com.hercules.truequelibre;

public class FbProperties{
	
	private static FbProperties instancia;
	String appId = "595790490538541"; 
	String appSecret = "ee3c67442fbbd654ed67bd7722cf26b9";
	
	private FbProperties(){

	}
	
	public static FbProperties getInstance() { 
		if(instancia == null)
		  instancia = new FbProperties();
		  return instancia;
	  }
	
}

