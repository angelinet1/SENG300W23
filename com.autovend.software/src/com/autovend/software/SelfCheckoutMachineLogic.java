package com.autovend.software;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.SellableUnit;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;

/**
 * @author tyson
 *
 */
public class SelfCheckoutMachineLogic{
	
	
	TransactionReciept currentBill;
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
			currentBill = new TransactionReciept(p);
		} else {
			currentBill.addProduct(p);
		}
		
		currentBill.augmentBillBalance(p.getPrice());
		
		// Update Expected Weight
		currentBill.augmentExpectedWeight(weight);
		
		this.askCustomerToPlaceItemGUI();
		
		
		}
	}
	
	


	public static BarcodedUnit getBarcodedUnitFromBarcode(Barcode barcode) {
		BarcodedProduct foundProduct = getBarcodedProductFromBarcode(barcode);
		
		BarcodedUnit bUnit = new BarcodedUnit(barcode, foundProduct.getExpectedWeight());
		
	
		return bUnit;
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
	 * 
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
	public  TransactionReciept getCurrentBill() {
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



}
