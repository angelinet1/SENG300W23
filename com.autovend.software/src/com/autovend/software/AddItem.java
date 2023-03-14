package com.autovend.software;

import com.autovend.Barcode;
import com.autovend.SellableUnit;
import com.autovend.products.Product;

public abstract class AddItem {
	
	static Bill currentBill;


	public static void addItem(Product p) {
		
		//Check if it available(Not needed for iteration 1)
		
		
		
		//Assuming it is available
		
		if(currentBill == null) {
			currentBill = new Bill(p);
		} else {
			currentBill.addProduct(p);
		}
		
		// Update Expected Weight
		

		
	}
	
	public static SellableUnit ProductToSellableUnit(Product p) {
		
		return null;
	}
	
	public static void waitForWeightChange() {
		// Stop the machine until a change from the electronic scale is detected
	}
}
