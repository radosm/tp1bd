package ubadb.core.components.bufferManager.bufferPool;

import ubadb.core.common.Page;
import ubadb.core.common.PageId;

public interface BufferPool
{
	boolean isPageInPool(PageId pageId);
	
	/**
	 * If the page is present in the pool, return it; otherwise throw an exception
	 */
	BufferFrame getBufferFrame(PageId pageId) throws BufferPoolException;
	

	/**
	 * pageToAddId parameter should be used if the space depends on the page that is trying to be added to the pool
	 */
	boolean hasSpace(PageId pageToAddId);
	
	/**
	 * In case there is not enough space or the page already exists in the pool, throw an exception
	 */
	BufferFrame addNewPage(Page page) throws BufferPoolException;
	
	/**
	 * In case the requested page is not in the pool, throw an exception
	 */
	void removePage(PageId pageId) throws BufferPoolException;
	
	/**
	 * pageId parameter should be used if the victim page depends on the page that is trying to be added to the pool
	 * If no victim is found, throw an Exception
	 */
	BufferFrame findVictim(PageId pageIdToBeAdded) throws BufferPoolException;
	
	int countPagesInPool();
}