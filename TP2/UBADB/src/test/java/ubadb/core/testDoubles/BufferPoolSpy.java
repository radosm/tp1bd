package ubadb.core.testDoubles;

import java.util.ArrayList;
import java.util.List;

import ubadb.core.common.Page;
import ubadb.core.common.PageId;
import ubadb.core.components.bufferManager.bufferPool.BufferFrame;
import ubadb.core.components.bufferManager.bufferPool.BufferPool;
import ubadb.core.components.bufferManager.bufferPool.BufferPoolException;

public class BufferPoolSpy implements BufferPool
{
	private List<BufferFrame> frames;
	private int maxSize;
	private PageId victimPageId;
	
	public BufferPoolSpy(int maxSize)
	{
		this.maxSize = maxSize;
		this.frames = new ArrayList<BufferFrame>();
	}
	
	public boolean isPageInPool(PageId pageId)
	{
		return findById(pageId)!= null;
	}

	private BufferFrame findById(PageId pageId)
	{
		BufferFrame ret = null;
		
		for(BufferFrame frame : frames)
		{
			if(frame.getPage().getPageId().equals(pageId))
			{
				ret = frame;
				break;
			}
		}
		
		return ret;
	}

	public BufferFrame getBufferFrame(PageId pageId) throws BufferPoolException
	{
		if(isPageInPool(pageId))
			return findById(pageId);
		else 
			throw new BufferPoolException("ERROR");
	}

	public boolean hasSpace(PageId pageToAddId)
	{
		return countPagesInPool() < maxSize;
	}

	public BufferFrame addNewPage(Page page) throws BufferPoolException
	{
		if(hasSpace(page.getPageId()))
		{
			BufferFrame frame = new BufferFrame(page);
			frames.add(frame);
			return frame;
		}
		else 
			throw new BufferPoolException("ERROR");
	}

	public void removePage(PageId pageId) throws BufferPoolException
	{
		if(isPageInPool(pageId))
			frames.remove(findById(pageId));
		else 
			throw new BufferPoolException("ERROR");
	}

	public BufferFrame findVictim(PageId pageIdToBeAdded) throws BufferPoolException
	{
		return findById(victimPageId);
	}

	public int countPagesInPool()
	{
		return frames.size();
	}
	
	public void setVictim(PageId pageId)
	{
		this.victimPageId = pageId;
	}
}
