package ubadb.external.bufferManagement;

import java.util.HashMap;
import java.util.Map;

import ubadb.core.components.bufferManager.BufferManager;
import ubadb.core.components.bufferManager.BufferManagerException;
import ubadb.core.components.bufferManager.BufferManagerImpl;
import ubadb.core.components.bufferManager.bufferPool.BufferPool;
import ubadb.core.components.bufferManager.bufferPool.pools.multiple.MultipleBufferPool;
import ubadb.core.components.bufferManager.bufferPool.pools.single.SingleBufferPool;
import ubadb.core.components.bufferManager.bufferPool.replacementStrategies.PageReplacementStrategy;
import ubadb.core.components.bufferManager.bufferPool.replacementStrategies.fifo.FIFOReplacementStrategy;
import ubadb.core.components.catalogManager.CatalogManager;
import ubadb.core.components.catalogManager.CatalogManagerException;
import ubadb.core.components.catalogManager.CatalogManagerImpl;
import ubadb.external.bufferManagement.etc.BufferManagementMetrics;
import ubadb.external.bufferManagement.etc.FaultCounterDiskManagerSpy;
import ubadb.external.bufferManagement.etc.PageReference;
import ubadb.external.bufferManagement.etc.PageReferenceTrace;
import ubadb.external.bufferManagement.etc.PageReferenceTraceSerializer;

public class MainEvaluator
{
	private static final int PAUSE_BETWEEN_REFERENCES	= 0;
	private static final String[] BUFFERPOOL_NAMES = {"DEFAULT", "KEEP", "RECYCLE"};
	private static final int[] BUFFERPOOL_SIZES = {80, 30, 10};
	private static final String CATALOGFILEPATH = "generated/catalog.db";
	private static final String FILEPATHPREFIX = "";
	
	public static void main(String[] args)
	{
		
		PageReplacementStrategy pageReplacementStrategy = new FIFOReplacementStrategy();
		PageReplacementStrategy pageReplacementStrategySingle = new FIFOReplacementStrategy();
		//String traceFileName = "generated/prueba.trace";
		String traceFileName = "generated_seq/prueba.trace";		

		
		Map<String, PageReplacementStrategy> pageReplacementStrategies = new HashMap<String, PageReplacementStrategy>();
		for(int i = 0; i < BUFFERPOOL_NAMES.length; i++)
			pageReplacementStrategies.put(BUFFERPOOL_NAMES[i], pageReplacementStrategy);
			
		Map<String, Integer> maxBufferPoolSizes = new HashMap<String, Integer>();
		
		int totalSize=0;
		for(int i = 0; i < BUFFERPOOL_NAMES.length; i++)
		{	
			maxBufferPoolSizes.put(BUFFERPOOL_NAMES[i], BUFFERPOOL_SIZES[i]);
			totalSize+=BUFFERPOOL_SIZES[i];
		}
		
		CatalogManager catalogManager = new CatalogManagerImpl(CATALOGFILEPATH, FILEPATHPREFIX);
		
		try {
			catalogManager.loadCatalog();
		} catch (CatalogManagerException e1) {
			// 
			e1.printStackTrace();
		}
		
		// Buffer Manager con múltiples buffer pools		
		BufferPool multipleBufferPool = new MultipleBufferPool(maxBufferPoolSizes, pageReplacementStrategies, catalogManager);
		FaultCounterDiskManagerSpy faultCounterDiskManagerSpy = new FaultCounterDiskManagerSpy();
		BufferManager bufferManager = new BufferManagerImpl(faultCounterDiskManagerSpy, catalogManager, multipleBufferPool);

		// Buffer Manager con un sólo buffer pool	
		BufferPool singleBufferPool = new SingleBufferPool(totalSize,pageReplacementStrategySingle);
		FaultCounterDiskManagerSpy faultCounterDiskManagerSpySingle = new FaultCounterDiskManagerSpy();
		BufferManager bufferManagerSingle = new BufferManagerImpl(faultCounterDiskManagerSpySingle, catalogManager, singleBufferPool);
		
		PageReferenceTrace trace = null;
		try {
			trace = getTrace(traceFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			evaluate(traceFileName, bufferManager);
			evaluate(traceFileName, bufferManagerSingle);
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		System.out.println("Multiple:");
		System.out.println("---------");
		BufferManagementMetrics metrics = new BufferManagementMetrics(trace, faultCounterDiskManagerSpy.getFaultsCount());
		metrics.showSummary();

		System.out.println("");
		System.out.println("Single:");
		System.out.println("-------");
		metrics = new BufferManagementMetrics(trace, faultCounterDiskManagerSpySingle.getFaultsCount());
		metrics.showSummary();
		
	}

	private static void evaluate(String traceFileName, BufferManager bufferManager) throws Exception
	{
		
		PageReferenceTrace trace = getTrace(traceFileName);
		
		for(PageReference pageReference : trace.getPageReferences())
		{
			//Pause references to have different dates in LRU and MRU
			Thread.sleep(PAUSE_BETWEEN_REFERENCES);
			
			switch(pageReference.getType())
			{
				case REQUEST:
				{
					try
					{
						bufferManager.readPage(pageReference.getPageId());
					}
					catch(BufferManagerException e)
					{
						System.out.println("NO MORE SPACE AVAILABLE, MEMORY FULL");
						throw e;
					}
					break;
				}
				case RELEASE:
				{
					bufferManager.releasePage(pageReference.getPageId());
					break;
				}
			}
		}
		
	}

	private static PageReferenceTrace getTrace(String traceFileName) throws Exception
	{
		PageReferenceTraceSerializer serializer = new PageReferenceTraceSerializer();
		return serializer.read(traceFileName);
	}
}
