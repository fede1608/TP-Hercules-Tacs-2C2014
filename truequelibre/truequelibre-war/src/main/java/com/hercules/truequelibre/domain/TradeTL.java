package com.hercules.truequelibre.domain;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;
import com.hercules.truequelibre.helpers.DBHandler;

@Entity
public class TradeTL {
	@Id public Long id;

	@Unindex
	public ItemTL offeredItem;
	@Unindex
	public ItemTL wantedItem;
	@Index
	long offeredItemId;//Hack para poder hacer query a los trades por id de item
	@Index
	long wantedItemId;//Hack para poder hacer query a los trades por id de item
	@Index
	int state; //Pensar mejor solución?   0: pending, 1: accepted, 2: declined, 3: cancelled
	@Index
	public long date;
	
	public TradeTL(){
		
	}
	public TradeTL(	ItemTL offeredItem, ItemTL wantedItem) {

		this.offeredItem = offeredItem;
		this.wantedItem = wantedItem;
		this.offeredItemId= offeredItem.id;
		this.wantedItemId= wantedItem.id;
		this.state = 0;
		this.date =  System.currentTimeMillis() / 1000L;
	}
	
	public int getState(){
		return this.state;
	}
/*  pensar toda esta parte bien*/
	public void accept() {
	
		//guarda el estado para que no cancele y decline el trade
		//cancelar y rechazar trades item ofrecido y solicitado
		//settear los items como intercambiados
		this.state = 1;
		DBHandler.getInstance().save(this);
		getOfferedItem().exchanged=true;
		getWantedItem().exchanged=true;
		DBHandler.getInstance().save(getOfferedItem());
		DBHandler.getInstance().save(getWantedItem());
		DBHandler.getInstance().cancelAndDeclineTrades(offeredItemId);
		DBHandler.getInstance().cancelAndDeclineTrades(wantedItemId);
	}
	
	public void decline() {
	
		this.state = 2; 
		DBHandler.getInstance().save(this);
	}
	public void cancel() {
		this.state = 3;
		DBHandler.getInstance().save(this);
	}
	
/****************************************/

	


	public ItemTL getOfferedItem() {
		return offeredItem;
	}

	
	public ItemTL getWantedItem() {
		return wantedItem;
	}

	
	
	@Override
	public String toString() //todo mover a clase Estado
	{
		String estado = this.state==0 ? "pending" : this.state ==1? "accepted" : this.state == 2? "declined" : "cancelled"; 
		return "wanted: " + this.getWantedItem().id.toString() + " - offered: " + this.getOfferedItem().id.toString() + " state: " 
				+ estado;
				
	}



}
