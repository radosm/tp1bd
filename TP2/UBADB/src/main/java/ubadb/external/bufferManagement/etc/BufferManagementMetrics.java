package ubadb.external.bufferManagement.etc;


public class BufferManagementMetrics
{
	private PageReferenceTrace trace;
	private int faultsCount;
	
	public BufferManagementMetrics(PageReferenceTrace trace, int faultsCount)
	{
		this.trace = trace;
		this.faultsCount = faultsCount;
	}

	private int countRequests()
	{
		int ret = 0;
		for(PageReference reference : trace.getPageReferences())
		{
			if(PageReferenceType.REQUEST.equals(reference.getType()))
				ret++;
		}
		
		return ret;
	}
	
	private double calculateHitRate()
	{
		return (double)(countRequests() - faultsCount)/(double)countRequests();
	}

	public void showSummary()
	{
		System.out.println("Hit rate: " + calculateHitRate());
	}
}
