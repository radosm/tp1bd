package ubadb.core.components.diskManager;

import ubadb.core.common.Page;
import ubadb.core.common.PageId;
import ubadb.core.common.TableId;

public interface DiskManager
{
	Page readPage(PageId pageId) throws DiskManagerException;
	void writeExistingPage(Page page) throws DiskManagerException;
	Page createNewPage(TableId tableId, byte[] pageContents) throws DiskManagerException;
}