package ubadb.external.bufferManagement.etc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;

import ubadb.core.common.PageId;
import ubadb.core.common.TableId;
import ubadb.core.common.TransactionId;

public class PageReferenceTraceSerializer
{
	private static final String CHARSET = "UTF-8";
	private static final String	COMMENT_IDENTIFIER	= "#";
	private static final String	TRANSACTION_SEPARATOR = ":";
	private static final String	TYPE_SEPARATOR	= " ";
	private static final String	TABLE_SEPARATOR	= ",";
	
	public void write(PageReferenceTrace trace, String fileName) throws Exception
	{
		Path path = Paths.get(fileName);
		
		try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName(CHARSET))) 
		{
			for(PageReference reference : trace.getPageReferences())
			{
				writer.write(convertPageReferenceToString(reference) + "\n");
			}
		} 
		catch (Exception e) 
		{
			throw new Exception("Cannot write a trace into a file",e);
		}
	}
	
	public PageReferenceTrace read(String fileName) throws Exception
	{
		PageReferenceTrace trace = new PageReferenceTrace();
		
		Path path = Paths.get(fileName);
		try(BufferedReader reader = Files.newBufferedReader(path,Charset.forName(CHARSET)))
		{
		    String line = reader.readLine();
		    while (line != null)
		    {
		        processLine(line,trace);
		        line = reader.readLine();
		    }
		} 
		catch (Exception e) 
		{
		    throw new Exception("Cannot read trace file",e);
		}
		
		return trace;
	}

	private String convertPageReferenceToString(PageReference reference)
	{
		String ret = reference.getTransactionId().toString() + TRANSACTION_SEPARATOR; 
		ret += reference.getType().toString() + TYPE_SEPARATOR;
		ret += reference.getPageId().getTableId().getInternalName() + TABLE_SEPARATOR;
		ret += reference.getPageId().getNumber();
		return  ret;
	}

	private void processLine(String line, PageReferenceTrace trace)
	{
		if(!isComment(line) && !line.trim().isEmpty())
		{
			trace.addPageReference(convertPageReferenceFromString(line));
		}
	}

	private boolean isComment(String line)
	{
		return line.startsWith(COMMENT_IDENTIFIER);
	}
	
	private PageReference convertPageReferenceFromString(String line)
	{
		StringTokenizer tokenizer1= new StringTokenizer(line, TRANSACTION_SEPARATOR);
		String transactionId = tokenizer1.nextToken();

		StringTokenizer tokenizer2= new StringTokenizer(tokenizer1.nextToken(), TYPE_SEPARATOR);
		String type = tokenizer2.nextToken();
		
		StringTokenizer tokenizer3 = new StringTokenizer(tokenizer2.nextToken(),TABLE_SEPARATOR);
		String tableName = tokenizer3.nextToken();
		String pageNumber = tokenizer3.nextToken();
		
		return new PageReference(new TransactionId(Long.valueOf(transactionId)), new PageId(Integer.valueOf(pageNumber), new TableId(tableName)), PageReferenceType.valueOf(type));
	}
}
