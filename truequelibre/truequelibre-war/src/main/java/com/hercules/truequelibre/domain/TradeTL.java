package com.hercules.truequelibre.domain;

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
	public long date;
	
	private TradeStateTL stateManager;
	
	public TradeTL(){
		
	}
	public TradeTL(	ItemTL offeredItem, ItemTL wantedItem) {

		this.offeredItem = offeredItem;
		this.wantedItem = wantedItem;
		this.offeredItemId= offeredItem.id;
		this.wantedItemId= wantedItem.id;
		this.stateManager = new TradeStateTL();
		this.date =  System.currentTimeMillis() / 1000L;
	}
	
	public int getState() {
		return this.stateManager.getCurrent();
	}
	public void accept() {
	
		this.stateManager.accept();

		DBHandler.getInstance().save(this);
		ItemTL offeredItem2 = getOfferedItem();
		ItemTL wantedItem2 = getWantedItem();

		offeredItem2.exchanged=true;
		wantedItem2.exchanged=true;
		DBHandler.getInstance().save(offeredItem2);
		DBHandler.getInstance().save(wantedItem2);
		DBHandler.getInstance().cancelAndDeclineTrades(offeredItemId);
		DBHandler.getInstance().cancelAndDeclineTrades(wantedItemId);
	}
	
	public void decline() {
	
		this.stateManager.decline();

		DBHandler.getInstance().save(this);
	}
	public void cancel() {
		this.stateManager.cancel();
		DBHandler.getInstance().save(this);
	}

	public ItemTL getOfferedItem() {
		return offeredItem;
	}

	
	public ItemTL getWantedItem() {
		return wantedItem;
	}

	
	
	@Override
	public String toString()
	{
		String estado = this.getState()==0 ? "pending" : this.getState() ==1? "accepted" : this.getState() == 2? "declined" : "cancelled"; 
		return "wanted: " + this.getWantedItem().id.toString() + " - offered: " + this.getOfferedItem().id.toString() + " state: " 
				+ estado;
				
	}



}
