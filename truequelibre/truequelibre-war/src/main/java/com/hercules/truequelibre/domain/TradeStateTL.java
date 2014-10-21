package com.hercules.truequelibre.domain;

import com.googlecode.objectify.annotation.Index;

@Index
public class TradeStateTL {

	private static final int PENDING = 0;
	private static final int ACCEPTED = 1;
	private static final int DECLINED = 2;
	private static final int CANCELLED = 3;
	@Index
	int current;
	
	public TradeStateTL()
	{
		this.current = PENDING;
	}
	
	public int getCurrent()
	{
		return this.current;
	}
	
	public void accept()
	{
		this.current = ACCEPTED;
	}
	
	public void decline()
	{
		this.current = DECLINED;
	}
	
	public void cancel()
	{
		this.current = CANCELLED;
	}
	
	public int parseStringState(String state)
	{
		int intState= 
			"accepted".equalsIgnoreCase(state) ? ACCEPTED :
			"declined".equalsIgnoreCase(state) ? DECLINED :
			"cancelled".equalsIgnoreCase(state) ? CANCELLED :
			"pending".equalsIgnoreCase(state) ? PENDING: -1;
		
		if( -1 == intState )
		{
			throw new IllegalArgumentException("El estado "+ state + " no existe. Los estados posibles son accepted, declined, cancelled y pending.");
		}
		return intState;
	}
	  
}
