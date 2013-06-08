package ubadb.external.bufferManagement.traceGenerators;

import java.util.List;

import ubadb.core.common.PageId;
import ubadb.core.common.TransactionId;
import ubadb.external.bufferManagement.etc.PageReferenceTrace;
import ubadb.external.bufferManagement.etc.PageReferenceType;

public class BNLJTraceGenerator extends PageReferenceTraceGenerator
{
	/**
	 * @param groupSize represent how many pages from the outer file are read on each BNLJ iteration 
	 */
	public PageReferenceTrace generateBNLJ(long transactionNumber, String tableNameOuter, int pageCountOuter, String tableNameInner, int pageCountInner, int groupSize)
	{
		PageReferenceTrace ret = new PageReferenceTrace();
		int groups = (int)Math.ceil((double)pageCountOuter/(double)groupSize);
		int offset = 0;
		for(int i=0; i < groups; i++)
		{
			List<PageId> outerBlock = generateSequentialPages(tableNameOuter, offset, Math.min(offset + groupSize, pageCountOuter));
			
			ret.concatenate(build(new TransactionId(transactionNumber), outerBlock,PageReferenceType.REQUEST));
			ret.concatenate(buildRequestAndRelease(new TransactionId(transactionNumber), generateSequentialPages(tableNameInner,0, pageCountInner)));
			ret.concatenate(build(new TransactionId(transactionNumber), outerBlock,PageReferenceType.RELEASE));
			
			offset+=groupSize;
		}
		return ret;
	}
}
