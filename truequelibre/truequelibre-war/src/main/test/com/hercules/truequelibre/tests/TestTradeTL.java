import org.junit.Assert.*;
import org.junit.*;

import com.hercules.truequelibre.domain.TradeTL;

public class TestTradeTL {
	
	private static final int PENDING = 0;
	private static final int ACCEPTED = 1;
	private static final int DECLINED = 2;
	private static final int CANCELLED = 3;
	
	TradeTL trade; 
	@Before
	public void setUp() {
		trade = new TradeTL();
	}
	
	@Test
	public void SePuedeAceptarTrade() {
		trade.accept();
		assertEquals(trade.getState(),ACCEPTED);
	} 
	@Test
	public void SePuedeRechazarTrade() {
		trade.decline();
		assertEquals(trade.getState(),DECLINED);
	} 
	
	@Test
	public void SePuedeCancelarTrade() {
		trade.cancel();
		assertEquals(trade.getState(),CANCELLED);
	}
		
	
}