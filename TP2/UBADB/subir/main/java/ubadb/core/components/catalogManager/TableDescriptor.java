package ubadb.core.components.catalogManager;

import ubadb.core.common.TableId;

public class TableDescriptor
{
	private TableId tableId;
	private String tableName;
	private String tablePath;
	private String tablePool;

	public TableDescriptor(TableId tableId, String tableName, String tablePath, String tablePool)
	{
		this.tableId = tableId;
		this.tableName = tableName;
		this.tablePath = tablePath;
		this.tablePool = tablePool;
	}

	public TableId getTableId()
	{
		return tableId;
	}

	public String getTableName()
	{
		return tableName;
	}

	public String getTablePath()
	{
		return tablePath;
	}
	
	public String getTablePool()
	{
		return tablePool;
	}
}
