/**
 *  @authors: Angeline Tran (301369846), Tyson Hartley (30117135), Jeongah Lee (30137463), Tyler Nguyen (30158563), Diane Doan (30052326), Nusyba Shifa (30162709)
 */

/*
 * Observer stub for BillSlotObserver
 */

package com.autovend.software;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BillSlot;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BillSlotObserver;

public class BillSlotObserverStub implements BillSlotObserver{

	public BillSlot billSlot; 
	
	public boolean billInsertedEvent = false;
    public boolean billEjectedEvent = false;
    public boolean billRemovedEvent = false;
    public AbstractDevice<? extends AbstractDeviceObserver> device = null;
	
	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		this.device = device;
		
	}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		this.device = device;
		
	}

	@Override
	public void reactToBillInsertedEvent(BillSlot slot) {
		billInsertedEvent = true;
		
	}

	@Override
	public void reactToBillEjectedEvent(BillSlot slot) {
		billEjectedEvent = true;
		
	}

	@Override
	public void reactToBillRemovedEvent(BillSlot slot) {
		billRemovedEvent = true;
		
	}
	
	public boolean getInsertedEvent() {
		return billInsertedEvent;
	}
	
	public void waitForBill() {
		this.billInsertedEvent = false;
	}

}
