package ubadb.core.testDoubles;

import ubadb.core.common.Page;
import ubadb.core.common.PageId;
import ubadb.core.common.TableId;

public class DummyObjectFactory
{
	public static final TableId TABLE_ID = new TableId("a");
	public static final PageId PAGE_ID = new PageId(0, TABLE_ID);
	public static final Page PAGE = new Page(PAGE_ID, "abc".getBytes());
}
