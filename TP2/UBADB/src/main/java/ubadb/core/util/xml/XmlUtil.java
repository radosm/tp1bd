package ubadb.core.util.xml;

public interface XmlUtil
{
	void toXml(Object obj, String fileName) throws XmlUtilException;
	Object fromXml(String fileName) throws XmlUtilException;
}
