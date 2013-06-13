package ubadb.core.components.bufferManager.bufferPool.replacementStrategies;

import java.util.Collection;

import ubadb.core.common.Page;
import ubadb.core.components.bufferManager.bufferPool.BufferFrame;

public interface PageReplacementStrategy
{
	/** If no victim is found, throw an exception */
	BufferFrame findVictim(Collection<BufferFrame> bufferFrames) throws PageReplacementStrategyException;
	
	BufferFrame createNewFrame(Page page);
}
