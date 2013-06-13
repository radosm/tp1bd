package ubadb.core.testDoubles;

import java.util.Collection;

import ubadb.core.common.Page;
import ubadb.core.components.bufferManager.bufferPool.BufferFrame;
import ubadb.core.components.bufferManager.bufferPool.replacementStrategies.PageReplacementStrategy;
import ubadb.core.components.bufferManager.bufferPool.replacementStrategies.PageReplacementStrategyException;

public class FakePageReplacementStrategy implements PageReplacementStrategy
{
	private BufferFrame victimFrame;
	
	public void setVictimFrame(BufferFrame victimFrame)
	{
		this.victimFrame = victimFrame;
	}

	public BufferFrame findVictim(Collection<BufferFrame> bufferFrames) throws PageReplacementStrategyException
	{
		if(victimFrame == null)
			throw new IllegalStateException("Method should not be called");
		else
			return victimFrame;
	}

	public BufferFrame createNewFrame(Page page)
	{
		return new BufferFrame(page);
	}
}
