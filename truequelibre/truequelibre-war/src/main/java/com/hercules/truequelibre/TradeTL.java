package com.hercules.truequelibre;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.hercules.truequelibre.domain.ItemTL;

@Entity

public class TradeTL {
	@Id public String id;

	public ItemTL offeredItem;
	public ItemTL wantedItem;
	
	
	public void acceptTrade() {
		String auxId = wantedItem.id;
		wantedItem.setUser(offeredItem.owner);
		offeredItem.setUser(auxId);
	}
	
	public void declineTrade() {
	
		//Borrar de trades? Avisar que fue rechazado?
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