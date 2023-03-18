/*
 *  @authors: Angeline Tran (301369846), Tyson Hartley (30117135), Jeongah Lee (30137463), Tyler Nguyen (30158563), Diane Doan (30052326), Nusyba Shifa (30162709)
 */

package com.autovend.software.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.*;
import com.autovend.devices.*;
import com.autovend.external.ProductDatabases;
import com.autovend.products.*;
import com.autovend.software.*;

public class AddItemByScannerTest {
	private SelfCheckoutStation station;
	private SelfCheckoutMachineLogic machineLogic;
	
	static Barcode milk_barcode;
	static Barcode bread_barcode;
	
	static BarcodedProduct milk_product;
	static BarcodedProduct bread_product;
	
	static Product milk;
	static Product bread;
	
	/**
	 * Set up before each test.
	 */
	@Before
	public void setUp() {
		milk_barcode = new Barcode(new Numeral[] {Numeral.zero});
		bread_barcode = new Barcode(new Numeral[] {Numeral.one});
		
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(milk_barcode, milk_product);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(bread_barcode, bread_product);
		
		Currency currency = Currency.getInstance("CAD");
		int[] billDenominations = {5, 10, 20};
		BigDecimal[] coinDenominations = {BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10),BigDecimal.valueOf(0.25)};
		station = new SelfCheckoutStation(currency, billDenominations, coinDenominations, 10000, 5);
		machineLogic = new SelfCheckoutMachineLogic(station);
		
	}

	/**
	 * Tear down after each test.
	 */
	@After
	public void tearDown() {
		station = null;
		machineLogic = null;
	}

	/**
	 * Tests when a barcode is scanned to get the product.
	 */
	@Test 
	public void getProduct() {
		SelfCheckoutMachineLogic.getBarcodedProductFromBarcode(milk_barcode);
		SelfCheckoutMachineLogic.getBarcodedProductFromBarcode(bread_barcode);
	}
	
	/**
	 * Tests when a scanned barcode is null.
	 */
	@Test 
	public void getProductNull() {
		SelfCheckoutMachineLogic.getBarcodedProductFromBarcode(null);
	}
	
	/**
	 * Tests when an scanned item is added.
	 */
	@Test 
	public void addItem() {
		machineLogic.addItemPerUnit(milk, 20);
	}
	
	/**
	 * Tests when an scanned item is added but machine is locked.
	 */
	@Test 
	public void addItemMachineLocked() {
		machineLogic.setMachineLock(true);
		machineLogic.addItemPerUnit(milk, 20);
	}
	
	/**
	 * Tests when an scanned item is added but the current bill is null.
	 * 85.7% coverage
	 */
	@Test 
	public void addItemBillNull() {
		assertEquals(machineLogic.getCurrentBill(), null);
		machineLogic.addItemPerUnit(milk, 20);
	}
}
