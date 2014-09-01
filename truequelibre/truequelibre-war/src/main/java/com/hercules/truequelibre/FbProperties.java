package com.hercules.truequelibre;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FbProperties extends Properties {
	
	private static final long serialVersionUID = 1L;
	private static FbProperties instancia;
	
	private FbProperties(){
		//lee el archivo de propierties de la app
		  String DIR = "app_facebook.properties";
		  InputStream istream = null;
		  try{
			  istream = new FileInputStream(DIR);
			  this.load(istream);
			  istream.close();
		  } catch(IOException ioexcep){
			  ioexcep.printStackTrace();
		  } finally {
		        if (istream != null) {
		            try {
		                istream.close();
		            } catch (IOException ioexcep) {
		                ioexcep.printStackTrace();
		            }
		        }
		  }		
	}
	
	public static FbProperties getInstance() { 
		if(instancia == null)
		  instancia = new FbProperties();
		  return instancia;
	  }
	
}

