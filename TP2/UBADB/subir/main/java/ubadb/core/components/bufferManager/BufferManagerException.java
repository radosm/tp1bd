package ubadb.core.components.bufferManager;

@SuppressWarnings("serial")
public class BufferManagerException extends Exception
{
	public BufferManagerException(String message)
	{
		super(message);
	}
	
	public BufferManagerException(String message, Exception e)
	{
		super(message,e);
	}
}
