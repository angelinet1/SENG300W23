package com.autovend.software;

import com.autovend.Barcode;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BarcodeScannerObserver;
import com.autovend.devices.observers.ElectronicScaleObserver;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;

public class SelfCheckoutMachineLogic implements BarcodeScannerObserver, ElectronicScaleObserver {
	
	
	TransactionReciept currentBill;
	public  boolean machineLocked = false;
	
	
	
	/**Codes for reasons the Machine is Locked
	 * -1: No Reason
	 * 0: Not Locked:
	 * 1: Unexpected Weight
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
		
		scStation.scale.register(this);
		
		this.setMachineLock(false);
	}

	
	public void addItemPerUnit(Product p, double weight) {
		
		if(!machineLocked) {
		
		//Assuming it is available		
		if(currentBill == null) {
			currentBill = new TransactionReciept(p);
		} else {
			currentBill.addProduct(p);
		}
		
		currentBill.addProduct(p);
		
		// Update Expected Weight
		currentBill.augmentExpectedWeight(weight);
		
		this.askCustomerToPlaceItemGUI();
		
		
		}
	}
	
	

	private BarcodedProduct getBarcodedProductFromBarcode(Barcode barcode) {
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
	
	
	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		this.machineLocked = false; 
		
	}



	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		this.machineLocked = true;
		
	}



	@Override
	public void reactToBarcodeScannedEvent(BarcodeScanner barcodeScanner, Barcode barcode) {
		BarcodedProduct scannedProduct = getBarcodedProductFromBarcode(barcode);
		
		if(scannedProduct != null) {
		addItemPerUnit(scannedProduct, scannedProduct.getExpectedWeight());
		}
		
	}
	
	public void setMachineLock(boolean newState) {
		
		if(newState == false) {
			this.setReasonForLock(0);
		}
		this.machineLocked = newState;
	}
	
	public  TransactionReciept getCurrentBill() {
		return currentBill;
	}


	@Override
	public void reactToWeightChangedEvent(ElectronicScale scale, double weightInGrams) {
		if(weightInGrams == currentBill.billExpectedWeight) {
			if(this.getReasonForLock() == 1) {
				this.setMachineLock(false);
				
			} 
				
		} else {
			this.setMachineLock(true);
			this.setReasonForLock(1);
			
		}
		
	}


	@Override
	public void reactToOverloadEvent(ElectronicScale scale) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void reactToOutOfOverloadEvent(ElectronicScale scale) {
		// TODO Auto-generated method stub
		
	}


}
