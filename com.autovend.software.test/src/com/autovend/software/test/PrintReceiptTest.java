package com.autovend.software.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.*;
import org.junit.Before;
import org.junit.Test;

import com.autovend.devices.*;
import com.autovend.software.*;

public class PrintReceiptTest {

  // Initialize SelfCheckoutStation and PrintReceipt objects
  private SelfCheckoutStation scs;
  private PrintReceipt pr;

  @Before
  public void setUp() {
	Currency currency = Currency.getInstance("CAD");
	int[] billDom = {5,10,20};
	BigDecimal[] coinDom = {BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.10),BigDecimal.valueOf(0.25)};
    scs = new SelfCheckoutStation(currency, billDom , coinDom, 10000, 2); 
    pr = new PrintReceipt(scs);
  }
  @After
  public void tearDown() {
	  scs = null;
	  pr = null;
  }
 
  /*tests that Empty Exception is thrown when trying to take a receipt with no paper*/
  @Test
  public void testTakeReceipt_noPaper() throws EmptyException {
    pr.takeReceipt();
  }
  
  /*tests that an Empty Exception is thrown when trying to take receipt that has not been cut*/
  @Test
  public void testTakeReceipt_uncutReceipt() throws EmptyException, OverloadException {
	scs.printer.addPaper(1);
    pr.takeReceipt();
  }
  
  /*tests that a valid receipt is printed, when there is content and has been cut*/
  @Test
  public void testTakeReceipt_validReceipt() throws EmptyException, OverloadException {
   scs.printer.addInk(100);
   scs.printer.addPaper(50);
   pr.takeReceipt();
    
  }

  /*need to test printBillRecord() when implemented */ 
  /*need to test */
 
  
}