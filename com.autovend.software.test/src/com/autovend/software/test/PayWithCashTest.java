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
import com.autovend.Numeral;
import com.autovend.devices.BillSlot;
import com.autovend.devices.BillValidator;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.external.ProductDatabases;
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
	public Barcode milk_barcode;
	public Barcode bread_barcode;
	public BarcodedProduct milk_product;
	public BarcodedProduct bread_product;
	
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
        
        milk_barcode = new Barcode(new Numeral[] {Numeral.zero});
		bread_barcode = new Barcode(new Numeral[] {Numeral.one});
		
		milk_product = new BarcodedProduct(milk_barcode,"2% Milk", BigDecimal.valueOf(5), 20);
		bread_product = new BarcodedProduct(bread_barcode,"White Bread", BigDecimal.valueOf(3), 5);
		
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(milk_barcode, milk_product);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(bread_barcode, bread_product);
		
		selfCheckout.addItemPerUnit(bread_product, 5);
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
    	assertEquals(BigDecimal.valueOf(5), selfCheckout.getTotal(total));
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
    	assertEquals(BigDecimal.valueOf(5), selfCheckout.getChange(change));
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
    	assertEquals(BigDecimal.valueOf(5), selfCheckout.getChange(change));
    }
    
    /*
     * Test when there's no change
     */
    @Test
    public void noChange() {
    	selfCheckout.setTotal(BigDecimal.valueOf(10));
    	listener_1.reactToBillInsertedEvent(billSlot);
    	listener_2.reactToValidBillDetectedEvent(billValidator, currency, 10);
    	selfCheckout.payWithCash();
    	assertEquals(BigDecimal.ZERO, selfCheckout.getChange(change));
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
    	selfCheckout.getChange(change);
    }
    
    
}
