package ubadb.core.components.bufferManager.bufferPool;

@SuppressWarnings("serial")
public class BufferFrameException extends Exception
{
	public BufferFrameException(String message)
	{
		super(message);
	}
	
	public BufferFrameException(String message, Exception e)
	{
		super(message,e);
	}
}
