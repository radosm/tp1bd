package ubadb.external.catalog;

import java.util.ArrayList;
import java.util.List;
import ubadb.core.common.TableId;
import ubadb.core.components.catalogManager.Catalog;
import ubadb.core.components.catalogManager.TableDescriptor;
import ubadb.core.util.xml.XstreamXmlUtil;
import com.thoughtworks.xstream.XStream;

public class CatalogGenerator
{
	public static void main(String[] args) throws Exception
	{
    	XstreamXmlUtil xmlUtil = new XstreamXmlUtil(new XStream());
    	List<TableDescriptor> list = new ArrayList<TableDescriptor>(); 
    	
    	addTableDescriptors(list);
    	
    	Catalog tableCatalog=new Catalog(list);
    	//TableCatalog catalogTable = new TableCatalog(list);
    	xmlUtil.toXml(tableCatalog,"generated/catalog.db");
    	
    	System.out.println("CATALOG GENERATED");
	}
//
//	//ADD HERE
	private static void addTableDescriptors(List<TableDescriptor> list)
	{
		TableId tableId;
		String tableName;
		String tablePath;
		String tablePool;
		
		//Table Student
		tableId = new TableId("student.table");
		tableName = "Student";
		tablePath = "/ruta";
		tablePool = "DEFAULT";
		list.add(new TableDescriptor(tableId,tableName,tablePath,tablePool));
    	
    	//Table University
//    	
	}

}
