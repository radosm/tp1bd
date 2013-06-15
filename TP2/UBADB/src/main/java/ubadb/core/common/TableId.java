package ubadb.core.common;

/**
 *	Immutable object that represents a table identifier 
 */
public class TableId
{
	private String internalName;	//internal name, not necessarily the same one defined by the user

	public TableId() {}
	
	public TableId(String internalName) 
	{
		this.internalName = internalName;
	}

	public String getInternalName()
	{
		return internalName;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((internalName == null) ? 0 : internalName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableId other = (TableId) obj;
		if (internalName == null) {
			if (other.internalName != null)
				return false;
		} else if (!internalName.equals(other.internalName))
			return false;
		return true;
	}
}
