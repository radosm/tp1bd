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

	/**
	 * Carga el catálogo del archivo indicado en el constructor usando XStream.
	 * 
	 * @throws CatalogManagerException si el archivo no es un catálogo válido.
	 */
	@Override
	public void loadCatalog() throws CatalogManagerException
	{
		XstreamXmlUtil xmlUtil = new XstreamXmlUtil(new XStream());
		try {
			this.catalog= (Catalog)xmlUtil.fromXml(this.catalogFilePath);
		} catch (XmlUtilException e) {
			throw new CatalogManagerException("No se pudo leer el catálogo",e);
		}
	}


	/** 
	 * Retorna el descriptor de una tabla 
	 * 
	 * @param tableId id de la tabla
	 * @return TableDescriptor de la tabla
	 */
	@Override
	public TableDescriptor getTableDescriptorByTableId(TableId tableId)
	{
		return catalog.getTableDescriptorByTableId(tableId);
	}
}
