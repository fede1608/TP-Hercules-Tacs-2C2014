package com.hercules.truequelibre.domain;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity

public class TradeTL {
	@Id public Long id;

	public ItemTL offeredItem;
	public ItemTL wantedItem;
	
	
	public void acceptTrade() {
	
		throw new NotImplementedException();
	}
	
	public void declineTrade() {
	
		throw new NotImplementedException();
	}

	public TradeTL(	ItemTL offeredItem, ItemTL wantedItem) {

		this.offeredItem = offeredItem;
		this.wantedItem = wantedItem;
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



}
