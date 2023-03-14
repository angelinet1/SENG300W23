package com.autovend.software;

import java.util.ArrayList;
import com.autovend.*;
import com.autovend.products.Product;

/**
 * @author Tyson
 *
 */
public class Bill {
	
	/**
	 * Creates a new Bill of Sale
	 */
	
	ArrayList<Product> currentBill;
	
	public Bill() {
		
		currentBill = new ArrayList<Product>();
	
	}
	
	/**
	 * Creates a new Bill of Sale with the product p in it
	 */
	
	public Bill(Product p) {
		
		currentBill = new ArrayList<Product>();
		this.currentBill.add(p);
	
	}
	
	
	/**
	 * Gets the product at index i. First product is stored at i = 1
	 */
	
	public Product getProductAt(int i) {
		return this.currentBill.get(i);
			
	}

	
	/**
	 * Returns the number of items in the bill
	 * @return int = number of items in bill
	 */
	public int getBillLength() {
		return currentBill.size();
	}
	


	public void addProduct(Product p) {
		currentBill.add(p);
		
	}
	
}
