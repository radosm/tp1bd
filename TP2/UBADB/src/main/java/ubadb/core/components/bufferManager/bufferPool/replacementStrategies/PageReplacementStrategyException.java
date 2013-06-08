package ubadb.core.components.bufferManager.bufferPool.replacementStrategies;

@SuppressWarnings("serial")
public class PageReplacementStrategyException extends Exception
{
	public PageReplacementStrategyException(String message)
	{
		super(message);
	}
	
	public PageReplacementStrategyException(String message, Exception e)
	{
		super(message,e);
	}
}
