package com.autovend.software;

import com.autovend.Barcode;
import com.autovend.SellableUnit;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;

public abstract class AddItem {
	
	static Bill currentBill;
	public static boolean machineLocked = false;


	public static void addItemPerUnit(Product p, double weight) {
		
		if(!machineLocked) {
		
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
		
		
		
	}
	
	public static void setMachineLock(boolean newState) {
		machineLocked = newState;
		
		
	}
	
	/**
	 * A method to be called to simulate a user placing an item on scale when machine prompts it
	 */
	public static void userPlaceItemPrompted() {
			
		if(machineLocked) {
			machineLocked = false;
		}
		
	};
	
	
}
