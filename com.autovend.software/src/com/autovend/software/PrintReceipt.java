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
		scs.printer.register(this); //Registering this controller as a listener for the receipt printer in the scs
		
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
	
	public void printBillRecord() {
		//Check if the full payment has been made and this is updated on the bill record
		
		//Print each item on the bill and its price
		
		//Print the details of payment
		
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
