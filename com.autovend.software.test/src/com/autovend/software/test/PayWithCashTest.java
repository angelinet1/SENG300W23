package com.autovend.software.test;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import java.util.currency;

import com.autovend.Barcode;
import com.autovend.devices.BillSlot;
import com.autovend.software.BillSlotObserverStub;
import com.autovend.software.CashIO;
import com.autovend.software.CustomerIO;
import com.autovend.software.PayWithCash;
import com.autovend.software.TransactionReceipt;

public class PayWithCashTest {

	public BillSlotObserverStub listener_1, listener_2, listener_3; // listeners from stub
	private BillSlot billSlot; // create bill slot
	
	@Before
	public void setup() {
		// create listener objects
		var listener_1 = new BillSlotObserverStub();
	    var listener_2 = new BillSlotObserverStub();
	    var listener_3 = new BillSlotObserverStub();
	  
	    var billSlot = new BillSlot();
	    var barcode = new Barcode();
	    var transactionreciept = new TransactionReceipt();
	    var cashIO = new CashIO();
	    var customerIO = new CustomerIO();
	    
	    

	    
	    // register listener 1 and 2
	    billSlot.register(listener_1);
	    billSlot.register(listener_2);
	    
	    // disable and enable BillSlot to register listeners with device
	    billSlot.disable();
	    billSlot.enable();	
	    
	    //Call main
	    PayWithCash.main();
	    //assertion
	    assertTrue(listener.getInsertedEvent());
	    
	}
	
	/*
     * Tear down objects to null
     */
    @After
    public void tearDown() {
        billSlot = null;
        listener_1 = null;
        listener_2 = null;
        listener_3 = null;
    }
}
