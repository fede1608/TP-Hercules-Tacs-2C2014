package com.hercules.truequelibre.domain;

import com.googlecode.objectify.annotation.Index;

/**
 *Clase que sirve para definir el estado en que se encuentra una solicitud de intercambio
 */
@Index
public class TradeStateTL {

	private static final int PENDING = 0;
	private static final int ACCEPTED = 1;
	private static final int DECLINED = 2;
	private static final int CANCELLED = 3;


	@Index
	int current;
	
	/**
	 * Se crea una solicitud en estado pendiente por default
	 */
	public TradeStateTL()
	{
		this.current = PENDING;
	}
	
	/**
	 * @return Estado actual de la solicitud de intercambio
	 */
	public int getCurrent()
	{
		return this.current;
	}
	
	/**
	 * Cuando el solicitado acepta la propuesta, se cambia el estado de la solicitud de intercambio a aceptado
	 */
	public void accept()
	{
		this.current = ACCEPTED;
	}
	
	/**
	 * Cuando el solicitado rechaza la propuesta, se cambia el estado de la solicitud de intercambio a rechazado
	 */
	public void decline()
	{
		this.current = DECLINED;
	}
	
	/**
	 * Cuando el solicitante decide cancelarlo, se cambia el estado de la solicitud de intercambio a cancelado
	 */
	public void cancel()
	{
		this.current = CANCELLED;
	}
	

	  
}
