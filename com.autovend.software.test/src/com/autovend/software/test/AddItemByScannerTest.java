package com.autovend.software.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.*;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.software.*;

public class AddItemByScannerTest {
	
	static Barcode milk;
	static Barcode bread;
	
	static BarcodedProduct milk_product;
	static BarcodedProduct bread_product;
	
	/**
	 * Set up before each test.
	 */
	@Before
	public void setUp() {
		
		milk = new Barcode(new Numeral[] {Numeral.zero});
		bread = new Barcode(new Numeral[] {Numeral.one});
		
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(milk, milk_product);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(bread, bread_product);
	}

	/**
	 * Tear down after each test.
	 */
	@After
	public void tearDown() {
		// not sure what to put here
	}

	/**
	 * Tests when an scanned item is found in the database.
	 */
	@Test 
	public void getProduct() {
		SelfCheckoutMachineLogic.getBarcodedProductFromBarcode(milk);
	}
	
	/**
	 * Tests when an scanned item is null.
	 */
	@Test 
	public void getProductNull() {
		SelfCheckoutMachineLogic.getBarcodedProductFromBarcode(null);
	}
	

}
