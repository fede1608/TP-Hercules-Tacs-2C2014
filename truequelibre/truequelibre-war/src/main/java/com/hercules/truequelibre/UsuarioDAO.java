package com.hercules.truequelibre;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class UsuarioDAO {
	@Id String id;
	@Index String name;
	
	public UsuarioDAO(String id, String name){
		this.id = id;
		this.name = name;
	}
}
