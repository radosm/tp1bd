package ubadb.core.testDoubles;

import java.util.HashMap;
import java.util.Map;

import ubadb.core.util.properties.PropertiesUtil;

public class FakePropertiesUtil implements PropertiesUtil
{
	private Map<String, String> map;
	
	public FakePropertiesUtil()
	{
		map = new HashMap<String, String>();
	}
	
	public void put(String key, Object value)
	{
		map.put(key, value.toString());
	}
	
	public String get(String key)
	{
		if(!map.containsKey(key))
			throw new RuntimeException("Property: " + key + " must be defined");
		
		return map.get(key);
	}
}
