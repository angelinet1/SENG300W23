/**
 *  @authors: Angeline Tran (301369846), Tyson Hartley (30117135), Jeongah Lee (30137463), Tyler Nguyen (30158563), Diane Doan (30052326), Nusyba Shifa (30162709)
 */

package com.autovend.software;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ReceiptPrinterObserver;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;


/*
 * Controls the logic of the receipt printer. Keeps track of if the printer runs our of paper and/or
 * while printing the receipt.
 */
public class PrintReceipt implements ReceiptPrinterObserver{
	
	private  SelfCheckoutStation scs;
	private AttendantIO attendant;
	private boolean outOfPaper = false;
	private boolean outOfInk = false;
	
	public PrintReceipt(SelfCheckoutStation selfcheckoutstation, AttendantIO attendant) {
		scs = selfcheckoutstation;
		scs.printer.register(this); //Registering this controller as a listener for the receipt printer in the scs
		
		this.attendant = attendant;
		
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
		
		attendant.informAttendant("Paper out in printer. Duplicate receipt must be printed and station needs maintenance.");
		outOfPaper = true;
		
	}

	@Override
	public void reactToOutOfInkEvent(ReceiptPrinter printer) {
		// TODO Auto-generated method stub
		
		attendant.informAttendant("Ink out in printer. Duplicate receipt must be printed and station needs maintenance.");
		outOfPaper = true;
	}

	@Override
	public void reactToPaperAddedEvent(ReceiptPrinter printer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToInkAddedEvent(ReceiptPrinter printer) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Prints the products, their price, and payment details of the bill record. 
	 * 
	 * @param billRecord: The bill record to print a receipt for.
	 * @throws OverloadException: If the extra character would spill off the end of the line.
	 * @throws EmptyException: If there is insufficient paper or ink to print the character.
	 */
	public void printBillRecord(TransactionReceipt billRecord) throws OverloadException, EmptyException{
		
		//Print each item on the bill and its price
		for(int i = 0; i < billRecord.getBillLength(); i++) {
			BarcodedProduct productToPrint = (BarcodedProduct) billRecord.getProductAt(i); //Get one product at a time from the bill record
			char[] productDescription = productToPrint.getDescription().toCharArray(); //Get the product description and store it in a char array
			
			//Print each char in the product description
			for (i = 0; i < productDescription.length; i++) {
				scs.printer.print(productDescription[i]);
				if (outOfPaper || outOfInk) { //If the printer is out of paper or ink, abort printing the receipt
					return;
				}

			}
			
			scs.printer.print('\t'); //tab
			if (outOfPaper || outOfInk) { //If the printer is out of paper or ink, abort printing the receipt
				return;
			}
			
			scs.printer.print('$'); //$ to go before printing the price
			if (outOfPaper || outOfInk) { //If the printer is out of paper or ink, abort printing the receipt
				return;
			}
			
			char[] productPrice = productToPrint.getPrice().toString().toCharArray();
			//Print each number in the price
			for (i = 0; i < productPrice.length; i++) {
				scs.printer.print(productPrice[i]);
				if (outOfPaper || outOfInk) { //If the printer is out of paper or ink, abort printing the receipt
					return;
				}

			}
			
			scs.printer.print('\n'); //Newline before printing the next product
			
		}
		
		//PRINT THE DETAILS OF PAYMENT
		
		
	}
	
	/**
	 * Cuts the receipt. Simulate the customer taking their receipt.
	 * 
	 * @throws EmptyException: If the receipt is not cut and the customer is unable to take their receipt.
	 */
	public void takeReceipt() throws EmptyException{
		scs.printer.cutPaper(); //Cut the receipt from the receipt printer
		String receipt = scs.printer.removeReceipt(); //Simulate a customer removing and taking their receipt
		
		if(receipt == null) {
			throw new EmptyException("The receipt cannot be removed because it has not been cut.");
		}
	}
	
	/**
	 * Return value of outOfPaper boolean.
	 * 
	 * @return: Boolean of if the printer is out of paper or not.
	 */
	public boolean getOutOfPaper() {
		return outOfPaper;
	}
	
	/**
	 * Return value of outOfInk boolean.
	 * 
	 * @return: Boolean of if the printer is out of ink or not.
	 */
	public boolean getOutOfInk() {
		return outOfInk;
	}
	

}
