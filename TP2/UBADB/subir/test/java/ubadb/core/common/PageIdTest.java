package ubadb.core.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ubadb.core.common.PageId;
import ubadb.core.common.TableId;

public class PageIdTest
{
	@Test
	public void equalsTrueSameObject()
	{
		PageId pageId1 = new PageId(1, new TableId("a"));
		assertTrue(pageId1.equals(pageId1));
	}
	
	@Test
	public void equalsTrueOtherObject()
	{
		PageId pageId1 = new PageId(1, new TableId("a"));
		PageId pageId2 = new PageId(1, new TableId("a"));
		
		assertTrue(pageId1.equals(pageId2));
		assertEquals(pageId1.hashCode(),pageId2.hashCode());
	}
	
	@Test
	public void equalsFalseNullObject()
	{
		PageId pageId1 = new PageId(1, new TableId("a"));
		
		assertFalse(pageId1.equals(null));
	}
	
	@Test
	public void equalsFalseDifferentType()
	{
		PageId pageId1 = new PageId(1, new TableId("a"));
		
		assertFalse(pageId1.equals("aaaaaaaaaaaaaaaaaa"));
	}
	
	@Test
	public void equalsFalseDifferentId()
	{
		PageId pageId1 = new PageId(1, new TableId("a"));
		PageId pageId2 = new PageId(222, new TableId("a"));
		
		assertFalse(pageId1.equals(pageId2));
	}
	
	@Test
	public void equalsFalseDifferentTableId()
	{
		PageId pageId1 = new PageId(1, new TableId("a"));
		PageId pageId2 = new PageId(1, new TableId("bbbbbbb"));
		
		assertFalse(pageId1.equals(pageId2));
	}
}
