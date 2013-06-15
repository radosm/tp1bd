package ubadb.core.components.catalogManager;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import ubadb.core.common.TableId;
import ubadb.core.util.xml.XmlUtilException;
import ubadb.core.util.xml.XstreamXmlUtil;

public class CatalogManagerImplTest {
	
	private static CatalogManagerImpl catManager;
	private static Catalog dummyCatalog;
	private static TableId dummyTableId;
	private static TableDescriptor dummyTableDescriptor;
	
	private static final String DUMMY_TABLEID = "student.table";
	private static final String DUMMY_TABLENAME = "Student";
	private static final String DUMMY_TABLEPATH = "/example/path";
	private static final String DUMMY_TABLEPOOL = "POOL_1";
	
	@BeforeClass
	public static void generateCatalog(){
		// Generacion del catalogo
    	XstreamXmlUtil xmlUtil = new XstreamXmlUtil(new XStream());
    	List<TableDescriptor> list = new ArrayList<TableDescriptor>(); 
    		
    	CatalogManagerImplTest.dummyTableId = new TableId(CatalogManagerImplTest.DUMMY_TABLEID);
    	CatalogManagerImplTest.dummyTableDescriptor = new TableDescriptor( CatalogManagerImplTest.dummyTableId,
    																	CatalogManagerImplTest.DUMMY_TABLENAME,
    																	CatalogManagerImplTest.DUMMY_TABLEPATH,
    																	CatalogManagerImplTest.DUMMY_TABLEPOOL); 

    	list.add(CatalogManagerImplTest.dummyTableDescriptor);
     	
    	CatalogManagerImplTest.dummyCatalog = new Catalog(list);

     	try {
			xmlUtil.toXml(dummyCatalog, "generated/catalog.db");
		} catch (XmlUtilException e) {
			e.printStackTrace();
		}		

     	CatalogManagerImplTest.catManager = new CatalogManagerImpl("generated/catalog.db", "dummyfileprefix");    
	}

	@Test
	public void testLoadCatalog() {
			
		try {
			CatalogManagerImplTest.catManager.loadCatalog();
		} catch (CatalogManagerException e) {
			e.printStackTrace();
		}
		 
		TableDescriptor loadedTableDescriptor = CatalogManagerImplTest.catManager.getTableDescriptorByTableId(CatalogManagerImplTest.dummyTableId);
		
		assertTrue(loadedTableDescriptor.getTableId().equals(CatalogManagerImplTest.dummyTableId));
		assertTrue(loadedTableDescriptor.getTableName().equals(CatalogManagerImplTest.DUMMY_TABLENAME));
		assertTrue(loadedTableDescriptor.getTablePath().equals(CatalogManagerImplTest.DUMMY_TABLEPATH));
		assertTrue(loadedTableDescriptor.getTablePool().equals(CatalogManagerImplTest.DUMMY_TABLEPOOL));
	}

	@Test
	public void testGetTableDescriptorByTableId() {
		TableDescriptor tmpTableDescriptor = null; 
		try {
			tmpTableDescriptor = CatalogManagerImplTest.catManager.getTableDescriptorByTableId(CatalogManagerImplTest.dummyTableId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertTrue((tmpTableDescriptor == null) || (tmpTableDescriptor.getTableId().equals(CatalogManagerImplTest.dummyTableId)));
	}

}
