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
public class PrintReceipt{
	
	private SelfCheckoutStation scs;
	private SelfCheckoutMachineLogic machineLogic;
	private AttendantIO attendant;
	private ReceiptPrinterObserverStub observerStub;
	
	public PrintReceipt(SelfCheckoutStation selfcheckoutstation, SelfCheckoutMachineLogic machineLogic, AttendantIO attendant) {
		this.scs = selfcheckoutstation;
		this.machineLogic = machineLogic;
		this.attendant = attendant;
		
		observerStub = new ReceiptPrinterObserverStub(attendant);
		scs.printer.register(observerStub); //Registering this controller as a listener for the receipt printer in the scs
		
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
			for (int j = 0; j < productDescription.length; j++) {
				scs.printer.print(productDescription[j]);
				if (observerStub.getOutOfPaper() || observerStub.getOutOfInk()) { //If the printer is out of paper or ink, abort printing the receipt
					machineLogic.setMachineLock(true);
					machineLogic.setReasonForLock(2);
					return;
				}

			}
			
			scs.printer.print(' '); //space
			if (observerStub.getOutOfPaper() || observerStub.getOutOfInk()) { //If the printer is out of paper or ink, abort printing the receipt
				machineLogic.setMachineLock(true);
				machineLogic.setReasonForLock(2);
				return;
			}
			
			scs.printer.print('$'); //$ to go before printing the price
			if (observerStub.getOutOfPaper() || observerStub.getOutOfInk()) { //If the printer is out of paper or ink, abort printing the receipt
				machineLogic.setMachineLock(true);
				machineLogic.setReasonForLock(2);
				return;
			}
			
			char[] productPrice = productToPrint.getPrice().toString().toCharArray();
			//Print each number in the price
			for (int p = 0; p < productPrice.length; p++) {
				scs.printer.print(productPrice[p]);
				if (observerStub.getOutOfPaper() || observerStub.getOutOfInk()) { //If the printer is out of paper or ink, abort printing the receipt
					machineLogic.setMachineLock(true);
					machineLogic.setReasonForLock(2);
					return;
				}

			}
			
			scs.printer.print('\n'); //Newline before printing the next product
			
		}
		
		//Print details of the payment
		String totalTextString = "TOTAL:$";
		char[] totalText = totalTextString.toCharArray();
		for (int u = 0; u < totalText.length; u++) {
			scs.printer.print(totalText[u]);
			if (observerStub.getOutOfPaper() || observerStub.getOutOfInk()) { //If the printer is out of paper or ink, abort printing the receipt
				machineLogic.setMachineLock(true);
				machineLogic.setReasonForLock(2);
				return;
			}
		}
		
		//char[] totalPrice = machineLogic.total.toString().toCharArray();
		
	}
	
	/**
	 * Cuts the receipt. Simulate the customer taking their receipt.
	 * 
	 */
	public String takeReceipt(){
		scs.printer.cutPaper(); //Cut the receipt from the receipt printer
		String receipt = scs.printer.removeReceipt(); //Simulate a customer removing and taking their receipt
		return receipt;
	}
	
	public ReceiptPrinterObserverStub getObserverStub() {
		return observerStub;
	}
	

}
