package ubadb.external.bufferManagement.traceGenerators;

import java.util.List;

import ubadb.core.common.PageId;
import ubadb.core.common.TransactionId;
import ubadb.external.bufferManagement.etc.PageReferenceTrace;

public class IndexScanTraceGenerator extends PageReferenceTraceGenerator
{
	public PageReferenceTrace generateIndexScanClustered(long transactionNumber, String tableName, int indexHeight, int referenceStart, int referenceCount)
	{
		List<PageId> indexPages = generateSequentialPages(tableName + "_index", 0, indexHeight);
		List<PageId> filePages = generateSequentialPages(tableName, referenceStart, referenceStart+referenceCount);
		
		indexPages.addAll(filePages);
		
		return buildRequestAndRelease(new TransactionId(transactionNumber), indexPages);
	}
	
	/**
	 *	Generates an index scan trace that reads just a single leaf and its pointers (with an unclustered index, this is not always the case as more leaves may be needed)
	 *	@param referenceCount How many pages are accessed from an index pointer
	 *	@param filePageCount  Total pages of the file being accessed by the index 
	 */
	public PageReferenceTrace generateIndexScanUnclusteredForASingleLeaf(long transactionNumber, String tableName, int indexHeight, int referenceCount, int filePageCount)
	{
		List<PageId> indexPages = generateSequentialPages(tableName + "_index", 0, indexHeight);
		List<PageId> filePages = generateRandomPages(tableName, referenceCount, filePageCount);
		
		indexPages.addAll(filePages);
		
		return buildRequestAndRelease(new TransactionId(transactionNumber), indexPages);
	}
}
