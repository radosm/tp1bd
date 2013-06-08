package ubadb.core.common;

/**
 *	Immutable object that represents a transaction identifier 
 */
public class TransactionId
{
	private long number;

	public TransactionId(long number)
	{
		this.number = number;
	}

	public long getNumber()
	{
		return number;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (number ^ (number >>> 32));
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
		TransactionId other = (TransactionId) obj;
		if (number != other.number)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return String.valueOf(number);
	}
}
