/**
 *  @authors: Angeline Tran (301369846), Tyson Hartley (30117135), Jeongah Lee (30137463), Tyler Nguyen (30158563), Diane Doan (30052326), Nusyba Shifa (30162709)
 */

package com.autovend.software;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import com.autovend.software.SelfCheckoutMachineLogic;
import com.autovend.devices.BillSlot;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.DisabledException;
import com.autovend.devices.OverloadException;
import com.autovend.Barcode;
import com.autovend.Bill;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BillSlotObserver;
import com.autovend.products.BarcodedProduct;

public class PayWithCash {
	public BillSlot billSlot; // create bill slot
	public BillDispenser dispenser; // create bill dispenser
	public Bill bill; // create a bill
	public Barcode barcode; // create a barcode
	public BarcodedProduct item; // create a barcoded product
	public BigDecimal price; // create local variable price
	public BigDecimal total; // create local variable total
	public BigDecimal remainder; // create local variable remainder
	public BillSlotObserverStub listener_1; // create listener
	public CashIO cashIO; // create cash i/o
	public CustomerIO customerIO; // create customer i/o
	public TransactionReceipt items; // create items from the purchase
	public boolean billInsertedEvent = false;
	public boolean billEjectedEvent = false;
	
	/*
	 * Main function for pay with cash
	 */
	public void main() throws OverloadException, DisabledException{
	    item = SelfCheckoutMachineLogic.getBarcodedProductFromBarcode(barcode); // get the scanned item
    	price = item.getPrice(); // get price of item
    	total = items.augmentBillBalance(price); // get the total purchase value
    	
    	remainder = total; // initialize remaining amount to pay
    	int compare = remainder.compareTo(BigDecimal.ZERO); // local variable to store comparison
    	while(compare == 1) { // comparison returns 1 if remainder > 0
    		if(listener_1.getInsertedEvent()) { // if event is true, continue with procedure
    		    int insertedBill = bill.getValue(); // get value of the inserted bill
    		    BigDecimal updateBill = BigDecimal.valueOf(insertedBill); // convert bill to BigDecimal type
    		    remainder = total.subtract(updateBill); // reduces the remaining amount due by value of inserted bill
    		}
    		else {
    			billEjectedEvent = true; // indicate that bill was ejected
    		}
    	}
    	
    	BigDecimal num = new BigDecimal("1");
    	BigDecimal change = remainder.multiply(num.negate()); // set the change to be the remaining amount of cash (multiplied by -1 to remove the negative)
    	cashIO.setChange(change); //set change using cashI/O
    	
    	// Change will then be distributed by BillStorage
    	// Move on to Receipt Printer
	}
}
