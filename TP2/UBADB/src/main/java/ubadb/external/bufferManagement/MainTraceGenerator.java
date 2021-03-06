package ubadb.external.bufferManagement;

import java.nio.file.Paths;
import java.util.ArrayList;
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
public class MainTraceGenerator
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
		String fileNameA2 = "generated/trazas/01-indexScanUnclustered-Materias.trace";
		PageReferenceTrace traceA2 = new IndexScanTraceGenerator().generateIndexScanUnclusteredForASingleLeaf(1,"Materias", 3, 800, 40);
		serialize(fileNameA2, traceA2, serializer);
			
		// Filescan de una tabla grande
		String fileNameA1 = "generated/trazas/02-fileScan-Cursadas.trace";
		PageReferenceTrace traceA1 = new FileScanTraceGenerator().generateFileScan(1, "Cursadas", 250);
		serialize(fileNameA1, traceA1, serializer);
		
		// Lectura aleatoria de tabla Estudiantes
		String fileNameA3 = "generated/trazas/03-indexScanUnclustered-Estudiantes.trace";
		PageReferenceTrace traceA3 = new IndexScanTraceGenerator().generateIndexScanUnclusteredForASingleLeaf(1,"Estudiantes", 3, 300, 100);
		serialize(fileNameA3, traceA3, serializer);
		
		// Lectura aleatoria de tabla Materias
		String fileNameA4 = "generated/trazas/04-indexScanUnclustered-Materias.trace";
		PageReferenceTrace traceA4 = new IndexScanTraceGenerator().generateIndexScanUnclusteredForASingleLeaf(1,"Materias", 3, 800, 40);
		serialize(fileNameA4, traceA4, serializer);
		
		// Lectura aleatoria de tabla Estudiantes
		String fileNameA5 = "generated/trazas/05-indexScanUnclustered-Estudiantes.trace";
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
		
		String folderA = "generated/trazas";
		String fileA = "generated/prueba.trace";
		mixTraces(fileA,folderA,15,5,serializer);
	}

	private static void mixTraces(String fileNameForNewTrace, String folderName, int totalTracesCount, int maxConcurrentTracesCount, PageReferenceTraceSerializer serializer) throws Exception
	{
		List<PageReferenceTrace> tracesToMix = buildTracesToMix(folderName,totalTracesCount,serializer);
		
		PageReferenceTrace mixedTrace = new MixedTraceGenerator().generateMixedTrace(tracesToMix, totalTracesCount, maxConcurrentTracesCount);
		
		serializer.write(mixedTrace, fileNameForNewTrace);
		System.out.println("File " + fileNameForNewTrace + " generated");
	}

	private static List<PageReferenceTrace> buildTracesToMix(String folderName, int totalTracesCount, PageReferenceTraceSerializer serializer) throws Exception
	{
		List<PageReferenceTrace> tracesToMix = new ArrayList<>();
		Random random = new Random(System.currentTimeMillis());
		
		String[] traceFiles = Paths.get(folderName).toFile().list();
		
		for(int i=0; i < totalTracesCount; i++)
		{
			int anyTraceIndex = random.nextInt(traceFiles.length); 
			PageReferenceTrace anyTrace = serializer.read(folderName + "/" + traceFiles[anyTraceIndex]);
			anyTrace.changeTransactionId(new TransactionId(i));
			
			tracesToMix.add(anyTrace);
		}
		
		return tracesToMix;
	}
}
