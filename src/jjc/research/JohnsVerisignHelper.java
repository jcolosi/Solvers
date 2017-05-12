package jjc.research;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

public class JohnsVerisignHelper implements VerisignHelper {

	TreeMap<Long, Record> byTime;
	HashMap<String, Integer> byIp;

	public JohnsVerisignHelper() {
		byTime = new TreeMap<Long, Record>();
		byIp = new HashMap<String, Integer>();
	}

	/**
	 * Load records into the Helper class for later processing.<br>
	 * Input data should be in the form IP.Domain.Timestamp<br>
	 * If data is in the wrong format, skip that line and continue to process.
	 * 
	 * @param lines
	 *            The String objects to be parsed and loaded.
	 */
	public void loadRecords(String[] lines) {
		String[] tokens;
		for (String line : lines) {
			tokens = line.split(",");
			if (tokens.length != 3) continue;
			Record current = new Record(tokens[0], tokens[1], tokens[2]);
			byTime.put(current.timestamp, current);
			if (byIp.containsKey(current.ip)) {
				byIp.put(current.ip, byIp.get(current.ip) + 1);
			} else {
				byIp.put(current.ip, 1);
			}
		}
	}

	/**
	 * Provide a listing of records sorted by timestamp.
	 * 
	 * @return A sorted set of records.
	 */
	public Collection<Record> getRecordsByTimestamp() {
		return byTime.values();
	}

	/**
	 * Provide a listing of all IPs sorted by the number of times the IP appears
	 * in the data. Sort the list in decreasing order.
	 * 
	 * @return A map of IP to appearance count.
	 */
	public Collection<IpCount> getRecordsByIpCount() {
		ArrayList<IpCount> counts = new ArrayList<IpCount>();
		for (String ip : byIp.keySet()) {
			counts.add(new IpCount(ip, byIp.get(ip)));
		}
		Collections.sort(counts);
		return counts;
	}

}

class Record extends AbstractRecord {
	static private final SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

	public Record(String ip, String domain, long timestamp) {
		this.ip = ip;
		this.domain = domain;
		this.timestamp = timestamp;
	}

	public Record(String ip, String domain, String timestamp) {
		this(ip, domain, Long.parseLong(timestamp));
	}

	public String toString() {
		return String.format("%s, %s, %s", ip, domain, df.format(new Date(timestamp * 1000)));
	}
}

class IpCount extends AbstractIpCount implements Comparable<IpCount> {

	public IpCount(String ip, int count) {
		this.ip = ip;
		this.count = count;
	}

	@Override
	public int compareTo(IpCount other) {
		return other.count - this.count;
	}

	public String toString() {
		return String.format("%s [%d]", ip, count);
	}
}
