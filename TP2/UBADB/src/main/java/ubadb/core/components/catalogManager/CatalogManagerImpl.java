package ubadb.core.components.catalogManager;

import com.thoughtworks.xstream.XStream;

import ubadb.core.common.TableId;
import ubadb.core.util.xml.XmlUtilException;
import ubadb.core.util.xml.XstreamXmlUtil;

@SuppressWarnings("unused")
public class CatalogManagerImpl implements CatalogManager
{
	private String catalogFilePath;
	private String filePathPrefix;
	private Catalog catalog;
	
	public CatalogManagerImpl(String catalogFilePath, String filePathPrefix) 
	{
		this.catalogFilePath = catalogFilePath;
		this.filePathPrefix = filePathPrefix;
	}

	@Override
	public void loadCatalog() throws CatalogManagerException
	{
		XstreamXmlUtil xmlUtil = new XstreamXmlUtil(new XStream());
		try {
			this.catalog=(Catalog)xmlUtil.fromXml(this.catalogFilePath);
		} catch (XmlUtilException e) {
			throw new CatalogManagerException("No se pudo leer el catálogo",e);
		}
	}


	@Override
	public TableDescriptor getTableDescriptorByTableId(TableId tableId)
	{
		return catalog.getTableDescriptorByTableId(tableId);
	}
}
