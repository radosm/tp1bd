package ubadb.core.components.catalogManager;

@SuppressWarnings("serial")
public class CatalogManagerException extends Exception
{
	public CatalogManagerException(String message)
	{
		super(message);
	}
	
	public CatalogManagerException(String message, Exception e)
	{
		super(message,e);
	}
}
