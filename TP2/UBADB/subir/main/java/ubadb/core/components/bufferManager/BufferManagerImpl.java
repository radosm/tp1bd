package ubadb.core.components.bufferManager;

import ubadb.core.common.Page;
import ubadb.core.common.PageId;
import ubadb.core.common.TableId;
import ubadb.core.components.bufferManager.bufferPool.BufferFrame;
import ubadb.core.components.bufferManager.bufferPool.BufferPool;
import ubadb.core.components.bufferManager.bufferPool.BufferPoolException;
import ubadb.core.components.catalogManager.CatalogManager;
import ubadb.core.components.diskManager.DiskManager;
import ubadb.core.components.diskManager.DiskManagerException;

public class BufferManagerImpl implements BufferManager
{
	private DiskManager diskManager;
	private CatalogManager catalogManager;
	private BufferPool bufferPool;
	
	public BufferManagerImpl(DiskManager diskManager, CatalogManager catalogManager, BufferPool bufferPool)
	{
		this.diskManager = diskManager;
		this.catalogManager = catalogManager;
		this.bufferPool = bufferPool;
	}

	/**
	 * If the requested page is in the buffer pool, pin and return it.
	 * If not, get it from the disk, add it to the buffer pool, pin and return it. 
	 * @throws BufferManagerException 
	 */
	public synchronized Page readPage(PageId pageId) throws BufferManagerException
	{
		try
		{
			BufferFrame bufferFrame;
			
			if(bufferPool.isPageInPool(pageId))
				bufferFrame = bufferPool.getBufferFrame(pageId);
			else
				bufferFrame = readFromDiskAndAddToPool(pageId);
			
			bufferFrame.pin();
			
			return bufferFrame.getPage();
		}
		catch(Exception e)
		{
			throw new BufferManagerException("Cannot read page", e);
		}
	}

	/**
	 * Unpins page, indicating that's no longer used by the one that read it before
	 */
	public synchronized void releasePage(PageId pageId) throws BufferManagerException
	{
		try
		{
			bufferPool.getBufferFrame(pageId).unpin();
		}
		catch (Exception e)
		{
			throw new BufferManagerException("Cannot not release page from pool", e);
		}
	}
	
	/**
	 * Create new page in disk and add it to the buffer pool
	 * @throws BufferManagerException 
	 */
	public synchronized Page createNewPage(TableId tableId, byte[] pageContents) throws BufferManagerException
	{
		//TODO: Implementation deferred
		return null;
//		try
//		{
//			Page pageFromDisk = diskManager.createNewPage(tableId, pageContents);
//			
//			addNewPageToBufferPool(pageFromDisk);
//			
//			return pageFromDisk;
//		}
//		catch(Exception e)
//		{
//			throw new BufferManagerException("Cannot create new page", e);
//		}
	}
	
	/**
	 * Read a page from disk and add it to the pool (if there is no space available, find a victim and remove it) 
	 */
	private BufferFrame readFromDiskAndAddToPool(PageId pageId) throws DiskManagerException, BufferPoolException
	{
		Page pageFromDisk = diskManager.readPage(pageId);
		
		BufferFrame bufferFrame = addNewPageToBufferPool(pageFromDisk);
		
		return bufferFrame;
	}

	/**
	 *	Add a new page to the buffer pool. If there is no space, find a victim and make space for the new page 
	 */
	protected BufferFrame addNewPageToBufferPool(Page pageFromDisk) throws BufferPoolException, DiskManagerException
	{
		if(!bufferPool.hasSpace(pageFromDisk.getPageId()))
		{
			Page victimPage = bufferPool.findVictim(pageFromDisk.getPageId()).getPage();

			flushPage(victimPage.getPageId());
			removePage(victimPage.getPageId());
		}
		
		return bufferPool.addNewPage(pageFromDisk);
	}
	
	/**
	 * If the page is dirty, save it to disk 
	 */
	protected void flushPage(PageId pageId) throws BufferPoolException, DiskManagerException
	{
		BufferFrame bufferFrame = bufferPool.getBufferFrame(pageId);
		
		if(bufferFrame.isDirty())
		{
			diskManager.writeExistingPage(bufferFrame.getPage());
			bufferFrame.setDirty(false);
		}
	}
	
	/**
	 * Remove page from buffer pool. No unpinning and flushing is performed. 
	 */
	private void removePage(PageId pageId) throws BufferPoolException
	{
		bufferPool.removePage(pageId);
	}
	
	/**
	 *	Method added for testing purposes 
	 */
	protected BufferPool getBufferPool()
	{
		return bufferPool;
	}
}
