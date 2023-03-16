/**
 *  @authors: Angeline Tran (301369846), Tyson Hartley (30117135), Jeongah Lee (30137463), Tyler Nguyen (30158563), Diane Doan (30052326), Nusyba Shifa (30162709)
 */

package com.autovend.software;
import java.util.Currency;
import java.util.List;

import com.autovend.devices.BillSlot;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.BillStorage;
import com.autovend.Bill;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BillDispenserObserver;

public class PayWithCash implements BillDispenserObserver{

	private BillDispenser change;

	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillsFullEvent(BillDispenser dispenser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillsEmptyEvent(BillDispenser dispenser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillAddedEvent(BillDispenser dispenser, Bill bill) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillRemovedEvent(BillDispenser dispenser, Bill bill) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillsLoadedEvent(BillDispenser dispenser, Bill... bills) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillsUnloadedEvent(BillDispenser dispenser, Bill... bills) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * Dispenses the change due to the customer
	 */
	public BillDispenser getChangeFromDispenser() {
		BillDispenser change = null; // initialize change
		change.unload(); // get list of bills unloaded 
		return change;
	}

//	public void calculateTotal() {
//		//this.inserted = getInsertedBills();
//		for(int i = 0; i < inserted.size(); i++) {
//			int updated = total - inserted; // calculate updated total
//		}
//		System.out.printf("Total due: %d", updated); // updates amount due displayed to the customer
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
