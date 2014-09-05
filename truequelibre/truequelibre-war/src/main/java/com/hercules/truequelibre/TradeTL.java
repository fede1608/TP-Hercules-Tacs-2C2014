package com.hercules.truequelibre;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity

public class TradeTL {
	@Id public String id;
	public UserTL requesterUser;
	public UserTL requestedUser;
	public ItemTL offeredItem;
	public ItemTL wantedItem;
	
	
	public void acceptTrade() {
		this.getRequesterUser().items.remove(offeredItem);
		this.getRequestedUser().items.add(offeredItem);
		this.getRequesterUser().items.add(wantedItem);
		this.getRequestedUser().items.remove(wantedItem);
	}
	
	public void declineTrade() {
	
		//Borrar de trades? Avisar que fue rechazado?
	}

	public TradeTL(UserTL requesterUser, UserTL requestedUser,
			ItemTL offeredItem, ItemTL wantedItem) {
		
		this.requesterUser = requesterUser;
		this.requestedUser = requestedUser;
		this.offeredItem = offeredItem;
		this.wantedItem = wantedItem;
	}

	public UserTL getRequesterUser() {
		return requesterUser;
	}

	public void setRequesterUser(UserTL requesterUser) {
		this.requesterUser = requesterUser;
	}

	public UserTL getRequestedUser() {
		return requestedUser;
	}

	public void setRequestedUser(UserTL requestedUser) {
		this.requestedUser = requestedUser;
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
