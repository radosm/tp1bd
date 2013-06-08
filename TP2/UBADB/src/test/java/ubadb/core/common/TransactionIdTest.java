package ubadb.core.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TransactionIdTest
{
	@Test
	public void equalsTrueSameObject()
	{
		TransactionId transactionId1 = new TransactionId(1);
		assertTrue(transactionId1.equals(transactionId1));
	}
	
	@Test
	public void equalsTrueOtherObject()
	{
		TransactionId transactionId1 = new TransactionId(1);
		TransactionId transactionId2 = new TransactionId(1);
		
		assertTrue(transactionId1.equals(transactionId2));
		assertEquals(transactionId1.hashCode(),transactionId2.hashCode());
	}
	
	@Test
	public void equalsFalseNullObject()
	{
		TransactionId transactionId1 = new TransactionId(1);
		
		assertFalse(transactionId1.equals(null));
	}

	@Test
	public void equalsFalseDifferentType()
	{
		TransactionId transactionId1 = new TransactionId(1);
		
		assertFalse(transactionId1.equals("aaaaaaaaaaaa"));
	}
	
	@Test
	public void equalsFalseDifferentObject()
	{
		TransactionId transactionId1 = new TransactionId(1);
		TransactionId transactionId2 = new TransactionId(9999);
		
		assertFalse(transactionId1.equals(transactionId2));
	}
}
