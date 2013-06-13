package ubadb.core.components.catalogManager;

import ubadb.core.common.TableId;

public interface CatalogManager
{
	void loadCatalog() throws CatalogManagerException;
	TableDescriptor getTableDescriptorByTableId(TableId tableId);
}