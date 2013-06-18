package ubadb.core.components.bufferManager.bufferPool.pools.multiple;

import java.util.HashMap;
import java.util.Map;

import ubadb.core.common.Page;
import ubadb.core.common.PageId;
import ubadb.core.components.bufferManager.bufferPool.BufferFrame;
import ubadb.core.components.bufferManager.bufferPool.BufferPool;
import ubadb.core.components.bufferManager.bufferPool.BufferPoolException;
import ubadb.core.components.bufferManager.bufferPool.replacementStrategies.PageReplacementStrategy;
import ubadb.core.components.catalogManager.CatalogManager;

public class MultipleBufferPool implements BufferPool {
	private Map<String, Map<PageId, BufferFrame>> framesMaps;	
	private Map<String, PageReplacementStrategy> pageReplacementStrategies;
	private Map<String, Integer> maxBufferPoolSizes;
	private CatalogManager catalogManager;

	public MultipleBufferPool(
			Map<String, Integer> maxBufferPoolSizes, 
			Map<String, PageReplacementStrategy> pageReplacementStrategies, 
			CatalogManager catalogManager) {
		this.maxBufferPoolSizes = maxBufferPoolSizes;
		this.pageReplacementStrategies = pageReplacementStrategies;
		this.framesMaps = new HashMap<String, Map<PageId, BufferFrame>>();
		this.catalogManager = catalogManager;
		for (Map.Entry<String, Integer> entry : maxBufferPoolSizes.entrySet()) {
			this.framesMaps.put(entry.getKey(), new HashMap<PageId, BufferFrame>(entry.getValue()));
		}
	}
	
	public boolean isPageInPool(PageId pageId) {
		return framesMaps.get(getPoolByPageId(pageId)).containsKey(pageId);
	}
	
	public BufferFrame getBufferFrame(PageId pageId) throws BufferPoolException {
        BufferFrame bf;
        try{
            String pool = getPoolByPageId(pageId);
			bf = framesMaps.get(pool).get(pageId);
		}catch(Exception e){
			throw new BufferPoolException("The requested page is not in the pool");
		}
        return bf;
	}
	
	public boolean hasSpace(PageId pageToAddId) {
		String pool = getPoolByPageId(pageToAddId);
		return ((Map<PageId, BufferFrame>) framesMaps.get(pool)).size() < maxBufferPoolSizes.get(pool);
	}
	
	public BufferFrame addNewPage(Page page) throws BufferPoolException	{
		String pool = getPoolByPage(page);
		
		if(!hasSpace(page.getPageId())) {
			throw new BufferPoolException("No space in pool for new page");
		} else if(isPageInPool(page.getPageId())) {
			throw new BufferPoolException("Page already exists in the pool");
		} else {
			//Add it to pool
			BufferFrame bufferFrame = pageReplacementStrategies.get(pool).createNewFrame(page);
			framesMaps.get(pool).put(page.getPageId(),bufferFrame);
			
			return bufferFrame;
		}
	}
	
	public void removePage(PageId pageId) throws BufferPoolException {
		if(isPageInPool(pageId)) {
			framesMaps.get(getPoolByPageId(pageId)).remove(pageId);
		} else {
			throw new BufferPoolException("Cannot remove an unexisting page");
		}
	}
	
	public BufferFrame findVictim(PageId pageIdToBeAdded) throws BufferPoolException {
		try	{
			String pool = getPoolByPageId(pageIdToBeAdded);
			return pageReplacementStrategies.get(pool).findVictim(framesMaps.get(pool).values());
		} catch(Exception e) {
			throw new BufferPoolException("Cannot find a victim page for removal", e);
		}
	}
	
	/**
	 * A este le puse la suma de todos. No se si está bien!
	 * 
	 */
	public int countPagesInPool() {
		int suma = 0;
		for (Map<PageId, BufferFrame> map : framesMaps.values()) {
			suma += map.size();
		}
		return suma;
	}
	
	private String getPoolByPageId(PageId pageId) {
		return catalogManager.getTableDescriptorByTableId(pageId.getTableId()).getTablePool();
	}

	private String getPoolByPage(Page page) {
		return catalogManager.getTableDescriptorByTableId(page.getPageId().getTableId()).getTablePool();
	}

}
