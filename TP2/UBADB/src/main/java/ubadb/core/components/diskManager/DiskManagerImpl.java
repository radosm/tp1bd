package ubadb.core.components.diskManager;

import java.io.IOException;
import java.io.RandomAccessFile;

import org.springframework.core.io.ClassPathResource;

import ubadb.core.common.Page;
import ubadb.core.common.PageId;
import ubadb.core.common.TableId;
import ubadb.core.components.catalogManager.CatalogManager;

public class DiskManagerImpl implements DiskManager
{
	private CatalogManager catalogManager;
	private final int pageSize;
	
	public DiskManagerImpl(CatalogManager catalogManager, int pageSize)
	{
		this.catalogManager = catalogManager;
		this.pageSize = pageSize;
	}
	
	public Page readPage(PageId pageId) throws DiskManagerException
	{
		try
		{
			RandomAccessFile file = getFile(pageId.getTableId());
			long offset = calculatePageOffset(pageId);
			
			checkPageIsExisting(offset, file.length());
			
			byte[] pageContents = new byte[pageSize];
			file.seek(offset);
			file.read(pageContents);
			file.close();
			
			return new Page(pageId,pageContents);
		}
		catch(Exception e)
		{
			throw new DiskManagerException("Cannot read page from disk",e);
		}
	}

	public void writeExistingPage(Page page) throws DiskManagerException
	{
		try
		{
			RandomAccessFile file = getFile(page.getPageId().getTableId());
			long offset = calculatePageOffset(page.getPageId());
			
			checkPageIsExisting(offset, file.length());
			checkPageSize(page.getPageContents());
			
			file.seek(offset);
			file.write(page.getPageContents());
			file.close();
		}
		catch(Exception e)
		{
			throw new DiskManagerException("Cannot write to page to disk",e);
		}
	}

	public Page createNewPage(TableId tableId, byte[] pageContents) throws DiskManagerException
	{
		try
		{
			checkPageSize(pageContents);
			
			RandomAccessFile file = getFile(tableId);
			long offset = file.length();
			
			file.seek(offset);
			file.write(pageContents);
			file.close();
			
			int newPageNumber = calculateNewPageNumber(offset);
			
			return new Page(new PageId(newPageNumber, tableId), pageContents);
		}
		catch(Exception e)
		{
			throw new DiskManagerException("Cannot create a new page on disk",e);
		}
	}

	private RandomAccessFile getFile(TableId tableId) throws IOException
	{
		ClassPathResource resource = new ClassPathResource(getFileFullPath(tableId));
		return new RandomAccessFile(resource.getFile(),"rw");	//rw = "read-write" mode
	}
	
	protected long calculatePageOffset(PageId pageId)
	{
		return (long)pageSize * (long)pageId.getNumber();
	}

	protected int calculateNewPageNumber(long fileLength)
	{
		return (int) Math.floor(fileLength / (long)pageSize);
	}
	
	private String getFileFullPath(TableId tableId)
	{
		return catalogManager.getTableDescriptorByTableId(tableId).getTablePath();
	}
	
	protected void checkPageIsExisting(long offset, long length) throws DiskManagerException
	{
		if(!((offset + pageSize) <= length))
			throw new DiskManagerException("The page requested doesn't exist in the file");
	}
	
	protected void checkPageSize(byte[] pageContents) throws DiskManagerException
	{
		if(pageContents.length != pageSize)
			throw new DiskManagerException("This page has a different page size from the one configured in DB");
	}
}
