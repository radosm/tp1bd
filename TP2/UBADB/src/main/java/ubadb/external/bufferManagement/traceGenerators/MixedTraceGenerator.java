package ubadb.external.bufferManagement.traceGenerators;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import ubadb.external.bufferManagement.etc.PageReferenceTrace;

public class MixedTraceGenerator extends PageReferenceTraceGenerator
{
	public PageReferenceTrace generateMixedTrace(List<PageReferenceTrace> traces, int totalTracesCount, int maxConcurrentTracesCount)
	{
		PageReferenceTrace mixedTrace = new PageReferenceTrace();

		//Use a queue to manipulate traces easily
		Queue<PageReferenceTrace> totalTracesQueue = new LinkedList<>(traces);
		
		//Get initial concurrent traces
		List<PageReferenceTrace> concurrentTraces = new ArrayList<>();
		for(int i=0; i < maxConcurrentTracesCount; i++)
			concurrentTraces.add(totalTracesQueue.poll());
			
		Random randomPickTraceToMix = new Random(System.currentTimeMillis());
		
		while(!concurrentTraces.isEmpty())
		{
			//Pick any concurrent trace
			int anyIndex = randomPickTraceToMix.nextInt(concurrentTraces.size());
			PageReferenceTrace anyTrace = concurrentTraces.get(anyIndex);
			
			//Take its first step, put it into the mixed trace and remove it from its original trace
			mixedTrace.addPageReference(anyTrace.getPageReferences().get(0));
			anyTrace.getPageReferences().remove(0);
			
			//If the trace has no more steps...
			if(anyTrace.getPageReferences().isEmpty())
			{
				//Remove it and get the next one from the total traces (if there is any!)
				concurrentTraces.remove(anyIndex);
				
				if(totalTracesQueue.peek() != null)
					concurrentTraces.add(totalTracesQueue.poll());
			}
		}
		
		return mixedTrace;
	}
}
