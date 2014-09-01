package com.hercules.truequelibre;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class ArticuloDAO {
	@Id String id;
	@Index String nombre;
	
	public ArticuloDAO(String id, String nombre){
		this.id = id;
		this.nombre = nombre;
	}
}
