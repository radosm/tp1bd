package ubadb.external.bufferManagement;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ubadb.core.common.TransactionId;
import ubadb.external.bufferManagement.etc.PageReferenceTrace;
import ubadb.external.bufferManagement.etc.PageReferenceTraceSerializer;
import ubadb.external.bufferManagement.traceGenerators.BNLJTraceGenerator;
import ubadb.external.bufferManagement.traceGenerators.FileScanTraceGenerator;
import ubadb.external.bufferManagement.traceGenerators.IndexScanTraceGenerator;
import ubadb.external.bufferManagement.traceGenerators.MixedTraceGenerator;

@SuppressWarnings("unused")
public class SeqMainTraceGenerator
{
	public static void main(String[] args) throws Exception
	{
		basicDataSet();
		complexDataSet();
	}
	
	private static void basicDataSet() throws Exception
	{
		PageReferenceTraceSerializer serializer = new PageReferenceTraceSerializer();
		
		// Lectura aleatoria de tabla Materias
		String fileNameA2 = "generated_seq/trazas/01-indexScanUnclustered-Materias.trace";
		PageReferenceTrace traceA2 = new IndexScanTraceGenerator().generateIndexScanUnclusteredForASingleLeaf(1,"Materias", 3, 800, 40);
		serialize(fileNameA2, traceA2, serializer);
		
		// Filescan de una tabla grande
		String fileNameA1 = "generated_seq/trazas/02-fileScan-Cursadas.trace";
		PageReferenceTrace traceA1 = new FileScanTraceGenerator().generateFileScan(1, "Cursadas", 250);
		serialize(fileNameA1, traceA1, serializer);
		
		// Lectura aleatoria de tabla Estudiantes
		String fileNameA3 = "generated_seq/trazas/03-indexScanUnclustered-Estudiantes.trace";
		PageReferenceTrace traceA3 = new IndexScanTraceGenerator().generateIndexScanUnclusteredForASingleLeaf(1,"Estudiantes", 3, 300, 100);
		serialize(fileNameA3, traceA3, serializer);
		
		// Lectura aleatoria de tabla Materias
		String fileNameA4 = "generated_seq/trazas/04-indexScanUnclustered-Materias.trace";
		PageReferenceTrace traceA4 = new IndexScanTraceGenerator().generateIndexScanUnclusteredForASingleLeaf(1,"Materias", 3, 800, 40);
		serialize(fileNameA4, traceA4, serializer);
		
		// Lectura aleatoria de tabla Estudiantes
		String fileNameA5 = "generated_seq/trazas/05-indexScanUnclustered-Estudiantes.trace";
		PageReferenceTrace traceA5 = new IndexScanTraceGenerator().generateIndexScanUnclusteredForASingleLeaf(1,"Estudiantes", 3, 300, 100);
		serialize(fileNameA5, traceA5, serializer);
		
	}
	
	private static void serialize(String fileName, PageReferenceTrace trace, PageReferenceTraceSerializer serializer) throws Exception
	{
		serializer.write(trace, fileName);
		System.out.println("File '" + fileName + "' generated!!");
	}
	
	private static void complexDataSet() throws Exception
	{
		PageReferenceTraceSerializer serializer = new PageReferenceTraceSerializer();
		String folderA = "generated_seq/trazas";
		String fileA = "generated_seq/prueba.trace";
		mixTraces(fileA,folderA,serializer);
	}

	private static void mixTraces(String fileNameForNewTrace, String folderName, PageReferenceTraceSerializer serializer) throws Exception
	{
		List<PageReferenceTrace> tracesToMix = buildTracesToMix(folderName,serializer);
	
		PageReferenceTrace result = new PageReferenceTrace();
		
		while(!tracesToMix.isEmpty())
		{
			//Pick any concurrent trace
			PageReferenceTrace trace = tracesToMix.get(0);
			
			while (!trace.getPageReferences().isEmpty()) {
				result.addPageReference(trace.getPageReferences().get(0));
				trace.getPageReferences().remove(0);
			}
			
			tracesToMix.remove(0);
			
//			if(totalTracesQueue.peek() != null)
//				concurrentTraces.add(totalTracesQueue.poll());
//			}
		}
		
		
		
		
//		PageReferenceTrace mixedTrace = new MixedTraceGenerator().generateMixedTrace(tracesToMix, totalTracesCount, maxConcurrentTracesCount);
		
		serializer.write(result, fileNameForNewTrace);
		System.out.println("File " + fileNameForNewTrace + " generated");
	}

	private static List<PageReferenceTrace> buildTracesToMix(String folderName, PageReferenceTraceSerializer serializer) throws Exception
	{
		List<PageReferenceTrace> tracesToMix = new ArrayList<>();
		List<String> traceFiles = new ArrayList<String>();
		
		for (String file : Paths.get(folderName).toFile().list()) {
			traceFiles.add(file);
		}
		Collections.sort(traceFiles);
		
		for(int i=0; i < traceFiles.size(); i++)
		{ 
			PageReferenceTrace anyTrace = serializer.read(folderName + "/" + traceFiles.get(i));
			anyTrace.changeTransactionId(new TransactionId(i));
			
			tracesToMix.add(anyTrace);
		}
		
		return tracesToMix;
	}
}
