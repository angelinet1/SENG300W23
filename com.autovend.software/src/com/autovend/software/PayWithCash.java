/**
 *  @authors: Angeline Tran (301369846), Tyson Hartley (30117135), Jeongah Lee (30137463), Tyler Nguyen (30158563), Diane Doan (30052326), Nusyba Shifa (30162709)
 */

package com.autovend.software;
import java.util.Currency;
import java.util.List;

import com.autovend.software.AddItemByScanning;
import com.autovend.devices.BillSlot;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.BillStorage;
import com.autovend.Bill;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BillSlotObserver;
import com.autovend.devices.observers.BillDispenserObserver;

public class PayWithCash {
	
	public BillSlotObserverStub listener_1, listener_2, listener_3; // listeners from stub
	private BillSlot billSlot; // create bill slot
	private BillDispenser dispenser; // create bill dispenser
	private Bill bill; // create a bill
	private AddItemByScanning items; // create items added from scanning
	private boolean billInsertedEvent = false;
	private boolean billEjectedEvent = false;
	
	// additems by scanning remaining
	// observer for billslot, on event that bill is inserted, set variable to true nad have a getter int he observer
	// in paywithcash, if true -> start with rest of procedure
	// get value of bill, pass in bill to the slot
	// balance remaining variable comes from additembyscanning, balance - valueofbill
	
	/*
	 * Setup listeners
	 * */
	public void enable() {
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
	
	    
	    billInsertedEvent = billSlot.accept(bill);
	    
	    if(listener_1.billInsertedEvent) {
		    if(billInsertedEvent == true) { // if event is true, continue with procedure
		    	int insertedBill = bill.getValue(); // get value of the inserted bill
		    	//int total = items.getBarcodedProductFromBarcode(null);
		    	int remainder = total - insertedBill; // reduces the remaining amount due by value of inserted bill
		    }
		    else {
		    	billEjectedEvent = true; // indicate that bill was ejected
		    }
	    }
	}
	
	/*
	 * Dispenses the change due to the customer
	 */
	public int getChangeFromDispenser() {
		int change = 0; // local variable change
		List<Bill> bills = dispenser.unload(); // dispense change, get list of bills unloaded
		for(int i = 0; i < bills.size(); i++) {
			change  += ((Bill) bills).getValue(); // get value of the change
		}
		return change;
	}
	
	
	
//	public void calculateTotal() {
//		//this.inserted = getInsertedBills();
//		for(int i = 0; i < inserted.size(); i++) {
//			int updated = total - inserted; // calculate updated total
//		}
//	}
	
//	public void checkRemainder() {
//		if(remaining > 0) {
//			// goto1
//		}
//		else if(remaining < 0) {
//			//signal to Cash I/O 
//		}
	}
	
	


}
