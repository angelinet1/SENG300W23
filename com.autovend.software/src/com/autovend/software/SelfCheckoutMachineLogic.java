/*
 *  @author: Angeline Tran (301369846),
 *  @author: Tyson Hartley (30117135), 
 *  @author: Jeongah Lee (30137463), 
 *  @author: Tyler Nguyen (30158563), 
 *  @author: Diane Doan (30052326), 
 *  @author: Nusyba Shifa (30162709)
 */

package com.autovend.software;

import java.math.BigDecimal;
import java.util.Currency;

import com.autovend.Barcode;
import com.autovend.devices.BillSlot;
import com.autovend.devices.BillDispenser;
import com.autovend.Bill;
import com.autovend.software.BillSlotObserverStub;
import com.autovend.software.BillValidatorObserverStub;
import com.autovend.products.BarcodedProduct;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.external.ProductDatabases;
import com.autovend.products.Product;

public class SelfCheckoutMachineLogic{
	
	public BillSlot billSlot; // create bill slot
	public BillDispenser dispenser; // create bill dispenser
	public Bill bill; // create a bill
	public Barcode barcode; // create a barcode
	public BarcodedProduct item; // create a barcoded product
	public BigDecimal price; // create local variable price
	public BigDecimal total; // create local variable total
	public BigDecimal remainder; // create local variable remainder
	public BigDecimal change; // create local variable change
	public BillSlotObserverStub listener_1; // create listener
	public CashIO cashIO; // create cash i/o
	public CustomerIO customerIO; // create customer i/o
	public TransactionReceipt items; // create items from the purchase
	public boolean billInsertedEvent = false;
	public boolean billValidEvent = false;
	
	TransactionReceipt currentBill;
	public  boolean machineLocked = false;
	
	public ElectronicScaleObserverStub esObserver = new ElectronicScaleObserverStub(this);
	public BarcodeScannerObserverStub bsObserver = new BarcodeScannerObserverStub(this);
	
	
	
	/**Codes for reasons the Machine is Locked
	 * -1: No Reason
	 * 0: Not Locked:
	 * 1: Locked until A change in scale weight
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
	 * Constructor for Adding observers to pieces of hardware
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
		
		this.setMachineLock(false);
	}

	
	
	
	/**
	 * 
	 * 	Adds an item that is sold by unit to the currentBill, updating the weight and total balance
	 * @param p: The product being added
	 * @param weight: the weight of p in grams
	 */
	public void addItemPerUnit(Product p, double weight) {
		
		if(!machineLocked) {
		
		//Assuming it is available		
		if(currentBill == null) {
			currentBill = new TransactionReceipt(p);
		} else {
			currentBill.addProduct(p);
		}
		
		currentBill.augmentBillBalance(p.getPrice());
		
		// Update Expected Weight
		currentBill.augmentExpectedWeight(weight);
		
		this.askCustomerToPlaceItemGUI();
		
		
		}
	}
	


	/**
	 * 	Takes a barecode and returns a barcoded product if it is a valid barcode
	 * Otherwise returns null
	 * @param barcode: The barcode of the Barcoded Product being looked for
	 * @return If the barcode corossponds to one in the database, return that product otherwise return null
	 */
	public static BarcodedProduct getBarcodedProductFromBarcode(Barcode barcode) {
		BarcodedProduct foundProduct = null;
		
		if(ProductDatabases.BARCODED_PRODUCT_DATABASE.containsKey(barcode)){
			foundProduct = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
				};
		
		return foundProduct;
	}
	


	/**
	 * Tells the machine to wait until the customer chnages the weight of the scale
	 */
	private void askCustomerToPlaceItemGUI() {
		
		//Prompt GUI to tell customer
		
		this.setMachineLock(true);
		this.setReasonForLock(1);
		
	}
	
	


	
	/**
	 * Sets the machines lock state to newState. If the machine is unlocked set reason for lock to 0
	 * @param newState
	 */
	public void setMachineLock(boolean newState) {
		
		if(newState == false) {
			this.setReasonForLock(0);
		}
		this.machineLocked = newState;
	}
	
	
	/**
	 * 
	 * @return Returns a reference to the current bill the machine is processing
	 */
	public  TransactionReceipt getCurrentBill() {
		return currentBill;
	}

	/**
	 * This function is called whenever there is a weightChanged detected by the scale.
	 * If the machine is currently being locked becuase it expects a change it weight
	 * It checks if it is what the billExpects the weight to be otherwise it will lock the machine
	 * @param totalWeightInGrams: the total weight of the scale in grams.
	 */
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

	
	/*
	 * Main function for pay with cash
	 */
	public void payWithCash(){
	    item = SelfCheckoutMachineLogic.getBarcodedProductFromBarcode(barcode); // get the scanned item
    	price = item.getPrice(); // get price of item
    	total = items.augmentBillBalance(price); // get the total purchase value
    	
    	remainder = total; // initialize remaining amount to pay
    	int compare = remainder.compareTo(BigDecimal.ZERO); // local variable to store comparison
    	while(compare == 1) { // comparison returns 1 if remainder > 0
    		if(listener_1.getInsertedEvent()) { // if event is true, continue with procedure
    			if(billValidEvent){
    				int insertedBill = bill.getValue(); // get value of the inserted bill
        		    BigDecimal updateBill = BigDecimal.valueOf(insertedBill); // convert bill to BigDecimal type
        		    remainder = total.subtract(updateBill); // reduces the remaining amount due by value of inserted bill
        		    customerIO.setAmount(remainder); // update Customer IO with amount
    			} else {
    				// Prompt again for another bill because last one was invalid
    				return;
    			}

    		}
    		else {
    			// Prompt for bill because no bill was inserted
    			return;
    		}
    		// Reset events at end of the loop
			billValidEvent = false;
			billInsertedEvent = false;
    	}
    	
    	change = remainder.abs(); // set the change to be an absolute value
    	cashIO.setChange(change); //set change using cashI/O
    	
    	// Change will then be distributed by BillStorage
    	// Move on to Receipt Printer
	}

}
