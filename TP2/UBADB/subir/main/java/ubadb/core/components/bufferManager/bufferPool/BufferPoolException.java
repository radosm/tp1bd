package ubadb.core.components.bufferManager.bufferPool;

@SuppressWarnings("serial")
public class BufferPoolException extends Exception
{
	public BufferPoolException(String message)
	{
		super(message);
	}
	
	public BufferPoolException(String message, Exception e)
	{
		super(message,e);
	}
}
