package com.autovend.software;

import com.autovend.Barcode;
import com.autovend.SellableUnit;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;

public abstract class AddItem {
	
	static TransactionReciept currentBill;
	public static boolean machineLocked = false;


	public static void addItemPerUnit(Product p, double weight) {
		
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
	
	public static void setMachineLock(boolean newState) {
		
	}
	
	public static TransactionReciept getCurrentBill() {
		return currentBill;
	}
	
}
