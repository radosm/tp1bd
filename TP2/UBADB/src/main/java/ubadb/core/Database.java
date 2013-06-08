package ubadb.core;

import ubadb.core.components.bufferManager.BufferManager;
import ubadb.core.components.catalogManager.CatalogManager;
import ubadb.core.components.diskManager.DiskManager;

public class Database
{
	private CatalogManager catalogManager;
	private DiskManager diskManager;
	private BufferManager bufferManager;
	
	public Database(CatalogManager catalogManager, DiskManager diskManager, BufferManager bufferManager)
	{
		this.catalogManager = catalogManager;
		this.diskManager = diskManager;
		this.bufferManager = bufferManager;
	}

	public CatalogManager getCatalogManager()
	{
		return catalogManager;
	}

	public DiskManager getDiskManager()
	{
		return diskManager;
	}

	public BufferManager getBufferManager()
	{
		return bufferManager;
	}
}
