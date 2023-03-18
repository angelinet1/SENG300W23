package com.autovend.software.test;

import org.junit.After;
import org.junit.Before;

import com.autovend.devices.BillSlot;
import com.autovend.software.BillSlotObserverStub;

public class PayWithCashTest {

	public BillSlotObserverStub listener_1, listener_2, listener_3; // listeners from stub
	private BillSlot billSlot; // create bill slot
	
	@Before
	public void setup() {
		// create listener objects
	    listener_1 = new BillSlotObserverStub();
	    listener_2 = new BillSlotObserverStub();
	    listener_3 = new BillSlotObserverStub();
	    
	    // register listener 1 and 2
	    billSlot.register(listener_1);
	    billSlot.register(listener_2);
	    
	    // disable and enable BillSlot to register listeners with device
	    billSlot.disable();
	    billSlot.enable();	
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
