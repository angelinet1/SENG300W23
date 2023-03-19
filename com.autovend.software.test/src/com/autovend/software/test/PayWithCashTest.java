/*
 *  @author: Angeline Tran (301369846),
 *  @author: Tyson Hartley (30117135), 
 *  @author: Jeongah Lee (30137463), 
 *  @author: Tyler Nguyen (30158563), 
 *  @author: Diane Doan (30052326), 
 *  @author: Nusyba Shifa (30162709)
 */


package com.autovend.software.test;

import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Currency;
import java.math.BigDecimal;


import com.autovend.Barcode;
import com.autovend.Bill;
import com.autovend.devices.BillSlot;
import com.autovend.devices.BillValidator;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;
import com.autovend.software.BillSlotObserverStub;
import com.autovend.software.BillValidatorObserverStub;
import com.autovend.software.CashIO;
import com.autovend.software.CustomerIO;
import com.autovend.software.SelfCheckoutMachineLogic;
import com.autovend.software.TransactionReceipt;

public class PayWithCashTest {

	public BillSlotObserverStub listener_1; // listener from stub
	public BillValidatorObserverStub listener_2; // listener from stub
	private Currency currency;
	private int[] denominations;
	private BigDecimal[] coinDom;
	private SelfCheckoutMachineLogic selfCheckout;
	private SelfCheckoutStation scs; 
	private BillValidator billValidator;
	private BillSlot billSlot;
	public BigDecimal remainder;
	public BigDecimal change;
	public BigDecimal total;
	public BigDecimal billValue;
	public Barcode barcode;
	public CashIO cashIO;
	public CustomerIO customerIO;
	public boolean billValidEvent = false;
	public boolean billInsertedEvent = false;
	public boolean barcodeScannedEvent = false;
	
	
	@Before
	public void setup() {
		// create new Bill
		currency = Currency.getInstance("USD");
		denominations = new int[] {5, 10, 15};
		coinDom = new BigDecimal[] {BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10),BigDecimal.valueOf(0.25)};
		billValidator = new BillValidator(currency, denominations);
		billSlot = new BillSlot(barcodeScannedEvent);
		TransactionReceipt currentBill = new TransactionReceipt();
		
		// create self checkout machine and i/o's
		scs = new SelfCheckoutStation(currency, denominations , coinDom, 10000, 2);
		selfCheckout = new SelfCheckoutMachineLogic(scs);
		cashIO = new CashIO();
		customerIO = new CustomerIO();
		
		// create listener objects
		listener_1 = new BillSlotObserverStub();
	    listener_2 = new BillValidatorObserverStub();
	  
	    // register listener 1 and 2
        billSlot.register(listener_1);
        billValidator.register(listener_2);
        
        // disable and enable BillValidator to register listeners with device
        billSlot.disable();
        billSlot.enable();
	}
	
	/*
     * Tear down objects to null
     */
    @After
    public void tearDown() {
    	scs = null;
    	selfCheckout = null;
    	listener_1 = null;
    	listener_2 = null;
    	
    }
    
    /*
     * When no bill is inserted- expected: no events
     */
    @Test
    public void noBillInserted() {
    	selfCheckout.setTotal(BigDecimal.valueOf(5));
    	selfCheckout.payWithCash();
    	assertEquals(BigDecimal.valueOf(5), selfCheckout.getTotal());
    }
    
    /*
     * When a valid bill is inserted
     */
    @Test
    public void ValidBillInserted() {
    	selfCheckout.setTotal(BigDecimal.valueOf(5));
    	listener_1.reactToBillInsertedEvent(billSlot);
    	listener_2.reactToValidBillDetectedEvent(billValidator, currency, 10);
    	selfCheckout.payWithCash();
    	assertEquals(BigDecimal.valueOf(5), selfCheckout.getChange());
    }
    
    /*
     * When an invalid bill is inserted
     */
    @Test
    public void InvalidBillInserted() {
    	selfCheckout.setTotal(BigDecimal.valueOf(5));
    	listener_1.reactToBillInsertedEvent(billSlot);
    	listener_2.reactToInvalidBillDetectedEvent(billValidator);
    	selfCheckout.payWithCash();
    	assertEquals(BigDecimal.valueOf(5), selfCheckout.getChange());
    }
    
    /*
     * Test Customer IO
     */
    @Test
    public void CustomerNotified(){
    	selfCheckout.setTotal(total);
    }
    
    
    /*
     * Test Cash IO
     */
    @Test
    public void CashNotified() {
    	selfCheckout.getChange();
    }
    
    
}
