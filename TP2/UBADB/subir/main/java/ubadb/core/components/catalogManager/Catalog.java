package ubadb.core.components.catalogManager;

import java.util.List;

import ubadb.core.common.TableId;


/**
 * Serializable class that represents the catalog
 * 
 */
public class Catalog
{
	private List<TableDescriptor> tableDescriptors;
	
	public Catalog(List<TableDescriptor> tableDescriptors)
	{
		this.tableDescriptors=tableDescriptors;
	}
	
	public TableDescriptor getTableDescriptorByTableId(TableId tableId)
	{
		for (TableDescriptor td: tableDescriptors) 
		{
			if (td.getTableId().equals(tableId))
			{
				return td;
			}
		}
		return null;
	}
}
