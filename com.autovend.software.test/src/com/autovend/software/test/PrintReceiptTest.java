package com.autovend.software.test;

import static org.junit.Assert.*;
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
    scs = new SelfCheckoutStation();
    pr = new PrintReceipt(scs);
  }
  
  /*tests that Empty Exception is thrown when trying to take a receipt with no paper*/
  @Test(expected = EmptyException.class)
  public void testTakeReceipt_noPaper() throws EmptyException {
    scs.printer.linesOfPaperRemaining = 0;
    pr.takeReceipt();
  }
  /*tests that an Empty Exception is thrown when trying to take a receipt with no paper*/
  @Test(expected = EmptyException.class)
  public void testTakeReceipt_uncutReceipt() throws EmptyException {
    scs.printer.linesOfPaperRemaining = 1;
    scs.printer.lastReceipt = null;
    pr.takeReceipt();
  }
  /*tests that a valid receipt is printed, when there is content and has been cut*/
  /*how-to/finished implement/ing all the contents of a receipt*/
  @Test
  public void testTakeReceipt_validReceipt() throws EmptyException {
    scs.printer.linesOfPaperRemaining = 1;
    scs.printer.lastReceipt = "Receipt contents";
    String receipt = pr.takeReceipt();
    assertEquals("Receipt contents", receipt);
  }

  /*need to test printBillRecord() when implemented */ 
  /*need to test 
  
  
}