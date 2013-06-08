package ubadb.core.components.bufferManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ubadb.core.common.Page;
import ubadb.core.common.PageId;
import ubadb.core.common.TableId;
import ubadb.core.components.bufferManager.BufferManagerImpl;
import ubadb.core.testDoubles.BufferPoolSpy;
import ubadb.core.testDoubles.DiskManagerSpy;



public class BufferManagerImplTest
{
	private static final Page PAGE_0 = new Page(new PageId(0, new TableId("a")),"aaa".getBytes());
	private static final Page PAGE_1 = new Page(new PageId(1, new TableId("b")),"bbb".getBytes());
	private static final Page PAGE_2 = new Page(new PageId(2, new TableId("c")),"ccc".getBytes());
	private BufferManagerImpl bufferManager;
	private DiskManagerSpy diskManagerSpy;
	private BufferPoolSpy bufferPoolSpy;
	private static final int BUFFER_POOL_MAX_SIZE = 2;
	
	@Before
	public void setUp()
	{
		diskManagerSpy = new DiskManagerSpy();
		bufferPoolSpy = new BufferPoolSpy(BUFFER_POOL_MAX_SIZE);
		bufferManager = new BufferManagerImpl(diskManagerSpy, null, bufferPoolSpy);
	}
	
	@Test
	public void testReadPageThatIsInPool() throws Exception
	{
		bufferPoolSpy.addNewPage(PAGE_0);
		
		Page actualPage = bufferManager.readPage(PAGE_0.getPageId());
		
		assertEquals(PAGE_0, actualPage);
		assertEquals(1, bufferPoolSpy.getBufferFrame(PAGE_0.getPageId()).getPinCount());
	}
	
	@Test
	public void testReadPageThatIsInDisk() throws Exception
	{
		diskManagerSpy.setSinglePage(PAGE_1);
		Page actualPage = bufferManager.readPage(PAGE_1.getPageId());
		
		assertEquals(PAGE_1, actualPage);
		assertEquals(1, bufferPoolSpy.getBufferFrame(PAGE_1.getPageId()).getPinCount());
	}

	@Test
	public void testAddNewPageToBufferPoolWithSpace() throws Exception
	{
		bufferManager.addNewPageToBufferPool(PAGE_0);
		assertEquals(1,bufferManager.getBufferPool().countPagesInPool());
	}
	
	@Test
	public void testAddNewPageWithoutSpace() throws Exception
	{
		bufferPoolSpy.addNewPage(PAGE_0);
		bufferPoolSpy.addNewPage(PAGE_1);
		bufferPoolSpy.setVictim(PAGE_0.getPageId());
		
		bufferManager.addNewPageToBufferPool(PAGE_2);
		
		assertEquals(2, bufferManager.getBufferPool().countPagesInPool());
		assertTrue(bufferManager.getBufferPool().isPageInPool(PAGE_1.getPageId()));
		assertTrue(bufferManager.getBufferPool().isPageInPool(PAGE_2.getPageId()));
	}
	
	@Test
	public void testFlushNonDirtyPage() throws Exception
	{
		bufferPoolSpy.addNewPage(PAGE_0);
		
		bufferManager.flushPage(PAGE_0.getPageId());
		
		assertTrue(diskManagerSpy.getWrittenPages().isEmpty());
	}
	
	@Test
	public void testFlushDirtyPage() throws Exception
	{
		bufferPoolSpy.addNewPage(PAGE_0);
		bufferPoolSpy.getBufferFrame(PAGE_0.getPageId()).setDirty(true);
		
		bufferManager.flushPage(PAGE_0.getPageId());
		
		assertTrue(diskManagerSpy.getWrittenPages().contains(PAGE_0));
		assertEquals(1, diskManagerSpy.getWrittenPages().size());
	}
	
	@Test
	public void testReleasePageFoundInPool() throws Exception
	{
		bufferPoolSpy.addNewPage(PAGE_0);
		bufferPoolSpy.getBufferFrame(PAGE_0.getPageId()).pin();
		
		bufferManager.releasePage(PAGE_0.getPageId());

		assertEquals(0,bufferPoolSpy.getBufferFrame(PAGE_0.getPageId()).getPinCount());
		assertTrue(bufferManager.getBufferPool().isPageInPool(PAGE_0.getPageId())); //Check that "releasePage" doesn't remove the page from pool (it just unpins it)
	}
	
	@Test(expected=BufferManagerException.class)
	public void testReleasePageNotFoundInPool() throws Exception
	{
		bufferManager.releasePage(PAGE_2.getPageId());
	}
}
