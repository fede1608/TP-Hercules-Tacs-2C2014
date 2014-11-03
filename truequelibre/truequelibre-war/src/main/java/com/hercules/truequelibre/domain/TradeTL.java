package com.hercules.truequelibre.domain;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;
import com.hercules.truequelibre.helpers.DBHandler;

/**
 * Clase que representa una solicitud de intercambio.
 * <p>Se compone del item interesado, el item a ofrecer, la fecha en la que se realizo y su estado</p>
 */
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
	
	public TradeTL(){}
	
	public TradeTL(	ItemTL offeredItem, ItemTL wantedItem) {

		this.offeredItem = offeredItem;
		this.wantedItem = wantedItem;
		this.offeredItemId= offeredItem.id;
		this.wantedItemId= wantedItem.id;
		this.stateManager = new TradeStateTL();
		this.date =  System.currentTimeMillis() / 1000L;
	}
	
	/**
	 * Obtiene el estado actual de la solicitud de intercambio
	 * @return Estado de la solicitud
	 */
	public int getState() {
		return this.stateManager.getCurrent();
	}
	
	/**
	 * Cuando el usuario del item solicitado acepta la solicitud
	 * <p>Se actualiza el estado del intercambio y el de los items</p>
	 */
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
	
	/**
	 * Cuando el usuario solicitado rechaza la solicitud
	 */
	public void decline() {
	
		this.stateManager.decline();

		DBHandler.getInstance().save(this);
	}
	
	/**
	 * Cuando el usuario solicitante cancela la solicitud
	 */
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

	
	/**
	 *@return un string descriptivo con la informacion de la solicitud de intercambio
	 */
	@Override
	public String toString()
	{
		String estado = this.getState()==0 ? "pending" : this.getState() ==1? "accepted" : this.getState() == 2? "declined" : "cancelled"; 
		return "wanted: " + this.getWantedItem().id.toString() + " - offered: " + this.getOfferedItem().id.toString() + " state: " 
				+ estado;
				
	}



}
