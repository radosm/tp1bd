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
	private Map<String, Map<PageId, BufferFrame>> framesMaps;				//mapa que por cada pool tiene el buffer
	private Map<String, PageReplacementStrategy> pageReplacementStrategies;	//mapa con las estrategias de reemplazo
	private Map<String, Integer> maxBufferPoolSizes;						//mapa con los tamaños de los pools
	private CatalogManager catalogManager;

	/**
	 * @param maxBufferPoolSizes Mapa donde las claves son los nombres de los BufferPool y los significados
	 *	son sus tamaños. 
	 * @param pageReplacementStrategies Mapa donde las claves son los nombres de los BufferPool y los
	 * 	significados son sus estrategias de reemplazo.
	 * @param catalogManager Catalog Manager que se está usando. Se necesita para averiguar los pools de 
	 * 	cada página.
	 * 
	 * @pre maxBufferPoolSizes y pageReplacementStrategies tienen las mismas claves. 
	 */
	public MultipleBufferPool(
			Map<String, Integer> maxBufferPoolSizes, 
			Map<String, PageReplacementStrategy> pageReplacementStrategies, 
			CatalogManager catalogManager) {
		this.maxBufferPoolSizes = maxBufferPoolSizes;
		this.pageReplacementStrategies = pageReplacementStrategies;
		this.framesMaps = new HashMap<String, Map<PageId, BufferFrame>>();
		this.catalogManager = catalogManager;
//		por cada pool creo un mapa con su tamaño y lo agrego al mapa framesMap
		for (Map.Entry<String, Integer> entry : maxBufferPoolSizes.entrySet()) {
			this.framesMaps.put(entry.getKey(), new HashMap<PageId, BufferFrame>(entry.getValue()));
		}
	}
	
	/**
	 * @param pageId id de la página. 
	 * @return true si pageId está en algún pool. 
	 */
	public boolean isPageInPool(PageId pageId) {
		return framesMaps.get(getPoolByPageId(pageId)).containsKey(pageId);
	}
	
	/**
	 * Si la página está en su pool, la retorna.
	 * 
	 * @param pageId id de la página.
	 * @return BufferFrame con la página.
	 * @throws BufferPoolException si la página no está en su pool.
	 */
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
	
	/**
	 * @param pageToAddId id de la página
	 * @return true si hay espacio para agregar la página a su pool.
	 */
	public boolean hasSpace(PageId pageToAddId) {
		String pool = getPoolByPageId(pageToAddId);
		return ((Map<PageId, BufferFrame>) framesMaps.get(pool)).size() < maxBufferPoolSizes.get(pool);
	}
	
	/**
	 * Agrega la página al pool. 
	 * 
	 * @param page la página a agregar.
	 * @return BufferFrame de la página agregada.
	 * @throws BufferPoolException si no hay espacio en el pool o si la página ya estaba en el pool.
	 */
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
	
	/**
	 * Borra la página del pool.
	 * 
	 * @param pageId id de la página.
	 * @throws BufferPoolException si la página no está en el pool.
	 */
	public void removePage(PageId pageId) throws BufferPoolException {
		if(isPageInPool(pageId)) {
			framesMaps.get(getPoolByPageId(pageId)).remove(pageId);
		} else {
			throw new BufferPoolException("Cannot remove an unexisting page");
		}
	}
	
	/**
	 * Encuentra un BufferFrame víctima para liberar según la estrategia de reemplazo que use el pool.
	 * 
	 * @param pageIdToBeAdded id de la página a agregar.
	 * @return BufferFrame víctima.
	 * @throws BufferPoolException si no se pudo encontrar ninguna víctima. 
	 */
	public BufferFrame findVictim(PageId pageIdToBeAdded) throws BufferPoolException {
		try	{
			String pool = getPoolByPageId(pageIdToBeAdded);
			return pageReplacementStrategies.get(pool).findVictim(framesMaps.get(pool).values());
		} catch(Exception e) {
			throw new BufferPoolException("Cannot find a victim page for removal", e);
		}
	}
	
	/**
	 * @return la suma de los tamaños de todos los bufferPools.
	 * 
	 */
	public int countPagesInPool() {
		int suma = 0;
		for (Map<PageId, BufferFrame> map : framesMaps.values()) {
			suma += map.size();
		}
		return suma;
	}
	
	/**
	 * Retorna El pool de la página indicada.
	 * 
	 * @param pageId id de la página.
	 * @return String con el nombre del pool.
	 */
	private String getPoolByPageId(PageId pageId) {
		return catalogManager.getTableDescriptorByTableId(pageId.getTableId()).getTablePool();
	}

	/**
	 * Retorna El pool de la página indicada.
	 * 
	 * @param page página cuyo pool se desea averiguar.
	 * @return String con el nombre del pool.
	 */
	private String getPoolByPage(Page page) {
		return catalogManager.getTableDescriptorByTableId(page.getPageId().getTableId()).getTablePool();
	}

}
