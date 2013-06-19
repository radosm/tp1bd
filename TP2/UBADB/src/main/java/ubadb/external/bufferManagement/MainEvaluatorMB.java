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
import ubadb.core.components.catalogManager.CatalogManagerException;
import ubadb.core.components.catalogManager.CatalogManagerImpl;
//import ubadb.core.components.diskManager.DiskManager;
import ubadb.external.bufferManagement.etc.BufferManagementMetrics;
import ubadb.external.bufferManagement.etc.FaultCounterDiskManagerSpy;
import ubadb.external.bufferManagement.etc.PageReference;
import ubadb.external.bufferManagement.etc.PageReferenceTrace;
import ubadb.external.bufferManagement.etc.PageReferenceTraceSerializer;
//import ubadb.core.common.TableId;
//import ubadb.core.components.catalogManager.TableDescriptor;


public class MainEvaluatorMB
{
	private static final int PAUSE_BETWEEN_REFERENCES	= 0;
	private static final String[] BUFFERPOOL_NAMES = {"DEFAULT", "KEEP", "RECYCLE"};
	private static final int[] BUFFERPOOL_SIZES = {5000, 5000, 5000};
	private static final String CATALOGFILEPATH = "generated/catalog.db";
	private static final String FILEPATHPREFIX = "";
	
	public static void main(String[] args)
	{
		
		PageReplacementStrategy pageReplacementStrategy = new FIFOReplacementStrategy();
		String traceFileName = "generated/fileScan-Cursadas.trace";
		//String traceFileName = "generated/indexScanUnclusteres-Materias.trace";
		
		Map<String, PageReplacementStrategy> pageReplacementStrategies = new HashMap<String, PageReplacementStrategy>();
		for(int i = 0; i < BUFFERPOOL_NAMES.length; i++)
			pageReplacementStrategies.put(BUFFERPOOL_NAMES[i], pageReplacementStrategy);
			
		Map<String, Integer> maxBufferPoolSizes = new HashMap<String, Integer>();
		for(int i = 0; i < BUFFERPOOL_NAMES.length; i++)
			maxBufferPoolSizes.put(BUFFERPOOL_NAMES[i], BUFFERPOOL_SIZES[i]);		
		
		CatalogManager catalogManager = new CatalogManagerImpl(CATALOGFILEPATH, FILEPATHPREFIX);
		
		try {
			catalogManager.loadCatalog();
		} catch (CatalogManagerException e1) {
			// 
			e1.printStackTrace();
		}
		
		//TableId tid=new TableId("Cursadas");
		//TableDescriptor t=catalogManager.getTableDescriptorByTableId(tid);
		//System.out.println(t.getTablePool());
		
		BufferPool multipleBufferPool = new MultipleBufferPool(maxBufferPoolSizes, pageReplacementStrategies, catalogManager);
		FaultCounterDiskManagerSpy faultCounterDiskManagerSpy = new FaultCounterDiskManagerSpy();
		BufferManager bufferManager = new BufferManagerImpl(faultCounterDiskManagerSpy, catalogManager, multipleBufferPool);
		
		PageReferenceTrace trace = null;
		try {
			trace = getTrace(traceFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			evaluate(traceFileName, bufferManager);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			evaluate(traceFileName, bufferManager);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		BufferManagementMetrics metrics = new BufferManagementMetrics(trace, faultCounterDiskManagerSpy.getFaultsCount());
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
