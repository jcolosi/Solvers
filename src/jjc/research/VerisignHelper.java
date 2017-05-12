package jjc.research;

import java.util.Collection;

public interface VerisignHelper {

	/**
	 * Load records into the Helper class for later processing.<br>
	 * Input data should be in the form IP.Domain.Timestamp<br>
	 * If data is in the wrong format, skip that line and continue to process.
	 * 
	 * @param lines
	 *            The String objects to be parsed and loaded.
	 */
	public void loadRecords(String[] lines);

	/**
	 * Provide a listing of records sorted by timestamp.
	 * 
	 * @return A sorted set of records.
	 */
	public Collection<? extends AbstractRecord> getRecordsByTimestamp();

	/**
	 * Provide a listing of all IPs sorted by the number of times the IP appears
	 * in the data. Sort the list in decreasing order.
	 * 
	 * @return A map of IP to appearance count.
	 */
	public Collection<? extends AbstractIpCount> getRecordsByIpCount();
}

abstract class AbstractRecord {
	public String ip;
	public String domain;
	public long timestamp;
}

abstract class AbstractIpCount {
	public String ip;
	public int count;
}