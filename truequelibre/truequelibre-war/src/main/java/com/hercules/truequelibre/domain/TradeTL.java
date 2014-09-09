package com.hercules.truequelibre.domain;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class TradeTL {
	@Id public Long id;

	@Index
	public ItemTL offeredItem;
	@Index
	public ItemTL wantedItem;
	@Index
	public boolean pending;//HACER STATE? pending, accepted, rejected, cancelled
	
	public boolean active;
	
	public void acceptTrade() {
	
		this.pending = false;
		//logica de aceptar
	}
	
	public void declineTrade() {
	
		this.pending = false;
		//logica de cancelar
	}

	public TradeTL(	ItemTL offeredItem, ItemTL wantedItem) {

		this.offeredItem = offeredItem;
		this.wantedItem = wantedItem;
		this.active = true;
	}


	public ItemTL getOfferedItem() {
		return offeredItem;
	}

	public void setOfferedItem(ItemTL offeredItem) {
		this.offeredItem = offeredItem;
	}

	public ItemTL getWantedItem() {
		return wantedItem;
	}

	public void setWantedItem(ItemTL wantedItem) {
		this.wantedItem = wantedItem;
	}
	
	@Override
	public String toString()
	{
		return "wanted: " + wantedItem.id.toString() + " - offered: " + offeredItem.id.toString() + " - active: " + active;
				
	}



}
