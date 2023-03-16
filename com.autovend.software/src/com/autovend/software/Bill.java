package com.autovend.software;

import java.math.BigDecimal;
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
	BigDecimal billBalance;




	double billExpectedWeight;
	
	public Bill() {
		
		currentBill = new ArrayList<>();
	
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
	
	public BigDecimal getBillBalance() {
		return billBalance;
	}

	public void setBillBalance(BigDecimal billBalance) {


		this.billBalance = billBalance;
	}
	
	/**
	 * Used to increase or decrease Balance by a Big Decimal
	 * 
	 * @param BigDecimal addend
	 */
	public void augmentBillBalance(BigDecimal addend) {
		this.billBalance.add(addend);
	}

	public double getBillExpectedWeight() {
		return billExpectedWeight;
	}

	
	
	public void setBillExpectedWeight(double billExpectedWeight) {
		this.billExpectedWeight = billExpectedWeight;
	}
	
	/**
	 * Used to increase or decrease the expected weight by a double
	 * 
	 * @param BigDecimal double
	 */
	public void augmentExpectedWeight(double addend) {
		this.billExpectedWeight += addend;
	}
	
}
