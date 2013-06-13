package ubadb.core.components.bufferManager.bufferPool.pools.single;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ubadb.core.common.Page;
import ubadb.core.common.PageId;
import ubadb.core.common.TableId;
import ubadb.core.components.bufferManager.bufferPool.BufferFrame;
import ubadb.core.components.bufferManager.bufferPool.BufferPoolException;
import ubadb.core.components.bufferManager.bufferPool.pools.single.SingleBufferPool;
import ubadb.core.testDoubles.DummyObjectFactory;
import ubadb.core.testDoubles.FakePageReplacementStrategy;


public class SingleBufferPoolTest
{
	private SingleBufferPool bufferPool;
	private static final int POOL_MAX_SIZE = 3;
	private static final Page PAGE_0 = new Page(new PageId(0, DummyObjectFactory.TABLE_ID), "abc".getBytes());
	private static final Page PAGE_1 = new Page(new PageId(1, DummyObjectFactory.TABLE_ID), "abc".getBytes());
	private static final Page PAGE_2 = new Page(new PageId(2, DummyObjectFactory.TABLE_ID), "abc".getBytes());
	private static final Page PAGE_3 = new Page(new PageId(3, DummyObjectFactory.TABLE_ID), "abc".getBytes());
	
	@Before
	public void setUp()
	{
		bufferPool = new SingleBufferPool(POOL_MAX_SIZE, new FakePageReplacementStrategy());
	}
	
	@Test
	public void testIsPageInPoolTrue() throws Exception
	{
		bufferPool.addNewPage(PAGE_0);
		
		assertTrue(bufferPool.isPageInPool(PAGE_0.getPageId()));
	}
	
	@Test
	public void testIsPageInPoolFalse() throws Exception
	{
		assertFalse(bufferPool.isPageInPool(PAGE_0.getPageId()));
	}
	
	@Test
	public void testGetExistingPage() throws Exception
	{
		BufferFrame expectedFrame = bufferPool.addNewPage(PAGE_0);
		
		assertEquals(expectedFrame, bufferPool.getBufferFrame(PAGE_0.getPageId()));
	}
	
	@Test(expected=BufferPoolException.class)
	public void testGetUnexistingPage() throws Exception
	{
		bufferPool.addNewPage(PAGE_0);
		
		assertNull(bufferPool.getBufferFrame(new PageId(99, new TableId("bbbb"))));
	}
	
	@Test
	public void testHasSpaceTrue() throws Exception
	{
		bufferPool.addNewPage(PAGE_0);
		assertTrue(bufferPool.hasSpace(PAGE_1.getPageId()));
	}
	
	@Test
	public void testHasSpaceFalse() throws Exception
	{
		bufferPool.addNewPage(PAGE_0);
		bufferPool.addNewPage(PAGE_1);
		bufferPool.addNewPage(PAGE_2);
		
		assertFalse(bufferPool.hasSpace(PAGE_3.getPageId()));
	}
	
	@Test
	public void testAddNewPageWithSpace() throws Exception
	{
		bufferPool.addNewPage(PAGE_0);
		bufferPool.addNewPage(PAGE_1);
		
		assertEquals(2,bufferPool.countPagesInPool());
	}
	
	@Test(expected=BufferPoolException.class)
	public void testAddNewPageWithoutSpace() throws Exception
	{
		bufferPool.addNewPage(PAGE_0);
		bufferPool.addNewPage(PAGE_1);
		bufferPool.addNewPage(PAGE_2);
		bufferPool.addNewPage(PAGE_3);
	}

	@Test(expected=BufferPoolException.class)
	public void testAddNewPageWithExisting() throws Exception
	{
		bufferPool.addNewPage(PAGE_0);
		bufferPool.addNewPage(PAGE_0);
	}
	
	@Test
	public void testNewPageIsUnpinnedAndNotDirtyByDefault() throws Exception
	{
		BufferFrame bufferFrame = bufferPool.addNewPage(PAGE_0);

		assertEquals(0,bufferFrame.getPinCount());
		assertFalse(bufferFrame.isDirty());
	}
	
	@Test
	public void testRemoveExistingPage() throws Exception
	{
		bufferPool.addNewPage(PAGE_0);
		bufferPool.addNewPage(PAGE_1);
		bufferPool.addNewPage(PAGE_2);
		
		bufferPool.removePage(PAGE_0.getPageId());
		
		assertEquals(2,bufferPool.countPagesInPool());
	}
	
	@Test(expected=BufferPoolException.class)
	public void testRemoveUnexistingPage() throws Exception
	{
		bufferPool.addNewPage(PAGE_0);
		bufferPool.addNewPage(PAGE_1);
		bufferPool.addNewPage(PAGE_2);
		
		bufferPool.removePage(PAGE_3.getPageId());
	}
	
	@Test
	public void countPagesEmptyPool()
	{
		assertEquals(0, bufferPool.countPagesInPool());
	}
	
	@Test
	public void countPagesNonEmptyPool() throws Exception
	{
		bufferPool.addNewPage(PAGE_0);
		bufferPool.addNewPage(PAGE_1);
		bufferPool.addNewPage(PAGE_2);
		
		assertEquals(3, bufferPool.countPagesInPool());
	}
}
