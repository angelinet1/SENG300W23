/**
 *  @authors: Angeline Tran (301369846), Tyson Hartley (30117135), Jeongah Lee (30137463), Tyler Nguyen (30158563), Diane Doan (30052326), Nusyba Shifa (30162709)
 */

package com.autovend.software;

/*
 * Simulates 
 */
public class CustomerDisplayIO {
	
	private String mostRecentMessageToCustomer;
	
	public void informCustomer(String message) {
		mostRecentMessageToCustomer = message;
	}
	
	public String getMostRecentMessage() {
		return mostRecentMessageToCustomer;
	}

}
