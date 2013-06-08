package ubadb.core.components.catalogManager;

import ubadb.core.common.PageId;
import ubadb.core.common.TableId;

@SuppressWarnings("unused")
public class CatalogManagerImpl implements CatalogManager
{
	private String catalogFilePath;
	private String filePathPrefix;
	private Catalog catalog;
	
	public CatalogManagerImpl(String catalogFilePath, String filePathPrefix) 
	{
		this.catalogFilePath = catalogFilePath;
		this.filePathPrefix = filePathPrefix;
	}

	@Override
	public void loadCatalog() throws CatalogManagerException
	{
		//TODO Completar levantando desde un XML el catálogo
	}


	@Override
	public TableDescriptor getTableDescriptorByTableId(TableId tableId)
	{
		return catalog.getTableDescriptorByTableId(tableId);
	}

	/* 
	 * Este método debería devolver el nombre del pool de una página.
	 * Si la página no está debería tirar una excepción.
	 */
	@Override
	public String getPoolByPageId(PageId pageId) throws CatalogManagerException {
		// TODO Auto-generated method stub
		return null;
	}
}
