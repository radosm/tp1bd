package ubadb.external.bufferManagement.etc;

import java.util.ArrayList;
import java.util.List;

import ubadb.core.common.TransactionId;

public class PageReferenceTrace
{
	private List<PageReference> pageReferences;

	public PageReferenceTrace()
	{
		pageReferences = new ArrayList<>();
	}

	public void addPageReference(PageReference pageReference)
	{
		pageReferences.add(pageReference);
	}
	
	public List<PageReference> getPageReferences()
	{
		return pageReferences;
	}
	
	public PageReferenceTrace concatenate(PageReferenceTrace otherTrace)
	{
		pageReferences.addAll(otherTrace.pageReferences);
		return this;
	}
	
	public void changeTransactionId(TransactionId newTransactionId)
	{
		for(PageReference pageReference : pageReferences)
			pageReference.setTransactionId(newTransactionId);
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		for(PageReference reference : pageReferences)
		{
			builder.append(reference.toString());
			builder.append('\n');
		}
		
		return builder.toString();
			
	}
}
