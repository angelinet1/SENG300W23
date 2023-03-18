/**
 *  @authors: Angeline Tran (301369846), Tyson Hartley (30117135), Jeongah Lee (30137463), Tyler Nguyen (30158563), Diane Doan (30052326), Nusyba Shifa (30162709)
 */

package com.autovend.software;

import com.autovend.Barcode;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;

public class SelfCheckoutMachineLogic{
	
	
	TransactionReceipt currentBill;
	public  boolean machineLocked = false;
	
	public ElectronicScaleObserverStub esObserver = new ElectronicScaleObserverStub(this);
	public BarcodeScannerObserverStub bsObserver = new BarcodeScannerObserverStub(this);
	
	public PrintReceipt printReceipt; //This is the controller for printing the receipt
	public AttendantIO attendant = new AttendantIO(); //Creating an attendantIO that will receive and store calls to attendant
	public CustomerDisplayIO customerDisplay = new CustomerDisplayIO(); //Creating a display where messages to customers can go
	
	
	
	/**Codes for reasons the Machine is Locked
	 * -1: No Reason
	 * 0: Not Locked:
	 * 1: Locked until A change in scale weight
	 * 2: Locked until printer out of paper and/or ink is handled
	 * ...
	 * Please add any lock codes used, and why the machine is locked
	 */
	private int reasonForLock;

	private int[] listOfLockCodes;
	private int numberOfLockCodes = 3;
	
	public int getReasonForLock() {
		return reasonForLock;
	}


	/**
	 * Set the reason the machine is being locked
	 * @param reasonForLock: Must be a value stored in listOfLockCodes 
	 * @return True: If reason was updated, false otherwise
	 */
	public boolean setReasonForLock(int reasonForLock) {
		for(int i = 0; i < numberOfLockCodes; i++) {
			if(reasonForLock == listOfLockCodes[i]) {
				this.reasonForLock = reasonForLock;
				return true;
			}
		}
		return false;
		
	}
	/**
	 * Constuctor for Adding observers to pieces of hardware
	 */
	public SelfCheckoutMachineLogic(SelfCheckoutStation scStation) {
		listOfLockCodes = new int[numberOfLockCodes];
		for(int i = 0; i < this.numberOfLockCodes; i++) {
			listOfLockCodes[i] = i-1;
		}
		
		scStation.scale.register(esObserver);
		scStation.scale.disable();
		scStation.scale.enable();
		
		scStation.handheldScanner.register(bsObserver);
		scStation.handheldScanner.disable();
		scStation.handheldScanner.enable();
		
		printReceipt = new PrintReceipt(scStation, this, attendant);
		
		this.setMachineLock(false);
	}

	
	public void addItemPerUnit(Product p, double weight) {
		
		if(!machineLocked) {
		
		//Assuming it is available		
		if(currentBill == null) {
			currentBill = new TransactionReceipt(p);
		} else {
			currentBill.addProduct(p);
		}
		
		currentBill.addProduct(p);
		
		// Update Expected Weight
		currentBill.augmentExpectedWeight(weight);
		
		this.askCustomerToPlaceItemGUI();
		
		
		}
	}
	
	






	public static BarcodedProduct getBarcodedProductFromBarcode(Barcode barcode) {
		BarcodedProduct foundProduct = null;
		
		if(ProductDatabases.BARCODED_PRODUCT_DATABASE.containsKey(barcode)){
			foundProduct = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
				};
		
		return foundProduct;
}
	


	private void askCustomerToPlaceItemGUI() {
		
		//Prompt GUI to tell customer
		
		this.setMachineLock(true);
		this.setReasonForLock(1);
		
	}
	
	


	
	public void setMachineLock(boolean newState) {
		
		if(newState == false) {
			this.setReasonForLock(0);
		}
		this.machineLocked = newState;
	}
	
	public  TransactionReceipt getCurrentBill() {
		return currentBill;
	}

	public void weightChanged(double totalWeightInGrams) {
		switch(this.reasonForLock) {
		
		case 1: if(totalWeightInGrams == this.currentBill.billExpectedWeight) {
			this.setMachineLock(false);
			this.setReasonForLock(0);
			
		}
			break;
		default: this.setMachineLock(true);
				this.setReasonForLock(1);
				break;
				
		}
		
	}
	
	/**
	 * Prints the receipt when signal is received that the customer has paid and the bill record is 
	 * updated with the payment details.
	 * 
	 * @param billRecord: Current bill that is printed
	 * @throws OverloadException: If the extra character would spill off the end of the line.
	 * @throws EmptyException: If there is insufficient paper or ink to print the character or
	 * 			the receipt has not been cut so unable for the customer to take it.
	 */
	public void signalToPrintReceipt(TransactionReceipt billRecord) throws OverloadException, EmptyException{
		//Check that the payment in full has been received 
		//Check that the bill record is updated with he details of payment
		
		printReceipt.printBillRecord(billRecord);
		//If the printer ran out of paper and/or ink while printing the receipt, printBillRecord() will return instead of continuing to print the receipt
		//Check if printer ran out of paper and/or ink while printing the receipt
		if(printReceipt.getObserverStub().getOutOfPaper() || printReceipt.getObserverStub().getOutOfInk()) {
			return;
		}
		
		printReceipt.takeReceipt();
		customerDisplay.informCustomer("Your session is complete. Thank you for shopping with us.");
		this.currentBill = null; //Null the current bill since the customer's session is over
	}



}
