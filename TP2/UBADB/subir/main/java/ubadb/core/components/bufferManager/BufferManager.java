package ubadb.core.components.bufferManager;

import ubadb.core.common.Page;
import ubadb.core.common.PageId;
import ubadb.core.common.TableId;

public interface BufferManager
{
	Page readPage(PageId pageId) throws BufferManagerException; 
	Page createNewPage(TableId tableId, byte[] pageContents) throws BufferManagerException;
	void releasePage(PageId pageId) throws BufferManagerException;
}