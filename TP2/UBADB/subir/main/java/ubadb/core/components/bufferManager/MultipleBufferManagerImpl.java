package ubadb.core.components.bufferManager;

import java.util.Collection;
import java.util.Map;

import ubadb.core.common.Page;
import ubadb.core.common.PageId;
import ubadb.core.common.TableId;
import ubadb.core.components.bufferManager.bufferPool.BufferFrame;
import ubadb.core.components.bufferManager.bufferPool.BufferPool;
import ubadb.core.components.bufferManager.bufferPool.BufferPoolException;
import ubadb.core.components.catalogManager.CatalogManager;
import ubadb.core.components.catalogManager.CatalogManagerException;
import ubadb.core.components.diskManager.DiskManager;
import ubadb.core.components.diskManager.DiskManagerException;

@SuppressWarnings("unused")
public class MultipleBufferManagerImpl implements BufferManager {
	private DiskManager diskManager;
	private CatalogManager catalogManager;
	private Map<String, BufferPool> bufferPools;
	
	public MultipleBufferManagerImpl(DiskManager diskManager, CatalogManager catalogManager, 
			Map<String, BufferPool> bufferPools) {
		this.diskManager = diskManager;
		this.catalogManager = catalogManager;
		this.bufferPools = bufferPools;
	}

	/**
	 * If the requested page is in the buffer pool, pin and return it.
	 * If not, get it from the disk, add it to the buffer pool, pin and return it. 
	 * @throws BufferManagerException 
	 */
	public synchronized Page readPage(PageId pageId) throws BufferManagerException {
		try	{
			BufferFrame bufferFrame;
			TableId tableId;
			String tablePool;
			
			tableId=pageId.getTableId();
			tablePool=catalogManager.getTableDescriptorByTableId(tableId).getTablePool();
			
			BufferPool pool = bufferPools.get(tablePool);
			
			if(pool.isPageInPool(pageId)) {
				bufferFrame = pool.getBufferFrame(pageId);
			} else {
				bufferFrame = readFromDiskAndAddToPool(pageId, pool);
			}
			
			bufferFrame.pin();
			
			return bufferFrame.getPage();
		} catch(Exception e) {
			throw new BufferManagerException("Cannot read page", e);
		}
	}

	/**
	 * Unpins page, indicating that's no longer used by the one that read it before
	 */
	public synchronized void releasePage(PageId pageId) throws BufferManagerException {
		try	{
			bufferPools.get(catalogManager.getPoolByPageId(pageId)).getBufferFrame(pageId).unpin();
		} catch (Exception e) {
			throw new BufferManagerException("Cannot not release page from pool", e);
		}
	}
	
	/**
	 * Create new page in disk and add it to the buffer pool
	 * @throws BufferManagerException 
	 */
//	es necesario implementar esto???
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
	 * @throws CatalogManagerException 
	 */
	private BufferFrame readFromDiskAndAddToPool(PageId pageId, BufferPool pool) 
			throws DiskManagerException, BufferPoolException, CatalogManagerException {
		Page pageFromDisk = diskManager.readPage(pageId);
		
		BufferFrame bufferFrame = addNewPageToBufferPool(pageFromDisk, pool);
		
		return bufferFrame;
	}

	/**
	 *	Add a new page to the buffer pool. If there is no space, find a victim and make space for the new page 
	 * @throws CatalogManagerException 
	 */
	protected BufferFrame addNewPageToBufferPool(Page pageFromDisk, BufferPool pool) 
			throws BufferPoolException, DiskManagerException, CatalogManagerException {		
		if(!pool.hasSpace(pageFromDisk.getPageId())) {
			Page victimPage = pool.findVictim(pageFromDisk.getPageId()).getPage();

			flushPage(victimPage.getPageId(), pool);
			removePage(victimPage.getPageId(), pool);
		}
		
		return pool.addNewPage(pageFromDisk);
	}
	
	/**
	 * If the page is dirty, save it to disk 
	 * @throws CatalogManagerException 
	 */
	protected void flushPage(PageId pageId, BufferPool pool) 
			throws BufferPoolException, DiskManagerException, CatalogManagerException {
		BufferFrame bufferFrame = pool.getBufferFrame(pageId);
		
		if(bufferFrame.isDirty()) {
			diskManager.writeExistingPage(bufferFrame.getPage());
			bufferFrame.setDirty(false);
		}
	}
	
	/**
	 * Remove page from buffer pool. No unpinning and flushing is performed. 
	 */
	private void removePage(PageId pageId, BufferPool pool) throws BufferPoolException {
		pool.removePage(pageId);
	}
//	
//	/**
//	 *	Method added for testing purposes 
//	 */
//	protected BufferPool getBufferPool()
//	{
//		return bufferPool;
//	}
}
