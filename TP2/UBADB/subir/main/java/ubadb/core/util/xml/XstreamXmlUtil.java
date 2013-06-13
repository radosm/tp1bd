package ubadb.core.util.xml;

import java.io.FileReader;
import java.io.FileWriter;

import ubadb.core.common.TableId;

import com.thoughtworks.xstream.XStream;

public class XstreamXmlUtil implements XmlUtil
{
	private XStream xStream;
	
	public XstreamXmlUtil(XStream xStream)
	{
		this.xStream = xStream;
		this.xStream.useAttributeFor(TableId.class, "internalName");
	}

	public void toXml(Object obj, String fileName) throws XmlUtilException
	{
		try
		{
			xStream.toXML(obj, new FileWriter(fileName));
		}
		catch(Exception e)
		{
			throw new XmlUtilException("Cannot object convert to XML",e);
		}
	}

	public Object fromXml(String fileName) throws XmlUtilException
	{
		try
		{
			return xStream.fromXML(new FileReader(fileName));
		}
		catch(Exception e)
		{
			throw new XmlUtilException("Cannot object convert from XML",e);
		}
	}
}
