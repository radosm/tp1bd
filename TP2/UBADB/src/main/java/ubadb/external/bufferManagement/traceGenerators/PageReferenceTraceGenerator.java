package ubadb.external.bufferManagement.traceGenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ubadb.core.common.PageId;
import ubadb.core.common.TableId;
import ubadb.core.common.TransactionId;
import ubadb.external.bufferManagement.etc.PageReference;
import ubadb.external.bufferManagement.etc.PageReferenceTrace;
import ubadb.external.bufferManagement.etc.PageReferenceType;

public class PageReferenceTraceGenerator
{
	protected List<PageId> generateSequentialPages(String tableName, int from, int to)
	{
		List<PageId> ret = new ArrayList<>();
		
		for(int i=from; i < to; i++)
			ret.add(new PageId(i, new TableId(tableName)));
		
		return ret;
	}
	
	protected List<PageId> generateRandomPages(String tableName, int pageCount, int fileTotalPagesCount)
	{
		List<PageId> ret = new ArrayList<>();
		Random random = new Random(System.currentTimeMillis());
		
		for(int i=0; i < pageCount ; i++)
			ret.add(new PageId(random.nextInt(fileTotalPagesCount), new TableId(tableName)));
		
		return ret;
	}
	
	protected PageReferenceTrace buildRequestAndRelease(TransactionId transactionId, List<PageId> pages)
	{
		PageReferenceTrace ret = new PageReferenceTrace();
		
		for(PageId pageId : pages)
		{
			ret.addPageReference(new PageReference(transactionId, pageId, PageReferenceType.REQUEST));
			ret.addPageReference(new PageReference(transactionId, pageId, PageReferenceType.RELEASE));
		}
		
		return ret;
	}
	
	protected PageReferenceTrace build(TransactionId transactionId, List<PageId> pages, PageReferenceType type)
	{
		PageReferenceTrace ret = new PageReferenceTrace();
		
		for(PageId pageId : pages)
		{
			ret.addPageReference(new PageReference(transactionId, pageId, type));
		}
		
		return ret;
	}
}
