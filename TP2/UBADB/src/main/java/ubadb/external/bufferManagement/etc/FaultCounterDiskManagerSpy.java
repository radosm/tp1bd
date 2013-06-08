package ubadb.external.bufferManagement.etc;

import java.util.ArrayList;
import java.util.List;

import ubadb.core.common.Page;
import ubadb.core.common.PageId;
import ubadb.core.common.TableId;
import ubadb.core.components.diskManager.DiskManager;
import ubadb.core.components.diskManager.DiskManagerException;

public class FaultCounterDiskManagerSpy implements DiskManager 
{
	private List<PageId> pageIdsWithFault;
	
	public FaultCounterDiskManagerSpy()
	{
		pageIdsWithFault = new ArrayList<>();
	}
	
	public Page readPage(PageId pageId) throws DiskManagerException
	{
		pageIdsWithFault.add(pageId);
		
		return new Page(pageId, "aaa".getBytes());
	}

	public void writeExistingPage(Page page) throws DiskManagerException
	{
		throw new DiskManagerException("This method should not be called");
	}

	public Page createNewPage(TableId tableId, byte[] pageContents) throws DiskManagerException
	{
		throw new DiskManagerException("This method should not be called");
	}

	public int getFaultsCount()
	{
		return pageIdsWithFault.size();
	}
}
