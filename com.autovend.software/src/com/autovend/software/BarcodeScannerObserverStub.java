package com.autovend.software;

import com.autovend.Barcode;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BarcodeScannerObserver;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;

public class BarcodeScannerObserverStub implements BarcodeScannerObserver{
	
	 public AbstractDevice<? extends AbstractDeviceObserver> device = null;
	 public boolean barcodeScaned;

	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		this.device = device;
		
	}



	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		this.device = device;
		
	}



	
	public void reactToBarcodeScannedEvent(SelfCheckoutMachineLogic scmLogic, BarcodeScanner barcodeScanner, Barcode barcode) {
		BarcodedProduct scannedProduct = null;
		scannedProduct = getBarcodedProductFromBarcode(barcode);
		
		scmLogic.addItemPerUnit(scannedProduct, scannedProduct.getExpectedWeight());
		
	}
	
	
	
	private BarcodedProduct getBarcodedProductFromBarcode(Barcode barcode) {
		BarcodedProduct foundProduct = null;
		
		if(ProductDatabases.BARCODED_PRODUCT_DATABASE.containsKey(barcode)){
			foundProduct = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
				};
		
		return foundProduct;
	}

}
