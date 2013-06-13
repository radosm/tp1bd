package ubadb.external.bufferManagement.etc;

import ubadb.core.common.PageId;
import ubadb.core.common.TransactionId;

public class PageReference
{
	private TransactionId transactionId;
	private PageId pageId;
	private PageReferenceType type;
	
	public PageReference(TransactionId transactionId, PageId pageId, PageReferenceType type)
	{
		this.transactionId = transactionId;
		this.pageId = pageId;
		this.type = type;
	}

	public TransactionId getTransactionId()
	{
		return transactionId;
	}

	public PageId getPageId()
	{
		return pageId;
	}

	public PageReferenceType getType()
	{
		return type;
	}

	public void setTransactionId(TransactionId transactionId)
	{
		this.transactionId = transactionId;
	}
	
	@Override
	public String toString()
	{
		return transactionId.toString() + ": " + type.toString() + "(" + pageId.toString() + ")";
	}
}
