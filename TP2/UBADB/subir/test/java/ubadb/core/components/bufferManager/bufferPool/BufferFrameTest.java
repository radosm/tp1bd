package ubadb.core.components.bufferManager.bufferPool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ubadb.core.components.bufferManager.bufferPool.BufferFrame;
import ubadb.core.testDoubles.DummyObjectFactory;

public class BufferFrameTest
{
	@Test
	public void testFrameIsNotDirtyAndWithoutPinsByDefault()
	{
		BufferFrame bufferFrame = new BufferFrame(DummyObjectFactory.PAGE);
		
		assertFalse(bufferFrame.isDirty());
		assertEquals(0, bufferFrame.getPinCount());
	}
	
	@Test
	public void testCanBeReplacedTrue()
	{
		BufferFrame bufferFrame = new BufferFrame(DummyObjectFactory.PAGE);
		
		assertTrue(bufferFrame.canBeReplaced());
	}
	
	@Test
	public void testCanBeReplacedFalse()
	{
		BufferFrame bufferFrame = new BufferFrame(DummyObjectFactory.PAGE);
		bufferFrame.pin();
		
		assertFalse(bufferFrame.canBeReplaced());
	}
	
	@Test
	public void testUnpinPinnedPage() throws Exception
	{
		BufferFrame bufferFrame = new BufferFrame(DummyObjectFactory.PAGE);
		bufferFrame.pin();
		bufferFrame.pin();
		
		bufferFrame.unpin();
		assertEquals(1, bufferFrame.getPinCount());
	}
	
	@Test(expected=BufferFrameException.class)	
	public void testUnpinUnpinnedPage() throws Exception
	{
		BufferFrame bufferFrame = new BufferFrame(DummyObjectFactory.PAGE);

		bufferFrame.unpin();
	}
}
