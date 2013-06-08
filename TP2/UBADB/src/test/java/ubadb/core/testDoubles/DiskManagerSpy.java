package ubadb.core.testDoubles;

import java.util.HashSet;
import java.util.Set;

import ubadb.core.common.Page;
import ubadb.core.common.PageId;
import ubadb.core.common.TableId;
import ubadb.core.components.diskManager.DiskManager;
import ubadb.core.components.diskManager.DiskManagerException;

public class DiskManagerSpy implements DiskManager
{
	private Page singlePage;
	private Set<Page> writtenPages;

	public DiskManagerSpy()
	{
		writtenPages = new HashSet<Page>();
	}
	
	public void setSinglePage(Page page)
	{
		singlePage = page;
	}
	
	public Page readPage(PageId pageId) throws DiskManagerException
	{
		if(singlePage!=null && pageId.equals(singlePage.getPageId()))
			return singlePage;
		else
			throw new DiskManagerException("Error");
	}

	public void writeExistingPage(Page page) throws DiskManagerException
	{
		writtenPages.add(page);
	}

	public Page createNewPage(TableId tableId, byte[] pageContents) throws DiskManagerException
	{
		throw new DiskManagerException("Error");
	}
	
	public Set<Page> getWrittenPages()
	{
		return writtenPages;
	}
}
