package com.autovend.software;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.EmptyException;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ReceiptPrinterObserver;

public class PrintReceipt implements ReceiptPrinterObserver{
	
	private  SelfCheckoutStation scs;
	
	public PrintReceipt(SelfCheckoutStation selfcheckoutstation) {
		scs = selfcheckoutstation;
		
	}

	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToOutOfPaperEvent(ReceiptPrinter printer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToOutOfInkEvent(ReceiptPrinter printer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToPaperAddedEvent(ReceiptPrinter printer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToInkAddedEvent(ReceiptPrinter printer) {
		// TODO Auto-generated method stub
		
	}
	
	public void takeReceipt() throws EmptyException{
		scs.printer.cutPaper();
		String receipt = scs.printer.removeReceipt();
		
		if(receipt == null) {
			throw new EmptyException("The receipt cannot be removed because it has not been cut.");
		}
		
		//Signals to customer I/O that the session is complete
		//How do we signal to customer I/O??
	}
	

}
