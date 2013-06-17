package ubadb.external.bufferManagement;

import java.util.HashMap;
import java.util.Map;

import ubadb.core.components.bufferManager.BufferManager;
import ubadb.core.components.bufferManager.BufferManagerException;
import ubadb.core.components.bufferManager.BufferManagerImpl;
import ubadb.core.components.bufferManager.bufferPool.BufferPool;
import ubadb.core.components.bufferManager.bufferPool.pools.multiple.MultipleBufferPool;
import ubadb.core.components.bufferManager.bufferPool.replacementStrategies.PageReplacementStrategy;
import ubadb.core.components.bufferManager.bufferPool.replacementStrategies.fifo.FIFOReplacementStrategy;
import ubadb.core.components.catalogManager.CatalogManager;
import ubadb.core.components.diskManager.DiskManager;
import ubadb.external.bufferManagement.etc.BufferManagementMetrics;
import ubadb.external.bufferManagement.etc.FaultCounterDiskManagerSpy;
import ubadb.external.bufferManagement.etc.PageReference;
import ubadb.external.bufferManagement.etc.PageReferenceTrace;
import ubadb.external.bufferManagement.etc.PageReferenceTraceSerializer;

public class MainEvaluatorMB
{
	private static final int PAUSE_BETWEEN_REFERENCES	= 0;
	private static final String[] BUFFERPOOL_NAMES = {"DEFAULT", "KEEP", "RECYCLE"};
	private static final int[] BUFFERPOOL_SIZES = {100, 100, 100};
	
	public static void main(String[] args)
	{
		try
		{
			PageReplacementStrategy pageReplacementStrategy = new FIFOReplacementStrategy();
			String traceFileName = "generated/a.trace";
			
			Map<String, PageReplacementStrategy> pageReplacementStrategies = new HashMap<String, PageReplacementStrategy>();
			for(int i = 0; i < BUFFERPOOL_NAMES.length; i++)
				pageReplacementStrategies.put(BUFFERPOOL_NAMES[i], pageReplacementStrategy);
				
			Map<String, Integer> maxBufferPoolSizes = new HashMap<String, Integer>();
			for(int i = 0; i < BUFFERPOOL_NAMES.length; i++)
				maxBufferPoolSizes.put(BUFFERPOOL_NAMES[i], BUFFERPOOL_SIZES[i]);		
			
			evaluate(pageReplacementStrategies, traceFileName, maxBufferPoolSizes);
		}
		catch(Exception e)
		{
			System.out.println("FATAL ERROR (" + e.getMessage() + ")");
			e.printStackTrace();
		}
	}

	private static void evaluate(Map<String, PageReplacementStrategy> pageReplacementStrategies,
								 String traceFileName,
								 Map<String, Integer> maxBufferPoolSizes) throws Exception, InterruptedException, BufferManagerException
	{
		FaultCounterDiskManagerSpy faultCounterDiskManagerSpy = new FaultCounterDiskManagerSpy();
//		CatalogManager catalogManager = new CatalogManagerImpl();
		CatalogManager catalogManager = null;
		BufferManager bufferManager = createBufferManager(faultCounterDiskManagerSpy, catalogManager, pageReplacementStrategies, maxBufferPoolSizes);
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
		
		BufferManagementMetrics metrics = new BufferManagementMetrics(trace, faultCounterDiskManagerSpy.getFaultsCount());
		metrics.showSummary();
	}

	private static PageReferenceTrace getTrace(String traceFileName) throws Exception
	{
		PageReferenceTraceSerializer serializer = new PageReferenceTraceSerializer();
		return serializer.read(traceFileName);
	}
	
	private static BufferManager createBufferManager(DiskManager diskManager, CatalogManager catalogManager, Map<String, PageReplacementStrategy> pageReplacementStrategies, Map<String, Integer> maxBufferPoolSizes)
	{
		BufferPool multipleBufferPool = new MultipleBufferPool(maxBufferPoolSizes, pageReplacementStrategies, catalogManager);
		BufferManager bufferManager = new BufferManagerImpl(diskManager, catalogManager, multipleBufferPool);
		
		return bufferManager;
	}
}
