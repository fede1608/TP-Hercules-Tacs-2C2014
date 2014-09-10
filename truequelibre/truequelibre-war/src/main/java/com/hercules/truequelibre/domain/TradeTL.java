package com.hercules.truequelibre.domain;



import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;
import com.hercules.truequelibre.domain.ItemTL.Deref;

@Entity
public class TradeTL {
	@Id public Long id;

	@Unindex
	public ItemTL offeredItem;
	@Unindex
	public ItemTL wantedItem;
	@Index
	int state; //Pensar mejor solución?   0: pending, 1: accepted, 2: declined, 3: cancelled
	public String date;
	
	public TradeTL(){
		
	}
	public TradeTL(	ItemTL offeredItem, ItemTL wantedItem) {

		this.offeredItem = offeredItem;
		this.wantedItem = wantedItem;
		this.state = 0;
	}
	
	public int getState(){
		return this.state;
	}
/*  pensar toda esta parte bien*/
	public void accept() {
	
		this.state = 1;
		//logica de aceptar
	}
	
	public void decline() {
	
		this.state = 2; 
		//logica de cancelar
	}
	public void cancel() {
		this.state = 3;
	}
	
/****************************************/

	


	public ItemTL getOfferedItem() {
		return offeredItem;
	}

	
	public ItemTL getWantedItem() {
		return wantedItem;
	}

	
	
	@Override
	public String toString()
	{
		String estado = this.state==0 ? "pending" : this.state ==1? "accepted" : this.state == 2? "declined" : "cancelled"; 
		return "wanted: " + this.getWantedItem().id.toString() + " - offered: " + this.getOfferedItem().id.toString() + " state: " 
				+ estado;
				
	}



}
