package com.autovend.software;

import com.autovend.Barcode;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BarcodeScannerObserver;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;

public class AddItemByScanning extends AddItem implements BarcodeScannerObserver{

	
	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		
		
	}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBarcodeScannedEvent(BarcodeScanner barcodeScanner, Barcode barcode) {
		
		BarcodedProduct scannedProduct = getBarcodedProductFromBarcode(barcode);
		
		if(scannedProduct != null) {
		addItemPerUnit(scannedProduct, scannedProduct.getExpectedWeight());
		}
		
	}

	private BarcodedProduct getBarcodedProductFromBarcode(Barcode barcode) {
		BarcodedProduct foundProduct = null;
		
		if(ProductDatabases.BARCODED_PRODUCT_DATABASE.containsKey(barcode)){
			foundProduct = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
				};
		
		return foundProduct;
	}



	

	
	
}
