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

public class SelfCheckoutMachineLogic implements BarcodeScannerObserver,ElectronicScaleObserver {
	
	
	static TransactionReciept currentBill;
	public  boolean machineLocked = false;
	
	public SelfCheckoutMachineLogic(SelfCheckoutStation scStation) {
		
		scStation.scale.register(this);
		
		this.machineLocked = true;
	}


	
	public  void addItemPerUnit(Product p, double weight) {
		
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
		
		}
	}
	
	

	private BarcodedProduct getBarcodedProductFromBarcode(Barcode barcode) {
		BarcodedProduct foundProduct = null;
		
		if(ProductDatabases.BARCODED_PRODUCT_DATABASE.containsKey(barcode)){
			foundProduct = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
				};
		
		return foundProduct;
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
	
	public static void setMachineLock(boolean newState) {
		
	}
	
	public static TransactionReciept getCurrentBill() {
		return currentBill;
	}



	@Override
	public void reactToWeightChangedEvent(ElectronicScale scale, double weightInGrams) {
		this.machineLocked = false;
		
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
