package ubadb.core.components.bufferManager.bufferPool.pools.multiple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ubadb.core.common.Page;
import ubadb.core.common.PageId;
import ubadb.core.common.TableId;
import ubadb.core.components.bufferManager.bufferPool.BufferFrame;
import ubadb.core.components.bufferManager.bufferPool.BufferPoolException;
import ubadb.core.components.bufferManager.bufferPool.replacementStrategies.PageReplacementStrategy;
import ubadb.core.components.catalogManager.CatalogManager;
import ubadb.core.components.catalogManager.CatalogManagerException;
import ubadb.core.components.catalogManager.CatalogManagerImpl;
import ubadb.core.testDoubles.DummyObjectFactory;
import ubadb.core.testDoubles.FakePageReplacementStrategy;

@SuppressWarnings("unused")
public class MultipleBufferPoolTest {
	private MultipleBufferPool bufferPool;
	private static final String POOL_1 = "POOL_1";
	private static final String POOL_2 = "POOL_2";	
	private static final TableId TABLE_A = new TableId("a");		//para el POOL_1
	private static final TableId TABLE_B = new TableId("b");		//para el POOL_2
	
//	Del 0 al 3 lo mando al POOL_1
	private static final Page PAGE_0 = new Page(new PageId(0, TABLE_A), "abc".getBytes());
	private static final Page PAGE_1 = new Page(new PageId(1, TABLE_A), "abc".getBytes());
	private static final Page PAGE_2 = new Page(new PageId(2, TABLE_A), "abc".getBytes());
	private static final Page PAGE_3 = new Page(new PageId(3, TABLE_A), "abc".getBytes());
	
//	Del 4 al 7 lo mando al POOL_2
	private static final Page PAGE_4 = new Page(new PageId(4, TABLE_B), "abc".getBytes());
	private static final Page PAGE_5 = new Page(new PageId(5, TABLE_B), "abc".getBytes());
	private static final Page PAGE_6 = new Page(new PageId(6, TABLE_B), "abc".getBytes());
	private static final Page PAGE_7 = new Page(new PageId(7, TABLE_B), "abc".getBytes());
	
	@Before
	public void setUp() throws CatalogManagerException	{
//		POOL_1 de tamaño 3 y POOL_2 de tamaño 4
		Map<String, Integer> maxBufferPoolSizes = new HashMap<String, Integer>();
		Map<String, PageReplacementStrategy> pageReplacementStrategies = new HashMap<String, PageReplacementStrategy>();
		maxBufferPoolSizes.put(POOL_1, 3);
		maxBufferPoolSizes.put(POOL_2, 4);
		pageReplacementStrategies.put(POOL_1, new FakePageReplacementStrategy());
		pageReplacementStrategies.put(POOL_2, new FakePageReplacementStrategy());
		CatalogManager catalogManager = new CatalogManagerImpl("generated/catalog_test_multiple.db", "generated/fake");
		
		catalogManager.loadCatalog();
		bufferPool = new MultipleBufferPool(maxBufferPoolSizes,	pageReplacementStrategies, catalogManager);
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

		bufferPool.getBufferFrame(new PageId(99, new TableId("bbbb")));
	}
	
	@Test
	public void testHasSpaceTrue() throws Exception
	{
		bufferPool.addNewPage(PAGE_6);
		assertTrue(bufferPool.hasSpace(PAGE_7.getPageId()));
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
		bufferPool.addNewPage(PAGE_5);
		bufferPool.addNewPage(PAGE_6);
		
		assertEquals(2, bufferPool.countPagesInPool());
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
		bufferPool.addNewPage(PAGE_5);
		
		bufferPool.removePage(PAGE_0.getPageId());
		
		assertEquals(2, bufferPool.countPagesInPool());
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
		bufferPool.addNewPage(PAGE_7);
		
		assertEquals(3, bufferPool.countPagesInPool());
	}
}
