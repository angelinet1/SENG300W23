package com.autovend.software.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.*;
import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.Numeral;
import com.autovend.devices.*;
import com.autovend.products.BarcodedProduct;
import com.autovend.software.*;

public class PrintReceiptTest {

// Initialize SelfCheckoutStation and PrintReceipt objects
  private SelfCheckoutStation scs;
  private SelfCheckoutMachineLogic machineLogic;
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
    
    machineLogic.total = BigDecimal.valueOf(17.58);
    
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
	  
	  String expected = "Milk $10.59\nBread $6.99\nTOTAL:$17.58";
	  
	  assertEquals(expected, actualReceipt);
  }
 
  
  /*
   * When ink runs out in the middle of printing the product name:
   * 1. The printing is aborted
   * 2. Attendant is notified
   */
  @Test
  public void printBillRecord_WithoutInkDuringProductName_PrintingAborted() throws OverloadException, EmptyException{
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
  
  /*
   * When ink runs out right after printing a $:
   * 1. The printing is aborted
   * 2. Attendant is notified
   */
  @Test
  public void printBillRecord_WithoutInkAfterPrinting$_PrintingAborted() throws OverloadException, EmptyException{
	  scs.printer.addPaper(100);
	  scs.printer.addInk(5);
	  
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
  
  /*
   * When ink runs out right after printing the price of a product:
   * 1. The printing is aborted
   * 2. Attendant is notified
   */
  @Test
  public void printBillRecord_WithoutInkAWhilePrintingPrice_PrintingAborted() throws OverloadException, EmptyException{
	  scs.printer.addPaper(100);
	  scs.printer.addInk(9);
	  
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
  
  
  /*
   * When paper runs out when going to nextline for the next product:
   * 1. The printing is aborted
   * 2. Attendant is notified
   */
  @Test
  public void printBillRecord_WithoutPaperNextLine_PrintingAborted() throws OverloadException, EmptyException{
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
   * When ink runs out in the middle of printing TOTAL:$ header:
   * 1. The printing is aborted
   * 2. Attendant is notified
   */
  @Test
  public void printBillRecord_WithoutInkWhileTotalHeader_PrintingAborted() throws OverloadException, EmptyException{
	  scs.printer.addPaper(100);
	  scs.printer.addInk(22);
	  
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
  
  /*
   * When ink runs out in the middle of printing the total price:
   * 1. The printing is aborted
   * 2. Attendant is notified
   */
  @Test
  public void printBillRecord_WithoutInkWhileTotalPrice_PrintingAborted() throws OverloadException, EmptyException{
	  scs.printer.addPaper(100);
	  scs.printer.addInk(28);
	  
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
  
  
  /*
   * Testing the cutting and taking of an empty receipt.
   */
  @Test
  public void takeReceipt_EmptyReceipt_TakeEmptyReceipt() throws EmptyException, OverloadException {
	  
	scs.printer.addPaper(1);
	String takenReceipt = machineLogic.printReceipt.takeReceipt();
	String expected = "";
	assertEquals(expected, takenReceipt);
	
  }
  
  /*
   * Testing if there is enough ink and paper, signalToPrintReceipt sends a session complete message
   * to the customer display and nulls the bill for the next customer.
   */
  @Test
  public void signalToPrintReceipt_WithPaperInk_PrintReceiptAndCleanUp() throws OverloadException, EmptyException{
	  scs.printer.addPaper(100);
	  scs.printer.addInk(100);
	  machineLogic.total = BigDecimal.valueOf(17.58);
	  machineLogic.signalToPrintReceipt(billRecord);
	  
	  //Checking if the customer display receives the session complete message
	  assertEquals("Your session is complete. Thank you for shopping with us.", machineLogic.customerDisplay.getMostRecentMessage());
	  assertEquals(null, machineLogic.getCurrentBill()); //Checking if the bill is nulled for the next customer
  }
  
  /*
   * Testing if the printer runs out of paper, that there is no cut receipt for the customer to take because printing was
   * aborted, and no session complete message is sent to the customer display
   */
  @Test
  public void signalToPrintReceipt_WithoutPaper_NoMessageToDisplay()throws OverloadException, EmptyException{
	  scs.printer.addPaper(1); 
	  scs.printer.addInk(100);
	  machineLogic.total = BigDecimal.valueOf(17.58);
	  machineLogic.signalToPrintReceipt(billRecord);
	  
	  assertEquals(null, scs.printer.removeReceipt()); //Checking that there is no cut receipt for the customer to take
	  assertEquals(null, machineLogic.customerDisplay.getMostRecentMessage()); 
  }
 
  /*
   * Testing if the printer runs out of ink, that there is no cut receipt for the customer to take because printing was
   * aborted, and no session complete message is sent to the customer display
   */
  @Test
  public void signalToPrintReceipt_WithoutInk_NoMessageToDisplay()throws OverloadException, EmptyException{
	  scs.printer.addPaper(100); 
	  scs.printer.addInk(1);
	  machineLogic.total = BigDecimal.valueOf(17.58);
	  machineLogic.signalToPrintReceipt(billRecord);
	  
	  assertEquals(null, scs.printer.removeReceipt()); //Checking that there is no cut receipt for the customer to take
	  assertEquals(null, machineLogic.customerDisplay.getMostRecentMessage()); 
  }
 
  /*
   * Testing if the printer runs out of both ink and paper, that there is no cut receipt for the customer to take because printing was
   * aborted, and no session complete message is sent to the customer display
   */
  @Test
  public void signalToPrintReceipt_WithoutPaperInk_NoMessageToDisplay()throws OverloadException, EmptyException{
	  scs.printer.addPaper(1); 
	  scs.printer.addInk(1);
	  machineLogic.total = BigDecimal.valueOf(17.58);
	  machineLogic.signalToPrintReceipt(billRecord);
	  
	  assertEquals(null, scs.printer.removeReceipt()); //Checking that there is no cut receipt for the customer to take
	  assertEquals(null, machineLogic.customerDisplay.getMostRecentMessage()); 
  }
  
}