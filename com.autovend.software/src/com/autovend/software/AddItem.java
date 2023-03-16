package com.autovend.software;

import com.autovend.Barcode;
import com.autovend.SellableUnit;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;

public abstract class AddItem {
	
	static Bill currentBill;


	public static void addItemPerUnit(Product p, double weight) {
		
		

		//Assuming it is available		
		if(currentBill == null) {
			currentBill = new Bill(p);
		} else {
			currentBill.addProduct(p);
		}
		
		currentBill.addProduct(p);
		
		// Update Expected Weight
		currentBill.augmentExpectedWeight(weight);
		
		
		
		
		
	}
	
	public static SellableUnit BarcodedProductToSellableUnit(BarcodedProduct p) {
		
		
		return null;
		
		
	}
	
	public static void waitForWeightChange() {
		// Stop the machine until a change from the electronic scale is detected
	}
}
