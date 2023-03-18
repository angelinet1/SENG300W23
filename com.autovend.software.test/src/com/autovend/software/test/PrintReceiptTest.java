package com.autovend.software.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;

import org.junit.*;
import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.Numeral;
import com.autovend.devices.*;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;
import com.autovend.software.*;

public class PrintReceiptTest {

// Initialize SelfCheckoutStation and PrintReceipt objects
  private SelfCheckoutStation scs;
  private SelfCheckoutMachineLogic machineLogic;
  //private PrintReceipt pr;
  private AttendantIO attendant1;
  private AttendantIO attendant2;
  private TransactionReceipt billRecord;
  private ReceiptPrinterObserverStub observerStub1;
  private ReceiptPrinterObserverStub observerStub2;

  @Before
  public void setUp() {
	Currency currency = Currency.getInstance("CAD");
	int[] billDom = {5,10,20};
	BigDecimal[] coinDom = {BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10),BigDecimal.valueOf(0.25)};
    scs = new SelfCheckoutStation(currency, billDom , coinDom, 10000, 2); 
    machineLogic = new SelfCheckoutMachineLogic(scs);
    
    Barcode milk_barcode = new Barcode(new Numeral[] {Numeral.zero});
    BarcodedProduct milk_product = new BarcodedProduct(milk_barcode, "Milk", BigDecimal.valueOf(10.59), 5);
    Barcode bread_barcode = new Barcode(new Numeral[] {Numeral.one});
    BarcodedProduct bread_product = new BarcodedProduct(bread_barcode, "Bread", BigDecimal.valueOf(6.99), 3);
    
    billRecord = new TransactionReceipt();
    billRecord.addProduct(milk_product);
    billRecord.addProduct(bread_product);
    
    attendant1 = new AttendantIO();
    attendant2 = new AttendantIO();

    observerStub1 = new ReceiptPrinterObserverStub(attendant1);
    observerStub2 = new ReceiptPrinterObserverStub(attendant2);
 
  }
  
  @After
  public void tearDown() {
	  scs = null;
	  machineLogic = null;
	  billRecord = null;
	  attendant1 = null;
	  attendant2 = null;
  }
  
  /*
   * Testing printing bill with 2 products with enough ink and paper.
   */
  @Test
  public void printBillRecord_WithPaperInk_ProductsOnBillPrinted() throws OverloadException, EmptyException{
	  scs.printer.addPaper(100);
	  scs.printer.addInk(100);
	  machineLogic.printReceipt.printBillRecord(billRecord);
	  scs.printer.cutPaper();
	  String actualReceipt = scs.printer.removeReceipt();
	  
	  String expected = "Milk $10.59\nBread $6.99\n";
	  
	  assertEquals(expected, actualReceipt);
  }
 
  /*
   * When paper runs out in the middle of printing a receipt:
   * 1. The printing is aborted
   * 2. Attendant is notified
   */
  @Test
  public void printBillRecord_WithoutPaper_PrintingAborted() throws OverloadException, EmptyException{
	  scs.printer.addPaper(1);
	  scs.printer.addInk(100);
	  
	  scs.printer.register(observerStub1); //stub1 is registered
	  scs.printer.register(observerStub2);
	  scs.printer.deregister(observerStub2); //stub2 is de-registered
	  
	  machineLogic.printReceipt.printBillRecord(billRecord); //When trying to print product 2 on a new line, paper will run out

	  Boolean outOfPaperAnnouncedToObserver1 = observerStub1.getOutOfPaper();
	  Boolean outOfPaperAnnouncedToObserver2 = observerStub2.getOutOfPaper();
	  
	  assertEquals(true,outOfPaperAnnouncedToObserver1); //stub1 is notified of out of paper event
	  assertEquals(false,outOfPaperAnnouncedToObserver2); //stub2 is not registered so it is not notified of out of paper event
	  
	  //stub1 sends a message to it's attendant about paper running out
	  assertEquals("Paper out in printer. Duplicate receipt must be printed and station needs maintenance.", attendant1.getMostRecentMessage());
	  assertEquals(null, attendant2.getMostRecentMessage());
	  
  }
  
  /*
   * When paper runs out in the middle of ink a receipt:
   * 1. The printing is aborted
   * 2. Attendant is notified
   */
  @Test
  public void printBillRecord_WithoutInk_PrintingAborted() throws OverloadException, EmptyException{
	  scs.printer.addPaper(100);
	  scs.printer.addInk(3);
	  
	  scs.printer.register(observerStub1); //stub1 is registered
	  scs.printer.register(observerStub2);
	  scs.printer.deregister(observerStub2); //stub2 is de-registered
	  
	  machineLogic.printReceipt.printBillRecord(billRecord); //When trying to print product 2 on a new line, ink will run out

	  Boolean outOfPaperAnnouncedToObserver1 = observerStub1.getOutOfInk();
	  Boolean outOfPaperAnnouncedToObserver2 = observerStub2.getOutOfInk();
	  
	  assertEquals(true,outOfPaperAnnouncedToObserver1); //stub1 is notified of out of ink event
	  assertEquals(false,outOfPaperAnnouncedToObserver2); //stub2 is not registered so it is not notified of out of ink event
	  
	  //stub1 sends a message to it's attendant about ink running out
	  assertEquals("Ink out in printer. Duplicate receipt must be printed and station needs maintenance.", attendant1.getMostRecentMessage());
	  assertEquals(null, attendant2.getMostRecentMessage());
	  
  }
  
  /*tests that Empty Exception is thrown when trying to take a receipt with no paper*/
  @Test
  public void testTakeReceipt_noPaper() throws EmptyException {
    machineLogic.printReceipt.takeReceipt();
  }
  
  /*tests that an Empty Exception is thrown when trying to take receipt that has not been cut*/
  @Test
  public void testTakeReceipt_uncutReceipt() throws EmptyException, OverloadException {
	  
	scs.printer.addPaper(1);
	String takenReceipt = machineLogic.printReceipt.takeReceipt();
	String expected = "";
	assertEquals(expected, takenReceipt);
	
  }
  
  /*tests that a valid receipt is printed, when there is content and has been cut*/
  @Test
  public void testTakeReceipt_validReceipt() throws EmptyException, OverloadException {
   scs.printer.addInk(100);
   scs.printer.addPaper(50);
   machineLogic.printReceipt.takeReceipt();
    
  }

  /*need to test printBillRecord() when implemented */ 
  /*need to test */
 
  
 
  
}